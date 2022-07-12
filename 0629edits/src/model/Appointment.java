package model;

import java.time.LocalDateTime;

/**
 * @author Keagan Farniok
 *
 * Class for appointments
 */

public class Appointment {
    public int apptId;
    private String apptName;
    private String apptDescription;
    private String apptLocation ;
    private String contactName;
    private String apptType;
    public LocalDateTime apptStart;
    public LocalDateTime apptEnd;
    private int custID;
    private int userID;

    public Appointment(int apptId, String apptName, String apptDescription, String apptLocation, String contactName,
                        String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int custID, int userID)
    {
        this.apptId = apptId;
        this.apptName = apptName;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.contactName = contactName;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.custID = custID;
        this.userID = userID;
    }
    /**
     * @return the id
     */
    public int getApptId() {
        return apptId;
    }
    /**
     * @param apptId the id to set
     */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }
    /**
     * @return the appointment name
     */
    public String getApptName() {
        return apptName;
    }
    /**
     * @param apptName the appointment name to set
     */
    public void setApptName(String apptName) {
        this.apptName = apptName;
    }
    /**
     * @return the appointment description
     */
    public String getApptDescription() {
        return apptDescription;
    }
    /**
     * @param apptDescription set appt description
     */
    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }
    /**
     * @return the appointment location
     */
    public String getApptLocation() {
        return apptLocation;
    }
    /**
     * @param apptLocation set appt location
     */
    public void setApptLocation(String apptLocation) {
        this.apptLocation = apptLocation;
    }
    /**
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * @param contactName set contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * @return the appointment type
     */
    public String getApptType() {
        return apptType;
    }
    /**
     * @param apptType set appt type
     */
    public void setApptType(String apptType) {
        this.apptType = apptType;
    }
    /**
     * @return get appointment start time
     */
    public LocalDateTime getApptStart() {
        return apptStart;
    }
    /**
     * @param apptStart set appt start time
     */
    public void setApptStart(LocalDateTime apptStart) {
        this.apptStart = apptStart;
    }
    /**
     * @return get appointment end time
     */
    public LocalDateTime getApptEnd() {
        return apptEnd;
    }
    /**
     * @param apptEnd set appt end time
     */
    public void setApptEnd(LocalDateTime apptEnd) {
        this.apptEnd = apptEnd;
    }
    /**
     * @return get customer ID
     */
    public int getCustID() {
        return custID;
    }
    /**
     * @param custID set customer ID
     */
    public void setCustID(int custID) {
        this.custID = custID;
    }
    /**
     * @return get User ID
     */
    public int getUserID() {
        return userID;
    }
    /**
     * @param userID set the userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
