module project2.project2 {
    requires transitive javafx.controls; // requires javafx.controls and all of its transitive dependencies, which includes javafx.base, javafx.graphics, and javafx.fxml
    requires javafx.fxml;
    requires jbcrypt;
    requires javafx.graphics;
    requires org.junit.jupiter.api;

    // opens the project2.project2 package to javafx.fxml and project2.project2.controllers to javafx.fxml so that the fxml files can access the controller classes
    opens project2.project2 to javafx.fxml;
    opens project2.project2.controllers to javafx.fxml;

    // exports the project2.project2 package so that other modules can access the classes in this package
    exports project2.project2;
    exports project2.project2.controllers to javafx.fxml;
}