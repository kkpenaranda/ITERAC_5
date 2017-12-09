package vos;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarMenu {
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="productoVenta")
	private Producto_Venta productoVenta;
	
	@JsonProperty(value="menu")
	private Menu menu;
	
	@JsonProperty(value="productos")
	private Set<Long> productos;
	
	
	/**
	 * Metodo constructor de la clase IngresarIngrediente
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarMenu(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante,@JsonProperty(value="contrasenia") String contrasenia, @JsonProperty(value= "productoVenta") Producto_Venta productoVenta, @JsonProperty(value= "menu") Menu menu, @JsonProperty(value="productos") Set<Long> productos) {
		super();
		this.idUsuarioRestaurante= idUsuarioRestaurante;
		this.contrasenia = contrasenia;
		this.productoVenta= productoVenta;
		this.menu = menu;
		this.productos = productos;
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Producto_Venta getProductoVenta() {
		return productoVenta;
	}

	public void setProductoVenta(Producto_Venta productoVenta) {
		this.productoVenta = productoVenta;
	}

	public Set<Long> getProductos() {
		return productos;
	}

	public void setProductos(Set<Long> productos) {
		this.productos = productos;
	}
	
}
