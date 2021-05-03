package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Particle {
    private final double positionX;
    private final double positionY;
    private final double radius;

    public Particle(double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
    }
}
