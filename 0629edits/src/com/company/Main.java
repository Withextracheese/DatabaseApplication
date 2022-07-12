package com.company;
import db.JDBC;
import db.Query;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Main class which creates app
 */

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../view/login.fxml")); //this should point to login FXML
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * set initial arguments
     */

    public static void main(String[] args) throws SQLException {
        //this is where we added data in SWI
    // write your code here
    JDBC.openConnection();
        //Query.insert("Queso","555 wood st","23565","1322225513","2022-06-15 08:21:25","script","2022-06-11 08:21:25","user", "1");
        launch(args);  //starts the javafx application cycle want in between JDBC connections
    JDBC.closeConnection();


    }
}
