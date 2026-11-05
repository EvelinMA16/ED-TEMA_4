package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;

public class vertice {

    private String nombre;
    private List<vertice> conexiones;

    // Constructor
    public vertice(String nombre) {
        this.nombre = nombre;
        this.conexiones = new ArrayList<>();
    }

    // Get vertex name
    public String getNombre() {
        return nombre;
    }

    // Get connections
    public List<vertice> getConexiones() {
        return conexiones;
    }

    // Add a connection
    public void agregarConexion(vertice vertice) {
        if (!conexiones.contains(vertice)) {
            conexiones.add(vertice);
        }
    }
}