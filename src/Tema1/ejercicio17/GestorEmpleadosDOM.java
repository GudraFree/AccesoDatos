/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio17;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
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

    static File ficheroEmpleados, gestorEmpleados;
    static Scanner sc;
    static Document document;

    public static void main(String[] args) {
        gestorEmpleados = new File("gestorEmpleados");
        ficheroEmpleados = new File(gestorEmpleados, "empleadosDOM.xml");
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
            System.out.println("\nGestor de empleados con DOM. Por favor introduzca una opción");
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
        int id = 0;
        if (ficheroEmpleados.exists()) {
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

        sc = new Scanner(System.in);

        //pedimos los datos y los almacenamos en el objeto Empleado creado
        Empleado em = Empleado.pedirEmpleado(id);
        
        //creamos el elemento empleado
        Element empleado = document.createElement("empleado");
        //lo añadimos al elemento raíz del documento (getDocumentElement)
        document.getDocumentElement().appendChild(empleado);
        
        // añadimos el id atributo y creamos los elementos hijos del elemento empleado
        empleado.setAttribute("id", id+"");
        crearElemento("nombre",em.nombre,empleado);
        crearElemento("apellidos",em.apellidos,empleado);
        crearElemento("departamento",em.departamento,empleado);
        
        
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
        // leemos el documento. Si no existe terminamos el método
        if (ficheroEmpleados.exists()) {
            // leer el fichero
            try {
                // factory to builder -> parse a file to document
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroEmpleados);
                document.getDocumentElement().normalize(); // limpia el documento
            } catch (ParserConfigurationException | SAXException | IOException e) {
            }
        } else {
            System.out.println("Error, debe realizar un alta antes de realizar una baja");
            return;
        }
        
        // pedimos el id a dar de baja
        System.out.println("Introduzca el id del empleado a dar de baja");
        int id = sc.nextInt();
        sc.nextLine(); //limpiamos scanner
        
        // obtenemos la lista de elementos empleados
        NodeList empleados = document.getElementsByTagName("empleado");
        
        boolean idEncontrado = false;
        
        // recorremos la lista de empleados
        for(int i=0; i<empleados.getLength(); i++) {
            Node empNode = empleados.item(i);
            // comprobamos que el nodo empleado es un elemento
            if (empNode.getNodeType()==Node.ELEMENT_NODE) {
                Element empElem = (Element) empNode; //lo convertimos a Element para poder trabajar con él
                int empId = Integer.parseInt(empElem.getAttribute("id")); // obtenemos el id
                if(id==empId) { // comparamos con el id a borrar
                    borrarNodo(empNode); //si coincide lo quitamos de su padre
                    idEncontrado = true;
                }
            }
        }
        
        // comprobamos si se ha realizado la baja. Si no, volvemos, para ahorrarnos la computación de generar el xml
        if (!idEncontrado) {
            System.out.println("Error, id de empleado inexistente");
            return;
        }
        
        //generamos el documento XML
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(ficheroEmpleados);
            
            // factory instance to transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {}

    } //fin baja

    static void modif() {
        // leemos el documento. Si no existe terminamos el método
        if (ficheroEmpleados.exists()) {
            // leer el fichero
            try {
                // factory to builder -> parse a file to document
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroEmpleados);
                document.getDocumentElement().normalize(); // limpia el documento
            } catch (ParserConfigurationException | SAXException | IOException e) {
            }
        } else {
            System.out.println("Error, debe realizar un alta antes de realizar una modificación");
            return;
        }
        
        // pedimos el id a modificar
        System.out.println("Introduzca el id del empleado a modificar");
        int id = sc.nextInt();
        sc.nextLine(); //limpiamos scanner
        
        // obtenemos la lista de elementos empleados
        NodeList empleados = document.getElementsByTagName("empleado");
        
        boolean idEncontrado = false;
        
        // recorremos la lista de empleados
        for(int i=0; i<empleados.getLength(); i++) {
            Node empNode = empleados.item(i);
            // comprobamos que el nodo empleado es un elemento
            if (empNode.getNodeType()==Node.ELEMENT_NODE) {
                Element empElem = (Element) empNode; //lo convertimos a Element para poder trabajar con él
                int empId = Integer.parseInt(empElem.getAttribute("id")); // obtenemos el id
                if(id==empId && !idEncontrado) { // comparamos con el id a modificar
                    //pedimos los datos de un nuevo empleado y lo guardamos en una variable Empleado
                    Empleado em = Empleado.pedirEmpleado(id);
                    
                    //borramos el nodo antiguo
                    borrarNodo(empNode);
                    
                    // creamos el nuevo elemento empleado y sus hijos
                    Element empleado = document.createElement("empleado");
                    document.getDocumentElement().appendChild(empleado);
                    
                    empleado.setAttribute("id", id+"");
                    crearElemento("nombre", em.nombre, empleado);
                    crearElemento("apellidos", em.apellidos, empleado);
                    crearElemento("departamento", em.departamento, empleado);
                    
                    idEncontrado = true;
                }
            }
        }
        
        // comprobamos si se ha realizado la modificación. Si no, volvemos, para ahorrarnos la computación de generar el xml
        if (!idEncontrado) {
            System.out.println("Error, id de empleado inexistente");
            return;
        }
        
        //generamos el documento XML
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(ficheroEmpleados);
            
            // factory instance to transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {}

    } //fin modif

    static void consulta() {
        if (ficheroEmpleados.exists()) {
            // leer el fichero
            try {
                // factory to builder -> parse a file to document
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroEmpleados);
                document.getDocumentElement().normalize(); // limpia el documento
            } catch (ParserConfigurationException | SAXException | IOException e) {
            }
        } else {
            System.out.println("Error, debe realizar un alta antes de realizar una modificación");
            return;
        }
        
        // obtenemos la lista de elementos empleados
        NodeList empleados = document.getElementsByTagName("empleado");

        boolean idEncontrado = false;

        switch (menuConsulta()) {
            case 1: // consulta por ID
                // pedimos el id a consultar
                System.out.println("Introduzca el id del empleado a consultar");
                int id = sc.nextInt();
                sc.nextLine(); //limpiamos scanner

                // recorremos la lista de empleados
                for(int i=0; i<empleados.getLength(); i++) {
                    Node empNode = empleados.item(i);
                    // comprobamos que el nodo empleado es un elemento
                    if (empNode.getNodeType()==Node.ELEMENT_NODE) {
                        Element empElem = (Element) empNode; //lo convertimos a Element para poder trabajar con él
                        int empId = Integer.parseInt(empElem.getAttribute("id")); // obtenemos el id
                        if(id==empId) { // comparamos con el id a consultar
                            mostrarEmpleadoElem(empElem); //si coincide lo mostramos
                            idEncontrado = true;
                        }
                    }
                }

                // comprobamos si se ha realizado la consulta.
                if (!idEncontrado) {
                    System.out.println("Error, id de empleado inexistente");
                }
                break;
            case 2: // listar todos los empleados

                // recorremos la lista de empleados
                for(int i=0; i<empleados.getLength(); i++) {
                    Node empNode = empleados.item(i);
                    // comprobamos que el nodo empleado es un elemento
                    if (empNode.getNodeType()==Node.ELEMENT_NODE) {
                        Element empElem = (Element) empNode; //lo convertimos a Element para poder trabajar con él
                        mostrarEmpleadoElem(empElem); //si coincide lo mostramos
                        idEncontrado = true;
                    }
                }
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
                int empId = Integer.parseInt(empElem.getAttribute("id"));
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
    
    static void borrarNodo(Node node) {
        node.getParentNode().removeChild(node);
    }
    
    static void mostrarEmpleadoElem(Element empleado) {
        System.out.println("Empleado #"+empleado.getAttribute("id"));
        System.out.println("\tNombre: "+getNodo("nombre",empleado));
        System.out.println("\tApellidos: "+getNodo("apellidos",empleado));
        System.out.println("\tDepartamento: "+getNodo("departamento",empleado));
    }
}
