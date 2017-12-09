package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RestaurantesFrecuentadosDia {

	@JsonProperty(value= "dia")
	private int dia;
	
	@JsonProperty(value= "restauranteMasFrecuentado")
	private Long restauranteMasFrecuentado;
	
	@JsonProperty(value= "restauranteMenosFrecuentado")
	private Long restauranteMenosFrecuentado;

	/**
	 * @param dia
	 * @param restauranteMasFrecuentado
	 * @param restauranteMenosFrecuentado
	 */
	public RestaurantesFrecuentadosDia(@JsonProperty(value= "dia") int dia, @JsonProperty(value= "restauranteMasFrecuentado") Long restauranteMasFrecuentado,
			@JsonProperty(value= "restauranteMenosFrecuentado") Long restauranteMenosFrecuentado) {
		super();
		this.dia = dia;
		this.restauranteMasFrecuentado = restauranteMasFrecuentado;
		this.restauranteMenosFrecuentado = restauranteMenosFrecuentado;
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
	 * @return the restauranteMasFrecuentado
	 */
	public Long getRestauranteMasFrecuentado() {
		return restauranteMasFrecuentado;
	}

	/**
	 * @param restauranteMasFrecuentado the restauranteMasFrecuentado to set
	 */
	public void setRestauranteMasFrecuentado(Long restauranteMasFrecuentado) {
		this.restauranteMasFrecuentado = restauranteMasFrecuentado;
	}

	/**
	 * @return the restauranteMenosFrecuentado
	 */
	public Long getRestauranteMenosFrecuentado() {
		return restauranteMenosFrecuentado;
	}

	/**
	 * @param restauranteMenosFrecuentado the restauranteMenosFrecuentado to set
	 */
	public void setRestauranteMenosFrecuentado(Long restauranteMenosFrecuentado) {
		this.restauranteMenosFrecuentado = restauranteMenosFrecuentado;
	}
	
	
}
