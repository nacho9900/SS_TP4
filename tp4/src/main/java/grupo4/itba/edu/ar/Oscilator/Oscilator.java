package grupo4.itba.edu.ar.Oscilator;

import com.sun.jdi.DoubleValue;

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
        private static double[] alphas = new double[] { (3 / 16.0), (251/360.0), 1.0, (11/18.0), (1/6.0), (1/60.0)};

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

        public static void oscilatorVerlet(){
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

        public static void oscilatorBeeman(){
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

        private static List<Double> calculateDerivs(double position, double velocity){
            List<Double> resp = new LinkedList<>();
            resp.add(position);
            resp.add(velocity);
            resp.add(-k / mass * position);
            resp.add(-k / mass * velocity);
            resp.add(Math.pow((-k / mass),2) * position);
            resp.add(Math.pow((-k / mass),2) * velocity);

            return resp;
        }

        private static List<Double> generatePrediction(List<Double> derivs){
            List<Double> resp = new LinkedList<>();
            double auxiliar;

            for(int i = 0; i < derivs.size(); i++){
                auxiliar = 0;
                for(int j = 0; j < derivs.size() - i; j++)
                    auxiliar += derivs.get(i + j) * Math.pow(deltaT, j) / calculateFactorial(j);
                resp.add(auxiliar);
            }
            return resp;
        }

        private static Double evalAccel(List<Double> predictions){
            double resp = (-k * predictions.get(0) - (gamma * predictions.get(1))) / mass;
            return ((resp - predictions.get(2)) * Math.pow(deltaT,2)) / 2;
        }

        public static void gearPredictorCorrector(){
            LinkedList<Double> positions = new LinkedList<>();
            LinkedList<Double> velocities = new LinkedList<>();

            double initialPosition = 1;
            positions.add(initialPosition);

            double initialVelocity = -A*gamma / (2f*mass);
            velocities.add(initialVelocity);

            List<Double> predictions = calculateDerivs(initialPosition,initialVelocity);
            double r2;
            double aux;

            for(int i = 1; i < 100; i++) {
                predictions = generatePrediction(predictions);
                r2 = evalAccel(predictions);

                for(int j = 0; j < predictions.size(); j++){
                    aux = predictions.get(j) + alphas[j] * r2 * calculateFactorial(j) / Math.pow(deltaT,j);
                    predictions.set(j, aux);
                }
                positions.add(predictions.get(0));
            }

            saveOscillatorMovement(positions);
        }


        private static double calculateFactorial(int n){
            if(n == 0 || n == 1) return 1;
            return n * calculateFactorial(n -1);
        }

    }
}
