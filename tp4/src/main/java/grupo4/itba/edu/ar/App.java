package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Model.Vector2;
import grupo4.itba.edu.ar.Oscilator.Oscilator;
import grupo4.itba.edu.ar.ParticlePropagation.ParticlePropagation;

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
        // Oscilator.OscilatorMethods.oscillatorAnalitic();
        // Oscilator.OscilatorMethods.oscilatorVerlet();
        // Oscilator.OscilatorMethods.oscilatorBeeman();

       double D = 1e-8;
       Vector2 v = new Vector2( 10e3, 100e3);
       double mass = 1e-27;
       double dT = 1e-13;
       ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dT);
       while( !particlePropagation.isDone() ) {
           particlePropagation.nextStep();
       }

       particlePropagation.saveMovement();
    }
}
