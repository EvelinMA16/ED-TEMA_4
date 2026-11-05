package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class grafodirigido {

    private final List<Vertice> vertices;

    // Create an empty directed graph
    public grafodirigido() {
        vertices = new ArrayList<>();
    }

    // Inner class to represent a vertex
    public static class Vertice {

        private String nombre;
        private final List<Vertice> conexiones;

        // Create a vertex
        public Vertice(String nombre) {
            this.nombre = nombre;
            conexiones = new ArrayList<>();
        }

        // Return the vertex name
        public String getNombre() {
            return nombre;
        }

        // Return outgoing connections
        public List<Vertice> getConexiones() {
            return conexiones;
        }
    }

    // Add a new vertex
    public boolean agregarVertice(String nombre) {

        if (buscarVertice(nombre) != null) {
            return false;
        }

        vertices.add(new Vertice(nombre));
        return true;
    }

    // Search for a vertex
    public Vertice buscarVertice(String nombre) {

        for (Vertice v : vertices) {
            if (v.getNombre().equalsIgnoreCase(nombre)) {
                return v;
            }
        }

        return null;
    }

    // Add a directed connection
    public boolean agregarConexion(String origen, String destino) {

        Vertice v1 = buscarVertice(origen);
        Vertice v2 = buscarVertice(destino);

        if (v1 == null || v2 == null) {
            return false;
        }

        if (v1.getConexiones().contains(v2)) {
            return false;
        }

        // Store the connection only in the origin vertex
        v1.getConexiones().add(v2);

        return true;
    }

    // Return all vertices
    public List<Vertice> getVertices() {
        return vertices;
    }

    // Check if the graph is empty
    public boolean estaVacio() {
        return vertices.isEmpty();
    }

    // Remove all vertices
    public void vaciar() {
        vertices.clear();
    }
    // Breadth First Search (BFS)
    public List<String> recorridoAnchura(String inicio) {

        List<String> recorrido = new ArrayList<>();
        Queue<Vertice> cola = new LinkedList<>();
        Set<Vertice> visitados = new HashSet<>();

        Vertice primero = buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        cola.add(primero);
        visitados.add(primero);

        // Visit vertices level by level
        while (!cola.isEmpty()) {

            Vertice actual = cola.poll();
            recorrido.add(actual.getNombre());

            for (Vertice vecino : actual.getConexiones()) {

                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        return recorrido;
    }

    // Depth First Search (DFS)
    public List<String> recorridoProfundidad(String inicio) {

        List<String> recorrido = new ArrayList<>();
        Set<Vertice> visitados = new HashSet<>();

        Vertice primero = buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        dfs(primero, visitados, recorrido);

        return recorrido;
    }

    // Recursive method for DFS
    private void dfs(
            Vertice actual,
            Set<Vertice> visitados,
            List<String> recorrido) {

        visitados.add(actual);
        recorrido.add(actual.getNombre());

        for (Vertice vecino : actual.getConexiones()) {

            if (!visitados.contains(vecino)) {
                dfs(vecino, visitados, recorrido);
            }
        }
    }
}