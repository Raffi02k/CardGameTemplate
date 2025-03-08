module se.iths.java24.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    // Original opens
    opens se.iths.java24.demo to javafx.fxml;

    // Card game opens for FXML
    opens com.cardgame to javafx.fxml;
    opens com.cardgame.controller to javafx.fxml;
    opens com.cardgame.view to javafx.fxml;

    // Original exports
    exports se.iths.java24.demo;

    // Card game exports
    exports com.cardgame to javafx.graphics;
    exports com.cardgame.controller to javafx.fxml;
    exports com.cardgame.model;
    exports com.cardgame.view;
}
