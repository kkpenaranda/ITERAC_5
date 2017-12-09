package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Producto_Venta;

public class DAOTablaProducto_Venta {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> products_venta;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaProducto_Venta
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaProducto_Venta() {
		products_venta = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de products_venta
	 * <b>post: </b> Todos los recurso del arreglo de products_venta han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : products_venta){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que inicializa la connection del DAO a la base de datos con la conexion que entra como parametro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Metodo que, usando la conexion a la base de datos, saca todos los products_venta de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PRODUCTOVENTA;
	 * @return Arraylist con los products_venta de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto_Venta> darProductos_Venta() throws SQLException, Exception {
		ArrayList<Producto_Venta> productos_venta = new ArrayList<Producto_Venta>();

		String sql = "SELECT * FROM PRODUCTOVENTA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id_ProductoVenta = rs.getLong("ID_PRODUCTOVENTA");
			Integer precio= rs.getInt("PRECIO");
			Integer costo= rs.getInt("COSTO");
			Integer cantidad= rs.getInt("CANTIDAD");
			Long nit= rs.getLong("NIT");
			Date fi = rs.getDate("FECHA_IN_SERVICIO");
			Date ff = rs.getDate("FECHA_FIN_SERVICIO");
			Integer cantidadMax= rs.getInt("CANTIDAD_MAXIMA");

			productos_venta.add(new Producto_Venta(id_ProductoVenta, nit, costo, precio,cantidad,fi,ff, cantidadMax));
		}
		return productos_venta;
	}


	/**
	 * Metodo que agrega el tiene_producto que entra como parametro a la base de datos.
	 * <b> post: </b> se ha agregado la tiene_producto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la tiene_producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la tiene_producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addProducto_Venta(Long idProductoVenta, Long nit, Integer costo, Integer precio, Integer cantidad, Date fi, Date ff, Integer cantidadMax) throws SQLException, Exception {

		String sql = "INSERT INTO PRODUCTOVENTA (ID_PRODUCTOVENTA,PRECIO,COSTO, CANTIDAD, NIT, FECHA_IN_SERVICIO, FECHA_FIN_SERVICIO, CANTIDAD_MAXIMA ) VALUES (";
		sql += idProductoVenta + ",";
		sql += precio + ",";
		sql += costo + ",";
		sql += cantidad +",";
		sql += nit + ",TO_DATE('";
		sql += fi + "','YYYY-MM-DD'),TO_DATE('";
		sql += ff + "','YYYY-MM-DD'),";
		sql += cantidadMax + ")";

		System.out.println("sql:"+sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		System.out.println(sql+"casi");
		prepStmt.executeQuery();

	}
	
	public void addProductoVenta(Producto_Venta prodVenta) throws SQLException, Exception {

		String sql = "INSERT INTO PRODUCTOVENTA (ID_PRODUCTOVENTA,PRECIO,COSTO, CANTIDAD, NIT, FECHA_IN_SERVICIO, FECHA_FIN_SERVICIO, CANTIDAD_MAXIMA ) VALUES (";
		sql += prodVenta.getId_ProductoVenta() + ",";
		sql += prodVenta.getPrecio() + ",";
		sql += prodVenta.getCosto() + ",";
		sql += prodVenta.getCantidad() +",";
		sql += prodVenta.getNit() + ",TO_DATE('";
		sql += prodVenta.getFechaInServicio() + "','YYYY-MM-DD'),TO_DATE('";
		sql += prodVenta.getFechaFinServicio() + "','YYYY-MM-DD'),";
		sql += prodVenta.getCantidadMaxima() +")";

		System.out.println("sql:"+sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		System.out.println(sql+"casi");
		prepStmt.executeQuery();

	}
	
	public Producto_Venta buscarProductoPorId(Long id) throws SQLException, Exception 
	{
		Producto_Venta producto_vent = null;

		String sql = "SELECT * FROM PRODUCTOVENTA WHERE ID_PRODUCTOVENTA=" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id_ProductoVenta = rs.getLong("ID_PRODUCTOVENTA");
			Integer precio= rs.getInt("PRECIO");
			Integer costo= rs.getInt("COSTO");
			Integer cantidad= rs.getInt("CANTIDAD");
			Long nit= rs.getLong("NIT");
			Date fi = rs.getDate("FECHA_IN_SERVICIO");
			Date ff = rs.getDate("FECHA_FIN_SERVICIO");
			Integer cant= rs.getInt("CANTIDAD_MAXIMA");
			producto_vent = new Producto_Venta(id_ProductoVenta, nit, costo, precio,cantidad,fi,ff, cant);
		}

		return producto_vent;
	}
	
	public Integer cantidadProducto(Long idProducto) throws SQLException, Exception 
	{
		String sql = "SELECT CANTIDAD FROM PRODUCTOVENTA WHERE ID_PRODUCTOVENTA=" + idProducto;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		Integer cantidad= null;
		if(rs.next()){
		     cantidad= rs.getInt("CANTIDAD");
		}
		return cantidad;
	}
	
	public void actualizarCantidadProductos(Long idProducto, Integer cantidad) throws SQLException, Exception 
	{
		System.out.println("actualizando cantidad de producto...");
		
		String sql = "UPDATE PRODUCTOVENTA SET ";
		sql += "CANTIDAD=" + cantidad;
		sql += " WHERE ID_PRODUCTOVENTA= " + idProducto;
		System.out.println(sql);


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void surtirProductos(Long idRestaurante) throws SQLException, Exception 
	{
		System.out.println("actualizando cantidad de producto...");
		
		String sql = "UPDATE PRODUCTOVENTA SET ";
		sql += "CANTIDAD= CANTIDAD_MAXIMA ";
		sql += "WHERE NIT =" + idRestaurante + " ";
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void reducirCantidadProductos(Long idProducto, Integer cantidad) throws SQLException, Exception 
	{
		System.out.println("actualizando cantidad de producto...");
		
		String sql = "UPDATE PRODUCTOVENTA SET ";
		sql += "CANTIDAD= ((SELECT CANTIDAD FROM PRODUCTOVENTA WHERE ID_PRODUCTOVENTA= " + idProducto +" ) -"+ cantidad+" )" ;
		sql += " WHERE ID_PRODUCTOVENTA= " + idProducto;
		System.out.println(sql);


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void updateProducto(Producto_Venta productoVenta) throws SQLException, Exception {

		String sql = "UPDATE PRODUCTOVENTA SET ";
		
		sql += "PRECIO" + productoVenta.getPrecio();
		sql += "COSTO" + productoVenta.getCosto();
		sql += "CANTIDAD" + productoVenta.getCantidad();
		sql += "NIT" + productoVenta.getNit();
		sql += "FECHA_IN_SERVICIO" + productoVenta.getFechaInServicio();
		sql += "FECHA_FIN_SERVICIO"+ productoVenta.getFechaFinServicio();
		sql += " WHERE ID_PRODUCTOVENTA = " + productoVenta.getId_ProductoVenta();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		products_venta.add(prepStmt);
		prepStmt.executeQuery();
	}
	
}