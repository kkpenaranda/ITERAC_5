package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresoClientesPorAdministrador {

	@JsonProperty(value= "idAdministrador")
	private Long idAdministrador;
	
	@JsonProperty(value= "contraseniaAdministrador")
	private String contraseniaAdministrador;
	
	@JsonProperty(value= "cliente")
	private ClienteRegistrado cliente;	

	public IngresoClientesPorAdministrador(@JsonProperty(value= "idAdministrador") Long idAdministrador, @JsonProperty(value= "cliente") ClienteRegistrado cliente, @JsonProperty(value= "contraseniaAdministrador") String contraseniaAdministrador){
		super();
		this.cliente = cliente;
		this.idAdministrador = idAdministrador;
		this.contraseniaAdministrador= contraseniaAdministrador;
	}

	/**
	 * @return the idAdministrador
	 */
	public Long getIdAdministrador() {
		return idAdministrador;
	}

	/**
	 * @param idAdministrador the idAdministrador to set
	 */
	public void setIdAdministrador(Long idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	/**
	 * @return the idCliente
	 */
	public ClienteRegistrado getCliente() {
		return cliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setCliente(ClienteRegistrado idCliente) {
		this.cliente = idCliente;
	}
	
	public String getContraseniaAdministrador() {
		return contraseniaAdministrador;
	}

	public void setContraseniaAdministrador(String contraseniaAdministrador) {
		this.contraseniaAdministrador = contraseniaAdministrador;
	}
	

	
	
	
	
	
}
