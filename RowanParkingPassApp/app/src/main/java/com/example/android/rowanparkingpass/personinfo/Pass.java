package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

/**
 * A pass
 */
public class Pass implements Serializable {

    private int requestID;
    private Driver driver;
    private Vehicle vehicle;
    private String fromDate;
    private String toDate;

    /**
     * Constructor with request id
     *
     * @param requestID request id
     * @param driver    driver
     * @param vehicle   vehicle
     * @param fromDate  from date
     * @param toDate    to date
     */
    public Pass(int requestID, Driver driver, Vehicle vehicle, String fromDate, String toDate) {
        this.requestID = requestID;
        this.driver = driver;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Constructor without request id
     *
     * @param driver   driver
     * @param vehicle  vehicle
     * @param fromDate from date
     * @param toDate   to date
     */
    public Pass(Driver driver, Vehicle vehicle, String fromDate, String toDate) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Constructor
     */
    public Pass() {
        requestID = -1;
        driver = new Driver(-1, "-1", "-1", "-1", "-1", "-1", "-1");
        vehicle = new Vehicle(-1, "-1", "-1", -1, "-1", "-1", "-1");
        fromDate = "-1";
        toDate = "-1";
    }

    /**
     * Gets the request id of pass
     *
     * @return request id
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     * Gets pass driver
     *
     * @return driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Sets driver
     *
     * @param driver driver for pass
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Gets pass vehicle
     *
     * @return vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets vehicle
     *
     * @param vehicle vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Gets from date
     *
     * @return date
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Gets to date
     *
     * @return date
     */
    public String getToDate() {
        return toDate;
    }

    @Override
    public String toString() {
        return "':driver'='" + driver + '\'' +
                "&':vehicle'='" + vehicle + '\'' +
                "&':start'='" + fromDate + '\'' +
                "&':end'=" + toDate + '\'';
    }
}
