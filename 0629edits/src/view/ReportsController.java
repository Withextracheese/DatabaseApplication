package view;

import db.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Reports controller
 */

public class ReportsController implements Initializable{
    public TextArea TArea;
    public ComboBox CBreports;
    public TableView TVReports;
    public TableColumn ReportsCol;
    Stage stage;
    Parent scene;
    static ObservableList<String> reportList = FXCollections.observableArrayList();

    /**
     * return to main
     */

    public void OnAReturntoMain(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * refresh table
     */

    public void OnARefresh(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * report of appts by type and month
     */

    public void OnATypeAndMonth(ActionEvent actionEvent) throws SQLException {
        TArea.setText(Query.reportApptTypesByMonth());
    }
    /**
     * Contact schedule report
     */

    public void ScheduleForContact(ActionEvent actionEvent) throws SQLException {
        TArea.setText(Query.reportContactSchedule());
    }

    /**
     * report of
     */

    public void TotalNumberApptsinUS(ActionEvent actionEvent) throws SQLException {
        TArea.setText(Query.reportTotal());
    }

    /**
     * report of appts by type and month
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ObservableList<String> reports = getAllReports();
        //CBreports.setItems(reports);
        //ReportsCol.setCellValueFactory(new PropertyValueFactory<>("Reports"));
    }



}
