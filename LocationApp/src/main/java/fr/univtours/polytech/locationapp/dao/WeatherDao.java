package fr.univtours.polytech.locationapp.dao;

import java.util.List;

import fr.univtours.polytech.locationapp.model.address.Feature;

public interface WeatherDao {

	public Double getTemperature(List<Double> coordonnees);
}
