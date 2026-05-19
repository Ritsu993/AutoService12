package ru.kafpin.autoservice.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class MainController {

    @FXML
    private Button btnAddFromStandard;

    @FXML
    private Button btnAddOperation;

    @FXML
    private Button btnCancelOperation;

    @FXML
    private Button btnCancelWriteOff;

    @FXML
    private Button btnClearSearch;

    @FXML
    private Button btnCompleteOperation;

    @FXML
    private Button btnConfirmWriteOff;

    @FXML
    private Button btnCreateOrder;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnPauseOperation;

    @FXML
    private Button btnRefreshOrders;

    @FXML
    private Button btnRequestPart;

    @FXML
    private Button btnSaveOperation;

    @FXML
    private Button btnSaveOrderInfo;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnStartOperation;

    @FXML
    private Button btnUpdateStatus;

    @FXML
    private Button btnWriteOffPart;

    @FXML
    private ComboBox<?> cbAvailableParts;

    @FXML
    private ComboBox<?> cbChangeStatus;

    @FXML
    private ComboBox<?> cbFilterStatus;

    @FXML
    private CheckBox chkIsAdditional;

    @FXML
    private TableColumn<?, ?> colCarInfo;

    @FXML
    private TableColumn<?, ?> colClientInfo;

    @FXML
    private TableColumn<?, ?> colCreatedAt;

    @FXML
    private TableColumn<?, ?> colHistDate;

    @FXML
    private TableColumn<?, ?> colHistDescription;

    @FXML
    private TableColumn<?, ?> colHistOrderNumber;

    @FXML
    private TableColumn<?, ?> colHistTotal;

    @FXML
    private TableColumn<?, ?> colOpActions;

    @FXML
    private TableColumn<?, ?> colOpActualHours;

    @FXML
    private TableColumn<?, ?> colOpCompleted;

    @FXML
    private TableColumn<?, ?> colOpCost;

    @FXML
    private TableColumn<?, ?> colOpName;

    @FXML
    private TableColumn<?, ?> colOpNormHours;

    @FXML
    private TableColumn<?, ?> colOpStarted;

    @FXML
    private TableColumn<?, ?> colOpStatus;

    @FXML
    private TableColumn<?, ?> colOrderNumber;

    @FXML
    private TableColumn<?, ?> colPartArticle;

    @FXML
    private TableColumn<?, ?> colPartName;

    @FXML
    private TableColumn<?, ?> colPartQuantity;

    @FXML
    private TableColumn<?, ?> colPartStatus;

    @FXML
    private TableColumn<?, ?> colPartTotalPrice;

    @FXML
    private TableColumn<?, ?> colPartUnitPrice;

    @FXML
    private TableColumn<?, ?> colPartWrittenOff;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTotalAmount;

    @FXML
    private Label lblAssignedMaster;

    @FXML
    private Label lblCarBrand;

    @FXML
    private Label lblCarColor;

    @FXML
    private Label lblCarLicensePlate;

    @FXML
    private Label lblCarMileage;

    @FXML
    private Label lblCarVin;

    @FXML
    private Label lblCarYear;

    @FXML
    private Label lblClientAddress;

    @FXML
    private Label lblClientEmail;

    @FXML
    private Label lblClientInn;

    @FXML
    private Label lblClientName;

    @FXML
    private Label lblClientPhone;

    @FXML
    private Label lblClientType;

    @FXML
    private Label lblCurrentDate;

    @FXML
    private Label lblCurrentPartPrice;

    @FXML
    private Label lblManager;

    @FXML
    private Label lblMasterName;

    @FXML
    private Label lblNormativeHours;

    @FXML
    private Label lblOrderCreatedAt;

    @FXML
    private Label lblOrderNumber;

    @FXML
    private Label lblOrderStatus;

    @FXML
    private Label lblPartsCost;

    @FXML
    private Label lblRecordCount;

    @FXML
    private Label lblSelectedOperation;

    @FXML
    private Label lblStatusMessage;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblWorkCost;

    @FXML
    private ProgressBar pbProgress;

    @FXML
    private Spinner<?> spPartQuantity;

    @FXML
    private TextArea taMasterComments;

    @FXML
    private TextArea taOrderDescription;

    @FXML
    private TextField tfActualHours;

    @FXML
    private TextField tfCompletedHours;

    @FXML
    private TextField tfPauseReason;

    @FXML
    private TextField tfSearchOrder;

    @FXML
    private TitledPane tpOperationControl;

    @FXML
    private TabPane tpOrderDetails;

    @FXML
    private TitledPane tpWriteOffPart;

    @FXML
    private TableView<?> tvCarHistory;

    @FXML
    private TableView<?> tvRepairOperations;

    @FXML
    private TableView<?> tvWorkOrderParts;

    @FXML
    private TableView<?> tvWorkOrders;

    @FXML
    void handleAddFromStandard(ActionEvent event) {

    }

    @FXML
    void handleAddOperation(ActionEvent event) {

    }

    @FXML
    void handleCancelOperation(ActionEvent event) {

    }

    @FXML
    void handleCancelWriteOff(ActionEvent event) {

    }

    @FXML
    void handleClearSearch(ActionEvent event) {

    }

    @FXML
    void handleCompleteOperation(ActionEvent event) {

    }

    @FXML
    void handleConfirmWriteOff(ActionEvent event) {

    }

    @FXML
    void handleCreateOrder(ActionEvent event) {

    }

    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void handlePauseOperation(ActionEvent event) {

    }

    @FXML
    void handleRefreshOrders(ActionEvent event) {

    }

    @FXML
    void handleRequestPart(ActionEvent event) {

    }

    @FXML
    void handleSaveOperation(ActionEvent event) {

    }

    @FXML
    void handleSaveOrderInfo(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {

    }

    @FXML
    void handleStartOperation(ActionEvent event) {

    }

    @FXML
    void handleUpdateStatus(ActionEvent event) {

    }

    @FXML
    void handleWriteOffPart(ActionEvent event) {

    }

}
