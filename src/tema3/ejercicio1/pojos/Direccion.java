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
public class Direccion {
    private int id;
    private String calle;
    private int numero;
    private String cpostal;
    private String provincia;

    public Direccion() {
    }

    public Direccion(String calle, int numero, String cpostal, String provincia) {
        this.calle = calle;
        this.numero = numero;
        this.cpostal = cpostal;
        this.provincia = provincia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCpostal() {
        return cpostal;
    }

    public void setCpostal(String cpostal) {
        this.cpostal = cpostal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public void mostrarDireccion() {
        System.out.println("Calle "+calle+" nº"+numero+", "+provincia+" - "+cpostal);
    }
}
