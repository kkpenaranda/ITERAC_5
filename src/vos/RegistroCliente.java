package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RegistroCliente {

	@JsonProperty(value= "usuario")
	private Usuario usuario;
	
	@JsonProperty(value= "banco")
	private String banco;
	
	@JsonProperty(value= "idAdministrador")
	private Long idAdministrador;
	
	@JsonProperty(value= "contraseniaAdministrador")
	private String contraseniaAdministrador;

	/**
	 * @param id
	 * @param nombre
	 * @param correoElectronico
	 * @param rol
	 * @param banco
	 */
	public RegistroCliente(@JsonProperty(value= "usuario") Usuario usuario, @JsonProperty(value= "banco") String banco, @JsonProperty(value= "idAdministrador") Long idAdministrador, @JsonProperty(value= "contraseniaAdministrador") String contraseniaAdministrador) {
		super();
		this.usuario= usuario;
		this.banco = banco;
		this.idAdministrador= idAdministrador;
		this.contraseniaAdministrador= contraseniaAdministrador;
	}

	

	/**
	 * @return the banco
	 */
	public String getBanco() {
		return banco;
	}

	/**
	 * @param banco the banco to set
	 */
	public void setBanco(String banco) {
		this.banco = banco;
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



	public String getContraseniaAdministrador() {
		return contraseniaAdministrador;
	}



	public void setContraseniaAdminitrador(String contraseniaAdministrador) {
		this.contraseniaAdministrador = contraseniaAdministrador;
	}
	
	
}
