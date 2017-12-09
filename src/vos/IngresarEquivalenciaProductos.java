package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngresarEquivalenciaProductos {
	
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="id_producto1")
	private Long id_producto1;
	
	@JsonProperty(value="id_producto2")
	private Long id_producto2;


	/**
	 * Metodo constructor de la clase Tiene_Pedido
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public IngresarEquivalenciaProductos(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante,@JsonProperty(value="contrasenia") String contrasenia, @JsonProperty(value="id_producto1") Long id_producto1,@JsonProperty(value="id_producto2") Long id_producto2 ) {
		super();
		this.idUsuarioRestaurante= idUsuarioRestaurante;
		this.contrasenia = contrasenia;
		this.id_producto1 = id_producto1;
		this.id_producto2 = id_producto2;
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
		return id_producto1;
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

}

	

