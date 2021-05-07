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
    }

    public void nextStep() {        
        for (Particle crystalParticle : crystal) {
            interactWith(crystalParticle);
        }

        particlePositions.add(particle.getPos());
    }

    private void interactWith(Particle crystalParticle) {
        // This method uses Verlet integration

        time += dT;
        double k = Math.pow(10, 10); // units: N*m^2/C^2
        double Q = Math.pow(10, -19); // units: C
        
        particle.setAcc(new Vector2(1, 1));  // TODO: replace with coulomb
        // acceleration = -(springConstant/mass) * particle.getX(); // TODO: replace spring force with Coulomb force
        
        if( firstInteraction ){
            particle.setVel( Vector2.add(particle.getVel(), Vector2.dot(particle.getAcc(), dT / particle.getM())) );
            particle.setPos( Vector2.add(particle.getPos(), Vector2.add(Vector2.dot(particle.getVel(), dT), Vector2.dot( particle.getAcc(), Math.pow(dT,2) / (2*particle.getM())))) );
            firstInteraction = false;
        }
        else {
            particle.setPos( Vector2.add(Vector2.sub(Vector2.dot(particle.getPos(), 2), prevPos), Vector2.dot(particle.getAcc(), Math.pow(dT,2) / particle.getM())) );
        }
        prevPos = particle.getPos();
    }

    public boolean isDone() {
        // TODO: implement exit condition
        return false;
    }

    public void saveMovement() {
        String dumpFilename = "crystal";

        dumpFilename = dumpFilename.replace( ".", "" );

        File dump = new File( dumpFilename + ".csv" );


        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
            StringBuilder builder = new StringBuilder();

            for ( Vector2 pos : particlePositions ) {
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
