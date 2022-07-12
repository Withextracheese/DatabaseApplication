package db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

/**
 * Queries
 */

public class Query {


    ////USER QUERIES///

    private static User user;
    private static User user1;

    /**
     * For login
     * @param username username checking username
     * @param password password checking password
     */

    public static boolean login(String username, String password)
    {
        try
        {
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int userId = rs.getInt("User_ID");
                String pword = rs.getString("Password");
                user = new User(userId, username, pword);
                return true;
            } else
            {
                return false;
            }

        } catch (SQLException e)
        {
            return false;
        }


    }

    /**
     * @return user current user
     * //@param userID
     */

    public static User getCurrentUser() {
        return user;
    }

    /**
     *
     * @return list of users
     * @throws SQLException
     */

    public static ObservableList<User> getUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String username = rs.getString("User_Name");
            String pword = rs.getString("Password");
            User Nuser = new User(userId, username, pword);
            users.add(Nuser);
        }
        return users;

    }



    ////COUNTRY QUERIES////

    /**
     * returns countries using country id
     * @param id country id parameter
     */

    public static Country getCountry(int id) {
        try {
            String sql = "SELECT * FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int cId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                return new Country(cId, country);
            }
        } catch (SQLException ignored) {

        }

        return null;
    }

    ////APPOINTMENT QUERIES///

    ////https://stackoverflow.com/questions/34626382/convert-localdatetime-to-localdatetime-in-utc
    ///https://stackoverflow.com/questions/56758712/correct-zoneid-for-eastern-time-et-either-us-eastern-or-america-new-york

    /**
     *
     * @param ldt local date time
     * @return the local date time of eastern standard
     */

    public static LocalDateTime getConvert(LocalDateTime ldt) {

        return ldt.atZone(ZoneId.of("US/Eastern")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Initially I had my times as strings. I had to convert everything in the program to LocalDateTime in order
     * to pull the correct time for each zone.
     * @param openTime local date time of open hours
     * @param closeTime local date time of closing hours
     * @return appointment times - for loading our combo boxes
     */

    public static ObservableList<String> getAppointmentTimes(LocalDateTime openTime, LocalDateTime closeTime) {
        //Temporary variables set to local time which are used to iterate through for loop
        LocalDateTime ldo = getConvert(openTime);
        LocalDateTime ldc = getConvert(closeTime);
        //array of appt times
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        for (int i = ldo.getHour(); i <= ldc.getHour(); i++) {  //enhanced for loop generally for arrays and collections
            appointmentTimes.add(String.format("%02d", i) + ":00"); //print integer and if shorter than 2 digits prefix with zeroes
        }

        return appointmentTimes;
    }

    /**
     *
     * @return a list of all contacts for loading our comboboxes
     */

    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(id, name, email);
                allContacts.add(contact);
            }

            return allContacts;
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return null;
    }

    /**
     * deletes appointments using appointment id
     * @param id appointment id
     */

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }


    ///CUSTOMER QUERIES///

    /**
     *
     * @return a list of all customers
     * @throws SQLException
     */

    public static ObservableList<Customer> allCustomerinfo() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT CUSTOMERS.Customer_ID, CUSTOMERS.Customer_name, CUSTOMERS.Address, CUSTOMERS.Postal_code," +
                "CUSTOMERS.Phone, CUSTOMERS.Division_ID, FIRST_LEVEL_DIVISIONS.Division, FIRST_LEVEL_DIVISIONS.COUNTRY_ID," +
                "COUNTRIES.Country FROM CUSTOMERS INNER JOIN FIRST_LEVEL_DIVISIONS on CUSTOMERS.Division_ID = " +
                "FIRST_LEVEL_DIVISIONS.Division_ID INNER JOIN COUNTRIES ON FIRST_LEVEL_DIVISIONS.COUNTRY_ID = " +
                "COUNTRIES.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(); //rs is variable. this is creating two dimensional list result set
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int custId = rs.getInt("Customer_ID"); //type exactly like table column retrieving info from Customer_ID and assigning it to variable
            String name = rs.getString("Customer_Name");
            String addy = rs.getString("Address");
            String zip = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String country = rs.getString("Country");
            String stateprov = rs.getString("Division");

            // populate into customer
            Customer Cust = new Customer(custId, name, addy, zip, phone, country, stateprov);
            allCustomers.add(Cust);

        }
        return allCustomers;

    }

    /**
     * delete customer by customer id
     * @param id customer ID
     * @throws SQLException
     */

    public static void deleteCust(int id) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?"; //sql statement selecting all records from table FRUITS, where Color_ID is equal to bind variable
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);  //because we are using a bind variable we need to use a setter to assign value to bind variable before any executes
        ps.setInt(1, id); //because colorID is an int type
        ps.executeUpdate(); //rs is variable. this is creating two dimensional list result set
        //while(rs.next()){ //using while loop to run through set returns true if there is data = true
    }

    /**
     * delete customer by customer id
     * @param id customer id
     */

    public static Customer selectCust(int id) throws SQLException {
        //ObservableList<Customer> cust = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CUSTOMERS WHERE Customer_ID = ?"; //sql statement selecting all records from table FRUITS, where Color_ID is equal to bind variable
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);  //because we are using a bind variable we need to use a setter to assign value to bind variable before any executes
        ps.setInt(1, id); //because colorID is an int type
        ps.executeUpdate(); //rs is variable. this is creating two dimensional list result set
        //while(rs.next()){ //using while loop to run through set returns true if there is data = true
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String name = rs.getString("Customer_Name");
            String addy = rs.getString("Address");
            String zip = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String country = rs.getString("Country");
            String stateprov = rs.getString("Division");

            return new Customer(id, name, addy, zip, phone, country, stateprov);
        }

        return null;
    }

    /**
     * list of all appointments
     */

    public static ObservableList<Appointment> allApptinfo(String id) throws SQLException {
        ObservableList<Appointment> allCustAppts = FXCollections.observableArrayList();
        String sql = "SELECT APPOINTMENTS.Appointment_ID, APPOINTMENTS.Title, APPOINTMENTS.Description," +
                "APPOINTMENTS.Location, APPOINTMENTS.Contact_ID, APPOINTMENTS.Type, APPOINTMENTS.Start," +
                "APPOINTMENTS.End, APPOINTMENTS.Customer_ID, APPOINTMENTS.User_ID, CONTACTS.Contact_Name " +
                "FROM APPOINTMENTS INNER JOIN CONTACTS on APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID " +
                "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery(); //rs is variable. this is creating two dimensional list result set
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int apptId = rs.getInt("Appointment_ID");
            String apptName = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custid = rs.getInt("Customer_ID");
            int userid = rs.getInt("User_ID");


            Appointment Appt = new Appointment(apptId, apptName, description, location, contact, type, start, end, custid, userid);
            allCustAppts.add(Appt);

        }
        return allCustAppts;

    }

    /**
     * list of all appointment information
     */

    public static ObservableList<Appointment> allApptinfo() throws SQLException {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        String sql = "SELECT APPOINTMENTS.Appointment_ID, APPOINTMENTS.Title, APPOINTMENTS.Description," +
                "APPOINTMENTS.Location, APPOINTMENTS.Contact_ID, APPOINTMENTS.Type, APPOINTMENTS.Start," +
                "APPOINTMENTS.End, APPOINTMENTS.Customer_ID, APPOINTMENTS.User_ID, CONTACTS.Contact_Name " +
                "FROM APPOINTMENTS INNER JOIN CONTACTS on APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(); //rs is variable. this is creating two dimensional list result set
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int apptId = rs.getInt("Appointment_ID");
            String apptName = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custid = rs.getInt("Customer_ID");
            int userid = rs.getInt("User_ID");


            Appointment Appt = new Appointment(apptId, apptName, description, location, contact, type, start, end, custid, userid);
            allAppts.add(Appt);

        }
        return allAppts;

    }

     /**
     * list of all appointment information by week
     */

    public static ObservableList<Appointment> weekApptinfo() throws SQLException { //not tested
        ObservableList<Appointment> weekAppts = FXCollections.observableArrayList();
        String sql = "SELECT APPOINTMENTS.Appointment_ID, APPOINTMENTS.Title, APPOINTMENTS.Description," +
                "APPOINTMENTS.Location, APPOINTMENTS.Contact_ID, APPOINTMENTS.Type, APPOINTMENTS.Start," +
                "APPOINTMENTS.End, APPOINTMENTS.Customer_ID, APPOINTMENTS.User_ID, CONTACTS.Contact_Name " +
                "FROM APPOINTMENTS INNER JOIN CONTACTS on APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID " +
                "WHERE week(Start) = week(now());";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(); //rs is variable. this is creating two dimensional list result set
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int apptId = rs.getInt("Appointment_ID"); //type exactly like table column retrieving info from Customer_ID and assigning it to variable
            String apptName = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custid = rs.getInt("Customer_ID");
            int userid = rs.getInt("User_ID");


            Appointment Appt = new Appointment(apptId, apptName, description, location, contact, type, start, end, custid, userid);
            weekAppts.add(Appt);

        }
        return weekAppts;

    }

    /**
     * This checks for appointments within 15 minutes
     * @return the appt found else return null
     *
     */

    public static Appointment apptCheck() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdate = now.atZone(zid);
        LocalDateTime ldate = zdate.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldate2 = ldate.plusMinutes(15);
        int user1 = getCurrentUser().getUserID();

            String sql = "SELECT * FROM appointments WHERE Start BETWEEN '" + ldate + "' AND '" + ldate2 + "' AND " +
                    "User_ID='" + user1 + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String apptName = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int custid = rs.getInt("Customer_ID");
                int userid = rs.getInt("User_ID");


                return new Appointment(apptId, apptName, description, location, contact, type, start, end, custid, userid);

        }
        return null;
    }

    /**
     * list of all appointment information by month
     */

    public static ObservableList<Appointment> monthApptinfo() throws SQLException { //not tested
        ObservableList<Appointment> monthAppts = FXCollections.observableArrayList();
        String sql = "SELECT APPOINTMENTS.Appointment_ID, APPOINTMENTS.Title, APPOINTMENTS.Description," +
                "APPOINTMENTS.Location, APPOINTMENTS.Contact_ID, APPOINTMENTS.Type, APPOINTMENTS.Start," +
                "APPOINTMENTS.End, APPOINTMENTS.Customer_ID, APPOINTMENTS.User_ID, CONTACTS.Contact_Name " +
                "FROM APPOINTMENTS INNER JOIN CONTACTS on APPOINTMENTS.Contact_ID = CONTACTS.Contact_ID " +
                "WHERE month(Start) = month(now());";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(); //rs is variable. this is creating two dimensional list result set
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int apptId = rs.getInt("Appointment_ID"); //type exactly like table column retrieving info from Customer_ID and assigning it to variable
            String apptName = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int custid = rs.getInt("Customer_ID");
            int userid = rs.getInt("User_ID");


            Appointment Appt = new Appointment(apptId, apptName, description, location, contact, type, start, end, custid, userid);
            monthAppts.add(Appt);

        }
        return monthAppts;

    }

    /**
     * delete all appointments associated with a customer id
     * @param id customer id
     */

    public static void deleteAllAppts(int id) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }




    /**
     * @return list of countries
     */

    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country from COUNTRIES";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) { //using while loop to run through set returns true if there is data = true
            int countryId = rs.getInt("Country_ID");
            String country = rs.getString("Country");
            Country country1 = new Country(countryId, country);
            allCountries.add(country1);
        }
        return allCountries;
    }

    /**
     * @return list of countries
     */

    public static ObservableList<String> getCountries() throws SQLException {

        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT Country from COUNTRIES";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            allCountries.add(rs.getString("Country"));
        }
        return allCountries;
    }

    /**
     * @return list of divisions
     */

    public static ObservableList<String> getFLDName() throws SQLException {
        ObservableList<String> allFLD = FXCollections.observableArrayList();
        String sql = "SELECT Division from FIRST_LEVEL_DIVISIONS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            allFLD.add(rs.getString("Division"));
        }
        return allFLD;
    }


    /**
     * @return list of all states/provinces
     */

    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivision> AllFirstLevelDivisions = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("COUNTRY_ID");
            FirstLevelDivision firstLevelDivision1 = new FirstLevelDivision(divisionId, divisionName, countryId);
            AllFirstLevelDivisions.add(firstLevelDivision1);
        }
        return AllFirstLevelDivisions;
    }

    /**
     * @return list of US states
     */
    public static ObservableList<String> getFLD() throws SQLException {
        ObservableList<String> FLDlist = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions WHERE Country_ID = 1";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        //ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String country1 = rs.getString("Division");
            FLDlist.add(country1);
        }
        return FLDlist;
    }

    /**
     * @return list of CA provinces
     */
    public static ObservableList<String> getFLD2() throws SQLException {
        ObservableList<String> FLDlist2 = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions WHERE Country_ID = 2";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        //ps.setString(1, country);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String country1 = rs.getString("Division");
            FLDlist2.add(country1);
        }
        return FLDlist2;
    }

    /**
     * @return list of Uk level divisions
     */
    public static ObservableList<String> getFLD3() throws SQLException {
        ObservableList<String> FLDlist3 = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions WHERE Country_ID = 3";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String country1 = rs.getString("Division");
            FLDlist3.add(country1);
        }
        return FLDlist3;
    }


    ////REPORTS QUERIES////

    /**
     * This compiles a report of appointments and grouped by month and type
     * @return string of appts
     * @throws SQLException
     */
    public static String reportApptTypesByMonth() throws SQLException {
        String sql = "SELECT MONTHNAME(start) as Start, Type, COUNT(*) as Amount " +
                    "FROM appointments GROUP BY MONTH(start), Type;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        StringBuilder sb = new StringBuilder();
        sb.append("Month   |    Type    |   # of each Type  \n\n");
        while (rs.next()) {
            sb.append(rs.getString("Start") + "     " + rs.getString("Type") + "       " + rs.getString("Amount") + "\n");
        }
        return sb.toString();
    }

    /**
     * This compiles a report of total customers within the US and grouped by State/division
     * @return string of customers
     * @throws SQLException
     */
    public static String reportTotal() throws SQLException {
        String sql = "SELECT CUSTOMERS.Customer_ID, FIRST_LEVEL_DIVISIONS.Division FROM CUSTOMERS, " +
                "FIRST_LEVEL_DIVISIONS WHERE CUSTOMERS.Division_ID = FIRST_LEVEL_DIVISIONS.Division_ID AND " +
                "first_level_divisions.Country_ID= 1 ORDER BY Division;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        StringBuilder sb = new StringBuilder();
        sb.append("Division   |  Customer ID  \n\n");
        while (rs.next()) {
            sb.append(rs.getString("Division") + "      " + rs.getString("Customer_ID") + "\n");
        }
        return sb.toString();
    }

    /**
     * This compiles a report of contacts and their schedules
     * @return a string of reports
     * @throws SQLException
     */

    public static String reportContactSchedule() throws SQLException {
        String sql = "SELECT CONTACTS.Contact_Name, APPOINTMENTS.Appointment_ID, APPOINTMENTS.Title, APPOINTMENTS.Type, " +
                "APPOINTMENTS.Description, CONVERT_TZ(Start, '+00:00', 'system') AS Start, " +
                "CONVERT_TZ(End, '+00:00', 'system') AS End, APPOINTMENTS.Customer_ID FROM APPOINTMENTS, " +
                "CONTACTS WHERE APPOINTMENTS.CONTACT_ID = CONTACTS.CONTACT_ID ORDER BY Contact_Name;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        StringBuilder sb = new StringBuilder();
        sb.append("Contact Name   | Appt ID |   Title    |    Type    | Description |  Start  |  End  | Cust ID  \n\n");
        while (rs.next()) {
            sb.append(rs.getString("Contact_Name") + "     " + rs.getString("Appointment_ID") +
                    "     " + rs.getString("Title") + "     " + rs.getString("Type") +
                    "     " + rs.getString("Description") + "     " + rs.getString("Start") +
                    "     " + rs.getString("End") + "     " + rs.getString("Customer_ID") + "\n");
        }
        return sb.toString();
    }

}