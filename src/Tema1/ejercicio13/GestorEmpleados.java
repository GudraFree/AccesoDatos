/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio13;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class GestorEmpleados {
    final static byte BORRADO = 1;
    final static byte ACTIVO = 0;
    static Scanner sc = new Scanner(System.in);
    static DataInputStream dis;
    static DataOutputStream dos;
    
    //objeto Empleado para recoger los datos del fichero
    private static class Empleado {
        int id;
        String nombre;
        String ape1;
        String ape2;
        String depart;
        String ciudad;
        float salario;
        byte control;
    }
    
    //clase para gestionar el display y las opciones del menú del gestor
    private static class Menu {
        enum opcionMenu { alta, baja, modif, consulta, salir}; //enum con las distintas opciones
        static opcionMenu[] opciones = opcionMenu.values();
        final static int NOPCIONES = opciones.length;
        public static opcionMenu menu() { //muestra menú, recibe una opción y devuelve opción elegida
            boolean opcionInvalida;
            int opcion;
            do {
                System.out.println("Gestor de usuarios. Opciones disponibles:\n\n\t1. Alta\nºt2. Baja\n\t3. Modificación\n\t4. Consulta\n\t5. Salir\n\nSeleccione una opción: ");
                opcion = sc.nextInt();
                opcionInvalida = opcion<1 || opcion>NOPCIONES; //si se introduce una opción no contemplada es inválida
                if(opcionInvalida) System.out.println("Error, opción inválida\n\n"); //mensaje de error si opción inválida
            } while (opcionInvalida); //repite mientras la opción sea inválida
            sc.nextLine(); //limpia el scanner
            return opciones[opcion-1];
        }
    }
    
    //clase auxiliar para escritura y comprobación de empleados
//    private static class EmplAux {
//        final static String EOFIELD = "|";
//        final static String EORECORD = System.getProperty("line.separator");
//        public static boolean esInvalida(String s) {
//            return (s.contains(EOFIELD) || s.contains(EORECORD));
//        }
//    }
    
    
    public static void main(String[] args) {
        Menu.opcionMenu opcion;
        File empleados = new File(new File(System.getProperty("user.home"),"Desktop"),"empleados.txt");
        File indice = new File(new File(System.getProperty("user.home"),"Desktop"),".indice"); //archivo oculto con la cuenta de índices de empleados, que son únicos
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(empleados)));
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados)));
        } catch (FileNotFoundException e) {}
        while((opcion=Menu.menu())!= Menu.opcionMenu.salir) {
            switch (opcion) {
                case alta:
                    //creamos fichero de empleados si este no existe
                    if(!empleados.exists()) try { empleados.createNewFile(); } catch (IOException e) {}
                    //creamos fichero ocutlo indice si no existe, y escribimos el primer índice como 0
                    if(!indice.exists()) {
                        try { indice.createNewFile(); } catch (IOException e) {}
                        try {
                            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indice)));
                            dos.writeInt(0);
                            dos.close();
                        } catch (IOException e) {}
                    } 
                    
                    //recogemos los datos del nuevo empleado en un objeto Empleado
                    Empleado empleado = new Empleado();
                    System.out.println("Introduzca nombre empleado:");
                    empleado.nombre = sc.nextLine();
                    System.out.println("Introduzca primer apellido empleado:");
                    empleado.ape1 = sc.nextLine();
                    System.out.println("Introduzca segundo apellido empleado:");
                    empleado.ape2 = sc.nextLine();
                    System.out.println("Introduzca departamento empleado:");
                    empleado.depart = sc.nextLine();
                    System.out.println("Introduzca ciudad empleado:");
                    empleado.ciudad = sc.nextLine();
                    System.out.println("Introduzca salario empleado:");
                    empleado.salario = sc.nextFloat();
                    sc.nextLine(); //limpiamos scanner
                    
                    // leemos el último índice.
                    try {
                        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(indice)));
                        empleado.id = dis.readInt();
                        dis.close();
                    } catch (IOException e) {}
                    empleado.control = ACTIVO; //ponemos el byte de control como empleado activo
                    
                    
                    //realizamos comprobaciones para verificar que el alta es válida
//                    if (EmplAux.esInvalida(empleado.nombre)) {
//                        System.out.println("Alta no realizada, carácter no admitido en "+empleado.nombre);
//                        break;
//                    }
//                    if (EmplAux.esInvalida(empleado.ape1)) {
//                        System.out.println("Alta no realizada, carácter no admitido en "+empleado.ape1);
//                        break;
//                    }
//                    if (EmplAux.esInvalida(empleado.ape2)) {
//                        System.out.println("Alta no realizada, carácter no admitido en "+empleado.ape2);
//                        break;
//                    }
//                    if (EmplAux.esInvalida(empleado.depart)) {
//                        System.out.println("Alta no realizada, carácter no admitido en "+empleado.depart);
//                        break;
//                    }
//                    if (EmplAux.esInvalida(empleado.ciudad)) {
//                        System.out.println("Alta no realizada, carácter no admitido en "+empleado.ciudad);
//                        break;
//                    }
                    if (empleado.salario < 0 || empleado.salario>99999) {
                        System.out.println("Alta no realizada, salario fuera de rango [0-99999]");
                        break;
                    }
                    
                    //si no pasa alguna de las comprobaciones no llegará a este punto y no se realizará el alta
                    
                    //realizamos el alta
                    try {
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados, true)));
                        dos.writeInt(empleado.id);
                        dos.writeUTF(empleado.nombre);
                        dos.writeUTF(empleado.ape1);
                        dos.writeUTF(empleado.ape2);
                        dos.writeUTF(empleado.depart);
                        dos.writeUTF(empleado.ciudad);
                        dos.writeByte(empleado.control);
                        dos.close();
                    } catch (IOException e) {}
                    
                    //una vez realizada el alta, incrementamos el índice
                    try {
                        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(indice)));
                        int id = dis.readInt();
                        dis.close();
                        id++;
                        indice.delete();
                        indice.createNewFile();
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(indice)));
                        dos.writeInt(id);
                        dos.close();
                    } catch (IOException e) {}
                    
                    //fin de alta
                    break;
            }
        }
    }
}
