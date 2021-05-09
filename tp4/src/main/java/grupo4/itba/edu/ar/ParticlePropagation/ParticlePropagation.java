package grupo4.itba.edu.ar.ParticlePropagation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import grupo4.itba.edu.ar.Model.Particle;
import grupo4.itba.edu.ar.Model.Vector2;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticlePropagation
{
    private List<Particle> crystal;
    private Particle particle;
    private Vector2 prevPos;
    private double D; //distance between particles in crystal
    private double L; //crystal width/height
    private double dT; // delta time, smallest time unit
    private double time;
    private List<Vector2> particlePositions;
    private boolean firstIteration = true;
    private final double Q = 1e-19;
    private final double k = 1e10;
    private boolean isDone = false;

    public ParticlePropagation( double D, Vector2 v, double mass, double dT, int seed ) {
        int N = 16; // amount of particles per row/column
        this.D = D;
        this.L = D * ( N - 1 );
        this.dT = dT;
        this.time = 0;
        this.particlePositions = new LinkedList<>();
        // TODO: verify that a negative x is not an issue
        Random r = new Random(seed);
        particle = new Particle( new Vector2( -D, r.nextDouble() * 2 * D + L/2-D ), v, mass, true );

        crystal = new LinkedList<>();
        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < N; j++ ) {
                crystal.add(
                        new Particle( new Vector2( i * D, j * D ), new Vector2( 0, 0 ), mass, ( i + j ) % 2 == 0 ) );
            }
        }
        particlePositions.add( particle.getPos() );
    }

    public void run() {
        boolean isDone = false;
        while( !isDone ) {
            isDone = nextStep();
        }

        saveMovement();
    }

    private boolean nextStep() {
        time += dT;
        Vector2 force = getForce();
        moveParticle( force );

        if ( isDone() ) {
            return true;
        } else {
            particlePositions.add( particle.getPos() );
            return false;
        }
    }

    private Vector2 getForce() {
        Vector2 force = new Vector2( 0, 0 );
        for ( Particle crystalParticle : crystal ) {
            force.add( getCrystalForce( crystalParticle ) );
        }
        force = Vector2.dot( force, this.Q * this.k );
        return force;
    }

    private Vector2 getCrystalForce( Particle crystalParticle ) {
        Vector2 distance = Vector2.sub( particle.getPos(), crystalParticle.getPos() );
        if (Vector2.abs(distance) < this.D * 0.01) { 
            this.isDone = true;
        }
        Vector2 unitVector = Vector2.div( distance, Vector2.abs( distance ) );
        Vector2 force = Vector2.dot( unitVector, ( this.Q * crystalParticle.getChargeSign() ) /
                                                 Math.pow( Vector2.abs( distance ), 2 ) );
        // acceleration = force * particle.getX() / -mass;
        return force;
    }

    private void moveParticle( Vector2 force ) {
        // System.out.println( particle.getPos() );
        // This method uses Verlet integration
        Vector2 acceleration = Vector2.div( force, particle.getM() ); // a = (F / m)
        particle.setAcc( acceleration );

        Vector2 velocity = particle.getVel();
        Vector2 position = particle.getPos();

        if ( firstIteration ) {
            particle.setVel( Vector2.add( velocity, Vector2.dot( particle.getAcc(), dT ) ) );
            // v1 = v0 + a * dt
            particle.setPos( Vector2.add( position, Vector2.add( Vector2.dot( particle.getVel(), dT ),
                                                                 Vector2.dot( particle.getAcc(),
                                                                              Math.pow( dT, 2 ) / 2 ) ) ) );
            // ri =  ri-1 + vi.dt + a. (dt^2 / 2)
            firstIteration = false;
        }
        else {
            particle.setVel( Vector2.add( particle.getVel(), Vector2.dot( particle.getAcc(), dT ) ) );
            // vi+1 = vi + a * dt
            particle.setPos( Vector2.add( Vector2.sub( Vector2.dot( particle.getPos(), 2 ), prevPos ),
                                          Vector2.dot( particle.getAcc(), Math.pow( dT, 2 ) ) ) );

        }

        prevPos = particle.getPos();
    }

    private boolean isDone() {
        // DEBUG
        if (this.isDone || outOfBounds()) {
            System.out.println("Exit condition:\n- Got too close to a crystal particle: " + this.isDone + "\n- Got out of bounds: "+ outOfBounds());
        }

        return this.isDone || outOfBounds();
    }

    private boolean outOfBounds() {
        Vector2 pos = this.particle.getPos();
        return pos.getX() < -D || pos.getX() > L || pos.getY() < 0 || pos.getY() > L;
    }

    private void saveMovement() {
        String dumpFilename = "crystal";

        dumpFilename = dumpFilename.replace( ".", "" );

        File dump = new File( dumpFilename + ".xyz" );

        StringBuilder crystalStringBuilder = new StringBuilder();
        String redString = " 1 0 0";
        String blueString = " 0 0 1";
        String whiteString = " 0 1 0";

        for ( Particle crystalParticle : crystal ) {
            crystalStringBuilder.append( crystalParticle.getPos().getX() )
                                .append( " " )
                                .append( crystalParticle.getPos().getY() )
                                .append( " " )
                                .append( "1.0E-9" )
                                .append( crystalParticle.getChargeSign() > 0 ? redString : blueString )
                                .append( System.lineSeparator() );
        }

        String crystalString = crystalStringBuilder.toString();

        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
            StringBuilder builder = new StringBuilder();

            for ( Vector2 pos : particlePositions ) {
                builder.append( crystal.size() + 1 ).append( System.lineSeparator() ).append( System.lineSeparator() );
                builder.append( crystalString );
                builder.append( pos.getX() )
                       .append( " " )
                       .append( pos.getY() )
                       .append( " " )
                       .append( "1.0E-9" )
                       .append( whiteString )
                       .append( System.lineSeparator() );
            }

            writer.write( builder.toString() );
            writer.flush();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
