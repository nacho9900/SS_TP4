package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Particle {
    private Vector2 pos;
    private Vector2 vel;
    private Vector2 acc;
    private double m;
    private boolean chargeIsPositive;
    // private List<Double> positionsHistory;

    public Particle(Vector2 pos, Vector2 vel, double m, boolean chargeIsPositive) {
        this.pos = pos;
        this.vel = vel;
        this.acc = new Vector2(0, 0);
        this.m = m;
        this.chargeIsPositive = chargeIsPositive;
        // this.positionsHistory = new LinkedList<>();
        // this.positionsHistory.add(currentPosition);
    }
}
