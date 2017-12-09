package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ConsultaClientes {

	@JsonProperty(value="idCliente")
	private Usuario usuario;
	
	@JsonProperty(value="preferenciaPrecio")
	private Double preferenciaPrecio;
	
	/**
	 * @return the preferenciaPrecio
	 */
	public Double getPreferenciaPrecio() {
		return preferenciaPrecio;
	}

	/**
	 * @param preferenciaPrecio the preferenciaPrecio to set
	 */
	public void setPreferenciaPrecio(Double preferenciaPrecio) {
		this.preferenciaPrecio = preferenciaPrecio;
	}

	/**
	 * @return the idPreferenciaZona
	 */
	public Long getIdPreferenciaZona() {
		return idPreferenciaZona;
	}

	/**
	 * @param idPreferenciaZona the idPreferenciaZona to set
	 */
	public void setIdPreferenciaZona(Long idPreferenciaZona) {
		this.idPreferenciaZona = idPreferenciaZona;
	}

	/**
	 * @return the idPreferenciaTipo
	 */
	public Long getIdPreferenciaTipo() {
		return idPreferenciaTipo;
	}

	/**
	 * @param idPreferenciaTipo the idPreferenciaTipo to set
	 */
	public void setIdPreferenciaTipo(Long idPreferenciaTipo) {
		this.idPreferenciaTipo = idPreferenciaTipo;
	}

	@JsonProperty(value="idPreferenciaZona")
	private Long idPreferenciaZona;
	
	@JsonProperty(value="idPreferenciaTipo")
	private Long idPreferenciaTipo;
	
	@JsonProperty(value="pedido")
	private Pedido pedido;

	public ConsultaClientes(@JsonProperty(value="usuario") Usuario usuario, @JsonProperty(value="preferenciaPrecio") Double preferenciaPrecio, @JsonProperty(value="idPreferenciaZona") Long idPreferenciaZona, @JsonProperty(value="idPreferenciaTipo") Long idPreferenciaTipo, @JsonProperty(value="pedido") Pedido pedido) {
		super();
		this.usuario = usuario;
		this.preferenciaPrecio = preferenciaPrecio;
		this.idPreferenciaZona = idPreferenciaZona;
		this.idPreferenciaTipo = idPreferenciaTipo;
		this.pedido = pedido;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	

	/**
	 * @return the pedido
	 */
	public Pedido getPedido() {
		return pedido;
	}

	/**
	 * @param pedido the pedido to set
	 */
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
}
