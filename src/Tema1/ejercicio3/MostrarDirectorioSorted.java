/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio3;

import java.io.File;

/**
 *
 * @author Perig
 */
public class MostrarDirectorioSorted {
    public static void main(String args[]){
        if (args.length!=1) {
            System.out.println("Error, sintaxis incorrecta.");
            mostrarAyuda();
            return;
        } else if (args[0].equals("-h")) {
            mostrarAyuda();
            return;
        } else {
            File f = new File(args[0]);
            if(f.isDirectory()) {
                System.out.println("El directorio contiene los siguientes archivos");
                for(int i=0; i<f.list().length; i++) {
                    System.out.println(f.list()[i]);
                }
            } else if (f.isFile()) System.out.println("El nombre del fichero es "+f.getName());
            else System.out.println("El fichero o directorio no existe");
        }
    }
    private static void mostrarAyuda() {
        System.out.println("Para usar el comando, se usa: java -jar MostrarDirectorio.java rutaDelArchivoODirectorio");
    }
}
