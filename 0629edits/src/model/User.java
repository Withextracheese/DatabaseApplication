package model;

import java.time.LocalDateTime;

public class User {
    //private static User user;
    public int userID;
    public String userName;
    public String userPassword;


    public User(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;

    }

    /**
     * @return the id
     */
    public int getUserID() {
        return userID;
    }
    /**
     * @param userID the user id to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    /**
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName the user name to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return the user password
     */
    public String getUserPassword() {
        return userPassword;
    }
    /**
     * @param userPassword the user password to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    //public static User getCurrentUser() {
        //return user;
   // }

}
