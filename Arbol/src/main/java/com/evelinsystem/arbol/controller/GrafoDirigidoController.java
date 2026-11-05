package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.grafodirigido;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoDirigidoController {

    @FXML
    private TextField txtVertice;

    @FXML
    private TextField txtInicio;

    @FXML
    private TextField txtOrigen;

    @FXML
    private TextField txtDestino;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Pane paneGrafo;

    private final grafodirigido grafo = new grafodirigido();

    // Add a new vertex
    @FXML
    private void agregarVertice() {

        String nombre = txtVertice.getText().trim();

        if (nombre.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese el nombre del vértice.");
            return;
        }

        if (!grafo.agregarVertice(nombre)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice repetido",
                    "El vértice ya existe.");
            return;
        }

        dibujarGrafo();
        txtVertice.clear();
        txtVertice.requestFocus();
    }

    // Add a directed connection
    @FXML
    private void agregarConexion() {

        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (origen.isEmpty() || destino.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Datos requeridos",
                    "Ingrese el origen y el destino.");
            return;
        }

        if (origen.equalsIgnoreCase(destino)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Conexión inválida",
                    "No puede conectar un vértice consigo mismo.");
            return;
        }

        if (grafo.buscarVertice(origen) == null ||
                grafo.buscarVertice(destino) == null) {

            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado",
                    "Primero agregue los dos vértices.");
            return;
        }

        if (!grafo.agregarConexion(origen, destino)) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Conexión repetida",
                    "Esta conexión ya existe.");
            return;
        }

        dibujarGrafo();

        txtOrigen.clear();
        txtDestino.clear();
    }

    // Draw the directed graph
    private void dibujarGrafo() {

        paneGrafo.getChildren().clear();

        if (grafo.estaVacio()) {
            return;
        }

        Map<grafodirigido.Vertice, double[]> posiciones =
                calcularPosiciones();

        // Draw connections and arrowheads
        for (grafodirigido.Vertice origen : grafo.getVertices()) {

            double[] p1 = posiciones.get(origen);

            for (grafodirigido.Vertice destino : origen.getConexiones()) {

                double[] p2 = posiciones.get(destino);

                double dx = p2[0] - p1[0];
                double dy = p2[1] - p1[1];

                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia == 0) {
                    continue;
                }

                double ux = dx / distancia;
                double uy = dy / distancia;

                // Calculate line positions
                double inicioX = p1[0] + ux * 30;
                double inicioY = p1[1] + uy * 30;

                double finX = p2[0] - ux * 30;
                double finY = p2[1] - uy * 30;

                Line linea = new Line(
                        inicioX, inicioY, finX, finY
                );

                linea.setStroke(Color.BLACK);
                linea.setStrokeWidth(2);

                // Calculate arrowhead
                double tamano = 12;

                double x1 = finX - tamano * ux - 6 * uy;
                double y1 = finY - tamano * uy + 6 * ux;

                double x2 = finX - tamano * ux + 6 * uy;
                double y2 = finY - tamano * uy - 6 * ux;

                Line flecha1 = new Line(finX, finY, x1, y1);
                Line flecha2 = new Line(finX, finY, x2, y2);

                flecha1.setStroke(Color.BLACK);
                flecha2.setStroke(Color.BLACK);

                paneGrafo.getChildren().addAll(
                        linea, flecha1, flecha2
                );
            }
        }

        // Draw vertices
        for (grafodirigido.Vertice v : grafo.getVertices()) {

            double[] p = posiciones.get(v);

            Circle circulo = new Circle(p[0], p[1], 30);

            circulo.setFill(Color.LIGHTBLUE);
            circulo.setStroke(Color.DARKBLUE);
            circulo.setStrokeWidth(2);

            Text texto = new Text(
                    p[0] - v.getNombre().length() * 4,
                    p[1] + 5,
                    v.getNombre()
            );

            texto.setStyle(
                    "-fx-font-size: 13px; -fx-font-weight: bold;"
            );

            paneGrafo.getChildren().addAll(circulo, texto);
        }
    }

    // Calculate vertex positions
    private Map<grafodirigido.Vertice, double[]> calcularPosiciones() {

        Map<grafodirigido.Vertice, double[]> posiciones =
                new HashMap<>();

        int cantidad = grafo.getVertices().size();

        double centroX = paneGrafo.getWidth() / 2;
        double centroY = paneGrafo.getHeight() / 2;

        double radio = Math.min(centroX, centroY) - 45;

        for (int i = 0; i < cantidad; i++) {

            double angulo = 2 * Math.PI * i / cantidad;

            double x = centroX + radio * Math.cos(angulo);
            double y = centroY + radio * Math.sin(angulo);

            posiciones.put(
                    grafo.getVertices().get(i),
                    new double[]{x, y}
            );
        }

        return posiciones;
    }

    // Display graph information
    @FXML
    private void mostrarGrafo() {

        if (grafo.estaVacio()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Grafo vacío",
                    "No hay vértices registrados.");
            return;
        }

        StringBuilder resultado = new StringBuilder();

        resultado.append("GRAFO DIRIGIDO\n\n");

        for (grafodirigido.Vertice v : grafo.getVertices()) {

            resultado.append(v.getNombre()).append(" -> ");

            if (v.getConexiones().isEmpty()) {
                resultado.append("Sin conexiones");
            } else {

                for (int i = 0; i < v.getConexiones().size(); i++) {

                    resultado.append(
                            v.getConexiones().get(i).getNombre()
                    );

                    if (i < v.getConexiones().size() - 1) {
                        resultado.append(", ");
                    }
                }
            }

            resultado.append("\n");
        }

        txtResultado.setText(resultado.toString());
    }

    // Display Breadth First Search
    @FXML
    private void mostrarRecorridoBFS() {

        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese el vértice inicial.");
            return;
        }

        List<String> recorrido = grafo.recorridoAnchura(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado",
                    "El vértice inicial no existe.");
            return;
        }

        txtResultado.appendText(
                "\nRECORRIDO EN ANCHURA (BFS):\n"
                        + String.join(" -> ", recorrido) + "\n"
        );
    }

    // Display Depth First Search
    @FXML
    private void mostrarRecorridoDFS() {

        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese el vértice inicial.");
            return;
        }

        List<String> recorrido =
                grafo.recorridoProfundidad(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Vértice no encontrado",
                    "El vértice inicial no existe.");
            return;
        }

        txtResultado.appendText(
                "\nRECORRIDO EN PROFUNDIDAD (DFS):\n"
                        + String.join(" -> ", recorrido) + "\n"
        );
    }

    // Clear the graph
    @FXML
    private void vaciar() {

        if (grafo.estaVacio()) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Grafo vacío",
                    "El grafo ya está vacío.");
            return;
        }

        grafo.vaciar();

        paneGrafo.getChildren().clear();
        txtResultado.clear();

        txtVertice.clear();
        txtOrigen.clear();
        txtDestino.clear();
        txtInicio.clear();
    }

    // Display validation messages
    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    // Return to the graph menu
    @FXML
    private void Onregresar(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/evelinsystem/arbol/menugrafos-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.setScene(new Scene(root));
    }
}