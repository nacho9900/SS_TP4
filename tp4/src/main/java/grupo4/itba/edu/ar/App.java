package grupo4.itba.edu.ar;

import grupo4.itba.edu.ar.Model.EndState;
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
        
        Vector2 v = new Vector2( 10e3, 100e3);
        double mass = 1e-27;
        double D = 1e-8;
        
        // Variable conditions
        double dT = 1e-13;
        int seed = 6432121;
        ParticlePropagation particlePropagation = new ParticlePropagation( D, v, mass, dT, seed );

        EndState state = particlePropagation.run();
        System.out.println(state);

        // For 2.3 - Calculates length for each dT
        // List<Double> lengths = particlePropagation.calculatePathLength();
        // System.out.println("Particle path length: " + lengths.stream().reduce(0d, (x, y) -> x + y));
    }

    //2.2
    private void energyThrowDifferentDts(){

    }
}
