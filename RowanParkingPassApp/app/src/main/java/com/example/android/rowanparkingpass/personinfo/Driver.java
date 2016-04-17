package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Driver implements Serializable {

    private int driverId;
    private String firstName;
    private String lastName;
    private String street;
    private String town;
    private String state;
    private String zipCode;

    public Driver(int driverId, String firstName, String lastName, String street, String town, String state, String zipCode) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.town = town;
        this.state = state;
        this.zipCode = zipCode;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return street + " " + town + ", " + state + " " + zipCode;
    }

    @Override
    public String toString() {
        States[] arrayOfStates = States.values();
        ArrayList<States> states = new ArrayList<>();
        states.addAll(Arrays.asList(arrayOfStates));
        return "{\"driver_id\":" + driverId + ",\"street\":\"" + street + "\",\"city\":\"" + town + "\",\"full_name\":\"" + getName() + "\",\"zip\":" + zipCode + ",\"state\":\"" + states.indexOf(state) + "\"}";
    }
}
