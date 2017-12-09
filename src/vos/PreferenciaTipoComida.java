package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class PreferenciaTipoComida {

	@JsonProperty(value= "idPreferencia")
	private Long idPreferencia;
	
	@JsonProperty(value= "idTipoComida")
	private Long idTipoComida;
	
	@JsonProperty(value="idCliente")
	private Long idCliente;

	/**
	 * @param idPreferencia
	 * @param idZona
	 * @param idCliente
	 */
	public PreferenciaTipoComida(@JsonProperty(value= "idPreferencia") Long idPreferencia, @JsonProperty(value= "idTipoComida") Long idTipoComida, @JsonProperty(value= "idCliente") Long idCliente) {
		super();
		this.idPreferencia = idPreferencia;
		this.idTipoComida = idTipoComida;
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
	 * @return the idTipoComida
	 */
	public Long getIdTipoComida() {
		return idTipoComida;
	}

	/**
	 * @param idTipo the idZona to set
	 */
	public void setIdTipo(Long idTipo) {
		this.idTipoComida = idTipo;
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
