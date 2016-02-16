package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

public class Pass implements Serializable {

    private Driver vistor;
    private Vehicle vehicle;
    private String fromDate;
    private String toDate;

    public Pass(Driver vistor, Vehicle vehicle, String fromDate, String toDate) {
        this.vistor = vistor;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Driver getVistor() {
        return vistor;
    }

    public void setVistor(Driver vistor) {
        this.vistor = vistor;
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
                "vistor='" + vistor + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", fromDate=" + fromDate +
                '}';
    }
}
