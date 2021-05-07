package grupo4.itba.edu.ar.Oscilator;

import grupo4.itba.edu.ar.Model.Particle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Oscilator {

    public static class OscilatorMethods{

        private static double mass = 70f; // kg
        private static double k = 10000f; // constant N/m
        private static double gamma = 100f; // kg/s
        private static double tf = 5f; // s
        private static double A = 1f; // TODO: cambiar. no se de donde sale esto
        private static double deltaT = 0.05f;
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

            double initialVelocity = -A*gamma / (2f*mass);

            double nextPosition;
            double nextVelocity;
            double aceleration;

            for(int i = 0; i < 100; i++ ){

                aceleration = -(k/mass) * positions.get(i);

                if(i==0){
                    nextVelocity = initialVelocity + aceleration * deltaT;
                    nextPosition = (initialPosition + nextVelocity * deltaT + Math.pow(deltaT,2) * aceleration / (2));
                }
                else
                    nextPosition = 2 * positions.get(i) - positions.get(i - 1) + aceleration * Math.pow(deltaT,2);

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

            double initialAceleration = -(k/mass) * positions.get(0);
            acelerations.add(initialAceleration);

            double initialVelocity = -A*gamma / (2f*mass);
            velocities.add(initialVelocity);

            double nextPosition = initialPosition + initialVelocity * deltaT + 2/3 * initialAceleration * Math.pow(deltaT,2);
            positions.add(nextPosition);

            double nextAceleration = -(k/mass) * positions.get(1);
            acelerations.add(nextAceleration);

            double nextVelocity = velocities.get(0) + (1/3) * nextAceleration * deltaT + (5/6) * acelerations.get(0) * deltaT;
            velocities.add(nextVelocity);

            for(int i = 1; i < 98; i++ ){

                nextPosition = positions.get(i) + velocities.get(i) * deltaT + (2/3) * acelerations.get(i) * Math.pow(deltaT,2) - (1/6) * acelerations.get(i - 1) * Math.pow(deltaT,2);
                nextAceleration = -(k/mass) * nextPosition;
                nextVelocity = velocities.get(i) + (1/3) * nextAceleration * deltaT + (5/6) * acelerations.get(i) * deltaT - (1/6) * acelerations.get(i - 1) * deltaT;
                positions.add(nextPosition);
                velocities.add(nextVelocity);
                acelerations.add(nextAceleration);
            }

            saveOscillatorMovement(positions);
        }

        private static void gearPredictorCorrector(){

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



        private float factorial(int n){
            if(n == 0 || n == 1) return 1;
            return n * factorial(n -1);
        }

        private static double calculateForce(double x, double v){
            return -k * x - (gamma * v);
        }

        private static double calculateAcceleration(double x, double v, double m){
            return (-k * x - (gamma * v)) / m;
        }

        public static void predictEuler(){
            LinkedList<Double> positions = new LinkedList<>();

            double initialPosition = 1;
            double initialVelocity = -A*gamma / (2f*mass);
            double acceleration = (-k * initialPosition - (gamma * initialVelocity)) / mass;

            double nextVelocity= initialVelocity + (deltaT * acceleration);

            double nextPosition = initialPosition + deltaT * nextVelocity + Math.pow(deltaT,2) * acceleration / (2);

            positions.add(initialPosition);
            positions.add(nextPosition);

            for(int i = 1; i < 100; i++){
                acceleration = (-k * positions.get(i) - (gamma * nextVelocity)) / mass;
                nextVelocity += (deltaT * acceleration);
                nextPosition = 2 * positions.get(i) - positions.get(i - 1) + acceleration * Math.pow(deltaT,2);
                positions.add(nextPosition);
            }
            saveOscillatorMovement(positions);
        }

        public static void predictBeeman(){
            LinkedList<Double> positions = new LinkedList<>();
            LinkedList<Double> velocities = new LinkedList<>();


            double initialVelocity = -A*gamma / (2f*mass);
            velocities.add(initialVelocity);

            double initialPosition = 1;
            positions.add(initialPosition);

            double previousAcceleration;

            double acceleration;
            double nextVelocity;
            double predictedVelocity;
            double nextAcceleration;
            double nextPosition;

            for(int i = 0; i < 100; i++) {
                acceleration = (-k * positions.get(i) - (gamma * velocities.get(i))) / mass;

                if(i == 0)
                    previousAcceleration = (-k * 0 - (gamma * 0)) / mass;
                else
                    previousAcceleration = (-k * positions.get(i-1) - (gamma * velocities.get(i-1))) / mass;

                nextPosition = positions.get(i) + velocities.get(i) * deltaT + ((2/3.0) * acceleration - (1/6.0) * previousAcceleration) * Math.pow(deltaT,2);
                positions.add(nextPosition);
                //predecir v
                predictedVelocity = velocities.get(i) + ((3 / 2.0) * acceleration - (1 / 2.0) * previousAcceleration) * deltaT;
                nextAcceleration = (-k * nextPosition - (gamma * predictedVelocity)) / mass;
                //calculate v
                nextVelocity = velocities.get(i) + ((1 / 3.0) * nextAcceleration + (5 / 6.0) * acceleration - (1 / 6.0) * previousAcceleration) * deltaT;
                velocities.add(nextVelocity);
            }
            saveOscillatorMovement(positions);
        }

    }
}
