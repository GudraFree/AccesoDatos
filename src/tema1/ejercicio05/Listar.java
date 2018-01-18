/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio05;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Perig
 */
public class Listar {
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("-h")) {
            System.out.println(mostrarAyuda());
        } else if (args.length==2) {
            File f = new File(args[1]);
            String p = ".*"+args[0].replace(".", "\\.").replace("?",".").replace("*",".*")+".*";
            if(f.isDirectory()) {
                System.out.println("El contenido del directorio "+f.getAbsolutePath()+" es:");
                File[] lista = f.listFiles();
                for (File file : lista) {
                    String name = file.getName();
                    if (name.matches(p)) {
                        System.out.print(permisos(file) + " " + file.getName());
                        if (file.isFile()) {
                            System.out.print("\t" + file.length());
                        }
                        System.out.println();
                    }
                }
            }
            else {
                System.out.print("Error, debe introducirse un directorio");
            }
        } else {
            try {
                throw new InvocacionException("Error de sintaxis\n"+mostrarAyuda());
            } catch(InvocacionException e) { e.getMessage(); }
        }
    }
    private static String permisos(File f) {
        String perms = "";
        if(f.isDirectory()) perms+="d"; else perms+="-";
        if(f.canRead()) perms+="r"; else perms+="-";
        if(f.canWrite()) perms+="w"; else perms+="-";
        perms+="-";
        return perms;
    }
    private static String mostrarAyuda() {
        return "Para usar el comando, se usa: java Listar.java patrón rutaDirectorio";
    }

    private static class InvocacionException extends Exception {

        public InvocacionException(String msg) {
        }
    }
}
