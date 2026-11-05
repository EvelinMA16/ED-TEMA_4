package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.arbolgeneral;
import com.evelinsystem.arbol.clases.arbolgeneral.NodoGeneral;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ArbolGeneralController {

    @FXML
    private TextField txtRaiz;

    @FXML
    private TextField txtPadre;

    @FXML
    private TextField txtHijo;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtResultado;

    @FXML
    private Pane paneArbol;

    @FXML
    private Button btnRegresar;

    private final arbolgeneral arbol = new arbolgeneral();

    // Shows error messages in alert windows
    private void mostrarError(
            String titulo,
            String mensaje) {

        Alert alerta =
                new Alert(Alert.AlertType.WARNING);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    // Shows information messages in alert windows
    private void mostrarInformacion(
            String titulo,
            String mensaje) {

        Alert alerta =
                new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    // Inserts the root
    @FXML
    private void insertarRaiz() {

        String texto =
                txtRaiz.getText().trim();

        // Validate empty field
        if (texto.isEmpty()) {

            mostrarError(
                    "Dato requerido",
                    "Ingrese un valor para la raíz."
            );

            return;
        }

        int valor;

        // Validate integer
        try {

            valor =
                    Integer.parseInt(texto);

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "Ingrese un número entero válido."
            );

            return;
        }

        // Validate existing root
        if (!arbol.insertarRaiz(valor)) {

            mostrarError(
                    "Raíz existente",
                    "El árbol ya tiene una raíz."
            );

            return;
        }

        // Clear the input
        txtRaiz.clear();

        // Draw the tree
        dibujarArbol();
    }

    // Inserts a child under a parent
    @FXML
    private void insertarHijo() {

        String textoPadre =
                txtPadre.getText().trim();

        String textoHijo =
                txtHijo.getText().trim();

        // Validate empty fields
        if (textoPadre.isEmpty()
                || textoHijo.isEmpty()) {

            mostrarError(
                    "Dato requerido",
                    "Ingrese el padre y el hijo."
            );

            return;
        }

        int padre;
        int hijo;

        // Validate integer values
        try {

            padre =
                    Integer.parseInt(textoPadre);

            hijo =
                    Integer.parseInt(textoHijo);

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "Ingrese números enteros válidos."
            );

            return;
        }

        // Validate empty tree
        if (arbol.estaVacio()) {

            mostrarError(
                    "Árbol vacío",
                    "Primero debe insertar la raíz."
            );

            return;
        }

        // Validate parent
        if (arbol.buscar(padre) == null) {

            mostrarError(
                    "Padre inexistente",
                    "El nodo padre no existe en el árbol."
            );

            return;
        }

        // Validate duplicate child
        if (arbol.buscar(hijo) != null) {

            mostrarError(
                    "Dato duplicado",
                    "Ese valor ya existe en el árbol."
            );

            return;
        }

        // Insert the child
        arbol.insertarHijo(
                padre,
                hijo
        );

        // Clear the input fields
        txtPadre.clear();
        txtHijo.clear();

        // Redraw the tree
        dibujarArbol();
    }

    // Searches for a node
    @FXML
    private void buscarNodo() {

        String texto =
                txtBuscar.getText().trim();

        // Validate empty field
        if (texto.isEmpty()) {

            mostrarError(
                    "Dato requerido",
                    "Ingrese un número para buscar."
            );

            return;
        }

        int valor;

        // Validate integer
        try {

            valor =
                    Integer.parseInt(texto);

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "Ingrese un número entero válido."
            );

            return;
        }

        // Validate empty tree
        if (arbol.estaVacio()) {

            mostrarError(
                    "Árbol vacío",
                    "No hay elementos para buscar."
            );

            return;
        }

        // Search for the node
        NodoGeneral encontrado =
                arbol.buscar(valor);

        if (encontrado != null) {

            // Show message when the node is found
            mostrarInformacion(
                    "Búsqueda",
                    "El número " + valor +
                            " sí se encuentra en el árbol."
            );

        } else {

            // Show message when the node is not found
            mostrarError(
                    "Nodo no encontrado",
                    "El número " + valor +
                            " no se encuentra en el árbol."
            );
        }

        // Clear the search field
        txtBuscar.clear();
    }

    // Shows the preorder traversal
    @FXML
    private void mostrarRecorrido() {

        // Validate empty tree
        if (arbol.estaVacio()) {

            mostrarError(
                    "Árbol vacío",
                    "No hay elementos para recorrer."
            );

            return;
        }

        List<Integer> recorrido =
                new ArrayList<>();

        // Generate preorder traversal
        arbol.recorridoPreorden(
                arbol.getRaiz(),
                recorrido
        );

        // Show the traversal in the text field
        txtResultado.setText(
                "Preorden: " + recorrido
        );
    }

    // Clears the tree
    @FXML
    private void limpiarArbol() {

        // Validate empty tree
        if (arbol.estaVacio()) {

            mostrarError(
                    "Árbol vacío",
                    "El árbol ya está vacío."
            );

            return;
        }

        // Clear the tree
        arbol.vaciar();

        // Clear the drawing
        paneArbol.getChildren().clear();

        // Clear the result
        txtResultado.clear();
    }

    // Draws the complete tree
    private void dibujarArbol() {

        paneArbol.getChildren().clear();

        if (arbol.estaVacio()) {
            return;
        }

        // Start drawing from the root
        dibujarNodo(
                arbol.getRaiz(),
                325,
                40,
                0,
                650
        );
    }

    // Draws a node and its children recursively
    private void dibujarNodo(
            NodoGeneral nodo,
            double x,
            double y,
            double xInicio,
            double xFin) {

        List<NodoGeneral> hijos =
                nodo.getHijos();

        // Draw connections to children
        if (!hijos.isEmpty()) {

            double espacio =
                    (xFin - xInicio)
                            / hijos.size();

            for (int i = 0;
                 i < hijos.size();
                 i++) {

                NodoGeneral hijo =
                        hijos.get(i);

                double hijoX =
                        xInicio
                                + espacio * i
                                + espacio / 2;

                double hijoY =
                        y + 90;

                // Draw the connection line
                Line linea =
                        new Line(
                                x,
                                y + 15,
                                hijoX,
                                hijoY - 15
                        );

                linea.setStroke(
                        Color.BLACK
                );

                paneArbol.getChildren()
                        .add(linea);

                // Draw the child recursively
                dibujarNodo(
                        hijo,
                        hijoX,
                        hijoY,
                        xInicio
                                + espacio * i,
                        xInicio
                                + espacio * (i + 1)
                );
            }
        }

        // Draw the node
        Circle circulo =
                new Circle(
                        x,
                        y,
                        20
                );

        circulo.setFill(
                Color.LIGHTBLUE
        );

        circulo.setStroke(
                Color.DARKBLUE
        );

        // Draw the node value
        Text texto =
                new Text(
                        x - 5,
                        y + 5,
                        String.valueOf(
                                nodo.getValor()
                        )
                );

        paneArbol.getChildren()
                .add(circulo);

        paneArbol.getChildren()
                .add(texto);
    }

    // Returns to the tree menu
    @FXML
    private void regresar() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/evelinsystem/arbol/menuarbol-view.fxml"
                            )
                    );

            Scene scene =
                    new Scene(
                            loader.load()
                    );

            Stage stage =
                    (Stage) btnRegresar
                            .getScene()
                            .getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo regresar al menú."
            );
        }
    }
}