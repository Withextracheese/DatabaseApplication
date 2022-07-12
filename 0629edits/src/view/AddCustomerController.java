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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Add customer controller for adding customers
 */

public class AddCustomerController implements Initializable {


    public ComboBox<String> ComboStateProvAddCust;
    public ComboBox<String> ComboCountyAddCust;
    public TextField TextFCustIDAddCust;
    public TextField TextFNameAddCust;
    public TextField TextFAddressAddCust;
    public TextField TextFPostalCodeAddCust;
    public TextField TextFPhoneAddCust;

    Stage stage;
    Parent scene;

    //private static User user;


    /**
     * return to main screen
     */

    public void OnActionReturntoMainAddCust(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Add customer and return to main
     */

    public void OnActionAddCustomerReturnMainAddCust(ActionEvent actionEvent) throws IOException, SQLException {

        try {
            int fld = 0;
            for (FirstLevelDivision FirstLevelDivision : Query.getAllFirstLevelDivisions()) {
                if (ComboStateProvAddCust.getSelectionModel().getSelectedItem().equals(FirstLevelDivision.getDivisionName())) {
                    fld = FirstLevelDivision.getDivisionID();
                }
            }

            // need to learn how shorten these and/or how to nest these correctly
            if (TextFNameAddCust.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter name");
                alert.showAndWait();
                return;
            }
            if (TextFAddressAddCust.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid address");
                alert.showAndWait();
                return;
            }
            if (TextFPostalCodeAddCust.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid zipcode");
                alert.showAndWait();
                return;
            }
            if (TextFPhoneAddCust.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid phone number");
                alert.showAndWait();
                return;
            }

            if (ComboStateProvAddCust.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose State/Province");
                alert.showAndWait();
            }else {
                String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Create_Date," +
                        "Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setString(1, TextFNameAddCust.getText());
                ps.setString(2, TextFAddressAddCust.getText());
                ps.setString(3, TextFPostalCodeAddCust.getText());
                ps.setString(4, TextFPhoneAddCust.getText());
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(6, "application"); // we have the option of updating this to user
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(8, "application"); // also option to update this to user
                ps.setInt(9, fld);//get divID from division choice combo box
                ps.executeUpdate();

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Add populate correct first level divisions when Country is picked from combobox
     */


    public void OnActionCountryCB(ActionEvent actionEvent) throws SQLException {
        if (ComboCountyAddCust.getSelectionModel().getSelectedItem().equals("U.S")) {
            ComboStateProvAddCust.setItems(Query.getFLD());
        }
        if (ComboCountyAddCust.getSelectionModel().getSelectedItem().equals("UK")) {
            ComboStateProvAddCust.setItems(Query.getFLD2());
        }
        if (ComboCountyAddCust.getSelectionModel().getSelectedItem().equals("Canada")) {
            ComboStateProvAddCust.setItems(Query.getFLD3());
        }
    }

    /**
     * loads combo boxes and locks customer ID This is my second lambda which removes countries which are null and
     * what the code looked like prior to editing This slightly lengthened the code but was used to satisfy the
     * second lambda requirement
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFCustIDAddCust.setEditable(false);

        try {
            ObservableList<Country> allCountries = Query.getAllCountries();
            ObservableList<String> countries = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivision> allStateProvince = Query.getAllFirstLevelDivisions();
            ObservableList<String> stateProvince = FXCollections.observableArrayList();

            //lambda #2
            //https://stackoverflow.com/questions/33317862/java-stream-api-count-items-of-a-nested-list
            //allCountries.stream().map(Country::getCountry).forEach(countries::add);
            allCountries.stream().map(Country::getCountry) // now it's a stream of regions
                    .filter(rs -> rs != null) // remove countries lists that are null
                    .forEach(countries::add);
                    //.mapToInt(countries::size) // stream of list sizes
                    //.sum();
            ComboCountyAddCust.setItems(countries);

            allStateProvince.stream()
                    .map(FirstLevelDivision::getDivisionName)
                    .forEach(stateProvince::add);
            //ComboStateProvAddCust.setItems(stateProvince);//this will be set on action of country picked

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}







