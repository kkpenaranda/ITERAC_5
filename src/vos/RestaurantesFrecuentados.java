package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class RestaurantesFrecuentados {

	@JsonProperty(value= "dia")
	private int dia;
	
	@JsonProperty(value= "restauranteMasFrecuentado")
	private Restaurante restauranteMasFrecuentado;
	
	@JsonProperty(value= "restauranteMenosFrecuentado")
	private Restaurante restauranteMenosFrecuentado;

	/**
	 * @param dia
	 * @param restauranteMasFrecuentado
	 * @param restauranteMenosFrecuentado
	 */
	public RestaurantesFrecuentados(@JsonProperty(value= "dia") int dia, @JsonProperty(value= "restauranteMasFrecuentado") Restaurante restauranteMasFrecuentado,
			@JsonProperty(value= "restauranteMenosFrecuentado") Restaurante restauranteMenosFrecuentado) {
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
	public Restaurante getRestauranteMasFrecuentado() {
		return restauranteMasFrecuentado;
	}

	/**
	 * @param restauranteMasFrecuentado the restauranteMasFrecuentado to set
	 */
	public void setRestauranteMasFrecuentado(Restaurante restauranteMasFrecuentado) {
		this.restauranteMasFrecuentado = restauranteMasFrecuentado;
	}

	/**
	 * @return the restauranteMenosFrecuentado
	 */
	public Restaurante getRestauranteMenosFrecuentado() {
		return restauranteMenosFrecuentado;
	}

	/**
	 * @param restauranteMenosFrecuentado the restauranteMenosFrecuentado to set
	 */
	public void setRestauranteMenosFrecuentado(Restaurante restauranteMenosFrecuentado) {
		this.restauranteMenosFrecuentado = restauranteMenosFrecuentado;
	}
	
	
}
