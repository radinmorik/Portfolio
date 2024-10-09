package tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Car;
import models.CarAd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Methods {
    public static int userId;

    public static final File carsJSON = new File("cars.json");
    public static final File adsJSON = new File("ads.json");

    public static void login(int id) {
        userId = id;
    }

    //Alle override metoder dekkes ikke av coverage
    public static Car registerCar(String make, int year, String model, String regnum){
        return registerCar(make, year, model, regnum, carsJSON);
    }

    public static Car registerCar(String make, int year, String model, String regnum, File file){
        Car newCar = new Car(make, year, model, 200000, regnum, "Manual", "Gas", 5, 4, userId);
        ArrayList<Car> cars = readCarsFromJSON(file);
        cars.add(newCar);
        writeCarsToJSON(cars, file);
        return newCar;
    }

    public static Car getCar(String regnum)
    {
        return getCar(regnum, carsJSON);
    }
    public static Car getCar(String regnum, File file)
    {
        ArrayList<Car> cars = readCarsFromJSON(file);
        for (Car car : cars)
        {
            if (regnum.equals(car.getRegistrationnumber()))
            {
                return car;
            }
        }
        System.out.println("Car with regnum " + regnum + " not found");
        return null;
    }

    public static void deleteCar(String regnum)
    {
        deleteCar(regnum, carsJSON);
        //delete corresponding ads
    }
    public static void deleteCar(String regnum, File file)
    {
        ArrayList<Car> cars = readCarsFromJSON(file);
        //find car with regnum and remove it from list
        for (Car car:
             cars) {
            if (regnum.equals(car.getRegistrationnumber())){
                cars.remove(car);
                break;
            }
        }
        writeCarsToJSON(cars, file);
    }

    public static CarAd createCarAd(String carRegnum, Date startDate, Date endDate){
        return createCarAd(carRegnum, startDate, endDate, adsJSON);
    }

    public static CarAd createCarAd(String carRegnum, Date startDate, Date endDate, File file){
        CarAd newAd = new CarAd(carRegnum, startDate, endDate);
        ArrayList<CarAd> carAds = readAdsFromJSON(file);
        carAds.add(newAd);
        writeAdsToJSON(carAds, file);
        return newAd;
    }

    public static void showCarAds(){
        // Method for showing all car ads in UI
        // List<Car> cars = readCarsFromJson()
        // Might move this to only MainGUI if this only changes GUI
    }
    public static boolean deleteCarAd(int id){return deleteCarAd(id, adsJSON);}

    public static boolean deleteCarAd(int id, File file){
        boolean value = false;
        ArrayList<CarAd> carAds = readAdsFromJSON(file);
        //find ad with adId and remove it from list
        for (CarAd ad:
                carAds) {
            if (id == ad.getAdId()){
                value = carAds.remove(ad);
                break;
            }
        }
        writeAdsToJSON(carAds, file);
        return value;
    }

    public static boolean rentCarAd(int id){
        return rentCarAd(id, adsJSON);
    }

    public static boolean rentCarAd(int adId, File file){
        boolean value = false;
        ArrayList<CarAd> carAds = readAdsFromJSON(file);
        //find ad with adId and add renterId
        for (CarAd ad:
                carAds) {
            if (adId == ad.getAdId()){
                value = ad.rentCar(userId);
                break;
            }
        }
        writeAdsToJSON(carAds, file);
        return value;
    }


    public static void cancelBooking(int id){
        cancelBooking(id, adsJSON);
    }

    public static void cancelBooking(int adId, File file){
        ArrayList<CarAd> carAds = readAdsFromJSON(file);
        //find ad with adId and remove renterId
        for (CarAd ad:
                carAds) {
            if (adId == ad.getAdId()){
                ad.cancelBooking();
                break;
            }
        }
        writeAdsToJSON(carAds, file);
    }

    // File Handling
    public static void writeCarsToJSON(List<Car> cars) {
        writeCarsToJSON(cars, carsJSON);
    }
    public static void writeCarsToJSON(List<Car> cars, File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, cars);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Car> readCarsFromJSON(){
        return readCarsFromJSON(carsJSON);
    }
    public static ArrayList<Car> readCarsFromJSON(File fil) {
        ArrayList<Car> returnList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Car[] carArray = objectMapper.readValue(fil, Car[].class);
            returnList.addAll(Arrays.asList(carArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static void writeAdsToJSON(List<CarAd> ads){
        writeAdsToJSON(ads, adsJSON);
    }
    public static void writeAdsToJSON(List<CarAd> ads, File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, ads);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<CarAd> readAdsFromJSON(){
        return readAdsFromJSON(adsJSON);
    }
    public static ArrayList<CarAd> readAdsFromJSON(File fil) {
        ArrayList<CarAd> returnList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CarAd[] adArray = objectMapper.readValue(fil, CarAd[].class);
            returnList.addAll(Arrays.asList(adArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnList;
    }
}
