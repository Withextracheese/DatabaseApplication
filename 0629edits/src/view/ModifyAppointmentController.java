package view;

import db.JDBC;
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
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Modify appointment controller for modifying appointments
 */

public class ModifyAppointmentController implements Initializable {

    public TableView<Appointment> ModifyApptTV;
    public TableColumn<Appointment, Integer> ApptIDColModAppt;
    public TableColumn<Appointment, String> TitleColModAppt;
    public TableColumn<Appointment, String> DescColModAppt;
    public TableColumn<Appointment, String> LocColModAppt;
    public TableColumn<Appointment, String> ContactColModAppt;
    public TableColumn<Appointment, String> TypeColModAppt;
    public TableColumn<Appointment, String> StartColModAppt;
    public TableColumn<Appointment, String> EndColModAppt;
    public TableColumn<Appointment, Integer> CustIDModAppt;
    public TableColumn<Appointment, Integer> UserIDColModAppt;
    public TextField TFLocation;
    public ComboBox CBEndTime;
    public ComboBox CBStartTime;
    public TextField TFApptIDLock;
    public TextField TFTitle;
    public TextField TFDescription;
    public TextField TFCustID;
    public TextField TFUserID;
    public TextField TFType;
    public ComboBox CBContactName;
    public DatePicker DPStartDate;
    public DatePicker DPEndDate;
    public RadioButton RBWeek;
    public ToggleGroup TogWeekMonth;
    public RadioButton TGMonth;
    public RadioButton TGAll;
    Stage stage;
    Parent scene;
    private Appointment ap;
    private static Appointment ApptModify;
    private static Appointment DeleteAppt;
    private final LocalTime open = LocalTime.of(8, 0);
    private final LocalTime close = LocalTime.of(22, 0);

    /**
     * Return to main
     */

