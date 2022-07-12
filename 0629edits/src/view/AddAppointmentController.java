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
import javafx.stage.Stage;
import model.*;
import model.User;

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
 * Add appointment controller for adding appointments
 */

public class AddAppointmentController implements Initializable {

    public TextField TFLocation;
    public ComboBox CBEndTime;
    public ComboBox CBStarttime;
    public TextField TFApptID;
    public TextField TFTitle;
    public TextField TFDescription;
    public TextField TFCustID;
    public TextField TFUserID;
    public TextField TFType;
    public ComboBox CBContact;
    public DatePicker DPStateDate;
    public DatePicker DPEnddate;
    public ComboBox CBCustID;
    public ComboBox CBUserID;
    Stage stage;
    Parent scene;
    private final LocalTime open = LocalTime.of(8, 0);
    private final LocalTime close = LocalTime.of(22, 0);

    /**
     * Return to main
     */

    public void OnARecturntoMain(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Add appt and return to main
     */

    public void OnAAddApptRtnToMain(ActionEvent actionEvent) {
        try {

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
            if (CBCustID.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter customer ID");
                alert.showAndWait();
                return;
            }
            if (CBUserID.getSelectionModel().isEmpty()) {
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
            if (DPStateDate == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose Start date");
                alert.showAndWait();
                return;
            }
            if (DPEnddate == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose end date");
                alert.showAndWait();
                return;
            }
            if (CBStarttime.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose start time");
                alert.showAndWait();
                return;
            }
            if (CBEndTime.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose end time");
                alert.showAndWait();
                return;
            }
            if (CBContact.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose contact");
                alert.showAndWait();
                return;
            }

        //String apptId = //auto incremented?
        String title = TFTitle.getText();
        String desc = TFDescription.getText();
        String loc = TFLocation.getText();
        String type = TFType.getText();
        LocalDateTime start = LocalDateTime.of(DPStateDate.getValue(), LocalTime.parse(CBStarttime.getValue().toString()));
        LocalDateTime end = LocalDateTime.of(DPEnddate.getValue(), LocalTime.parse(CBEndTime.getValue().toString()));
        String custId = CBCustID.getValue().toString();
        String userId = CBUserID.getValue().toString();
        String contactId = CBContact.getValue().toString();

        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdatedBy = "Application";

        LocalDateTime createDate;
        String createdBy;

        createDate = LocalDateTime.now();
        createdBy = "Application";

        insertAppt(title, desc, loc, type, start, end, createDate,createdBy, lastUpdate, lastUpdatedBy,
                custId, userId, contactId);

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }
        catch(NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert appt
     *
     * @param title title of appt
     * @param contactId contact id for appt
     * @param desc description of appt
     * @param end end time of appt
     * @param start start time of appt
     * @param custId customer id of appt
     * @param loc location of appt
     *
     */


    public void insertAppt(String title, String desc, String loc, String type, LocalDateTime start,
                           LocalDateTime end, LocalDateTime createDate, String createdBy,
                           LocalDateTime lastUpdate, String lastUpdatedBy, String custId, String userID,
                           String contactId)
    {
        try {
            int cnt = 0;
            for (Contact Contact : Query.getAllContacts())
            {
                if (CBContact.getSelectionModel().getSelectedItem().equals(Contact.getContactName()))
                {
                    cnt = Contact.getContactId();
                }
            }

            LocalDate date = DPStateDate.getValue();
            LocalDate date1 = DPEnddate.getValue();

            if (!date.equals(date1)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Appointment start and end date must be the same date");
                alert.showAndWait();
            }

            String cid = CBCustID.getValue().toString();
            LocalDateTime startTime = LocalDateTime.of(DPStateDate.getValue(), LocalTime.parse(CBStarttime.getValue().toString()));
            LocalDateTime endTime = LocalDateTime.of(DPEnddate.getValue(), LocalTime.parse(CBEndTime.getValue().toString()));

            if (checkAppt(cid, startTime, endTime)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Overlapping appointment times.");
                alert.showAndWait();
            } else {
                String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Create_Date, " +
                        "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

    /** list of customer ID for combobox
     */
    private void custIDList() {
        ObservableList<Integer> customerIDComboList = FXCollections.observableArrayList();
        try {
            ObservableList<Customer> customers = Query.allCustomerinfo();
            for (Customer customer: customers) {
                customerIDComboList.add(customer.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CBCustID.setItems(customerIDComboList);
    }

    /** list of users for combobox
     */
    private void userIDList() {
        ObservableList<Integer> userIDComboList = FXCollections.observableArrayList();
        try {
            ObservableList<User> users = Query.getUsers();
            for (User user: users) {
                userIDComboList.add(user.getUserID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CBUserID.setItems(userIDComboList);
    }
    /**
     * locks appt id, sets local date to now, sets start and end time with open and closed hours, Queries and sets contact combo box
     *
     * */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TFApptID.setEditable(false);
        LocalDate ld = LocalDate.now();
        ObservableList<String> time = Query.getAppointmentTimes(LocalDateTime.of(ld, open), LocalDateTime.of(ld, close));
        CBStarttime.setItems(time);
        CBEndTime.setItems(time);

        ObservableList<Contact> allContacts = Query.getAllContacts();
        ObservableList<String> contacts = FXCollections.observableArrayList();
        allContacts.stream().map(Contact::getContactName).forEach(contacts::add);
        CBContact.setItems(contacts);
        custIDList();
        userIDList();
    }


}

