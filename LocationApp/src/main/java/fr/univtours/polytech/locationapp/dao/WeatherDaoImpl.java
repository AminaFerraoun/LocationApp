package fr.univtours.polytech.locationapp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import fr.univtours.polytech.locationapp.model.weather.WsWeatherResult;

@Stateless
public class WeatherDaoImpl implements WeatherDao {

	private static String URL = "https://api.openweathermap.org/data/2.5";

	@Override
	public Double getTemperature(List<Double> coordinates) {
		// Instanciation du client.
		Client client = ClientBuilder.newClient();

		// On indique l'URL du Web Service.
		WebTarget target = client.target(URL);
		String appId="f647ddf6b56fe4ac0d33bff0bd22e5e8";
		// On indique le "end point" (on aurait aussi pu directement le mettre dans
		// l'URL).
		// C'est également avec cette méthode qu'on pourrait ajouter des "path
		// parameters" si besoin.
		target = target.path("weather");
		// On précise (lorsqu'il y en a) les "query parameters".
		target = target.queryParam("appid", appId );
		target = target.queryParam("lon", coordinates.get(0));
		target = target.queryParam("lat", coordinates.get(1));

		// On appelle le WS en précisant le type de l'objet renvoyé, ici un
		// WsAdressResult.
		System.out.println(target.getUri());
		WsWeatherResult wsResult = target.request(MediaType.APPLICATION_JSON).get(WsWeatherResult.class);
		return wsResult.getMain().getTemp();
	}
}
