package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.clases.vertice;
import com.evelinsystem.arbol.clases.grafo;

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
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class GrafoController {

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtCiudad1;

    @FXML
    private TextField txtCiudad2;

    @FXML
    private TextField txtInicio;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Pane paneGrafo;

    private final grafo grafo = new grafo();

    // Add a city
    @FXML
    private void agregarCiudad() {

        String ciudad = txtCiudad.getText().trim();

        if (ciudad.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese el nombre de una ciudad."
            );
            return;
        }

        boolean agregada = grafo.agregarVertice(ciudad);

        if (!agregada) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Ciudad repetida",
                    "La ciudad ya existe en el grafo."
            );
            return;
        }

        dibujarGrafo();
        txtCiudad.clear();
        txtCiudad.requestFocus();
    }

    // Add a connection between two cities
    @FXML
    private void agregarConexion() {

        String ciudad1 = txtCiudad1.getText().trim();
        String ciudad2 = txtCiudad2.getText().trim();

        if (ciudad1.isEmpty() || ciudad2.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Datos requeridos",
                    "Ingrese las dos ciudades."
            );
            return;
        }

        if (ciudad1.equalsIgnoreCase(ciudad2)) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Conexión inválida",
                    "Una ciudad no puede conectarse consigo misma."
            );
            return;
        }

        boolean conexion = grafo.agregarConexion(ciudad1, ciudad2);

        if (!conexion) {

            if (grafo.buscarVertice(ciudad1) == null ||
                    grafo.buscarVertice(ciudad2) == null) {

                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Ciudad no encontrada",
                        "Primero debe agregar las dos ciudades."
                );

            } else {

                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Conexión repetida",
                        "Las ciudades ya están conectadas."
                );
            }

            return;
        }

        dibujarGrafo();

        txtCiudad1.clear();
        txtCiudad2.clear();
    }

    // Draw the graph
    private void dibujarGrafo() {

        paneGrafo.getChildren().clear();

        if (grafo.estaVacio()) {
            return;
        }

        Map<vertice, double[]> posiciones = calcularPosiciones();

        // Draw connections first
        for (vertice v : grafo.getVertices()) {

            double[] posicion1 = posiciones.get(v);

            for (vertice conexion : v.getConexiones()) {

                double[] posicion2 = posiciones.get(conexion);

                // Draw each undirected connection only once
                if (grafo.getVertices().indexOf(v) <
                        grafo.getVertices().indexOf(conexion)) {

                    Line linea = new Line(
                            posicion1[0],
                            posicion1[1],
                            posicion2[0],
                            posicion2[1]
                    );

                    linea.setStroke(Color.BLACK);
                    linea.setStrokeWidth(2);

                    paneGrafo.getChildren().add(linea);
                }
            }
        }

        // Draw cities
        for (vertice v : grafo.getVertices()) {

            double[] posicion = posiciones.get(v);

            Circle circulo = new Circle(
                    posicion[0],
                    posicion[1],
                    30
            );

            circulo.setFill(Color.LIGHTGREEN);
            circulo.setStroke(Color.DARKGREEN);

            Text texto = new Text(
                    posicion[0] - 20,
                    posicion[1] + 5,
                    v.getNombre()
            );

            texto.setStyle(
                    "-fx-font-size: 13px; -fx-font-weight: bold;"
            );

            paneGrafo.getChildren().addAll(circulo, texto);
        }
    }

    // Calculate the position of each city
    private Map<vertice, double[]> calcularPosiciones() {

        Map<vertice, double[]> posiciones = new HashMap<>();

        int cantidad = grafo.getVertices().size();

        double centroX = paneGrafo.getPrefWidth() / 2;
        double centroY = 190;
        double radio = 150;

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

    // Show the list of cities
    @FXML
    private void mostrarCiudades() {

        if (grafo.estaVacio()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Grafo vacío",
                    "No hay ciudades en el grafo."
            );
            return;
        }

        StringBuilder resultado = new StringBuilder();

        resultado.append("CIUDADES DEL GRAFO:\n\n");

        for (vertice v : grafo.getVertices()) {

            resultado.append("- ")
                    .append(v.getNombre())
                    .append("\n");
        }

        txtResultado.setText(resultado.toString());
    }

    // Breadth First Search (BFS)
    @FXML
    private void mostrarRecorridoBFS() {

        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese la ciudad inicial."
            );
            return;
        }

        List<String> recorrido = recorridoAnchura(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Ciudad no encontrada",
                    "La ciudad inicial no existe."
            );
            return;
        }

        txtResultado.appendText(
                "\nRECORRIDO EN ANCHURA (BFS):\n"
                        + String.join(" -> ", recorrido) + "\n"
        );
    }

    // Breadth First Search algorithm
    private List<String> recorridoAnchura(String inicio) {

        List<String> recorrido = new java.util.ArrayList<>();

        Queue<vertice> cola = new LinkedList<>();
        Set<vertice> visitados = new HashSet<>();

        vertice primero = grafo.buscarVertice(inicio);

        if (primero == null) {
            return recorrido;
        }

        cola.add(primero);
        visitados.add(primero);

        // Visit cities level by level
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
    @FXML
    private void mostrarRecorridoDFS() {

        String inicio = txtInicio.getText().trim();

        if (inicio.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Dato requerido",
                    "Ingrese la ciudad inicial."
            );
            return;
        }

        List<String> recorrido = recorridoProfundidad(inicio);

        if (recorrido.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Ciudad no encontrada",
                    "La ciudad inicial no existe."
            );
            return;
        }

        txtResultado.appendText(
                "\nRECORRIDO EN PROFUNDIDAD (DFS):\n"
                        + String.join(" -> ", recorrido) + "\n"
        );
    }

    // Depth First Search algorithm
    private List<String> recorridoProfundidad(String inicio) {

        List<String> recorrido = new java.util.ArrayList<>();

        Set<vertice> visitados = new HashSet<>();

        vertice primero = grafo.buscarVertice(inicio);

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
    @FXML
    private void vaciar() {

        if (grafo.estaVacio()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Grafo vacío",
                    "El grafo ya está vacío."
            );
            return;
        }

        grafo.vaciar();

        paneGrafo.getChildren().clear();
        txtResultado.clear();

        txtCiudad.clear();
        txtCiudad1.clear();
        txtCiudad2.clear();
        txtInicio.clear();
    }

    // Show an alert message
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
    void Onregresar(ActionEvent event) throws IOException {

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