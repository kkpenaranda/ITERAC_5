package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ConsultaPedidosPorProducto {

	@JsonProperty(value= "idProductoVenta")
	private Long idProductoVenta;
	
	@JsonProperty(value= "sumaTotalVenta")
	private Double sumaTotalVenta;
	
	@JsonProperty(value= "totalProductos")
	private Double totalProductos;

	/**
	 * @param idRestaurante
	 * @param sumaTotalVenta
	 * @param totalProductos
	 */
	public ConsultaPedidosPorProducto(@JsonProperty(value= "idProductoVenta") Long idRestaurante, @JsonProperty(value= "sumaTotalVenta") Double sumaTotalVenta, @JsonProperty(value= "totalProductos") Double totalProductos) {
		this.idProductoVenta = idRestaurante;
		this.sumaTotalVenta = sumaTotalVenta;
		this.totalProductos = totalProductos;
	}

	
	/**
	 * @return the idProductoVenta
	 */
	public Long getIdProductoVenta() {
		return idProductoVenta;
	}


	/**
	 * @param idProductoVenta the idProductoVenta to set
	 */
	public void setIdProductoVenta(Long idProductoVenta) {
		this.idProductoVenta = idProductoVenta;
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
