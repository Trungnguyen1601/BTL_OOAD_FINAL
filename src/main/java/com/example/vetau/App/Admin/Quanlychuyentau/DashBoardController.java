package com.example.vetau.App.Admin.Quanlychuyentau;

import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Check_Status;
import com.example.vetau.helpers.Database;
import com.example.vetau.helpers.GenerateData_VeTau;
import com.example.vetau.models.Chuyen_tau;
import com.example.vetau.models.Ga_tau;
import com.example.vetau.models.Passenger;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private Button quanlytau_form_btn;
    @FXML
    private Button clear_btn_id;
    @FXML
    private Button dashbroaed_close;
    @FXML
    private Button dashbroard_form_btn;
    @FXML
    private Button dashbroard_minus;
    @FXML
    private Button delete_btn_id;
    @FXML
    private TableColumn<Chuyen_tau, String> gaden_col;
    @FXML
    private TableColumn<Chuyen_tau, String> gadi_col;
    @FXML
    private TableColumn<Chuyen_tau, String> giodi_col;
    @FXML
    private TextField giodi_id;
    @FXML
    private Button insert_btn_id;
    @FXML
    private Button passenger_form_btn;
    @FXML
    private Button search_btn_id;
    @FXML
    private Button signout_btn;
    @FXML
    private TableColumn<Chuyen_tau, String> tau_col;
    @FXML
    private Button train_form_btn;
    @FXML
    private Button update_btn_id;
    @FXML
    private TableView<Chuyen_tau> Train_table;
    @FXML
    private TableColumn<Chuyen_tau, Button> Update_col;

    @FXML
    private TableColumn<Chuyen_tau, Button> Delete_col;
    @FXML
    private DatePicker Ngayden_id;

    @FXML
    private DatePicker Ngaydi_id;
    @FXML
    private TableColumn<Chuyen_tau, LocalDate> ngayden_col;

    @FXML
    private TableColumn<Chuyen_tau, LocalDate> ngaydi_col;

    @FXML
    private ComboBox<Ga_tau> gadi_id_combox;
    @FXML
    private ComboBox<Ga_tau> gaden_id_combox;
    @FXML
    private ComboBox<Tau> tau_id_combox;
    // Quản lý khách hàng
    @FXML
    private TextField Addr_passenger_id;
    @FXML
    private TableColumn<Passenger,Date> Date_passenger_col;
    @FXML
    private TextField Date_passenger_id;
    @FXML
    private TableColumn<Passenger, Button> Delete_passenger_col;
    @FXML
    private TableColumn<Passenger, String> Diachi_passenger_col;
    @FXML
    private TableColumn<Passenger, String> Level_passenger_col;
    @FXML
    private TableView<Passenger> Passenger_table;
    @FXML
    private TableColumn<Passenger, String> Ten_passenger_col;
    @FXML
    private TableColumn<Passenger, Button> Update_passenger_col;
    @FXML
    private TableColumn<Passenger, String> cccd_passenger_col;
    @FXML
    private TextField cccd_passenger_id;
    @FXML
    private Button clear_btn_passenger;
    @FXML
    private TextField email_passenger_id;
    @FXML
    private TableColumn<Passenger, String> gender_passenger_col;
    @FXML
    private TextField gender_passenger_id;
    @FXML
    private TextField hoten_passenger_id;
    @FXML
    private TextField level_passenger_id;
    @FXML
    private TextField sdt_passenger_id;
    @FXML
    private Button search_btn_passenger;
    @FXML
    private Button update_btn_passenger;
    private Alert alert;
    String query = null;
    Connection connection = null;
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

    Stage stage_dashbroard = new Stage();

    ObservableList<Chuyen_tau> chuyentauList = FXCollections.observableArrayList();

    private void loadData()
    {
        connection = Database.connectionDB();
        refreshTable();

        gadi_col.setCellValueFactory(new PropertyValueFactory<>("TenGaDi"));
        gaden_col.setCellValueFactory(new PropertyValueFactory<>("TenGaDen"));
        tau_col.setCellValueFactory(new PropertyValueFactory<>("IDTau"));
        giodi_col.setCellValueFactory(new PropertyValueFactory<>("Giodi"));
        ngaydi_col.setCellValueFactory(new PropertyValueFactory<>("ngaydi"));
        ngayden_col.setCellValueFactory(new PropertyValueFactory<>("ngayden"));


    }

    private void refreshTable() {
        chuyentauList.clear();
        Combobox_gadi();
        Combobox_gaden();
        Combobox_tau(tau_id_combox);
        giodi_id.setText("");
        Ngaydi_id.setValue(null);
        Ngayden_id.setValue(null);
        try {
            query = "SELECT c.ID_Chuyentau,\n" +
                    "     g1.ID_Gatau as ID_GaDi, g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as DiaDiem_gadi,\n" +
                    "      g2.ID_Gatau as ID_GaDen,g2.Ten_Gatau AS TenGaDen,g2.Dia_diem as DiaDiem_gaden,\n" +
                    "      t.ID_Tau as ID_taudi, t.Soluongtoa as Soluong,\n" +
                    "\t\tc.Gio_di, c.Ngay_di, c.Ngay_den\n" +
                    "FROM chuyen_tau c\n" +
                    "INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                    "INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                    "INNER JOIN tau t ON c.ID_Tau = t.ID_Tau;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                gadi =new Ga_tau(resultSet.getString("ID_Gadi")
                        ,resultSet.getString("TenGaDi")
                        ,resultSet.getString("DiaDiem_gadi"));
                gaden = new Ga_tau(resultSet.getString("ID_GaDen")
                        ,resultSet.getString("TenGaDen")
                        ,resultSet.getString("DiaDiem_gaden"));
                tau = new Tau(resultSet.getString("ID_taudi")
                        ,resultSet.getInt("Soluong"));
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
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void Delete_inDB(String ID)
    {

        query = "DELETE FROM chuyen_tau WHERE ID_Chuyentau = ?";
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Bạn xác nhận muốn xóa?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,ID);
                int check = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            refreshTable();
        }
    }

    public int Count_Database_chuyentau()
    {
        int totalCount = 0;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM chuyen_tau");
            if(resultSet.next())
            {
                totalCount = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalCount;
    }
    public String MaxID_Database_chuyentau()
    {
        String MaxID = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(ID_Chuyentau) AS MaxId FROM chuyen_tau");
            if(resultSet.next())
            {
                MaxID = resultSet.getString("MaxId");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return MaxID;
    }
    public boolean Check_Exist_ChuyenTau(String ID_Tau, String ID_Gadi, String ID_Gaden, LocalDate Ngay_di, LocalDate Ngay_den ) throws SQLException {
        boolean flag = false;
        query = "SELECT * FROM chuyen_tau " +
                "WHERE ID_Tau = ? " +
                "AND ID_Gadi = ? " +
                "AND ID_Gaden = ? " +
                "AND Ngay_di = ? " +
                "AND Ngay_den = ? ";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,ID_Tau);
        preparedStatement.setString(2,ID_Gadi);
        preparedStatement.setString(3,ID_Gaden);
        preparedStatement.setDate(4, Date.valueOf(Ngay_di));
        preparedStatement.setDate(5, Date.valueOf(Ngay_den));
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {
            flag = true;
        }
        return flag;
    }
    public boolean Check_Status_Tau_Chuyentau(String ID_Tau) throws SQLException {
        boolean flag_status = false;
        resultSet = Database.SELECT_String_FROM_TABLE(connection,"tau","ID_Tau",ID_Tau);
        if(resultSet.next())
        {
            String Status = resultSet.getString("Trangthai");
            flag_status = Check_Status.ReturnBoolean_Check(Status);
        }

        return flag_status;

    }
    public void Add_train() throws SQLException {

        if((gadi_id_combox.getValue() == null) || (gaden_id_combox.getValue() == null)
                || (tau_id_combox.getValue()==null) || (giodi_id.getText().isEmpty())
                || (Ngaydi_id.getValue() == null) || (Ngayden_id.getValue() == null) )
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Điền đẩy đủ thông tin chuyến tàu thêm");
            alert.showAndWait();
        }
        else {
            if (!Check_Status_Tau_Chuyentau(tau_id_combox.getValue().getIDTau())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Tàu không tồn tại hoặc Tàu đang không được sử dụng");
                alert.showAndWait();

            } else {
                if (Check_Exist_ChuyenTau(tau_id_combox.getValue().getIDTau(), gadi_id_combox.getValue().getID_Gatau(),
                        gaden_id_combox.getValue().getID_Gatau(), Ngaydi_id.getValue(), Ngayden_id.getValue()))
                {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Chuyến tàu đã tồn tại");
                    alert.showAndWait();
                }
                else
                {

                    int total_ChuyenTau = Count_Database_chuyentau();
                    query = "INSERT INTO chuyen_tau (ID_chuyentau,ID_Gadi, ID_Gaden, ID_Tau, Gio_di,Ngay_di,Ngay_den) VAlUES (?,?,?,?,?,?,?)";
                    try {
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "CT" + (total_ChuyenTau + 1));
                        preparedStatement.setString(2, gadi_id_combox.getValue().getID_Gatau());
                        preparedStatement.setString(3, gaden_id_combox.getValue().getID_Gatau());
                        preparedStatement.setString(4, tau_id_combox.getValue().getIDTau());
                        preparedStatement.setString(5, giodi_id.getText());
                        preparedStatement.setDate(6, java.sql.Date.valueOf(Ngaydi_id.getValue()));
                        preparedStatement.setDate(7, java.sql.Date.valueOf(Ngayden_id.getValue()));

                        int check = preparedStatement.executeUpdate();
                        if (check > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Note Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Thêm chuyến tàu thành công");
                            alert.showAndWait();
                        } else {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Note Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Thêm chuyến tàu không thành công");
                            alert.showAndWait();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    GenerateData_VeTau.GenerateData_VeTau_AddTrain("CT" + (total_ChuyenTau + 1), tau_id_combox.getValue().getIDTau());
                    refreshTable();
                }
            }
        }
    }
    public void Search_train()
    {
        String ID_Tau = null;
        Date ndi = null;
        Date nden = null;
        Ga_tau ga_di = new Ga_tau();
        Ga_tau ga_den = new Ga_tau();
        query = "SELECT *\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        c.ID_Chuyentau as ID_CT,\n" +
                "        g1.ID_Gatau as ID_GaDi, g1.Ten_Gatau as TenGaDi, g1.Dia_diem as DiaDiem_gadi,\n" +
                "        g2.ID_Gatau as ID_GaDen, g2.Ten_Gatau as TenGaDen, g2.Dia_diem as DiaDiem_gaden,\n" +
                "        t.ID_Tau as ID_taudi, t.Soluongtoa as Soluong,\n" +
                "        c.Gio_di as gio_di, c.Ngay_di as ngay_di, c.Ngay_den as ngay_den\n" +
                "    FROM chuyen_tau c\n" +
                "    INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "    INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                "    INNER JOIN tau t ON c.ID_Tau = t.ID_Tau\n" +
                " ) as TT_Chuyentau\n" +
                "WHERE (ID_GaDi = ? OR ? is null ) " +
                "AND (ID_GaDen = ? OR ? is null ) " +
                "AND (ID_taudi = ? OR ? is null) " +
                "AND (gio_di = ? OR ? = ?) " +
                "AND (ngay_di = ? OR ? is null ) " +
                "AND (ngay_den = ? OR ? is null )";
        connection = Database.connectionDB();
        if (tau_id_combox.getValue() != null )
        {
            ID_Tau = tau_id_combox.getValue().getIDTau();
        }
        if (Ngaydi_id.getValue() != null)
        {
            ndi = java.sql.Date.valueOf(Ngaydi_id.getValue());
        }
        if(Ngayden_id.getValue() != null)
        {
            nden = java.sql.Date.valueOf(Ngayden_id.getValue());
        }
        if (gadi_id_combox.getValue()!= null)
        {
            ga_di = gadi_id_combox.getValue();
        }
        if(gaden_id_combox.getValue() != null) {
            ga_den = gaden_id_combox.getValue();
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,ga_di.getID_Gatau());
            preparedStatement.setString(2,ga_di.getID_Gatau());
            preparedStatement.setString(3,ga_den.getID_Gatau());
            preparedStatement.setString(4,ga_den.getID_Gatau());
            preparedStatement.setString(5,ID_Tau);
            preparedStatement.setString(6,ID_Tau);
            preparedStatement.setString(7,giodi_id.getText());
            preparedStatement.setString(8,giodi_id.getText());
            preparedStatement.setString(9,"");
            preparedStatement.setDate(10, ndi);
            preparedStatement.setDate(11,ndi);
            preparedStatement.setDate(12, nden);
            preparedStatement.setDate(13,nden);
            resultSet = preparedStatement.executeQuery();

            chuyentauList.clear();
            while (resultSet.next())
            {

                gadi =new Ga_tau(resultSet.getString("TT_Chuyentau.ID_GaDi")
                        ,resultSet.getString("TT_Chuyentau.TenGaDi")
                        ,resultSet.getString("TT_Chuyentau.DiaDiem_gadi"));
                gaden = new Ga_tau(resultSet.getString("TT_Chuyentau.ID_GaDen")
                        ,resultSet.getString("TT_Chuyentau.TenGaDen")
                        ,resultSet.getString("TT_Chuyentau.DiaDiem_gaden"));
                tau = new Tau(resultSet.getString("TT_Chuyentau.ID_taudi")
                        ,resultSet.getInt("TT_Chuyentau.Soluong"));

                chuyentauList.add(new Chuyen_tau(
                        resultSet.getString("TT_Chuyentau.ID_CT"),
                        gadi,
                        gaden,
                        tau,
                        resultSet.getString("TT_Chuyentau.gio_di"),
                        resultSet.getDate("TT_Chuyentau.ngay_di"),
                        resultSet.getDate("TT_Chuyentau.ngay_den")));
                Train_table.setItems(chuyentauList);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void Refresh_click(MouseEvent event) {
        refreshTable();

    }
    @FXML
    void Search_click(MouseEvent event) {
        Search_train();

    }
    @FXML
    void Setdashbroaed_close(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Setdashbroard_minus(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void Clear_Click(MouseEvent event) {
        giodi_id.setText(null);
        Ngaydi_id.setValue(null);
        Ngayden_id.setValue(null);
        tau_id_combox.setValue(null);
        gadi_id_combox.setValue(null);
        gaden_id_combox.setValue(null);
    }
    public void Button_Extend_Chuyentau()
    {
        TableColumn<Chuyen_tau, Void> InforColumn = new TableColumn<>("Xem");
        Callback<TableColumn<Chuyen_tau, Void>, TableCell<Chuyen_tau, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Chuyen_tau, Void> call(final TableColumn<Chuyen_tau, Void> param) {
                final TableCell<Chuyen_tau, Void> cell = new TableCell<>() {
                    private final Button InforButton = new Button("Chi tiết");

                    {
                        InforButton.setOnAction(event -> {

                            Chuyen_tau chuyentau = getTableView().getItems().get(getIndex());
                            Train_id = chuyentau.getID_train();
                            String FXMLPATH = "/Admin/InformationView/Information_train.fxml";
                            try {
                                Show_Window showWindow = new Show_Window();
                                showWindow.Show(FXMLPATH);
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
                            setGraphic(InforButton);
                        }
                    }
                };
                return cell;
            }
        };
        InforColumn.setCellFactory(cellFactory);
        Train_table.getColumns().add(InforColumn);

        TableColumn<Chuyen_tau, Void> DeleteColumn = new TableColumn<>("Xóa");
        Callback<TableColumn<Chuyen_tau, Void>, TableCell<Chuyen_tau, Void>> cellFactory1 = new Callback<>() {
            @Override
            public TableCell<Chuyen_tau, Void> call(final TableColumn<Chuyen_tau, Void> param) {
                final TableCell<Chuyen_tau, Void> cell = new TableCell<>() {
                    private final Button DeleteButton = new Button("Xóa");

                    {
                        DeleteButton.setOnAction(event -> {

                            Chuyen_tau chuyentau = getTableView().getItems().get(getIndex());
                            chuyentauList.remove(chuyentau);
                            Delete_inDB(chuyentau.getID_train());

                            //Tau_table.getItems().remove(item);

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(DeleteButton);
                        }
                    }
                };
                return cell;
            }
        };
        DeleteColumn.setCellFactory(cellFactory1);
        Train_table.getColumns().add(DeleteColumn);
    }

    public void Combobox_gadi()
    {
        try {
            Ga_tau ga_tau = null;
            query = "SELECT *  FROM ga_tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                String ID_Gatau = resultSet.getString("ID_Gatau");
                String Gatau  = resultSet.getString("Ten_Gatau");
                String Dia_diem = resultSet.getString("Dia_diem");
                ga_tau = new Ga_tau(ID_Gatau,Gatau,Dia_diem);
                gadi_id_combox.getItems().add(ga_tau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Combobox_gaden()
    {
        try {
            Ga_tau ga_tau = null;
            query = "SELECT *  FROM ga_tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ID_Gatau = resultSet.getString("ID_Gatau");
                String Gatau  = resultSet.getString("Ten_Gatau");
                String Dia_diem = resultSet.getString("Dia_diem");
                ga_tau = new Ga_tau(ID_Gatau,Gatau,Dia_diem);
                gaden_id_combox.getItems().add(ga_tau);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Combobox_tau(ComboBox <Tau> comboBox) {
        try {

            Tau tau = null;
            query = "SELECT *  FROM tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ID_Tau = resultSet.getString("ID_Tau");

                int soluongtoa = resultSet.getInt("Soluongtoa");
                tau = new Tau(ID_Tau, soluongtoa);
                comboBox.getItems().add(tau);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void GetValue_Gadi (MouseEvent event){
        chuyentau.setGadi(gadi_id_combox.getValue());

    }
    @FXML
    void GetValue_Gaden (MouseEvent event){
        chuyentau.setGaden(gaden_id_combox.getValue());
    }
    @FXML
    void GetValue_Tau (MouseEvent event){
        tau.setTau(tau_id_combox.getValue());
    }
    @FXML
    void Switch_quanlytau (MouseEvent event) throws IOException {
        stage_dashbroard = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        String FXMLPATH_quanlytau = "/Admin/Quanlytau/quanlytau.fxml";
        try {
            Show_Window showWindow = new Show_Window();
            showWindow.Show(FXMLPATH_quanlytau);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage_dashbroard.close();
    }

    @FXML
    void Switch_xemKhachhang (MouseEvent event) throws IOException {
        stage_dashbroard = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        String FXMLPATH_quanlykhachhang = "/Admin/QuanlyKhachhang/quanlyKhachhang.fxml";
        try {
            Show_Window showWindow = new Show_Window();
            showWindow.Show(FXMLPATH_quanlykhachhang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage_dashbroard.close();
    }

    public void logout () {
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
            } else {
                refreshTable();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadData();
        Button_Extend_Chuyentau();

    }
}