    public void OnAReturntoMain(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Save edit and refresh table
     */

    public void OnASaveEditsRefreshTable(ActionEvent actionEvent) throws IOException {

        // need to learn how shorten these and/or how to nest these correctly
        if (TFTitle.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter Title");
            alert.showAndWait();
            return;
        }
        if (TFDescription.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter Description");
            alert.showAndWait();
            return;
        }
        if (TFLocation.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter location");
            alert.showAndWait();
            return;
        }
        if (TFCustID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter customer ID");
            alert.showAndWait();
            return;
        }
        if (TFUserID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter User ID");
            alert.showAndWait();
            return;
        }
        if (TFType.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Enter type");
            alert.showAndWait();
            return;
        }
        //not working
        if (DPStartDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("choose Start date");
            alert.showAndWait();
            return;
        }
        //not working
        if (DPEndDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("choose end date");
            alert.showAndWait();
            return;
        }

        //String apptId = //auto incremented?
        String title = TFTitle.getText();
        String desc = TFDescription.getText();
        String loc = TFLocation.getText();
        String type = TFType.getText();
        LocalDateTime start = LocalDateTime.of(DPStartDate.getValue(), LocalTime.parse(CBStartTime.getValue().toString()));
        LocalDateTime end = LocalDateTime.of(DPEndDate.getValue(), LocalTime.parse(CBEndTime.getValue().toString()));
        String custId = TFCustID.getText();
        String userId = TFUserID.getText();
        String contactId = CBContactName.getValue().toString();
        String apptId = TFApptIDLock.getText();

        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdatedBy = "application";

        LocalDateTime createDate;
        String createdBy;

        createDate = LocalDateTime.now();
        createdBy = "application";

        updateAppt(title, desc, loc, type, start, end, createDate,createdBy, lastUpdate, lastUpdatedBy,
                custId, userId, contactId, apptId);

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/modifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Insert appointment using several parameters needed for insert
     */

    public void updateAppt(String title, String desc, String loc, String type, LocalDateTime start,
                           LocalDateTime end, LocalDateTime createDate, String createdBy,
                           LocalDateTime lastUpdate, String lastUpdatedBy, String custId, String userID,
                           String contactId, String appttID)
    {
        try {
            int cnt = 0;
            for (model.Contact Contact : Query.getAllContacts()) {
                if (CBContactName.getSelectionModel().getSelectedItem().equals(Contact.getContactName())) {
                    cnt = Contact.getContactId();
                }
            }

            LocalDate date = DPStartDate.getValue();
            LocalDate date1 = DPEndDate.getValue();

            if (!date.equals(date1)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Appointment start and end date must be the same date");
                alert.showAndWait();
            }

            String cid = TFCustID.getText();
            LocalDateTime startTime = LocalDateTime.of(DPStartDate.getValue(), LocalTime.parse(CBStartTime.getValue().toString()));
            LocalDateTime endTime = LocalDateTime.of(DPEndDate.getValue(), LocalTime.parse(CBEndTime.getValue().toString()));

            if (checkAppt(cid, startTime, endTime)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Overlapping appointment times.");
                alert.showAndWait();
            } else {
                String sql = "UPDATE APPOINTMENTS SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Create_Date=?," +
                        "Created_By=?, Last_Update=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? " +
                        "WHERE Appointment_ID=?";

                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, title);
                ps.setString(2, desc);
                ps.setString(3, loc);
                ps.setString(4, type);
                ps.setTimestamp(5, Timestamp.valueOf(start));
                ps.setTimestamp(6, Timestamp.valueOf(end));
                ps.setTimestamp(7, Timestamp.valueOf(createDate));
                ps.setString(8, createdBy);
                ps.setTimestamp(9, Timestamp.valueOf(lastUpdate));
                ps.setString(10, lastUpdatedBy);
                ps.setString(11, custId);
                ps.setString(12, userID);
                ps.setInt(13, cnt);
                ps.setString(14, appttID);
                ps.executeUpdate();
            }
        }
        catch(SQLException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Couple different checks for overlap of appointments
     */

    public static boolean checkAppt(String cid, LocalDateTime apStart, LocalDateTime apEnd) throws SQLException {
        ObservableList<Appointment> appts = Query.allApptinfo(cid);

        for(Appointment a: appts) {
            LocalDateTime apptstart = a.apptStart;
            LocalDateTime apptend = a.apptEnd;

            if((apStart.isAfter(apptstart) || apStart.isEqual(apptstart)) && apStart.isBefore(apptend)) {
                return true;
            }

            else if((apStart.isBefore(apptstart) || apStart.isEqual(apptstart)) && (apEnd.isAfter(apptend) || apEnd.isEqual(apptend))) {
                return true;
            }

            else if((apStart.isBefore(apptstart) || apStart.isEqual(apptstart)) && (apEnd.isAfter(apptend) || apEnd.isEqual(apptend))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Choose appt to edit and fill boxes
     */

    public void OnAEditAppt(ActionEvent actionEvent) {
        ApptModify = ModifyApptTV.getSelectionModel().getSelectedItem();

        if (ApptModify == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Select customer to modify");
            alert.showAndWait();

        }
        else {

            //fill all text boxes
            TFApptIDLock.setText(String.valueOf(ApptModify.getApptId()));
            //TFCustID.setText(String.valueOf(ApptModify.getCustID()));
            TFTitle.setText(String.valueOf(ApptModify.getApptName()));
            TFDescription.setText(String.valueOf(ApptModify.getApptDescription()));
            TFLocation.setText(String.valueOf(ApptModify.getApptLocation()));
            TFCustID.setText(String.valueOf(ApptModify.getCustID()));
            TFUserID.setText(String.valueOf(ApptModify.getUserID()));
            TFType.setText(String.valueOf(ApptModify.getApptType()));
            LocalDate ld = LocalDate.now();
            ObservableList<String> time = Query.getAppointmentTimes(LocalDateTime.of(ld, open), LocalDateTime.of(ld, close));
            CBStartTime.setItems(time);
            CBEndTime.setItems(time);
            //CBStartTime.set;
            CBStartTime.setValue(String.valueOf(ApptModify.getApptStart().toLocalTime()));
            CBEndTime.setValue(String.valueOf(ApptModify.getApptEnd().toLocalTime()));
            CBContactName.setValue(ApptModify.getContactName());

            //DP
            LocalDateTime d = ApptModify.getApptStart();
            DPStartDate.setValue(LocalDate.from(d));
            DPEndDate.setValue(LocalDate.from(d));
        }
    }


    /**
     * Delete appointment and provide prompt with appointment ID and type
     */

    public void OnADeleteAppt(ActionEvent actionEvent) {
        //Select the appointment to delete
        DeleteAppt = ModifyApptTV.getSelectionModel().getSelectedItem();
        //Make sure a appointment was selected
        try {
            if (ModifyApptTV.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Appointment not deleted. Select appointment to delete");
                alert.showAndWait();
                return;
            }
            {
                TFType.setText(String.valueOf(DeleteAppt.getApptType()));
                TFApptIDLock.setText(String.valueOf(DeleteAppt.getApptId()));
                String appid = TFApptIDLock.getText();
                String appty = TFType.getText();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to delete appointment with appointment ID: '" + appid + "' and type: '" + appty + "' Press Ok. Otherwise close prompt");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {

                    int delete = DeleteAppt.getApptId();
                    Query.delete(delete);

                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("../view/modifyAppointment.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }



    /**
     * refresh modify appt tableview
     */

    public void ButtonRefreshModApptTV(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/modifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void OnAweekRB(ActionEvent actionEvent) throws SQLException {
        ModifyApptTV.setItems(Query.weekApptinfo());
    }

    public void OnAMonthapptRB(ActionEvent actionEvent) throws SQLException {
        ModifyApptTV.setItems(Query.monthApptinfo());
    }

    public void OnAallapptRB(ActionEvent actionEvent) throws SQLException {
        ModifyApptTV.setItems(Query.allApptinfo());
    }

    /**
     * sets tableview, sets radiobuttons with functions for week, monthly, and all appts
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //JDBC.openConnection();
        TFApptIDLock.setEditable(false);
        TGAll.setSelected(true);
        TFCustID.setEditable(false);
        TFUserID.setEditable(false);


        try {
            ModifyApptTV.setItems(Query.allApptinfo());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ApptIDColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        TitleColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptName"));
        DescColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        LocColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        ContactColModAppt.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        TypeColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        StartColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
        EndColModAppt.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));
        CustIDModAppt.setCellValueFactory(new PropertyValueFactory<>("custID"));
        UserIDColModAppt.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
