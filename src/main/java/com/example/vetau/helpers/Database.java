package com.example.vetau.helpers;


import com.example.vetau.models.Account;
import com.example.vetau.models.Passenger;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;

public class Database {
    Connection connection = connectionDB();
    public static Connection connectionDB (){
        try{
            //Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlyvetau", "root", "trung1601");
            return  connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet SELECT_ALL_FROM_TABLE(Connection connection,String TableName, String ColumnName)
    {
        String sql = "SELECT * "+ " FROM " + TableName + " WHERE " + ColumnName ;

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }
    public static ResultSet SELECT_String_FROM_TABLE(Connection connection,String TableName, String ColumnName,String Data)
    {
        String sql = "SELECT * "+ " FROM " + TableName + " WHERE " + ColumnName + " = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,Data);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }
    public static ResultSet SELECT_INT_FROM_TABLE(Connection connection,String TableName, String ColumnName,int Data)
    {
        String sql = "SELECT *" + ColumnName + " FROM " + TableName + " WHERE " + ColumnName + " = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,Data);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }
    public static void DELETE_String_FROM_TABLE(Connection connection,String TableName, String ColumnName,String Data)
    {
        String sql = "DELETE" + " FROM " + TableName + " WHERE " + ColumnName + " = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,Data);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }
    public static void DELETE_INT_FROM_TABLE(Connection connection,String TableName, String ColumnName,int Data)
    {
        String sql = "DELETE" + " FROM " + TableName + " WHERE " + ColumnName + " = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,Data);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }
    public static void UPDATE_String_DATA_FROM_TABLE(Connection connection,String TableName, String ColumnName_Set, String Data,String ColumnName_Where, String row_Data)
    {
        String query = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;

        query = "UPDATE " + TableName +"\n" +
                "SET " + ColumnName_Set + " = ?\n" +
                "WHERE " + ColumnName_Where +" = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Data);
            preparedStatement.setString(2,row_Data);
            int check = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void UPDATE_Int_DATA_FROM_TABLE(Connection connection,String TableName, String ColumnName_Set, int Data,String ColumnName_Where, String row_Data)
    {
        String query = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;

        query = "UPDATE " + TableName +"\n" +
                "SET " + ColumnName_Set + " = ?\n" +
                "WHERE " + ColumnName_Where +" = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Data);
            preparedStatement.setString(2,row_Data);
            int check = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean Check_ExistInDatabase(Connection connection,String TableName, String ColumnName,String Data) throws SQLException {
        boolean flag_exist = false;
        ResultSet resultSet = SELECT_String_FROM_TABLE(connection,TableName,ColumnName,Data);
        if(resultSet.next())
        {
            flag_exist = true;
        }
        return flag_exist;
    }
    static void Insert() throws SQLException {
        Connection connection = Database.connectionDB();
        int count = 0;
        for (int i =1; i <= 5;i++) {
            String query1 = "SELECT Soluongtoa from tau WHERE ID_Tau = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setString(1,"TAU"+i);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("Soluongtoa");
                    System.out.println(count);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        String query = "INSERT INTO toa_tau (ID_Toatau, ID_Tau, Soluongghe) VALUES (?, ?, ?)\n";
        for (int j = 6; j<=count; j++)
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"TOA" + j + "TAU" + i);
            preparedStatement.setString(2,"TAU"+i);
            preparedStatement.setInt(3,20);
            preparedStatement.executeUpdate();
        }
        }
    }
    static void Update() throws SQLException {
        Connection connection = Database.connectionDB();
        int count = 0;
        String loaitoa = null;
        for (int i =1; i <= 5;i++) {
            String query1 = "SELECT Soluongtoa from tau WHERE ID_Tau = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setString(1,"TAU"+i);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("Soluongtoa");
                    System.out.println(count);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String query = "UPDATE toa_tau SET Loaitoa = ?\n" +
                          "WHERE (ID_Toatau = ? ) and (ID_Tau = ?)";
            for (int j = 1; j<=count; j++)
            {
                if (j <= 5)
                {
                    loaitoa = "Vip";
                }
                else {
                    loaitoa = "Thuong";
                }
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,loaitoa);
                    preparedStatement.setString(2,"TOA"+j+"TAU"+i);
                    preparedStatement.setString(3,"TAU"+i);
                    System.out.println("TOA"+i+"TAU"+j + " " + loaitoa);
                    preparedStatement.executeUpdate();
            }
        }

    }
    public Account fetchAccount(String ID_Account) {
        Account account = null;
        try {
            String query = "SELECT * FROM account_user WHERE ID_Account = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID_Account);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String UserName = resultSet.getString("Username");
                String Password = resultSet.getString("Password");
                account = new Account(ID_Account, UserName, Password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return account;
    }

    public Passenger fetchUser(String ID_Account) {
        Passenger passenger = null;
        Account account = fetchAccount(ID_Account);
        try {
            // Execute a SQL query to retrieve user data
            String query = "SELECT ID_Khachhang, Ten, CCCD, Ngay_sinh, Gioi_tinh, ID_Account, Email, Level, Dia_chi, SDT FROM khach_hang WHERE ID_Account = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ID_Account);

            ResultSet resultSet = statement.executeQuery();

            // Fetch the user data from the result set
            if (resultSet.next()) {
                String ID_Passenger = resultSet.getString("ID_Khachhang");
                String Ten = resultSet.getString("Ten");
                String CCCD = resultSet.getString("CCCD");
                Date Ngay_sinh = resultSet.getDate("Ngay_sinh");
                String Gioi_tinh = resultSet.getString("Gioi_tinh");
                String Email = resultSet.getString("Email");
                int Level = resultSet.getInt("Level");
                String Dia_chi = resultSet.getString("Dia_chi");
                String SDT = resultSet.getString("SDT");

                passenger = new Passenger(ID_Passenger, Ten, CCCD, Gioi_tinh, Ngay_sinh, Dia_chi, SDT, Email, Level, account);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return passenger;
    }

    public void updateUser(Passenger passenger) {
        try {
            // Execute a SQL update query to update the user data
            String query = "UPDATE khach_hang SET Ten = ?, Ngay_sinh = ?, CCCD = ?, Email = ?, Dia_chi = ?, SDT = ?, Gioi_tinh = ? WHERE ID_Khachhang = \"" + passenger.getID_Passenger() + '"';
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, passenger.getHo_va_Ten());
            statement.setDate(2, java.sql.Date.valueOf(passenger.getLocalDate()));
            statement.setString(3, passenger.getCccd());
            statement.setString(4, passenger.getEmail()); // Assuming the User class has an "id" property
            statement.setString(5, passenger.getAddress());
            statement.setString(6, passenger.getPhone());
            statement.setString(7, passenger.getGender());
            statement.executeUpdate();

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Connection connection = connectionDB();
        UPDATE_String_DATA_FROM_TABLE(connection,"tau","Trangthai","Yes","ID_Tau","TAU");

    }
}
