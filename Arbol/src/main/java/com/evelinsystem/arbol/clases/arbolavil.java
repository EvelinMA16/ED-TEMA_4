package com.evelinsystem.arbol.clases;

import java.util.ArrayList;
import java.util.List;

public class arbolavil {

    private Nodo raiz;

    // AVL node
    public static class Nodo {

        private int valor;
        private int altura;
        private Nodo izquierdo;
        private Nodo derecho;

        public Nodo(int valor) {
            this.valor = valor;
            this.altura = 1;
        }

        public int getValor() {
            return valor;
        }

        public int getAltura() {
            return altura;
        }

        public Nodo getIzquierdo() {
            return izquierdo;
        }

        public Nodo getDerecho() {
            return derecho;
        }
    }

    // Check if the tree is empty
    public boolean estaVacio() {
        return raiz == null;
    }

    // Get the root
    public Nodo getRaiz() {
        return raiz;
    }

    // Get the height of a node
    private int altura(Nodo nodo) {

        if (nodo == null) {
            return 0;
        }

        return nodo.altura;
    }

    // Get the balance factor
    private int factorBalance(Nodo nodo) {

        if (nodo == null) {
            return 0;
        }

        return altura(nodo.izquierdo)
                - altura(nodo.derecho);
    }

    // Get the maximum value
    private int max(int a, int b) {
        return Math.max(a, b);
    }

    // Insert a value into the AVL tree
    public boolean insertar(int valor) {

        if (buscar(valor)) {
            return false;
        }

        raiz = insertarRecursivo(
                raiz,
                valor
        );

        return true;
    }

    // Insert recursively and balance the tree
    private Nodo insertarRecursivo(
            Nodo nodo,
            int valor) {

        // Create a new node
        if (nodo == null) {
            return new Nodo(valor);
        }

        // Insert into the left subtree
        if (valor < nodo.valor) {

            nodo.izquierdo =
                    insertarRecursivo(
                            nodo.izquierdo,
                            valor
                    );

        } else {

            // Insert into the right subtree
            nodo.derecho =
                    insertarRecursivo(
                            nodo.derecho,
                            valor
                    );
        }

        // Update node height
        nodo.altura =
                1 + max(
                        altura(nodo.izquierdo),
                        altura(nodo.derecho)
                );

        // Get balance factor
        int balance =
                factorBalance(nodo);

        // Left Left case
        if (balance > 1
                && valor < nodo.izquierdo.valor) {

            return rotacionDerecha(nodo);
        }

        // Right Right case
        if (balance < -1
                && valor > nodo.derecho.valor) {

            return rotacionIzquierda(nodo);
        }

        // Left Right case
        if (balance > 1
                && valor > nodo.izquierdo.valor) {

            nodo.izquierdo =
                    rotacionIzquierda(
                            nodo.izquierdo
                    );

            return rotacionDerecha(nodo);
        }

        // Right Left case
        if (balance < -1
                && valor < nodo.derecho.valor) {

            nodo.derecho =
                    rotacionDerecha(
                            nodo.derecho
                    );

            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Right rotation
    private Nodo rotacionDerecha(
            Nodo y) {

        Nodo x = y.izquierdo;
        Nodo temporal = x.derecho;

        x.derecho = y;
        y.izquierdo = temporal;

        y.altura =
                1 + max(
                        altura(y.izquierdo),
                        altura(y.derecho)
                );

        x.altura =
                1 + max(
                        altura(x.izquierdo),
                        altura(x.derecho)
                );

        return x;
    }

    // Left rotation
    private Nodo rotacionIzquierda(
            Nodo x) {

        Nodo y = x.derecho;
        Nodo temporal = y.izquierdo;

        y.izquierdo = x;
        x.derecho = temporal;

        x.altura =
                1 + max(
                        altura(x.izquierdo),
                        altura(x.derecho)
                );

        y.altura =
                1 + max(
                        altura(y.izquierdo),
                        altura(y.derecho)
                );

        return y;
    }

    // Search for a value
    public boolean buscar(int valor) {

        return buscarRecursivo(
                raiz,
                valor
        );
    }

    // Search recursively
    private boolean buscarRecursivo(
            Nodo nodo,
            int valor) {

        if (nodo == null) {
            return false;
        }

        if (valor == nodo.valor) {
            return true;
        }

        if (valor < nodo.valor) {

            return buscarRecursivo(
                    nodo.izquierdo,
                    valor
            );
        }

        return buscarRecursivo(
                nodo.derecho,
                valor
        );
    }

    // In-order traversal
    public String inOrden() {

        List<Integer> valores =
                new ArrayList<>();

        inOrdenRecursivo(
                raiz,
                valores
        );

        return valores.toString();
    }

    private void inOrdenRecursivo(
            Nodo nodo,
            List<Integer> valores) {

        if (nodo != null) {

            inOrdenRecursivo(
                    nodo.izquierdo,
                    valores
            );

            valores.add(
                    nodo.valor
            );

            inOrdenRecursivo(
                    nodo.derecho,
                    valores
            );
        }
    }

    // Pre-order traversal
    public String preOrden() {

        List<Integer> valores =
                new ArrayList<>();

        preOrdenRecursivo(
                raiz,
                valores
        );

        return valores.toString();
    }

    private void preOrdenRecursivo(
            Nodo nodo,
            List<Integer> valores) {

        if (nodo != null) {

            valores.add(
                    nodo.valor
            );

            preOrdenRecursivo(
                    nodo.izquierdo,
                    valores
            );

            preOrdenRecursivo(
                    nodo.derecho,
                    valores
            );
        }
    }

    // Post-order traversal
    public String postOrden() {

        List<Integer> valores =
                new ArrayList<>();

        postOrdenRecursivo(
                raiz,
                valores
        );

        return valores.toString();
    }

    private void postOrdenRecursivo(
            Nodo nodo,
            List<Integer> valores) {

        if (nodo != null) {

            postOrdenRecursivo(
                    nodo.izquierdo,
                    valores
            );

            postOrdenRecursivo(
                    nodo.derecho,
                    valores
            );

            valores.add(
                    nodo.valor
            );
        }
    }

    // Clear the tree
    public void vaciar() {
        raiz = null;
    }
}