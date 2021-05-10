package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Model.EndState;
import grupo4.itba.edu.ar.Model.Vector2;
import grupo4.itba.edu.ar.ParticlePropagation.ParticlePropagation;
import grupo4.itba.edu.ar.util.Values;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Comparator;
import java.util.LinkedList;
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

        // defaultRun(mass, D, dT, seed, v);
        energyThroughDifferentDts();
        // particleLengthsByVelocity(mass, D, dT, seed, v);
    }

    private static void defaultRun(double mass, double D, double dT, int seed, Vector2 v) {
        ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dT, seed );
        EndState state = particlePropagation.run(true);
        System.out.println(state);
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

    //2.3
    private static void particleLengthsByVelocity(double mass, double D, double dT, int seed, Vector2 normalVelocity) {
        // Variable conditions
        int samplePoints = 100; // amount of point equally distributed between L/2-D and L/2+D
        double[] velocityModulos = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        normalVelocity = Vector2.div(normalVelocity, Vector2.abs(normalVelocity));  // normalization
        for (double vM : velocityModulos) {
            List<List<Double>> listOfLengths = new LinkedList<>();
            Vector2 vel = Vector2.dot(normalVelocity, vM);
            for (int i = 0; i < samplePoints; i++) {
                double relativePos = i/samplePoints;
                ParticlePropagation pp = new ParticlePropagation(D, vel, mass, dT, seed, relativePos);
                pp.run(false);
                List<Double> lengths = pp.calculatePathLength();
                listOfLengths.add(lengths);
            }
            
            List<Double> avgLengths = new LinkedList<>();
            List<Double> stdLengths = new LinkedList<>();
            int maxSize = listOfLengths.stream().map(lengths -> lengths.size()).max(Comparator.naturalOrder()).get();
            for (int i = 0; i < maxSize; i++) {
                List<Double> valuesAtI = new LinkedList<>();
                for (List<Double> lengths : listOfLengths) {
                    if (lengths.size() >= i) {
                        valuesAtI.add(lengths.get(i));
                    }
                }
                Double avg = valuesAtI.stream().reduce((x, y) -> x + y).get() / valuesAtI.size();
                Double std = Math.sqrt(valuesAtI.stream()
                                                .map( v -> Math.pow(v-avg, 2) )
                                                .reduce((v1, v2) -> v1 + v2).get() / valuesAtI.size());
                avgLengths.add(avg);
                stdLengths.add(std);
            }

            // System.out.println(avgLengths);
            // System.out.println(stdLengths);

            // Output to file
            String dumpFilename = "ej2_3/vM_" + (int)vM;

            dumpFilename = dumpFilename.replace( ".", "" );
            File dump = new File( dumpFilename + ".csv" );

            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < avgLengths.size(); i++) {
                    builder.append( (double)i * dT )
                        .append( " " )
                        .append( avgLengths.get(i) )
                        .append( " " )
                        .append( stdLengths.get(i) )
                        .append( System.lineSeparator() );
                    i++;
                }
                writer.write( builder.toString() );
                writer.flush();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
