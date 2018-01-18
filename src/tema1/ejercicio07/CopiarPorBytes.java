/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio07;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Perig
 */
public class CopiarPorBytes {
    public static void main(String[] args) throws IOException { // fDestino fOrigen [-a]
        boolean append=false;
            if(args.length == 2 || (append=(args.length == 3 && args[2].equals("-a")))) {
            System.out.println("debug");
            File fDestino = new File(args[0]);
            File fOrigen = new File(args[1]);
            FileInputStream input = null;
            FileOutputStream output = null;
            try {
                if(!fDestino.exists()) fDestino.createNewFile();
                input = new FileInputStream(fOrigen);
                output = new FileOutputStream(fDestino, append);
                int c;
                while ((c=input.read()) != -1) {
                    output.write(c);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error, archivo no encontrado");
            } catch(IOException e) {
                System.out.println("Error de E/S");
            } finally {
                if(input!=null) input.close();
                if(output!=null) output.close();
            }
            
        } else {
            try { throw new NumeroArgumentosException("Error, sintaxis inválida. El uso correcto es\n\tjava EnMayuscula fDestino fOrigen [-a]"); } 
            catch(NumeroArgumentosException e) { System.out.println(e.getMessage());}
        }
    }

    private static class NumeroArgumentosException extends Exception {

        public NumeroArgumentosException(String msg) {
            super(msg);
        }
    }
}
