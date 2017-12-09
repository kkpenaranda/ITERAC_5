package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoMenu {
	
	@JsonProperty(value= "nombre")
	private String nombre;
	
	@JsonProperty(value= "tipo")
	private String tipo;

	@JsonProperty(value= "productoVenta")
	private Producto_Venta productoVenta;
	
	

	/**
	 * @param productoVenta
	 * @param nombre
	 * @param tipo
	 */
	public ProductoMenu(@JsonProperty(value= "nombre") String nombre, @JsonProperty(value= "tipo") String tipo, @JsonProperty(value= "productoVenta") Producto_Venta productoVenta) {
		super();
		
		this.nombre = nombre;
		this.tipo = tipo;
		this.productoVenta = productoVenta;
	}

	public Producto_Venta getProductoVenta() {
		return productoVenta;
	}

	public void setProductoVenta(Producto_Venta productoVenta) {
		this.productoVenta = productoVenta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
