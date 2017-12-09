package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Ingrediente;
import vos.Ingrediente_Restaurante;
import vos.Usuario;

public class DAOTablaIngrediente_Restaurante {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaIngrediente_Restaurante
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaIngrediente_Restaurante() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de ingredientes_restaurantes
	 * <b>post: </b> Todos los recurso del arreglo de ingredientes_restaurantes han sido cerrados
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los ingredientes_restaurantes de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM INGREDIENTES_RESTAURANTE;
	 * @return Arraylist con los ingredientes_restaurantes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Ingrediente_Restaurante> darIngrediente_Restaurantes() throws SQLException, Exception {
		ArrayList<Ingrediente_Restaurante> ingredientes_restaurantes = new ArrayList<Ingrediente_Restaurante>();

		String sql = "SELECT * FROM INGREDIENTES_RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id_ingrediente = rs.getLong("ID_INGREDIENTE");
			Long id_producto= rs.getLong("NIT");

			ingredientes_restaurantes.add(new Ingrediente_Restaurante(id_ingrediente, id_producto));
		}
		return ingredientes_restaurantes;
	}


	/**
	 * Metodo que agrega el ingrediente_producto que entra como parametro a la base de datos.
	 * @param ingrediente_producto - la ingrediente_producto a agregar. ingrediente_producto !=  null
	 * <b> post: </b> se ha agregado la ingrediente_producto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la ingrediente_producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la ingrediente_producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addIngrediente_Restaurante(Long idIngrediente, Long nit) throws SQLException, Exception {

		String sql = "INSERT INTO INGREDIENTES_RESTAURANTE (ID_INGREDIENTE, NIT) VALUES (";
		sql += idIngrediente + ",";
		sql += nit + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public Ingrediente_Restaurante buscarIngredientePorId(Long idIngrediente) throws SQLException, Exception 
	{
		Ingrediente_Restaurante ingrediente_Restaurante = null;

		String sql = "SELECT * FROM INGREDIENTES_RESTAURANTE WHERE ID_INGREDIENTE =" + idIngrediente;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long idIngrediente2 = rs.getLong("ID_INGREDIENTE");
			Long nit = rs.getLong("NIT");


			ingrediente_Restaurante = new Ingrediente_Restaurante(idIngrediente2, nit);
		}

		return ingrediente_Restaurante;
	}
	
	public void deleteRelacion(Long idIngrediente) throws SQLException, Exception {

		String sql = "DELETE FROM INGREDIENTES_RESTAURANTE";
		sql += " WHERE ID_INGREDIENTE = " + idIngrediente;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
}
