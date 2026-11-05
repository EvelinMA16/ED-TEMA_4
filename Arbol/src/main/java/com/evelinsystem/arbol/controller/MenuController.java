package com.evelinsystem.arbol.controller;

import com.evelinsystem.arbol.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    void Onarbol(ActionEvent event)  throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("menuarbol-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setTitle("Arbol");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void Ongrafos(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("menugrafos-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setTitle("Grafos");
        stage.setScene(scene);
        stage.show();

}
}
