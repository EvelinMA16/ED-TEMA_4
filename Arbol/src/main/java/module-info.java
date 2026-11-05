module com.evelinsystem.arbol {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.evelinsystem.arbol to javafx.fxml;
    exports com.evelinsystem.arbol;
    exports com.evelinsystem.arbol.controller;
    opens com.evelinsystem.arbol.controller to javafx.fxml;
}