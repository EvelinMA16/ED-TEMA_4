package com.evelinsystem.arbol.clases;

public class Nodo {

    private int valor;
    private Nodo izquierdo;
    private Nodo derecho;

    // Constructor con parametro
    public Nodo(int valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
    }

    // Get node value
    public int getValor() {
        return valor;
    }

    // Get left child
    public Nodo getIzquierdo() {
        return izquierdo;
    }

    // Get right child
    public Nodo getDerecho() {
        return derecho;
    }

    // Set left child
    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    // Set right child
    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }
}