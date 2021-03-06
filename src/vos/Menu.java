
package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.*;

/**
 * Clase que representa un Menu
 * @author vn.gomez_kk.penaranda
 */
public class Menu {

	//// Atributos

	/**
	 * Id del menu
	 */
	@JsonProperty(value="id")
	private Long id;

	/**
	 * Costo del menu
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	@JsonProperty(value="idProductoVenta")
	private Long idProductoVenta;

	/**
	 * Metodo constructor de la clase Menu
	 * <b>post: </b> Crea el menu con los valores que entran como parametro
	 * @param id - id del menu.
	 * @param nombre - nombre del menu.
	 */
	public Menu(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="idProductoVenta") Long idProductoVenta) {
		super();
		this.id = id;
		this.nombre= nombre;
		this.idProductoVenta = idProductoVenta;
	}

	/**
	 * Metodo getter del atributo id
	 * @return id del Menu
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Metodo setter del atributo id <b>post: </b> El id del menu ha sido
	 * cambiado con el valor que entra como parametro
	 * @param id - id del menu
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getIdProductoVenta() {
		return idProductoVenta;
	}

	public void setIdRestaurante(Long idProductoVenta) {
		this.idProductoVenta = idProductoVenta;
	}

	
	
}
