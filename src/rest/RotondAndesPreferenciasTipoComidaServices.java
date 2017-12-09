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
import vos.PreferenciaTipoComida;

@Path("preferenciasTiposComida")
public class RotondAndesPreferenciasTipoComidaServices {

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
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasTipo
	 * @return Json con todas las preferenciasTipo de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getpreferenciasTipo() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<PreferenciaTipoComida> preferencias;
		try {
			preferencias = tm.darPreferenciasTipo();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencias).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el preferenciasTipo con el id que entra como parametro
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasTipo/<<id>>" para la busqueda"
	 * @param id - id de la preferenciasZona a buscar que entra en la URL como parametro 
	 * @return Json con la preferenciasTipo encontrado con el nombre que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getPreferenciaTipos( @PathParam( "id" ) Long id )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			PreferenciaTipoComida preferencia = tm.buscarPreferenciaTipoPorId(id);
			return Response.status( 200 ).entity( preferencia ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


	/**
	 * Metodo que expone servicio REST usando POST que agrega el PreferenciaTipo que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasTipo/PreferenciaTipo
	 * @param preferencia - PreferenciaTipo a agregar
	 * @return Json con el PreferenciaTipo que agrego o Json con el error que se produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPreferencia(PreferenciaTipoComida preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addPreferenciaTipo(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}



	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el PreferenciaTipo que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasTipo
	 * @param preferencia - PreferenciaTipo a actualizar. 
	 * @return Json con el PreferenciaTipo que actualizo o Json con el error que se produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePreferenciaTipo(PreferenciaTipoComida preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updatePreferenciaTipoComida(preferencia);
			return Response.status(200).entity(preferencia).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el PreferenciaTipo que recibe en Json
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/preferenciasTipo
	 * @param preferencia - PreferenciaTipo a aliminar. 
	 * @return Json con el PreferenciaTipo que elimino o Json con el error que se produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePreferenciaTipo(PreferenciaTipoComida preferencia) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deletePreferenciaTipo(preferencia);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(preferencia).build();
	}

}
