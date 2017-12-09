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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.CancelarPedido;
import vos.Pedido;

@Path("pedidos")
public class RotondAndesPedidoServices {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servnitor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}


	/**
	 * Metodo que expone servicio REST usando GET que da todos los pedidos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/pedidos
	 * @return Json con todos los pedidos de la base de datos o json con 
	 * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getpedidos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Pedido> pedidos;
		try {
			pedidos = tm.darPedidos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidos).build();
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el pedido con el id que entra como parametro
	 * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/pedidos/<<nit>>" para la busqueda"
	 * @param nit - Id de la pedido a buscar que entra en la URL como parametro 
	 * @return Json con el pedido encontrado con el nombre que entra como parametro o json con 
	 * el error que se produjo
	 */
	@GET
	@Path( "{numeroOrden: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getpedido( @PathParam( "numeroOrden" ) Long numeroOrden )
	{
		RotondAndesTM tm = new RotondAndesTM( getPath( ) );
		try
		{
			Pedido v = tm.buscarPedidoPorNumeroDeOrden(numeroOrden);
			return Response.status( 200 ).entity( v ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	
	
    /**
     * Metodo que expone servicio REST usando DELETE que elimina el pedido que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/RotondAndes/rest/pedidos
     * @param pedido - pedido a aliminar. 
     * @return Json con el pedido que elimino o Json con el error que se produjo
     */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletepedido(CancelarPedido pedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.cancelarPedido(pedido);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedido).build();
	}

	
}
