package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RegistroRestaurante {

	@JsonProperty(value= "usuario")
	private Usuario usuario;
	
	@JsonProperty(value="especialidad")
	private String especialidad;
	
	
	@JsonProperty(value= "idAdministrador")
	private Long idAdministrador;
	
	@JsonProperty(value="contraseniaAdministrador")
	private String contraseniaAdministrador;

	/**
	 * totalProductosDisponibles del restaurante
	 */
	@JsonProperty(value="totalProductosDisponibles")
	private Integer totalProductosDisponibles;

	/**
	 * tipo de comida del restaurante
	 */
	@JsonProperty(value="tipoComida")
	private String tipoComida;
	
	@JsonProperty(value= "paginaWeb")
	private String paginaWeb;
	
	@JsonProperty(value= "idZona")
	private Long idZona;

	/**
	 * @param id
	 * @param nombre
	 * @param correoElectronico
	 * @param rol
	 * @param especialidad
	 * @param totalProductosDisponibles
	 * @param tipoComida
	 */
	public RegistroRestaurante(@JsonProperty(value= "usuario") Usuario usuario, @JsonProperty(value= "especialidad") String especialidad,
			@JsonProperty(value= "totalProductosDisponibles") Integer totalProductosDisponibles,  @JsonProperty(value= "tipoComida") String tipoComida, @JsonProperty(value= "idAdministrador") Long idAdministrador, @JsonProperty(value= "paginaWeb") String paginaWeb, @JsonProperty(value= "idZona") Long idZona, @JsonProperty(value="contraseniaAdministrador") String contraseniaAdministrador) {
		super();
		this.usuario= usuario;
		this.especialidad = especialidad;
		this.totalProductosDisponibles = totalProductosDisponibles;
		this.tipoComida = tipoComida;
		this.idAdministrador= idAdministrador;
		this.paginaWeb = paginaWeb;
		this.idZona= idZona;
		this.contraseniaAdministrador= contraseniaAdministrador;
	}

	

	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the totalProductosDisponibles
	 */
	public Integer getTotalProductosDisponibles() {
		return totalProductosDisponibles;
	}

	/**
	 * @param totalProductosDisponibles the totalProductosDisponibles to set
	 */
	public void setTotalProductosDisponibles(Integer totalProductosDisponibles) {
		this.totalProductosDisponibles = totalProductosDisponibles;
	}

	/**
	 * @return the tipoComida
	 */
	public String getTipoComida() {
		return tipoComida;
	}

	/**
	 * @param tipoComida the tipoComida to set
	 */
	public void setTipoComida(String tipoComida) {
		this.tipoComida = tipoComida;
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
	 * @return the paginaWeb
	 */
	public String getPaginaWeb() {
		return paginaWeb;
	}



	/**
	 * @param paginaWeb the paginaWeb to set
	 */
	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
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



	public String getContraseniaAdministrador() {
		return contraseniaAdministrador;
	}



	public void setContraseniaAdministrador(String contraseniaAdministrador) {
		this.contraseniaAdministrador = contraseniaAdministrador;
	}

}
