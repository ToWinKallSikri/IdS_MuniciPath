package Synk.Api.Model.Post.WeatherService.Json;

import java.util.List;

public class Forecastday{
	 public String date;
	 public int date_epoch;
	 public Day day;
	 public Astro astro;
	 public List<Hour> hour;
	}
