package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector2
{
    private double x, y;

    public Vector2( double x, double y ) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add( Vector2 a, Vector2 b ) {
        return new Vector2( a.x + b.x, a.y + b.y );
    }

    public static Vector2 sub( Vector2 a, Vector2 b ) {
        return new Vector2( a.x - b.x, a.y - b.y );
    }

    public static Vector2 dot( Vector2 a, Vector2 b ) {
        return new Vector2( a.x * b.x, a.y * b.y );
    }

    public static Vector2 dot( Vector2 a, double b ) {
        return new Vector2( a.x * b, a.y * b );
    }

    public static Vector2 div( Vector2 a, Vector2 b ) {
        return new Vector2( a.x / b.x, a.y / b.y );
    }

    public static Vector2 div( Vector2 a, double b ) {
        return new Vector2( a.x / b, a.y / b );
    }

    public static double abs( Vector2 a ) {
        return Math.sqrt( Math.pow( a.x, 2 ) + Math.pow( a.y, 2 ) );
    }

    public static Vector2 pow( Vector2 a, double b ) {
        return new Vector2( Math.pow( a.x, b ), Math.pow( a.y, b ) );
    }

    public static Vector2 neg( Vector2 a ) {
        return new Vector2( -a.x, -a.y );
    }

    public void add(Vector2 otherVector) {
        this.x += otherVector.x;
        this.y += otherVector.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
