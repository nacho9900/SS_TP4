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
    List<Particle> crystal;
    Particle particle;
    double D; //distance between particles in crystal
    double L; //crystal width/height
    double dT; // delta time, smallest time unit
    double time;
    List<Vector2> particlePositions;
    
    public ParticlePropagation(double D, double vx, double vy, double mass, double dT) {
        int N = 16; // amount of particles per row/column
        this.D = D;
        this.L = D*N;
        this.dT = dT;
        this.time = 0;
        this.particlePositions = new LinkedList<>();
        particle = new Particle(new Vector2(-D, 7.5 * D), new Vector2(vx, vy), mass, true);

        for (int i = 0; i < N; i++ ) {
            for (int j = 0; j < N; j++ ) {
                crystal.add(new Particle(new Vector2(i * D, j * D), new Vector2(0, 0), mass, (i + j) % 2 == 0));
            }
        }
    }

    public void nextStep() {        
        // Verlet integration
        for (Particle crystalParticle : crystal) {
            interactWith(crystalParticle);
        }

        particlePositions.add(particle.getPos());
    }

    private void interactWith(Particle crystalParticle) {
        // TODO: simulate particle interaction with the crystal using some integration method (crystal is unaffected)

        time += dT;
        
        // aceleration = -(springConstant/mass) * particle.getX(); // TODO: replace spring force with Coulomb force
        // particle.getX();
        // if(i==0){
        //     nextVelocity = initialVelocity + aceleration * dT / mass;
        //     nextPosition = initialPosition + nextVelocity * dT + Math.pow(dT,2) * aceleration / (2*mass);
        // }
        // else {
        //     nextPosition = 2 * positions.get(i) - positions.get(i - 1) + aceleration * Math.pow(dT,2) / mass;
        // }
    }

    public boolean isDone() {
        // TODO: implement exit condition
        return false;
    }

    public void saveMovement() {
        String dumpFilename = "oscillator";

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
