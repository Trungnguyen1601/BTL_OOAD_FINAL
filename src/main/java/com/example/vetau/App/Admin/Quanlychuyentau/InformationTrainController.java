package com.example.vetau.App.Admin.Quanlychuyentau;

import com.example.vetau.helpers.Database;
import com.example.vetau.models.Ga_tau;
import com.example.vetau.models.Tau;
import com.example.vetau.models.Chuyen_tau;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class InformationTrainController implements Initializable {

    @FXML
    private Button Edit_in_infor_btn;

    @FXML
    private Button close_infor_btn;

    @FXML
    private TextField gadden_infor_text;

    @FXML
    private TextField gadi_infor_text;

    @FXML
    private TextField giodi_infor_text;

    @FXML
    private TextField idTau_infor_text;

    @FXML
    private TextField sotoa_infor_text;
    @FXML
    private TextField tentau_infor_text;

    @FXML
    private TextField ve_conlai_infor_text;

    @FXML
    private TextField veban_infor_text;
    // Edit form
    @FXML
    private Button Edit_close_btn;
    @FXML
    private AnchorPane Infor_form;

    @FXML
    private AnchorPane Edit_form;

    @FXML
    private Button OK_edit_btn;

    @FXML
    private Button edit_minus_btn;

    @FXML
    private TextField gaden_edit_text;

    @FXML
    private TextField gadi_edit_text;

    @FXML
    private TextField giodi_edit_text;

    @FXML
    private TextField soluongtoa_edit_text;

    @FXML
    private ComboBox<Tau> tau_edit_combo;

    @FXML
    private DatePicker ngaydi_edit_date;

    @FXML
    private DatePicker ngayden_edit_date;
    @FXML
    private TextField diemdi_infor_id;

    @FXML
    private TextField diemden_infor_id;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Chuyen_tau chuyentau = null;
    String Train_id_edit;
    private Alert alert;
    @FXML
    private ComboBox<Ga_tau> gaden_edit_combo;

    @FXML
    private ComboBox<Ga_tau> gadi_edit_combo;

    @FXML
    public void setClose_infor_btn(MouseEvent event)
    {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

    }
    @FXML
    void setedit_minus_btn(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void setEdit_close_btn(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void setEdit_in_infor_btn(MouseEvent event) {
        Edit_form.setVisible(true);
        Infor_form.setVisible(false);
        Get_Edit_Train();
    }

    @FXML
    void setOK(MouseEvent event) {
        Combobox_tau_edit();
        Train_id_edit = DashBoardController.getTrain_id();
        if(gadi_edit_combo.getValue() != null)
        {
            chuyentau.setGadi(gadi_edit_combo.getValue());
        }
        if(gaden_edit_combo.getValue() != null)
        {
            chuyentau.setGaden(gaden_edit_combo.getValue());
        }
        if (!giodi_edit_text.getText().isEmpty())
        {
            chuyentau.setGiodi(giodi_edit_text.getText());
        }
        if (tau_edit_combo.getValue() != null)
        {
            chuyentau.setTau(tau_edit_combo.getValue());
        }
        if(!Train_id_edit.isEmpty())
        {
            chuyentau.setID_train(Train_id_edit);
        }
        if(ngaydi_edit_date.getValue() != null)
        {
            chuyentau.setNgaydi(java.sql.Date.valueOf(ngaydi_edit_date.getValue()));
        }
        if(ngayden_edit_date.getValue() != null)
        {
            chuyentau.setNgayden(java.sql.Date.valueOf(ngayden_edit_date.getValue()));
        }
        query = "UPDATE chuyen_tau \n" +
                "SET ID_Gadi = ?, \n" +
                "ID_Gaden = ?,\n " +
                "ID_Tau = ?, \n"+
                "Gio_di = ?, \n"+
                "Ngay_di = ?, \n" +
                "Ngay_den = ? \n" +
                "WHERE ID_Chuyentau = ?";
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to change information?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, chuyentau.getGadi().getID_Gatau());
                preparedStatement.setString(2, chuyentau.getGaden().getID_Gatau());
                preparedStatement.setString(3, chuyentau.getTau().getIDTau());
                preparedStatement.setString(4, chuyentau.getGiodi());
                preparedStatement.setDate(5,(Date) chuyentau.getNgaydi());
                preparedStatement.setDate(6, (Date) chuyentau.getNgayden());
                preparedStatement.setString(7,Train_id_edit);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }
    public void Combobox_gadi_edit()
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
                gadi_edit_combo.getItems().add(ga_tau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Combobox_gaden_edit()
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
                gaden_edit_combo.getItems().add(ga_tau);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Combobox_tau_edit()
    {
        try {
            Ga_tau ga_tau = null;
            query = "SELECT * FROM tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ID_Tau = resultSet.getString("ID_Tau");
                int Soluongtoa = resultSet.getInt("Soluongtoa");

                Tau tau = new Tau(ID_Tau,Soluongtoa);
                tau_edit_combo.getItems().add(tau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Start()
    {
        connection = Database.connectionDB();
        Combobox_gadi_edit();
        Combobox_gaden_edit();
        Combobox_tau_edit();
        String Train_ID_infor = DashBoardController.getTrain_id();
        query = "SELECT c.ID_Chuyentau,\n" +
                "      g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as DiaDiem_gadi,\n" +
                "      g2.Ten_Gatau AS TenGaDen,g2.Dia_diem as DiaDiem_gaden,\n" +
                "      t.ID_Tau as ID_taudi, t.Soluongtoa as Soluong ,\n" +
                "\t\tc.Gio_di, c.Ngay_di, c.Ngay_den\n" +
                "FROM chuyen_tau c\n" +
                "INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                "INNER JOIN tau t ON c.ID_Tau = t.ID_Tau\n" +
                "WHERE ID_Chuyentau = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Train_ID_infor);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                gadi_infor_text.setText(resultSet.getString("TenGaDi"));
                gadden_infor_text.setText(resultSet.getString("TenGaDen"));
                giodi_infor_text.setText(resultSet.getString("Gio_di"));
                sotoa_infor_text.setText(String.valueOf(resultSet.getInt("Soluong")));
                idTau_infor_text.setText(resultSet.getString("ID_taudi"));
                diemdi_infor_id.setText(resultSet.getString("DiaDiem_gadi"));
                diemden_infor_id.setText(resultSet.getString("DiaDiem_gaden"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Get_Edit_Train()
    {
        String Train_id_edit = DashBoardController.getTrain_id();
        connection = Database.connectionDB();
        query = "SELECT c.ID_Chuyentau,\n" +
                "     g1.ID_Gatau as ID_GaDi, g1.Ten_Gatau AS TenGaDi,g1.Dia_diem as DiaDiem_gadi,\n" +
                "      g2.ID_Gatau as ID_GaDen,g2.Ten_Gatau AS TenGaDen,g2.Dia_diem as DiaDiem_gaden,\n" +
                "      t.ID_Tau, t.Soluongtoa,\n" +
                "\t\tc.Gio_di, c.Ngay_di, c.Ngay_den\n" +
                "FROM chuyen_tau c\n" +
                "INNER JOIN ga_tau g1 ON c.ID_Gadi = g1.ID_Gatau\n" +
                "INNER JOIN ga_tau g2 ON c.ID_Gaden = g2.ID_Gatau\n" +
                "INNER JOIN tau t ON c.ID_Tau = t.ID_Tau\n" +
                "WHERE ID_Chuyentau = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Train_id_edit);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {

                Ga_tau gadi = new Ga_tau(resultSet.getString("ID_GaDi")
                        , resultSet.getString("TenGaDi")
                        , resultSet.getString("DiaDiem_gadi"));
                Ga_tau gaden = new Ga_tau(resultSet.getString("ID_GaDen")
                        ,resultSet.getString("TenGaDen")
                        ,resultSet.getString("DiaDiem_gaden"));
                Tau tau = new Tau(resultSet.getString("ID_Tau"),resultSet.getInt("Soluongtoa"));
                chuyentau = new Chuyen_tau(
                        resultSet.getString("ID_Chuyentau"),
                        gadi,
                        gaden,
                        tau,
                        resultSet.getString("Gio_di"),
                        resultSet.getDate("Ngay_di"),
                        resultSet.getDate("Ngay_den"));
            }

            try {

                // Xử lý sự kiện khi combobox được chọn
                tau_edit_combo.setOnAction(event -> {
                    String selectedTrain = tau_edit_combo.getValue().getIDTau();
                    try {
                        String query1 = "SELECT Soluongtoa FROM tau WHERE ID_Tau = ?";
                        PreparedStatement selectedTrainStatement = connection.prepareStatement(query1);
                        selectedTrainStatement.setString(1, selectedTrain);
                        ResultSet resultSet1 = selectedTrainStatement.executeQuery();
                        if (resultSet1.next()) {
                            int numOfCoaches = resultSet1.getInt("Soluongtoa");
                            soluongtoa_edit_text.setText(String.valueOf(numOfCoaches));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Infor_form.setVisible(true);
        Edit_form.setVisible(false);

        Start();

    }
}
