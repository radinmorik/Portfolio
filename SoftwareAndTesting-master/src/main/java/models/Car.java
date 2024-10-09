package models;

import java.util.Date;

public class Car {
    private String make;
    private int modelYear;
    private String model;
    private int kmDistance;
    private String registrationnumber;
    private String gearType;
    private String fuelType;
    private int seats;
    private int doors;
    private int user;

    public Car(){

    }

    public Car(String make, int modelYear, String model, int kmDistance, String registrationnumber, String gearType, String fuelType, int seats, int doors, int user) {
        this.make = make;
        this.modelYear = modelYear;
        this.model = model;
        this.kmDistance = kmDistance;
        this.registrationnumber = registrationnumber;
        this.gearType = gearType;
        this.fuelType = fuelType;
        this.seats = seats;
        this.doors = doors;
        this.user = user;
    }

    public CarAd createCarAd(String carRegnum, Date startDate, Date endDate) {
        CarAd carad = new CarAd(carRegnum, startDate, endDate, 0);
        return carad;
    }

    @Override
    public String toString() {
        return "model.Car{" +
                "make='" + make + '\'' +
                ", modelYear=" + modelYear +
                ", model='" + model + '\'' +
                ", kmDistance=" + kmDistance +
                ", registrationnumber='" + registrationnumber + '\'' +
                ", gearType='" + gearType + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", seats=" + seats +
                ", doors=" + doors +
                ", user=" + user +
                '}';
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getKmDistance() {
        return kmDistance;
    }

    public void setKmDistance(int kmDistance) {
        this.kmDistance = kmDistance;
    }

    public String getRegistrationnumber() {
        return registrationnumber;
    }

    public void setRegistrationnumber(String registrationnumber) {
        this.registrationnumber = registrationnumber;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }
}
