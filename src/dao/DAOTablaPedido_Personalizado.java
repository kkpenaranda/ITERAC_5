package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Pedido_Personalizado;


public class DAOTablaPedido_Personalizado {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablapedidoPers
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPedido_Personalizado() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de pedidoPerss
	 * <b>post: </b> Todos los recurso del arreglo de pedidoPerss han sido cerrados
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los pedidoPerss de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM pedidoPers;
	 * @return Arraylist con los pedidoPerss de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Pedido_Personalizado> darPedidos_Personalizados() throws SQLException, Exception {
		ArrayList<Pedido_Personalizado> pedidosPers = new ArrayList<Pedido_Personalizado>();

		String sql = "SELECT * FROM PEDIDO_PERSONALIZADO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long numeroOrden = rs.getLong("ID_PEDIDO");
			Long idMenu = rs.getLong("ID_MENU");
			Long idProductoCambiado= rs.getLong("ID_PRODUCTO_ORIGINAL");
			Long idProductoNuevo =  rs.getLong("ID_PRODUCTO_NUEVO");

			pedidosPers.add(new Pedido_Personalizado(numeroOrden,idMenu,idProductoCambiado,idProductoNuevo));
		}
		return pedidosPers;
	}


	
	
	/**
	 * Metodo que busca el pedidoPers con el id que entra como parametro.
	 * @param numeroOrden - Id del pedidoPers a buscar
	 * @return pedidoPers encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Pedido_Personalizado buscarPedido_PersonalizadoPorId(Long numeroOrden) throws SQLException, Exception 
	{
		Pedido_Personalizado pedidoPers = null;

		String sql = "SELECT * FROM PEDIDO_PERSONALIZADO WHERE NUMERO_ORDEN =" + numeroOrden;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long numeroOrden2 = rs.getLong("ID_PEDIDO");
			Long idMenu = rs.getLong("ID_MENU");
			Long idProductoCambiado= rs.getLong("ID_PRODUCTO_ORIGINAL");
			Long idProductoNuevo =  rs.getLong("ID_PRODUCTO_NUEVO");

			pedidoPers = new Pedido_Personalizado(numeroOrden2,idMenu,idProductoCambiado,idProductoNuevo);
		}

		return pedidoPers;
	}
	

	/**
	 * Metodo que agrega el pedidoPers que entra como parametro a la base de datos.
	 * @param pedidoPers - la pedidoPers a agregar. pedidoPers !=  null
	 * <b> post: </b> se ha agregado la pedidoPers a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la pedidoPers baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la pedidoPers a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPedido_PersonalizadoPorCliente(Long numOrden, Long idMenu, Long pc, Long pn) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO_PERSONALIZADO (ID_PEDIDO, ID_MENU, ID_PRODUCTO_ORIGINAL, ID_PRODUCTO_NUEVO) VALUES (";
		sql += numOrden + ",";
		sql += idMenu+ ",";
		sql += pc +",";
		sql +=  pn + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

//	public void updatePedido_Personalizado(Pedido_Personalizado pedidoPers) throws SQLException, Exception {
//
//		String sql = "UPDATE PEDIDO_PERSONALIZADO SET ";
//		sql += "TOTAL_PAGAR=" +pedidoPers.getTotalAPagar();
//		sql += " WHERE NUMERO_ORDEN = " + pedidoPers.getNumeroOrden();
//		sql += "IDCLIENTE" + pedidoPers.getIdCliente();
//		sql += "SERVIDO" + pedidoPers.isServido();
//		sql += "FECHA_SERVICIO"+ pedidoPers.getFechaServicio();
//		sql += "ID_PRODUCTO_VENTA"+ pedidoPers.getIdProductoVenta();
//
//
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		pedidoPerss.add(prepStmt);
//		prepStmt.executeQuery();
//	}

	/**
	 * Metodo que elimina el pedidoPers que entra como parametro en la base de datos.
	 * @param pedidoPers - la pedidoPers a borrar. pedidoPers !=  null
	 * <b> post: </b> se ha borrado la pedidoPers en la base de datos en la transaction actual. pendiente que el  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedidoPers.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deletePedido_Personalizado(Pedido_Personalizado pedidoPers) throws SQLException, Exception {

		String sql = "DELETE FROM PEDIDO_PERSONALIZADO";
		sql += " WHERE ID_PEDIDO = " + pedidoPers.getNumeroOrden();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	public void cancelarPedido(Long idPedido) throws SQLException, Exception {

		String sql = "DELETE FROM PEDIDO_PERSONALIZADO";
		sql += " WHERE ID_PEDIDO = " + idPedido + " ";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
