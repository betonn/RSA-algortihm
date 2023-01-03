module com.example.rsaalgorithm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rsaalgorithm to javafx.fxml;
    exports com.example.rsaalgorithm;
}