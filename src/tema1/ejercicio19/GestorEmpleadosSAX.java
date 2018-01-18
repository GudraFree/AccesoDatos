/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio19;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Perig
 */
public class GestorEmpleadosSAX {
    static File ficheroEmpleados, gestorEmpleados;
    static Scanner sc;
    static Empleados listaEmp;

    public static void main(String[] args) {
        gestorEmpleados = new File("gestorEmpleados");
        ficheroEmpleados = new File(gestorEmpleados, "empleadosSAX.xml");
        int opcion;
        while ((opcion = menu()) != 5) {
            switch (opcion) {
                case 1: // Alta
                    alta();
                    break;
                case 2: // Baja
                    baja();
                    break;
                case 3: // Modificación
                    modif();
                    break;
                case 4: // Consulta
                    consulta();
                    break;
            }
        }

    }

    static int menu() {
        sc = new Scanner(System.in);
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nGestor de empleados con SAX. Por favor introduzca una opción");
            System.out.println("\t1. Alta");
            System.out.println("\t2. Baja");
            System.out.println("\t3. Modificación");
            System.out.println("\t4. Consulta");
            System.out.println("\t5. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 5;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        return opcion;
    }

    static int menuConsulta() { //ver menu()
        int opcion = 0;
        boolean opcionInvalida = true;
        do {
            System.out.println("\n¿Según qué campo quiere consultar?");
            System.out.println("\t1. ID de empleado");
            System.out.println("\t2. Listar todos los empleados");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 2;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        return opcion;
    }

    static void alta() {
        // comprobamos que los ficheros existen y almacenamos el último ID obtenido
        int id = comprobacionInicialFicheros();
        
        //creamos el objeto empleado y pedimos sus datos
        Empleado nuevoEmpleado = Empleado.pedirEmpleado(id);
        
        //añadimos el nuevo empleado a la lista y escribimos el fichero XML
        listaEmp.add(nuevoEmpleado);
        escribirEnXML();
        
    } // fin alta()

    static void baja() {
        // comprobamos que haya empleados dados de alta antes de realizar una baja
        if(!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una baja");
            return;
        }
        
        //leemos el fichero XML
        leerXML();
        
        // pedir un ID con tratamiento de excepciones
        boolean excepcion = false;
        int idABuscar = 0;
        do {
            System.out.println("Introduzca el id del empleado a dar de baja");
            try {
                idABuscar = sc.nextInt();
                excepcion = false;
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                excepcion = true;
                sc.nextLine(); //limpiar escaner. Muy necesario o bucle infinito
            }
        } while(excepcion);
        sc.nextLine(); //limpiamos escaner
        
        //obtenemos el empleado a borrar
        Empleado empABorrar = listaEmp.getEmpleado(idABuscar);
        //si existe, lo borramos y sobreescribimos el XML. Si no, notificamos al usuario que la operación no se ha realizado
        if(empABorrar!=null) {
            listaEmp.remove(empABorrar);
            escribirEnXML();
            System.out.println("Baja realizada con éxito");
        }
        else System.out.println("Error, ID no encontrado");
    } //fin baja

    static void modif() {
        // comprobamos que haya empleados dados de alta antes de realizar una modificacion
        if(!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una modificacion");
            return;
        }
        
        //leemos el fichero XML
        leerXML();
        
        // pedir un ID con tratamiento de excepciones
        boolean excepcion = false;
        int idABuscar = 0;
        do {
            System.out.println("Introduzca el id del empleado a dar de baja");
            try {
                idABuscar = sc.nextInt();
                excepcion = false;
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                excepcion = true;
                sc.nextLine(); //limpiar escaner. Muy necesario o bucle infinito
            }
        } while(excepcion);
        sc.nextLine(); //limpiamos escaner
        
        //obtenemos el empleado a borrar
        Empleado empAModificar = listaEmp.getEmpleado(idABuscar);
        //si existe, lo borramos y sobreescribimos el XML. Si no, notificamos al usuario que la operación no se ha realizado
        if(empAModificar!=null) {
            Empleado emp = Empleado.pedirEmpleado(idABuscar);
            empAModificar.nombre=emp.nombre;
            empAModificar.apellidos=emp.apellidos;
            empAModificar.departamento=emp.departamento;

            escribirEnXML();
            System.out.println("Modificación realizada con éxito");
        }
        else System.out.println("Error, ID no encontrado");
    } //fin modif

    static void consulta() {
        // obtenemos la lista de empleados con el SAX
        leerXML();

        switch (menuConsulta()) {
            case 1: // consulta por ID
                // Pedimos ID a buscar
                // pedir un ID con tratamiento de excepciones
                boolean excepcion = false;
                int idABuscar = 0;
                do {
                    System.out.println("Introduzca el id del empleado a dar de baja");
                    try {
                        idABuscar = sc.nextInt();
                        excepcion = false;
                    } catch (InputMismatchException e) {
                        System.out.println("Error, debe introducir un número entero");
                        excepcion = true;
                        sc.nextLine(); //limpiar escaner. Muy necesario o bucle infinito
                    }
                } while(excepcion);
                sc.nextLine(); //limpiamos escaner
                
                listaEmp.mostrarEmpleados(idABuscar);
                break;
            case 2: // listar todos los empleados
                // Recorremos la lista de empleados y los mostramos todos
                listaEmp.mostrarEmpleados();
                break;
        }
    }
    
    static int comprobacionInicialFicheros() { //devuelve el ID a usar en la siguiente alta
        int id = 0;
        //creamos los ficheros si estos no existen
        try { 
            if(!gestorEmpleados.isDirectory()) gestorEmpleados.mkdir();
            if(!ficheroEmpleados.exists()) {
                ficheroEmpleados.createNewFile();
                listaEmp = new Empleados();
            }
            else { //si el ficheroEmpleados existe, lo leemos y devolvemos el id más alto (es el siguiente que hay que poner)
                leerXML();
                id = listaEmp.idMasAlto();
            }
        } catch (IOException e) {
            System.out.println("Error en la creación de ficheros");
        }
        return id;
        
    }
    
    static void escribirEnXML() {
        XStream xs = new XStream();
        xs.alias("empleados",Empleados.class); //renombramos la clase Empleados como <empleados>
        xs.alias("empleado", Empleado.class); // renombramos la clase Empleado como <empleado>
        xs.addImplicitCollection(Empleados.class, "listaEmpleados"); // omitimos la colección listaEmpleados de la clase Empleados
        xs.useAttributeFor(Empleado.class, "id"); // indicamos que el campo "id" de empleado será un atributo
        xs.registerConverter(new IDConverter()); // y asignamos el convertidor necesario para usarlo
        try {
            FileOutputStream fos = new FileOutputStream(ficheroEmpleados); //creamos el FOS con el fichero xml donde guardaremos los datos
            xs.toXML(listaEmp,fos); // y pasamos a XML el objeto de la clase Empleados
        } catch (FileNotFoundException e) {
            System.out.println("Error, archivo no encontrado");
        }
    }
    
    static void leerXML() {
        try {
            listaEmp = new Empleados();
            SAXParserFactory.newInstance().newSAXParser().parse(ficheroEmpleados, new EmpHandler());
        } catch (ParserConfigurationException e) {
            System.out.println("Error de configuración del parseador");
        } catch (SAXException e) {
            System.out.println("Error de SAX");
        } catch (IOException e) {
            System.out.println("Error de E/S");
        }
    }
    
    static String currentElement;
    static Empleado emp;
    
    static class EmpHandler extends DefaultHandler {

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentElement = qName;
            if(currentElement.equals("empleado")) {
                emp = new Empleado();
                String id = attributes.getValue("id");
                emp.id = new ID(Integer.parseInt(id));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(qName.equals("empleado")) {
                listaEmp.add(emp);
            }
            currentElement = "";
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentElement.equals("nombre")) emp.nombre = new String(ch, start, length);
            if(currentElement.equals("apellidos")) emp.apellidos = new String(ch, start, length);
            if(currentElement.equals("departamento")) emp.departamento = new String(ch, start, length);
//            if(currentElement.equals("apellido")) emp.nombre = new String(ch, start, length);
//            if(currentElement.equals("dep")) emp.apellidos = new String(ch, start, length);
//            if(currentElement.equals("salario")) emp.departamento = new String(ch, start, length);
            
        }
        
    }
    
}
