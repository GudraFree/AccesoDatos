
package tema1.ejemplos;

/**
 *
 * @author miguelb
 */

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
   
/**
 * Use SAX Parser to display all employees: id, apellido, dep, salario
 */
public class SAXParserEmpleados {
   private String currentElement;
   private int bookCount = 1;
   
   // Constructor
   public SAXParserEmpleados() {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         saxParser.parse(new File("src/saxparserempleados/empleados.xml"), new MyHandler());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   // Entry main method
   public static void main(String args[]) {
      new SAXParserEmpleados();
   }
   
   /*
    * Inner class for the Callback Handlers.
    */
   class MyHandler extends DefaultHandler {
      // Callback to handle element start tag
      @Override
      public void startElement(String uri, String localName, String qName,
                               Attributes attributes) throws SAXException {
         currentElement = qName;
         
         if (currentElement.equals("empleado")) {
            System.out.println("Empleado " + bookCount);
            bookCount++;
            
            String id = attributes.getValue("id");
            System.out.println("\tId (atributo):\t" + id);
            
            //'apellido' no tiene atributos, así que se mostrará 'null'
            String apellido = attributes.getValue("apellido");
            System.out.println("\tApellido (atributo):\t" + apellido);
            
         }
      }
   
      // Callback to handle element end tag
      @Override
      public void endElement(String uri, String localName, String qName)
            throws SAXException {
         currentElement = "";
      }
   
      // Callback to handle the character text data inside an element
      @Override
      public void characters(char[] chars, int start, int length) throws SAXException {
         if (currentElement.equals("id")) {
            System.out.println("\tId:\t" + new String(chars, start, length));
         } else if (currentElement.equals("apellido")) {
            System.out.println("\tApellido (caracteres):\t" + new String(chars, start, length));
         }else if (currentElement.equals("dep")) {
            System.out.println("\tDepartamento:\t" + new String(chars, start, length));
         }else if (currentElement.equals("salario")) {
            System.out.println("\tSalario:\t" + new String(chars, start, length));
         }
         
      }
   }
}

