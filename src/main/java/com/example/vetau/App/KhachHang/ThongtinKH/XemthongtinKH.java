package com.example.vetau.App.KhachHang.ThongtinKH;

import com.example.vetau.Main.MainController;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.Passenger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class XemthongtinKH implements Initializable {
    @FXML
    private Label nameTop;
    @FXML
    private Label emailTop;
    @FXML
    private Label level;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;
    @FXML
    private TextField name;

    @FXML
    private TextField email;
    @FXML
    private DatePicker dobpicker;
    @FXML
    private TextField dob;
    @FXML
    private TextField cccd;

    @FXML
    private Label gioiTinh;
    @FXML
    private Label Label_TenKH;

    @FXML
    private HBox toggleGioiTinh;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    private ToggleGroup toggleGroup;

    @FXML
    private TextField phone;

    @FXML
    private TextField address;

    private Passenger passenger;
    private boolean isEditMode = false;

    private String ID_Account = MainController.Acc_Signin.getID_Account();
    Stage My_Stage, Stage_VeDaDat = new Stage();
    Stage Stage_DatVe = new Stage();
    Stage Stage_Login = new Stage();

    @FXML
    private void handleEditButtonClick() {
        isEditMode = true;
        setEditable(true);
    }

    @FXML
    private void handleSaveButtonClick() {
        if (isEditMode) {
            passenger.setHo_va_Ten(name.getText());
            //passenger.setDate(dobpicker.getValue());
            System.out.println(dobpicker.getValue());
            passenger.setCccd(cccd.getText());

            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
            if (selectedRadioButton == maleRadioButton) {
                passenger.setGender("Nam");
            } else if (selectedRadioButton == femaleRadioButton) {
                passenger.setGender("Nữ");
            }
            passenger.setEmail(email.getText());

            // Update the user data in the database
            Database databaseHandler = new Database();
            databaseHandler.updateUser(passenger);

            // Disable editing mode and reload the user data
            isEditMode = false;
            setEditable(false);
            loadUserData(ID_Account);
        }
    }

    private void setEditable(boolean editable) {
        name.setEditable(editable);
        dob.setVisible(!editable);
        dobpicker.setVisible(editable);
        cccd.setEditable(editable);
        gioiTinh.setVisible(!editable);
        toggleGioiTinh.setVisible(editable);
        email.setEditable(editable);
        address.setEditable(editable);
        phone.setEditable(editable);

        editButton.setVisible(!editable);
        saveButton.setVisible(editable);
    }

    private void loadUserData(String ID_Account) {
        Database databaseHandler = new Database();
        passenger = databaseHandler.fetchUser(ID_Account);

        if (passenger != null) {
            name.setText(passenger.getHo_va_Ten());
            nameTop.setText(passenger.getHo_va_Ten() + " #" + passenger.getID_Passenger());
            emailTop.setText(passenger.getEmail());
            email.setText(passenger.getEmail());
            gioiTinh.setText(passenger.getGender());
            if ("Nam".equalsIgnoreCase(passenger.getGender())) {
                toggleGroup.selectToggle(maleRadioButton);
            } else if ("Nữ".equalsIgnoreCase(passenger.getGender())) {
                toggleGroup.selectToggle(femaleRadioButton);
            }
            dob.setText(String.valueOf(passenger.getDate()));
            level.setText("Khách hàng hạng " + passenger.getLevel());
            cccd.setText(passenger.getCccd());
            phone.setText(passenger.getPhone());
            address.setText(passenger.getAddress());
        }
    }
    @FXML
    void Click_Switch_DatVe(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Khach_Hang/DatVeTau/Timkiem_VeTau/Tim_kiemVe.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_DatVe.initStyle(StageStyle.TRANSPARENT);
        Stage_DatVe.setScene(scene_quanlykhachhang);

        Stage_DatVe.show();
        My_Stage.close();
    }

    @FXML
    void Click_Switch_VeDaDat(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Khach_Hang/DatVeTau/Ve_TauDaDat/Ve_DaDat.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_VeDaDat.initStyle(StageStyle.TRANSPARENT);
        Stage_VeDaDat.setScene(scene_quanlykhachhang);

        Stage_VeDaDat.show();
        My_Stage.close();
    }

    @FXML
    void Click_log_out(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Main/login-view.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_Login.initStyle(StageStyle.TRANSPARENT);
        Stage_Login.setScene(scene_quanlykhachhang);

        Stage_Login.show();
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
    void Cancel_Click(MouseEvent event) throws IOException {
        My_Stage =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Parent root2 = FXMLLoader.load(getClass().getResource("/Khach_Hang/DatVeTau/Timkiem_VeTau/Tim_kiemVe.fxml"));
        Scene scene_quanlykhachhang = new Scene(root2);
        Stage_DatVe.initStyle(StageStyle.TRANSPARENT);
        Stage_DatVe.setScene(scene_quanlykhachhang);

        Stage_DatVe.show();
        My_Stage.close();
    }


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        toggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGroup);
        femaleRadioButton.setToggleGroup(toggleGroup);
        loadUserData(ID_Account);

        Label_TenKH.setText(passenger.getHo_va_Ten());
        //dobpicker.setValue(passenger.getDate());
    }

}



