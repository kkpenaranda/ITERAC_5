package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ConsultaPedidos {

	@JsonProperty(value= "idRestaurante")
	private Long idRestaurante;
	
	@JsonProperty(value= "sumaTotalVenta")
	private Double sumaTotalVenta;
	
	@JsonProperty(value= "totalProductos")
	private Double totalProductos;

	/**
	 * @param idRestaurante
	 * @param sumaTotalVenta
	 * @param totalProductos
	 */
	public ConsultaPedidos(@JsonProperty(value= "idRestaurante") Long idRestaurante, @JsonProperty(value= "sumaTotalVenta") Double sumaTotalVenta, @JsonProperty(value= "totalProductos") Double totalProductos) {
		this.idRestaurante = idRestaurante;
		this.sumaTotalVenta = sumaTotalVenta;
		this.totalProductos = totalProductos;
	}

	/**
	 * @return the idRestaurante
	 */
	public Long getIdRestaurante() {
		return idRestaurante;
	}

	/**
	 * @param idRestaurante the idRestaurante to set
	 */
	public void setIdRestaurante(Long idRestaurante) {
		this.idRestaurante = idRestaurante;
	}

	/**
	 * @return the sumaTotalVenta
	 */
	public Double getSumaTotalVenta() {
		return sumaTotalVenta;
	}

	/**
	 * @param sumaTotalVenta the sumaTotalVenta to set
	 */
	public void setSumaTotalVenta(Double sumaTotalVenta) {
		this.sumaTotalVenta = sumaTotalVenta;
	}

	/**
	 * @return the totalProductos
	 */
	public Double getTotalProductos() {
		return totalProductos;
	}

	/**
	 * @param totalProductos the totalProductos to set
	 */
	public void setTotalProductos(Double totalProductos) {
		this.totalProductos = totalProductos;
	}
	
	
}
