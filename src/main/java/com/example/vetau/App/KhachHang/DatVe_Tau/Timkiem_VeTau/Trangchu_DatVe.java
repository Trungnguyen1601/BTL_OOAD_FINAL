package com.example.vetau.App.KhachHang.DatVe_Tau.Timkiem_VeTau;

import com.example.vetau.Main.MainController;
import com.example.vetau.Show.Show_Window;
import com.example.vetau.models.Chuyen_tau;
import com.example.vetau.models.Ga_tau;
import com.example.vetau.models.Tau;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import com.example.vetau.helpers.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Trangchu_DatVe implements Initializable {
    @FXML
    private TableColumn<Chuyen_tau, String> gaden_col;

    @FXML
    private TableColumn<Chuyen_tau, String> gadi_col;

    @FXML
    private TableColumn<Chuyen_tau, String> giodi_col;

    @FXML
    private TableColumn<Chuyen_tau, LocalDate> ngayden_col;

    @FXML
    private TableColumn<Chuyen_tau, LocalDate> ngaydi_col;

    @FXML
    private TableView<Chuyen_tau> Train_table;

    @FXML
    private TableColumn<Chuyen_tau, String> tau_col;
    @FXML
    private ComboBox<String> Diem_den_combobox;

    @FXML
    private ComboBox<String> Diem_di_combobox;

    @FXML
    private Label Label_TenKH;
    public static Chuyen_tau Chuyentau_Search = new Chuyen_tau();

    private Alert alert;
    String query = null;
    Connection connection = Database.connectionDB();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Chuyen_tau chuyentau = new Chuyen_tau();
    Ga_tau gadi = null;
    Ga_tau gaden = null;
    Tau tau = new Tau();

    private static String Train_id;

    public static String getTrain_id() {
        return Train_id;
    }
    Stage My_Stage = new Stage();
    Stage Stage_Login = new Stage();
    Stage Stage_Xemthongtin = new Stage();
    Stage Stage_VeDaDat = new Stage();

    Parent root2;

    ObservableList<Chuyen_tau> chuyentauList = FXCollections.observableArrayList();

    public void Combobox_Diemdi() throws SQLException {
        String Dia_diem_di = null;

        query = "SELECT DISTINCT Dia_diem_di\n" +
                "FROM\n" +
                "(\n" +
                "SELECT c.ID_Chuyentau,c.ID_Gadi, g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as Dia_diem_di, c.ID_Gaden, \n" +
                "g2.Ten_Gatau AS TenGaDen, g2.Dia_diem as Dia_diem_den\n" +
                "\n" +
                "FROM chuyen_tau c\n" +
                "JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                ") as Chuyentau_full\n";
        connection = Database.connectionDB();
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Dia_diem_di = resultSet.getString("Dia_diem_di");
            Diem_di_combobox.getItems().add(Dia_diem_di);
        }
    }

    public void Combobox_Diemden() throws SQLException {
        String Dia_diem_den = null;

        query = "SELECT DISTINCT Dia_diem_den\n" +
                "FROM\n" +
                "(\n" +
                "SELECT c.ID_Chuyentau,c.ID_Gadi, g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as Dia_diem_di, c.ID_Gaden, \n" +
                "g2.Ten_Gatau AS TenGaDen, g2.Dia_diem as Dia_diem_den\n" +
                "\n" +
                "FROM chuyen_tau c\n" +
                "JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                ") as Chuyentau_full\n";
        connection = Database.connectionDB();
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Dia_diem_den = resultSet.getString("Dia_diem_den");
            Diem_den_combobox.getItems().add(Dia_diem_den);
        }
    }

    public void Button_Extend_Chuyentau() {
        TableColumn<Chuyen_tau, Void> UpdateColumn = new TableColumn<>("Show");
        Callback<TableColumn<Chuyen_tau, Void>, TableCell<Chuyen_tau, Void>> cellFactory1 = new Callback<>() {
            @Override
            public TableCell<Chuyen_tau, Void> call(final TableColumn<Chuyen_tau, Void> param) {
                final TableCell<Chuyen_tau, Void> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Show");

                    {
                        updateButton.setOnAction(event -> {

                            Chuyentau_Search = getTableView().getItems().get(getIndex());
                            String FXMLPATH_Them_tau = "/Khach_Hang/DatVeTau/DatVe_ChiTiet/DatVe_Tau.fxml";
                            try {
                                Show_Window showWindow = new Show_Window();
                                showWindow.Show(FXMLPATH_Them_tau);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //Tau_table.getItems().remove(item);

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(updateButton);
                        }
                    }
                };
                return cell;
            }
        };
        UpdateColumn.setCellFactory(cellFactory1);
        Train_table.getColumns().add(UpdateColumn);
    }

    public void RefreshTable_Chuyentau() {
        connection = Database.connectionDB();
        chuyentauList.clear();
        try {
            query = "SELECT c.ID_Chuyentau,\n" +
                    " g1.ID_Gatau as ID_GaDi, g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as DiaDiem_gadi,\n" +
                    " g2.ID_Gatau as ID_GaDen,g2.Ten_Gatau AS TenGaDen,g2.Dia_diem as DiaDiem_gaden,\n" +
                    " t.ID_Tau as ID_taudi, t.Soluongtoa as Soluong,\n" +
                    "\t\tc.Gio_di, c.Ngay_di, c.Ngay_den\n" +
                    "FROM chuyen_tau c\n" +
                    "INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                    "INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                    "INNER JOIN tau t ON c.ID_Tau = t.ID_Tau;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gadi = new Ga_tau(resultSet.getString("ID_Gadi")
                        , resultSet.getString("TenGaDi")
                        , resultSet.getString("DiaDiem_gadi"));
                gaden = new Ga_tau(resultSet.getString("ID_GaDen")
                        , resultSet.getString("TenGaDen")
                        , resultSet.getString("DiaDiem_gaden"));
                tau = new Tau(resultSet.getString("ID_taudi")
                        , resultSet.getInt("Soluong"));
                chuyentauList.add(new Chuyen_tau(
                        resultSet.getString("ID_Chuyentau"),
                        gadi,
                        gaden,
                        tau,
                        resultSet.getString("Gio_di"),
                        resultSet.getDate("Ngay_di"),
                        resultSet.getDate("Ngay_den")));
                Train_table.setItems(chuyentauList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void LoadData_Chuyentau() throws SQLException {
        connection = Database.connectionDB();
        RefreshTable_Chuyentau();
        Combobox_Diemdi();
        Combobox_Diemden();
        Button_Extend_Chuyentau();
        gadi_col.setCellValueFactory(new PropertyValueFactory<>("TenGaDi"));
        gaden_col.setCellValueFactory(new PropertyValueFactory<>("TenGaDen"));
        tau_col.setCellValueFactory(new PropertyValueFactory<>("IDTau"));
        giodi_col.setCellValueFactory(new PropertyValueFactory<>("Giodi"));
        ngaydi_col.setCellValueFactory(new PropertyValueFactory<>("ngaydi"));
        ngayden_col.setCellValueFactory(new PropertyValueFactory<>("ngayden"));

    }

    public void Timkiem_Chuyentau() throws SQLException {
        chuyentauList.clear();
        query = "SELECT *\n" +
                "FROM (\n" +
                "    SELECT c.ID_Chuyentau as ID_CT,\n" +
                "        g1.ID_Gatau as ID_GaDi, g1.Ten_Gatau as TenGaDi, g1.Dia_diem as DiaDiem_gadi,\n" +
                "        g2.ID_Gatau as ID_GaDen, g2.Ten_Gatau as TenGaDen, g2.Dia_diem as DiaDiem_gaden,\n" +
                "        t.ID_Tau as ID_taudi, t.Soluongtoa as Soluong,\n" +
                "        c.Gio_di as gio_di, c.Ngay_di as ngay_di, c.Ngay_den as ngay_den\n" +
                "    FROM chuyen_tau c\n" +
                "    INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "    INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                "    INNER JOIN tau t ON c.ID_Tau = t.ID_Tau\n" +
                ") as TT_Chuyentau\n" +
                "WHERE DiaDiem_gadi = ? and DiaDiem_gaden = ?";
        if (Diem_di_combobox.getValue() == null || Diem_den_combobox.getValue() == null) {
            RefreshTable_Chuyentau();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Điền đẩy đủ thông tin tàu thêm");
            alert.showAndWait();
        } else {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Diem_di_combobox.getValue());
            preparedStatement.setString(2, Diem_den_combobox.getValue());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gadi = new Ga_tau(resultSet.getString("ID_Gadi")
                        , resultSet.getString("TenGaDi")
                        , resultSet.getString("DiaDiem_gadi"));
                gaden = new Ga_tau(resultSet.getString("ID_GaDen")
                        , resultSet.getString("TenGaDen")
                        , resultSet.getString("DiaDiem_gaden"));
                tau = new Tau(resultSet.getString("ID_taudi")
                        , resultSet.getInt("Soluong"));
                chuyentauList.add(new Chuyen_tau(
                        resultSet.getString("ID_CT"),
                        gadi,
                        gaden,
                        tau,
                        resultSet.getString("Gio_di"),
                        resultSet.getDate("Ngay_di"),
                        resultSet.getDate("Ngay_den")));
                Train_table.setItems(chuyentauList);
            }
        }
    }

    public void Refresh_TableChuyenTau() throws SQLException {
        RefreshTable_Chuyentau();
    }

    public void refreshComboBoxes() throws SQLException {
        Diem_di_combobox.getItems().clear();
        Diem_den_combobox.getItems().clear();
        Combobox_Diemdi();
        Combobox_Diemden();
    }
    @FXML
    void Click_Xemthongtin(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Khach_Hang/Thong_tinKH/thongtin.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_Xemthongtin.initStyle(StageStyle.TRANSPARENT);
        Stage_Xemthongtin.setScene(scene_quanlykhachhang);

        Stage_Xemthongtin.show();
        My_Stage.close();
    }



    @FXML
    void Click_Close(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Click_Minus(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void Click_SignOut(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Main/login-view.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_Login.initStyle(StageStyle.TRANSPARENT);
        Stage_Login.setScene(scene_quanlykhachhang);

        Stage_Login.show();
        My_Stage.close();
    }

    @FXML
    void Click_VeDaDat(MouseEvent event) throws SQLException, IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Khach_Hang/DatVeTau/Ve_TauDaDat/Ve_DaDat.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_VeDaDat.initStyle(StageStyle.TRANSPARENT);
        Stage_VeDaDat.setScene(scene_quanlykhachhang);

        Stage_VeDaDat.show();
        My_Stage.close();
    }

    @FXML
    void Refresh_TableChuyenTau(MouseEvent event) throws SQLException {
        RefreshTable_Chuyentau();
        refreshComboBoxes(); // Refresh ComboBoxes when the button is clicked
    }

    @FXML
    void Timkiem_ChuyenTau(MouseEvent event) throws SQLException {
        Timkiem_Chuyentau();
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        resultSet = Database.SELECT_String_FROM_TABLE(connection,"khach_hang","ID_Account",MainController.Acc_Signin.getID_Account());
        try {
            if(resultSet.next()) {
                Label_TenKH.setText(resultSet.getString("Ten"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            LoadData_Chuyentau();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
