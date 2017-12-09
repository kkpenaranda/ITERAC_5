package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.PreferenciaPrecio;
import vos.PreferenciaTipoComida;
import vos.PreferenciaZona;
import vos.PreferenciaZona;
import vos.PreferenciaZona;

@Path("preferenciasCostos")
public class RotondAndesPreferenciasCostoServices {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	

	/**
	 * Metodo que expone servicio REST usando GET que da todas las preferencias de zonas de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasCostos
	 * @return Json con todas las preferenciasCostos de la base de datos o json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPreferenciasCostos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<PreferenciaPrecio> preferenciasCostos;
		try {
			preferenciasCostos = tm.darPreferenciasPrecio();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferenciasCostos).build();
	}

	 /**
     * Metodo que expone servicio REST usando GET que busca el preferenciasCostos con el id que entra como parametro
     * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasCostos/<<id>>" para la busqueda"
     * @param id - id de la preferenciasZona a buscar que entra en la URL como parametro 
     * @return Json con la preferenciasCostos encontrado con el nombre que entra como parametro o json con 
     * el error que se produjo
     */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPreferenciaCostos( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			PreferenciaPrecio preferencia = tm.buscarPreferenciaPorPrecio(id);
			return Response.status( 200 ).entity( preferencia ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


    /**
     * Metodo que expone servicio REST usando POST que agrega el PreferenciaZona que recibe en Json
     * @param preferencia - PreferenciaZona a agregar
     * @return Json con el PreferenciaZona que agrego o Json con el error que se produjo
     */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPreferencia(PreferenciaPrecio preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addPreferenciaCosto(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}
	
    
	
    /**
     * Metodo que expone servicio REST usando PUT que actualiza el PreferenciaZona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasCostos
     * @param preferencia - PreferenciaZona a actualizar. 
     * @return Json con el PreferenciaZona que actualizo o Json con el error que se produjo
     */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePreferencia(PreferenciaPrecio preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			System.out.println("ss");

			tm.updatePreferenciaCosto(preferencia);
			return Response.status(200).entity(preferencia).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el PreferenciaZona que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasCostos
     * @param preferencia - PreferenciaZona a aliminar. 
     * @return Json con el PreferenciaZona que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePreferencia(PreferenciaPrecio preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deletePreferenciaCosto(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}
	
}
