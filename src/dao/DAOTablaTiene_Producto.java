package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Producto;
import vos.Tiene_Producto;
import vos.Zona;

public class DAOTablaTiene_Producto {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> tiene_productos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaTiene_Producto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaTiene_Producto() {
		tiene_productos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de tiene_productos
	 * <b>post: </b> Todos los recurso del arreglo de tiene_productos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : tiene_productos){
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los tiene_productos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM TIENE_PRODUCTO;
	 * @return Arraylist con los tiene_productos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Tiene_Producto> darTiene_Productos() throws SQLException, Exception {
		ArrayList<Tiene_Producto> zones = new ArrayList<Tiene_Producto>();

		String sql = "SELECT * FROM TIENE_PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		tiene_productos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id_menu = rs.getLong("ID_MENU");
			Long id_producto= rs.getLong("ID_PRODUCTO");
			Long id_categoria= rs.getLong("ID_CATEGORIA");

			zones.add(new Tiene_Producto(id_menu, id_producto, id_categoria));
		}
		return zones;
	}
	
	//para req 10
	public ArrayList<Long> darProductosDelMenu(Long idMenu) throws SQLException, Exception {
		ArrayList<Long> productos = new ArrayList<Long>();

		String sql = "SELECT ID_PRODUCTO FROM TIENE_PRODUCTO WHERE ID_MENU="+idMenu+"";

		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		tiene_productos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(id);
		}
		System.out.println("tantos productos -> "+productos.size());
		return productos;
	}
	
//	public ArrayList<Tiene_Producto> darProductosDelMenu2(Long idMenu) throws SQLException, Exception {
//		ArrayList<Tiene_Producto> zones = new ArrayList<Tiene_Producto>();
//
//		String sql = "SELECT ID_PRODUCTO FROM TIENE_PRODUCTO WHERE ID_MENU="+idMenu+"";
//
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		tiene_productos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
//
//		while (rs.next()) {
//			Long id_menu = rs.getLong("ID_MENU");
//			Long id_producto= rs.getLong("ID_PRODUCTO");
//			Long id_categoria= rs.getLong("ID_CATEGORIA");
//
//			zones.add(new Tiene_Producto(id_menu, id_producto, id_categoria));
//		}
//		return zones;
//	}
	



	/**
	 * Metodo que agrega el tiene_producto que entra como parametro a la base de datos.
	 * <b> post: </b> se ha agregado la tiene_producto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la tiene_producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la tiene_producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addTiene_Producto(Long idMenu, Long idProducto, Long idCategoria) throws SQLException, Exception {

		String sql = "INSERT INTO TIENE_PRODUCTO (ID_MENU, ID_PRODUCTO, ID_CATEGORIA) VALUES (";
		sql += idMenu + ",";
		sql += idProducto + ",";
		sql += idCategoria + ")";

		System.out.println("sql:"+sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		tiene_productos.add(prepStmt);
		System.out.println(sql+"casi");
		prepStmt.executeQuery();

	}
	
	public void deleteProducto(Long producto) throws SQLException, Exception {

		String sql = "DELETE FROM TIENE_PRODUCTO";
		sql += " WHERE ID_PRODUCTO = " + producto;

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		tiene_productos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
}