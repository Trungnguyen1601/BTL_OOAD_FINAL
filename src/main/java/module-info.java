module com.example.vetau {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vetau.App.Admin.QuanlyKhachHang to javafx.fxml;
    exports com.example.vetau.App.Admin.QuanlyKhachHang;
    opens com.example.vetau.App.Admin.QuanLyTau to javafx.fxml;
    exports com.example.vetau.App.Admin.QuanLyTau;
    exports com.example.vetau.helpers;

    opens com.example.vetau.helpers to javafx.fxml;

    opens com.example.vetau.models to javafx.base;
    exports com.example.vetau.Main;
    opens com.example.vetau.Main to javafx.fxml;
    exports com.example.vetau.App.Admin.Quanlychuyentau;
    opens com.example.vetau.App.Admin.Quanlychuyentau to javafx.fxml;

    opens com.example.vetau.App.Admin.QuanLyTau.ThemTau to javafx.fxml;
    exports com.example.vetau.App.Admin.QuanLyTau.ThemTau;
    opens com.example.vetau.App.Admin.QuanLyTau.EditTrain to javafx.fxml;
    exports com.example.vetau.App.Admin.QuanLyTau.EditTrain;
    exports com.example.vetau.Password;
    opens com.example.vetau.Password to javafx.fxml;
    exports com.example.vetau.App.KhachHang.DatVe_Tau.DatVe;
    opens com.example.vetau.App.KhachHang.DatVe_Tau.DatVe to javafx.fxml;

    exports com.example.vetau.App.KhachHang.DatVe_Tau.Timkiem_VeTau;
    opens com.example.vetau.App.KhachHang.DatVe_Tau.Timkiem_VeTau to javafx.fxml;

    exports com.example.vetau.App.KhachHang.ThongtinKH;
    opens com.example.vetau.App.KhachHang.ThongtinKH to javafx.fxml;

    exports com.example.vetau.App.KhachHang.DatVe_Tau.Ve_TauDaDat;
    opens com.example.vetau.App.KhachHang.DatVe_Tau.Ve_TauDaDat to javafx.fxml;



}