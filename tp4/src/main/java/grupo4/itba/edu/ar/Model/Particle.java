package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Particle {
    private final double currentPosition;
    private List<Double> positionsHistory;

    public Particle(double currentPosition) {
        this.currentPosition = currentPosition;
        this.positionsHistory = new LinkedList<>();
        this.positionsHistory.add(currentPosition);
    }
}
