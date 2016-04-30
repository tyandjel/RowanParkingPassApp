package com.example.android.rowanparkingpass.personinfo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Driver implements Serializable {

    private int driverId;
    private Bitmap image;
    private String firstName;
    private String lastName;
    private String street;
    private String town;
    private String state;
    private String zipCode;

    public Driver(int driverId, Bitmap image, String firstName, String lastName, String street, String town, String state, String zipCode) {
        this.driverId = driverId;
        this.image = image;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
