package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Pedido_Personalizado {
	
	@JsonProperty(value= "numeroOrden")
	private Long numeroOrden;
	
	@JsonProperty(value="idMenu")
	private Long idMenu;
	
	@JsonProperty(value="productoCambiado")
	private Long productoCambiado;
	
	@JsonProperty(value="productoNuevo")
	private Long productoNuevo;

	/**
	 * Metodo constructor de la clase Tiene_Pedido
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public Pedido_Personalizado(@JsonProperty(value="numeroOrden") Long numeroOrden,@JsonProperty(value="idMenu") Long idMenu,@JsonProperty(value="productoCambiado") Long productoCambiado,@JsonProperty(value="productoNuevo") Long productoNuevo) {
		super();
		this.numeroOrden= numeroOrden;
		this.idMenu = idMenu;
		this.productoCambiado = productoCambiado;
		this.productoNuevo = productoNuevo;
	}

	public Long getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Long getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Long idMenu) {
		this.idMenu = idMenu;
	}

	public Long getProductoCambiado() {
		return productoCambiado;
	}

	public void setProductoCambiado(Long productoCambiado) {
		this.productoCambiado = productoCambiado;
	}

	public Long getProductoNuevo() {
		return productoNuevo;
	}

	public void setProductoNuevo(Long productoNuevo) {
		this.productoNuevo = productoNuevo;
	}

	
}
