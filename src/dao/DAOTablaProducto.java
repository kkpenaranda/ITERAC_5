package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Producto;
import vos.Producto;

public class DAOTablaProducto {


	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOProducto
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaProducto() {
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
	 * Metodo que, usando la conexion a la base de datos, saca todas las productos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM producto;
	 * @return Arraylist con las productos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> darProductos() throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		int n=0;
		while (rs.next() && n<100) {
			Long id = rs.getLong("ID_PRODUCTO");
			boolean personalizable = rs.getBoolean("PERSONALIZABLE");
			String nombre = rs.getString("NOMBRE");
			String traduccion = rs.getString("TRADUCCION");
			Integer tiempo = rs.getInt("TIEMPO_PREPARACION");
			String descripcion = rs.getString("DESCRIPCION");
			Long idCategoria = rs.getLong("ID_CATEGORIA");
			Long idTipoComida = rs.getLong("ID_TIPO");
			Long idProdVenta = rs.getLong("ID_PRODUCTO_VENTA");
			productos.add(new Producto(id, personalizable, nombre, traduccion, tiempo, descripcion, idCategoria,idTipoComida, idProdVenta));
			
			n++;
		}
		return productos;
	}

	/**
	 * Metodo que busca el producto con el nombre que entra como parametro.
	 * @param nombre - nombre del producto a buscar
	 * @return producto encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Producto buscarProductoPorNombre(String nombre) throws SQLException, Exception 
	{
		Producto producto = null;

		String sql = "SELECT * FROM PRODUCTO WHERE NOMBRE ='" + nombre + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			boolean personalizable = rs.getBoolean("PERSONALIZABLE");
			String nombre2 = rs.getString("NOMBRE");
			String traduccion = rs.getString("TRADUCCION");
			Integer tiempo = rs.getInt("TIEMPO_PREPARACION");
			String descripcion = rs.getString("DESCRIPCION");
			Long idCategoria = rs.getLong("ID_CATEGORIA");
			Long idTipoComida = rs.getLong("ID_TIPO");
			Long idProdVenta = rs.getLong("ID_PRODUCTO_VENTA");
			producto = new Producto(id, personalizable, nombre2, traduccion, tiempo, descripcion, idCategoria,idTipoComida, idProdVenta);
		}

		return producto;
	}	

	/**
	 * Metodo que busca el producto con el  nit que entra como parametro.
	 * @param nit - nit del producto a buscar
	 * @return producto encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Producto buscarProductoPorId(Long id) throws SQLException, Exception 
	{
		Producto producto = null;

		String sql = "SELECT * FROM PRODUCTO WHERE ID_PRODUCTO =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id2 = rs.getLong("ID_PRODUCTO");
			boolean personalizable = rs.getBoolean("PERSONALIZABLE");
			String nombre = rs.getString("NOMBRE");
			String traduccion = rs.getString("TRADUCCION");
			Integer tiempo = rs.getInt("TIEMPO_PREPARACION");
			String descripcion = rs.getString("DESCRIPCION");
			Long idCategoria = rs.getLong("ID_CATEGORIA");
			Long idTipoComida = rs.getLong("ID_TIPO");
			Long idProdVenta = rs.getLong("ID_PRODUCTO_VENTA");
			producto = new Producto(id2, personalizable, nombre, traduccion, tiempo, descripcion, idCategoria, idTipoComida,idProdVenta);
		}

		return producto;
	}

	/**
	 * Metodo que agrega el producto que entra como parametro a la base de datos.
	 * @param producto - el producto a agregar. producto !=  null
	 * <b> post: </b> se ha agregado la producto a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el producto baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el producto a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	

	/**
	 * Metodo que actualiza el producto que entra como parametro en la base de datos.
	 * @param producto - el producto a actualizar. producto !=  null
	 * <b> post: </b> se ha actualizado el producto en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el producto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	

	/**
	 * Metodo que elimina el producto que entra como parametro en la base de datos.
	 * @param producto - el producto a borrar. producto !=  null
	 * <b> post: </b> se ha borrado el producto en la base de datos en la transaction actual. pendiente que el  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el producto.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	

	public Producto buscarProductoEncontradoMayorCantidadMenus() throws SQLException, Exception 
	{
		Producto producto = null;

		String sql = "(SELECT MAXIMO, ID FROM (SELECT  MAX(S) AS MAXIMO FROM (SELECT ID_PRODUCTO AS ID, COUNT(ID_PRODUCTO) AS S FROM TIENE_PRODUCTO GROUP BY ID_PRODUCTO))T1 INNER JOIN (SELECT ID_PRODUCTO AS ID, COUNT(ID_PRODUCTO) AS S FROM TIENE_PRODUCTO GROUP BY ID_PRODUCTO)T2 ON T1.MAXIMO= T2.S)";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id = rs.getLong("ID");
			producto = buscarProductoPorId(id);
		}

		return producto;
	}

	public ArrayList<Producto> buscarPorFiltrosLosProductosServidosRotondAndes(Long idRestaurante) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();


		String sql = "SELECT * FROM PRODUCTOVENTA PV, PRODUCTO P WHERE PV.NIT=" + idRestaurante + " AND P.ID_PRODUCTO_VENTA= PV.ID_PRODUCTOVENTA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}

	public ArrayList<Producto> buscarPorFiltroCategoria(Long idCategoria) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTO P WHERE P.ID_CATEGORIA=" + idCategoria;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}

	public ArrayList<Producto> buscarPorFiltroRangoPrecio(Double minimo, Double maximo) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTOVENTA PV, PRODUCTO P WHERE PV.ID_PRODUCTOVENTA=P.ID_PRODUCTO_VENTA AND PV.PRECIO BETWEEN " + minimo + " AND " + maximo + " ORDER BY PRECIO ASC";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}

	public ArrayList<Producto> buscarPorZona(Long idZona) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM PRODUCTOVENTA PV WHERE PV.NIT IN (SELECT NIT FROM RESTAURANTE R WHERE R.ID_ZONA=" + idZona+")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}

	public Producto buscarProductoPorIdProductoVenta(Long idProductoVenta) throws SQLException, Exception 
	{
		Producto producto = null;

		String sql = "SELECT * FROM PRODUCTO WHERE ID_PRODUCTO_VENTA = '" + idProductoVenta + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long id2 = rs.getLong("ID_PRODUCTO");
			boolean personalizable = rs.getBoolean("PERSONALIZABLE");
			String nombre = rs.getString("NOMBRE");
			String traduccion = rs.getString("TRADUCCION");
			Integer tiempo = rs.getInt("TIEMPO_PREPARACION");
			String descripcion = rs.getString("DESCRIPCION");
			Long idCategoria = rs.getLong("ID_CATEGORIA");
			Long idTipoComida = rs.getLong("ID_TIPO");
			Long idProdVenta = rs.getLong("ID_PRODUCTO_VENTA");
			producto = new Producto(id2, personalizable, nombre, traduccion, tiempo, descripcion, idCategoria, idTipoComida,idProdVenta);
		}

		return producto;
	}

	public ArrayList<Producto> buscarPorProductosConsumidosPorCliente(Long idCliente) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();
		String sql = "SELECT * from PEDIDO PE , PRODUCTO PR WHERE PE.ID_PRODUCTO_VENTA = PR.ID_PRODUCTO_VENTA AND PE.SERVIDO=1 AND PE.IDCLIENTE= "+idCliente ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}
	public ArrayList<Producto> buscarPorProductosMenuConsumidos(Long idCliente) throws SQLException, Exception{
		ArrayList<Producto> productos = new ArrayList<Producto>();
		String sql = "SELECT * FROM PEDIDO PE, MENU M, TIENE_PRODUCTO TP, PRODUCTO P WHERE PE.ID_PRODUCTO_VENTA = M.ID_PRODUCTOVENTA AND M.ID_MENU = TP.ID_MENU AND P.ID_PRODUCTO=TP.ID_PRODUCTO AND PE.SERVIDO=1 AND PE.IDCLIENTE= "+idCliente ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}
	public ArrayList<Producto> buscarPorProductosMesaConsumidos(Long idCliente) throws SQLException, Exception{

		ArrayList<Producto> productos = new ArrayList<Producto>();
		String sql = "SELECT * FROM PEDIDO PE , PRODUCTO PR  WHERE PE.ID_PRODUCTO_VENTA = PR.ID_PRODUCTO_VENTA AND PE.SERVIDO=1 AND PE.ID_MESA IS NOT NULL AND PE.IDCLIENTE= "+idCliente ;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("ID_PRODUCTO");
			productos.add(buscarProductoPorId(id));
		}
		System.out.println(productos.get(0));
		return productos;
	}
}
