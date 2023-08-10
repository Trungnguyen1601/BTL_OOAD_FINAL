package com.example.vetau.App.KhachHang.DatVe_Tau.DatVe;

import com.example.vetau.App.KhachHang.DatVe_Tau.Timkiem_VeTau.Trangchu_DatVe;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.Chuyen_tau;
import com.example.vetau.models.Ga_tau;
import com.example.vetau.models.Tau;
import com.example.vetau.models.VeTau;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;


public class DatVeTauController implements Initializable {
    @FXML
    private TableView<VeTau> VeTable;

    @FXML
    private Button cancelButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableColumn<VeTau, Integer> giave_col;

    @FXML
    private TableColumn<VeTau, String> idChuyentau_col;

    @FXML
    private TableColumn<VeTau, String> idToatau_col;

    @FXML
    private TableColumn<VeTau, String> idVetau_col;

    @FXML
    private TableColumn<VeTau, String> loaive_col;

    @FXML
    private ComboBox<String> TinhTrangSearch_Combobox;
    @FXML
    private TableColumn<VeTau, String> tinhtrangve_col;

    @FXML
    private TableColumn<VeTau, Integer> vitringoi_col;
    @FXML
    private ComboBox<String> toatauSearch_Combobox;


    @FXML
    private TextField vitringoiSearch;

    @FXML
    private TextField giaveSearch;

    @FXML
    private TextField gadenTxt;

    @FXML
    private TextField gadiTxt;

    @FXML
    private TextField giaveTxt;
    @FXML
    private TextField idvetauTxt;

    @FXML
    private TextField loaiveTxt;
    @FXML
    private TextField tinhtrangveTxt;
    @FXML
    private TextField toatauTxt;
    @FXML
    private TextField vitringoiTxt;
    Connection connection = null;
    String query = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Alert alert;

    Ga_tau gadi = null;
    Ga_tau gaden = null;
    Tau tau = null;
    Chuyen_tau chuyenTau = new Chuyen_tau();





    ObservableList<VeTau> vetauList = FXCollections.observableArrayList();
    Integer index;

