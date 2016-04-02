package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

public class Pass implements Serializable {

    private int requestID;
    private Driver driver;
    private Vehicle vehicle;
    private String fromDate;
    private String toDate;

    public Pass(int requestID, Driver driver, Vehicle vehicle, String fromDate, String toDate) {
        this.requestID = requestID;
        this.driver = driver;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Pass() {
        requestID = -1;
        driver = new Driver(-1, "-1", "-1", "-1", "-1", "-1", "-1");
        vehicle = new Vehicle(-1, "-1", "-1", -1, "-1", "-1", "-1");
        fromDate = "-1";
        toDate = "-1";
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "Pass{" +
                "driver='" + driver + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", fromDate=" + fromDate +
                '}';
    }
}
