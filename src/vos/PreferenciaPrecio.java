package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class PreferenciaPrecio {

	@JsonProperty(value= "idPreferencia")
	private Long idPreferencia;
	
	@JsonProperty(value= "costo")
	private Long costo;
	
	@JsonProperty(value="idCliente")
	private Long idCliente;

	/**
	 * @param idPreferencia
	 * @param idZona
	 * @param idCliente
	 */
	public PreferenciaPrecio(@JsonProperty(value= "idPreferencia") Long idPreferencia, @JsonProperty(value= "costo") Long costo, @JsonProperty(value= "idCliente") Long idCliente) {
		super();
		this.idPreferencia = idPreferencia;
		this.costo = costo;
		this.idCliente = idCliente;
	}

	/**
	 * @return the idPreferencia
	 */
	public Long getIdPreferencia() {
		return idPreferencia;
	}

	/**
	 * @param idPreferencia the idPreferencia to set
	 */
	public void setIdPreferencia(Long idPreferencia) {
		this.idPreferencia = idPreferencia;
	}

	/**
	 * @return the coato
	 */
	public Long getIdCosto() {
		return costo;
	}

	/**
	 * @param coato the coato to set
	 */
	public void setIdCosto(Long costo) {
		this.costo = costo;
	}

	/**
	 * @return the idCliente
	 */
	public Long getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
}
