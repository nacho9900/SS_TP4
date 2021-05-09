package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Oscilator.Oscilator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App
{
    public static void main( String[] args ) {
        // Oscilator.OscilatorMethods.oscillatorAnalitic();
        // Oscilator.OscilatorMethods.oscilatorVerlet();
        // Oscilator.OscilatorMethods.oscilatorBeeman();

        Vector2 v = new Vector2( 10e3, 100e3 );
        double mass = 1e-27;
        double D = 1e-8;

        // Variable conditions
        double dT = 1e-13;
        int seed = 6432121;
        //        ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dT, seed );
        //
        //        EndState state = particlePropagation.run();
        //        System.out.println( state );

        //For 2.2 - Energy trough time
        App.energyThroughDifferentDts();

        // For 2.3 - Calculates length for each dT
        // List<Double> lengths = particlePropagation.calculatePathLength();
        // System.out.println("Particle path length: " + lengths.stream().reduce(0d, (x, y) -> x + y));
    }

    //2.2
    private static void energyThroughDifferentDts() {
        final List<Double> dTs = Arrays.asList( 5e-14, 1e-14, 5e-15 );
        final int experimentsCount = 10;
        final Vector2 v = new Vector2( 10e3, 100e3 );
        double mass = 1e-27;
        double D = 1e-8;

        Random random = new Random();

        for ( double dt : dTs ) {
            final Map<Integer, Values> results = new HashMap<>();

            for ( int i = 0; i < experimentsCount; i++ ) {
                if ( i % 10 == 0 ) {
                    System.out.println( i );
                }

                ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dt, random.nextInt() );
                particlePropagation.run(false);
                int index = 0;
                for ( double deltaEnergy : particlePropagation.getDeltaEnergyThroughTime() ) {
                    Values values;
                    if ( !results.containsKey( index ) ) {
                        values = new Values();
                        results.put( index, values );
                    }
                    else {
                        values = results.get( index );
                    }

                    values.getValues().add( deltaEnergy );

                    index++;
                }
            }

            App.generateEnergyThoughtTimeCsv( dt, results );
        }
    }

    private static void generateEnergyThoughtTimeCsv( double dt, Map<Integer, Values> result ) {
        String csvFileName = String.format( "energy_%s", Double.toString( dt ).replace( ".", "" ) );

        File csvFile = new File( csvFileName + ".csv" );

        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( csvFile ) ) ) {
            StringBuilder builder = new StringBuilder();

            double time = dt;
            int timeIndex = 1;
            for ( Values values : result.values() ) {
                final double mean = values.getMean();
                final double error = Values.getStandardError( values, mean );

                if ( error == 0 ) {
                    break;
                }

                builder.append( time )
                       .append( "," )
                       .append( mean )
                       .append( "," )
                       .append( error )
                       .append( System.lineSeparator() );

                timeIndex++;
                time = timeIndex * dt;
            }

            writer.write( builder.toString() );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
