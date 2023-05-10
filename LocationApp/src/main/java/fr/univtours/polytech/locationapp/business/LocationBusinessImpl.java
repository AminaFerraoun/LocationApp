package fr.univtours.polytech.locationapp.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.univtours.polytech.locationapp.dao.AddressDao;
import fr.univtours.polytech.locationapp.dao.LocationDao;
import fr.univtours.polytech.locationapp.dao.WeatherDao;
import fr.univtours.polytech.locationapp.model.LocationBean;
import fr.univtours.polytech.locationapp.model.address.Feature;

@Stateless
public class LocationBusinessImpl implements LocationBusinessLocal, LocationBusinessRemote {

	@Inject
	private LocationDao locationDao;
	@Inject
	private WeatherDao weatherDao;
	@Inject
	private AddressDao addressDao;

	@Override
	public void addLocation(LocationBean bean) {
		locationDao.createLocation(bean);
	}

	@Override
	public List<LocationBean> getLocations() {
		
		for (LocationBean location : locationDao.getLocations()) {
			
			String address = location.getAddress();
			String zipCode = location.getZipCode();
			String fullAddress = address + " " + zipCode;
			if (addressDao.getAddresses(fullAddress).size() > 0) {
				Feature addressFeature = addressDao.getAddresses(fullAddress).get(0);
				List<Double> coordinates = addressFeature.getGeometry().getCoordinates();
				location.setTemperature(weatherDao.getTemperature(coordinates) - 273);
			}
		}
		return locationDao.getLocations();

	}

	@Override
	public LocationBean getLocation(Integer id) {
		LocationBean location = locationDao.getLocation(id);
		
		String address = location.getAddress();
		Feature addressFeature = addressDao.getAddresses(address).get(0);
		List<Double> coordinates = addressFeature.getGeometry().getCoordinates();
		location.setTemperature(weatherDao.getTemperature(coordinates)-273);
		return location;
	}

	@Override
	public void updateLocation(LocationBean locationBean) {
		locationDao.updateLocation(locationBean);
	}

	@Override
	public void deleteLocation(Integer id) {
		LocationBean locationBean = getLocation(id);
		locationDao.deleteLocation(locationBean);
	}

	@Override
	public List<LocationBean> getLocationsCity(String city) {
		return locationDao.getLocationCity(city);
	}

	@Override
	public List<LocationBean> getLocationsSortedCityAsc() {
		return locationDao.getLocationSortedCityAsc();
	}

	@Override
	public List<LocationBean> getLocationsSortedCityDesc() {
		return locationDao.getLocationSortedCityDesc();

	}

}
