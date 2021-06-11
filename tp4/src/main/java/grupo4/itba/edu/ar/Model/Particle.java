package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Particle
{
    @Setter
    private Vector position;
    @Setter
    private Vector velocity;
    @Setter
    private Vector acceleration;
    private final double m;
    private final double charge;
    private static final double k = 1e10;

    public Particle( Vector position, Vector velocity, double m, double charge ) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new Vector( 0, 0 );
        this.m = m;
        this.charge = charge;
    }

    public double getKineticEnergy() {
        return 0.5 * this.m * Math.pow( Vector.abs( this.velocity ), 2 );
    }

    public double getElectrostaticPotentialEnergy( List<Particle> particles ) {
        double energy = 0;
        for(Particle particle : particles) {
            double distance = Vector.abs( Vector.sub( this.getPosition(), particle.getPosition() ) );
            energy += particle.charge / distance;
        }
        energy *= this.charge * Particle.k;
        return energy;
    }

    public double getTotalEnergy(List<Particle> particles) {
        return this.getKineticEnergy() + this.getElectrostaticPotentialEnergy( particles );
    }
}
