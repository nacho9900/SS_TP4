package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Model.EndState;
import grupo4.itba.edu.ar.Model.Vector;
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

        Vector v = new Vector( 5e3, 0 );
        double mass = 1e-27;
        double D = 1e-8;

        // Variable conditions
        double dT = 1e-17;
        int seed = 999;

        // defaultRun(mass, D, dT, seed, v);
        energyThroughDifferentDts();
        // particleLengthsByVelocity(mass, D, dT, seed);
        // endStatesByVelocity(mass, D, dT, seed);
    }

    private static void defaultRun( double mass, double D, double dT, int seed, Vector v ) {
        ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dT, seed );
        EndState state = particlePropagation.run( true );
        System.out.println( state );
    }

    //2.2
    private static void energyThroughDifferentDts() {
        final List<Double> dTs = Arrays.asList( 1e-15, 1e-16, 1e-17 );
        final double maxDt = dTs.stream()
                                .reduce( 0.0, ( a, b ) -> a > b ? a : b );
        final int experimentsCount = 10;
        final Vector velocity = new Vector( 10e3, 0 );
        double mass = 1e-27;
        double D = 1e-8;

        Random random = new Random();
        List<Integer> seeds = new LinkedList<>();
        for ( int i = 0; i < experimentsCount; i++ ) {
            seeds.add( random.nextInt() );
        }

        final Map<Double, Values> io = new HashMap<>();
        for ( double dt : dTs ) {
            io.put( dt, new Values() );
            final Map<Integer, Values> results = new HashMap<>();
            for ( int seed : seeds ) {
                System.out.printf( "{ seed: %d, dt: %s}%n", seed, dt );

                ParticlePropagation particlePropagation = new ParticlePropagation( D, velocity, mass, dt, seed );
                particlePropagation.run( false );
                int index = 1;
                int N = particlePropagation.getDeltaEnergyThroughTime()
                                           .size();
                double sum = 0;
                for ( double deltaEnergy : particlePropagation.getDeltaEnergyThroughTime() ) {
                    Values values;
                    if ( !results.containsKey( index ) ) {
                        values = new Values();
                        results.put( index, values );
                    }
                    else {
                        values = results.get( index );
                    }

                    values.getValues()
                          .add( deltaEnergy );

                    index++;
                    sum += deltaEnergy;
                }

                sum /= N;
                io.get( dt )
                  .getValues()
                  .add( sum );
            }

            App.generateEnergyThoughtTimeCsv( dt, maxDt, results );
            App.generateEnergyVsDtCsv( io );
        }
    }

    private static void generateEnergyVsDtCsv( Map<Double, Values> io ) {
        String csvFileName = "io_energy_vs_dt.csv";

        File csvFile = new File( csvFileName + ".csv" );

        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( csvFile ) ) ) {
            StringBuilder builder = new StringBuilder();
            for ( double dt : io.keySet() ) {
                double mean = io.get( dt )
                                .getMean();
                builder.append( dt )
                       .append( "," )
                       .append( mean )
                       .append( "," )
                       .append( Values.getStandardError( io.get( dt ), mean ) )
                       .append( System.lineSeparator() );
            }
            writer.write( builder.toString() );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private static void generateEnergyThoughtTimeCsv( double dt, double maxDt, Map<Integer, Values> result ) {
        String csvFileName = String.format( "energy_%s", Double.toString( dt )
                                                               .replace( ".", "" ) );

        File csvFile = new File( csvFileName + ".csv" );

        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( csvFile ) ) ) {
            StringBuilder builder = new StringBuilder();
            double ratio = maxDt / dt;
            int timeIndex = (int) ratio;
            int index = 1;
            while ( result.containsKey( timeIndex ) ) {
                final double mean = result.get( timeIndex )
                                          .getMean();
                final double error = Values.getStandardError( result.get( timeIndex ), mean );

                if ( error == 0 ) {
                    break;
                }

                builder.append( index * maxDt )
                       .append( "," )
                       .append( mean )
                       .append( "," )
                       .append( error )
                       .append( System.lineSeparator() );

                index++;
                timeIndex = (int) ( ratio * index );
            }

            writer.write( builder.toString() );
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    //2.3
    private static void particleLengthsByVelocity(double mass, double D, double dT, int seed) {
        // Variable conditions
        int samplePoints = 10; // amount of points equally distributed between L/2-D and L/2+D
        double[] velocityModulos = new double[]{100, 150, 200, 250, 300, 350, 400, 450, 500, 550};
        // double[] velocityModulos = new double[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
        
        Vector normalVelocity = new Vector( 1e+2 , 0 );
        // normalVelocity = Vector.div( normalVelocity, Vector.abs( normalVelocity));  // normalization
        for (double vM : velocityModulos) {
            List<List<Double>> listOfLengths = new LinkedList<>();
            Vector vel = Vector.dot( normalVelocity, vM );
            for ( int i = 0; i < samplePoints; i++ ) {
                System.out.printf( "{ vM: %f, dt: %s}%n", vM, dT );
                double relativePos = i / samplePoints;
                ParticlePropagation pp = new ParticlePropagation( D, vel, mass, dT, seed, relativePos );
                pp.run( false );
                List<Double> lengths = pp.calculatePathLength();
                listOfLengths.add( lengths );
            }

            List<Double> avgLengths = new LinkedList<>();
            List<Double> stdLengths = new LinkedList<>();
            int maxSize = listOfLengths.stream()
                                       .map( lengths -> lengths.size() )
                                       .max( Comparator.naturalOrder() )
                                       .get();
            for ( int i = 0; i < maxSize; i++ ) {
                List<Double> valuesAtI = new LinkedList<>();
                for ( List<Double> lengths : listOfLengths ) {
                    if ( lengths.size() >= i ) {
                        valuesAtI.add( lengths.get( i ) );
                    }
                }
                Double avg = valuesAtI.stream()
                                      .reduce( ( x, y ) -> x + y )
                                      .get() / valuesAtI.size();
                Double std = Math.sqrt( valuesAtI.stream()
                                                 .map( v -> Math.pow( v - avg, 2 ) )
                                                 .reduce( ( v1, v2 ) -> v1 + v2 )
                                                 .get() / valuesAtI.size() );
                avgLengths.add( avg );
                stdLengths.add( std );
            }

            // System.out.println(avgLengths);
            // System.out.println(stdLengths);

            // Output to file
            // String dumpFilename = "ej2_3/vM_" + (int)vM;
            String dumpFilename = "ej2_3/acum_vM_" + (int)vM;

            dumpFilename = dumpFilename.replace( ".", "" );
            File dump = new File( dumpFilename + ".csv" );

            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
                StringBuilder builder = new StringBuilder();
                for ( int i = 0; i < avgLengths.size(); i++ ) {
                    builder.append( (double) i * dT )
                           .append( " " )
                           .append( avgLengths.get( i ) )
                           .append( " " )
                           .append( stdLengths.get( i ) )
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

    //2.4
    private static void endStatesByVelocity(double mass, double D, double dT, int seed) {
        // Variable conditions
        int seedAmounts = 100;
        // double[] velocityModulos = new double[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
        double[] velocityModulos = new double[]{100, 150, 200, 250, 300, 350, 400, 450, 500, 550};
        
        Vector normalVelocity = new Vector( 1e+2 , 0 );
        // normalVelocity = Vector.div( normalVelocity, Vector.abs( normalVelocity));  // normalization
        for (double vM : velocityModulos) {
            List<EndState> endStates = new LinkedList<>();
            Vector vel = Vector.dot( normalVelocity, vM );
            System.out.printf( "{ seed: %d, dt: %s}%n", seed, dT );
            for ( int i = 0; i < seedAmounts; i++ ) {
                ParticlePropagation pp = new ParticlePropagation( D, vel, mass, dT, i );
                EndState endState = pp.run( false );
                // System.out.print(pp.calculatePathLength().stream().reduce(0.0, Double::sum) + ", ");
                endStates.add( endState );
            }

            int leftWall = 0;
            int rightWall = 0;
            int upperWall = 0;
            int lowerWall = 0;
            int inside = 0;
            for ( EndState endState : endStates ) {
                switch ( endState ) {
                    case LEFT_WALL:
                        leftWall++;
                        break;
                    case RIGHT_WALL:
                        rightWall++;
                        break;
                    case UPPER_WALL:
                        upperWall++;
                        break;
                    case LOWER_WALL:
                        lowerWall++;
                        break;
                    case INSIDE:
                        inside++;
                        break;
                    default:
                        break;
                }
            }
            // rightWall /= listOfEndStates.size();
            // leftWall /= listOfEndStates.size();
            // upperWall /= listOfEndStates.size();
            // lowerWall /= listOfEndStates.size();
            // inside /= listOfEndStates.size();
            System.out.println( rightWall + " " + leftWall + " " + upperWall + " " + lowerWall + " " + inside );

            // Output to file
            String dumpFilename = "ej2_4/vM_" + (int) vM;

            dumpFilename = dumpFilename.replace( ".", "" );
            File dump = new File( dumpFilename + ".csv" );

            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
                StringBuilder builder = new StringBuilder();
                builder.append( leftWall )
                       .append( System.lineSeparator() )
                       .append( rightWall )
                       .append( System.lineSeparator() )
                       .append( upperWall )
                       .append( System.lineSeparator() )
                       .append( lowerWall )
                       .append( System.lineSeparator() )
                       .append( inside );
                writer.write( builder.toString() );
                writer.flush();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
