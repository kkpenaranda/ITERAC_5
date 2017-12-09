package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Equivalencia_Productos {
	
	@JsonProperty(value="id_producto1")
	private Long id_producto1;
	
	@JsonProperty(value="id_producto2")
	private Long id_producto2;
	
	@JsonProperty(value= "id_restaurante")
	private Long id_restaurante;

	/**
	 * Metodo constructor de la clase Tiene_Pedido
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public Equivalencia_Productos(@JsonProperty(value="id_producto1") Long id_producto1,@JsonProperty(value="id_producto2") Long id_producto2, @JsonProperty(value="id_restaurante") Long id_restaurante) {
		super();
		this.id_producto1 = id_producto1;
		this.id_producto2 = id_producto2;
		this.id_restaurante= id_restaurante;
	}
	
	public Long getId_producto1() {
		return id_producto1;
	}


	public void setId_producto1(Long id_producto1) {
		this.id_producto1 = id_producto1;
	}

	public Long getId_producto2() {
		return id_producto2;
	}


	public void setId_producto2(Long id_producto2) {
		this.id_producto2 = id_producto2;
	}



	public Long getId_restaurante() {
		return id_restaurante;
	}

	public void setId_restaurante(Long id_restaurante) {
		this.id_restaurante = id_restaurante;
	}
}
