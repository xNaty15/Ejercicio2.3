package com.example.ejercicio23.modelo;

public class Fotografía {

    private byte[] image;
    private String descripcion;

    public Fotografía(byte[] image, String description) {
        this.image = image;
        this.descripcion = description;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
