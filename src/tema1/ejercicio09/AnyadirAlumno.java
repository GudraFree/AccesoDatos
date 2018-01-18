/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio09;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class AnyadirAlumno {
    public static void main(String[] args) throws IOException{
        File desktop = new File(System.getProperty("user.home"), "Desktop");
        File listaClase = new File (desktop, "ListaDeClase.txt");
        PrintWriter out = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca un nuevo alumno");
        String alumno = sc.nextLine();
        try {
            out = new PrintWriter(new FileWriter(listaClase, true));
            out.println(alumno);
        } finally {
            if(out!=null) out.close();
        }
    }
}
