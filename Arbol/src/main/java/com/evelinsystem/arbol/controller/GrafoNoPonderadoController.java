package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.grafonoponderado;
import com.evelinsystem.arbol.clases.grafonoponderado.Vertice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.List;

public class GrafoNoPonderadoController {

    @FXML
    private TextField txtVertice;

    @FXML
    private TextField txtOrigen;

    @FXML
    private TextField txtDestino;

    @FXML
    private TextField txtInicio;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Pane paneGrafo;

    private final grafonoponderado grafo = new grafonoponderado();

    // Adds a vertex to the graph
    @FXML
    private void agregarVertice() {
        String nombre = txtVertice.getText().trim();

        if (nombre.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido", "Ingrese el nombre del vértice.");
            return;
        }

        if (!grafo.agregarVertice(nombre)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice existente", "Ese vértice ya se encuentra registrado.");
            return;
        }

        txtResultado.setText("Vértice agregado: " + nombre);
        txtVertice.clear();
        dibujarGrafo();
    }

    // Adds a connection between two vertices
    @FXML
    private void agregarConexion() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Datos requeridos", "Ingrese los dos vértices.");
            return;
        }

        if (origen.equalsIgnoreCase(destino)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Conexión inválida", "Un vértice no puede conectarse consigo mismo.");
            return;
        }

        if (grafo.buscarVertice(origen) == null
                || grafo.buscarVertice(destino) == null) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado",
                    "Primero debe agregar los dos vértices.");
            return;
        }

        if (!grafo.agregarConexion(origen, destino)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Conexión existente",
                    "Estos vértices ya están conectados.");
            return;
        }

        txtResultado.setText("Conexión agregada: " + origen + " — " + destino);
        txtOrigen.clear();
        txtDestino.clear();
        dibujarGrafo();
    }

    // Draws the graph in the interface
    private void dibujarGrafo() {
        paneGrafo.getChildren().clear();

        List<Vertice> vertices = grafo.getVertices();

        if (vertices.isEmpty()) {
            return;
        }

        double centroX = paneGrafo.getWidth() / 2;
        double centroY = paneGrafo.getHeight() / 2;
        double radio = Math.min(centroX, centroY) - 45;

        if (radio < 40) {
            radio = 40;
        }

        double[] x = new double[vertices.size()];
        double[] y = new double[vertices.size()];

        // Calculates the position of each vertex
        for (int i = 0; i < vertices.size(); i++) {
            double angulo = 2 * Math.PI * i / vertices.size();

            x[i] = centroX + radio * Math.cos(angulo);
            y[i] = centroY + radio * Math.sin(angulo);
        }

        // Draws each connection only once
        for (int i = 0; i < vertices.size(); i++) {
            Vertice actual = vertices.get(i);

            for (Vertice conectado : actual.getConexiones()) {
                int j = vertices.indexOf(conectado);

                if (j > i) {
                    Line linea = new Line(x[i], y[i], x[j], y[j]);
                    linea.setStroke(Color.web("#777777"));
                    linea.setStrokeWidth(2);
                    paneGrafo.getChildren().add(linea);
                }
            }
        }

        // Draws the vertices
        for (int i = 0; i < vertices.size(); i++) {
            Circle circulo = new Circle(x[i], y[i], 22);
            circulo.setFill(Color.web("#D9C6A5"));
            circulo.setStroke(Color.web("#765C3A"));
            circulo.setStrokeWidth(2);

            Text texto = new Text(
                    x[i] - vertices.get(i).getNombre().length() * 3.5,
                    y[i] + 4,
                    vertices.get(i).getNombre()
            );

            texto.setFill(Color.web("#302719"));

            paneGrafo.getChildren().addAll(circulo, texto);
        }
    }

    // Displays all vertices and their connections
    @FXML
    private void mostrarVertices() {
        if (grafo.estaVacio()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Grafo vacío", "No hay vértices registrados.");
            return;
        }

        StringBuilder resultado = new StringBuilder("VÉRTICES Y CONEXIONES\n\n");

        for (Vertice vertice : grafo.getVertices()) {
            resultado.append(vertice.getNombre()).append(" -> ");

            if (vertice.getConexiones().isEmpty()) {
                resultado.append("Sin conexiones");
            } else {
                for (Vertice conexion : vertice.getConexiones()) {
                    resultado.append(conexion.getNombre()).append("  ");
                }
            }

            resultado.append("\n");
        }

        txtResultado.setText(resultado.toString());
    }

    // Displays the Breadth-First Search traversal
    @FXML
    private void mostrarRecorridoBFS() {
        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido", "Ingrese el vértice inicial.");
            return;
        }

        if (grafo.buscarVertice(inicio) == null) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado", "El vértice inicial no existe.");
            return;
        }

        List<String> recorrido = grafo.recorridoAnchura(inicio);
        txtResultado.setText("RECORRIDO BFS (ANCHURA)\n\n"
                + String.join(" -> ", recorrido));
    }

    // Displays the Depth-First Search traversal
    @FXML
    private void mostrarRecorridoDFS() {
        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido", "Ingrese el vértice inicial.");
            return;
        }

        if (grafo.buscarVertice(inicio) == null) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado", "El vértice inicial no existe.");
            return;
        }

        List<String> recorrido = grafo.recorridoProfundidad(inicio);
        txtResultado.setText("RECORRIDO DFS (PROFUNDIDAD)\n\n"
                + String.join(" -> ", recorrido));
    }

    // Clears the graph and the interface
    @FXML
    private void vaciar() {
        grafo.vaciar();
        paneGrafo.getChildren().clear();
        txtVertice.clear();
        txtOrigen.clear();
        txtDestino.clear();
        txtInicio.clear();
        txtResultado.clear();
    }

    // Displays validation messages
    private void mostrarMensaje(Alert.AlertType tipo,
                                String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Returns to the main graph menu
    @FXML
    private void Onregresar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(
                            "/com/evelinsystem/arbol/menu-view.fxml"
                    )
            );

            Stage stage = (Stage) paneGrafo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú de Grafos");

        } catch (IOException e) {
            mostrarMensaje(Alert.AlertType.ERROR,
                    "Error", "No se pudo regresar al menú.");
        }
    }
}