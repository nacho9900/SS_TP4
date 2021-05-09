package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Particle
{
    @Setter
    private Vector2 pos;
    @Setter
    private Vector2 vel;
    @Setter
    private Vector2 acc;
    private final double m;
    private final double charge;
    private static final double k = 1e10;

    public Particle( Vector2 pos, Vector2 vel, double m, double charge ) {
        this.pos = pos;
        this.vel = vel;
        this.acc = new Vector2( 0, 0 );
        this.m = m;
        this.charge = charge;
    }

    public double getKineticEnergy() {
        return 0.5 * this.m * Math.pow( Vector2.abs( this.vel ), 2 );
    }

    public double getElectrostaticPotentialEnergy( List<Particle> particles ) {
        double energy = 0;
        for(Particle particle : particles) {
            double distance = Vector2.abs( Vector2.sub( this.getPos(), particle.getPos() ) );
            energy += particle.charge / distance;
        }
        energy *= this.charge * Particle.k;
        return energy;
    }

    public double getTotalEnergy(List<Particle> particles) {
        return this.getKineticEnergy() + this.getElectrostaticPotentialEnergy( particles );
    }

    public int getChargeSign() {
        return this.charge > 0 ? 1 : -1;
    }
}
