package com.example.android.rowanparkingpass.personinfo;

import java.io.Serializable;

/**
 * Vehicle
 */
public class Vehicle implements Serializable {

    private int vehicleId;
    private String make;
    private String model;
    private int year;
    private String color;
    private String vehicleState;
    private String licensePlate;

    /**
     * Constructor
     *
     * @param vehicleId    vehicle id
     * @param make         make
     * @param model        model
     * @param year         year
     * @param color        car color
     * @param vehicleState vehicle state
     * @param licensePlate license plate
     */
    public Vehicle(int vehicleId, String make, String model, int year, String color, String vehicleState, String licensePlate) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vehicleState = vehicleState;
        this.licensePlate = licensePlate;
    }

    /**
     * Gets vehicle id
     *
     * @return vehicle id
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets vehicle id
     *
     * @param vehicleId vehicle id
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Gets the vehicle make
     *
     * @return make
     */
    public String getMake() {
        return make;
    }

    /**
     * Get vehicle model
     *
     * @return vehicle model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets vehicle model
     *
     * @param model model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets vehicle year
     *
     * @return vehicle year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets vehicle year
     *
     * @param year year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the car color
     *
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets teh car color
     *
     * @param color color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the vehicle state
     *
     * @return state
     */
    public String getVehicleState() {
        return vehicleState;
    }

    /**
     * Gets the license plate for vehicle
     *
     * @return license plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * All vehicle info
     *
     * @return vehicle info
     */
    public String getCarInfo() {
        return year + " " + make + " " + model + " " + vehicleState + " " + licensePlate;
    }

    @Override
    public String toString() {
        States[] arrayOfStates = States.values();
        int stateNum = 0;
        for (int i = 0; i < arrayOfStates.length; i++) {
            if (vehicleState.equals(arrayOfStates[i].toString())) {
                stateNum = i;
                break;
            }
        }
        return ("{`vehicle_id`:" + vehicleId + ",`make`:`" + make + "`,`model`:`" + model + "`,`license`:`" + licensePlate + "`,`state`:" + stateNum + ",`color`:" + color + ",`year`:" + year + "}").replace("`", "\"");
    }
}
