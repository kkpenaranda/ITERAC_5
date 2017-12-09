package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarEquivalenciaIngredientes {
	
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="id_ingrediente1")
	private Long id_ingrediente1;
	
	@JsonProperty(value="id_ingrediente2")
	private Long id_ingrediente2;


	/**
	 * Metodo constructor de la clase Tiene_Pedido
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarEquivalenciaIngredientes(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante,@JsonProperty(value="contrasenia") String contrasenia, @JsonProperty(value="id_ingrediente1") Long id_ingrediente1,@JsonProperty(value="id_ingrediente2") Long id_ingrediente2 ) {
		super();
		this.idUsuarioRestaurante= idUsuarioRestaurante;
		this.contrasenia = contrasenia;
		this.id_ingrediente1 = id_ingrediente1;
		this.id_ingrediente2 = id_ingrediente2;
	}


	public Long getIdUsuarioRestaurante() {
		return idUsuarioRestaurante;
	}


	public void setIdUsuarioRestaurante(Long idUsuarioRestaurante) {
		this.idUsuarioRestaurante = idUsuarioRestaurante;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	public Long getId_ingrediente1() {
		return id_ingrediente1;
	}


	public void setId_ingrediente1(Long id_ingrediente1) {
		this.id_ingrediente1 = id_ingrediente1;
	}


	public Long getId_ingrediente2() {
		return id_ingrediente2;
	}


	public void setId_ingrediente2(Long id_ingrediente2) {
		this.id_ingrediente2 = id_ingrediente2;
	}

}

	

