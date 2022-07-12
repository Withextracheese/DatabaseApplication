package view;


import com.sun.javafx.charts.Legend;
import db.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Login controller for logging into the appointment application
 */

public class LoginController implements Initializable {
    public TextField TFUserID;
    public TextField TFPassword;
    public Label LanguageLable;
    public Label LabelUserID;
    public Label LabelPW;
    public Label LabelEnter;
    public Label communicate;
    public Button Enterbutton;
    public Button refresh;
    Stage stage;
    Parent scene;

   private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages/lang", Locale.getDefault());


    public void OnArefresh(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    /**
     * sets errors for username and password in each language logs successful and unsuccessful attempts
     * moves to main screen then checks for appts within 15 mins Lambda #1 tells user to refresh when appropriate
     * This fixed when the submit button doesn't function after an attempted unsuccessful login
     */
    //https://stackoverflow.com/questions/31726418/localdatetime-remove-the-milliseconds
    public void OnALogin(ActionEvent actionEvent) throws IOException {
        String un = TFUserID.getText();
        boolean authorized = Query.login(un, TFPassword.getText());
        //lambda 1
        //this fixes when the login screen needs a refresh and enter isn't working. It prompts the user to refresh if screen needs it
        Enterbutton.setOnAction(e -> System.out.println("Press refresh"));

        if (TFUserID.getText().isEmpty() || TFPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resourceBundle.getString("Error"));
            alert.setContentText(resourceBundle.getString("LoginError"));
            alert.showAndWait();
            return;

        }

        FileWriter fw = new FileWriter("src/login_activity.txt", true);
        PrintWriter pw = new PrintWriter(fw);

        if (!authorized) {
            LocalDateTime ldt = LocalDateTime.now();
            pw.println("Unsuccessful login by " + un + " " + ldt.withNano(0));
            pw.close();
            communicate.setText(resourceBundle.getString("LoginError"));
            return;


        } else {
            LocalDateTime ldt1 = LocalDateTime.now();
            pw.println("Successful login by" + un + " " + ldt1.withNano(0));
            pw.close();

            Appointment appt = null;
            try {
                appt = Query.apptCheck();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (appt != null) {
                int appid = appt.getApptId();
                LocalDateTime appty = appt.getApptStart();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Appointment ID: " + appid + " has an appointment at " + appty + " ");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("There are no appointments within 15 minutes");
                alert.showAndWait();
            }


            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

        Enterbutton.setOnAction(e -> System.out.println("Refresh Screen"));

    }



    /**
     * sets the labels with correct language English or French
     **/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle=ResourceBundle.getBundle("languages/lang", Locale.getDefault());
        communicate.setText(resourceBundle.getString("communicate"));
        LabelUserID.setText(resourceBundle.getString("LabelUserID"));
        LabelPW.setText(resourceBundle.getString("LabelPW"));
        LanguageLable.setText(ZoneId.systemDefault().toString());
        LabelEnter.setText(resourceBundle.getString("LabelEnter"));
    }

}
