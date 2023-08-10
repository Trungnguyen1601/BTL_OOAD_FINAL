package com.example.vetau.App.Admin.QuanLyTau.ThemTau;

import com.example.vetau.App.Admin.QuanLyTau.QuanLyTauController;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.ChitietTau;
import com.example.vetau.models.Tau;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ThemTauController implements Initializable {

    @FXML
    private TableColumn<ChitietTau, String> Loaitoa_add_col;

    @FXML
    private TableColumn<ChitietTau, String> Soluongghe_add_coll;

    @FXML
    private Label TauAdd;

    @FXML
    private TableView<ChitietTau> TauAdd_Table;

    @FXML
    private Button Tau_add_Cancle_btn;

    @FXML
    private Button Tau_add_OK_btn;

    @FXML
    private Button Tau_add_close_btn;

    @FXML
    private Button Tau_add_minus_btn;

    @FXML
    private Label Tau_added_label;

    @FXML
    private TableColumn<ChitietTau, String> Toa_add_col;
    private Map<ChitietTau, String> temporarySLgheValues;
    private Map<ChitietTau,String> temporacyLoai_toaValues;
    Tau tau = null;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;

    ObservableList<ChitietTau> ChitiettauList = FXCollections.observableArrayList();

    public ChitietTau chitietTau_add = new ChitietTau();

    public void Start()
    {
        chitietTau_add = QuanLyTauController.getChitietTau_Add();
    }
    @FXML
    void Close_themtau_Click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Minus_themtau_Click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void Cancle_Click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void LoadData_tau()
    {
        temporarySLgheValues = new HashMap<>();
        temporacyLoai_toaValues = new HashMap<>();

        refreshTable();
        Toa_add_col.setCellValueFactory(new PropertyValueFactory<>("ID_toa"));
        Soluongghe_add_coll.setCellValueFactory(data -> {
            ChitietTau item = data.getValue();
            String temporaryValue = temporarySLgheValues.getOrDefault(item, item.getSLghe());
            return new SimpleStringProperty(temporaryValue);
        });
        Soluongghe_add_coll.setCellFactory(createTextFieldCellFactory_SLghe());
        Soluongghe_add_coll.setOnEditCommit(event -> {
            ChitietTau item = event.getRowValue();
            item.setSoluongghe(Integer.parseInt(event.getNewValue()));
            //temporarySLgheValues.remove(item);
        });

        Loaitoa_add_col.setCellValueFactory(data -> {
            ChitietTau item = data.getValue();
            String temporaryValue = temporacyLoai_toaValues.getOrDefault(item, item.getLoaitoa());
            return new SimpleStringProperty(temporaryValue);
        });
        Loaitoa_add_col.setCellFactory(createComboBoxCellFactory_Loaitoa());
        Loaitoa_add_col.setOnEditCommit(event -> {
            ChitietTau item = event.getRowValue();
            item.setLoaitoa(event.getNewValue());
        });


    }

    private Callback<TableColumn<ChitietTau, String>, TableCell<ChitietTau, String>> createTextFieldCellFactory_SLghe() {
        return column -> new TextFieldTableCell<ChitietTau, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ChitietTau rowItem = getTableView().getItems().get(getIndex());
                    TextField textField = new TextField(item);
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue) {
                            commitEdit(textField.getText());
                            temporarySLgheValues.put(rowItem, textField.getText());
                            rowItem.setSoluongghe(Integer.parseInt(textField.getText()));
                        }
                    });
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        };
    }
    private Callback<TableColumn<ChitietTau, String>, TableCell<ChitietTau, String>> createComboBoxCellFactory_Loaitoa() {
        return column -> new ComboBoxTableCell<ChitietTau, String>(FXCollections.observableArrayList("VIP", "Thuong")) {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ChitietTau rowItem = getTableView().getItems().get(getIndex());
                    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("VIP", "Thuong"));
                    comboBox.getSelectionModel().select(item);
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        commitEdit(newValue);
                        temporacyLoai_toaValues.put(rowItem, comboBox.getValue());
                        rowItem.setLoaitoa(comboBox.getValue());
                    });
                    setGraphic(comboBox);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            }
        };

    }
    private void refreshTable()
    {
        chitietTau_add = QuanLyTauController.getChitietTau_Add();
        TauAdd.setText("Tàu thêm là" + chitietTau_add.getID_Tau());
        ChitiettauList.clear();
        for (int i = 1; i <= chitietTau_add.getSoluongtoa(); i++)
        {
            tau = new Tau(chitietTau_add.getID_Tau(), chitietTau_add.getSoluongtoa());
            ChitiettauList.add(new ChitietTau(tau,"TOA"+i,20,"Thuong"));
        }
        TauAdd_Table.setItems(ChitiettauList);
    }
    public void Insert_ChitietTau(ChitietTau chitietTau)
    {
        query = "INSERT INTO toa_tau (ID_Toatau,ID_Tau,Soluongghe,Loaitoa) VALUES (?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,chitietTau.getID_toa()+chitietTau.getID_Tau());
            preparedStatement.setString(2,chitietTau.getID_Tau());
            preparedStatement.setInt(3,chitietTau.getSoluongghe());
            preparedStatement.setString(4,chitietTau.getLoaitoa());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void Insert_Tau()
    {
        query = "INSERT INTO tau (ID_Tau,Soluongtoa,Trangthai) VAlUES (?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,chitietTau_add.getID_Tau());
            preparedStatement.setInt(2,chitietTau_add.getSoluongtoa());
            preparedStatement.setString(3,chitietTau_add.isTrangthaiTau_String());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Save_Tau_Click(MouseEvent event) {
        Insert_Tau();
        for (ChitietTau item : TauAdd_Table.getItems()) {
            String name = item.getID_Tau();
            String SLghe= item.getSLghe();
            String Loaitoa = item.getLoaitoa();
            String toa = item.getID_toa();
            System.out.println("Name: " + name + ", Toa" + toa+", So luong ghe " + SLghe + ", Loai toa " + Loaitoa);
            Insert_ChitietTau(item);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = Database.connectionDB();
        LoadData_tau();
    }
}
