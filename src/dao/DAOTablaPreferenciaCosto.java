package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.PreferenciaPrecio;
import vos.Producto;

public class DAOTablaPreferenciaCosto {


	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOPreferenciaCosto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPreferenciaCosto() {
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
	 * Metodo que, usando la conexion a la base de datos, saca todas las preferencia de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM PreferenciaCosto;
	 * @return Arraylist con las productos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<PreferenciaPrecio> darPreferenciaPrecio() throws SQLException, Exception {
		ArrayList<PreferenciaPrecio> preferenciascostos = new ArrayList<PreferenciaPrecio>();

		String sql = "SELECT * FROM PREFERENCIAS_PRECIOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("IDPREFERENCIA");
			Long idcosto = rs.getLong("PRECIO");
			Long idCliente = rs.getLong("IDCLIENTE");
			preferenciascostos.add(new PreferenciaPrecio(id, idcosto, idCliente));
		}
		return preferenciascostos;
	}	

	/**
	 * Metodo que busca la preferencia con el id que entra como parametro.
	 * @param nit - nit de la preferencia a buscar
	 * @return preferencia encontrada
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public PreferenciaPrecio buscarPreferenciaCostoPorId(Long id) throws SQLException, Exception 
	{
		PreferenciaPrecio preferencia = null;

		String sql = "SELECT * FROM PREFERENCIAS_PRECIOS WHERE IDPREFERENCIA =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id2 = rs.getLong("IDPREFERENCIA");
			Long idcosto = rs.getLong("PRECIO");
			Long idCliente = rs.getLong("IDCLIENTE");
			preferencia = new PreferenciaPrecio(id2, idcosto, idCliente);
		}

		return preferencia;
	}

	/**
	 * Metodo que agrega el producto que entra como parametro a la base de datos.
	 * @param preferencia - el producto a agregar. producto !=  null
	 * <b> post: </b> se ha agregado la producto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPreferencia(PreferenciaPrecio preferencia) throws SQLException, Exception {

		String sql = "INSERT INTO PREFERENCIAS_PRECIOS (IDPREFERENCIA, PRECIO, IDCLIENTE) VALUES (";
		sql += preferencia.getIdPreferencia() + ",";
		sql += preferencia.getIdCosto() + ",";
		sql += preferencia.getIdCliente() +")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza el preferencia que entra como parametro en la base de datos.
	 * @param preferencia - preferencia a actualizar. preferencia !=  null
	 * <b> post: </b> se ha actualizado la preferencia en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el producto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updatePreferencia(PreferenciaPrecio preferencia) throws SQLException, Exception {

		String sql = "UPDATE PREFERENCIAS_PRECIOS SET ";
		sql += "PRECIO=" + preferencia.getIdCosto()+ ",";
		sql += "IDCLIENTE=" + preferencia.getIdCliente();
		sql += " WHERE IDPREFERENCIA = " + preferencia.getIdPreferencia();
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la preferencia que entra como parametro en la base de datos.
	 * @param preferencia - la preferencia a borrar. preferencia !=  null
	 * <b> post: </b> se ha borrado la preferencia en la base de datos en la transaction actual. pendiente que el  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la preferencia.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deletePreferenciaCosto(PreferenciaPrecio preferencias) throws SQLException, Exception {

		String sql = "DELETE FROM PREFERENCIAS_PRECIOS";
		sql += " WHERE IDPREFERENCIA = " + preferencias.getIdPreferencia();

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
