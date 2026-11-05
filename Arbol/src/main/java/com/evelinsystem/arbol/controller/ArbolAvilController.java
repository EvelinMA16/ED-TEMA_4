package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.arbolavil;
import com.evelinsystem.arbol.clases.arbolavil.Nodo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ArbolAvilController {

    @FXML
    private TextField txtValor;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Pane paneArbol;

    private final arbolavil arbol =
            new arbolavil();

    // Insert a value into the AVL tree
    @FXML
    private void insertar() {

        String texto =
                txtValor.getText().trim();

        if (texto.isEmpty()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese un número."
            );

            return;
        }

        int valor;

        try {

            valor = Integer.parseInt(texto);

        } catch (NumberFormatException e) {

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Dato inválido",
                    "Solo se permiten números enteros."
            );

            return;
        }

        if (valor < 0) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Número inválido",
                    "No se permiten números negativos."
            );

            return;
        }

        boolean insertado =
                arbol.insertar(valor);

        if (!insertado) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Número repetido",
                    "El número " + valor +
                            " ya existe en el árbol."
            );

            return;
        }

        // Draw the balanced tree
        dibujarArbol();

        txtValor.clear();
        txtValor.requestFocus();
    }

    // Search for a value
    @FXML
    private void buscar() {

        String texto =
                txtValor.getText().trim();

        if (texto.isEmpty()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese un número para buscar."
            );

            return;
        }

        int valor;

        try {

            valor = Integer.parseInt(texto);

        } catch (NumberFormatException e) {

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Dato inválido",
                    "Solo se permiten números enteros."
            );

            return;
        }

        if (valor < 0) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Número inválido",
                    "No se permiten números negativos."
            );

            return;
        }

        if (arbol.buscar(valor)) {

            txtResultado.setText(
                    "El número " + valor +
                            " sí existe en el árbol AVL."
            );

        } else {

            txtResultado.setText(
                    "El número " + valor +
                            " no existe en el árbol AVL."
            );
        }
    }

    // Show in-order traversal
    @FXML
    private void inOrden() {

        if (arbol.estaVacio()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Árbol vacío",
                    "El árbol está vacío."
            );

            return;
        }

        txtResultado.setText(
                "InOrden: " +
                        arbol.inOrden()
        );
    }

    // Show pre-order traversal
    @FXML
    private void preOrden() {

        if (arbol.estaVacio()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Árbol vacío",
                    "El árbol está vacío."
            );

            return;
        }

        txtResultado.setText(
                "PreOrden: " +
                        arbol.preOrden()
        );
    }

    // Show post-order traversal
    @FXML
    private void postOrden() {

        if (arbol.estaVacio()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Árbol vacío",
                    "El árbol está vacío."
            );

            return;
        }

        txtResultado.setText(
                "PostOrden: " +
                        arbol.postOrden()
        );
    }

    // Clear the tree
    @FXML
    private void vaciar() {

        if (arbol.estaVacio()) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Árbol vacío",
                    "El árbol ya está vacío."
            );

            return;
        }

        arbol.vaciar();

        paneArbol.getChildren().clear();

        txtResultado.clear();
    }

    // Draw the complete AVL tree
    private void dibujarArbol() {

        paneArbol.getChildren().clear();

        if (!arbol.estaVacio()) {

            dibujarNodo(
                    arbol.getRaiz(),
                    paneArbol.getWidth() / 2,
                    50,
                    250
            );
        }
    }

    // Draw each node and its connections
    private void dibujarNodo(
            Nodo nodo,
            double x,
            double y,
            double separacion) {

        if (nodo == null) {
            return;
        }

        // Draw the left child
        if (nodo.getIzquierdo() != null) {

            double xIzquierdo =
                    x - separacion;

            double yHijo =
                    y + 80;

            Line linea = new Line(
                    x,
                    y,
                    xIzquierdo,
                    yHijo
            );

            linea.setStroke(
                    Color.BLACK
            );

            paneArbol.getChildren().add(
                    linea
            );

            dibujarNodo(
                    nodo.getIzquierdo(),
                    xIzquierdo,
                    yHijo,
                    separacion / 2
            );
        }

        // Draw the right child
        if (nodo.getDerecho() != null) {

            double xDerecho =
                    x + separacion;

            double yHijo =
                    y + 80;

            Line linea = new Line(
                    x,
                    y,
                    xDerecho,
                    yHijo
            );

            linea.setStroke(
                    Color.BLACK
            );

            paneArbol.getChildren().add(
                    linea
            );

            dibujarNodo(
                    nodo.getDerecho(),
                    xDerecho,
                    yHijo,
                    separacion / 2
            );
        }

        // Draw the node
        Circle circulo =
                new Circle(
                        x,
                        y,
                        25
                );

        circulo.setFill(
                Color.LIGHTBLUE
        );

        circulo.setStroke(
                Color.DARKBLUE
        );

        Text textoNodo =
                new Text(
                        x - 8,
                        y + 5,
                        String.valueOf(
                                nodo.getValor()
                        )
                );

        textoNodo.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        paneArbol.getChildren().addAll(
                circulo,
                textoNodo
        );
    }

    // Show an alert message
    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    // Return to the tree menu
    @FXML
    void Onregresar(
            ActionEvent event) throws IOException {

        Parent root =
                FXMLLoader.load(
                        getClass().getResource(
                                "/com/evelinsystem/arbol/menuarbol-view.fxml"
                        )
                );

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(
                new Scene(root)
        );
    }
}