package Synk.Api.Controller;
import java.util.ArrayList;
import java.util.List;
import Synk.Api.Model.City;
import Synk.Api.Model.Position;

public class RestController {

    List<City> cities;

    int idCounter = 0;

    public CityHandler() {
        cities = new ArrayList<City>();
    }

    public boolean createCity(String cityName, int cap, String curator, Position pos ) {

        City c1 = new City("idCounter",cityName,pos, curator, cap);

        if (!checkIfAlreadyExists(c1.getId())) {
            cities.add(c1);
            idCounter++;
            System.out.println("City created");
        } else {
            System.out.println("City already exists");
        }
    }

    public boolean checkIfAlreadyExists (String cityId) {
        City city = new City(cityId, null, null, null, 0);
        return cities.contains(city);
    }

    public void updateCity(String cityName, int cap, String curator, Position pos ){
        City c1 = new City("idCounter",cityName,pos, curator, cap);
        if (checkIfAlreadyExists(c1.getId())) {
            cities.remove(c1);
            cities.add(c1);
            System.out.println("City updated");
        } else {
            System.out.println("City does not exist");
        }
    }

    public void deleteCity(String cityId) {
        City city = new City(cityId, null, null, null, 0);
        if (checkIfAlreadyExists(cityId)) {
            cities.remove(city);
            System.out.println("City deleted");
        } else {
            System.out.println("City does not exist");
        }
    }

    public City getCity(String cityId) {
        City city = new City(cityId, null, null, null, 0);
        if (checkIfAlreadyExists(cityId)) {
            return cities.get(cities.indexOf(city));
        } else {
            System.out.println("City does not exist");
            return null;
        }
    }


}
