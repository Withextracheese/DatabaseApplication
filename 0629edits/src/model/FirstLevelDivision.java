package model;

public class FirstLevelDivision {
    //private static String divisionName;
    //private static int divisionID;
    public int divisionID;
    public String divisionName;
    public int countryId;


    public FirstLevelDivision(int divisionID, String divisionName, int countryId) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }
    /**
     * @return the division id
     */
    public int getDivisionID() {
        return divisionID;
    }
    /**
     * @param divisionID the division id to set
     */
    public void setDivisionID(int divisionID) {
        divisionID = divisionID;
    }
    /**
     * @return the division id
     */
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @param divisionName the division name to set
     */
    public void setDivisionName(String divisionName) {
        divisionName = divisionName;
    }
    /**
     * @return the country id
     */
    public int getCountryId() {
        return countryId;
    }
    /**
     * @param countryId the country id to set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
