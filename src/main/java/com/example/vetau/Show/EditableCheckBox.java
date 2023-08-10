package com.example.vetau.Show;

import com.example.vetau.App.Admin.QuanLyTau.QuanLyTauController;
import com.example.vetau.helpers.Check_Status;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.ChitietTau;
import com.example.vetau.models.Tau;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

import java.sql.*;
import java.util.Optional;

public class EditableCheckBox<S, T> extends CheckBoxTableCell<S, T> {

    private final CheckBox checkBox;

    Alert alert = null;
    boolean status;
    boolean Updatestatus = false;

    public EditableCheckBox() {

        this.checkBox = new CheckBox();
        this.checkBox.setDisable(false);
        this.checkBox.setOnMouseClicked(event -> {
            if (getTableRow() != null) {
                TableView<S> tableView = getTableView();
                S rowData = tableView.getItems().get(getIndex());
                TableColumn<S, T> column = getTableColumn();
                Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> cellValueFactory = column.getCellValueFactory();
                ObservableValue<T> cellObservableValue = cellValueFactory.call(new TableColumn.CellDataFeatures<>(tableView, column, rowData));
                if (cellObservableValue instanceof SimpleBooleanProperty) {
                    SimpleBooleanProperty booleanProperty = (SimpleBooleanProperty) cellObservableValue;
//                    booleanProperty.set(this.checkBox.isSelected());

                }
            }
        });
//        if(QuanLyTauController.flag_for_alertInformation)
//        {
//            this.checkBox.setDisable(false);
//        }
//        else {
//            this.checkBox.setDisable(true);
//        }

        this.checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Updatestatus) {
                return;
            }
            S rowData = getTableRow().getItem();
            status = this.checkBox.isSelected();

            if (rowData instanceof Tau) {
                Connection connection = Database.connectionDB();
                // In ra thông tin Person khi trạng thái của ô đánh dấu tích thay đổi
                Tau tau = (Tau) rowData;
                System.out.println(tau.getIDTau() + "Trạng thái " + status);
                System.out.println("Trạng thái cờ " + QuanLyTauController.flag_for_alertInformation);

                if(QuanLyTauController.flag_for_alertInformation) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    if (!this.checkBox.isSelected()) {
                        alert.setContentText("Bạn không muốn sử dụng tàu không?");
                    } else {
                        alert.setContentText("Bạn có muốn sử dụng tàu không?");
                    }
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        Update_status(connection, "tau", "Trangthai", status, "ID_Tau", ((Tau) rowData).getIDTau());
                    } else
                    {
                        Updatestatus = true; // Tạm thời tắt lắng nghe sự kiện khi thiết lập lại trạng thái CheckBox
                        this.checkBox.setSelected(!status); // Thiết lập lại giá trị trạng thái CheckBox thành giá trị cũ
                        Updatestatus = false; // Bật lại lắng nghe sự kiện
                        alert.close();
                    }
                }


            }
            if (rowData instanceof ChitietTau)
            {
                Connection connection = Database.connectionDB();
                ChitietTau tau = (ChitietTau) rowData;
                System.out.println(tau.getID_Tau() + tau.getID_toa() ) ;
                if(QuanLyTauController.flag_for_alertInformation) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    if (!this.checkBox.isSelected()) {
                        alert.setContentText("Bạn không muốn sử dụng tàu không?");
                    } else {
                        alert.setContentText("Bạn có muốn sử dụng tàu không?");
                    }
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        Update_status(connection, "toa_tau", "Trangthai", status, "ID_Toatau", ((ChitietTau) rowData).getID_toa());
                    } else
                    {
                        Updatestatus = true; // Tạm thời tắt lắng nghe sự kiện khi thiết lập lại trạng thái CheckBox
                        this.checkBox.setSelected(!status); // Thiết lập lại giá trị trạng thái CheckBox thành giá trị cũ
                        Updatestatus = false; // Bật lại lắng nghe sự kiện
                        alert.close();
                    }

                }


            }
        });

    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            setGraphic(checkBox);
            checkBox.setSelected((Boolean) item);

        }
    }
    private void Update_status(Connection connection,String TableName, String ColumnName, boolean status,String row_Name, String row_Data)
    {
        String query = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String trangthai = Check_Status.ReturnString_Check(status);
        query = "UPDATE " + TableName +"\n" +
                "SET " + ColumnName + " = ?\n" +
                "WHERE " + row_Name +" = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,trangthai);
            preparedStatement.setString(2,row_Data);
            int check = preparedStatement.executeUpdate();
            if (check != 0)
            {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Chỉnh sửa trạng thái thành công");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Chỉnh sửa trạng thái không thành công");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

