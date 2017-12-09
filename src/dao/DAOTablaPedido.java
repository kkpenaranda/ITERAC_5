package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vos.ConsultaPedidos;
import vos.ConsultaPedidosPorProducto;
import vos.Pedido;
import vos.PreferenciaPrecio;
import vos.ProductosPedidos;
import vos.RentabilidadRestaurante;
import vos.RestaurantesFrecuentadosDia;
import vos.ServirPedido;

public class DAOTablaPedido {
	/**
	 * Arraylist de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> pedidos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;	

	/**
	 * Metodo constructor que crea DAOTablapedido
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaPedido() {
		pedidos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan en el arreglo de pedidos
	 * <b>post: </b> Todos los recurso del arreglo de pedidos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : pedidos){
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
	 * Metodo que, usando la conexion a la base de datos, saca todos los pedidos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM pedido;
	 * @return Arraylist con los pedidos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Pedido> darPedidos() throws SQLException, Exception {
		ArrayList<Pedido> pedido = new ArrayList<Pedido>();

		String sql = "SELECT * FROM PEDIDO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long idPedido = rs.getLong("NUMERO_ORDEN");
			Integer totalAPagar = rs.getInt("TOTAL_PAGAR");
			Long idCliente = rs.getLong("IDCLIENTE");
			boolean servido = rs.getBoolean("SERVIDO");
			Date fechaServicio = rs.getDate("FECHA_SERVICIO");
			Long idProductoVenta = rs.getLong("ID_PRODUCTO_VENTA");
			Integer dia = rs.getInt("DIA");
			boolean personalizado = rs.getBoolean("PERSONALIZADO");
			Integer cantidadSolicitada= rs.getInt("CANTIDAD_SOLICITADA");
			Long idMesa =  rs.getLong("ID_MESA");

			pedido.add(new Pedido(idPedido, totalAPagar, idCliente, servido, fechaServicio, idProductoVenta, dia, personalizado, cantidadSolicitada, idMesa));
		}
		return pedido;
	}




	/**
	 * Metodo que busca el pedido con el id que entra como parametro.
	 * @param numeroOrden - Id del pedido a buscar
	 * @return pedido encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Pedido buscarPedidoPorId(Long numeroOrden) throws SQLException, Exception 
	{
		Pedido pedido = null;

		String sql = "SELECT * FROM PEDIDO WHERE NUMERO_ORDEN =" + numeroOrden;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long idPedido2 = rs.getLong("NUMERO_ORDEN");
			Integer totalAPagar = rs.getInt("TOTAL_PAGAR");
			Long idCliente = rs.getLong("IDCLIENTE");
			boolean servido = rs.getBoolean("SERVIDO");
			Date fechaServicio = rs.getDate("FECHA_SERVICIO");
			Long idProductoVenta = rs.getLong("ID_PRODUCTO_VENTA");
			Integer dia = rs.getInt("DIA");
			boolean personalizado = rs.getBoolean("PERSONALIZADO");
			Integer cantidadSolicitada= rs.getInt("CANTIDAD_SOLICITADA");
			Long idMesa =  rs.getLong("ID_MESA");

			pedido = new Pedido(idPedido2, totalAPagar, idCliente, servido, fechaServicio, idProductoVenta,dia,personalizado, cantidadSolicitada, idMesa);
		}

		return pedido;
	}


	/**
	 * Metodo que agrega el pedido que entra como parametro a la base de datos.
	 * @param pedido - la pedido a agregar. pedido !=  null
	 * <b> post: </b> se ha agregado la pedido a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que la pedido baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar la pedido a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addPedidoPorCliente(Long numOrden, Integer totalAPagar, Long idCliente, Long idProdVenta, boolean personalizado, Integer cantSolic, Long idMesa) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO (NUMERO_ORDEN, TOTAL_PAGAR, IDCLIENTE, SERVIDO, FECHA_SERVICIO, ID_PRODUCTO_VENTA, DIA, PERSONALIZADO, CANTIDAD_SOLICITADA, ID_MESA) VALUES (";
		sql += numOrden + ",";
		sql += totalAPagar+ ",";
		sql += idCliente + ",";
		sql +=  "0,TO_DATE('";
		Date fechaActual=new Date(System.currentTimeMillis());
		int dia=fechaActual.getDay(); 
		sql += fechaActual+ "','YYYY-MM-DD hh24:mi:ss'),";
		sql += idProdVenta+ ",";
		sql += dia +",";

		if(personalizado==false){
			sql +=  "0,";
		}
		else{
			sql +=  "1,";
		}
		sql += cantSolic +",";
		sql +=  idMesa + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void servirPedido(Long numeroOrden) throws SQLException, Exception {

		Date fechaActual=new Date(System.currentTimeMillis());
		int dia=fechaActual.getDay(); 
		String s=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(fechaActual);

		String sql = "UPDATE PEDIDO SET ";
		sql += "SERVIDO = " + "1,";
		sql += "FECHA_SERVICIO= TO_DATE('"+ fechaActual +"','YYYY-MM-DD hh24:mi:ss'),";
		sql += "DIA="+ (dia);
		sql += " WHERE NUMERO_ORDEN = " + numeroOrden;

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		System.out.println("FIN, ya lo servii :)!!!");
	}	


	public void servirPedido(Pedido servirPedido) throws SQLException, Exception {

		Date fechaActual=new Date(System.currentTimeMillis());
		int dia=fechaActual.getDay(); 
		String s=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(fechaActual);

		String sql = "UPDATE PEDIDO SET ";
		if(servirPedido.isServido()){sql += "SERVIDO = " + " 1, ";};
		sql += "FECHA_SERVICIO= TO_DATE('"+ fechaActual +"','YYYY-MM-DD hh24:mi:ss'),";
		sql += "DIA="+ (dia);
		sql += " WHERE NUMERO_ORDEN = " + servirPedido.getNumeroOrden();
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza el pedido que entra como parametro en la base de datos.
	 * @param pedido - la pedido a actualizar. pedido !=  null
	 * <b> post: </b> se ha actualizado la pedido en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el pedido.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	//	public void updatePedido(Pedido pedido) throws SQLException, Exception {
	//
	//		String sql = "UPDATE PEDIDO SET ";
	//		sql += "TOTAL_PAGAR=" +pedido.getTotalAPagar();
	//		sql += " WHERE NUMERO_ORDEN = " + pedido.getNumeroOrden();
	//		sql += "IDCLIENTE" + pedido.getIdCliente();
	//		sql += "SERVIDO" + pedido.isServido();
	//		sql += "FECHA_SERVICIO"+ pedido.getFechaServicio();
	//		sql += "ID_PRODUCTO_VENTA"+ pedido.getIdProductoVenta();
	//
	//
	//		PreparedStatement prepStmt = conn.prepareStatement(sql);
	//		pedidos.add(prepStmt);
	//		prepStmt.executeQuery();
	//	}

	/**
	 * Metodo que elimina el pedido que entra como parametro en la base de datos.
	 * @param pedido - la pedido a borrar. pedido !=  null
	 * <b> post: </b> se ha borrado la pedido en la base de datos en la transaction actual. pendiente que el  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar la pedido.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void cancelarPedido(Long idPedido) throws SQLException, Exception {

		String sql = "DELETE FROM PEDIDO";
		sql += " WHERE NUMERO_ORDEN = " + idPedido + " ";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	public ArrayList<Long> buscarPedidosMesa(Long numeroMesa) throws SQLException, Exception 
	{
		ArrayList<Long> pedido = new ArrayList<>();

		String sql = "SELECT NUMERO_ORDEN FROM PEDIDO WHERE ID_MESA=" + numeroMesa;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long idPedido2 = rs.getLong("NUMERO_ORDEN");

			pedido.add(idPedido2);
		}

		return pedido;
	}
	
	public Long confirmarMesaPedido(Long idPedido) throws SQLException, Exception 
	{
		Long mesa = null;

		String sql = "SELECT ID_MESA FROM PEDIDO WHERE NUMERO_ORDEN=" + idPedido;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			mesa = rs.getLong("ID_MESA");
		}

		return mesa;
	}
	
	public ArrayList<ConsultaPedidos> consultarPedidos() throws SQLException, Exception 
	{
		ArrayList<ConsultaPedidos> pedido = new ArrayList<>();

		String sql = "SELECT ID_RESTAURANTE, SUM(TOTAL_PAGAR) AS TOTALVENTA, SUM(CANTIDAD_SOLICITADA) AS TOTALPRODUCTOS FROM PEDIDO JOIN TIENEN_PEDIDOS ON ID_PEDIDO = NUMERO_ORDEN GROUP BY ID_RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long idRest= rs.getLong("ID_RESTAURANTE");
			Double sum1= rs.getDouble("TOTALVENTA");
			Double sum2= rs.getDouble("TOTALPRODUCTOS");

			pedido.add(new ConsultaPedidos(idRest, sum1, sum2));
		}

		return pedido;
	}
	
	
	public ArrayList<ConsultaPedidosPorProducto> consultarPedidosPorProducto() throws SQLException, Exception 
	{
		ArrayList<ConsultaPedidosPorProducto> pedido = new ArrayList<>();

		String sql = "SELECT ID_PRODUCTO_VENTA, SUM(TOTAL_PAGAR) AS TOTALVENTA, SUM(CANTIDAD_SOLICITADA) AS TOTALPRODUCTOS FROM PEDIDO JOIN TIENEN_PEDIDOS ON ID_PEDIDO = NUMERO_ORDEN GROUP BY ID_PRODUCTO_VENTA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id= rs.getLong("ID_PRODUCTO_VENTA");
			Double sum1= rs.getDouble("TOTALVENTA");
			Double sum2= rs.getDouble("TOTALPRODUCTOS");

			pedido.add(new ConsultaPedidosPorProducto(id, sum1, sum2));
		}

		return pedido;
	}
	
	public void addPedidoPorCliente(Pedido pedido) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO (NUMERO_ORDEN, TOTAL_PAGAR, IDCLIENTE, SERVIDO, FECHA_SERVICIO, ID_PRODUCTO_VENTA, DIA, PERSONALIZADO, CANTIDAD_SOLICITADA, ID_MESA) VALUES (";
		sql += pedido.getNumeroOrden() + ",";
		sql += pedido.getTotalAPagar()+ ",";
		sql += pedido.getIdCliente() + ",";
		sql +=  "0,TO_DATE('";
		Date fechaActual=new Date(System.currentTimeMillis());
		int dia=fechaActual.getDay(); 
		sql += fechaActual+ "','YYYY-MM-DD hh24:mi:ss'),";
		sql += pedido.getIdProductoVenta()+ ",";
		sql += dia +",";

		if(pedido.isPersonalizado()){
			sql +=  "1,";
		}
		else{
			sql +=  "0,";
		}
		sql += pedido.getCantidadSolicitada() +",";
		sql +=  pedido.getMesa() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	public ArrayList<ProductosPedidos> buscarProductosSegunCantidadPedidos() throws SQLException, Exception 
	{
		ArrayList<ProductosPedidos> pedido = new ArrayList<>();

		String sql = "SELECT DIA, ID_PRODUCTO_MAS_VENDIDO, ID_PRODUCTO_MENOS_VENDIDO FROM (SELECT DIA, ID AS ID_PRODUCTO_MAS_VENDIDO, MAXIMO AS CANTIDAD_MAS_SOLICITADA FROM ((SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MAXIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA) JOIN (SELECT DISTINCT DIA AS DIA2, MAX(MAXIMO)AS MAXT FROM(SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MAXIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA)T1 GROUP BY DIA ORDER BY DIA) ON DIA = DIA2 AND MAXIMO = MAXT))T11 INNER JOIN(SELECT DIA AS D, ID AS ID_PRODUCTO_MENOS_VENDIDO, MINIMO AS CANTIDAD_SOLICITADA_MENOR FROM ((SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MINIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA) JOIN (SELECT DISTINCT DIA AS DIA2, MIN(MINIMO)AS MINT FROM(SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MINIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA)T1 GROUP BY DIA ORDER BY DIA) ON DIA = DIA2 AND MINIMO = MINT))T12 ON T11.DIA=T12.D";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		
		

		while(rs.next()) {
			Integer dia = rs.getInt("DIA");
			Long id1= rs.getLong("ID_PRODUCTO_MAS_VENDIDO");
			Long id2= rs.getLong("ID_PRODUCTO_MENOS_VENDIDO");

			pedido.add(new ProductosPedidos(dia, id1, id2));
			
		}

		return pedido;
	}
	
	
	public ArrayList<RestaurantesFrecuentadosDia> buscarRestaurantesSolicitados() throws SQLException, Exception 
	{
		ArrayList<RestaurantesFrecuentadosDia> restaurante = new ArrayList<>();

		String sql = "SELECT D AS DIA, ID_RESTAURANTE AS MAS_SOLICITADO, MENOR AS MENOS_SOLICITADO FROM ((SELECT NUEVO AS D, ID_RESTAURANTE FROM TIENEN_PEDIDOS INNER JOIN (SELECT NUEVO, ID_PRODUCTO, NUMERO_ORDEN FROM PEDIDO INNER JOIN (SELECT DIA AS NUEVO, ID AS ID_PRODUCTO, MAXIMO AS CANTIDAD_SOLICITADO FROM ((SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MAXIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA) JOIN (SELECT DISTINCT DIA AS DIA2, MAX(MAXIMO)AS MAXT FROM(SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MAXIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA)T1 GROUP BY DIA ORDER BY DIA) ON DIA = DIA2 AND MAXIMO = MAXT))T1 ON PEDIDO.ID_PRODUCTO_VENTA= T1.ID_PRODUCTO AND PEDIDO.DIA= T1.NUEVO ORDER BY NUMERO_ORDEN)T2 ON TIENEN_PEDIDOS.ID_PEDIDO= T2.NUMERO_ORDEN GROUP BY ID_RESTAURANTE, NUEVO ORDER BY NUEVO)T21 INNER JOIN (SELECT NUEVO, ID_RESTAURANTE AS MENOR FROM TIENEN_PEDIDOS INNER JOIN (SELECT NUEVO, ID_PRODUCTO, NUMERO_ORDEN FROM PEDIDO INNER JOIN (SELECT DIA AS NUEVO, ID AS ID_PRODUCTO, MINIMO AS CANTIDAD_SOLICITADO FROM ((SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MINIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA) JOIN (SELECT DISTINCT DIA AS DIA2, MIN(MINIMO)AS MINT FROM(SELECT DIA, ID_PRODUCTO_VENTA AS ID, COUNT(ID_PRODUCTO_VENTA) AS MINIMO FROM PEDIDO GROUP BY DIA, ID_PRODUCTO_VENTA ORDER BY DIA)T1 GROUP BY DIA ORDER BY DIA) ON DIA = DIA2 AND MINIMO = MINT))T1 ON PEDIDO.ID_PRODUCTO_VENTA= T1.ID_PRODUCTO AND PEDIDO.DIA= T1.NUEVO ORDER BY NUMERO_ORDEN)T2 ON TIENEN_PEDIDOS.ID_PEDIDO= T2.NUMERO_ORDEN GROUP BY ID_RESTAURANTE, NUEVO ORDER BY NUEVO)T22 ON T21.D = T22.NUEVO)";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();		

		while(rs.next()) {
			Integer dia = rs.getInt("DIA");
			Long id1= rs.getLong("MAS_SOLICITADO");
			Long id2= rs.getLong("MENOS_SOLICITADO");

			restaurante.add(new RestaurantesFrecuentadosDia(dia, id1, id2));			
		}

		return restaurante;
	}
	
	public ArrayList<RentabilidadRestaurante> buscarRentabilidadPorCategoria(Long idCategoria, String fecha1, String fecha2) throws SQLException, Exception 
	{
		ArrayList<RentabilidadRestaurante> restabilidad = new ArrayList<>();

		String sql = "SELECT ID_RESTAURANTE, SUM(CANTIDAD_SOLICITADA) AS TOTAL_VENDIDOS, SUM(TOTAL_PAGAR) AS TOTAL_PAGAR FROM PEDIDO INNER JOIN TIENEN_PEDIDOS ON NUMERO_ORDEN= ID_PEDIDO WHERE ID_PRODUCTO_VENTA IN (SELECT ID_PRODUCTO_VENTA FROM PRODUCTO WHERE ID_CATEGORIA=" + idCategoria + ") AND FECHA_SERVICIO BETWEEN '" + fecha1 + "' AND '" + fecha2 + "' GROUP BY ID_RESTAURANTE ORDER BY ID_RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()) {
			String id= rs.getString("ID_RESTAURANTE");
			Integer totalPedidos = rs.getInt("TOTAL_VENDIDOS");
			Double totalAPagar = rs.getDouble("TOTAL_PAGAR");			

			restabilidad.add(new RentabilidadRestaurante(id, totalPedidos, totalAPagar));
		}

		return restabilidad;
	}
	
	public ArrayList<RentabilidadRestaurante> buscarRentabilidadPorZona(Long idZona, String fecha1, String fecha2) throws SQLException, Exception 
	{
		ArrayList<RentabilidadRestaurante> restabilidad = new ArrayList<>();

		String sql = "SELECT ID_RESTAURANTE, SUM(CANTIDAD_SOLICITADA) AS TOTAL_VENDIDOS, SUM(TOTAL_PAGAR) AS TOTAL_PAGAR FROM PEDIDO INNER JOIN TIENEN_PEDIDOS ON NUMERO_ORDEN= ID_PEDIDO WHERE ID_MESA IN (SELECT NUMERO_MESA FROM MESA WHERE ID_ZONA=" + idZona + ") AND FECHA_SERVICIO BETWEEN '" + fecha1 + "' AND '" + fecha2 + "' GROUP BY ID_RESTAURANTE ORDER BY ID_RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()) {
			String id= rs.getString("ID_RESTAURANTE");
			Integer totalPedidos = rs.getInt("TOTAL_VENDIDOS");
			Double totalAPagar = rs.getDouble("TOTAL_PAGAR");			

			restabilidad.add(new RentabilidadRestaurante(id, totalPedidos, totalAPagar));
		}

		return restabilidad;
	}
	
	public ArrayList<RentabilidadRestaurante> buscarRentabilidadPorProducto(Long idProducto, String fecha1, String fecha2) throws SQLException, Exception 
	{
		ArrayList<RentabilidadRestaurante> restabilidad = new ArrayList<>();

		String sql = "SELECT ID_RESTAURANTE, SUM(CANTIDAD_SOLICITADA) AS TOTAL_VENDIDOS, SUM(TOTAL_PAGAR) AS TOTAL_PAGAR FROM PEDIDO INNER JOIN TIENEN_PEDIDOS ON NUMERO_ORDEN= ID_PEDIDO WHERE ID_PRODUCTO_VENTA =" + idProducto + " AND FECHA_SERVICIO BETWEEN '" + fecha1 + "' AND '" + fecha2 + "' GROUP BY ID_RESTAURANTE ORDER BY ID_RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		pedidos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()) {
			String id= rs.getString("ID_RESTAURANTE");
			Integer totalPedidos = rs.getInt("TOTAL_VENDIDOS");
			Double totalAPagar = rs.getDouble("TOTAL_PAGAR");			

			restabilidad.add(new RentabilidadRestaurante(id, totalPedidos, totalAPagar));
		}

		return restabilidad;
	}
}
