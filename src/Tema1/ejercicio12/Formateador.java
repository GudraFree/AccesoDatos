/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio12;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Perig
 */
public class Formateador {
    public static void main(String[] args) throws IOException {
        boolean arch2 = false;
        if(args.length == 1 || (arch2 = args.length == 2)) {
            System.out.println("Start");
            File fOrigen, fDestino;
            BufferedReader br = null;
            BufferedWriter bw = null;
            ArrayList<String> texto = new ArrayList();
            ArrayList<String> fTexto = new ArrayList();
            try {
                fOrigen = new File(args[0]);
                String n = fOrigen.getPath();
                fDestino = arch2 ? new File(args[1]) : new File(n.substring(0, n.lastIndexOf("."))+"-formateado"+n.substring(n.lastIndexOf(".")));
                br = new BufferedReader(new FileReader(fOrigen));
                String l;
                while ((l=br.readLine()) != null) {
                    System.out.println("readed");
                    texto.add(l);
                }
                for(String lin : texto) {
                    System.out.println("modified");
                    fTexto.add(capitalize(lin.replaceAll("\\.",". ").replaceAll(" +", " ")));
                }
                bw = new BufferedWriter(new FileWriter(fDestino));
                for (String lin : fTexto) {
                    System.out.println("writed");
                    bw.write(lin+"\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error, archivo no encontrado\n"+e.getMessage());
            } catch (IOException e) {
                System.out.println("Error de E/S\n"+e.getMessage());
            } finally {
                if(br != null) br.close();
                if(bw != null) bw.close();
            }
        }
    }
    private static String capitalize(String sentence) {
        StringBuilder result = new StringBuilder(sentence.length());
        //First one is capital!
        boolean capitalize = true;

        //Go through all the characters in the sentence.
        for(int i = 0; i < sentence.length(); i++) {
            //Get current char
            char c = sentence.charAt(i);

            //If it's period then set next one to capital
            if(c == '.') {
                capitalize = true;
            }
            //If it's alphabetic character...
            else if(capitalize && Character.isAlphabetic(c)) {
                //...we turn it to uppercase
                c = Character.toUpperCase(c);
                //Don't capitalize next characters
                capitalize = false;
            }

            //Accumulate in result
            result.append(c);
        }
        return result.toString();
    }
}
