package com.example.vetau.Password;

import com.example.vetau.Main.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.vetau.Main.MainController.pass;

public class Show_PassWordController implements Initializable {
    public static boolean Flag_show_pass = false;
    public TextField textfield_showPass_signup;

    @FXML
    void Copy_PassWord(MouseEvent event) {
        Flag_show_pass = true;
    }
    @FXML
    void Close_Show_Pass(MouseEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textfield_showPass_signup.setEditable(false);
        textfield_showPass_signup.setText(pass);
    }
}
