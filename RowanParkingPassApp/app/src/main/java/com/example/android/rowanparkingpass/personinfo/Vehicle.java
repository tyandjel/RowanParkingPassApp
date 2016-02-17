package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

public class Vehicle implements Serializable {

    private String make;
    private String model;
    private int year;
    private String color;
    private String vehicleState;
    private String licensePlate;

    public Vehicle(String make, String model, int year, String color, String vehicleState, String licensePlate) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vehicleState = vehicleState;
        this.licensePlate = licensePlate;
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
        return color + " " + year + " " + make + " " + model;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", color=" + color +
                ", vehicleState='" + vehicleState + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}