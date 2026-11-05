package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;

public class arbolbinario {

    private Nodo raiz;

    // Check if the tree is empty
    public boolean estaVacio() {
        return raiz == null;
    }

    // Get the root
    public Nodo getRaiz() {
        return raiz;
    }

    // Insert a value
    public boolean insertar(int valor) {

        if (raiz == null) {
            raiz = new Nodo(valor);
            return true;
        }

        return insertarRecursivo(raiz, valor);
    }

    // Insert recursively
    private boolean insertarRecursivo(Nodo nodo, int valor) {

        if (valor == nodo.getValor()) {
            return false;
        }

        if (valor < nodo.getValor()) {

            if (nodo.getIzquierdo() == null) {
                nodo.setIzquierdo(new Nodo(valor));
                return true;
            }

            return insertarRecursivo(nodo.getIzquierdo(), valor);

        } else {

            if (nodo.getDerecho() == null) {
                nodo.setDerecho(new Nodo(valor));
                return true;
            }

            return insertarRecursivo(nodo.getDerecho(), valor);
        }
    }

    // Search for a value
    public boolean buscar(int valor) {
        return buscarRecursivo(raiz, valor);
    }

    // Search recursively
    private boolean buscarRecursivo(Nodo nodo, int valor) {

        if (nodo == null) {
            return false;
        }

        if (valor == nodo.getValor()) {
            return true;
        }

        if (valor < nodo.getValor()) {
            return buscarRecursivo(nodo.getIzquierdo(), valor);
        }

        return buscarRecursivo(nodo.getDerecho(), valor);
    }
    // In-order traversal
    public String inOrden() {
        List<Integer> valores = new ArrayList<>();
        inOrdenRecursivo(raiz, valores);
        return valores.toString();
    }
    private void inOrdenRecursivo(Nodo nodo, List<Integer> valores) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.getIzquierdo(), valores);
            valores.add(nodo.getValor());
            inOrdenRecursivo(nodo.getDerecho(), valores);
        }
    }
    // Pre-order traversal
    public String preOrden() {
        List<Integer> valores = new ArrayList<>();
        preOrdenRecursivo(raiz, valores);
        return valores.toString();
    }
    private void preOrdenRecursivo(Nodo nodo, List<Integer> valores) {
        if (nodo != null) {
            valores.add(nodo.getValor());
            preOrdenRecursivo(nodo.getIzquierdo(), valores);
            preOrdenRecursivo(nodo.getDerecho(), valores);
        }
    }
    // Post-order traversal
    public String postOrden() {
        List<Integer> valores = new ArrayList<>();
        postOrdenRecursivo(raiz, valores);
        return valores.toString();
    }

    private void postOrdenRecursivo(Nodo nodo, List<Integer> valores) {

        if (nodo != null) {

            postOrdenRecursivo(nodo.getIzquierdo(), valores);

            postOrdenRecursivo(nodo.getDerecho(), valores);

            valores.add(nodo.getValor());
        }
    }

    // Clear the tree
    public void vaciar() {
        raiz = null;
    }
}