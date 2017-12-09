package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductosPedidos {

	@JsonProperty(value= "dia")
	private int dia;
	
	@JsonProperty(value= "productoMasPedido")
	private Long productoMasPedido;
	
	@JsonProperty(value= "productoMenosPedido")
	private Long productoMenosPedido;

	/**
	 * @param dia
	 * @param productoMasPedido
	 * @param productoMenosPedido
	 */
	public ProductosPedidos(@JsonProperty(value= "dia") int dia, @JsonProperty(value= "productoMasPedido") Long productoMasPedido, @JsonProperty(value= "productoMenosPedido") Long productoMenosPedido) {
		this.dia = dia;
		this.productoMasPedido = productoMasPedido;
		this.productoMenosPedido = productoMenosPedido;
	}

	/**
	 * @return the dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setDia(int dia) {
		this.dia = dia;
	}

	/**
	 * @return the productoMasPedido
	 */
	public Long getProductoMasPedido() {
		return productoMasPedido;
	}

	/**
	 * @param productoMasPedido the productoMasPedido to set
	 */
	public void setProductoMasPedido(Long productoMasPedido) {
		this.productoMasPedido = productoMasPedido;
	}

	/**
	 * @return the productoMenosPedido
	 */
	public Long getProductoMenosPedido() {
		return productoMenosPedido;
	}

	/**
	 * @param productoMenosPedido the productoMenosPedido to set
	 */
	public void setProductoMenosPedido(Long productoMenosPedido) {
		this.productoMenosPedido = productoMenosPedido;
	}
	
	
	
	
}
