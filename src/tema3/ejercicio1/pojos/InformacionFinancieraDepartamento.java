/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema3.ejercicio1.pojos;

/**
 *
 * @author Perig
 */
public class InformacionFinancieraDepartamento {
    private int id;
    private Departamento departamento;
    private float presupuesto;
    private float ingresos;
    private float gastos;

    public InformacionFinancieraDepartamento(Departamento departamento, float presupuesto, float ingresos, float gastos) {
        this.departamento = departamento;
        this.presupuesto = presupuesto;
        this.ingresos = ingresos;
        this.gastos = gastos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public float getGastos() {
        return gastos;
    }

    public void setIngresos(float ingresos) {
        this.ingresos = ingresos;
    }

    public float getIngresos() {
        return ingresos;
    }

    public void setPresupuesto(float presupuesto) {
        this.presupuesto = presupuesto;
    }

    public float getPresupuesto() {
        return presupuesto;
    }
    
    
}
