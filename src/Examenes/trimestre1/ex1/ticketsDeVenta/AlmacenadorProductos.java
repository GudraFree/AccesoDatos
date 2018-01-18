/*
Programa recibe un xml escrito a mano (ejemplo en hoja)

Lee todos los productos, y almacena las cantidades totales en un totales.xml
- Si el producto es nuevo, lo añade
- Si el producto existe, solo suma la cantidad

Enunciado:
1. Redactar un programa en Java que tome como entra(entrada) un archivo de html(xml) que
contiene información sobre una venta

<productos>
	<pasta codigo="28">
		<marca>Gallo</marca>
		<cantidad>2</cantidad>
		<precio>1.2</precio>
	</pasta>
	<tomate codigo="12">
		<marca>Fruco</marca>
		<cantidad>1</cantidad>
		<precio>0.9</precio>
	</tomate>
</productos>
El programa debe actualizar (o crear, la primera vez) un archivo XML
que contenga la misma información y la misma estructura, pero en la que
se actualiza la cantidad total en los productos que ya estuvieran registrados
o añade los nuevos productos que ya estuvieran previamente registrados
 */
package Examenes.trimestre1.ex1.ticketsDeVenta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Perig
 */
public class AlmacenadorProductos {
    //variables
    static Document document;
    static File totales, venta;
    static TreeMap<Integer,Producto> productosTotales;
    static ArrayList<Producto> productosVendidos;
    
    public static void main(String[] args) {
        // comprobamos que recibe un único argumento y que es un fichero existente
        if (args.length != 1) {
            System.out.println("Error, debe introducir un único fichero como argumento");
            System.exit(0);
        }
        venta = new File(args[0]);
        if(!venta.exists()) {
            System.out.println("Error, el fichero provisto no existe");
            System.exit(0);
        }
        
        // creo el file de totales
        totales = new File("totales.xml");
        
        
        leerProductosTotales();
        
        leerProductosVendidos();
        
        anyadirProductosVendidos();
        
        // creamos el documento XML donde vamos a meter todos los productos totales
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation().createDocument(null, "productos", null);
        } catch (ParserConfigurationException ex) {
            System.out.println("Error de configuración del parseador");
        }
        
        Iterator it = productosTotales.entrySet().iterator();
        
        while(it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            Producto p = (Producto)e.getValue();
            Element producto = document.createElement(p.tipo);
            document.getDocumentElement().appendChild(producto);

            producto.setAttribute("codigo", p.codigo+"");
            crearElemento("marca", p.marca, producto);
            crearElemento("cantidad", p.cantidad+"", producto);
            crearElemento("precio", p.precio+"", producto);
        }
        
        //generamos el documento XML
        try {
            Source source = new DOMSource(document);
            Result result = new StreamResult(totales);
            
            // factory instance to transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {}
    }
    
    static void leerProductosVendidos() {
        // leer el fichero. El handler guarda todos los productos en productosVendidos como objetos Producto
        try {
            productosVendidos = new ArrayList();
            SAXParserFactory.newInstance().newSAXParser().parse(venta, new ProductHandler());
        } catch (ParserConfigurationException e) {
            System.out.println("Error de configuración del parseador");
        } catch (SAXException e) {
            System.out.println("Error de SAX");
        } catch (IOException e) {
            System.out.println("Error de E/S");
        }
    }
    
    static void leerProductosTotales() {
        //comprobación de la existencia del fichero totales para crear el documento
        productosTotales = new TreeMap();
        if (totales.exists()) {
            // leer el fichero
            try {
                // leemos con SAX para almacenarlo en el map
                SAXParserFactory.newInstance().newSAXParser().parse(totales, new TotalesHandler());
                
            } catch (ParserConfigurationException e) {
                System.out.println("Error de configuración del parseador");
            } catch (SAXException e) {
                System.out.println("Error de SAX");
            } catch (IOException e) {
                System.out.println("Error de E/S");
            }
        }
    }
    
    static void anyadirProductosVendidos() {
        //recorremos todos los productos del ticket de venta para añadirlos
        for(Producto producto : productosVendidos) { 
            int codigo = producto.codigo;
            // por cada producto, obtenemos el producto del mismo código de la lista de totales
            Producto enTotal = productosTotales.get(codigo); 
            if (enTotal == null) {
                // si el producto obtenido es null es que no está en totales y hay que añadirlo
                productosTotales.put(codigo, producto);
            } else {
                // el producto existe en los totales, así que sumamos la cantidad
                enTotal.cantidad = enTotal.cantidad + producto.cantidad;
            }
        }
    }
    
    // variables del Handler
    static String currentElement;
    static Producto prod;
    
    // handler para leer el ticket de venta
    static class ProductHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentElement = qName;
            if(!currentElement.equals("marca") && !currentElement.equals("precio") && !currentElement.equals("cantidad") && !currentElement.equals("productos")) {
                System.out.println(currentElement);
                prod = new Producto();
                String codigo = attributes.getValue("codigo");
                prod.codigo = Integer.parseInt(codigo);
                prod.tipo = currentElement;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(!currentElement.equals("marca") && !currentElement.equals("precio") && !currentElement.equals("cantidad") && !currentElement.equals("productos")) {
                productosVendidos.add(prod);
            }
            currentElement = "";
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentElement.equals("marca")) prod.marca = new String(ch, start, length);
            if(currentElement.equals("cantidad")) prod.cantidad = Integer.parseInt(new String(ch, start, length));
            if(currentElement.equals("precio")) prod.precio = Float.parseFloat(new String(ch, start, length));
            
        }
    }
    
    // handler para leer el documento de productos totales
    static class TotalesHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentElement = qName;
            if(!currentElement.equals("marca") && !currentElement.equals("precio") && !currentElement.equals("cantidad") && !currentElement.equals("productos")) {
                prod = new Producto();
                String codigo = attributes.getValue("codigo");
                prod.codigo = Integer.parseInt(codigo);
                prod.tipo = currentElement;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(!currentElement.equals("marca") && !currentElement.equals("precio") && !currentElement.equals("cantidad") && !currentElement.equals("productos")) {
                productosTotales.put(prod.codigo,prod);
            }
            currentElement = "";
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if(currentElement.equals("marca")) prod.marca = new String(ch, start, length);
            if(currentElement.equals("cantidad")) prod.cantidad = Integer.parseInt(new String(ch, start, length));
            if(currentElement.equals("precio")) prod.precio = Float.parseFloat(new String(ch, start, length));
            
        }
    }
    
    
    // métodos auxiliares para DOM
    
    static void crearElemento(String etiqueta, String valor, Element padre) {
        Element elem = document.createElement(etiqueta); //creamos el elemento hijo, nodo tipo Element
        Text texto = document.createTextNode(valor); // creamos el elemento del valor que tendrá el hijo, nodo de texto
        padre.appendChild(elem); // le añado al elemento padre el hijo
        elem.appendChild(texto); // al hijo le añado su contenido
        
    }
}
