/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio08;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Perig
 */
public class EnMayuscula {
    public static void main(String[] args) throws IOException { // fOrigen fDestino
        boolean append=true;
        if(args.length == 2) {
            System.out.println("debug");
            File fDestino = new File(args[1]);
            File fOrigen = new File(args[0]);
            FileReader input = null;
            FileWriter output = null;
            try {
                input = new FileReader(fOrigen);
                output = new FileWriter(fDestino, append);
                int c;
                while ((c=input.read()) != -1) {
                    output.write(Character.toUpperCase(c));
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error, archivo no encontrado\n"+e.getMessage());
            } catch(IOException e) {
                System.out.println("Error de E/S\n"+e.getMessage());
            } finally {
                if(input!=null) input.close();
                if(output!=null) output.close();
            }
            
        } else {
            try { throw new NumeroArgumentosException("Error, sintaxis inválida. El uso correcto es\n\tjava EnMayuscula fOrigen fDestino"); } 
            catch(NumeroArgumentosException e) { System.out.println(e.getMessage());}
        }
    }

    private static class NumeroArgumentosException extends Exception {

        public NumeroArgumentosException(String msg) {
            super(msg);
        }
    }
}
