package Synk.Api.Model;

public class WeatherForecast {

    DateTime time;

    String city;

    public WeatherForecast(DateTime time, String city) {
        this.time = time;
        this.city = city;
    }

    public WeatherForecast getWeather(String city, DateTime time) {
        //da definire le API per il meteo
        return null;
    }
}
