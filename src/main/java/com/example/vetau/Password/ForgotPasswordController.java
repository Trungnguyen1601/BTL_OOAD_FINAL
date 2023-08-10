package com.example.vetau.Password;

import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ForgotPasswordController {
    @FXML
    private Button accept_btn;

    @FXML
    private AnchorPane changePass_form;

    @FXML
    private CheckBox changePass_selectShowPass;

    @FXML
    private Button close_btn_changePass;

    @FXML
    private Button minimize_btn_changePass;

    @FXML
    private PasswordField newPass_change;

    @FXML
    private TextField showNewPass_change;

    @FXML
    private TextField showVerifyNewPass_change;

    @FXML
    private PasswordField verifyNewPass_change;
    @FXML
    private Button close_btn_forgotPass;

    @FXML
    private TextField email_forgotPass;

    @FXML
    private Button minimize_btn_forgotPass;

    @FXML
    private Button ok_btn;
    @FXML
    private TextField username_forgotPass;
    @FXML
    private AnchorPane forgotPass_form;
    private static String username;

    public static String getUsername() {
        return username;
    }
    // TOOLs FOR DATABASE
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    String query = null;
    private Alert alert;

    public void forgotPass_close() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to quit?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                /* TO HIDE MAIN FORM */
                close_btn_forgotPass.getScene().getWindow().hide();
                /* LINK YOUR LOGIN FORM AND SHOW IT */
                Parent root_logout = FXMLLoader.load(getClass().getResource("/Main/login-view.fxml"));
                Stage stage_logout = new Stage();
                Scene scene_logout = new Scene(root_logout);
                stage_logout.initStyle(StageStyle.TRANSPARENT);
                stage_logout.setScene(scene_logout);
                stage_logout.show();
            }
            else {
                // Retrieve the current stage and hide it
                Stage currentStage = (Stage) close_btn_forgotPass.getScene().getWindow();
                currentStage.hide();

                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/forgot_Password.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Create a new stage and set the scene
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forgotPass_minimize() {
        Stage stage = (Stage) forgotPass_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void OK_btn() {
        if (username_forgotPass.getText().isEmpty() || email_forgotPass.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please complete all information!");
            Optional<ButtonType> option = alert.showAndWait();
        }
        else {
            query = "SELECT \n" +
                    "    account_user.Username,\n" +
                    "    khach_hang.Email\n" +
                    "    FROM khach_hang \n" +
                    "    INNER JOIN account_user ON khach_hang.ID_Account = account_user.ID_Account \n" +
                    "    WHERE account_user.Username = ? AND khach_hang.Email = ?";
            connection = Database.connectionDB();
            username = username_forgotPass.getText();
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username_forgotPass.getText());
                preparedStatement.setString(2, email_forgotPass.getText());

                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    ok_btn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/Main/changePassword.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid username or email!");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* Change password controller */
    public void changePass_close() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Error Message");
//            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to quit?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                /* TO HIDE MAIN FORM */
                close_btn_changePass.getScene().getWindow().hide();
                /* LINK YOUR LOGIN FORM AND SHOW IT */
                Parent root_logout = FXMLLoader.load(getClass().getResource("/Main/login-view.fxml"));
                Stage stage_logout = new Stage();
                Scene scene_logout = new Scene(root_logout);
                stage_logout.initStyle(StageStyle.TRANSPARENT);
                stage_logout.setScene(scene_logout);
                stage_logout.show();
            }
            else {
                // Retrieve the current stage and hide it
                Stage currentStage = (Stage) close_btn_changePass.getScene().getWindow();
                currentStage.hide();

                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/changePassword.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Create a new stage and set the scene
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePass_minimize() {
        Stage stage = (Stage) changePass_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void showPassword() {
        if(changePass_selectShowPass.isSelected()) {
            showNewPass_change.setText(newPass_change.getText());
            showNewPass_change.setVisible(true);
            newPass_change.setVisible(false);

            showVerifyNewPass_change.setText(verifyNewPass_change.getText());
            showVerifyNewPass_change.setVisible(true);
            verifyNewPass_change.setVisible(false);
        } else {
            showNewPass_change.setText(newPass_change.getText());
            showNewPass_change.setVisible(false);
            newPass_change.setVisible(true);

            showVerifyNewPass_change.setText(verifyNewPass_change.getText());
            showVerifyNewPass_change.setVisible(false);
            verifyNewPass_change.setVisible(true);
        }
    }

    public void accept_btn () {
        if (newPass_change.getText().isEmpty() || verifyNewPass_change.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please complete all information!");
            Optional<ButtonType> option = alert.showAndWait();
        } else if (!newPass_change.getText().equals(verifyNewPass_change.getText())) {
            /* Check if the new password and confirmation are not match */
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Password does not match!");
            Optional<ButtonType> option = alert.showAndWait();
        } else {
            String updateData = "UPDATE account_user SET account_user.Password = ? WHERE account_user.Username = ?";
            connection = Database.connectionDB();
            try {
                preparedStatement = connection.prepareStatement(updateData);
                preparedStatement.setString(1, newPass_change.getText());
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully change password!");
                Optional<ButtonType> option = alert.showAndWait();

                accept_btn.getScene().getWindow().hide();
                String FXMLPATH = "/Main/login-view.fxml";
                Show_Window showWindow = new Show_Window();
                showWindow.Show(FXMLPATH);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
