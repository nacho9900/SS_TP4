package grupo4.itba.edu.ar.util;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Values
{
    @Getter
    private final List<Double> values;

    public Values() {
        values = new LinkedList<>();
    }

    public double getMean() {
        return this.values.stream().reduce( 0.0, Double::sum ) / this.values.size();
    }

    public static double getStandardError( Values values, double mean ) {
        if(values.values.size() == 1) {
            return 0;
        }

        double sum = values.values.stream().reduce( 0.0, ( a, b ) -> a + Math.pow( b - mean, 2 ) );
        double standardDeviation = Math.sqrt( sum / ( values.values.size() - 1 ) );
        return standardDeviation / Math.sqrt( values.values.size() );
    }
}
