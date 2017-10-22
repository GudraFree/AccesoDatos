/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio17;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Perig
 */
public class GestorEmpleadosDOM {

    static final byte ACTIVO = 1, BORRADO = -1;
    static File ficheroEmpleados, gestorEmpleados;
    static Scanner sc;
    static Document document;

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
        boolean ficheroCreado = ficheroEmpleados.exists();
        if (ficheroCreado) {
            // leer el fichero
            try {
                // factory to builder -> parse a file to document
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroEmpleados);
                document.getDocumentElement().normalize(); // limpia el documento
            } catch (ParserConfigurationException | SAXException | IOException e) {
            }
            // leer el último id
            id = ultimoId(document) +1;
        } else {
            // crear el fichero
            try {
                // factory to builder to DOMImplementation to document
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation().createDocument(null, "empleados", null);
                document.setXmlVersion("1.0");
            } catch (ParserConfigurationException e) {
            }
        }

        Empleado emp = new Empleado();
        sc = new Scanner(System.in);

        //pedimos los datos y los almacenamos en el objeto Empleado creado
        emp.id = id;
        System.out.println("Introduzca nombre empleado");
        emp.nombre = sc.nextLine();
        System.out.println("Introduzca apellidos empleado");
        emp.apellidos = sc.nextLine();
        System.out.println("Introduzca departamento empleado");
        emp.departamento = sc.nextLine();
        
        //creamos el elemento empleado
        Element empleado = document.createElement("empleado");
        //lo añadimos al elemento raíz del documento (getDocumentElement)
        document.getDocumentElement().appendChild(empleado);
        
        crearElemento("id",emp.id+"",empleado);
        crearElemento("nombre",emp.nombre,empleado);
        crearElemento("apellidos",emp.apellidos,empleado);
        crearElemento("departamento",emp.departamento,empleado);
        
        
        //generamos el documento XML
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(ficheroEmpleados);
            
            // factory instance to transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {}
        
    } // fin alta()

    static void baja() {
        // TODO: implementar baja

    } //fin baja

    static void modif() {
        // TODO: implementar modif

    } //fin modif

    static void consulta() {
        //controlamos que hay al menos un empleado al que realizar la consulta (el fichero se crea al realizar un alta)
        if (!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una consulta");
            return;
        }

        switch (menuConsulta()) {
            case 1: // consulta por ID
            // TODO: implementar consulta.id
            case 2: // listar todos los empleados
                // TODO: implementar consulta.listar
                break;
        }
    }

    static int ultimoId(Document doc) {
        int id = 0;
        NodeList empleados = doc.getElementsByTagName("empleado");
        for (int i = 0; i < empleados.getLength(); i++) {
            Node emp = empleados.item(i); //guardamos el nodo sobre el que iteramos
            if (emp.getNodeType() == Node.ELEMENT_NODE) { //comprobamos que el nodo es un elemento
                Element empElem = (Element) emp; //si lo es podemos hacer el casting a Element sin complicaciones
                int empId = Integer.parseInt(getNodo("id",empElem));
                if(empId>id) id = empId;
            }
        }

        return id;
    }

    private static String getNodo(String etiqueta, Element elem) {
        //0º)elem
        //1º)elem.getElementsByTagName(etiqueta): devuelve un NodeList con todos
        //                                        los nodos -raices de subárboles-
        //                                        que contengan esa etiqueta
        //2º).item(0): devuelve el primer (se supone que único) nodo del NodeList
        //3º).getChildNodes(): Devuelve un NodeList con todos los nodos hijo -raices
        //                  de subárboles-de ese nodo padre
        //
        //P ej:          Element
        //              (empleado)
        //             /      \    \           
        //         Element Element  etc...     ======>0º)elem  1º)[id] 2º) id 3º) [1]
        //          (id)   (apellido)
        //          /             \
        //        Text           Text
        //        ("1")        ("López") 

        NodeList nodo = elem.getElementsByTagName(etiqueta).item(0).getChildNodes();

        //5º).item(0): Devuelve el primer nodo de esa lista  =======> 1
        Node valorNodo = (Node) nodo.item(0);

        //Devuelve el valor de ese nodo (que es una cadena)=========>  "1"
        return valorNodo.getNodeValue();//devuelve el valor del nodo  
    }
    
    static void crearElemento(String etiqueta, String valor, Element padre) {
        Element elem = document.createElement(etiqueta); //creamos el elemento hijo, nodo tipo Element
        Text texto = document.createTextNode(valor); // creamos el elemento del valor que tendrá el hijo, nodo de texto
        padre.appendChild(elem); // le añado al elemento padre el hijo
        elem.appendChild(texto); // al hijo le añado su contenido
        
    }
}
