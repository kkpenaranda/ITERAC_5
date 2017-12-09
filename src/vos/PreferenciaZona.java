package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class PreferenciaZona {

	@JsonProperty(value= "idPreferencia")
	private Long idPreferencia;
	
	@JsonProperty(value= "idZona")
	private Long idZona;
	
	@JsonProperty(value="idCliente")
	private Long idCliente;

	/**
	 * @param idPreferencia
	 * @param idZona
	 * @param idCliente
	 */
	public PreferenciaZona(@JsonProperty(value= "idPreferencia") Long idPreferencia, @JsonProperty(value= "idZona") Long idZona, @JsonProperty(value= "idCliente") Long idCliente) {
		super();
		this.idPreferencia = idPreferencia;
		this.idZona = idZona;
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
	 * @return the idZona
	 */
	public Long getIdZona() {
		return idZona;
	}

	/**
	 * @param idZona the idZona to set
	 */
	public void setIdZona(Long idZona) {
		this.idZona = idZona;
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
