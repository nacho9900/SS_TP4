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
        Oscilator.OscilatorMethods.predictBeeman();
    }
}
