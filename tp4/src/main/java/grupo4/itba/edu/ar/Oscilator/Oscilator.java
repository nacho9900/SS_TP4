package grupo4.itba.edu.ar.Oscilator;

import com.sun.jdi.DoubleValue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Oscilator {

    public static class OscilatorMethods {

        private static double mass = 70f; // kg
        private static double k = 10000f; // constant N/m
        private static double gamma = 100f; // kg/s
        private static double tf = 5f; // s
        private static double A = 1f; // TODO: cambiar. no se de donde sale esto
        private static double springConstant = 1;
        private static double[] alphasArray = new double[]{(3 / 16.0), (251 / 360.0), 1.0, (11 / 18.0), (1 / 6.0), (1 / 60.0)};

        public static List<Double> oscillatorAnalitic(double deltaT) {
            LinkedList<Double> rs = new LinkedList<>();
            LinkedList<Double> vs = new LinkedList<>();
            double r = 1f; // m
            double v = -A * gamma / (2f * mass); // m/s

            // Analitic solution
            for (int i = 0; i < 100; i++) {
                double t = i * deltaT;
                r = A * Math.exp(-gamma * t / (2f * mass)) * Math.cos(Math.pow(k / mass - gamma * gamma / (4 * mass * mass), 0.5) * t);
                rs.add(r);
            }
            saveOscillatorMovement(rs, "Analytics");
            return rs;
        }

        private static void saveOscillatorMovement(List<Double> rs, String method) {
            String dumpFilename = "oscillator";

            dumpFilename = dumpFilename.replace(".", "");

            File dump = new File(dumpFilename + "-" + method + ".csv");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dump))) {
                StringBuilder builder = new StringBuilder();

                for (double r : rs) {
                    builder.append(r)
                            .append(System.lineSeparator());
                }

                writer.write(builder.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static List<Double> oscilatorVerlet(double deltaT) {
            LinkedList<Double> positions = new LinkedList<>();

            double initialPosition = 1;
            double initialVelocity = -A * gamma / (2f * mass);
            double acceleration = (-k * initialPosition - (gamma * initialVelocity)) / mass;

            double nextVelocity = initialVelocity + (deltaT * acceleration);

            double nextPosition = initialPosition + deltaT * nextVelocity + Math.pow(deltaT, 2) * acceleration / (2);

            positions.add(initialPosition);
            positions.add(nextPosition);

            for (int i = 1; i < 99; i++) {
                acceleration = (-k * positions.get(i) - (gamma * nextVelocity)) / mass;
                nextVelocity += (deltaT * acceleration);
                nextPosition = 2 * positions.get(i) - positions.get(i - 1) + acceleration * Math.pow(deltaT, 2);
                positions.add(nextPosition);
            }
            saveOscillatorMovement(positions, "Verlet");
            return positions;
        }

        public static List<Double> oscilatorBeeman(double deltaT) {
            LinkedList<Double> positions = new LinkedList<>();
            LinkedList<Double> velocities = new LinkedList<>();


            double initialVelocity = -A * gamma / (2f * mass);
            velocities.add(initialVelocity);

            double initialPosition = 1;
            positions.add(initialPosition);

            double previousAcceleration;

            double acceleration;
            double nextVelocity;
            double predictedVelocity;
            double nextAcceleration;
            double nextPosition;

            for (int i = 0; i < 99; i++) {
                acceleration = (-k * positions.get(i) - (gamma * velocities.get(i))) / mass;

                if (i == 0)
                    previousAcceleration = (-k * 0 - (gamma * 0)) / mass;
                else
                    previousAcceleration = (-k * positions.get(i - 1) - (gamma * velocities.get(i - 1))) / mass;

                nextPosition = positions.get(i) + velocities.get(i) * deltaT + ((2 / 3.0) * acceleration - (1 / 6.0) * previousAcceleration) * Math.pow(deltaT, 2);
                positions.add(nextPosition);
                predictedVelocity = velocities.get(i) + ((3 / 2.0) * acceleration - (1 / 2.0) * previousAcceleration) * deltaT;
                nextAcceleration = (-k * nextPosition - (gamma * predictedVelocity)) / mass;
                nextVelocity = velocities.get(i) + ((1 / 3.0) * nextAcceleration + (5 / 6.0) * acceleration - (1 / 6.0) * previousAcceleration) * deltaT;
                velocities.add(nextVelocity);
            }

            saveOscillatorMovement(positions, "Beeman");

            return positions;
        }

        public static List<Double> gearPredictorCorrector(double deltaT) {
            LinkedList<Double> positions = new LinkedList<>();
            LinkedList<Double> velocities = new LinkedList<>();

            double initialPosition = 1;
            positions.add(initialPosition);

            double initialVelocity = -A * gamma / (2f * mass);
            velocities.add(initialVelocity);

            List<Double> predictions = calculateDerivs(initialPosition, initialVelocity);
            double r2;
            double aux;

            for (int i = 1; i < 100; i++) {
                predictions = generatePrediction(predictions, deltaT);
                r2 = evalAccel(predictions, deltaT);

                for (int j = 0; j < predictions.size(); j++) {
                    aux = predictions.get(j) + alphasArray[j] * r2 * calculateFactorial(j) / Math.pow(deltaT, j);
                    predictions.set(j, aux);
                }
                positions.add(predictions.get(0));
            }

            saveOscillatorMovement(positions, "Gear");

            return positions;
        }

        private static List<Double> calculateDerivs(double position, double velocity) {
            List<Double> resp = new LinkedList<>();
            resp.add(position);
            resp.add(velocity);
            resp.add(-k / mass * position);
            resp.add(-k / mass * velocity);
            resp.add(Math.pow((-k / mass), 2) * position);
            resp.add(Math.pow((-k / mass), 2) * velocity);

            return resp;
        }

        private static List<Double> generatePrediction(List<Double> derivs, double deltaT) {
            List<Double> resp = new LinkedList<>();
            double auxiliar;

            for (int i = 0; i < derivs.size(); i++) {
                auxiliar = 0;
                for (int j = 0; j < derivs.size() - i; j++)
                    auxiliar += derivs.get(i + j) * Math.pow(deltaT, j) / calculateFactorial(j);
                resp.add(auxiliar);
            }
            return resp;
        }

        private static Double evalAccel(List<Double> predictions, double deltaT) {
            double resp = (-k * predictions.get(0) - (gamma * predictions.get(1))) / mass;
            return ((resp - predictions.get(2)) * Math.pow(deltaT, 2)) / 2;
        }




        private static double calculateFactorial(int n) {
            if (n == 0 || n == 1) return 1;
            return n * calculateFactorial(n - 1);
        }

        private static void saveOscillatorMovement(Map<Double, List<Double>> data, String extention) {
            String dumpFilename = "oscillator";

            dumpFilename = dumpFilename.replace(".", "");

            File dump = new File(dumpFilename + "-" + extention + ".csv");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dump))) {

                for (Map.Entry<Double, List<Double>> entry : data.entrySet()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(entry.getKey());
                    for (Double value : entry.getValue()) {
                        builder.append('\t')
                                .append(value);
                    }
                    builder.append(System.lineSeparator());
                    writer.write(builder.toString());
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void calculateError() {
            List<Double> dts = Arrays.asList(1e-2, 1e-3, 1e-4, 1e-5, 1e-6);

            List<Double> verlet0 = oscilatorVerlet(dts.get(0));
            List<Double> beeman0 = oscilatorBeeman(dts.get(0));
            List<Double> gear0 = gearPredictorCorrector(dts.get(0));
            List<Double> analitic0 = oscillatorAnalitic(dts.get(0));

            List<Double> verlet1 = oscilatorVerlet(dts.get(1));
            List<Double> beeman1 = oscilatorBeeman(dts.get(1));
            List<Double> gear1 = gearPredictorCorrector(dts.get(1));
            List<Double> analitic1 = oscillatorAnalitic(dts.get(1));

            List<Double> verlet2 = oscilatorVerlet(dts.get(2));
            List<Double> beeman2 = oscilatorBeeman(dts.get(2));
            List<Double> gear2 = gearPredictorCorrector(dts.get(2));
            List<Double> analitic2 = oscillatorAnalitic(dts.get(2));

            List<Double> verlet3 = oscilatorVerlet(dts.get(3));
            List<Double> beeman3 = oscilatorBeeman(dts.get(3));
            List<Double> gear3 = gearPredictorCorrector(dts.get(3));
            List<Double> analitic3 = oscillatorAnalitic(dts.get(3));

            List<Double> verlet4 = oscilatorVerlet(dts.get(4));
            List<Double> beeman4 = oscilatorBeeman(dts.get(4));
            List<Double> gear4 = gearPredictorCorrector(dts.get(4));
            List<Double> analitic4 = oscillatorAnalitic(dts.get(4));

            double verletError0 = 0;
            double beemanError0 = 0;
            double gearError0 = 0;

            double verletError1 = 0;
            double beemanError1 = 0;
            double gearError1 = 0;

            double verletError2 = 0;
            double beemanError2 = 0;
            double gearError2 = 0;

            double verletError3 = 0;
            double beemanError3 = 0;
            double gearError3 = 0;

            double verletError4 = 0;
            double beemanError4 = 0;
            double gearError4 = 0;

            for (int i = 0; i < 100; i++) {
                verletError0 += Math.pow(analitic0.get(i) - verlet0.get(i), 2);
                beemanError0 += Math.pow(analitic0.get(i) - beeman0.get(i), 2);
                gearError0 += Math.pow(analitic0.get(i) - gear0.get(i), 2);

                verletError1 += Math.pow(analitic1.get(i) - verlet1.get(i), 2);
                beemanError1 += Math.pow(analitic1.get(i) - beeman1.get(i), 2);
                gearError1 += Math.pow(analitic1.get(i) - gear1.get(i), 2);

                verletError2 += Math.pow(analitic2.get(i) - verlet2.get(i), 2);
                beemanError2 += Math.pow(analitic2.get(i) - beeman2.get(i), 2);
                gearError2 += Math.pow(analitic2.get(i) - gear2.get(i), 2);

                verletError3 += Math.pow(analitic3.get(i) - verlet3.get(i), 2);
                beemanError3 += Math.pow(analitic3.get(i) - beeman3.get(i), 2);
                gearError3 += Math.pow(analitic3.get(i) - gear3.get(i), 2);

                verletError4 += Math.pow(analitic4.get(i) - verlet4.get(i), 2);
                beemanError4 += Math.pow(analitic4.get(i) - beeman4.get(i), 2);
                gearError4 += Math.pow(analitic4.get(i) - gear4.get(i), 2);
            }

            verletError0 /= 100;
            beemanError0 /= 100;
            gearError0 /= 100;

            verletError1 /= 100;
            beemanError1 /= 100;
            gearError1 /= 100;

            verletError2 /= 100;
            beemanError2 /= 100;
            gearError2 /= 100;

            verletError3 /= 100;
            beemanError3 /= 100;
            gearError3 /= 100;

            verletError4 /= 100;
            beemanError4 /= 100;
            gearError4 /= 100;

            List<Double> errors0 = new LinkedList<>();
            List<Double> errors1 = new LinkedList<>();
            List<Double> errors2 = new LinkedList<>();
            List<Double> errors3 = new LinkedList<>();
            List<Double> errors4 = new LinkedList<>();

            errors0.add(verletError0);
            errors0.add(beemanError0);
            errors0.add(gearError0);

            errors1.add(verletError1);
            errors1.add(beemanError1);
            errors1.add(gearError1);

            errors2.add(verletError2);
            errors2.add(beemanError2);
            errors2.add(gearError2);

            errors3.add(verletError3);
            errors3.add(beemanError3);
            errors3.add(gearError3);

            errors4.add(verletError4);
            errors4.add(beemanError4);
            errors4.add(gearError4);

            Map<Double, List<Double>> data = new HashMap<>();

            data.put(dts.get(0), errors0);
            data.put(dts.get(1), errors1);
            data.put(dts.get(2), errors2);
            data.put(dts.get(3), errors3);
            data.put(dts.get(4), errors4);

            saveOscillatorMovement(data, "errors");
        }
    }
}
