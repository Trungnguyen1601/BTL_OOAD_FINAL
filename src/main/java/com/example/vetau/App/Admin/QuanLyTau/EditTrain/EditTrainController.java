package com.example.vetau.App.Admin.QuanLyTau.EditTrain;

import com.example.vetau.helpers.Database;
import com.example.vetau.models.ChitietTau;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EditTrainController implements Initializable {

    @FXML
    private ComboBox<String> Loaitoa_editTau_combo;

    @FXML
    private TextField SLghe_editTau_text;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;

    private ChitietTau chitietTau_Update = null;


    @FXML
    void Cancel_EditTau_Click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Close_edit_tau_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void Loaitoa_Combo_edit(MouseEvent event) {
        try {
            query = "SELECT DISTINCT Loaitoa FROM toa_tau";
            preparedStatement = connection.prepareStatement(query);
            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String loaitoa = resultSet.getString("Loaitoa");

                Loaitoa_editTau_combo.getItems().add(loaitoa);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Minus_edit_tau_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    void Save_EditTau_Click(MouseEvent event) {
        Edit_Tau();
    }

    private void Edit_Tau()
    {
        if(SLghe_editTau_text.getText() != null)
        {
            chitietTau_Update.setSoluongghe(Integer.parseInt(SLghe_editTau_text.getText()));
        }
        if (Loaitoa_editTau_combo.getValue()!=null)
        {
            chitietTau_Update.setLoaitoa(Loaitoa_editTau_combo.getValue());
        }
        query = "UPDATE toa_tau \n" +
                "SET Soluongghe = ?, \n" +
                "Loaitoa= ?\n " +
                "WHERE ID_Toatau = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,chitietTau_Update.getSoluongghe());
            preparedStatement.setString(2,chitietTau_Update.getLoaitoa());
            preparedStatement.setString(3,chitietTau_Update.getID_toa());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = Database.connectionDB();
//        chitietTau_Update = QuanLyTauController.Get_ChitietTau_Update();
//        String tau_update = chitietTau_Update.getID_Tau();
//        String toa_update = chitietTau_Update.getID_toa();
//        String toa_new = toa_update.replace(tau_update,"");

    }
}