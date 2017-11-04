/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio19;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Perig
 */
public class GestorEmpleadosSAX {

    static final byte ACTIVO = 1, BORRADO = -1;
    static File ficheroEmpleados, gestorEmpleados;
    static Scanner sc;
    static Empleados listaEmp;

    public static void main(String[] args) {
        gestorEmpleados = new File("gestorEmpleados");
        ficheroEmpleados = new File(gestorEmpleados, "empleados.xml");
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
        int opcion;
        boolean opcionInvalida;
        do {
            System.out.println("\nGestor de empleados con DOM. Por favor introduzca una opción");
            System.out.println("\t1. Alta");
            System.out.println("\t2. Baja");
            System.out.println("\t3. Modificación");
            System.out.println("\t4. Consulta");
            System.out.println("\t5. Salir");
            opcion = sc.nextInt();
            opcionInvalida = opcion < 1 || opcion > 5;
            if (opcionInvalida) {
                System.out.println("Error, opción introducida no válida\n");
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        return opcion;
    }

    static int menuConsulta() {
        int opcion;
        boolean opcionInvalida;
        do {
            System.out.println("\n¿Según qué campo quiere consultar?");
            System.out.println("\t1. ID de empleado");
            System.out.println("\t2. Listar todos los empleados");
            opcion = sc.nextInt();
            opcionInvalida = opcion < 1 || opcion > 2;
            if (opcionInvalida) {
                System.out.println("Error, opción introducida no válida\n");
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        return opcion;
    }

    static void alta() {
        int id = 0;
        //creamos los ficheros si estos no existen
        try { 
            if(!gestorEmpleados.isDirectory()) gestorEmpleados.mkdir();
            if(!ficheroEmpleados.exists()) {
                ficheroEmpleados.createNewFile();
                listaEmp = new Empleados();
            }
            else {
                leerXML();
                id = listaEmp.idMasAlto();
            }
        } catch (IOException e) {
            System.out.println("Error en la creación de ficheros");
        }
        
        //creamos el objeto empleado y pedimos sus datos
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.id = new ID(id);
        System.out.println("Introduzca nombre empleado");
        nuevoEmpleado.nombre=sc.nextLine();
        System.out.println("Introduzca apellidos empleado");
        nuevoEmpleado.apellidos=sc.nextLine();
        System.out.println("Introduzca departamento empleado");
        nuevoEmpleado.departamento=sc.nextLine();
        
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
        
        System.out.println("Introduzca el id del empleado a dar de baja");
        int idABuscar = sc.nextInt();
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
        
        System.out.println("Introduzca el id del empleado a modificar");
        int idABuscar = sc.nextInt();
        sc.nextLine(); //limpiamos escaner
        
        //obtenemos el empleado a borrar
        Empleado empAModificar = listaEmp.getEmpleado(idABuscar);
        //si existe, lo borramos y sobreescribimos el XML. Si no, notificamos al usuario que la operación no se ha realizado
        if(empAModificar!=null) {
            System.out.println("Introduzca nombre empleado");
            empAModificar.nombre=sc.nextLine();
            System.out.println("Introduzca apellidos empleado");
            empAModificar.apellidos=sc.nextLine();
            System.out.println("Introduzca departamento empleado");
            empAModificar.departamento=sc.nextLine();
            
            escribirEnXML();
            System.out.println("Baja realizada con éxito");
        }
        else System.out.println("Error, ID no encontrado");
    } //fin modif

    static void consulta() {
        // obtenemos la lista de empleados con el SAX
        leerXML();

        switch (menuConsulta()) {
            case 1: // consulta por ID
                // Pedimos ID a buscar
                System.out.println("Introduzca el ID del empleado a buscar");
                int idABuscar = sc.nextInt();
                listaEmp.mostrarEmpleados(idABuscar);
                break;
            case 2: // listar todos los empleados
                // Recorremos la lista de empleados y los mostramos todos
                listaEmp.mostrarEmpleados();
                break;
        }
    }
    
    static void escribirEnXML() {
        XStream xs = new XStream();
        xs.alias("empleados",Empleados.class);
        xs.alias("empleado", Empleado.class);
        xs.addImplicitCollection(Empleados.class, "listaEmpleados");
        xs.useAttributeFor(Empleado.class, "id");
        xs.registerConverter(new IDConverter());
        try {
            FileOutputStream fos = new FileOutputStream(ficheroEmpleados);
            xs.toXML(listaEmp,fos);
        } catch (Exception e) {}
    }
    
    static void leerXML() {
        try {
            listaEmp = new Empleados();
            SAXParserFactory.newInstance().newSAXParser().parse(ficheroEmpleados, new EmpHandler());
        } catch (Exception e) {}
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
