package fr.univtours.polytech.locationapp.business;

import java.util.List;

import javax.ejb.Local;

import fr.univtours.polytech.locationapp.model.LocationBean;

@Local
public interface LocationBusinessLocal {

	public void addLocation(LocationBean locationBean);

	public List<LocationBean> getLocations();

	public LocationBean getLocation(Integer id);

	public List<LocationBean> getLocationsCity(String city);
	
	public List<LocationBean> getLocationsSortedCityAsc();
	
	public List<LocationBean> getLocationsSortedCityDesc();
	
	public void updateLocation(LocationBean locationBean);

	public void deleteLocation(Integer id);
	
}
