package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;

public class arbolgeneral {

    private NodoGeneral raiz;

    // General tree node
    public static class NodoGeneral {

        private int valor;
        private NodoGeneral padre;
        private List<NodoGeneral> hijos;

        public NodoGeneral(int valor) {
            this.valor = valor;
            this.hijos = new ArrayList<>();
        }

        public int getValor() {
            return valor;
        }

        public NodoGeneral getPadre() {
            return padre;
        }

        public List<NodoGeneral> getHijos() {
            return hijos;
        }

        // Adds a child to this node
        public void agregarHijo(NodoGeneral hijo) {
            hijo.padre = this;
            hijos.add(hijo);
        }
    }

    // Checks if the tree is empty
    public boolean estaVacio() {
        return raiz == null;
    }

    // Returns the root
    public NodoGeneral getRaiz() {
        return raiz;
    }

    // Inserts the root
    public boolean insertarRaiz(int valor) {

        if (raiz != null) {
            return false;
        }

        raiz = new NodoGeneral(valor);

        return true;
    }

    // Searches for a value in the tree
    public NodoGeneral buscar(int valor) {

        if (raiz == null) {
            return null;
        }

        return buscarRecursivo(raiz, valor);
    }

    // Recursive search
    private NodoGeneral buscarRecursivo(
            NodoGeneral nodo,
            int valor) {

        if (nodo.getValor() == valor) {
            return nodo;
        }

        for (NodoGeneral hijo : nodo.getHijos()) {

            NodoGeneral encontrado =
                    buscarRecursivo(hijo, valor);

            if (encontrado != null) {
                return encontrado;
            }
        }

        return null;
    }

    // Inserts a child under a parent
    public boolean insertarHijo(
            int valorPadre,
            int valorHijo) {

        // Prevent duplicate values
        if (buscar(valorHijo) != null) {
            return false;
        }

        // Find the parent
        NodoGeneral padre = buscar(valorPadre);

        if (padre == null) {
            return false;
        }

        // Create the child
        NodoGeneral nuevoHijo =
                new NodoGeneral(valorHijo);

        // Add the child to the parent
        padre.agregarHijo(nuevoHijo);

        return true;
    }

    // Performs preorder traversal
    public void recorridoPreorden(
            NodoGeneral nodo,
            List<Integer> resultado) {

        if (nodo == null) {
            return;
        }

        // Visit the current node
        resultado.add(nodo.getValor());

        // Visit all children
        for (NodoGeneral hijo : nodo.getHijos()) {

            recorridoPreorden(
                    hijo,
                    resultado
            );
        }
    }

    // Clears the tree
    public void vaciar() {
        raiz = null;
    }
}