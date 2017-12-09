package vos;
import org.codehaus.jackson.annotate.JsonProperty;

public class CancelarPedido {

	@JsonProperty(value= "idRestaurante")
	private Long idRestaurante;
	
	@JsonProperty(value="contraseniaRestaurante")
	private String contraseniaRestaurante;
	
	@JsonProperty(value="numeroOrdenCancelada")
	private Long numeroOrdenCancelada;

	/**
	 * @param idRestaurante
	 * @param idProductoVenta
	 */
	public CancelarPedido(@JsonProperty(value= "idRestaurante") Long idRestaurante, @JsonProperty(value="contraseniaRestaurante") String contraseniaRestaurante, @JsonProperty(value="numeroOrdenCancelada") Long numeroOrdenCancelada) {
		super();
		this.idRestaurante = idRestaurante;
		this.contraseniaRestaurante= contraseniaRestaurante;
		this.numeroOrdenCancelada= numeroOrdenCancelada;
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

	public Long getNumeroOrdenCancelada() {
		return numeroOrdenCancelada;
	}

	public void setNumeroOrdenCancelada(Long numeroOrdenCancelada) {
		this.numeroOrdenCancelada = numeroOrdenCancelada;
	}
	
	
}
