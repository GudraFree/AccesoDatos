/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examenes.trimestre1.exTrimestral.localizarFicheros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Perig
 */
public class LocalizarFicheros {
    File directorio = null;
    String nombreArchivo;
    File indice = null;
    TreeMap<String,ArrayList<String>> listaIndice = null;
    
    
    public static void main(String[] args) {
        LocalizarFicheros lf = new LocalizarFicheros();
        
        // comprobacion argumentos
        try {
            lf.compruebaArgumentos(args);
        } catch (SintaxisException e) {
            System.out.println("Error de sintaxis: la sintaxis correcta del comando es:");
            System.out.println("\tPara crear un índice: java LocalizarFicheros -g directorio");
            System.out.println("\tPara buscar un archivo: java LocalizarFicheros -c archivo indice");
            return;
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        //comprobamos qué opción  hemos elegido en línea de comandos y ejecutamos la operación correspondiente
        
        if(lf.directorio!=null) lf.creaIndice(lf.directorio);
        if(lf.indice!=null) lf.buscaArchivo();
    }
    
    private void compruebaArgumentos(String[] args) throws SintaxisException, FileNotFoundException {
        try {
            switch (args[0]) {
                case "-g":
                    directorio = new File(args[1]);
                    if(!directorio.isDirectory()) throw new FileNotFoundException("El directorio "+directorio.getName()+" no existe");
                    break;
                case "-c":
                    indice = new File(args[2]);
                    if(!indice.isFile()) throw new FileNotFoundException("El índice "+indice.getName()+" no existe");
                    nombreArchivo = args[1];
                    break;
                default:
                    throw new SintaxisException();
            }
        } catch (IndexOutOfBoundsException e) {
            throw new SintaxisException();
        }
    }
    
    private void creaIndice(File dir) {
        String nombreIndice = "";
        boolean primeraIteracion = false;
        
        if(listaIndice==null) { //creamos nuevo TreeMap solo en la primera iteración que guardará parejas (archivo,rutas)
            listaIndice = new TreeMap();
            primeraIteracion = true;
            nombreIndice = directorio.getPath().replace('\\', '_').replace(":", "")+".ind";
        } 
        File[] contenidosDirectorio = dir.listFiles();
        for(File file : contenidosDirectorio) {
            if(file.isDirectory()) creaIndice(file); //recursividad sobre el directorio que hemos encontrado. 
                                                     //Todas estas iteraciones no entrarán en el if de la primera iteración y usarán el mismo TreeMap global
            else if (file.isFile()) {
                ArrayList<String> rutas = listaIndice.get(file.getName()); //obtenemos la entrada del treemap referente al archivo
                if(rutas==null) { // no existe todavía la entrada en el TreeMap, la creamos
                    rutas = new ArrayList();
                    rutas.add(file.getAbsolutePath());
                    listaIndice.put(file.getName(), rutas);
                } else { //existe la entrada y hay que actualizarla
                    rutas.add(file.getAbsolutePath());
                }
                //debug
//                System.out.println(file.getAbsolutePath());
            }
        } //aquí el TreeMap está ya completo con todas las parejas (archivo,rutas)
        
        if(primeraIteracion) { //si estamos en la primera iteración, la que se ha llamado desde el main, debemos crear el índice
//            System.out.println(nombreIndice);
            File archivoIndice = new File(nombreIndice);
            PrintWriter pw = null;
            try {
                if(archivoIndice.exists()) archivoIndice.delete(); //si el archivo ya existe, lo borramos
                archivoIndice.createNewFile(); // y lo creamos de nuevo/por primera vez
                pw = new PrintWriter(new FileWriter(archivoIndice, true));
                for(Map.Entry<String,ArrayList<String>> entry : listaIndice.entrySet()) {
                    String cadena = entry.getKey()+":"; // la key es el nombre del archivo
                    for (String s : entry.getValue()) { //recorremos el arrayList de rutas almacenado en el value
                        cadena+=s+",";
                    }
                    //hemos escrito en el formato nombre:ruta,ruta,ruta,
                    cadena = cadena.substring(0,cadena.length()-2); // quitamos la última coma
                    pw.println(cadena); // la escribimos en el fichero (con salto de línea)
//                    System.out.println(cadena);
                }
            } catch (IOException e) {
                System.out.println("Error de E/S: creación índice");
                e.printStackTrace();
            } finally {
                pw.close();
            }
        }
        
    }
    
    private void buscaArchivo() {
        ArrayList<File> rutas = new ArrayList(); // para almacenar las distintas rutas si el fichero aparece más de una vez
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        boolean archivoEncontrado = false; // para controlar si encontramos el archivo introducido
        try {
            BufferedReader br = new BufferedReader(new FileReader(indice));
            String line;
            while((line=br.readLine())!=null) {
                if(line.startsWith(nombreArchivo+":")) { //si la línea es la correspondiente al archivo. El ":" para controlar el final de la cadena
                    String cadenaRutas = line.substring(nombreArchivo.length()+1); // eliminamos de la línea el 'nombreArchivo:' para tener solo las rutas separadas por comas
                    for(String ruta : cadenaRutas.split(",")) { // recorremos el array de rutas
//                        System.out.println("RUTA: "+ruta);
                        rutas.add(new File(ruta));
                    }
                    archivoEncontrado = true; //hemos encontrado el archivo que buscábamos
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error, fichero índice no encontrado");
        } catch (IOException e) {
            System.out.println("Error de E/S: lectura del índice");
        }
        
        if(!archivoEncontrado) { // si no hemos encontrado el archivo, terminamos programa
            System.out.println("Error, el archivo introducido ("+nombreArchivo+") no existe");
            return;
        }
        
        // si lo hemos terminado, ejecutamos este bucle que recorre los archivos y muestra la información pertinente
        for(File file : rutas) {
            System.out.println(file.getAbsolutePath());
            System.out.println(file.length()+" bytes, "+df.format(new Date(file.lastModified())));
        }
    }
}
