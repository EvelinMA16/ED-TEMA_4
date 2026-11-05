package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class grafo {

    private List<vertice> vertices;

    // Constructor
    public grafo() {
        vertices = new ArrayList<>();
    }

    // Check if the graph is empty
    public boolean estaVacio() {
        return vertices.isEmpty();
    }

    // Get all vertices
    public List<vertice> getVertices() {
        return vertices;
    }

    // Add a vertex
    public boolean agregarVertice(String nombre) {

        if (buscarVertice(nombre) != null) {
            return false;
        }

        vertices.add(new vertice(nombre));
        return true;
    }

    // Find a vertex
    public vertice buscarVertice(String nombre) {

        for (vertice v : vertices) {

            if (v.getNombre().equalsIgnoreCase(nombre)) {
                return v;
            }
        }

        return null;
    }

    // Add a connection between two vertices
    public boolean agregarConexion(String nombre1, String nombre2) {

        vertice vertice1 = buscarVertice(nombre1);
        vertice vertice2 = buscarVertice(nombre2);

        if (vertice1 == null || vertice2 == null) {
            return false;
        }

        if (vertice1 == vertice2) {
            return false;
        }

        if (vertice1.getConexiones().contains(vertice2)) {
            return false;
        }

        vertice1.agregarConexion(vertice2);
        vertice2.agregarConexion(vertice1);

        return true;
    }

    // Breadth First Search (BFS)
    public List<String> recorridoAnchura(String inicio) {

        List<String> recorrido = new ArrayList<>();

        Queue<vertice> cola = new LinkedList<>();
        Set<vertice> visitados = new HashSet<>();

        vertice primero = buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        cola.add(primero);
        visitados.add(primero);

        // Visit vertices level by level
        while (!cola.isEmpty()) {

            vertice actual = cola.poll();

            recorrido.add(actual.getNombre());

            for (vertice vecino : actual.getConexiones()) {

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

        Set<vertice> visitados = new HashSet<>();

        vertice primero = buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        dfs(primero, visitados, recorrido);

        return recorrido;
    }

    // Recursive method for DFS
    private void dfs(
            vertice actual,
            Set<vertice> visitados,
            List<String> recorrido) {

        visitados.add(actual);
        recorrido.add(actual.getNombre());

        for (vertice vecino : actual.getConexiones()) {

            if (!visitados.contains(vecino)) {

                dfs(vecino, visitados, recorrido);
            }
        }
    }

    // Clear the graph
    public void vaciar() {
        vertices.clear();
    }
}