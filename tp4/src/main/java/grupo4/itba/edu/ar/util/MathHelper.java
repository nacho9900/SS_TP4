package grupo4.itba.edu.ar.util;

import java.util.Random;

public class MathHelper
{
    public static double randBetween( Random r, double low, double high ) {
        return ( r.nextDouble() * ( high - low ) ) + low;
    }
}
