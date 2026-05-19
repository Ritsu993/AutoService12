package ru.kafpin.autoservice.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PartSelectionDialogController {

    @FXML
    private ComboBox<?> cbPartBrand;

    @FXML
    private ComboBox<?> cbPartCategory;

    @FXML
    private CheckBox chkOnlyAvailable;

    @FXML
    private TableColumn<?, ?> colPartSelArticle;

    @FXML
    private TableColumn<?, ?> colPartSelCategory;

    @FXML
    private TableColumn<?, ?> colPartSelLocation;

    @FXML
    private TableColumn<?, ?> colPartSelName;

    @FXML
    private TableColumn<?, ?> colPartSelPrice;

    @FXML
    private TableColumn<?, ?> colPartSelStock;

    @FXML
    private Label lblAvailableQuantity;

    @FXML
    private Label lblSelectedPart;

    @FXML
    private Spinner<?> spSelectedQuantity;

    @FXML
    private TextField tfSearchPart;

    @FXML
    private TableView<?> tvParts;

}
