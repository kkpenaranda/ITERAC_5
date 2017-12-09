package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductosDetalladosPedidos {

	@JsonProperty(value= "dia")
	private int dia;
	
	@JsonProperty(value= "productoMasPedido")
	private ProductoMenu productoMasPedido;
	
	@JsonProperty(value= "productoMenosPedido")
	private ProductoMenu productoMenosPedido;

	/**
	 * @param dia
	 * @param productoMasPedido
	 * @param productoMenosPedido
	 */
	public ProductosDetalladosPedidos(@JsonProperty(value= "dia") int dia, @JsonProperty(value= "productoMasPedido") ProductoMenu productoMasPedido, @JsonProperty(value= "productoMenosPedido") ProductoMenu productoMenosPedido) {
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
	public ProductoMenu getProductoMasPedido() {
		return productoMasPedido;
	}

	/**
	 * @param productoMasPedido the productoMasPedido to set
	 */
	public void setProductoMasPedido(ProductoMenu productoMasPedido) {
		this.productoMasPedido = productoMasPedido;
	}

	/**
	 * @return the productoMenosPedido
	 */
	public ProductoMenu getProductoMenosPedido() {
		return productoMenosPedido;
	}

	/**
	 * @param productoMenosPedido the productoMenosPedido to set
	 */
	public void setProductoMenosPedido(ProductoMenu productoMenosPedido) {
		this.productoMenosPedido = productoMenosPedido;
	}
	
	
	
	
}
