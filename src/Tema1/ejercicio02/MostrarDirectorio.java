/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio02;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Perig
 */
public class MostrarDirectorio {
    public static void main(String args[]) throws FileNotFoundException{
        if (args.length!=1) {
            try {
                throw new LineaComandosException(mostrarAyuda());
            } catch(LineaComandosException e) { e.getMessage(); }
        } 
        else if (args[0].equals("-h")) {
            System.out.println(mostrarAyuda());
        } else {
            File f = new File(args[0]);
            if (!f.exists()) throw new FileNotFoundException("El archivo no existe");
            if(f.isDirectory()) {
                System.out.println("El directorio contiene los siguientes archivos");
                for (String list : f.list()) {
                    System.out.println(list);
                }
            } else if (f.isFile()) System.out.println("El nombre del fichero es "+f.getName());
        }
    }
    private static String mostrarAyuda() {
        return "Para usar el comando, se usa: java MostrarDirectorio rutaDelArchivoODirectorio";
    }
}
