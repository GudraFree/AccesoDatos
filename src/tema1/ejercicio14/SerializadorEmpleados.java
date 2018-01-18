/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio14;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Perig
 */
public class SerializadorEmpleados {
    static class Empleado implements Serializable {
        String nombre;
        String departamento;
        public Empleado() {
        }

        public Empleado(String nombre, String departamento) {
            this.nombre = nombre;
            this.departamento = departamento;
        }

        public String getDepartamento() {
            return departamento;
        }

        public String getNombre() {
            return nombre;
        }

        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        public void mostrarEmpleado() {
            System.out.println("Empleado "+nombre+", departamento "+departamento);
        }
    }
    
    static File fichero;
    static ObjectOutputStream oos;
    static ObjectInputStream ois;
    
    public static void main(String[] args) {
        fichero = new File("empleados.dat");
        try {
            if(!fichero.exists()) fichero.createNewFile();
            oos = new ObjectOutputStream(new FileOutputStream(fichero));
        } catch (IOException e) {
            System.out.println("Error, archivo no encontrado\n"+e.getMessage());
        }
        Empleado empEntrada = new Empleado("Pepe Perez", "IT"), empSalida;
        serializarPorAtributos(empEntrada);
//        serializarPorObjeto(empEntrada);
        try {
            oos.close();
            ois = new ObjectInputStream(new FileInputStream(fichero));
        } catch (IOException e) {
            System.out.println("Error, archivo no encontrado\n"+e.getMessage());
        }
        empSalida = deserializarPorAtributos();
//        empSalida = deserializarPorObjeto();
        empSalida.mostrarEmpleado();
        
        try {
            ois.close();
        } catch (IOException e) {
            System.out.println("Error, archivo no encontrado\n"+e.getMessage());
        }
    }
    
    static void serializarPorAtributos(Empleado emp) {
        try {
            oos.writeUTF(emp.getNombre());
            oos.writeUTF(emp.getDepartamento());
        } catch (IOException e) {
            System.out.println("Error de escritura\n"+e.getMessage());
        }
    }
    static void serializarPorObjeto(Empleado emp) {
        try {
            oos.writeObject(emp);
        } catch (IOException e) {
            System.out.println("Error de escritura\n"+e.getMessage());
        }
    }
    static Empleado deserializarPorAtributos() {
        Empleado emp = new Empleado();
        try {
            emp.setNombre(ois.readUTF());
            emp.setDepartamento(ois.readUTF());
        } catch (IOException e) {
            System.out.println("Error de lectura\n");
            e.printStackTrace();
        }
        return emp;
    }
    static Empleado deserializarPorObjeto() {
        Empleado emp = null;
        try {
            emp = (Empleado)ois.readObject();
        } catch (IOException e) {
            System.out.println("Error de escritura\n"+e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerializadorEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emp;
    }
}
