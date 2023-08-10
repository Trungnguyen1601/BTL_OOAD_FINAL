package com.example.vetau.App.Admin.QuanlyKhachHang;

import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.Account;
import com.example.vetau.models.Passenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InformationPassengerController implements Initializable {
    @FXML
    private Button dashbroard_form_btn;
    @FXML
    private Button quanlytau_form_btn;
    @FXML
    private Button train_form_btn;
    @FXML
    private Button signout_btn;
    @FXML
    private TextField search_customer_txt ;
    @FXML
    private TableView<Passenger> Customer_Table;
    @FXML
    private TableColumn<Passenger, String> Email;
    @FXML
    private TableColumn<Passenger, String> ID_Khachhang;
    @FXML
    private TableColumn<Passenger, String> Name;
    @FXML
    private TableColumn<Passenger, String> Password;
    @FXML
    private TableColumn<Passenger, String> PhoneNumber;
    @FXML
    private TableColumn<Passenger, String> Username;
    @FXML
    private Button dashboard_close;
    @FXML
    private Button dashboard_minus;
    @FXML
    private TableColumn<Passenger, Button> Delete_Col;
    @FXML
    private TableColumn<Passenger, Button> Infor_Col;
    private Alert alert;
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Passenger customerInformation = null;
    Stage stage_dashbroard= new Stage();

    private static String ID_Customer;

    public static String getID_Customer() {
        return ID_Customer;
    }
    ObservableList<Passenger> CustomerList = FXCollections.observableArrayList();

    @FXML
    void Setdashboard_close(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Setdashboard_minus(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    private void loadData()
    {
        connection = Database.connectionDB();
        refreshTable();

        ID_Khachhang.setCellValueFactory(new PropertyValueFactory<>("ID_Passenger"));
        Name.setCellValueFactory(new PropertyValueFactory<>("ho_va_Ten"));
        PhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        Password.setCellValueFactory(new PropertyValueFactory<>("Password"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));

        searchCustomer();
    }

    private void refreshTable() {
        CustomerList.clear();
        Account account = new Account();
        try {
            query = "SELECT \n" +
                    "    khach_hang.Ten,\n" +
                    "    khach_hang.ID_Khachhang,\n" +
                    "    khach_hang.Email,\n" +
                    "    khach_hang.SDT,\n" +
                    "    account_user.ID_Account,\n" +
                    "    account_user.Username,\n" +
                    "    account_user.Password\n" +
                    "    From khach_hang\n" +
                    "    inner join account_user on khach_hang.ID_Account = account_user.ID_Account";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                account = new Account(resultSet.getString("ID_Account"), resultSet.getString("Username"), resultSet.getString("Password"));
                CustomerList.add(new Passenger(
                        resultSet.getString("ID_Khachhang"),
                        resultSet.getString("Ten"),
                        resultSet.getString("SDT"),
                        account,
                        resultSet.getString("Email")));
            }
            Customer_Table.setItems(CustomerList);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void Delete_inDB(String ID) {

        query = "DELETE khach_hang, account_user \n" +
                "\tFROM khach_hang \n" +
                "    INNER JOIN account_user ON khach_hang.ID_Account = account_user.ID_Account \n" +
                "    WHERE khach_hang.ID_Khachhang = ?";
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, ID);
                int check = preparedStatement.executeUpdate();
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        } else {
            refreshTable();
            alert.close();
        }
    }

    private void searchCustomer() {
        FilteredList<Passenger> filteredList = new FilteredList<>(CustomerList, b -> true);
        search_customer_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customerInformation -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customerInformation.getID_Passenger().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (customerInformation.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (customerInformation.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (customerInformation.getPhone().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Passenger> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(Customer_Table.comparatorProperty());
        Customer_Table.setItems(sortedList);
    }
    @FXML
    void Switch_quanlytau(MouseEvent event) throws IOException {
        stage_dashbroard =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
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
    void Switch_train_form(MouseEvent event) throws IOException {
        stage_dashbroard =  (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        String FXMLPATH_quanlychuyentau = "/Admin/dashboard.fxml";
        try {
            Show_Window showWindow = new Show_Window();
            showWindow.Show(FXMLPATH_quanlychuyentau);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage_dashbroard.close();
    }

    public  void  logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
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
                refreshTable();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void Button_Extend_KhachHang()
    {
        TableColumn<Passenger, Void> UpdateColumn = new TableColumn<>("Xem");
        Callback<TableColumn<Passenger, Void>, TableCell<Passenger, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Passenger, Void> call(final TableColumn<Passenger, Void> param) {
                final TableCell<Passenger, Void> cell = new TableCell<>() {
                    private final Button InforButton = new Button("Xem");

                    {
                        InforButton.setOnAction(event -> {

                            Passenger customerInformation = getTableView().getItems().get(getIndex());
                            ID_Customer = customerInformation.getID_Passenger();
                            String FXMLPATH = "/Admin/InformationView/Information_passenger.fxml";
                            try {
                                Show_Window showWindow = new Show_Window();
                                showWindow.Show(FXMLPATH);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

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
        UpdateColumn.setCellFactory(cellFactory);
        Customer_Table.getColumns().add(UpdateColumn);


        TableColumn<Passenger, Void> DeleteColumn = new TableColumn<>("Xóa");
        Callback<TableColumn<Passenger, Void>, TableCell<Passenger, Void>> cellFactory1 = new Callback<>() {
            @Override
            public TableCell<Passenger, Void> call(final TableColumn<Passenger, Void> param) {
                final TableCell<Passenger, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");

                    {
                        deleteButton.setOnAction(event -> {

                                Passenger customerInformation = getTableView().getItems().get(getIndex());
                                CustomerList.remove(customerInformation);
                                Delete_inDB(customerInformation.getID_Passenger());

                            //Tau_table.getItems().remove(item);

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
        DeleteColumn.setCellFactory(cellFactory1);
        Customer_Table.getColumns().add(DeleteColumn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        Button_Extend_KhachHang();

    }
}

