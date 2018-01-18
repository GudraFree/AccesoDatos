package tema1.ejemplos;


//Clases para generar el árbol en RAM
import org.w3c.dom.*; //DOMImplementation, Document;
import javax.xml.parsers.*; //DocumentBuilderFactory, DocumentBuilder

//Clases para transformar el árbol en un fichero XML
import javax.xml.transform.*;  //Source, Result, TransformerConfigurationException, TransformerException
import javax.xml.transform.dom.*;  //DOMsource
import javax.xml.transform.stream.*;  //StreamResult

import java.io.IOException;
import java.lang.String;
import java.io.File;
import java.util.Scanner;


public class CrearEmpleadoXml {

	/**
	 *@param etiqueta La etiqueta xml de este elemento
	 *@param valorTexto El texto asociado a esa etiqueta
	 *@param raiz el nodo padre, del árbol de objetos que estamos construyendo en RAM, de este nuevo nodo que vamos a construir
	 *       Nota: 'raiz' debe ser un nodo del objeto que representa 'documento'
	 *@param documento el objeto que representa el documento que estamos contruyendo en RAM
	 */
	private static void CrearElemento(String etiqueta, String valorTexto,Element raiz, Document documento)
	{
		Element elem= documento.createElement(etiqueta);  //creamos un hijo
		Text text=documento.createTextNode(valorTexto);   //creamos un objeto que representa un texto
		raiz.appendChild(elem);                           //pegamos el nuevo hijo a a la raiz
		elem.appendChild(text);                           //pegamos el valor como texto a este hijo
	}
	
	
	public static void main(String argv[]) throws IOException
	{
		//Datos que definen un empleado
		int id, dep;
		double salario;
		char apellido[]=new char[10];
		
		
		//1º) Construimos los objetos necesarios para empezar a construir el arbol de nodos
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		
		try{
		  DocumentBuilder builder=factory.newDocumentBuilder();
		  
		  //The DOMImplementation interface provides a number of methods for performing 
		  //operations that are independent of any particular instance of the document object model 
		  DOMImplementation implementation=builder.getDOMImplementation();
		  
		  Document document=implementation.createDocument(null, "empleados", null);
		  
		  document.setXmlVersion("1.0");
		  
		  
		  //2º) Repetir para cada nodo Empleado:
		  for(;;)
		  {
			Scanner scanner= new Scanner(System.in);
			  
			//2.1º) Obtenemos los datos que necesitaremos para construir un empleado
			
			  //Pedir los datos del  empleado (y comprobar que son correctos)
			  System.out.println("Introduzca el código del empleado (número entero no repetido mayor que 1). Si código es 0, significa que no hay mas empleados");
			  id=scanner.nextInt();
			  if (id==0) break;
			  scanner.nextLine();
			  System.out.println("Introduzca el Apellido del empleado (cadena no vacia de tamaño máximo 10)");
			  apellido=scanner.nextLine().toCharArray();
			  scanner.nextLine();
			  System.out.println("Introduzca el código del departamento (número mayor que 1");
			  dep=scanner.nextInt();
			  System.out.println("Introduzca el salario del empleado (número real positivo");
			  salario=scanner.nextDouble();
			  
			  
			//2.2º) Construirmos el subárbol que representa un empleado
			  
			  //Construir el elemento Empleado
			  
			  Element raizEmpleado=document.createElement("empleado");
			  
			  //getDocumentElement():
			  //This is a convenience attribute that allows direct access to the child node 
			  //that is the "document element" (en nuestro caso el elemento "Empleados", es decir,
			  //el primer nodo que hemos decidido nosotros) of the document 
			  
			  //appendChild (class Node): a un objeto nodo (el devuelto por 
			  //"document.getDocumentElement()" que hace el papel de padre, le añade un nodo hijo 
			  document.getDocumentElement().appendChild(raizEmpleado);
			  
			  CrearElemento("id",Integer.toString(id),raizEmpleado,document);
			  CrearElemento("apellido",new String(apellido).trim(),raizEmpleado,document);
			  CrearElemento("dep",Integer.toString(dep),raizEmpleado,document);
			  CrearElemento("salario",Double.toString(salario),raizEmpleado,document);  
		  }
		  
	 //3º) A partir del árbol que hemos construido en RAM, generar el fichero XML
		  
		  Source source= new DOMSource(document);
		  Result result= new StreamResult(new File("empleados.xml"));
		  
		  TransformerFactory transformerFactory=TransformerFactory.newInstance();
		  Transformer transformer= transformerFactory.newTransformer();
		  transformer.transform(source, result);
		  
		  
	 //4º)Se genera también el documento XML para ser mostrado en la consola
		  
		  Result consoleResult= new StreamResult(System.out);
		  transformer.transform(source, consoleResult);
		  
		
		}catch(ParserConfigurationException pce){System.err.println(pce.getMessage());}
		 catch(TransformerConfigurationException tce){System.err.println(tce.getMessage());}
		 catch(TransformerException te){System.err.println(te.getMessage());}
		
		
	}
	
		
}
