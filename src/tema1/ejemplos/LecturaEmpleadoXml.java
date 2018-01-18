package tema1.ejemplos;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class LecturaEmpleadoXml {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(new File("empleados.xml"));
			//Normalize es un método de la interfaz 'Node';
			//La clase 'Element' implementa la interfaz 'Node'
			//node.normalize(): Elimina nodos de texto vacios y combina los adayacentes
			//en caso de que los hubiera
			//Duda ¿Es cierto lo siguiente:?
			//Con esto creamos un árbol DOM ajustándonos a lo establecido por el estándar
			//XML: no pueden existir Tags ('Element' en el árbol DOM) con hijos vacios
			//(tags sin texto asociado: p ej: <empleado>/*vacio*</empleado>) y 
			//combina nodos adyacentes del mismo tipo
			document.getDocumentElement().normalize();
			
			System.out.println("Elemento raíz: " + document.getDocumentElement().getNodeName());
			//crea una lista con todos los nodos empleado
			NodeList empleados = document.getElementsByTagName("empleado");
			//recorrer la lista
			for(int i=0;i<empleados.getLength();i++){
				Node emple = empleados.item(i);//obtener un nodo
				if(emple.getNodeType() == Node.ELEMENT_NODE){//tipo de nodo
					Element elemento = (Element) emple;//obtener los elementos del nodo
					System.out.println("ID: " + getNodo("id", elemento));
					System.out.println("APELLIDO: " + getNodo("apellido", elemento));
					System.out.println("DEPARTAMENTO: " + getNodo("dep", elemento));
					System.out.println("SALARIO: " + getNodo("salario", elemento));
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}

	//obtener la información de un nodo
	private static String getNodo(String etiqueta, Element elem){
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
		Node valorNodo = (Node)nodo.item(0);
		
		//Devuelve el valor de ese nodo (que es una cadena)=========>  "1"
		return valorNodo.getNodeValue();//devuelve el valor del nodo  
	}
}//class