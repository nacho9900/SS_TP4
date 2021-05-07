package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector2 {
    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 dot(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }

    public static Vector2 dot(Vector2 a, double b) {
        return new Vector2(a.x * b, a.y * b);
    }

    public static Vector2 div(Vector2 a, Vector2 b) {
        return new Vector2(a.x / b.x, a.y / b.y);
    }

    public static Vector2 div(Vector2 a, double b) {
        return new Vector2(a.x / b, a.y / b);
    }
}
