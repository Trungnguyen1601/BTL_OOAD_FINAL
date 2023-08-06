package com.example.vetau.TableView.QuanLyTau;

import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.ChitietTau;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class QuanLyTauController implements Initializable {


    @FXML
    private TableColumn<ChitietTau, String> ID_Tau_ct_col;

    @FXML
    private ComboBox<String> Loaitoa_id_combobox;

    @FXML
    private TableColumn<ChitietTau,Integer> Soluongghe_ct_col;

    @FXML
    private TextField Soluongtoa_id;

    @FXML
    private TableView<ChitietTau> chitietTauTableView;

    @FXML
    private TableColumn<ChitietTau, String> Toa_col;

    @FXML
    private ComboBox<String> Toa_id_combobox;

    @FXML
    private TableColumn<ChitietTau, Button> Update_col;

    @FXML
    private Label chucnang_id;

    @FXML
    private Button clear_btn_add_id;

    @FXML
    private Button clear_btn_search_id;

    @FXML
    private Button dashbroard_form_btn;

    @FXML
    private Button insert_btn_id;

    @FXML
    private TableColumn<ChitietTau, String> loaitoa_ct_col;

    @FXML
    private Button passenger_form_btn;

    @FXML
    private Button quanlytau_close;

    @FXML
    private Button quanlytau_form_btn;

    @FXML
    private Button quanlytau_minus;

    @FXML
    private Button quanlytau_phongto;

    @FXML
    private Button search_btn_id;

    @FXML
    private Button signout_btn;

    @FXML
    private TextField tau_add_id;

    @FXML
    private ComboBox<Tau> tau_id_combobox;

    @FXML
    private Button train_mana_form_btn;
    // Quản lý tàu
    @FXML
    private AnchorPane QuanlyTau_form;

    @FXML
    private AnchorPane QuanlychitietTau_form;

    @FXML
    private TableColumn<Tau, String> ID_Tau_tau_col;
    @FXML
    private TableColumn<Tau, Integer> Soluongtoa_tau_col;
    @FXML
    private TableView<Tau> Tau_TableView;
    @FXML
    private TableColumn<Tau, String> Trangthai_tau_col;

    @FXML
    private ComboBox<Tau> tau_id_combobox1;

    @FXML
    private ComboBox<String> trangthai_combobox;
    @FXML
    private TextField ten_tau_text_id;
    @FXML
    private TextField soluongtoa_tau_text_id;
    @FXML
    private TextField trangthai_tau_text_id;


    // Biến cho Chức năng chung
    Alert alert = null;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Tau tau = null;

    public static ChitietTau chitietTau_Add = new ChitietTau();

    public static ChitietTau chitiettau_Update = new ChitietTau();

    public static ChitietTau Get_ChitietTau_Update()
    {
        return chitiettau_Update;
    }

    // Chức năng cho Quản lý chi tiết tàu

    ObservableList<ChitietTau> ChitietTauList = FXCollections.observableArrayList();
    ObservableList<Tau> TauList = FXCollections.observableArrayList();
    @FXML
    void Clear_Click_chitietTau(MouseEvent event) {
        tau_id_combobox.setValue(null);
        Toa_id_combobox.setValue(null);
        Loaitoa_id_combobox.setValue(null);
    }

    @FXML
    void Search_click_chitietTau(MouseEvent event) {
        Search_chitietTau();
    }
    @FXML
    void Refresh_click_chitietTau(MouseEvent event) {
        loadData_ChitietTau();
    }
    @FXML
    void Insert_tau_click_chitietTau(MouseEvent event) throws SQLException {

        if ((tau_add_id.getText().isEmpty()) || (Soluongtoa_id.getText().isEmpty()) )
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Điền đẩy đủ thông tin tàu thêm");
            alert.showAndWait();
        }

        else {

            String ID_tau = tau_add_id.getText();
            String SLtoa = Soluongtoa_id.getText();
            resultSet = Database.SELECT_STRING_FROM_TABLE(connection, "tau", "ID_Tau", ID_tau);
            if (resultSet.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Note Message");
                alert.setHeaderText(null);
                alert.setContentText("Tàu đã tồn tại");
                alert.showAndWait();

            }
            else {


                Tau tau_insert = new Tau(ID_tau, Integer.parseInt(SLtoa));
                chitietTau_Add.setTau(tau_insert);
                String FXMLPATH_Them_tau = "/DashBroard/Quanlytau/ThemTau/themtau.fxml";
                try {
                    Show_Window showWindow = new Show_Window();
                    showWindow.Show(FXMLPATH_Them_tau);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    @FXML
    void quanlytau_close_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void quanlytau_minus_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void quanlytau_phongto_click(MouseEvent event) {

    }
    Parent root;
    Stage stage_dashbroard = new Stage();
    Stage stage_quanlytau = new Stage();


    {
        try {
            root = FXMLLoader.load(getClass().getResource("/DashBroard/dashbroard.fxml"));
            Scene scene_DashBroard = new Scene(root);
            stage_dashbroard.initStyle(StageStyle.TRANSPARENT);
            stage_dashbroard.setTitle("DashBroard");
            stage_dashbroard.setScene(scene_DashBroard);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void Switch_train_form(MouseEvent event) {
        stage_quanlytau = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage_dashbroard.show();
        stage_quanlytau.close();
    }

    public void loadData_ChitietTau()
    {
        RefreshTable_ChitietTau();
        Combobox_tau(tau_id_combobox);
        combobox_loaitoa(Loaitoa_id_combobox);
        ID_Tau_ct_col.setCellValueFactory(new PropertyValueFactory<>("ID_Tau"));
        Toa_col.setCellValueFactory(new PropertyValueFactory<>("ID_toa"));
        Soluongghe_ct_col.setCellValueFactory(new PropertyValueFactory<>("Soluongghe"));
        loaitoa_ct_col.setCellValueFactory(new PropertyValueFactory<>("Loaitoa"));

    }

    public void RefreshTable_ChitietTau()
    {
        ChitietTauList.clear();
        connection = Database.connectionDB();
        query = "SELECT \n" +
                "\ttoa_tau.ID_Tau as ID_tau ,\n" +
                "    toa_tau.ID_Toatau as Toa,\n" +
                "    toa_tau.Loaitoa as loaitoa,\n" +
                "    toa_tau.Soluongghe as SLghe,\n" +
                "    tau.Soluongtoa as soluongtoa\n" +
                "FROM toa_tau\n" +
                "INNER JOIN tau ON tau.ID_Tau = toa_tau.ID_Tau ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_tau"),resultSet.getInt("soluongtoa"));

                ChitietTauList.add(new ChitietTau(tau,
                        resultSet.getString("Toa"),
                        resultSet.getInt("SLghe"),
                        resultSet.getString("loaitoa"))
                );



            }
            chitietTauTableView.setItems(ChitietTauList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void Combobox_tau(ComboBox <Tau> comboBox)
    {
        try {
            Tau tau = null;
            query = "SELECT *  FROM tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                    System.out.println(resultSet.getInt("idTau"));
                String ID_Tau = resultSet.getString("ID_Tau");
                int soluongtoa = resultSet.getInt("Soluongtoa");

                tau = new Tau(ID_Tau,soluongtoa);
//                tau_id_combobox.getItems().add(tau);
                comboBox.getItems().add(tau);
            }

            // Xử lý sự kiện khi combobox được chọn
//            gadi_id_combox.setOnAction(event -> {
//                String selectedTrain = gadi_id_combox.getValue();
//
//            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Select_toa_combobox(MouseEvent event) {
        Toa_id_combobox.getItems().clear();
        if (tau_id_combobox.getValue() == null)
        {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Valid train");
            alert.showAndWait();
        }
        else {
            Tau tau = tau_id_combobox.getValue();
            String ID_tau = tau.getIDTau();
            System.out.println(ID_tau);
            query = "SELECT ID_Toatau FROM toa_tau\n" +
                    "WHERE ID_Tau = ?";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ID_tau);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Toa_id_combobox.getItems().add(resultSet.getString("ID_Toatau"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void combobox_loaitoa(ComboBox<String> comboBox)
    {
        try {

            query = "SELECT DISTINCT Loaitoa FROM toa_tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String loaitoa = resultSet.getString("Loaitoa");
                comboBox.getItems().add(loaitoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void Search_chitietTau()
    {
        connection = Database.connectionDB();
        ChitietTau chitietTau = new ChitietTau();
        Tau tau = null;
        String Tau = null;
        String Toa  = null;
        String Loaitoa = null;
        query = "SELECT *\n" +
                "FROM\n" +
                "(\n" +
                "\tSELECT \n" +
                "\t\ttoa_tau.ID_Tau as ID_tau ,\n" +
                "\t\ttoa_tau.ID_Toatau as Toa,\n" +
                "\t\ttoa_tau.Loaitoa as loaitoa,\n" +
                "\t\ttoa_tau.Soluongghe as SLghe,\n" +
                "\t\ttau.Soluongtoa as soluongtoa\n" +
                "\tFROM toa_tau\n" +
                "\n" +
                "\tINNER JOIN tau ON tau.ID_Tau = toa_tau.ID_Tau \n" +
                ") as Chitiettau\n" +
                "WHERE (ID_tau = ? OR ? is null ) " +
                "AND (Toa = ? OR ? is null ) " +
                "AND (loaitoa = ? OR ? is null) " ;
        ChitietTauList.clear();

        if (tau_id_combobox.getValue() == null)
        {
            Tau = null;
        }
        else
        {
            Tau = tau_id_combobox.getValue().getIDTau();
        }
        Toa = Toa_id_combobox.getValue();
        Loaitoa = Loaitoa_id_combobox.getValue();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Tau);
            preparedStatement.setString(2,Tau);
            preparedStatement.setString(3,Toa);
            preparedStatement.setString(4,Toa);
            preparedStatement.setString(5,Loaitoa);
            preparedStatement.setString(6,Loaitoa);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_tau"),resultSet.getInt("soluongtoa"));
                ChitietTauList.add(new ChitietTau(tau,
                        resultSet.getString("Toa"),
                        resultSet.getInt("SLghe"),
                        resultSet.getString("loaitoa")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        chitietTauTableView.setItems(ChitietTauList);

    }

    @FXML
    void Switch_xemKhachhang(MouseEvent event) {

    }

    @FXML
    void logout(MouseEvent event) {

    }
    public static ChitietTau getChitietTau_Add() {
        return chitietTau_Add;
    }
    private void Delete_in_DBchitiettau(String ID_Tau)
    {
        Database.DELETE_String_FROM_TABLE(connection,"tau","ID_Tau",ID_Tau);
    }

    @FXML
    void Switch_Quanlychitiettau(MouseEvent event) {
        QuanlychitietTau_form.setVisible(true);
        QuanlyTau_form.setVisible(false);

    }

    @FXML
    void Switch_Quanlytau(MouseEvent event) {
        QuanlyTau_form.setVisible(true);
        QuanlychitietTau_form.setVisible(false);
    }

    // Quản lý tàu
    public void combobox_trangthai(ComboBox<String> comboBox)
    {
        try {

            query = "SELECT DISTINCT Trangthai FROM tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String Trangthai = resultSet.getString("Trangthai");
                comboBox.getItems().add(Trangthai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void RefreshTable_Tau()
    {
        TauList.clear();
        connection = Database.connectionDB();
        query = "SELECT * FROM tau";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_Tau"),
                        resultSet.getInt("Soluongtoa"),
                        resultSet.getString("Trangthai"));
                TauList.add(tau);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Tau_TableView.setItems(TauList);

    }
    public void loadData_Tau()
    {
        RefreshTable_Tau();
        Combobox_tau(tau_id_combobox1);
        combobox_trangthai(trangthai_combobox);
        ID_Tau_tau_col.setCellValueFactory(new PropertyValueFactory<>("IDTau"));
        Soluongtoa_tau_col.setCellValueFactory(new PropertyValueFactory<>("Soluongtoa"));
        Trangthai_tau_col.setCellValueFactory(new PropertyValueFactory<>("Trangthai"));

    }


    @FXML
    void Search_Click_tau(MouseEvent event) {
        Search_Tau();
    }
    @FXML
    void Clear_click_Tau(MouseEvent event) {
        tau_id_combobox1.setValue(null);
        trangthai_combobox.setValue(null);
    }

    @FXML
    void Clear_click_Tau1(MouseEvent event) {
        ten_tau_text_id.setText(null);
        soluongtoa_tau_text_id.setText(null);
    }

    @FXML
    void Insert_click_Tau(MouseEvent event) throws SQLException {
        if ((ten_tau_text_id.getText().isEmpty()) || (soluongtoa_tau_text_id.getText().isEmpty()) )
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Điền đẩy đủ thông tin tàu thêm");
            alert.showAndWait();
        }

        else {

            String ID_tau = ten_tau_text_id.getText();
            String SLtoa = soluongtoa_tau_text_id.getText();
            System.out.println("So luong toa là" + SLtoa);
            resultSet = Database.SELECT_STRING_FROM_TABLE(connection, "tau", "ID_Tau", ID_tau);
            if (resultSet.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Note Message");
                alert.setHeaderText(null);
                alert.setContentText("Tàu đã tồn tại");
                alert.showAndWait();

            }
            else {

                Tau tau_insert = new Tau(ID_tau, Integer.parseInt(SLtoa));
                chitietTau_Add.setTau(tau_insert);
                String FXMLPATH_Them_tau = "/DashBroard/Quanlytau/ThemTau/themtau.fxml";
                try {
                    Show_Window showWindow = new Show_Window();
                    showWindow.Show(FXMLPATH_Them_tau);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void Search_Tau()
    {
        TauList.clear();
        String ID_Tau = null;
        String Trangthai = null;
        if (tau_id_combobox1.getValue() == null)
        {
            ID_Tau = null;
        }
        else
        {
            ID_Tau = tau_id_combobox1.getValue().getIDTau();
        }


        query = "SELECT *\n" +
                "FROM tau\n" +
                "WHERE (ID_tau = ? or ? is null)\n" +
                "AND (Trangthai = ? or ? is null)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,ID_Tau);
            preparedStatement.setString(2,ID_Tau);
            preparedStatement.setString(3,Trangthai);
            preparedStatement.setString(4,Trangthai);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_Tau"),
                        resultSet.getInt("Soluongtoa"),
                        resultSet.getString("Trangthai"));
                TauList.add(tau);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Tau_TableView.setItems(TauList);

    }

    public void Button_Extend_ChitietTau()
    {
        TableColumn<ChitietTau, Void> UpdateColumn = new TableColumn<>("Update");
        Callback<TableColumn<ChitietTau, Void>, TableCell<ChitietTau, Void>> cellFactory1 = new Callback<>() {
            @Override
            public TableCell<ChitietTau, Void> call(final TableColumn<ChitietTau, Void> param) {
                final TableCell<ChitietTau, Void> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Update");

                    {
                        updateButton.setOnAction(event -> {

                            chitiettau_Update = getTableView().getItems().get(getIndex());
                            String FXMLPATH_Them_tau = "/DashBroard/Quanlytau/EditTau/EditTau.fxml";
                            try {
                                Show_Window showWindow =new Show_Window();
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
        chitietTauTableView.getColumns().add(UpdateColumn);

        TableColumn<ChitietTau, Void> deleteColumn = new TableColumn<>("Delete");
        Callback<TableColumn<ChitietTau, Void>, TableCell<ChitietTau, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ChitietTau, Void> call(final TableColumn<ChitietTau, Void> param) {
                final TableCell<ChitietTau, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {

                            ChitietTau item = getTableView().getItems().get(getIndex());
                            chitietTauTableView.getItems().remove(item);
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn xác nhận muốn xóa?");
                            Optional<ButtonType> option = alert.showAndWait();
                            if (option.get().equals(ButtonType.OK)) {
                                Delete_in_DBchitiettau(item.getID_Tau());
                            } else {
                                RefreshTable_ChitietTau();
                            }

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
                return cell;
            }
        };
        deleteColumn.setCellFactory(cellFactory);
        chitietTauTableView.getColumns().add(deleteColumn);
    }
    public void Button_Extend_Tau()
    {
        TableColumn<Tau, Void> UpdateColumn = new TableColumn<>("Xóa");
        Callback<TableColumn<Tau, Void>, TableCell<Tau, Void>> cellFactory1 = new Callback<>() {
            @Override
            public TableCell<Tau, Void> call(final TableColumn<Tau, Void> param) {
                final TableCell<Tau, Void> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Xóa");

                    {
                        updateButton.setOnAction(event -> {



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
        Tau_TableView.getColumns().add(UpdateColumn);
    }

    // Chạy khởi tạo ban đầu
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData_ChitietTau();
        loadData_Tau();
        Button_Extend_ChitietTau();
        Button_Extend_Tau();

    }
}
