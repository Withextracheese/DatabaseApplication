package model;

public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    public Contact(int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }
    /**
     * @return the id
     */
    public int getContactId() {
        return contactId;
    }
    /**
     * @param contactId the id to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /**
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * @param contactName the contact name to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
