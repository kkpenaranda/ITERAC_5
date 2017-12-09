package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Producto_Venta {
	@JsonProperty(value="id_ProductoVenta")
	private Long id_ProductoVenta;
	
	@JsonProperty(value= "nit")
	private Long nit;
	
	@JsonProperty(value= "costo")
	private Integer costo;
	
	@JsonProperty(value= "precio")
	private Integer precio;
	
	@JsonProperty(value= "cantidad")
	private Integer cantidad;
	
	@JsonProperty(value= "fechaInServicio")
	private Date fechaInServicio;
	
	@JsonProperty(value= "fechaFinServicio")
	private Date fechaFinServicio;
	
	@JsonProperty(value= "cantidadMaxima")
	private Integer cantidadMaxima;
	
	/**
	 * Metodo constructor de la clase id_ProductoVenta
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 */
	public Producto_Venta(@JsonProperty(value="id_ProductoVenta") Long id_ProductoVenta,@JsonProperty(value="nit") Long nit,@JsonProperty(value="costo") Integer costo, @JsonProperty(value="precio") Integer precio,@JsonProperty(value="cantidad") Integer cantidad,@JsonProperty(value="fechaInServicio") Date fechaInServicio,@JsonProperty(value="fechaFinServicio") Date fechaFinServicio, @JsonProperty(value="cantidadMaxima") Integer cantidadMaxima ) {
		super();
		this.id_ProductoVenta= id_ProductoVenta;
		this.nit = nit;
		this.costo = costo;
		this.precio = precio;
		this.cantidad = cantidad;
		this.fechaInServicio = fechaInServicio;
		this.fechaFinServicio = fechaFinServicio;
		this.cantidadMaxima= cantidadMaxima;
    }

	public Long getId_ProductoVenta() {
		return id_ProductoVenta;
	}

	public void setId_ProductoVenta(Long id_ProductoVenta) {
		this.id_ProductoVenta = id_ProductoVenta;
	}

	public Long getNit() {
		return nit;
	}

	public void setNit(Long nit) {
		this.nit = nit;
	}

	public Integer getCosto() {
		return costo;
	}

	public void setCosto(Integer costo) {
		this.costo = costo;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaInServicio() {
		return fechaInServicio;
	}

	public void setFechaInServicio(Date fechaInServicio) {
		this.fechaInServicio = fechaInServicio;
	}

	public Date getFechaFinServicio() {
		return fechaFinServicio;
	}

	public void setFechaFinServicio(Date fechaFinServicio) {
		this.fechaFinServicio = fechaFinServicio;
	}

	public Integer getCantidadMaxima() {
		return cantidadMaxima;
	}

	public void setCantidadMaxima(Integer cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
}