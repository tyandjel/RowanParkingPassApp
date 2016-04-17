package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Vehicle implements Serializable {

    private int vehicleId;
    private String make;
    private String model;
    private int year;
    private String color;
    private String vehicleState;
    private String licensePlate;

    public Vehicle(int vehicleId, String make, String model, int year, String color, String vehicleState, String licensePlate) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vehicleState = vehicleState;
        this.licensePlate = licensePlate;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(String vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarInfo() {
        return year + " " + make + " " + model + " " + vehicleState + " " + licensePlate;
    }

    @Override
    public String toString() {
        States[] arrayOfStates = States.values();
        ArrayList<States> states = new ArrayList<>();
        states.addAll(Arrays.asList(arrayOfStates));
        return ("{`vehicle_id`:" + vehicleId + ",`make`:`" + make + "`,`model`:`" + model + "`,`license`:`" + licensePlate + "`,`state`:" + states.indexOf(vehicleState) + ",`color`:" + color + ",`year`:" + year + "}").replace("`", "\"");
    }
}
