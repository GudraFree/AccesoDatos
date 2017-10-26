/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio19;

import Tema1.ejercicio15.*;

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
    int id;
    String nombre;
    String apellidos;
    String departamento;
    float salario;
    
    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellidos, String departamento, float salario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
        this.salario = salario;
    }

    
    
    public void mostrarEmpleado() {
        System.out.println("\nEmpleado #"+id+"\n\tNombre: "+apellidos+", "+nombre+"\n\tDepartamento: "+departamento);
    }

    public String getApellidos() {
        return apellidos;
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

    public float getSalario() {
        return salario;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }
    
}
