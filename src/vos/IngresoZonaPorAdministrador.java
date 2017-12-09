package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresoZonaPorAdministrador {

	@JsonProperty(value= "idAdministrador")
	private Long idAdministrador;
	
	@JsonProperty(value="contraseniaAdministrador")
	private String contraseniaAdministrador;
	
	@JsonProperty(value= "zona")
	private Zona zona;
	/**
	 * @param idAdministrador
	 * @param zona
	 */
	public IngresoZonaPorAdministrador(@JsonProperty(value= "idAdministrador") Long idAdministrador, @JsonProperty(value= "zona") Zona zona, @JsonProperty(value="contraseniaAdministrador") String contraseniaAdministrador) {
		super();
		this.idAdministrador = idAdministrador;
		this.zona = zona;
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
	 * @return the zona
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * @param zona the zona to set
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public String getContraseniaAdministrador() {
		return contraseniaAdministrador;
	}

	public void setContraseniaAdministrador(String contraseniaAdministrador) {
		this.contraseniaAdministrador = contraseniaAdministrador;
	}
}
