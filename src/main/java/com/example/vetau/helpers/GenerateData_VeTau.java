package com.example.vetau.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class GenerateData_VeTau {


    public static void Generate_data() throws SQLException {
        Connection connection = Database.connectionDB();
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        Random random = new Random();

        int [] giave = {100000,120000,140000,150000, 160000};

        int count = 1;
        query = "SELECT * FROM chuyen_tau";

        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            String ID_Chuyentau = resultSet.getString("ID_Chuyentau");
            String Tau = resultSet.getString("ID_Tau");
            int select_giave = random.nextInt(4);
            int Giave_tau = giave[select_giave];
            query = "SELECT * FROM toa_tau WHERE ID_Tau = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Tau);
            resultSet1 = preparedStatement.executeQuery();
            while (resultSet1.next())
            {
                int Giave_chitiet = 0;
                String Toa_Tau = resultSet1.getString("ID_Toatau");
                int Soluongghe = resultSet1.getInt("Soluongghe");
                String Loai_Toa = resultSet1.getString("Loaitoa");
                if(Loai_Toa.equals("Thuong"))
                {
                    Giave_chitiet = Giave_tau * 1;
                }
                else if(Loai_Toa.equals("Vip")) {
                    Giave_chitiet = (int) (Giave_tau * 1.5);
                }
                for (int i = 1; i<=Soluongghe; i++)
                {
                    query = "INSERT INTO ve_tau (STT,ID_Vetau,ID_Chuyentau,ID_Toatau,Vitringoi,Giave,Tinhtrangve) " +
                            "VALUES (?,?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,count);
                    preparedStatement.setString(2,ID_Chuyentau+"_"+Toa_Tau+"_"+i);
                    preparedStatement.setString(3,ID_Chuyentau);
                    preparedStatement.setString(4,Toa_Tau);
                    preparedStatement.setInt(5,i);
                    preparedStatement.setInt(6,Giave_chitiet);
                    preparedStatement.setString(7,"Chưa mua");

                    preparedStatement.executeUpdate();

                    count = count +1;
                }
            }
        }


    }
    public static void GenerateData_VeTau_AddTrain(String ID_Chuyentau,String ID_Tau ) throws SQLException {
        Connection connection = Database.connectionDB();
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        Random random = new Random();
        int [] giave = {100000,120000,140000,150000, 160000};
        int count =1;

        String ID_Chuyentau_gen = ID_Chuyentau;
        String Tau = ID_Tau;
        int select_giave = random.nextInt(4);
        int Giave_tau = giave[select_giave];
        query = "SELECT * FROM toa_tau WHERE ID_Tau = ?";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,Tau);
        resultSet1 = preparedStatement.executeQuery();
        while (resultSet1.next())
        {
            int Giave_chitiet = 0;
            String Toa_Tau = resultSet1.getString("ID_Toatau");
            int Soluongghe = resultSet1.getInt("Soluongghe");
            String Loai_Toa = resultSet1.getString("Loaitoa");
            if(Loai_Toa.equals("Thuong"))
            {
                Giave_chitiet = Giave_tau * 1;
            }
            else if(Loai_Toa.equals("Vip")) {
                Giave_chitiet = (int) (Giave_tau * 1.5);
            }
            for (int i = 1; i<=Soluongghe; i++)
            {
                query = "INSERT INTO ve_tau (STT,ID_Vetau,ID_Chuyentau,ID_Toatau,Vitringoi,Giave,Tinhtrangve) " +
                        "VALUES (?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,count);
                preparedStatement.setString(2,ID_Chuyentau+"_"+Toa_Tau+"_"+i);
                preparedStatement.setString(3,ID_Chuyentau);
                preparedStatement.setString(4,Toa_Tau);
                preparedStatement.setInt(5,i);
                preparedStatement.setInt(6,Giave_chitiet);
                preparedStatement.setString(7,"Chưa mua");

                preparedStatement.executeUpdate();

                count = count +1;
            }
        }
    }

    public static  void Delete_All() throws SQLException {
        Connection connection = Database.connectionDB();
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        int Max = 90;

        query = "SELECT MAX(STT) as max FROM ve_tau";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            Max = resultSet.getInt("max");
        }
        for (int i = 1; i<=Max;i++)
        {
            query = "DELETE FROM ve_tau WHERE STT = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,i);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) throws SQLException {
        Generate_data();
        //Delete_All();

    }

}
