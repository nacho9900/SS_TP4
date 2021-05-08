package grupo4.itba.edu.ar.ParticlePropagation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import grupo4.itba.edu.ar.Model.Particle;
import grupo4.itba.edu.ar.Model.Vector2;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticlePropagation {
    private List<Particle> crystal;
    private Particle particle;
    private Vector2 prevPos;
    private double D; //distance between particles in crystal
    private double L; //crystal width/height
    private double dT; // delta time, smallest time unit
    private double time;
    private List<Vector2> particlePositions;
    private boolean firstInteraction = true;
    
    public ParticlePropagation(double D, Vector2 v, double mass, double dT) {
        int N = 16; // amount of particles per row/column
        this.D = D;
        this.L = D*(N-1);
        this.dT = dT;
        this.time = 0;
        this.particlePositions = new LinkedList<>();
        // TODO: verify that a negative x is not an issue
        particle = new Particle(new Vector2(-D, this.L / 2), v, mass, true);

        crystal = new LinkedList<>();
        for (int i = 0; i < N; i++ ) {
            for (int j = 0; j < N; j++ ) {
                crystal.add(new Particle(new Vector2(i * D, j * D), new Vector2(0, 0), mass, (i + j) % 2 == 0));
            }
        }
        particlePositions.add(particle.getPos());
    }

    public void nextStep() {        
        time += dT;

        Vector2 force = new Vector2(0, 0);
        for (Particle crystalParticle : crystal) {
            force = Vector2.add(force, getCrystalForce(crystalParticle));
        }
        System.out.println(force);
        
        moveParticle(force);
        particlePositions.add(particle.getPos());
    }

    private Vector2 getCrystalForce(Particle crystalParticle) {
        double k = Math.pow(10, 10); // units: N*m^2/C^2
        double Q = Math.pow(10, -19); // units: C
        
        Vector2 dist = Vector2.sub(crystalParticle.getPos(), particle.getPos());
        Vector2 unitDist = Vector2.div(dist, Vector2.len(dist));
        Vector2 force = Vector2.dot(Vector2.dot(Vector2.div(Vector2.pow(Vector2.abs(dist), 2), Q * crystalParticle.getChargeSign()), unitDist), k * Q);
        // acceleration = force * particle.getX() / -mass;
        return force;
    }

    private void moveParticle(Vector2 force) {
        // This method uses Verlet integration
        particle.setAcc(Vector2.div(force, particle.getM()));
        if( firstInteraction ){
            particle.setVel( Vector2.add(particle.getVel(), Vector2.dot(particle.getAcc(), dT)) );
            particle.setPos( Vector2.add(particle.getPos(), Vector2.add(Vector2.dot(particle.getVel(), dT), Vector2.dot( particle.getAcc(), Math.pow(dT,2) / 2))) );
            firstInteraction = false;
        }
        else {
            particle.setVel( Vector2.add(particle.getVel(), Vector2.dot(particle.getAcc(), dT)) );
            particle.setPos( Vector2.add(Vector2.sub(Vector2.dot(particle.getPos(), 2), prevPos), Vector2.dot(particle.getAcc(), Math.pow(dT,2))) );
        }
        prevPos = particle.getPos();
        System.out.println(prevPos);
        System.out.println();
    }

    public boolean isDone() {
        // TODO: implement exit condition
        return false;
    }

    public void saveMovement() {
        String dumpFilename = "crystal";

        dumpFilename = dumpFilename.replace( ".", "" );

        File dump = new File( dumpFilename + ".xyz" );


        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
            StringBuilder builder = new StringBuilder();

            for ( Vector2 pos : particlePositions ) {
                builder.append( crystal.size() + 1 )
                        .append( System.lineSeparator() )
                        .append( System.lineSeparator() );
                for ( Particle crystalParticle : crystal ) {
                    builder.append(crystalParticle.getPos().getX())
                    .append(" ")
                    .append(crystalParticle.getPos().getY())
                    .append( System.lineSeparator() );
                }
                builder.append( pos.getX() )
                        .append( " " )
                        .append( pos.getY() )
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
