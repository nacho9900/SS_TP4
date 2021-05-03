package grupo4.itba.edu.ar.Oscilator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Oscilator {

    public static class OscilatorMethods{

        public static void oscillatorAnalitic() {
            double mass = 70f; // kg
            double k = 10000f; // constant N/m
            double gamma = 100f; // kg/s
            double tf = 5f; // s
            double A = 2f; // TODO: cambiar. no se de donde sale esto
            double deltaT = 0.00001f;

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
    }
}
