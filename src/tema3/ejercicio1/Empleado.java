package tema3.ejercicio1;
// Generated 08-ene-2018 9:40:03 by Hibernate Tools 4.3.1



/**
 * Empleado generated by hbm2java
 */
public class Empleado  implements java.io.Serializable {


     private Integer id;
//     private Departamento departamento;
     private String nombre;
     private String apellido;
     private Float salario;

    public Empleado() {
    }

    public Empleado(/*Departamento departamento, */String nombre, String apellido, Float salario) {
//       this.departamento = departamento;
       this.nombre = nombre;
       this.apellido = apellido;
       this.salario = salario;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
//    public Departamento getDepartamento() {
//        return this.departamento;
//    }
//    
//    public void setDepartamento(Departamento departamento) {
//        this.departamento = departamento;
//    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return this.apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public Float getSalario() {
        return this.salario;
    }
    
    public void setSalario(Float salario) {
        this.salario = salario;
    }




}


