package grupo4.itba.edu.ar;

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

        ParticlePropagation particlePropagation = new ParticlePropagation();
        while( !particlePropagation.isDone() ) {
            particlePropagation.nextStep();
        }

        particlePropagation.saveMovement();
    }
}
