/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema1.ejercicio19;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Perig
 */
public class Empleados {
    List<Empleado> listaEmpleados = new ArrayList();

    public Empleados() {
    }
    
    void add (Empleado e) {
        listaEmpleados.add(e);
    }
    
    void remove(Empleado e) {
        listaEmpleados.remove(e);
    }
    
    List getContent() {
        return listaEmpleados;
    }
    
    void mostrarEmpleados() {
        for(Empleado em : listaEmpleados) {
            em.mostrarEmpleado();
        }
    }
    
    void mostrarEmpleados(int idABuscar) {
        for(Empleado em : listaEmpleados) {
            if (em.getId()==idABuscar) em.mostrarEmpleado();
        }
    }
    
    Empleado getEmpleado(int idABuscar) {
        for(Empleado em : listaEmpleados) {
            if (em.getId()==idABuscar) return em;
        }
        return null;
    }
    
    int idMasAlto() {
        int id = 0;
        for (Empleado em : listaEmpleados) {
            if(em.getId()>=id) id = em.getId() +1;
        }
        return id;
    }
}