    public void closeDatVeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void RefreshTable_Vetau() {
        connection = Database.connectionDB();
        vetauList.clear();
        Chuyen_tau Chuyen_tau_Datve = Trangchu_DatVe.Chuyentau_Search;
        try {
            query = "SELECT ve.*,\n" +
                    "ct.Ngay_di,ct.Ngay_den, ct.Gio_di,\n" +
                    "t.*,\n" +
                    "ga_di.Ten_Gatau as TenGaDi, ga_di.Dia_diem as DiaDiem_gadi , ga_di.ID_Gatau as ID_gadi,\n" +
                    "ga_den.Ten_Gatau as TenGaDen, ga_den.Dia_diem as DiaDiem_gaden , ga_den.ID_Gatau as ID_gaden\n" +
                    "\n" +
                    "FROM ve_tau ve  \n" +
                    "INNER JOIN chuyen_tau ct ON ve.ID_Chuyentau = ct.ID_Chuyentau  \n" +
                    "INNER JOIN tau t ON t.ID_Tau = ct.ID_Tau\n" +
                    "INNER JOIN ga_tau ga_di ON ga_di.ID_Gatau = ct.ID_Gadi\n" +
                    "INNER JOIN ga_tau ga_den ON ga_den.ID_Gatau = ct.ID_Gaden\n" +
                    "WHERE ve.ID_Chuyentau = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Chuyen_tau_Datve.getID_train());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String query_Idvetau = resultSet.getString("ID_Vetau");
//                String query_IdChuyentau = resultSet.getString("ID_Chuyentau");
                gadi = new Ga_tau(resultSet.getString("ID_gadi"),
                        resultSet.getString("TenGaDi"),
                        resultSet.getString("DiaDiem_gadi"));
                gaden = new Ga_tau(resultSet.getString("ID_gaden"),
                        resultSet.getString("TenGaDen"),
                        resultSet.getString("DiaDiem_gaden"));
                tau = new Tau(resultSet.getString("ID_Tau"),
                        resultSet.getInt("Soluongtoa"));
                chuyenTau = new Chuyen_tau(resultSet.getString("ID_Chuyentau"),
                        gadi,
                        gaden,
                        tau,
                        resultSet.getString("Gio_di"),
                        resultSet.getDate("Ngay_di"),
                        resultSet.getDate("Ngay_den")
                );
                String query_IdToaTau = resultSet.getString("ID_Toatau");
                Integer query_Vitringoi = resultSet.getInt("Vitringoi");
                Integer query_Giave = resultSet.getInt("Giave");
                String query_Tinhtrangve = resultSet.getString("Tinhtrangve");
                String query_Loaive = resultSet.getString("Loaive");
                vetauList.add(new VeTau(query_Idvetau,
                        chuyenTau,
                        query_IdToaTau,
                        query_Vitringoi,
                        query_Giave,
                        query_Tinhtrangve,
                        query_Loaive));

                VeTable.setItems(vetauList);

                //Print to txt

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void LoadData_Vetau() throws SQLException {
        connection = Database.connectionDB();
        RefreshTable_Vetau();
        Combobox_Toatau();
        Combobox_tinhtrangve();
        idVetau_col.setCellValueFactory(new PropertyValueFactory<>("idVetau"));
        idChuyentau_col.setCellValueFactory(new PropertyValueFactory<>("ID_Chuyentau"));
        idToatau_col.setCellValueFactory(new PropertyValueFactory<>("idToatau"));
        vitringoi_col.setCellValueFactory(new PropertyValueFactory<>("vitringoi"));
        giave_col.setCellValueFactory(new PropertyValueFactory<>("giave"));
        tinhtrangve_col.setCellValueFactory(new PropertyValueFactory<>("tinhtrangve"));
        loaive_col.setCellValueFactory(new PropertyValueFactory<>("loaive"));


        VeTable.setOnMouseClicked(mouseEvent -> {
            index = VeTable.getSelectionModel().getSelectedIndex();
            if (index <= -1) {
                return;
            }

            String selectedIdChuyentau = idChuyentau_col.getCellData(index);
            try {
                loadGadiGadenData(selectedIdChuyentau);
            } catch (SQLException e) {
                // Handle any exceptions here
            }

            idvetauTxt.setText(idVetau_col.getCellData(index).toString());
            toatauTxt.setText(idToatau_col.getCellData(index).toString());
            vitringoiTxt.setText(vitringoi_col.getCellData(index).toString());
            giaveTxt.setText(giave_col.getCellData(index).toString());
            tinhtrangveTxt.setText(tinhtrangve_col.getCellData(index).toString());
//            loaiveTxt.setText(loaive_col.getCellData(index).toString());
        });
    }
    private void loadGadiGadenData(String idChuyentau) throws SQLException {
        connection = Database.connectionDB();
        query = "SELECT ct.ID_Gadi, ct.ID_Gaden, ga1.Ten_Gatau AS Ten_Gadi, ga2.Ten_Gatau AS Ten_Gaden " +
                "FROM chuyen_tau ct " +
                "JOIN ga_tau ga1 ON ct.ID_Gadi = ga1.ID_Gatau " +
                "JOIN ga_tau ga2 ON ct.ID_Gaden = ga2.ID_Gatau " +
                "WHERE ct.ID_Chuyentau = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idChuyentau);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String tenGadi = resultSet.getString("Ten_Gadi");
            String tenGaden = resultSet.getString("Ten_Gaden");

            gadiTxt.setText(tenGadi);
            gadenTxt.setText(tenGaden);
        }
    }

    public void Combobox_Toatau() throws SQLException {
        query = "SELECT Soluongtoa FROM tau WHERE ID_Tau = ?";
        preparedStatement = connection.prepareStatement(query);
        String ID_Tau = Trangchu_DatVe.Chuyentau_Search.getIDTau();
        int SLToa = 0;
        preparedStatement.setString(1,ID_Tau);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
        {
            SLToa = resultSet.getInt("Soluongtoa");
        }
        for(int i = 1;i<= SLToa;i++)
        {
            toatauSearch_Combobox.getItems().add("TOA"+i);
        }
    }
    public void Combobox_tinhtrangve()
    {
        TinhTrangSearch_Combobox.getItems().add("Chưa mua");
        TinhTrangSearch_Combobox.getItems().add("Mua");
    }

    public void Search_Thongtinve() throws SQLException{
        String toatauSearchText = null;
        String tinhtrangveSearchText = null;
        int vitringoiSearchText = 0;
        int giaveSearchText = 0;
        if (toatauSearch_Combobox.getValue() != null)
        {
            toatauSearchText = toatauSearch_Combobox.getValue().trim();
        }
        if (!vitringoiSearch.getText().isEmpty())
        {
            vitringoiSearchText = Integer.parseInt(vitringoiSearch.getText());
        }
        if (!giaveSearch.getText().isEmpty())
        {
            giaveSearchText = Integer.parseInt(giaveSearch.getText());
        }
        if (TinhTrangSearch_Combobox.getValue() != null)
        {
            tinhtrangveSearchText = TinhTrangSearch_Combobox.getValue().trim();
        }

        vetauList.clear();

        connection = Database.connectionDB();

        Chuyen_tau Chuyen_tau_Datve = Trangchu_DatVe.Chuyentau_Search;

        query = "SELECT *\n" +
                "FROM\n" +
                "(\n" +
                "SELECT ve.*,\n" +
                "ct.Ngay_di,ct.Ngay_den, ct.Gio_di,\n" +
                "t.*,\n" +
                "ga_di.Ten_Gatau as TenGaDi, ga_di.Dia_diem as Diadiem_gadi, ga_di.ID_Gatau as ID_gadi,\n" +
                "ga_den.Ten_Gatau as TenGaDen, ga_den.Dia_diem as Diadiem_gaden, ga_den.ID_Gatau as ID_gaden\n" +
                "\n" +
                "FROM ve_tau ve  \n" +
                "INNER JOIN chuyen_tau ct ON ve.ID_Chuyentau = ct.ID_Chuyentau  \n" +
                "INNER JOIN tau t ON t.ID_Tau = ct.ID_Tau\n" +
                "INNER JOIN ga_tau ga_di ON ga_di.ID_Gatau = ct.ID_Gadi\n" +
                "INNER JOIN ga_tau ga_den ON ga_den.ID_Gatau = ct.ID_Gaden\n" +
                "WHERE ve.ID_Chuyentau = ? \n" +
                ") as chitietve\n" +
                "WHERE chitietve.ID_Toatau = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, Trangchu_DatVe.Chuyentau_Search.getID_train());
        preparedStatement.setString(2,toatauSearchText+Chuyen_tau_Datve.getTau().getIDTau());
//                preparedStatement.setString(1, toatauSearchText+Chuyen_tau_Datve.getTau().getIDTau());
//                preparedStatement.setString(2,toatauSearchText+Chuyen_tau_Datve.getTau().getIDTau() );
//                preparedStatement.setInt(3,vitringoiSearchText);
//                preparedStatement.setInt(4,vitringoiSearchText);
//                preparedStatement.setInt(5,giaveSearchText);
//                preparedStatement.setInt(6,giaveSearchText);
//                preparedStatement.setString(7,tinhtrangveSearchText);
//                preparedStatement.setString(8,tinhtrangveSearchText);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String query_Idvetau = resultSet.getString("ID_Vetau");
//                String query_IdChuyentau = resultSet.getString("ID_Chuyentau");
            gadi = new Ga_tau(resultSet.getString("ID_gadi"),
                    resultSet.getString("TenGaDi"),
                    resultSet.getString("DiaDiem_gadi"));
            gaden = new Ga_tau(resultSet.getString("ID_gaden"),
                    resultSet.getString("TenGaDen"),
                    resultSet.getString("DiaDiem_gaden"));
            tau = new Tau(resultSet.getString("ID_Tau"),
                    resultSet.getInt("Soluongtoa"));
            chuyenTau = new Chuyen_tau(resultSet.getString("ID_Chuyentau"),
                    gadi,
                    gaden,
                    tau,
                    resultSet.getString("Gio_di"),
                    resultSet.getDate("Ngay_di"),
                    resultSet.getDate("Ngay_den")
            );
            String query_IdToaTau = resultSet.getString("ID_Toatau");
            Integer query_Vitringoi = resultSet.getInt("Vitringoi");
            Integer query_Giave = resultSet.getInt("Giave");
            String query_Tinhtrangve = resultSet.getString("Tinhtrangve");
            String query_Loaive = resultSet.getString("Loaive");
            vetauList.add(new VeTau(query_Idvetau,
                    chuyenTau,
                    query_IdToaTau,
                    query_Vitringoi,
                    query_Giave,
                    query_Tinhtrangve,
                    query_Loaive));

            VeTable.setItems(vetauList);

        }
    }
    @FXML
    void Clear_InputSearch(MouseEvent event) {
        toatauSearch_Combobox.setValue(null);
        TinhTrangSearch_Combobox.setValue(null);
        vitringoiSearch.setText("");
        giaveSearch.setText("");

    }

