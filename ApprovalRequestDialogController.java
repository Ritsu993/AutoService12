package ru.kafpin.autoservice.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ApprovalRequestDialogController {

    @FXML
    private ButtonType btnSendRequest;

    @FXML
    private Label lblAdditionalPartsCost;

    @FXML
    private Label lblAdditionalWorkCost;

    @FXML
    private Label lblApprovalOrderNumber;

    @FXML
    private Label lblTotalAdditional;

    @FXML
    private ListView<?> lvAdditionalOperations;

    @FXML
    private TextArea taClientComment;

}
