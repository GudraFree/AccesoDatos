/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author Perig
 */
public class MostrarDirectorioOrdenados {
    public static void main (String args[]) {
        if (args.length!=1) {
            try {
                throw new NumeroArgumentosException(mostrarAyuda());
            } catch (NumeroArgumentosException e) { e.getMessage(); }
            
        } else if (args[0].equals("-h")) {
            System.out.println(mostrarAyuda());
        } else {
            File f = new File(args[0]);
            if(f.isDirectory()) {
                System.out.println("El directorio contiene los siguientes archivos");
                conArrayList(f);
                conTreeSet(f);
            } else if (f.isFile()) System.out.println("El nombre del fichero es "+f.getName());
            else {
                try {
                    throw new FileNotFoundException("El fichero o directorio no existe");
                } catch (FileNotFoundException e) {
                    e.getMessage();
                }
            }
        }
    }
    private static String mostrarAyuda() {
        return "Para usar el comando, se usa: java MostrarDirectorio rutaDelArchivoODirectorio";
    }
    private static void conArrayList(File f) {
        ArrayList<File> directorios = new ArrayList();
        ArrayList<File> archivos = new ArrayList();
        File[] lista = f.listFiles();
        for (File file : lista) {
            try {
                if (file.isDirectory()) {
                    directorios.add(file);
                } else if (file.isFile()) {
                    archivos.add(file);
                } else throw new FileNotFoundException("Error, fichero no existe");
            } catch (FileNotFoundException e) {
                e.getMessage();
            } catch(Exception e) {
                e.getMessage();
            }
        }
        Arrays.sort(directorios.toArray());
        Arrays.sort(archivos.toArray());
        System.out.println("Directorios");
        for(int i=0; i<directorios.size(); i++) System.out.println("\t"+directorios.get(i).getName());
        System.out.println("Archivos");
        for(int i=0; i<archivos.size(); i++) System.out.println("\t"+archivos.get(i).getName());
    }
    private static void conTreeSet(File f) {
        TreeSet<File> directorios = new TreeSet();
        TreeSet<File> archivos = new TreeSet();
        File[] lista = f.listFiles();
        for (File file : lista) {
            if (file.isDirectory()) {
                directorios.add(file);
            } else if (file.isFile()) {
                archivos.add(file);
            }
        }
        Iterator<File> itDir = directorios.iterator();
        Iterator<File> itAr = archivos.iterator();
        System.out.println("Directorios");
        while(itDir.hasNext()) System.out.println("\t"+itDir.next().getName());
        System.out.println("Archivos");
        while(itAr.hasNext()) System.out.println("\t"+itAr.next().getName());
    }

    private static class NumeroArgumentosException extends Exception {

        public NumeroArgumentosException(String msg) {
            super(msg);
        }
    }
    
}
