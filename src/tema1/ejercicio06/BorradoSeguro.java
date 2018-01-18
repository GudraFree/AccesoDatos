/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio06;

import java.io.File;

/**
 *
 * @author Perig
 */
public class BorradoSeguro {
    public static void main(String[] args) {
        File desktop = new File(System.getProperty("user.home"), "Desktop");
        File papelera = new File (desktop, "papelera");
        if (!papelera.exists()) papelera.mkdir();
        String[] fileNames = args;
        for(String fileName : fileNames) {
            File file = new File(fileName);
            String altName = file.getName();
            String auxName = altName;
            int index = 1;
            while (new File(papelera,altName).exists()) {
                altName = auxName.substring(0,auxName.indexOf(".")) +"("+index+")"+auxName.substring(auxName.indexOf("."));
                index++;
            }
            file.renameTo(new File(papelera,altName));
        }
    }
}
