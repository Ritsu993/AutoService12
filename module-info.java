module ru.kafpin.autoservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens ru.kafpin.autoservice to javafx.fxml;
    exports ru.kafpin.autoservice;
    exports ru.kafpin.autoservice.Controller;
    opens ru.kafpin.autoservice.Controller to javafx.fxml;
}