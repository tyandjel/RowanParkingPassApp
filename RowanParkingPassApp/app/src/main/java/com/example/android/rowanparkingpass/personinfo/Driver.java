package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

/**
 * A Driver
 */
public class Driver implements Serializable {

    private int driverId;
    private String firstName;
    private String lastName;
    private String street;
    private String town;
    private String state;
    private String zipCode;

    /**
     * Constructor
     *
     * @param driverId  driver id
     * @param firstName first name
     * @param lastName  last name
     * @param street    street
     * @param town      town
     * @param state     state
     * @param zipCode   zip code
     */
    public Driver(int driverId, String firstName, String lastName, String street, String town, String state, String zipCode) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.town = town;
        this.state = state;
        this.zipCode = zipCode;
    }

    /**
     * Gets the driver id
     *
     * @return driver id
     */
    public int getDriverId() {
        return driverId;
    }

    /**
     * Sets the driver id
     *
     * @param driverId driver id
     */
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    /**
     * Gets the driver's name
     *
     * @return name
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the street
     *
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the town
     *
     * @return town
     */
    public String getTown() {
        return town;
    }

    /**
     * Gets the state
     *
     * @return state
     */
    public String getState() {
        return state;
    }


    /**
     * Sets the state
     *
     * @param state state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the zip code
     *
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code
     *
     * @param zipCode zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        States[] arrayOfStates = States.values();
        int stateNum = 0;
        for (int i = 0; i < arrayOfStates.length; i++) {
            if (state.equals(arrayOfStates[i].toString())) {
                stateNum = i;
                break;
            }
        }
        return "{\"driver_id\":" + driverId + ",\"street\":\"" + street + "\",\"city\":\"" + town + "\",\"full_name\":\"" + getName() + "\",\"zip\":" + zipCode + ",\"state\":" + stateNum + "}";
    }
}
