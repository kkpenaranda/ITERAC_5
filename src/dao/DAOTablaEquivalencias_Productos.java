package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Equivalencia_Productos;
import vos.IngresarEquivalenciaProductos;
import vos.Pedido;
import vos.Usuario;
import vos.Equivalencia_Productos;
import vos.Zona;

public class DAOTablaEquivalencias_Productos {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaEquivalencia_Productos
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaEquivalencias_Productos() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los recursos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM EQUIVALENCIAS_PRODUCTOS;
	 * @return Arraylist con los recursos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Equivalencia_Productos> darEquivalencias_Productos() throws SQLException, Exception {
		ArrayList<Equivalencia_Productos> equivalentes = new ArrayList<Equivalencia_Productos>();

		String sql = "SELECT * FROM EQUIVALENCIAS_PRODUCTOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id_producto1= rs.getLong("ID_PRODUCTO1");
			Long id_producto2= rs.getLong("ID_PRODUCTO2");
			Long id_restaurante = rs.getLong("NIT_RESTAURANTE");

			equivalentes.add(new Equivalencia_Productos(id_producto1,id_producto2, id_restaurante));
		}
		return equivalentes;
	}

	
	public void addEquivalencia_Productos(Equivalencia_Productos ep) throws SQLException, Exception {

		String sql = "INSERT INTO EQUIVALENCIAS_PRODUCTOS (ID_PRODUCTO1,ID_PRODUCTO2,NIT_RESTAURANTE) VALUES (";
		sql += ep.getId_producto1() + ",";
		sql += ep.getId_producto2() + ",";
		sql += ep.getId_restaurante() + ")";

		System.out.println("sql:"+sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public void addEquivalencia_Productos(Long p1, Long p2, Long rest) throws SQLException, Exception {

		String sql = "INSERT INTO EQUIVALENCIAS_PRODUCTOS (ID_PRODUCTO1,ID_PRODUCTO2,NIT_RESTAURANTE) VALUES (";
		sql += p1 + ",";
		sql += p2 + ",";
		sql += rest + ")";

		System.out.println("sql:"+sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public Equivalencia_Productos buscarEquivalenciaProductosPorIds(Long p1, Long p2) throws SQLException, Exception 
	{
		Equivalencia_Productos ep = null;

		String sql = "SELECT * FROM EQUIVALENCIAS_PRODUCTOS WHERE ID_PRODUCTO1 =" + p1 +" AND ID_PRODUCTO2="+p2+" OR ID_PRODUCTO1 =" + p2 +" AND ID_PRODUCTO2="+p1;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id_producto1= rs.getLong("ID_PRODUCTO1");
			Long id_producto2= rs.getLong("ID_PRODUCTO2");
			Long id_restaurante = rs.getLong("NIT_RESTAURANTE");

			ep = new Equivalencia_Productos(id_producto1,id_producto2, id_restaurante);
		}

		return ep;
	}
	
	
	public void delete(IngresarEquivalenciaProductos idProducto) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIAS_PRODUCTOS";
		sql += " WHERE ID_PRODUCTO1 = " + idProducto.getId_producto1() +  " AND ID_PRODUCTO2=" + idProducto.getId_producto2();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
