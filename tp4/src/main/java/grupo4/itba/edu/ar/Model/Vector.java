package grupo4.itba.edu.ar.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector
{
    private double x, y;

    public Vector( double x, double y ) {
        this.x = x;
        this.y = y;
    }

    public static Vector add( Vector a, Vector b ) {
        return new Vector( a.x + b.x, a.y + b.y );
    }

    public static Vector sub( Vector a, Vector b ) {
        return new Vector( a.x - b.x, a.y - b.y );
    }

    public static Vector dot( Vector a, Vector b ) {
        return new Vector( a.x * b.x, a.y * b.y );
    }

    public static Vector dot( Vector a, double b ) {
        return new Vector( a.x * b, a.y * b );
    }

    public static Vector div( Vector a, Vector b ) {
        return new Vector( a.x / b.x, a.y / b.y );
    }

    public static Vector div( Vector a, double b ) {
        return new Vector( a.x / b, a.y / b );
    }

    public static double abs( Vector a ) {
        return Math.sqrt( Math.pow( a.x, 2 ) + Math.pow( a.y, 2 ) );
    }

    public static Vector pow( Vector a, double b ) {
        return new Vector( Math.pow( a.x, b ), Math.pow( a.y, b ) );
    }

    public static Vector neg( Vector a ) {
        return new Vector( -a.x, -a.y );
    }

    public void add( Vector otherVector) {
        this.x += otherVector.x;
        this.y += otherVector.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
