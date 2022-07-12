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
import model.Appointment;
import model.Country;
import model.Customer;
import db.JDBC;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    public TableColumn<Customer, Integer> CustIDCol;
    public TableColumn<Customer, String> NameCol;
    public TableColumn<Customer, String> AddressCol;
    //public TableColumn Address2col;
    public TableColumn<Customer, String> ZipcodeCol;
    public TableColumn<Customer, String> PhNumCol;
    public TableColumn<Customer, Integer> Countrycol;
    public TableColumn<Customer, String> Stateprovincecol;
    public TableView<Customer> ModifyCustTV;
    public Button ButtonSaveChangesCustomer;
    public ComboBox CBStateprov;
    public ComboBox CBCountry;
    public TextField TBCustID;
    public TextField TBName;
    public TextField TBAddress;
    public TextField TBPostalCode;
    public TextField TBPhone;
    public Button ButtonDeleteCustAndAppts;
    Stage stage;
    Parent scene;
    private static Customer customerModify;



    public void OnAReturnMain(ActionEvent actionEvent) throws IOException
    {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void OnActionSelectCusttomod(ActionEvent actionEvent) throws IOException, SQLException {
        //ObservableList<String> countries = FXCollections.observableArrayList();
        //ObservableList<String> stateProvince = FXCollections.observableArrayList();
        customerModify = ModifyCustTV.getSelectionModel().getSelectedItem();

        if (customerModify == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Select customer to modify");
            alert.showAndWait();
        } else {
            //this.customer = customer1; not sure we need this
            String dId = customerModify.getDivIdCountry();
            //int cid = DivisionQuery.getCountryId(dId);
            //fill all text boxes
            TBCustID.setText(String.valueOf(customerModify.getId()));
            TBName.setText(customerModify.getName());
            TBAddress.setText(String.valueOf(customerModify.getAddy()));
            TBPostalCode.setText(String.valueOf(customerModify.getZipcode()));
            TBPhone.setText(String.valueOf(customerModify.getPhone()));
            CBCountry.setItems(Query.getCountries());
            CBCountry.setValue(dId); // this hasnt been tested yet
            //CBCountry.setValue(Query.getCountry(cid));
            //CBCountry.getSelectionModel().select(customerModify.getDivIdCountry());
            CBStateprov.setItems(Query.getFLDName());
            CBStateprov.getSelectionModel().select(customerModify.getDivisionStateProvince());//I havent modified this yet
        }
    }


    public void OnAButtonSaveChangeCust(ActionEvent actionEvent) {

            // need to learn how shorten these and/or how to nest these correctly
            if (TBName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter name");
                alert.showAndWait();
                return;
            }
            if (TBAddress.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid address");
                alert.showAndWait();
                return;
            }
            if (TBPostalCode.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid zipcode");
                alert.showAndWait();
                return;
            }
            if (TBPhone.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Enter valid phone number");
                alert.showAndWait();
                //return;
            }

            if (CBStateprov.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("choose State/Province");
                alert.showAndWait();
            }

        try {
            String id = TBCustID.getText();
            String name = TBName.getText();
            String address = TBAddress.getText();
            String postalCode = TBPostalCode.getText();
            String phone = TBPhone.getText();
            //String divId = CBStateprov.getSelectionModel().getSelectedItem();
            int fld = 0;
            for (FirstLevelDivision FirstLevelDivision : Query.getAllFirstLevelDivisions())
            {
                if (CBStateprov.getSelectionModel().getSelectedItem().equals(FirstLevelDivision.getDivisionName()))
                {
                    fld = FirstLevelDivision.getDivisionID();
                }
            }


            //int id = customer.getId();

            update(id, name, address, postalCode, phone, fld);
            //updateCust(TBCustID.getText());
            ModifyCustTV.setItems(Query.allCustomerinfo()); //or call screen again?
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void update(String customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        try {
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionId);
            ps.setString(6, customerId);
            ps.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

    }
public void updateCust(String text) throws SQLException {

    int fld = 0;
    for (model.FirstLevelDivision FirstLevelDivision : Query.getAllFirstLevelDivisions()) {
        if (CBStateprov.getSelectionModel().getSelectedItem().equals(FirstLevelDivision.getDivisionName())) {
            fld = FirstLevelDivision.getDivisionID();
        }
        String sql = "INSERT INTO CUSTOMERS SET (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?,?,?,?,?,?,?,?,?) WHERE Customer_ID = ?"; //use column names
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        //ps.setInt(1, id); //setting parameters for bind variable above pretty sure cust ID is auto incremented?
        ps.setString(1, TBName.getText()); // setting to second bind variable and second paramater colorID
        ps.setString(2, TBAddress.getText());
        ps.setString(3, TBPostalCode.getText());
        ps.setString(4, TBPhone.getText());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, "script"); // will be updated when appt added
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, "script"); // will be updated when appt added
        ps.setInt(9, fld);//get divID from division choice combo box
        ps.executeUpdate();
        //ps.setInt(10, getdivisionid(division));
        //int rowsAffected = ps.executeUpdate(); //this is going to show the number of rows affected
        //return rowsAffected;
        //stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        //scene = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        //stage.setScene(new Scene(scene));
        //stage.show();
    }
}

    /**
     * Delete customer and appointments associated with customer
     */

    public void OnADeleteCustAndAppts(ActionEvent actionEvent) {
        //Select the customer to delete
        Customer cust = ModifyCustTV.getSelectionModel().getSelectedItem();

        //Make sure a customer was selected
        try {
            if (ModifyCustTV.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Customer not deleted. Select customer to delete");
                alert.showAndWait();
                return;

            }
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to delete this customer and all appointments? Press Ok. Otherwise close prompt");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {

                    int delete = cust.getId();
                    Query.deleteAllAppts(delete);
                    Query.deleteCust(delete);

                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("../view/modifyCustomer.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                    //int selectedPart = PartTableViewIMS.getSelectionModel().getSelectedIndex();
                    //PartTableViewIMS.getItems().remove(selectedPart);
                    //appt.remove(appt);

                    //associatedParts.remove(selectedPart);
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void OnACountry(ActionEvent actionEvent) throws SQLException {
        if (CBCountry.getSelectionModel().getSelectedItem().equals("U.S")) {
            CBStateprov.setItems(Query.getFLD());
        }
        if (CBCountry.getSelectionModel().getSelectedItem().equals("UK")) {
            CBStateprov.setItems(Query.getFLD2());
        }
        if (CBCountry.getSelectionModel().getSelectedItem().equals("Canada")) {
            CBStateprov.setItems(Query.getFLD3());
        }
    }

    /**
     * set customer Id as locked, set tableview,
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //JDBC.openConnection();
        TBCustID.setEditable(false);

        try {
            ModifyCustTV.setItems(Query.allCustomerinfo());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        CustIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("addy"));
        ZipcodeCol.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        PhNumCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Countrycol.setCellValueFactory(new PropertyValueFactory<>("divIdCountry"));
        Stateprovincecol.setCellValueFactory(new PropertyValueFactory<>("divisionStateProvince"));

        //JDBC.closeConnection(); // wont this closing cause problems?
    }


}
