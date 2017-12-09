package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Pedido {
	@JsonProperty(value="numeroOrden")
	private Long numeroOrden;
	
	@JsonProperty(value= "totalAPagar")
	private Integer totalAPagar;
	
	@JsonProperty(value="idCliente")
	private Long idCliente;
	
	@JsonProperty(value="servido")
	private boolean servido;
	
	@JsonProperty(value="fechaServicio")
	private Date fechaServicio;
	
	@JsonProperty(value="idProductoVenta")
	private Long idProductoVenta;
	
	@JsonProperty(value="dia")
	private Integer dia;
	
	@JsonProperty(value="personalizado")
	private boolean personalizado;
	
	@JsonProperty(value="cantidadSolicitada")
	private Integer cantidadSolicitada;
	
	@JsonProperty(value="mesa")
	private Long mesa;	
	
	
	
	/**
	 * Metodo constructor de la clase Pedido
	 * <b>post: </b> Crea la totalAPagar con los valores que entran como parametro
	 */
	public Pedido(@JsonProperty(value="numeroOrden") Long numeroOrden, @JsonProperty(value= "totalAPagar") Integer totalAPagar, @JsonProperty(value="idCliente") Long idCliente, @JsonProperty(value="servido") boolean servido, @JsonProperty(value="fechaServicio") Date fechaServicio, @JsonProperty(value="idProductoVenta") Long idProductoVenta, @JsonProperty(value="dia") Integer dia, @JsonProperty(value="personalizado") boolean personalizado, @JsonProperty(value="cantidadSolicitada") Integer cantidadSolicitada, @JsonProperty(value="mesa") Long mesa  ) {
		super();
		this.numeroOrden= numeroOrden;
		this.totalAPagar= totalAPagar;
		this.idCliente = idCliente;
		this.servido = servido;
		this.fechaServicio = fechaServicio;
		this.idProductoVenta = idProductoVenta;
		this.dia = dia;
		this.personalizado = personalizado;
		this.cantidadSolicitada = cantidadSolicitada;
		this.mesa = mesa;
	}

	/**
	 * @return the numeroOrden
	 */
	public Long getNumeroOrden() {
		return numeroOrden;
	}

	/**
	 * @param numeroOrden the numeroOrden to set
	 */
	public void setNumeroOrden(Long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	/**
	 * @return the totalAPagar
	 */
	public Integer getTotalAPagar() {
		return totalAPagar;
	}

	/**
	 * @param totalAPagar the totalAPagar to set
	 */
	public void setTotalAPagar(Integer totalAPagar) {
		this.totalAPagar = totalAPagar;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public boolean isServido() {
		return servido;
	}

	public void setServido(boolean servido) {
		this.servido = servido;
	}

	public Date getFechaServicio() {
		return fechaServicio;
	}

	public void setFechaServicio(Date fechaServicio) {
		this.fechaServicio = fechaServicio;
	}

	public Long getIdProductoVenta() {
		return idProductoVenta;
	}

	public void setIdProductoVenta(Long idProductoVenta) {
		this.idProductoVenta = idProductoVenta;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
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

	public Long getMesa() {
		return mesa;
	}

	public void setMesa(Long mesa) {
		this.mesa = mesa;
	}
	
}
