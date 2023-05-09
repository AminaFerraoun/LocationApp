package fr.univtours.polytech.locationapp.webservice;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.univtours.polytech.locationapp.business.LocationBusinessLocal;
import fr.univtours.polytech.locationapp.model.LocationBean;

@Stateless
@Path("api")
public class WebService {

	@EJB
	private LocationBusinessLocal locationBusiness;

	@GET
	@Path("locations/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public LocationBean getLocations(@PathParam("id") Integer id) {
		return locationBusiness.getLocation(id);
	}

	@GET
	@Path("locations")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<LocationBean> getLocationsCity(@QueryParam("city") String city, @QueryParam("sort") String sort) {

		List<LocationBean> locations;
		if (city == null) {
			locations = locationBusiness.getLocations();
		} else {
			locations = locationBusiness.getLocationsCity(city);
		}

		if (sort == null) {
			locations = locationBusiness.getLocations();
		} else if (sort.equals("asc")) {
			locations = locationBusiness.getLocationsSortedCityAsc();
		}
		return locations;
	}

	@DELETE
	@Path("locations/{id}")
	public Response deleteLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@PathParam("id") Integer locationID) {

		if (token == null) {

			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!token.equals("42")) {

			return Response.status(Status.FORBIDDEN).build();
		}

		if (locationBusiness.getLocation(locationID) == null)
			// La location n'existe pas
			return Response.status(Status.NOT_FOUND).build();

		else {
			locationBusiness.deleteLocation(locationID);
			return Response.status(Status.OK).build();
		}

	}

	@POST
	@Path("locations")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// Méthode appelée lorsqu'on ajoute toutes les informations dans le corps de la
	// requête.
	public Response createLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, LocationBean locationBean) {

		if (token == null) {

			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!token.equals("42")) {

			return Response.status(Status.FORBIDDEN).build();
		}

		// Vérification que tous les champs sont renseignés
		if (locationBean.getAddress() == null || locationBean.getCity() == null || locationBean.getNightPrice() == null
				|| locationBean.getZipCode() == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		locationBusiness.addLocation(locationBean);
		return Response.status(Status.CREATED).build();
	}

	@POST
	@Path("locations")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// Méthode appelée lorsqu'on soumet un formulaire HTML.
	public Response createLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
			@FormParam("address") String address, @FormParam("city") String city, @FormParam("nightPrice") Double price,
			@FormParam("zipCode") Integer zipCode, @FormParam("picture") byte[] url) {

		if (token == null) {

			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!token.equals("42")) {

			return Response.status(Status.FORBIDDEN).build();
		}
		

		// Vérification que tous les champs sont renseignés
		if (address == null || city == null || price == null
				|| zipCode == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		// Création de l'objet LocationBean correspondant
	    LocationBean locationBean = new LocationBean();
	    locationBean.setAddress(address);
	    locationBean.setCity(city);
	    locationBean.setNightPrice(price);
	    locationBean.setZipCode(zipCode.toString());
	    locationBean.setPicture(url);
	    locationBean.getBase64Image();

	    // Ajout de la location dans la base de données
	    locationBusiness.addLocation(locationBean);
		return Response.status(Status.CREATED).build();

	}
	
	@PUT
	@Path("locations/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, @PathParam("id") Integer id,
			LocationBean locationBean) {

		if (token == null) {

			return Response.status(Status.UNAUTHORIZED).build();
		}

		if (!token.equals("42")) {

			return Response.status(Status.FORBIDDEN).build();
		}

		 
		    
		 // Vérification que tous les champs sont renseignés
			if (locationBean.getAddress() == null || locationBean.getCity() == null || locationBean.getNightPrice() == null
					|| locationBean.getZipCode() == null) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			locationBean.setId(id);
		    locationBusiness.updateLocation(locationBean);
		    return Response.ok().build();
		    
	}
	
	
	@PATCH
	@Path("locations/{locationID}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response updatePartialLocation(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
										@PathParam("locationID")int locationID,
										LocationBean locationBean) {
		
		if (token == null) {
			// 401 : Utilisateur non authentifié 
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		// Utilisateur authentifié 
		if (!token.equals("42")){
			// 403 : Accès refusé
			return Response.status(Status.FORBIDDEN).build();
		}
		
		LocationBean locationBDD = locationBusiness.getLocation(locationID);
		if (null != locationBean.getAddress()) {
			locationBDD.setAddress(locationBean.getAddress());
		}
		if (null != locationBean.getCity()) {
			locationBDD.setCity(locationBean.getCity());
		}
		if (null != locationBean.getNightPrice()) {
			locationBDD.setNightPrice(locationBean.getNightPrice());
		}
		if (null != locationBean.getZipCode()) {
			locationBDD.setZipCode(locationBean.getZipCode());
		}
		
		// Ressource trouvée
		locationBusiness.updateLocation(locationBean);
		// 200 : Ok
		return Response.ok().build();
	}

}
