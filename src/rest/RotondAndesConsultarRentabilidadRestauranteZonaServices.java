
package rest;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import tm.RotondAndesTM;
import vos.RentabilidadRestaurante;
import vos.Usuario;

/**
 * Clase que expone servicios REST con ruta base: http://"ip o nombre de host":8080/RotondAndes/rest/zonas/...
 * @author vn.gomez_kk.penaranda
 */
@Path("consultarRentabilidadPorZona")
public class RotondAndesConsultarRentabilidadRestauranteZonaServices {

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
	///{fecha1:\\d+}/{fecha2: \\d+}  @PathParam( "fecha1" )  Date fecha1,@PathParam( "fecha2" )  Date fecha2

	@GET
	@Path("{idZona:\\d+}/{fechas}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRentabilidad(@PathParam( "idZona" ) Long idZona,@QueryParam( "fecha1" )  String fecha1,@QueryParam( "fecha2" )  String fecha2 ) {
//		System.out.println(fechas);
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<RentabilidadRestaurante> usuarios;
//		String[] fechass = new String[2];
//		fechass = fechas.split("y");
	
		try {
			usuarios = tm.darRentabilidadRestauranteZona(idZona, fecha1.replaceAll("-", "/"), fecha2.replaceAll("-", "/"));
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(usuarios).build();
	}
}
