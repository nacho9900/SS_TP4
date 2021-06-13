package grupo4.itba.edu.ar.ParticlePropagation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import grupo4.itba.edu.ar.Model.EndState;
import grupo4.itba.edu.ar.Model.Particle;
import grupo4.itba.edu.ar.Model.Vector;
import grupo4.itba.edu.ar.util.MathHelper;
import lombok.Getter;

public class ParticlePropagation
{
    private final List<Particle> crystal;
    private Particle particle;
    private final double D; //distance between particles in crystal
    private final double L; //crystal width/height
    private final double dT; // delta time, smallest time unit
    private double time;
    private final List<Vector> particlePositions;
    private boolean firstIteration = true;
    private final double Q = 1e-19;
    private final double k = 1e10;
    private final int seed;
    private boolean collidedInside = false;
    @Getter
    private final List<Double> deltaEnergyThroughTime;
    private final double startingTotalEnergy;

    public ParticlePropagation( double D, Vector v, double mass, double dT, int seed ) {
        int N = 16; // amount of particles per row/column
        this.D = D;
        this.L = D * ( N - 1 );
        this.dT = dT;
        this.time = 0;
        this.seed = seed;
        this.particlePositions = new LinkedList<>();
        Random r = new Random( seed );
        double upperBoundary = ( L / 2 ) + D;
        double lowerBoundary = ( L / 2 ) - D;
        double y = MathHelper.randBetween( r, lowerBoundary, upperBoundary );
        particle = new Particle( new Vector( -D, y ), v, mass, this.Q );

        crystal = new LinkedList<>();
        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < N; j++ ) {
                boolean isPositiveCharge = ( i + j ) % 2 == 0;
                crystal.add( new Particle( new Vector( i * D, j * D ), new Vector( 0, 0 ), mass,
                                           isPositiveCharge ? this.Q : -1 * this.Q ) );
            }
        }
        particlePositions.add( particle.getPosition() );
        deltaEnergyThroughTime = new ArrayList<>();
        this.startingTotalEnergy = particle.getTotalEnergy( crystal );
    }

    public ParticlePropagation( double D, Vector v, double mass, double dT, int seed, double relativePos ) {
        this( D, v, mass, dT, seed );

        // Replace particle position to be in the range of [L/2-D, L/2+D] (according to the percentage `relativePos`)
        particle = new Particle( new Vector( -D, relativePos * 2 * D + L / 2 - D ), v, mass, this.Q );
        particlePositions.set( 0, particle.getPosition() );
    }

    public EndState run( boolean saveMovement ) {
        EndState state = EndState.NOT_DONE;
        while ( state == EndState.NOT_DONE ) {
            state = nextStep();
        }

        if ( saveMovement ) {
            saveMovement();
        }
        return state;
    }

    private EndState nextStep() {
        this.time += this.dT;

        Vector force = this.getForce();
        this.moveParticle( force );

        EndState state = this.getCurrentState();
        if ( state == EndState.NOT_DONE ) {
            this.particlePositions.add( this.particle.getPosition() );
        }

        this.deltaEnergyThroughTime.add(
                Math.abs( this.startingTotalEnergy - this.particle.getTotalEnergy( this.crystal ) ) );
        return state;
    }

    private Vector getForce() {
        Vector force = new Vector( 0, 0 );
        for ( Particle crystalParticle : crystal ) {
            force.add( getCrystalForce( crystalParticle ) );
        }
        force = Vector.dot( force, this.Q * this.k );
        return force;
    }

    private Vector getCrystalForce( Particle crystalParticle ) {
        Vector distance = Vector.sub( particle.getPosition(), crystalParticle.getPosition() );
        if ( Vector.abs( distance ) < this.D * 0.01 ) {
            this.collidedInside = true;
        }
        Vector unitVector = Vector.div( distance, Vector.abs( distance ) );
        Vector force = Vector.dot( unitVector,
                                   ( crystalParticle.getCharge() ) / Math.pow( Vector.abs( distance ), 2 ) );
        // acceleration = force * particle.getX() / -mass;
        return force;
    }

    private void moveParticle( Vector force ) {
        // This method uses Verlet integration
        Vector acceleration = Vector.div( force, particle.getM() ); // a = (F / m)
        this.particle.setAcceleration( acceleration );

        if ( this.firstIteration ) {
            Vector velocity = Vector.add( particle.getVelocity(), Vector.dot( acceleration, dT ) );
            this.particle.setVelocity( velocity );
            // v1 = v0 + a * dt
            Vector displacedDistance = Vector.add( Vector.dot( velocity, dT ),
                                                   Vector.dot( acceleration, Math.pow( this.dT, 2 ) / 2.0 ) );
            Vector position = Vector.add( particle.getPosition(), displacedDistance );
            particle.setPosition( position );
            // ri =  ri-1 + vi.dt + a. (dt^2 / 2)
            this.firstIteration = false;
        }
        else {
            Vector previousPosition = particlePositions.get( particlePositions.size() - 2 );
            Vector position = Vector.add( Vector.sub( Vector.dot( particle.getPosition(), 2 ), previousPosition ),
                                          Vector.dot( acceleration, Math.pow( this.dT, 2 ) ) );
            particle.setPosition( position );
            Vector velocity = Vector.dot( Vector.sub( position, previousPosition ), 1 / ( 2 * this.dT ) );
            particle.setVelocity( velocity );
        }
    }

    private EndState getCurrentState() {
        // DEBUG
        if ( this.collidedInside ) {
            return EndState.INSIDE;
        }

        return outOfBounds();
    }

    private EndState outOfBounds() {
        Vector pos = this.particle.getPosition();
        if ( pos.getX() < -2 * D ) {
            return EndState.LEFT_WALL;
        }
        else if ( pos.getX() > L ) {
            return EndState.RIGHT_WALL;
        }
        else if ( pos.getY() < 0 ) {
            return EndState.LOWER_WALL;
        }
        else if ( pos.getY() > L ) {
            return EndState.UPPER_WALL;
        }
        return EndState.NOT_DONE;
    }

    public List<Double> calculatePathLength() {
        Vector prevPos = null;
        double totalLength = 0.0;
        List<Double> lengths = new LinkedList<>();
        for ( Vector pos : particlePositions ) {
            if ( prevPos == null ) {
                prevPos = pos;
                continue;
            }

            Double len = Vector.abs( Vector.sub( pos, prevPos ) );
            
            // Acumulada
            totalLength += len;
            lengths.add( totalLength );
            
            // Simple
            // lengths.add( len );
        }

        return lengths;
    }

    private void saveMovement() {
        String dumpFilename = String.format( "crystal_%d", this.seed );

        File dump = new File( dumpFilename + ".xyz" );

        StringBuilder crystalStringBuilder = new StringBuilder();
        String redString = " 1 0 0";
        String blueString = " 0 0 1";
        String greenString = " 0 1 0";

        for ( Particle crystalParticle : crystal ) {
            crystalStringBuilder.append( crystalParticle.getPosition()
                                                        .getX() )
                                .append( " " )
                                .append( crystalParticle.getPosition()
                                                        .getY() )
                                .append( " " )
                                .append( "1.0E-9" )
                                .append( crystalParticle.getCharge() > 0 ? redString : blueString )
                                .append( System.lineSeparator() );
        }

        String crystalString = crystalStringBuilder.toString();

        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
            StringBuilder builder = new StringBuilder();

            for ( Vector pos : particlePositions ) {
                builder.setLength( 0 );
                builder.append( crystal.size() + 1 )
                       .append( System.lineSeparator() )
                       .append( System.lineSeparator() );
                builder.append( crystalString );
                builder.append( pos.getX() )
                       .append( " " )
                       .append( pos.getY() )
                       .append( " " )
                       .append( "1.0E-9" )
                       .append( greenString )
                       .append( System.lineSeparator() );

                writer.write( builder.toString() );
                writer.flush();
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
