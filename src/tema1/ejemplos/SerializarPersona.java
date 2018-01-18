/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejemplos;

import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class SerializarPersona implements Serializable{

    
    
   public SerializarPersona() throws IOException{
       
       Persona p= new Persona("Juanito", 5);
       
       
       //1º) -----------------Serializar la persona------------------
       
       File fichero1 = new File("FicheroPersona.dat");
       FileOutputStream fileout= new FileOutputStream(fichero1);
       ObjectOutputStream dataOS= new ObjectOutputStream(fileout);
       
       
       //1º.a) Serializamos cada atributo del objeto "Persona" 
      // dataOS.writeUTF(p.getNombre());
     //  dataOS.writeInt(p.getEdad());
       
       //1º.b) Serializamos el objeto "Persona" en un solo paso
       
       dataOS.writeObject(p); //El objeto "p" debe ser instancia de una
                              //clase que implemente la interfaz 'Serializable'
       
       
       dataOS.close(); 
       
       p.mostrarPersona();
       
       
             
       
      //2º)---------------"deserializar" el objeto persona-----------------
       Persona p2 = new Persona();
       
       File fichero2 = new File("FicheroPersona.dat");
       FileInputStream filein= new FileInputStream(fichero1);
       ObjectInputStream dataIS = new ObjectInputStream(filein);
       
      //2º.a) Serializamos cada atributo del objeto "Persona" 
     //  p2.setNombre(dataIS.readUTF());
     //  p2.setEdad(dataIS.readInt());
       
      //2º.b) Serializamos el objeto "Persona" en un solo paso
       
       try{
       p2=(Persona)dataIS.readObject(); //El objeto "p" debe ser instancia de una
                              //clase que implemente la interfaz 'Serializable'
       }catch(ClassNotFoundException cnfe){}
       
       
       dataIS.close();
  
       p2.mostrarPersona();
       
   }
    
   private class Persona implements Serializable{
    
       private String nombre;
       private int edad;
       
       public Persona(String nombre, int edad){
           this.nombre=nombre;
           this.edad=edad; 
       }
       
       public Persona(){
           this.nombre=null;
           this.edad=0;
       }
       
       public void setNombre(String nombre){
           this.nombre=nombre;
       }
       public void setEdad(int edad){
           this.edad=edad;
       }
       
       public String getNombre(){
           return nombre;
       }
       public int getEdad(){
           return edad;
       }
       
       public void mostrarPersona(){
           System.out.println(getNombre()+","+getEdad());
       }
    
    
   }
   
   
   
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
       SerializarPersona sp= new SerializarPersona();
        

        
        
    }
}






