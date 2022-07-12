package model;

/**
 * @author Keagan Farniok
 *
 * Class for customers
 */

public class Customer {

        private int id;
        private String name;
        private String addy;
        private String zipcode ;
        private String phone;
        private String country;
        private String stateProvince;

        public Customer(int id, String name, String addy, String zipcode, String phone, String country,
                        String stateProvince)
        {
            this.id = id;
            this.name = name;
            this.addy = addy;
            this.zipcode = zipcode;
            this.phone = phone;
            this.country = country;
            this.stateProvince = stateProvince;
        }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the address
     */
    public String getAddy() {
        return addy;
    }
    /**
     * @param addy the address to set
     */
    public void setAddy(String addy) {
        this.addy = addy;
    }
    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }
    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    /**
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * @return the country
     */
    public String getDivIdCountry() {
        return country;
    }
    /**
     * @param divIdCountry the country to set
     */
    public void setDivIdCountry(String divIdCountry) {
        this.country = divIdCountry;
    }
    /**
     * @return the state/province
     */
    public String getDivisionStateProvince() {
        return stateProvince;
    }
    /**
     * @param divisionStateProvince the state/province to set
     */
    public void setDivisionStateProvince(String divisionStateProvince) {
        this.stateProvince = divisionStateProvince;
    }
}
