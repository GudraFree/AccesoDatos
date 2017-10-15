/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio15;

/**
 *
 * @author Perig
 */
public class Empleado {
    byte control;
    int id;
    String nombre;
    String apellidos;
    String departamento;

    public Empleado() {
    }

    public Empleado(byte control, int id, String nombre, String apellidos, String departamento) {
        this.control = control;
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
    }
    
    public void mostrarEmpleado() {
        System.out.println("\nEmpleado #"+id+"\n\tNombre: "+apellidos+", "+nombre+"\n\tDepartamento: "+departamento);
    }

    public String getApellidos() {
        return apellidos;
    }

    public byte getControl() {
        return control;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getId() {
        return id;
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

    public void setControl(byte control) {
        this.control = control;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
