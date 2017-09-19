/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio1;

/**
 *
 * @author Perig
 */
import java.io.File;

public class MostrarDirectorioActual {
    
    public static void main (String args[]) {
        File f = new File(".");
        System.out.println("Los ficheros del directorio actual son:");
        for(int i=0; i<f.list().length; i++) {
            System.out.println(f.list()[i]);
        }
    }
}
