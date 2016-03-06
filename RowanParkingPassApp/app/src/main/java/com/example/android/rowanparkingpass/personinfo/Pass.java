package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

public class Pass implements Serializable {

    private Driver driver;
    private Vehicle vehicle;
    private String fromDate;
    private String toDate;

    public Pass(Driver driver, Vehicle vehicle, String fromDate, String toDate) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
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
