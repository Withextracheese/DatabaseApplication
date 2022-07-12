package view;

import db.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Main screen controller for getting to different screens
 */

public class MainController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * move to add customer screen
     *
     */

    public void OnAAddCustScreen(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("addCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * move to modify customer screen
     *
     */

    public void OnAModCustScreen(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/modifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * move to add appointment screen
     *
     */

    public void OnAAddApptScreen(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/addAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * move to modify appointment screen
     *
     */

    public void OnAModApptScreen(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/modifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * move to screen with full calendar
     *
     */

    public void OnAModApptScreenforCal(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/modifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * move to report screen
     *
     */

    public void OnAReportScreen(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * exit program
     *
     */

    public void OnAExitProgram(ActionEvent actionEvent) {
        System.exit(0); // code to exit program
    }

    /**
     * Checks for appt within 15 minutes of login
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

