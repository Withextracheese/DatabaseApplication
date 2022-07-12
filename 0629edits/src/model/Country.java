package model;

public class Country {

    private int countryID;
    private String country;

    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }
    /**
     * @return the id
     */
    public int getCountryID() {
        return countryID;
    }
    /**
     * @param countryID the id to set
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }
    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

}
