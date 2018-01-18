/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema1.ejercicio19;

import java.util.Scanner;

/**
 *
 * @author Perig
 * 
 * Empleado en xml
 * <empleado id="id">
 *      <nombre>Nombre</nombre>
 *      <apell>Apellidos</apell>
 *      <depart>Departamento</depart>
 * </empleado>
 */
public class Empleado {
    ID id;
    String nombre;
    String apellidos;
    String departamento;
    
    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellidos, String departamento) {
        this.id = new ID(id);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
    }

    
    
    public void mostrarEmpleado() {
        System.out.println("\nEmpleado #"+id.getId()+"\n\tNombre: "+apellidos+", "+nombre+"\n\tDepartamento: "+departamento);
    }
    
    public static Empleado pedirEmpleado(int id) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca nombre empleado");
        String nombre = sc.nextLine();
        System.out.println("Introduzca apellidos empleado");
        String apellidos = sc.nextLine();
        System.out.println("Introduzca departamento empleado");
        String departamento = sc.nextLine();
        return new Empleado(id,nombre,apellidos,departamento);
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getId() {
        return id.getId();
    }

    public String getNombre() {
        return nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setId(int id) {
        this.id = new ID(id);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
