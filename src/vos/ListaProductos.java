/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: ProductoAndes
 * Autor: Juan Felipe García - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package vos;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa una arreglo de Producto
 * @author 
 */
public class ListaProductos {
	
	/**
	 * List con los Productos
	 */
	@JsonProperty(value="productos")
	private List<Producto> productos;
	
	/**
	 * Constructor de la clase ListaProductos
	 * @param Productos - Productos para agregar al arreglo de la clase
	 */
	public ListaProductos( @JsonProperty(value="productos")List<Producto> Productos){
		this.productos = Productos;
	}

	/**
	 * Método que retorna la lista de Productos
	 * @return  List - List con los Productos
	 */
	public List<Producto> getProductos() {
		return productos;
	}

	/**
	 * Método que asigna la lista de Productos que entra como parametro
	 * @param  Productos - List con los Productos ha agregar
	 */
	public void setProducto(List<Producto> Productos) {
		this.productos = Productos;
	}
	
}
