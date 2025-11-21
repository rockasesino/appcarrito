package com.example.carritodecompras.models;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private String imagenUrl; // nombre del recurso local (ej: "audifonos")

    public Producto(int id, String nombre, double precio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    public Producto(String nombre, double precio, String imagenUrl) {
        this(-1, nombre, precio, imagenUrl);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
