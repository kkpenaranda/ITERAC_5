package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarIngrediente {
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value= "ingrediente")
	private Ingrediente ingrediente;
	
	
	/**
	 * Metodo constructor de la clase IngresarIngrediente
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarIngrediente(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante,@JsonProperty(value="contrasenia") String contrasenia, @JsonProperty(value= "ingrediente") Ingrediente ingrediente) {
		super();
		this.idUsuarioRestaurante= idUsuarioRestaurante;
		this.contrasenia= contrasenia;
		this.ingrediente = ingrediente;
		
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

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente) {
		this.ingrediente = ingrediente;
	}
	
}
