/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examenes.trimestre1.ex1.ticketsDeVenta;

/**
 *
 * @author Perig
 */
public class Producto {
    int codigo;
    String tipo;
    String marca;
    int cantidad;
    float precio;

    public Producto() {
    }

    public Producto(int codigo, String tipo, String marca, int cantidad, float precio) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.marca = marca;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public float getPrecio() {
        return precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
