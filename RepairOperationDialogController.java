package ru.kafpin.autoservice.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class RepairOperationDialogController {

    @FXML
    private ComboBox<?> cbStandardWork;

    @FXML
    private CheckBox chkIsAdditional;

    @FXML
    private Label lblStdBaseCost;

    @FXML
    private Label lblStdNormHours;

    @FXML
    private Label lblWorkCode;

    @FXML
    private RadioButton rbFromStandard;

    @FXML
    private RadioButton rbManual;

    @FXML
    private TextArea taOperationComment;

    @FXML
    private TextArea taOperationDescription;

    @FXML
    private TextField tfManualCost;

    @FXML
    private TextField tfManualNormHours;

    @FXML
    private TextField tfOperationName;

    @FXML
    private ToggleGroup tgOperationSource;

    @FXML
    private VBox vbManualInput;

    @FXML
    private VBox vbStandardSelection;

}
