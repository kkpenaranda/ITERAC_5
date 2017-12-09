package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Mesa {

	@JsonProperty(value="numeroMesa")
	private Long numeroMesa;
	
	@JsonProperty(value="cantidadSillas")
	private Integer cantidadSillas;
	
//	@JsonProperty(value= "disponible")
//	private boolean disponible;

	/**
	 * @param numeroMesa
	 * @param cantidadSillas
	 * @param disponible
	 */
	public Mesa(@JsonProperty(value="numeroMesa") Long numeroMesa,@JsonProperty(value="cantidadSillas") Integer cantidadSillas) {
		super();
		this.numeroMesa = numeroMesa;
		this.cantidadSillas = cantidadSillas;
//		this.disponible = disponible;
	}

	public Long getNumeroMesa() {
		return numeroMesa;
	}

	public void setNumeroMesa(Long numeroMesa) {
		this.numeroMesa = numeroMesa;
	}

	public Integer getCantidadSillas() {
		return cantidadSillas;
	}

	public void setCantidadSillas(Integer cantidadSillas) {
		this.cantidadSillas = cantidadSillas;
	}

//	public boolean isDisponible() {
//		return disponible;
//	}
//
//	public void setDisponible(boolean disponible) {
//		this.disponible = disponible;
//	}
}
