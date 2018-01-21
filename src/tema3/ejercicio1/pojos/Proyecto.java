/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema3.ejercicio1.pojos;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author Perig
 * 
 *  
 */
public class Proyecto {
    /* 
    -- sql -- 
    
    create table proyecto (
        id int primary key auto_increment,
        nombre varchar(20),
        descripcion varchar(200),
        fechaInicio date,
        fechaFin date,
        departamento int foreign key references departamento(id),
        idResponsable int foreign key references empleado(id)
    );
    
    create table proy_emp (
        proyId int foreign key references proyecto(id),
        empId int foreign key references empleado(id),
        constraint pk_proy_emp primary key (proyId, empId)
    );
    */
    int id;
    String nombre;
    String descripcion;
    Date fechaInicio;
    Date fechaFin;
    Departamento departamento;
    Empleado responsable;
    Set<Empleado> empleados;

    public Proyecto() {
    }

    public Proyecto(String nombre, String descripcion, Date fechaInicio, Date fechaFin, Departamento departamento, Empleado responsable, Set<Empleado> empleados) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.departamento = departamento;
        this.responsable = responsable;
        this.empleados = empleados;
    }

    public Proyecto(String nombre, String descripcion, Date fechaInicio, Date fechaFin, Departamento departamento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.departamento = departamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Set<Empleado> empleados) {
        this.empleados = empleados;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }
    
    
    
    
}
