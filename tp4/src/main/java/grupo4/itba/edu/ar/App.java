package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Oscilator.Oscilator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
//         Oscilator.OscilatorMethods.oscillatorAnalitic(0.05);
//         Oscilator.OscilatorMethods.oscilatorVerlet(0.05);
//         Oscilator.OscilatorMethods.oscilatorBeeman(0.05);
//         Oscilator.OscilatorMethods.gearPredictorCorrector(0.05);
        Oscilator.OscilatorMethods.calculateError();

//       double D = Math.pow(10, -8);
//       Vector2 v = new Vector2(Math.pow(10, 4), Math.pow(10, 5));
//       double mass = Math.pow(10, -27);
//       double dT = 0.001;
//       ParticlePropagation particlePropagation = new ParticlePropagation(D, v, mass, dT);
//       int i = 3; // TODO: remove once idDone() is implemented
//       while( !particlePropagation.isDone() && i-- > 0 ) {
//           particlePropagation.nextStep();
//       }
//
//       particlePropagation.saveMovement();
    }
}
