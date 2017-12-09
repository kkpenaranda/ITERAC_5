package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import vos.Usuario;

public class DAOTablaUsuario {


	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOusuario
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaUsuario() {
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


	//	
	
	public ArrayList<Usuario> darUsuariosCliente() throws SQLException, Exception {
		ArrayList<Usuario> clientesRegistrados = new ArrayList<Usuario>();
	
		String sql = "SELECT * FROM (SELECT ID_USUARIO, NOMBRE, CORREO, ROL, PRECIO, IDZONA, ID_TIPO FROM USUARIO FULL OUTER JOIN (SELECT * FROM ((PREFERENCIAS_PRECIOS NATURAL JOIN PREFERENCIAS_ZONAS) FULL OUTER JOIN (PREFERENCIAS_TIPO_COMIDA) ON IDCLIENTE= ID_CLIENTE))"
				+ "	ON ID_USUARIO=IDCLIENTE WHERE ROL= 'Cliente') FULL OUTER JOIN PEDIDO ON ID_USUARIO=IDCLIENTE ORDER BY ID_USUARIO, FECHA_SERVICIO";
	
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		System.out.println("holiis");
	
		while (rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			clientesRegistrados.add(new Usuario(identificacion, nombre, correo, rol, "*********"));
		}
		System.out.println(clientesRegistrados.get(0));
		return clientesRegistrados;
	}

	/**
	 * Metodo que, usando la conexion a la base de datos, saca todas las usuarios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM usuario;
	 * @return Arraylist con las usuarios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Usuario> darUsuarios() throws SQLException, Exception {
		ArrayList<Usuario> clientesRegistrados = new ArrayList<Usuario>();

		String sql = "SELECT * FROM USUARIO";

		System.out.println("SACANDO INFO DE USUARIOS...");
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			String contrasenia= "*********";
			clientesRegistrados.add(new Usuario(identificacion, nombre, correo, rol, contrasenia));
		}
		return clientesRegistrados;
	}
	//	

	public ArrayList<Usuario> darUsuariosConsumoRestaurante(Long idRestaurante, String fecha1, String fecha2) throws SQLException, Exception {
		ArrayList<Usuario> clientesRegistrados = new ArrayList<Usuario>();

		String sql = "SELECT DISTINCT ID_USUARIO, NOMBRE, CORREO, ROL FROM USUARIO U, PEDIDO P, TIENEN_PEDIDOS TP"
			       	+ " WHERE P.NUMERO_ORDEN=TP.ID_PEDIDO AND TP.ID_RESTAURANTE="+ idRestaurante+" AND P.SERVIDO= 1"
			       	+ " AND U.ID_USUARIO=P.IDCLIENTE AND P.FECHA_SERVICIO BETWEEN '"+fecha1+"' AND '"+fecha2+"'";
		// TO_DATE('"+ fecha1 +"','YYYY-MM-DD hh24:mi:ss') AND TO_DATE('"+ fecha2 +"','YYYY-MM-DD hh24:mi:ss') 
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		System.out.println("sql "+sql);
		ResultSet rs = prepStmt.executeQuery();
		System.out.println("terminò");

		while (rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			clientesRegistrados.add(new Usuario(identificacion, nombre, correo, rol, "*********"));
		}
		return clientesRegistrados;
	}

	
	public ArrayList<Usuario> darUsuariosNOConsumoRestaurante(Long idRestaurante, String fecha1, String fecha2) throws SQLException, Exception {
		ArrayList<Usuario> clientesRegistrados = new ArrayList<Usuario>();

		String sql = "SELECT distinct ID_USUARIO, NOMBRE, CORREO, ROL FROM USUARIO U, PEDIDO P, TIENEN_PEDIDOS TP"
				+" WHERE U.ID_USUARIO NOT IN (SELECT IDCLIENTE FROM  PEDIDO P, TIENEN_PEDIDOS TP"
				+" WHERE P.NUMERO_ORDEN=TP.ID_PEDIDO AND TP.ID_RESTAURANTE="+idRestaurante+" AND P.SERVIDO=1 AND P.FECHA_SERVICIO "
				+ " BETWEEN '"+fecha1+"' AND '"+fecha2+"')AND U.ROL='Cliente' ORDER BY ID_USUARIO";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		System.out.println("sql "+sql);
		ResultSet rs = prepStmt.executeQuery();
		System.out.println("terminò");

		while (rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			clientesRegistrados.add(new Usuario(identificacion, nombre, correo, rol, "*********"));
		}
		return clientesRegistrados;
	}
	
	public ArrayList<Usuario> darBuenosClientes() throws SQLException, Exception {
		ArrayList<Usuario> buenosClientes = new ArrayList<Usuario>();

		String sql = "SELECT * FROM ((SELECT DISTINCT IDCLIENTE, U.NOMBRE, CORREO, ROL FROM PRODUCTO P, PEDIDO PP, PRODUCTOVENTA PV, USUARIO U WHERE PP.IDCLIENTE = U.ID_USUARIO AND P.ID_PRODUCTO_VENTA = PP.ID_PRODUCTO_VENTA AND PV.ID_PRODUCTOVENTA= P.ID_PRODUCTO_VENTA  AND P.ID_CATEGORIA=2 AND PV.PRECIO > (24590.57 * 1.5)) UNION (SELECT IDCLIENTE, NOMBRE, CORREO, ROL FROM USUARIO INNER JOIN (SELECT * FROM PEDIDO WHERE IDCLIENTE NOT IN(SELECT DISTINCT  IDCLIENTE FROM MENU M, PEDIDO P, USUARIO U WHERE M.ID_PRODUCTOVENTA = P.ID_PRODUCTO_VENTA))T1 ON USUARIO.ID_USUARIO= T1.IDCLIENTE) UNION (SELECT IDCLIENTE, NOMBRE, CORREO, ROL FROM USUARIO U, (SELECT IDCLIENTE,COUNT(SEMANA_DEL_ANIO)AS SEMANAS_VISITAS FROM(SELECT IDCLIENTE, SEMANA_DEL_ANIO FROM (SELECT IDCLIENTE,FECHA_SERVICIO, DIA, to_char(to_date(FECHA_SERVICIO,'DD/MM/YYYY'),'WW') as SEMANA_DEL_ANIO , to_char(to_date(FECHA_SERVICIO,'DD/MM/YYYY'),'W') as SEMANA_DEL_MES, EXTRACT(MONTH FROM FECHA_SERVICIO) AS MES FROM PEDIDO P, USUARIO U WHERE U.ID_USUARIO=P.IDCLIENTE AND SERVIDO=1)T GROUP BY IDCLIENTE, SEMANA_DEL_ANIO ORDER BY IDCLIENTE, SEMANA_DEL_ANIO) GROUP BY IDCLIENTE) NATURAL JOIN (SELECT IDCLIENTE, (to_char(to_date((CURRENT_DATE),'DD/MM/YYYY'),'WW') - to_char(to_date(MIN(FECHA_SERVICIO),'DD/MM/YYYY'),'WW'))+1 AS TOTAL_SEMANAS_DESDE_PV FROM PEDIDO GROUP BY IDCLIENTE) WHERE SEMANAS_VISITAS = TOTAL_SEMANAS_DESDE_PV AND U.ID_USUARIO=IDCLIENTE))";
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long identificacion = rs.getLong("IDCLIENTE");
			String nombre = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			buenosClientes.add(new Usuario(identificacion, nombre, correo, rol, "*********"));
		}
		return buenosClientes;
	}


	/**
	 * Metodo que busca el usuario con el nombre que entra como parametro.
	 * @param nombre - nombre del usuario a buscar
	 * @return usuario encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Usuario buscarUsuarioPorNombre(String nombre) throws SQLException, Exception 
	{
		Usuario usuario = null;

		String sql = "SELECT * FROM USUARIO WHERE NOMBRE ='" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre2 = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			String contrasenia= rs.getString("CONTRASENIA");

			usuario = new Usuario(identificacion, nombre2, correo, rol, contrasenia);
		}

		return usuario;
	}



	/**
	 * Metodo que busca el usuario con el  nit que entra como parametro.
	 * @param id - nit del usuario a buscar
	 * @return usuario encontrado
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public Usuario buscarUsuarioPorId(Long id) throws SQLException, Exception 
	{
		Usuario usuario = null;

		String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Long identificacion = rs.getLong("ID_USUARIO");
			String nombre2 = rs.getString("NOMBRE");
			String correo = rs.getString("CORREO");
			String rol = rs.getString("ROL");
			String contrasenia= rs.getString("CONTRASENIA");

			usuario = new Usuario(identificacion, nombre2, correo, rol, contrasenia);
		}

		return usuario;
	}

	/**
	 * Metodo que agrega el usuario que entra como parametro a la base de datos.
	 * @param usuario - el usuario a agregar. usuario !=  null
	 * <b> post: </b> se ha agregado la usuario a la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que el usuario baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el usuario a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "INSERT INTO USUARIO (ID_USUARIO, NOMBRE, CORREO, ROL, CONTRASENIA) VALUES (";
		sql += usuario.getId() + ",'";
		sql += usuario.getNombre() + "','";
		sql += usuario.getCorreoElectronico() + "','";
		sql += usuario.getRol() + "','";
		sql += usuario.getContrasenia() + "')" ;

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza el usuario que entra como parametro en la base de datos.
	 * @param usuario - el usuario a actualizar. usuario !=  null
	 * <b> post: </b> se ha actualizado el usuario en la base de datos en la transaction actual. pendiente que el master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el usuario.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateUsuario(Usuario usuario) throws SQLException, Exception {

		System.out.println("dto");

		String sql = "UPDATE USUARIO SET ";
		sql += "NOMBRE= '" + usuario.getNombre() + "',";
		sql += "CORREO='" + usuario.getCorreoElectronico() + "',";
		sql += "ROL= '" + usuario.getRol() + "',";
		sql += "CONSTRASENIA='" + usuario.getContrasenia() + "'";
		sql += " WHERE ID_USUARIO = " + usuario.getId();


		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina el usuario que entra como parametro en la base de datos.
	 * @param usuario - el usuario a borrar. usuario !=  null
	 * <b> post: </b> se ha borrado el usuario en la base de datos en la transaction actual. pendiente que el  master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el usuario.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteUsuario(Usuario usuario) throws SQLException, Exception {

		String sql = "DELETE FROM USUARIO";
		sql += " WHERE ID_USUARIO = " + usuario.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public String verificarContraseniaUsuario(Long  id) throws SQLException, Exception 
	{

		String sql = "SELECT CONTRASENIA FROM USUARIO WHERE ID_USUARIO =" + id + "";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		String contra= null;
		if(rs.next()){
			contra = rs.getString("CONTRASENIA");
		}


		return contra;
	}

}
