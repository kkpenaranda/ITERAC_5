package vos;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarProducto {
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="productoVenta")
	private Producto_Venta productoVenta;
	
	@JsonProperty(value= "producto")
	private Producto producto;
	
	@JsonProperty(value="ingredientes")
	private Set<Long> ingredientes;
	
	
	/**
	 * Metodo constructor de la clase IngresarIngrediente
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarProducto(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante,@JsonProperty(value="contrasenia") String contrasenia,@JsonProperty(value= "productoVenta") Producto_Venta productoVenta, @JsonProperty(value= "producto") Producto producto, @JsonProperty(value="ingredientes") Set<Long> ingredientes) {
		super();
		this.idUsuarioRestaurante= idUsuarioRestaurante;
		this.contrasenia=contrasenia;
		this.productoVenta = productoVenta;
		this.producto = producto;
		this.ingredientes = ingredientes;
	}

	public Long getIdUsuarioRestaurante() {
		return idUsuarioRestaurante;
	}

	public void setIdUsuarioRestaurante(Long idUsuarioRestaurante) {
		this.idUsuarioRestaurante = idUsuarioRestaurante;
	}
	

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public Producto_Venta getProductoVenta() {
		return productoVenta;
	}

	public void setProductoVenta(Producto_Venta productoVenta) {
		this.productoVenta = productoVenta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Set<Long> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(Set<Long> ingredientes) {
		this.ingredientes = ingredientes;
	}
	
}
