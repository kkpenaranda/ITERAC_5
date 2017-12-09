package rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.IngresarPedido;
import vos.IngresoZonaPorAdministrador;
import vos.ServirPedido;

@Path("ServirPedido")
public class RotondAndesServirPedidoServices {
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
	
	
//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response servirPedido(ServirPedido  servirPedido) {
//		RotondAndesTM tm = new RotondAndesTM(getPath());
//		try {
//			System.out.println("sirviendo pedido ");
//
//			tm.servirPedido(servirPedido);
//			System.out.println("servicio ejecutado");
//			return Response.status(200).entity(servirPedido).build();
//		} catch (Exception e) {
//			return Response.status(500).entity(doErrorMessage(e)).build();
//		}
//	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response servirPedido(ServirPedido  servirPedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			System.out.println("sirviendo pedido ");

			tm.servirPedido(servirPedido);
			System.out.println("servicio ejecutado");
			return Response.status(200).entity(servirPedido).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
}
