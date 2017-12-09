package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RentabilidadRestaurante {
	
	@JsonProperty(value= "idRestaurante")
	private String idRestaurante;

	@JsonProperty(value= "totalVendidos")
	private Integer totalVendidos;
	
	@JsonProperty(value= "totalPagar")
	private Double totalPagar;
	
	/**
	 * @param totalVendidos
	 * @param totalPagar
	 */
	public RentabilidadRestaurante(@JsonProperty(value= "idRestaurante") String idRestaurante, @JsonProperty(value= "totalVendidos") Integer totalVendidos, @JsonProperty(value= "totalPagar") Double totalPagar) {
		super();
		this.idRestaurante= idRestaurante;
		this.totalVendidos = totalVendidos;
		this.totalPagar = totalPagar;
	}

	/**
	 * @return the totalVendidos
	 */
	public Integer getTotalVendidos() {
		return totalVendidos;
	}

	/**
	 * @param totalVendidos the totalVendidos to set
	 */
	public void setTotalVendidos(Integer totalVendidos) {
		this.totalVendidos = totalVendidos;
	}

	/**
	 * @return the totalPagar
	 */
	public Double getTotalPagar() {
		return totalPagar;
	}

	/**
	 * @param totalPagar the totalPagar to set
	 */
	public void setTotalPagar(Double totalPagar) {
		this.totalPagar = totalPagar;
	}

	/**
	 * @return the idRestaurante
	 */
	public String getIdRestaurante() {
		return idRestaurante;
	}

	/**
	 * @param idRestaurante the idRestaurante to set
	 */
	public void setIdRestaurante(String idRestaurante) {
		this.idRestaurante = idRestaurante;
	}

	
	
}
