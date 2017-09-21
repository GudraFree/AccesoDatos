/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio5;

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
            mostrarAyuda();
        } else if (args.length==2) {
            File f = new File(args[1]);
            String p = ".*"+args[0].replace(".", "\\.").replace("?",".").replace("*",".*")+".*";
            Pattern pattern = Pattern.compile(p);
            if(f.isDirectory()) {
                System.out.println("El contenido del directorio "+f.getAbsolutePath()+" es:");
                File[] lista = f.listFiles();
                for(int i=0; i<lista.length; i++) {
                    String name = lista[i].getName();
                    Matcher m=pattern.matcher(name);
                    if (Pattern.matches(p, name)) {
                        System.out.print(permisos(lista[i])+" "+lista[i].getName());
                        if(lista[i].isFile()) System.out.print("\t"+lista[i].length());
                        System.out.println();
                    }
                }
            }
            else {
                System.out.print("Error, debe introducirse un directorio");
            }
        } else {
            System.out.println("Error de sintaxis.");
            mostrarAyuda();
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
    private static void mostrarAyuda() {
        System.out.println("Para usar el comando, se usa: java Listar.java patrón rutaDirectorio");
    }
}
