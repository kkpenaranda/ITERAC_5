package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Equivalencia_Ingredientes {
	
	@JsonProperty(value="id_ingrediente1")
	private Long id_ingrediente1;
	
	@JsonProperty(value="id_ingrediente2")
	private Long id_ingrediente2;
	
	@JsonProperty(value= "id_restaurante")
	private Long id_restaurante;

	/**
	 * Metodo constructor de la clase Tiene_Pedido
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public Equivalencia_Ingredientes(@JsonProperty(value="id_ingrediente1") Long id_ingrediente1,@JsonProperty(value="id_ingrediente2") Long id_ingrediente2, @JsonProperty(value="id_restaurante") Long id_restaurante) {
		super();
		this.id_ingrediente1 = id_ingrediente1;
		this.id_ingrediente2 = id_ingrediente2;
		this.id_restaurante= id_restaurante;
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

	public Long getId_restaurante() {
		return id_restaurante;
	}

	public void setId_restaurante(Long id_restaurante) {
		this.id_restaurante = id_restaurante;
	}
	
}
