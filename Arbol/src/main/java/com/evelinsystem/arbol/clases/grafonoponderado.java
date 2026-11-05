package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class grafonoponderado {

    private final List<Vertice> vertices;

    public grafonoponderado() {
        vertices = new ArrayList<>();
    }

    // Represents a vertex and its connections
    public static class Vertice {
        private final String nombre;
        private final List<Vertice> conexiones;

        public Vertice(String nombre) {
            this.nombre = nombre;
            conexiones = new ArrayList<>();
        }

        public String getNombre() {
            return nombre;
        }

        public List<Vertice> getConexiones() {
            return conexiones;
        }
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public boolean estaVacio() {
        return vertices.isEmpty();
    }

    // Adds a new vertex
    public boolean agregarVertice(String nombre) {
        if (buscarVertice(nombre) != null) {
            return false;
        }

        vertices.add(new Vertice(nombre));
        return true;
    }

    // Searches for a vertex
    public Vertice buscarVertice(String nombre) {
        for (Vertice vertice : vertices) {
            if (vertice.getNombre().equalsIgnoreCase(nombre)) {
                return vertice;
            }
        }
        return null;
    }

    // Adds an undirected connection without weight
    public boolean agregarConexion(String origen, String destino) {
        Vertice v1 = buscarVertice(origen);
        Vertice v2 = buscarVertice(destino);

        if (v1 == null || v2 == null || v1 == v2) {
            return false;
        }

        if (v1.getConexiones().contains(v2)) {
            return false;
        }

        v1.getConexiones().add(v2);
        v2.getConexiones().add(v1);

        return true;
    }

    // Performs Breadth-First Search
    public List<String> recorridoAnchura(String inicio) {
        List<String> recorrido = new ArrayList<>();
        Vertice primero = buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        Queue<Vertice> cola = new LinkedList<>();
        Set<Vertice> visitados = new HashSet<>();

        cola.add(primero);
        visitados.add(primero);

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

    // Performs Depth-First Search
    public List<String> recorridoProfundidad(String inicio) {
        List<String> recorrido = new ArrayList<>();
        Vertice primero = buscarVertice(inicio);

        if (primero != null) {
            Set<Vertice> visitados = new HashSet<>();
            dfs(primero, visitados, recorrido);
        }

        return recorrido;
    }

    // Recursive method for DFS
    private void dfs(Vertice actual, Set<Vertice> visitados,
                     List<String> recorrido) {

        visitados.add(actual);
        recorrido.add(actual.getNombre());

        for (Vertice vecino : actual.getConexiones()) {
            if (!visitados.contains(vecino)) {
                dfs(vecino, visitados, recorrido);
            }
        }
    }

    // Removes all vertices and connections
    public void vaciar() {
        vertices.clear();
    }
}