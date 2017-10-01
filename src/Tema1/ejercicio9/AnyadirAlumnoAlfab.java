/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class AnyadirAlumnoAlfab {
    public static void main(String[] args) throws IOException{
        File desktop = new File(System.getProperty("user.home"), "Desktop");
        File listaClase = new File (desktop, "ListaDeClase.txt");
        if(!listaClase.exists()) System.out.println("ERROR FATAL");
        BufferedReader in = null;
        PrintWriter out = null;
        ArrayList<String> listaOrdenada = new ArrayList(); 
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca un nuevo alumno");
        String alumno = sc.nextLine();
        try {
            in = new BufferedReader(new FileReader(listaClase));
            String l;
            while((l=in.readLine()) != null) {
                System.out.println("debug");
                listaOrdenada.add(l);
            }
            listaOrdenada.add(alumno);
            Collections.sort(listaOrdenada);
            out = new PrintWriter(new FileWriter(listaClase));
            for(String alum : listaOrdenada) {
                System.out.println(alum);
                out.println(alum);
            }
        } catch (IOException e) {
            System.out.println("Error de E/S "+e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general "+e.getMessage());
        } finally {
            if(in!=null) in.close();
            if(out!=null) out.close();
        }
    }
}
