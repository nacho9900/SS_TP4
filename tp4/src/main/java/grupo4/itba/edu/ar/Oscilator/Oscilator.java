package grupo4.itba.edu.ar.Oscilator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Oscilator {

    public static class OscilatorMethods{

        private static double mass = 70f; // kg
        private static double k = 10000f; // constant N/m
        private static double gamma = 100f; // kg/s
        private static double tf = 5f; // s
        private static double A = 2f; // TODO: cambiar. no se de donde sale esto
        private static double deltaT = 0.00001f;
        private static double springConstant = 1;

        public static void oscillatorAnalitic() {
            LinkedList<Double> rs = new LinkedList<>();
            LinkedList<Double> vs = new LinkedList<>();
            double r = 1f; // m
            double v = -A*gamma / (2f*mass); // m/s

            // Analitic solution
            for ( int i = 0; i < 1000000; i++ ) {
                double t = i * deltaT;
                r = Math.pow(A, -gamma*t/(2f*mass)) * Math.cos(Math.pow(k/mass - gamma*gamma/(4*mass*mass), 0.5) * t);
                rs.add(r);
            }
            saveOscillatorMovement(rs);
        }

        public static void oscilatorVerlet(){
            LinkedList<Double> positions = new LinkedList<>();

            double initialPosition = 1;
            positions.add(initialPosition);

            double initialVelocity = 0;

            double nextPosition;
            double nextVelocity;
            double aceleration;

            for(int i = 0; i < 1000000; i++ ){

                aceleration = -(springConstant/mass) * positions.get(i);

                if(i==0){
                    nextVelocity = initialVelocity + aceleration * deltaT / mass;
                    nextPosition = initialPosition + nextVelocity * deltaT + Math.pow(deltaT,2) * aceleration / (2*mass);
                }
                else
                    nextPosition = 2 * positions.get(i) - positions.get(i - 1) + aceleration * Math.pow(deltaT,2) / mass;

                positions.add(nextPosition);
            }

            saveOscillatorMovement(positions);
        }


        public static void oscilatorBeeman(){
            LinkedList<Double> positions = new LinkedList<>();
            LinkedList<Double> velocities = new LinkedList<>();
            LinkedList<Double> acelerations = new LinkedList<>();


            double initialPosition = 1;
            positions.add(initialPosition);

            double initialAceleration = -(springConstant/mass) * positions.get(0);
            acelerations.add(initialAceleration);

            double initialVelocity = acelerations.get(0) * deltaT;
            velocities.add(initialVelocity);

            double nextPosition = initialPosition + initialVelocity * deltaT;
            positions.add(nextPosition);
            double nextVelocity;
            double nextAceleration = -(springConstant/mass) * positions.get(1);
            acelerations.add(nextAceleration);

            for(int i = 1; i < 1000000; i++ ){

                nextPosition = positions.get(i) + velocities.get(i) * deltaT + (2/3) * acelerations.get(i) * Math.pow(deltaT,2) - (1/6) * acelerations.get(i - 1) * Math.pow(deltaT,2);
                nextAceleration = -(springConstant/mass) * nextPosition;
                nextVelocity = velocities.get(0) + (1/3) * nextAceleration * deltaT + (5/6) * acelerations.get(i) * deltaT - (1/6) * acelerations.get(i - 1) * Math.pow(deltaT,2);
                positions.add(nextPosition);
                velocities.add(nextVelocity);
                acelerations.add(nextAceleration);
            }

            saveOscillatorMovement(positions);
        }

        private static void saveOscillatorMovement(List<Double> rs) {
            String dumpFilename = "oscillator";

            dumpFilename = dumpFilename.replace( ".", "" );

            File dump = new File( dumpFilename + ".csv" );


            try ( BufferedWriter writer = new BufferedWriter( new FileWriter( dump ) ) ) {
                StringBuilder builder = new StringBuilder();

                for ( double r : rs ) {
                    builder.append( r )
                            .append( System.lineSeparator() );
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
