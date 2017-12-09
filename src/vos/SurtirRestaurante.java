package vos;
import org.codehaus.jackson.annotate.JsonProperty;

public class SurtirRestaurante {

	@JsonProperty(value= "idRestaurante")
	private Long idRestaurante;
	
	@JsonProperty(value="contraseniaRestaurante")
	private String contraseniaRestaurante;

	/**
	 * @param idRestaurante
	 * @param idProductoVenta
	 */
	public SurtirRestaurante(@JsonProperty(value= "idRestaurante") Long idRestaurante, @JsonProperty(value="contraseniaRestaurante") String contraseniaRestaurante) {
		super();
		this.idRestaurante = idRestaurante;
		this.contraseniaRestaurante= contraseniaRestaurante;
	}

	public Long getIdRestaurante() {
		return idRestaurante;
	}

	public void setIdRestaurante(Long idRestaurante) {
		this.idRestaurante = idRestaurante;
	}

	
	public String getContraseniaRestaurante() {
		return contraseniaRestaurante;
	}

	public void setContraseniaRestaurante(String contraseniaRestaurante) {
		this.contraseniaRestaurante = contraseniaRestaurante;
	}
	
	
}
