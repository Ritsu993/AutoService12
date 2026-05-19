package ru.kafpin.autoservice.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StandardWorksReferenceController {

    @FXML
    private Button btnAddWork;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDeleteWork;

    @FXML
    private Button btnEditWork;

    @FXML
    private ComboBox<?> cbWorkCategory;

    @FXML
    private TableColumn<?, ?> colWorkActive;

    @FXML
    private TableColumn<?, ?> colWorkBaseCost;

    @FXML
    private TableColumn<?, ?> colWorkCategory;

    @FXML
    private TableColumn<?, ?> colWorkCode;

    @FXML
    private TableColumn<?, ?> colWorkName;

    @FXML
    private TableColumn<?, ?> colWorkNormHours;

    @FXML
    private TextField tfSearchWork;

    @FXML
    private TableView<?> tvStandardWorks;

    @FXML
    void handleEditWork(ActionEvent event) {

    }

    @FXML
    void handleAddWork(ActionEvent event) {

    }

    @FXML
    void handleClose(ActionEvent event) {

    }

    @FXML
    void handleDeleteWork(ActionEvent event) {

    }

}
