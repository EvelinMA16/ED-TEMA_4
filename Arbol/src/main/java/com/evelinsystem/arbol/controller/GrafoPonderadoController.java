package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.grafoponderado;
import com.evelinsystem.arbol.clases.grafoponderado.Vertice;
import com.evelinsystem.arbol.clases.grafoponderado.Conexion;

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

public class GrafoPonderadoController {

    @FXML
    private TextField txtVertice;

    @FXML
    private TextField txtOrigen;

    @FXML
    private TextField txtDestino;

    @FXML
    private TextField txtPeso;

    @FXML
    private TextField txtInicio;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Pane paneGrafo;

    private final grafoponderado grafo = new grafoponderado();

    // Add a new vertex
    @FXML
    private void agregarVertice() {

        String nombre = txtVertice.getText().trim();

        if (nombre.isEmpty()) {
            mostrarMensaje("Ingrese el nombre del vértice.");
            return;
        }

        if (grafo.agregarVertice(nombre)) {
            txtResultado.appendText("Vértice agregado: " + nombre + "\n");
            txtVertice.clear();
            dibujarGrafo();
        } else {
            mostrarMensaje("El vértice ya existe.");
        }
    }

    // Add a weighted connection
    @FXML
    private void agregarConexion() {

        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        String pesoTexto = txtPeso.getText().trim();

        if (origen.isEmpty() || destino.isEmpty() || pesoTexto.isEmpty()) {
            mostrarMensaje("Complete todos los campos de la conexión.");
            return;
        }

        if (origen.equalsIgnoreCase(destino)) {
            mostrarMensaje("No puede conectar un vértice consigo mismo.");
            return;
        }

        double peso;

        try {
            peso = Double.parseDouble(pesoTexto);

            if (!Double.isFinite(peso) || peso < 0) {
                mostrarMensaje("Ingrese un peso válido, mayor o igual a cero.");
                return;
            }

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese solamente valores numéricos para el peso.");
            return;
        }

        if (grafo.agregarConexion(origen, destino, peso)) {

            txtResultado.appendText(
                    "Conexión agregada: " + origen + " - " +
                            destino + " | Peso: " + peso + "\n"
            );

            txtOrigen.clear();
            txtDestino.clear();
            txtPeso.clear();

            dibujarGrafo();

        } else {
            mostrarMensaje(
                    "No se pudo agregar la conexión. Verifique que ambos vértices existan y que la conexión no esté repetida."
            );
        }
    }

    // Draw the weighted graph
    @FXML
    private void dibujarGrafo() {

        paneGrafo.getChildren().clear();

        List<Vertice> vertices = grafo.getVertices();

        if (vertices.isEmpty()) {
            return;
        }

        int cantidad = vertices.size();

        double centroX = paneGrafo.getWidth() / 2;
        double centroY = paneGrafo.getHeight() / 2;

        double radio = Math.min(centroX, centroY) - 55;

        if (radio < 50) {
            radio = 50;
        }

        double[] posicionesX = new double[cantidad];
        double[] posicionesY = new double[cantidad];

        // Calculate vertex positions
        for (int i = 0; i < cantidad; i++) {

            double angulo = 2 * Math.PI * i / cantidad;

            posicionesX[i] = centroX + radio * Math.cos(angulo);
            posicionesY[i] = centroY + radio * Math.sin(angulo);
        }

        // Draw connections and weights
        for (int i = 0; i < cantidad; i++) {

            Vertice actual = vertices.get(i);

            for (Conexion conexion : actual.getConexiones()) {

                Vertice destino = conexion.getDestino();
                int j = vertices.indexOf(destino);

                // Draw each undirected connection only once
                if (j > i) {

                    Line linea = new Line(
                            posicionesX[i], posicionesY[i],
                            posicionesX[j], posicionesY[j]
                    );

                    linea.setStroke(Color.DARKSLATEGRAY);
                    linea.setStrokeWidth(2);

                    paneGrafo.getChildren().add(linea);

                    // Display connection weight
                    double medioX = (posicionesX[i] + posicionesX[j]) / 2;
                    double medioY = (posicionesY[i] + posicionesY[j]) / 2;

                    Text textoPeso = new Text(
                            medioX + 5, medioY - 5,
                            String.valueOf(conexion.getPeso())
                    );

                    textoPeso.setFill(Color.DARKRED);
                    textoPeso.setStyle("-fx-font-weight: bold;");

                    paneGrafo.getChildren().add(textoPeso);
                }
            }
        }

        // Draw vertices
        for (int i = 0; i < cantidad; i++) {

            Circle circulo = new Circle(
                    posicionesX[i], posicionesY[i], 23
            );

            circulo.setFill(Color.web("#DCE8D5"));
            circulo.setStroke(Color.web("#456B4A"));
            circulo.setStrokeWidth(2);

            Text nombre = new Text(
                    posicionesX[i] - 8,
                    posicionesY[i] + 5,
                    vertices.get(i).getNombre()
            );

            nombre.setFill(Color.BLACK);
            nombre.setStyle("-fx-font-weight: bold;");

            paneGrafo.getChildren().addAll(circulo, nombre);
        }
    }

    // Display Breadth First Search
    @FXML
    private void mostrarRecorridoBFS() {

        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje("Ingrese el vértice inicial.");
            return;
        }

        List<String> recorrido = grafo.recorridoAnchura(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje("El vértice inicial no existe.");
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
            mostrarMensaje("Ingrese el vértice inicial.");
            return;
        }

        List<String> recorrido = grafo.recorridoProfundidad(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje("El vértice inicial no existe.");
            return;
        }

        txtResultado.appendText(
                "\nRECORRIDO EN PROFUNDIDAD (DFS):\n"
                        + String.join(" -> ", recorrido) + "\n"
        );
    }

    // Clear the graph and fields
    @FXML
    private void vaciar() {

        grafo.vaciar();

        txtVertice.clear();
        txtOrigen.clear();
        txtDestino.clear();
        txtPeso.clear();
        txtInicio.clear();
        txtResultado.clear();

        paneGrafo.getChildren().clear();
    }

    // Display validation messages
    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Return to the main menu
    @FXML
    private void Onregresar() throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/evelinsystem/arbol/menugrafos-view.fxml"
                )
        );

        Stage stage = (Stage) paneGrafo.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Menú Principal");
        stage.show();
    }
}