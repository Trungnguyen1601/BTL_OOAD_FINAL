package com.example.vetau.App.Admin.QuanLyTau;

import com.example.vetau.Show.EditableCheckBox;
import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Check_Status;
import com.example.vetau.helpers.Database;
import com.example.vetau.helpers.ExtractString;
import com.example.vetau.models.ChitietTau;
import com.example.vetau.models.Tau;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

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
    private TableView<ChitietTau> chitietTauTableView;

    @FXML
    private TableColumn<ChitietTau, String> Toa_col;

    @FXML
    private ComboBox<String> Toa_id_combobox;
    @FXML
    private ComboBox<String> Loaitoa_id_combobox_for_themtoa;
    @FXML
    private ComboBox<Tau> tau_id_combobox_for_themtoa;

    @FXML
    private TextField SLGhe_text_for_themtoa;
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
    public Button Edit_Status_ToaTau_btn;

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
    private TableColumn<Tau, Boolean> Trangthai_tau_col;

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
    @FXML
    private TableColumn<ChitietTau, Boolean> trangthai_toa_col;
    Stage stage_quanlykhachhang = new Stage();

    // Biến cho Chức năng chung
    Alert alert = null;
    public static boolean flag_for_alertInformation = false;

    private boolean flag_check_edit = false;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Tau tau = null;
    String Trangthai = null;

    public static ChitietTau chitietTau_Add = new ChitietTau();

    public static ChitietTau chitiettau_Update = new ChitietTau();
    public ChitietTau chitietTau_Delete = new ChitietTau();

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
    void Edit_Status_ToaTau(MouseEvent event) {
        flag_for_alertInformation = true;
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
    void Insert_toa_click_chitietTau(MouseEvent event) throws SQLException {
        if((tau_id_combobox_for_themtoa.getValue() == null) || ( SLGhe_text_for_themtoa.getText().isEmpty()) || (Loaitoa_id_combobox_for_themtoa.getValue() == null))
        {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đủ thông tin toa cần thêm");
            alert.showAndWait();
        }
        else {
            int SLToa = 0;
            Tau tau_isAdded = tau_id_combobox_for_themtoa.getValue();
            String ID_Tau_isAdded_Toa = tau_isAdded.getIDTau();
            ResultSet rs = Database.SELECT_String_FROM_TABLE(connection,"tau","ID_Tau",ID_Tau_isAdded_Toa);
            if (rs.next()) {
                SLToa = rs.getInt("Soluongtoa");
                int ToaAdded = SLToa + 1;
                Database.UPDATE_Int_DATA_FROM_TABLE(connection,"tau","Soluongtoa",
                        SLToa+1,"ID_Tau",ID_Tau_isAdded_Toa);
                String TOA_IsAdded = "TOA" + ToaAdded + tau_isAdded.getIDTau();
                String Loai_toa_tauisAdded = Loaitoa_id_combobox_for_themtoa.getValue();
                int SLghe_tauisAdded = Integer.parseInt(SLGhe_text_for_themtoa.getText());
                query = "INSERT INTO toa_tau (ID_Toatau,ID_Tau,Soluongghe,Loaitoa) VALUES " +
                        "(?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,TOA_IsAdded);
                preparedStatement.setString(2,ID_Tau_isAdded_Toa);
                preparedStatement.setInt(3,SLghe_tauisAdded);
                preparedStatement.setString(4,Loai_toa_tauisAdded);
                preparedStatement.executeUpdate();
            }
            else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Note Message");
                alert.setHeaderText(null);
                alert.setContentText("Không có dữ liệu số lượng toa");
                alert.showAndWait();
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
            root = FXMLLoader.load(getClass().getResource("/Admin/dashboard.fxml"));
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
    private void TooltipForButton()
    {
        Edit_Status_ToaTau_btn = new Button("Cho phép chỉnh sửa");

        // Tạo Tooltip tùy chỉnh với thông tin tên nút
        Tooltip tooltip = new Tooltip("This is a button");
        tooltip.setAutoHide(false);
        tooltip.setHideDelay(Duration.INDEFINITE);
        Tooltip.install(Edit_Status_ToaTau_btn, tooltip);

        // Đặt vị trí hiển thị Tooltip bên dưới nút
        Edit_Status_ToaTau_btn.setOnMouseEntered(event -> {
            Bounds bounds = Edit_Status_ToaTau_btn.localToScreen(Edit_Status_ToaTau_btn.getBoundsInLocal());
            double x = bounds.getMinX();
            double y = bounds.getMaxY();
            tooltip.show(Edit_Status_ToaTau_btn, x, y);
        });

        Edit_Status_ToaTau_btn.setOnMouseExited(event -> {
            tooltip.hide();
        });
    }
    public void loadData_ChitietTau()
    {
        RefreshTable_ChitietTau();
        Combobox_tau(tau_id_combobox);
        combobox_loaitoa(Loaitoa_id_combobox);

        Combobox_tau(tau_id_combobox_for_themtoa);
        combobox_loaitoa(Loaitoa_id_combobox_for_themtoa);


        ID_Tau_ct_col.setCellValueFactory(new PropertyValueFactory<>("ID_Tau"));
        Toa_col.setCellValueFactory(new PropertyValueFactory<>("ID_toa"));
        Soluongghe_ct_col.setCellValueFactory(new PropertyValueFactory<>("Soluongghe"));
        loaitoa_ct_col.setCellValueFactory(new PropertyValueFactory<>("Loaitoa"));
        trangthai_toa_col.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isTrangthai()));
        trangthai_toa_col.setCellFactory(column -> new EditableCheckBox<>());

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
                "    tau.Soluongtoa as soluongtoa,\n" +
                "    toa_tau.Trangthai as trangthai\n" +
                "FROM toa_tau\n" +
                "INNER JOIN tau ON tau.ID_Tau = toa_tau.ID_Tau ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_tau"),resultSet.getInt("soluongtoa"));
                Trangthai = resultSet.getString("trangthai");
                boolean check = Check_Status.ReturnBoolean_Check(Trangthai);
                ChitietTauList.add(new ChitietTau(tau,
                        resultSet.getString("Toa"),
                        resultSet.getInt("SLghe"),
                        resultSet.getString("loaitoa"),
                        check)
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
                String ID_Tau = resultSet.getString("ID_Tau");
                int soluongtoa = resultSet.getInt("Soluongtoa");
                tau = new Tau(ID_Tau,soluongtoa);
                comboBox.getItems().add(tau);
            }


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
    void Switch_xemKhachhang(MouseEvent event) throws IOException {
        stage_dashbroard =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Admin/QuanlyKhachhang/quanlyKhachhang.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        stage_quanlykhachhang.initStyle(StageStyle.TRANSPARENT);
        stage_quanlykhachhang.setScene(scene_quanlykhachhang);

        stage_quanlykhachhang.show();
        stage_dashbroard.close();
    }

    @FXML
    void logout(MouseEvent event) {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Note Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                /* TO HIDE MAIN FORM */
                signout_btn.getScene().getWindow().hide();
                /* LINK YOUR LOGIN FORM AND SHOW IT */
                Parent root_logout = FXMLLoader.load(getClass().getResource("/Main/login-view.fxml"));
                Stage stage_logout = new Stage();
                Scene scene_logout = new Scene(root_logout);
                stage_logout.initStyle(StageStyle.TRANSPARENT);
                stage_logout.setScene(scene_logout);
                stage_logout.show();
            }
            else {
                RefreshTable_Tau();
                RefreshTable_ChitietTau();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChitietTau getChitietTau_Add() {
        return chitietTau_Add;
    }
    private void Delete_Toa_in_DBchitiettau(String ID_Toatau) throws SQLException {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Message");
            alert.setHeaderText(null);
            alert.setContentText("Sẽ xóa vĩnh viễn toa tàu khỏi dữ liệu?");
            Optional<ButtonType>  option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                Database.DELETE_String_FROM_TABLE(connection,"toa_tau","ID_Toatau",ID_Toatau);
                String ID_Tau_Delete = chitietTau_Delete.getID_Tau();
                ResultSet rs = Database.SELECT_String_FROM_TABLE(connection,"tau","ID_Tau",ID_Tau_Delete);
                if (rs.next()) {
                    int SLToa = rs.getInt("Soluongtoa");
                    int Index_Start_to_Delete = Integer.parseInt(ExtractString.extractNumber_from_ToaTau(ID_Toatau)) + 1;
                    for (int i = Index_Start_to_Delete; i <= SLToa; i++) {
                        int Index_Start = i - 1;
                        String TOA_UPDATE = "TOA" + Index_Start + ID_Tau_Delete;
                        String TOA_IS_UPDATED = "TOA" + i + ID_Tau_Delete;
                        Database.UPDATE_String_DATA_FROM_TABLE(connection, "toa_tau", "ID_Toatau",
                                TOA_UPDATE, "ID_Toatau", TOA_IS_UPDATED);
                    }
                    Database.UPDATE_Int_DATA_FROM_TABLE(connection,"tau","Soluongtoa",
                            SLToa -1,"ID_Tau",ID_Tau_Delete);
                }
            }



    }
    private void Delete_Tau_In_DBTau(String ID_Tau) throws SQLException {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Message");
        alert.setHeaderText(null);
        alert.setContentText("Sẽ xóa vĩnh viễn tàu và các dữ liệu liên quan khỏi cơ sở dữ liệu?\n Bạn có chắc chắn không ?");
        Optional<ButtonType>  option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            resultSet = Database.SELECT_String_FROM_TABLE(connection,"chuyen_tau","ID_Tau",ID_Tau);
            while(resultSet.next())
            {
                String ID_CT = resultSet.getString("ID_Chuyentau");
                Database.DELETE_String_FROM_TABLE(connection,"chuyen_tau","ID_Chuyentau",ID_CT);
                Database.DELETE_String_FROM_TABLE(connection,"ve_tau","ID_Chuyentau",ID_CT);
            }
            Database.DELETE_String_FROM_TABLE(connection,"toa_tau","ID_Tau",ID_Tau);
            Database.DELETE_String_FROM_TABLE(connection,"tau","ID_Tau",ID_Tau);
        }
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
                String Trangthai = resultSet.getString("Trangthai");
                boolean check = Check_Status.ReturnBoolean_Check(Trangthai);
                tau = new Tau(resultSet.getString("ID_Tau"),
                        resultSet.getInt("Soluongtoa"),
                        check);
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
        Trangthai_tau_col.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isTrangthai()));
        Trangthai_tau_col.setCellFactory(column -> new EditableCheckBox<>());
        flag_for_alertInformation = false;

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
    void Edit_Status_Tau(MouseEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Message");
        alert.setHeaderText(null);
        if(!flag_for_alertInformation) {
            alert.setContentText("Bạn muốn bật chức năng sửa trạng thái ?");
        }else
        {
            alert.setContentText("Bạn muốn tắt chức năng sửa trạng thái ?");
        }
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            flag_for_alertInformation = !flag_for_alertInformation;
        }
        System.out.println(flag_for_alertInformation+"++++++=");
    }
    @FXML
    void Refresh_click_TauTableView(MouseEvent event) {
        loadData_Tau();

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
            resultSet = Database.SELECT_String_FROM_TABLE(connection, "tau", "ID_Tau", ID_tau);
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
                String FXMLPATH_Them_tau = "/Admin/Quanlytau/ThemTau/themtau.fxml";
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
                Trangthai = resultSet.getString("Trangthai");
                boolean check = Check_Status.ReturnBoolean_Check(Trangthai);
                tau = new Tau(resultSet.getString("ID_Tau"),
                        resultSet.getInt("Soluongtoa"),
                        check);
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
                            chitietTau_Delete = item;
                            chitietTauTableView.getItems().remove(item);
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Note Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn xác nhận muốn xóa?");
                            Optional<ButtonType> option = alert.showAndWait();
                            if (option.get().equals(ButtonType.OK)) {
                                try {
                                    Delete_Toa_in_DBchitiettau(item.getID_toa());
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
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
                            Tau tau_Delete = getTableView().getItems().get(getIndex());
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirm Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn muốn xóa tàu?");
                            Optional<ButtonType>  option = alert.showAndWait();
                            if (option.get().equals(ButtonType.OK)) {
                                try {
                                    Delete_Tau_In_DBTau(tau_Delete.getIDTau());
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }



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
        TooltipForButton();

    }
}
