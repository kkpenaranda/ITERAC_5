package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Categoria;
import vos.Mesa;

public class DAOTablaMesa {
	
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> mesas;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablaCategoria
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaMesa() {
		mesas = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de categorias
	 * <b>post: </b> Todos los recurso del arreglo de categorias han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : mesas){
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los categorias de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM categoria;
	 * @return Arraylist con los categorias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Mesa> darMesas() throws SQLException, Exception {
		ArrayList<Mesa> mesa = new ArrayList<Mesa>();

		String sql = "SELECT * FROM MESA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		mesas.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long numeroMesa = rs.getLong("NUMERO_MESA");
			Integer cantidad= rs.getInt("CANTIDAD_SILLAS");
//			boolean disponible= rs.getBoolean("DISPONIBLE");

			mesa.add(new Mesa(numeroMesa, cantidad));
		}
		return mesa;
	}


	
	
	/**
	 * Metodo que busca el categoria con el id que entra como parametro.
	 * @param idcategoria - Id del categoria a buscar
	 * @return categoria encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Mesa buscarMesaPorId(Long idMesa) throws SQLException, Exception 
	{
		Mesa mesa = null;

		String sql = "SELECT * FROM MESA WHERE NUMERO_MESA =" + idMesa;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		mesas.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long numeroMesa = rs.getLong("NUMERO_MESA");
			Integer cantidad= rs.getInt("CANTIDAD_SILLAS");
//			boolean disponible= rs.getBoolean("DISPONIBLE");

			mesa = new Mesa(numeroMesa, cantidad);
		}

		return mesa;
	}

	/**
	 * Metodo que agrega el categoria que entra como parametro a la base de datos.
	 * @param mesa - la categoria a agregar. categoria !=  null
	 * <b> post: </b> se ha agregado la categoria a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la categoria baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la categoria a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addMesa(Mesa mesa) throws SQLException, Exception {

		String sql = "INSERT INTO MESA (NUMERO_MESA, CANTIDAD_SILLAS) VALUES (";
		sql += mesa.getNumeroMesa() + ",";
		sql += mesa.getCantidadSillas() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		mesas.add(prepStmt);
		prepStmt.executeQuery();

	}
}
