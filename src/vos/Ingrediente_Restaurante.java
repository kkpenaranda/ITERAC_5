package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Ingrediente_Restaurante {
	@JsonProperty(value="id_Ingrediente")
	private Long id_Ingrediente;
	
	
	@JsonProperty(value= "nit_restaurante")
	private Long nit_restaurante;
	
	/**
	 * Metodo constructor de la clase nit_restaurante
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public Ingrediente_Restaurante(@JsonProperty(value="id_Ingrediente") Long id_Ingrediente,@JsonProperty(value="nit_restaurante") Long nit_restaurante) {
		super();
		this.id_Ingrediente= id_Ingrediente;
		this.nit_restaurante = nit_restaurante;
	}

	public Long getId_Ingrediente() {
		return id_Ingrediente;
	}

	public void setId_Ingrediente(Long id_Ingrediente) {
		this.id_Ingrediente = id_Ingrediente;
	}

	public Long getNit_restaurante() {
		return nit_restaurante;
	}

	public void setNit_restaurante(Long nit_restaurante) {
		this.nit_restaurante = nit_restaurante;
	}	
}
