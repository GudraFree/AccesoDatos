/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio19;

import java.io.File;
import java.util.LinkedList;
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
    static LinkedList<Empleado> listaEmp;

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
        // TODO: implementar alta
    } // fin alta()

    static void baja() {
        // TODO: implementar baja
    } //fin baja

    static void modif() {
        // TODO: implementar modif
    } //fin modif

    static void consulta() {
        // TODO: implementar consulta
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(ficheroEmpleados, new EmpHandler());
        } catch (Exception e) {}

        switch (menuConsulta()) {
            case 1: // consulta por ID
                // TODO: implementar consulta.id
                break;
            case 2: // listar todos los empleados
                // TODO: implementar consulta.listar
                for(Empleado em : listaEmp) {
                    em.mostrarEmpleado();
                }
                break;
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
                emp.id = Integer.parseInt(id);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(qName.equals("empleado")) {
                listaEmp.add(emp);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
//            if(currentElement.equals("nombre")) emp.nombre = new String(ch, start, length);
//            if(currentElement.equals("apell")) emp.apellidos = new String(ch, start, length);
//            if(currentElement.equals("depart")) emp.departamento = new String(ch, start, length);
            if(currentElement.equals("apellidos")) emp.nombre = new String(ch, start, length);
            if(currentElement.equals("dep")) emp.apellidos = new String(ch, start, length);
            if(currentElement.equals("salario")) emp.departamento = new String(ch, start, length);
            
        }
        
    }
    
}
