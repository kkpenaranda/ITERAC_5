package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Equivalencia_Ingredientes;
import vos.IngresarEquivalenciaIngredientes;
import vos.IngresarEquivalenciaProductos;
import vos.Equivalencia_Ingredientes;
import vos.Zona;

public class DAOTablaEquivalencias_Ingredientes {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaEquivalencia_Ingredientes
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaEquivalencias_Ingredientes() {
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
	 * <b>SQL Statement:</b> SELECT * FROM EQUIVALENCIAS_INGREDIENTES;
	 * @return Arraylist con los recursos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Equivalencia_Ingredientes> darEquivalencias_Ingredientes() throws SQLException, Exception {
		ArrayList<Equivalencia_Ingredientes> equivalentes = new ArrayList<Equivalencia_Ingredientes>();

		String sql = "SELECT * FROM EQUIVALENCIAS_INGREDIENTES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id_ingrediente1= rs.getLong("ID_INGREDIENTE1");
			Long id_ingrediente2= rs.getLong("ID_INGREDIENTE2");
			Long id_restaurante = rs.getLong("NIT_RESTAURANTE");

			equivalentes.add(new Equivalencia_Ingredientes(id_ingrediente1,id_ingrediente2, id_restaurante));
		}
		return equivalentes;
	}

	
	public void addEquivalencia_Ingredientes(Equivalencia_Ingredientes ei) throws SQLException, Exception {

		String sql = "INSERT INTO EQUIVALENCIAS_INGREDIENTES (ID_INGREDIENTE1,ID_INGREDIENTE2,NIT_RESTAURANTE) VALUES (";
		sql += ei.getId_ingrediente1() + ",";
		sql += ei.getId_ingrediente2() + ",";
		sql += ei.getId_restaurante() + ")";

		System.out.println("sql:"+sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public void addEquivalencia_Ingredientes(Long i1, Long i2, Long rest) throws SQLException, Exception {

		String sql = "INSERT INTO EQUIVALENCIAS_INGREDIENTES (ID_INGREDIENTE1,ID_INGREDIENTE2,NIT_RESTAURANTE) VALUES (";
		sql += i1 + ",";
		sql += i2 + ",";
		sql += rest + ")";

		System.out.println("sql:"+sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public void delete(IngresarEquivalenciaIngredientes idProducto) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIAS_INGREDIENTES";
		sql += " WHERE ID_INGREDIENTE1 = " + idProducto.getId_ingrediente1() +  " AND ID_INGREDIENTE2=" + idProducto.getId_ingrediente2();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
