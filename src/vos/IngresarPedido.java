package vos;

import java.sql.Date;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarPedido {
	
	@JsonProperty(value="idCliente")
	private Long idCliente;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="numeroOrden")
	private Long numeroOrden;
	
	@JsonProperty(value="idProductoVenta")
	private Long idProductoVenta;
	
	@JsonProperty(value="personalizado")
	private boolean personalizado;
	
	@JsonProperty(value="cantidadSolicitada")
	private Integer cantidadSolicitada;
	
	@JsonProperty(value="mesa")
	private Long mesa;	
	
	@JsonProperty(value="cantidadSillas")
	private Integer cantidadSillas;
	
	@JsonProperty(value="productoCambiado")
	private Long productoCambiado;
	
	@JsonProperty(value="productoNuevo")
	private Long productoNuevo;
	
	
	/**
	 * Metodo constructor de la clase IngresarIngrediente
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarPedido(@JsonProperty(value="idCliente") Long idCliente, @JsonProperty(value= "contrasenia")String contrasenia,@JsonProperty(value= "numeroOrden") Long numeroOrden, @JsonProperty(value= "idProductoVenta") Long idProductoVenta,@JsonProperty(value="personalizado") boolean personalizado, @JsonProperty(value="cantidadSolicitada") Integer cantidadSolicitada, @JsonProperty(value="mesa") Long mesa,@JsonProperty(value="cantidadSillas") Integer cantidadSillas,@JsonProperty(value="productoCambiado") Long productoCambiado,@JsonProperty(value="productoNuevo") Long productoNuevo) {
		super();
		this.idCliente = idCliente;
		this.contrasenia = contrasenia;
		this.numeroOrden = numeroOrden;
		this.idProductoVenta = idProductoVenta;
		this.personalizado = personalizado;
		this.cantidadSolicitada = cantidadSolicitada;
		this.productoCambiado = productoCambiado;
		this.productoNuevo = productoNuevo;
		this.mesa = mesa;
		this.cantidadSillas = cantidadSillas;
	}


	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	public Long getNumeroOrden() {
		return numeroOrden;
	}


	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}


	public Long getIdProductoVenta() {
		return idProductoVenta;
	}


	public void setIdProductoVenta(Long idProductoVenta) {
		this.idProductoVenta = idProductoVenta;
	}


	public boolean isPersonalizado() {
		return personalizado;
	}


	public void setPersonalizado(boolean personalizado) {
		this.personalizado = personalizado;
	}


	public Integer getCantidadSolicitada() {
		return cantidadSolicitada;
	}


	public void setCantidadSolicitada(Integer cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
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


	public Long getMesa() {
		return mesa;
	}


	public void setMesa(Long mesa) {
		this.mesa = mesa;
	}


	public Integer getCantidadSillas() {
		return cantidadSillas;
	}


	public void setCantidadSillas(Integer cantidadSillas) {
		this.cantidadSillas = cantidadSillas;
	}
	
	

}