    @FXML
    void Search_click(MouseEvent event) throws SQLException{
        Search_Thongtinve();
    }

    @FXML
    void Datve_Input(MouseEvent event) throws SQLException {
        ThemVe_Chitiet();
    }
    public void ThemVe_Chitiet() {

        String addData = "INSERT INTO chitiet_vetau (ID_Vetau, ID_Khachhang, Ngaydatve, Giamuave) " +
                "VALUES (?, ?, ?, ?)";

        connection = Database.connectionDB();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(addData);
            preparedStatement.setString(1, idvetauTxt.getText());
            preparedStatement.setString(2, "KH1");
            preparedStatement.setDate(3, new java.sql.Date(new Date().getTime()));
            preparedStatement.setInt(4, Integer.parseInt(giaveTxt.getText()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void RefreshThongtinVe() throws SQLException{
        gadiTxt.setText("");
        gadenTxt.setText("");
        idvetauTxt.setText("");
        toatauTxt.setText("");
        vitringoiTxt.setText("");
        giaveTxt.setText("");
        tinhtrangveTxt.setText("");
        loaiveTxt.setText("");
    }

    public void RefreshTableVeTau() throws SQLException {
        RefreshTable_Vetau();
        RefreshThongtinVe();
    }
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        try {
            LoadData_Vetau();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
