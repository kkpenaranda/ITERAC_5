package vos;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

public class Producto {

////Atributos

	/**
	 * Id del producto
	 */
	private Long idProducto;
	
	private boolean personalizable;	
	
	@JsonProperty(value= "nombre")
	private String nombre;
	
	@JsonProperty(value="traduccion")
	private String traduccion;
	
	@JsonProperty(value= "tiempoPreparacion")
	private double tiempoPreparacion;
	
	@JsonProperty(value= "descripcion")
	private String descripcion;
	
	private Long idCategoria;
	
	private Long idTipoComida;
	
	private Long idProductoVenta;

	/**
	 * Metodo constructor de la clase Producto
	 * <b>post: </b> Crea el producto con los valores que entran como parametro
	 * @param idProducto
	 * @param personalizable
	 * @param nombre
	 * @param traduccion
	 * @param tiempoPreparacion
	 * @param descripcion
	 */
	public Producto(Long idProducto, boolean personalizable, @JsonProperty(value= "nombre") String nombre, 	@JsonProperty(value="traduccion") String traduccion, @JsonProperty(value= "tiempoPreparacion") double tiempoPreparacion, @JsonProperty(value= "descripcion") String descripcion, Long idCategoria,  Long idTipoComida, Long idProductoVenta){ 
		
		this.idProducto= idProducto;
		this.personalizable= personalizable;
		this.nombre= nombre;
		this.traduccion= traduccion;
		this.tiempoPreparacion= tiempoPreparacion;
		this.descripcion=descripcion;
		this.idCategoria = idCategoria;
		this.idTipoComida = idTipoComida;
		this.idProductoVenta = idProductoVenta;
	}
	
	public Producto(  @JsonProperty(value= "nombre") String nombre, @JsonProperty(value="traduccion") String traduccion, @JsonProperty(value= "tiempoPreparacion") double tiempoPreparacion, @JsonProperty(value= "descripcion") String descripcion){
		this.nombre= nombre;
		this.traduccion= traduccion;
		this.tiempoPreparacion= tiempoPreparacion;
		this.descripcion=descripcion;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}

	public double getTiempoPreparacion() {
		return tiempoPreparacion;
	}

	public void setTiempoPreparacion(double tiempoPreparacion) {
		this.tiempoPreparacion = tiempoPreparacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
}
