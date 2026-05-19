package ru.kafpin.autoservice.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WorkOrderDialogController {

    @FXML
    private Button btnAddCar;

    @FXML
    private ComboBox<?> cbCar;

    @FXML
    private ComboBox<?> cbMaster;

    @FXML
    private ComboBox<?> cbStatus;

    @FXML
    private Label lblClientInfo;

    @FXML
    private TextArea taDescription;

    @FXML
    private TextArea taNotes;

    @FXML
    private TextField tfNormativeHours;

    @FXML
    private TextField tfOrderNumber;

    @FXML
    void handleAddCar(ActionEvent event) {

    }

}
