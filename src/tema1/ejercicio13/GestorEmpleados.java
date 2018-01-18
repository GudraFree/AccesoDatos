/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio13;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        void mostrar() {
            System.out.println("Empleado #"+id+"\n\tNombre: "+nombre+" "+ape1+" "+ape2+"\n\tDepartamento: "+depart+"\n\tCiudad: "+ciudad+"\n\tSalario: "+salario);
        }
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
                System.out.println("Gestor de usuarios. Opciones disponibles:\n\n\t1. Alta\n\t2. Baja\n\t3. Modificación\n\t4. Consulta\n\t5. Salir\n\nSeleccione una opción: ");
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
//        try {
//            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(empleados)));
//            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados)));
//        } catch (FileNotFoundException e) {}
        while((opcion=Menu.menu())!= Menu.opcionMenu.salir) {
            ArrayList<Empleado> listaEmpleados;
            Empleado empleadoAux;
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
                    if (empleado.salario < 0 || empleado.salario>99999) {
                        System.out.println("Alta no realizada, salario fuera de rango [0-99999]");
                        break;
                    }
                    
                    //si no pasa la comprobación no llegará a este punto y no se realizará el alta
                    
                    //realizamos el alta
                    try {
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados, true)));
                        dos.writeInt(empleado.id);
                        dos.writeUTF(empleado.nombre);
                        dos.writeUTF(empleado.ape1);
                        dos.writeUTF(empleado.ape2);
                        dos.writeUTF(empleado.depart);
                        dos.writeUTF(empleado.ciudad);
                        dos.writeFloat(empleado.salario);
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
                case baja: 
                    if(!empleados.exists()) {
                        System.out.println("Error, no existen empleados. Por favor realice al menos un alta antes de realizar una baja.\n");
                        break;
                    }
                    
                    //leemos el archivo y almacenamos sus datos en una lista de empleados
                    listaEmpleados = new ArrayList();
                    try {
                        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(empleados)));
                        while(dis.available()>0) {
                            empleadoAux = new Empleado();
                            empleadoAux.id = dis.readInt();
                            empleadoAux.nombre = dis.readUTF();
                            empleadoAux.ape1 = dis.readUTF();
                            empleadoAux.ape2 = dis.readUTF();
                            empleadoAux.depart = dis.readUTF();
                            empleadoAux.ciudad = dis.readUTF();
                            empleadoAux.salario = dis.readFloat();
                            empleadoAux.control = dis.readByte();
                            listaEmpleados.add(empleadoAux);
                        }
                        dis.close();
                    } catch (IOException e) {} 
                    
                    System.out.println("Introduzca el id del empleado a dar de baja:");
                    int idBaja = sc.nextInt();
                    sc.nextLine(); //limpiamos escaner
                    
                    boolean exitoBaja = false; //booleano de control para comprobar si se ha podido realizar la baja
                    
                    // volvemos a escribir todos los empleados en el fichero, cambiando antes el byte de control del empleado que damos de baja
                    try {
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados, false))); //no utilizamos el append pues queremos sobreescribir el fichero
                        for(Empleado emp : listaEmpleados) {
                            if(emp.id==idBaja) {
                                emp.control = BORRADO;
                                exitoBaja = true; //si el id es válido entrará a este if, realiza la baja y se confirma el éxito de la operación
                                System.out.println("Baja del empleado "+emp.id+" realizada con éxito");
                            }
                            dos.writeInt(emp.id);
                            dos.writeUTF(emp.nombre);
                            dos.writeUTF(emp.ape1);
                            dos.writeUTF(emp.ape2);
                            dos.writeUTF(emp.depart);
                            dos.writeUTF(emp.ciudad);
                            dos.writeFloat(emp.salario);
                            dos.writeByte(emp.control);
                        }
                        
                        dos.close();
                        
                    } catch (IOException e) {}
                    
                    //mensaje de error si el id no coincide con ninguno
                    if(!exitoBaja) System.out.println("Error, id inválido, no se realizó ninguna baja");
                    
                    //terminamos el caso baja
                    break;
                case modif: 
                    if(!empleados.exists()) {
                        System.out.println("Error, no existen empleados. Por favor realice al menos un alta antes de realizar una modificación.\n");
                        break;
                    }
                    
                    //leemos el archivo y almacenamos sus datos en una lista de empleados
                    listaEmpleados = new ArrayList();
                    try {
                        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(empleados)));
                        while(dis.available()>0) {
                            empleadoAux = new Empleado();
                            empleadoAux.id = dis.readInt();
                            empleadoAux.nombre = dis.readUTF();
                            empleadoAux.ape1 = dis.readUTF();
                            empleadoAux.ape2 = dis.readUTF();
                            empleadoAux.depart = dis.readUTF();
                            empleadoAux.ciudad = dis.readUTF();
                            empleadoAux.salario = dis.readFloat();
                            empleadoAux.control = dis.readByte();
                            listaEmpleados.add(empleadoAux);
                        }
                        dis.close();
                    } catch (IOException e) {} 
                    
                    System.out.println("Introduzca el id del empleado a modificar:");
                    int idModif = sc.nextInt();
                    sc.nextLine(); //limpiamos escaner
                    boolean exitoModif = false;
                    
                    // volvemos a escribir todos los empleados en el fichero, cambiando antes el byte de control del empleado que damos de baja
                    try {
                        dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(empleados, false))); //no utilizamos el append pues queremos sobreescribir el fichero
                        for(Empleado emp : listaEmpleados) {
                            Empleado empleadoModif = emp;
                            if(emp.id==idModif) {
                                if(emp.control == BORRADO) System.out.println("Error, empleado no existente");
                                else{
                                    System.out.println("Introduzca nuevo nombre empleado:");
                                    empleadoModif.nombre = sc.nextLine();
                                    System.out.println("Introduzca nuevo primer apellido empleado:");
                                    empleadoModif.ape1 = sc.nextLine();
                                    System.out.println("Introduzca nuevo segundo apellido empleado:");
                                    empleadoModif.ape2 = sc.nextLine();
                                    System.out.println("Introduzca nuevo departamento empleado:");
                                    empleadoModif.depart = sc.nextLine();
                                    System.out.println("Introduzca nuevo ciudad empleado:");
                                    empleadoModif.ciudad = sc.nextLine();
                                    System.out.println("Introduzca nuevo salario empleado:");
                                    empleadoModif.salario = sc.nextFloat();
                                    sc.nextLine(); //limpiamos scanner
                                    
                                    exitoModif = true;
                                    System.out.println("Modificación realizada con éxito");
                                }
                            }
                            dos.writeInt(empleadoModif.id);
                            dos.writeUTF(empleadoModif.nombre);
                            dos.writeUTF(empleadoModif.ape1);
                            dos.writeUTF(empleadoModif.ape2);
                            dos.writeUTF(empleadoModif.depart);
                            dos.writeUTF(empleadoModif.ciudad);
                            dos.writeFloat(empleadoModif.salario);
                            dos.writeByte(empleadoModif.control);
                        }
                        dos.close();
                        
                    } catch (IOException e) {}
                    
                    if(!exitoModif) System.out.println("Error, id inválido, no se realizó ninguna modificación");
                    
                    //terminamos el caso modif
                    break;
                case consulta:
                    if(!empleados.exists()) {
                        System.out.println("Error, no existen empleados. Por favor realice al menos un alta antes de realizar una consulta.\n");
                        break;
                    }
                    
                    //leemos el archivo y almacenamos sus datos en una lista de empleados
                    listaEmpleados = new ArrayList();
                    try {
                        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(empleados)));
                        while(dis.available()>0) {
                            empleadoAux = new Empleado();
                            empleadoAux.id = dis.readInt();
                            empleadoAux.nombre = dis.readUTF();
                            empleadoAux.ape1 = dis.readUTF();
                            empleadoAux.ape2 = dis.readUTF();
                            empleadoAux.depart = dis.readUTF();
                            empleadoAux.ciudad = dis.readUTF();
                            empleadoAux.salario = dis.readFloat();
                            empleadoAux.control = dis.readByte();
                            listaEmpleados.add(empleadoAux);
                        }
                        dis.close();
                    } catch (IOException e) {} 
                    
                    System.out.println("Introduzca el id del empleado a consultar:");
                    int idConsulta = sc.nextInt();
                    sc.nextLine(); //limpiamos escaner
                    boolean exitoCons = false;
                    
                    // volvemos a escribir todos los empleados en el fichero, cambiando antes el byte de control del empleado que damos de baja
                    
                    for(Empleado emp : listaEmpleados) {
                        if(emp.id==idConsulta) {
                            if(emp.control == BORRADO) System.out.println("Error, empleado inexistente"); 
                            else emp.mostrar();
                            exitoCons = true;
                        }
                    }
                    
                    if(!exitoCons) System.out.println("Error, id inválido, no se pudo realizar consulta");
                    
                    //terminamos el caso consulta
                    break;
            }
        }
    }
}
