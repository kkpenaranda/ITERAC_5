package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServirPedido {
	
	@JsonProperty(value="idUsuarioRestaurante")
	private Long idUsuarioRestaurante;	
	
	@JsonProperty(value="contrasenia")
	private String contrasenia;
	
	@JsonProperty(value="idPedido")
	private Long idPedido;
	
	/**
	 * Metodo constructor de la clase Pedido
	 * <b>post: </b> Crea la totalAPagar con los valores que entran como parametro
	 */
	public ServirPedido(@JsonProperty(value="idUsuarioRestaurante") Long idUsuarioRestaurante, @JsonProperty(value= "contrasenia") String contrasenia, @JsonProperty(value="idPedido") Long idPedido ) {
		super();
		this.idUsuarioRestaurante = idUsuarioRestaurante;
		this.contrasenia = contrasenia;
		this.idPedido = idPedido;
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

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	
}
