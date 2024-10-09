import forms.MainGUI;
import models.Car;
import models.CarAd;
import models.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.Methods;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Testing {

        @Nested
        class Car_Renting {

            @Test
            public void Car_Can_Not_Get_Rented_If_Unavailable() {
                User user = new User(12, "Arne", 52);
                User user2 = new User(9, "Ronny", 25);
                Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
                CarAd carAd = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
                carAd.rentCar(user.getId());
                assertEquals(false, carAd.rentCar(user2.getId()));
            }

            @Test
            public void Car_Can_Get_Rented_If_Available() {
                User user = new User(12, "Arne", 52);
                User user2 = new User(9, "Ronny", 25);
                Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
                CarAd carAd = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
                assertEquals(true, carAd.rentCar(user2.getId()));
            }

            @Test
            public void Car_Can_Not_Get_Rented_Twice() {
                User user = new User(12, "Arne", 52);
                User user2 = new User(9, "Ronny", 25);
                Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
                CarAd carAd = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
                carAd.rentCar(user2.getId());
                assertEquals(false, carAd.rentCar(user2.getId()));
            }

            @Test
            public void Booking_Can_Get_Cancelled_After_CarAd_Is_Rented() {
                File carAdJSON = new File("carAdTesting.json");
                ArrayList<CarAd> emptyList = new ArrayList<>();
                Methods.writeAdsToJSON(emptyList, carAdJSON);
                User user = new User(12, "Arne", 52);
                User user2 = new User(9, "Ronny", 25);
                Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
                CarAd carAd = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), null, null, carAdJSON);
                Methods.login(user2.getId());
                Methods.rentCarAd(carAd.getAdId(), carAdJSON);
                Methods.cancelBooking(carAd.getAdId(), carAdJSON);
                assertEquals(0, carAd.getRenterId());
            }


            @Test
            public void Car_Gets_Rented_If_Available() {
                User user = new User(12, "Arne", 52);
                Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
                CarAd carAd = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
                carAd.rentCar(user.getId());
                assertEquals(12, carAd.getRenterId());
            }


        }

    @Nested
    class JSON_File_Handling_With_Car_Object{

        @Test
        public void Car_Object_Returns_In_JSON_Format() {
            ArrayList<Car> cars = new ArrayList<>();
            User user = new User(12, "Arne", 52);
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            assertEquals("model.Car{make='Nissan', modelYear=2018, model='Leaf', kmDistance=200000, registrationnumber='RJ3292', gearType='Manual', fuelType='Gas', seats=5, doors=4, user=12}", nissanLeaf.toString());
        }

        @Test
        public void Car_Gets_deleted_From_JSON_File() {
            ArrayList<Car> cars = new ArrayList<>();
            User user = new User(12, "Arne", 52);
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            File carsJSON = new File("carsTesting.json");
            Methods.writeCarsToJSON(cars, carsJSON);
            Methods.deleteCar("RJ3292", carsJSON);
            ArrayList<Car> emptyList = new ArrayList<>();
            assertEquals(emptyList, Methods.readCarsFromJSON(carsJSON));
        }

        @Test
        public void Car_Gets_Added_To_JSON() {
            File carsJSON = new File("carsTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Methods.writeCarsToJSON(cars, carsJSON); // Overwrite JSON test file
            User user = new User(12, "Arne", 52);
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            Methods.writeCarsToJSON(cars, carsJSON);
            String test1 = cars.toString();
            String test2 = Methods.readCarsFromJSON(carsJSON).toString();
            assertEquals(cars.toString(), Methods.readCarsFromJSON(carsJSON).toString());
        }


        @Test
        public void CarAd_Gets_Deleted_From_JSON_File() {
            File carAdJSON = new File("carAdTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            ArrayList<CarAd> emptyList = new ArrayList<>();
            Methods.writeAdsToJSON(emptyList, carAdJSON); // Overwrite JSON test file
            User user = new User(0, "Arne", 52);
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            CarAd carAd = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), null, null, carAdJSON);
            Methods.deleteCarAd(carAd.getAdId(), carAdJSON);
            assertEquals(emptyList, Methods.readAdsFromJSON(carAdJSON));
        }

        @Test
        public void CarAd_Gets_Added_To_JSON_File(){
            File carAdJSON = new File("carAdTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            ArrayList<CarAd> expectedList = new ArrayList<>();
            Methods.writeAdsToJSON(expectedList, carAdJSON); // Overwrite JSON test file
            User user = new User(0, "Arne", 52);
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            CarAd carAd = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), null, null, carAdJSON);
            expectedList.add(carAd);
            String test1 = expectedList.toString();
            String test2 = Methods.readAdsFromJSON(carAdJSON).toString();
            assertEquals(expectedList.toString(), Methods.readAdsFromJSON(carAdJSON).toString());
        }

        // Test for writing to JSON IOexception when stream error accours.
        // basically impossible to write test for this without changing read/write permissions in windows

        // Read car and car ads JSON method returns IO Exception stack tree to terminal
        // but works as intended
        @Test
        public void Read_Car_From_JSON_Returns_IOException_When_Wrong_File() {
            File carsJSON = new File("carsTesting12.json");
            ArrayList<Car> cars = new ArrayList<>();
            try  {
                Methods.readCarsFromJSON(carsJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        public void Read_CarAd_From_JSON_Returns_IOException_When_Wrong_File() {
            File carsJSON = new File("carsTesting13.json");
            ArrayList<CarAd> carAds = new ArrayList<>();
            try  {
                Methods.readAdsFromJSON(carsJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nested
    class Car_Ads{
        @Test
        public void User_Can_Create_CarAd() {
            File carAdJSON = new File("carAdTesting.json");
            User user = new User(0, "Arne", 52);
            ArrayList<Car> cars = new ArrayList<>();
            ArrayList<CarAd> expectedList = new ArrayList<>();
            Methods.writeAdsToJSON(expectedList, carAdJSON); // Overwrite JSON test file
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            CarAd carad = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), null, null, carAdJSON);
            expectedList.add(carad);
            assertEquals(expectedList.toString(), Methods.readAdsFromJSON(carAdJSON).toString());
        }

        @Test
        public void User_Can_Set_StartDate_On_CarAd() {
            File carAdJSON = new File("carAdTesting.json");
            User user = new User(0, "Arne", 52);
            ArrayList<Car> cars = new ArrayList<>();
            ArrayList<CarAd> expectedList = new ArrayList<>();
            Methods.writeAdsToJSON(expectedList, carAdJSON); // Overwrite JSON test file
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            // Date constructor is deprecated. Date parameters calculate from the year 1900-00-00.
            Date date = new Date(122, 11, 24);
            CarAd carad = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), date, null, carAdJSON);
            expectedList.add(carad);
            assertEquals(date, carad.getStartDate());
        }

        @Test
        public void User_Can_Set_EndDate_On_CarAd() {
            File carAdJSON = new File("carAdTesting.json");
            User user = new User(0, "Arne", 52);
            ArrayList<Car> cars = new ArrayList<>();
            ArrayList<CarAd> expectedList = new ArrayList<>();
            Methods.writeAdsToJSON(expectedList, carAdJSON); // Overwrite JSON test file
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, user.getId());
            cars.add(nissanLeaf);
            // Date constructor is deprecated. Date parameters calculate from the year 1900-00-00.
            Date date = new Date(122, 11, 26);
            CarAd carad = Methods.createCarAd(nissanLeaf.getRegistrationnumber(), null , date, carAdJSON);
            expectedList.add(carad);
            assertEquals(date, carad.getEndDate());
        }

    }

    @Nested
    class GUI_Testing{
        @Test
        public void Correct_Amount_Of_Cars_Are_Shown() {
            File carsTestingJSON = new File("carsTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, 1);
            Car toyotaCorolla = new Car("Toyota", 1990, "Corolla", 200000, "AB12345", "Manual", "Gas", 5, 4, 2);
            Car miniMorris = new Car("Mini", 1969, "Morris", 200000, "CE14234", "Manual", "Gas", 4, 2, 2);
            cars.add(nissanLeaf);
            cars.add(toyotaCorolla);
            cars.add(miniMorris);
            Methods.writeCarsToJSON(cars, carsTestingJSON);
            MainGUI mainGUI = new MainGUI("CarX");
            Methods.userId = 2;
            assertEquals(2, mainGUI.showCars(carsTestingJSON));
        }
        @Test
        public void Correct_Amount_Of_Ads_Are_Shown() {
            File carsTestingJSON = new File("carsTesting.json");
            File carAdTestingJSON = new File("carAdTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, 1);
            cars.add(nissanLeaf);
            Methods.writeCarsToJSON(cars, carsTestingJSON);
            ArrayList<CarAd> carAds = new ArrayList<>();
            CarAd ad1 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
            CarAd ad2 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
            CarAd ad3 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
            carAds.add(ad1);
            carAds.add(ad2);
            carAds.add(ad3);
            Methods.writeAdsToJSON(carAds, carAdTestingJSON);
            MainGUI mainGUI = new MainGUI("CarX");
            Methods.userId = 1;
            assertEquals(3, mainGUI.showCarAds(carAdTestingJSON, carsTestingJSON));
        }

        @Test
        public void Correct_Amount_Of_Bookings_Are_Shown() {
            File carsTestingJSON = new File("carsTesting.json");
            File carAdTestingJSON = new File("carAdTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, 1);
            cars.add(nissanLeaf);
            Methods.writeCarsToJSON(cars, carsTestingJSON);
            ArrayList<CarAd> carAds = new ArrayList<>();
            CarAd ad1 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 0);
            CarAd ad2 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 2);
            CarAd ad3 = new CarAd(nissanLeaf.getRegistrationnumber(), null, null, 2);
            carAds.add(ad1);
            carAds.add(ad2);
            carAds.add(ad3);
            Methods.writeAdsToJSON(carAds, carAdTestingJSON);
            MainGUI mainGUI = new MainGUI("CarX");
            Methods.userId = 2;
            assertEquals(2, mainGUI.showBookings(carAdTestingJSON, carsTestingJSON));
        }
    }

    @Nested
    class User_Testing
    {
        @Test
        public void User_Login_Changes_UserId_To_Users_Id() {
            int sessionId = 1;
            Methods.login(sessionId);
            assertEquals(sessionId, Methods.userId);
        }
        @Test
        public void Log_Out_Changes_UserId_To_Zero() {
            Methods.login(0);
            assertEquals(0, Methods.userId);
        }
    }

    @Nested
    class Car_Testing
    {
        @Test
        public void Car_Can_Get_Registered() {
            File carsJSON = new File("carsTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Methods.writeCarsToJSON(cars, carsJSON); // Overwrite JSON test file
            Methods.userId = 1;
            Methods.registerCar("bmw", 2018, "sykt", "AB02938", carsJSON);
            assertEquals("[model.Car{make='bmw', modelYear=2018, model='sykt', kmDistance=200000, registrationnumber='AB02938', gearType='Manual', fuelType='Gas', seats=5, doors=4, user=1}]", Methods.readCarsFromJSON(carsJSON).toString());
        }

        @Test
        public void Get_Car_Returns_Car_With_Correct_Regnum() {
            File carsTestingJSON = new File("carsTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Car nissanLeaf = new Car("Nissan", 2018, "Leaf", 200000, "RJ3292", "Manual", "Gas", 5, 4, 1);
            Car toyotaCorolla = new Car("Toyota", 1990, "Corolla", 200000, "AB12345", "Manual", "Gas", 5, 4, 2);
            Car miniMorris = new Car("Mini", 1969, "Morris", 200000, "CE14234", "Manual", "Gas", 4, 2, 2);
            cars.add(nissanLeaf);
            cars.add(toyotaCorolla);
            cars.add(miniMorris);
            Methods.writeCarsToJSON(cars, carsTestingJSON);
            assertEquals(toyotaCorolla.getRegistrationnumber(), Methods.getCar(toyotaCorolla.getRegistrationnumber(), carsTestingJSON).getRegistrationnumber());
        }

        @Test
        public void Get_Car_Returns_Null_If_Car_Doesnt_Exist() {
            File carsTestingJSON = new File("carsTesting.json");
            ArrayList<Car> cars = new ArrayList<>();
            Methods.writeCarsToJSON(cars, carsTestingJSON);
            assertNull(Methods.getCar("AB12345", carsTestingJSON));
        }

        @Test
        public void Car_Can_Get_Deleted() {
            File carsJSON = new File("carsTesting.json");
            ArrayList<Car> emptyList = new ArrayList<>();
            Methods.writeCarsToJSON(emptyList, carsJSON); // Overwrite JSON test file
            Methods.userId = 1;
            Car car = Methods.registerCar("bmw", 2018, "sykt", "AB02938", carsJSON);
            Methods.deleteCar(car.getRegistrationnumber(), carsJSON);
            assertEquals(emptyList.toString(), Methods.readCarsFromJSON(carsJSON).toString());
        }
    }

}
