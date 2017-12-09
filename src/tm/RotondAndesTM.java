/**-------------------------------------------------------------------
 * $Id$
 * Universidad de los Andes (BogotÃ¡ - Colombia)
 * Departamento de IngenierÃ­a de Sistemas y ComputaciÃ³n
 *
 * Materia: Sistemas Transaccionales
 * Ejercicio: RotondAndes
 * Autor: Juan Felipe GarcÃ­a - jf.garcia268@uniandes.edu.co
 * -------------------------------------------------------------------
 */
package tm;

import java.io.File;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.corba.se.impl.legacy.connection.SocketFactoryConnectionImpl;
import com.sun.swing.internal.plaf.metal.resources.metal;

import dao.DAOTablaCategoria;
import dao.DAOTablaClienteNoRegistrado;
import dao.DAOTablaClienteRegistrado;
import dao.DAOTablaCondicionTecnica;
import dao.DAOTablaEquivalencias_Ingredientes;
import dao.DAOTablaEquivalencias_Productos;
import dao.DAOTablaEspacio;
import dao.DAOTablaIngrediente;
import dao.DAOTablaIngrediente_Producto;
import dao.DAOTablaIngrediente_Restaurante;
import dao.DAOTablaMenu;
import dao.DAOTablaMesa;
import dao.DAOTablaPedido;
import dao.DAOTablaPedido_Personalizado;
import dao.DAOTablaPreferenciaCosto;
import dao.DAOTablaPreferenciaTipoComida;
import dao.DAOTablaPreferenciaZona;
import dao.DAOTablaProducto;
import dao.DAOTablaReserva;
import dao.DAOTablaRestaurante;
import dao.DAOTablaTiene_Pedidos;
import dao.DAOTablaTiene_Producto;
import dao.DAOTablaTipoComida;
import dao.DAOTablaUsuario;
import dao.DAOTablaProducto_Venta;
import dao.DAOTablaZona;
import dtm.RotondAndesDistributed;
import jms.NonReplyException;
import oracle.net.aso.i;
import vos.CancelarPedido;
import vos.Categoria;
import vos.ClienteNoRegistrado;
import vos.ClienteRegistrado;
import vos.CondicionTecnica;
import vos.ConsultaPedidos;
import vos.ConsultaPedidosPorProducto;
import vos.Equivalencia_Productos;
import vos.Espacio;
import vos.Ingrediente;
import vos.IngresarEquivalenciaIngredientes;
import vos.IngresarEquivalenciaProductos;
import vos.IngresarIngrediente;
import vos.IngresarMenu;
import vos.IngresarPedido;
import vos.IngresarProducto;
import vos.IngresoClientesPorAdministrador;
import vos.IngresoRestaurantesPorAdministrador;
import vos.IngresoZonaPorAdministrador;
import vos.ListaProductos;
import vos.Menu;
import vos.Mesa;
import vos.Pedido;
import vos.PreferenciaPrecio;
import vos.PreferenciaTipoComida;
import vos.PreferenciaZona;
import vos.Producto;
import vos.ProductoMenu;
import vos.Reserva;
import vos.Restaurante;
import vos.RestaurantesFrecuentados;
import vos.RestaurantesFrecuentadosDia;
import vos.ServirPedido;
import vos.SurtirRestaurante;
import vos.TipoComida;
import vos.Usuario;
import vos.Producto_Venta;
import vos.ProductosDetalladosPedidos;
import vos.ProductosPedidos;
import vos.RegistroCliente;
import vos.RegistroRestaurante;
import vos.RentabilidadRestaurante;
import vos.Zona;

/**
 * Transaction Manager de la aplicacion (TM)
 * Fachada en patron singleton de la aplicacion
 * @author vn.gomez_kk.penaranda
 */
public class RotondAndesTM {

	/**
	 * Atributo estÃ¡tico que contiene el path relativo del archivo que tiene los datos de la conexiÃ³n
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estÃ¡tico que contiene el path absoluto del archivo que tiene los datos de la conexiÃ³n
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;
	
	/**
	 * ConexiÃ³n a la base de datos
	 */
	private Connection conn;
	
	private RotondAndesDistributed dtm;


	/**
	 * MÃ©todo constructor de la clase VideoAndesMaster, esta clase modela y contiene cada una de las 
	 * transacciones y la logia de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto VideoAndesMaster, se inicializa el path absoluto de el archivo de conexiÃ³n y se
	 * inicializa los atributos que se usan par la conexiÃ³n a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public RotondAndesTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
		System.out.println("Instancing DTM...");
		dtm = RotondAndesDistributed.getInstance(this);
		System.out.println("Done!");
	}

	/*
	 * MÃ©todo que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexiÃ³n a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MÃ©todo que  retorna la conexiÃ³n a la base de datos
	 * @return Connection - la conexiÃ³n a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexiÃ³n a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	////////////////////////////////////////
	///////Transacciones////////////////////
	////////////////////////////////////////

	
	
	public ListaProductos darProductosLocal() throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try 
		{
			//////TransacciÃ³n
			this.conn = darConexion();
			daoProductos.setConn(conn);
			productos = daoProductos.darProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return new ListaProductos(productos);
	}


	/**
	 * Metodo que modela la transaccion que retorna todos los restaurantes de la base de datos.
	 * @return ListaRestaurantes - objeto que modela  un arreglo de restaurantes. este arreglo contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public List<Restaurante> darRestaurantes() throws Exception {
		List<Restaurante> restaurantes;
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoRestaurantes.setConn(conn);
			restaurantes = daoRestaurantes.darRestaurantes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}
	
	public ListaProductos darProductos() throws Exception {
		ListaProductos remL = darProductosLocal();
		try
		{
			ListaProductos resp = dtm.getRemoteProductos();
			System.out.println(resp.getProductos().size());
			remL.getProductos().addAll(resp.getProductos());
		}
		catch(NonReplyException e)
		{
			
		}
		return remL;
	}

	public List<Producto> darProductos1() throws Exception {
		List<Producto> productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoProductos.setConn(conn);
			productos = daoProductos.darProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}


	public List<Categoria> darCategorias() throws Exception {
		List<Categoria> categorias;
		DAOTablaCategoria daoCategorias = new DAOTablaCategoria();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCategorias.setConn(conn);
			categorias = daoCategorias.darCategorias();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCategorias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return categorias;
	}

	public List<PreferenciaZona> darPreferenciasZonas() throws Exception {
		List<PreferenciaZona> preferenciasZonas;
		DAOTablaPreferenciaZona  daoTablaPreferencias = new DAOTablaPreferenciaZona();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaPreferencias.setConn(conn);
			preferenciasZonas = daoTablaPreferencias.darPreferenciaZona();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferenciasZonas;
	}

	public List<PreferenciaTipoComida> darPreferenciasTipo() throws Exception {
		List<PreferenciaTipoComida> preferenciasTipo;
		DAOTablaPreferenciaTipoComida  daoTablaPreferencias = new DAOTablaPreferenciaTipoComida();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaPreferencias.setConn(conn);
			preferenciasTipo = daoTablaPreferencias.darPreferenciaTipoComida();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferenciasTipo;
	}

	public List<PreferenciaPrecio> darPreferenciasPrecio() throws Exception {
		List<PreferenciaPrecio> preferencias;
		DAOTablaPreferenciaCosto  daoTablaPreferencias = new DAOTablaPreferenciaCosto();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaPreferencias.setConn(conn);
			preferencias = daoTablaPreferencias.darPreferenciaPrecio();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferencias;
	}

	public List<CondicionTecnica> darCondicionesTecnicas() throws Exception {
		List<CondicionTecnica> condiciones;
		DAOTablaCondicionTecnica daoCondiciones = new DAOTablaCondicionTecnica();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCondiciones.setConn(conn);
			condiciones = daoCondiciones.darCondicionesTecnicas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCondiciones.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return condiciones;
	}


	public List<Espacio> darEspacios() throws Exception {
		List<Espacio> espacios;
		DAOTablaEspacio daoEspacios = new DAOTablaEspacio();
		try 
		{
			this.conn = darConexion();
			daoEspacios.setConn(conn);
			espacios = daoEspacios.darEspacios();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspacios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return espacios;
	}

	public List<Pedido> darPedidos() throws Exception {
		List<Pedido> pedidos;
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try 
		{
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			pedidos = daoPedidos.darPedidos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidos;
	}

	public List<Reserva> darReservas() throws Exception {
		List<Reserva> reservas;
		DAOTablaReserva daoReservas = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			daoReservas.setConn(conn);
			reservas = daoReservas.darReservas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReservas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservas;
	}

	public List<TipoComida> darTiposComida() throws Exception {
		List<TipoComida> tipos;
		DAOTablaTipoComida daoTiposComida = new DAOTablaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoTiposComida.setConn(conn);
			tipos = daoTiposComida.darTiposComidas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTiposComida.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return tipos;
	}

	public List<Zona> darZonas() throws Exception {
		List<Zona> zonas;
		DAOTablaZona daoZonas = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			daoZonas.setConn(conn);
			zonas = daoZonas.darZonas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}

	public List<Ingrediente> darIngredientes() throws Exception {
		List<Ingrediente> ingredientes;
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			daoIngredientes.setConn(conn);
			ingredientes = daoIngredientes.darIngredientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingredientes;
	}

	public List<Menu> darMenus() throws Exception {
		List<Menu> menus;
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			daoMenus.setConn(conn);
			menus = daoMenus.darMenus();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menus;
	}

	public List<Usuario> darUsuarios() throws Exception {
		List<Usuario> usuarios;
		DAOTablaUsuario  daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuarios = daoUsuario.darUsuarios();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}

	public List<ClienteRegistrado> darClientesRegistrados() throws Exception {
		List<ClienteRegistrado> clienteRegistrado;
		DAOTablaClienteRegistrado daoClienteRegistrado = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			daoClienteRegistrado.setConn(conn);
			clienteRegistrado = daoClienteRegistrado.darClientesRegistrados();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClienteRegistrado.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clienteRegistrado;
	}

	public List<ClienteNoRegistrado> darClientesNoRegistrados() throws Exception {
		List<ClienteNoRegistrado> clientesNoRegistrados;
		DAOTablaClienteNoRegistrado daoClientes = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			daoClientes.setConn(conn);
			clientesNoRegistrados = daoClientes.darClientesNoRegistrados();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientesNoRegistrados;
	}

	/**
	 * Metodo que modela la transaccion que busca el restaurante en la base de datos con el nombre entra como parametro.
	 * @param name - Nombre del restaurante a buscar. name != null
	 * @return Restaurante - restaurante que  contiene el resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Restaurante buscarRestaurantePorNombre(String nombre) throws Exception {
		Restaurante restaurante;
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			conn.setAutoCommit(false);
			restaurante = daoRestaurantes.buscarRestaurantePorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurante;
	}

	public PreferenciaZona buscarPreferenciaZonaPorId(Long id) throws Exception {
		PreferenciaZona preferencia;
		DAOTablaPreferenciaZona daoPreferencias = new DAOTablaPreferenciaZona();
		try 
		{
			this.conn = darConexion();
			daoPreferencias.setConn(conn);
			preferencia = daoPreferencias.buscarPreferenciaZonaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferencia;
	}

	public PreferenciaTipoComida buscarPreferenciaTipoPorId(Long id) throws Exception {
		PreferenciaTipoComida preferencia;
		DAOTablaPreferenciaTipoComida daoPreferencias = new DAOTablaPreferenciaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoPreferencias.setConn(conn);
			preferencia = daoPreferencias.buscarPreferenciaTipoComidaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferencia;
	}

	public PreferenciaPrecio buscarPreferenciaPorPrecio(Long id) throws Exception {
		PreferenciaPrecio preferencia;
		DAOTablaPreferenciaCosto daoPreferencias = new DAOTablaPreferenciaCosto();
		try 
		{
			this.conn = darConexion();
			daoPreferencias.setConn(conn);
			preferencia = daoPreferencias.buscarPreferenciaCostoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return preferencia;
	}

	public Menu buscarMenuPorNombre(String nombre) throws Exception {
		Menu menu;
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			daoMenus.setConn(conn);
			menu = daoMenus.buscarMenuPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menu;
	}

	public Ingrediente buscarIngredientePorNombre(String nombre) throws Exception {
		Ingrediente ingrediente;
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			daoIngredientes.setConn(conn);
			ingrediente = daoIngredientes.buscarIngredientePorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingrediente;
	}

	public Producto buscarProductoPorNombre(String nombre) throws Exception {
		Producto productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProductos.setConn(conn);
			productos = daoProductos.buscarProductoPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public Categoria buscarCategoriaPorNombre(String nombre) throws Exception {
		Categoria categoria;
		DAOTablaCategoria daoCategoria = new DAOTablaCategoria();
		try 
		{
			this.conn = darConexion();
			daoCategoria.setConn(conn);
			categoria = daoCategoria.buscarCategoriaPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCategoria.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return categoria;
	}


	public Zona buscarZonaPorNombre(String nombre) throws Exception {
		Zona zona;
		DAOTablaZona daoZona = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			daoZona.setConn(conn);
			zona = daoZona.buscarZonaPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zona;
	}

	public Usuario buscarUsuarioPorNombre(String nombre) throws Exception {
		Usuario usuario;
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuario = daoUsuario.buscarUsuarioPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuario;
	}

	public ClienteRegistrado buscarClienteRegistradoPorNombre(String nombre) throws Exception {
		ClienteRegistrado cliente;
		DAOTablaClienteRegistrado daoCliente = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			cliente = daoCliente.buscarClienteRegistradoPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cliente;
	}


	public ClienteNoRegistrado buscarClienteNoRegistradoPorNombre(String nombre) throws Exception {
		ClienteNoRegistrado cliente;
		DAOTablaClienteNoRegistrado daoCliente = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			cliente = daoCliente.buscarClienteNoRegistradoPorNombre(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cliente;
	}



	/**
	 * Metodo que modela la transaccion que busca el restaurante en la base de datos con el nit que entra como parametro.
	 * @param nit - nit del restaurante a buscar. nit != null
	 * @return Restaurante - Resultado de la busqueda
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Restaurante buscarRestaurantePorNit(Long nit) throws Exception {
		Restaurante restaurante;
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			restaurante = daoRestaurantes.buscarRestaurantePorNit(nit);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurante;
	}

	public Menu buscarMenuPorId(Long id) throws Exception {
		Menu menu;
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			daoMenus.setConn(conn);
			menu = daoMenus.buscarMenuPorNit(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menu;
	}

	public Pedido buscarPedidoPorNumeroDeOrden(Long id) throws Exception {
		Pedido pedido;
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try 
		{
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			pedido = daoPedidos.buscarPedidoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedido;
	}


	public Reserva buscarReservaPorId(Long id) throws Exception {
		Reserva reserva;
		DAOTablaReserva daoReservas = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			daoReservas.setConn(conn);
			reserva = daoReservas.buscarReservaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReservas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reserva;
	}

	public TipoComida buscarTipoComidaPorId(Long id) throws Exception {
		TipoComida tipoComida;
		DAOTablaTipoComida daoTablaTipoComida = new DAOTablaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoTablaTipoComida.setConn(conn);
			tipoComida = daoTablaTipoComida.buscarTipoComidaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaTipoComida.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return tipoComida;
	}

	public Zona buscarZonaPorId(Long id) throws Exception {
		Zona zona;
		DAOTablaZona daoTablaZonas = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			daoTablaZonas.setConn(conn);
			zona = daoTablaZonas.buscarZonaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaZonas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zona;
	}

	public Ingrediente buscarIngredientePorNit(Long id) throws Exception {
		Ingrediente ingrediente;
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			daoIngredientes.setConn(conn);
			ingrediente = daoIngredientes.buscarIngredientePorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ingrediente;
	}

	public Producto buscarProductoPorId(Long id) throws Exception {
		Producto producto;
		DAOTablaProducto daoTablaProductos = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoTablaProductos.setConn(conn);
			producto = daoTablaProductos.buscarProductoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return producto;
	}

	public Espacio buscarEspacioPorId(Long id) throws Exception {
		Espacio espacio;
		DAOTablaEspacio daoEspacios = new DAOTablaEspacio();
		try 
		{
			this.conn = darConexion();
			daoEspacios.setConn(conn);
			espacio = daoEspacios.buscarEspacioPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspacios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return espacio;
	}

	public Categoria buscarCategoriaPorId(Long id) throws Exception {
		Categoria categoria;
		DAOTablaCategoria daoTablaCategoria = new DAOTablaCategoria();
		try 
		{
			this.conn = darConexion();
			daoTablaCategoria.setConn(conn);
			categoria = daoTablaCategoria.buscarCategoriaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaCategoria.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return categoria;
	}

	public CondicionTecnica buscarCondicionPorId(Long id) throws Exception {
		CondicionTecnica condicion;
		DAOTablaCondicionTecnica tablaCondicion = new DAOTablaCondicionTecnica();
		try 
		{
			this.conn = darConexion();
			tablaCondicion.setConn(conn);
			condicion = tablaCondicion.buscarCondicionPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tablaCondicion.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return condicion;
	}

	public Usuario buscarUsuarioPorId(Long id) throws Exception {
		Usuario usuario;
		DAOTablaUsuario tablaUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			tablaUsuario.setConn(conn);
			usuario = tablaUsuario.buscarUsuarioPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tablaUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuario;
	}

	public ClienteRegistrado buscarClienteRegistradoPorId(Long id) throws Exception {
		ClienteRegistrado cliente;
		DAOTablaClienteRegistrado tablaCliente = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			tablaCliente.setConn(conn);
			cliente = tablaCliente.buscarClienteRegistradoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tablaCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cliente;
	}


	public ClienteNoRegistrado buscarClienteNoRegistradoPorId(Long id) throws Exception {
		ClienteNoRegistrado cliente;
		DAOTablaClienteNoRegistrado tablaCliente = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			tablaCliente.setConn(conn);
			cliente = tablaCliente.buscarClienteNoRegistradoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tablaCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cliente;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo restaurante a la base de datos.
	 * <b> post: </b> se ha agregado el restaurante que entra como parametro
	 * @param restaurante - el restaurante a agregar. restaurante != null
	 * @throws Exception - cualquier error que se genere agregando el restaurante
	 */
	public void addRestaurante(Restaurante restaurante) throws Exception {
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			conn.setAutoCommit(false);
			daoRestaurantes.addRestaurante(restaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addPreferenciaZona(PreferenciaZona preferenciaZona) throws Exception {
		DAOTablaPreferenciaZona daoPreferencias = new DAOTablaPreferenciaZona();
		DAOTablaUsuario tablaUsuario= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoPreferencias.setConn(conn);
			conn.setAutoCommit(false);
			tablaUsuario.setConn(conn);

			if(tablaUsuario.buscarUsuarioPorId(preferenciaZona.getIdCliente())!=null)
			{
				if(tablaUsuario.buscarUsuarioPorId(preferenciaZona.getIdCliente()).getRol().equalsIgnoreCase("Cliente"))
				{
					daoPreferencias.addPreferencia(preferenciaZona);
					conn.commit();
				}
				else
					throw new Exception("Esta operación solo la puede realizar un usuario Cliente. El usuario ingresado corresponde a un " + tablaUsuario.buscarUsuarioPorId(preferenciaZona.getIdCliente()).getRol());
			}
			else
				throw new Exception("El id de quien realiza la preferencia no se encuentra en el sistema de RotondAndes");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				tablaUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addPreferenciaTipo(PreferenciaTipoComida preferenciaTipo) throws Exception {
		DAOTablaPreferenciaTipoComida daoPreferencias = new DAOTablaPreferenciaTipoComida();
		DAOTablaUsuario tablaUsuario= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPreferencias.setConn(conn);
			tablaUsuario.setConn(conn);

			if(tablaUsuario.buscarUsuarioPorId(preferenciaTipo.getIdCliente())!=null)
			{
				if(tablaUsuario.buscarUsuarioPorId(preferenciaTipo.getIdCliente()).getRol().equalsIgnoreCase("Cliente"))
				{
					daoPreferencias.addPreferencia(preferenciaTipo);
					conn.commit();
				}
				else
					throw new Exception("Esta operación solo la puede realizar un usuario Cliente. El usuario ingresado corresponde a un " + tablaUsuario.buscarUsuarioPorId(preferenciaTipo.getIdCliente()).getRol());
			}
			else
				throw new Exception("El id de quien realiza la preferencia no se encuentra en el sistema de RotondAndes");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				tablaUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addPreferenciaCosto(PreferenciaPrecio preferencia) throws Exception {
		DAOTablaPreferenciaCosto daoPreferencias = new DAOTablaPreferenciaCosto();
		DAOTablaUsuario tablaUsuario= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPreferencias.setConn(conn);

			if(tablaUsuario.buscarUsuarioPorId(preferencia.getIdCliente())!=null)
			{
				if(tablaUsuario.buscarUsuarioPorId(preferencia.getIdCliente()).getRol().equalsIgnoreCase("Cliente"))
				{
					daoPreferencias.addPreferencia(preferencia);
					conn.commit();
				}
				else
					throw new Exception("Esta operación solo la puede realizar un usuario Cliente. El usuario ingresado corresponde a un " + tablaUsuario.buscarUsuarioPorId(preferencia.getIdCliente()).getRol());
			}
			else
				throw new Exception("El id de quien realiza la preferencia no se encuentra en el sistema de RotondAndes");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPreferencias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addIngrediente(Ingrediente ingrediente) throws Exception {
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoIngredientes.setConn(conn);
			daoIngredientes.addIngrediente(ingrediente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addMenu(Menu menu) throws Exception {
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoMenus.setConn(conn);
			daoMenus.addMenu(menu);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addEspacio(Espacio espacio) throws Exception {
		DAOTablaEspacio daoEspacios = new DAOTablaEspacio();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoEspacios.setConn(conn);
			daoEspacios.addEspacio(espacio);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspacios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addCondicionTecnica(CondicionTecnica condicion) throws Exception {
		DAOTablaCondicionTecnica daoCondiciones = new DAOTablaCondicionTecnica();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCondiciones.setConn(conn);
			daoCondiciones.addCondicion(condicion);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCondiciones.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	

	public void addCategoria(Categoria categorie) throws Exception {
		DAOTablaCategoria categoria = new DAOTablaCategoria();
		try 
		{
			this.conn = darConexion();
			categoria.setConn(conn);
			categoria.addCategoria(categorie);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				categoria.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	//	public void addPedido(Pedido pedido) throws Exception {
	//		DAOTablaPedido pedido2 = new DAOTablaPedido();
	//		try 
	//		{
	//			this.conn = darConexion();
	//			pedido2.setConn(conn);
	//			pedido2.addPedido(pedido);
	//			conn.commit();
	//
	//		} catch (SQLException e) {
	//			System.err.println("SQLException:" + e.getMessage());
	//			e.printStackTrace();
	//			throw e;
	//		} catch (Exception e) {
	//			System.err.println("GeneralException:" + e.getMessage());
	//			e.printStackTrace();
	//			throw e;
	//		} finally {
	//			try {
	//				pedido2.cerrarRecursos();
	//				if(this.conn!=null)
	//					this.conn.close();
	//			} catch (SQLException exception) {
	//				System.err.println("SQLException closing resources:" + exception.getMessage());
	//				exception.printStackTrace();
	//				throw exception;
	//			}
	//		}
	//	}

	public void addReserva(Reserva reserva) throws Exception {
		DAOTablaReserva reserva2 = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			reserva2.setConn(conn);
			reserva2.addReserva(reserva);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				reserva2.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addTipoComida(TipoComida tipo) throws Exception {
		DAOTablaTipoComida tipoComida = new DAOTablaTipoComida();
		try 
		{
			this.conn = darConexion();
			tipoComida.setConn(conn);
			tipoComida.addTipo_Comida(tipo);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tipoComida.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addZona(Zona zona) throws Exception {
		DAOTablaZona zona2 = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			zona2.setConn(conn);
			zona2.addZona(zona);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				zona2.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void addClienteNoRegistrado(ClienteNoRegistrado cliente) throws Exception {
		DAOTablaClienteNoRegistrado daoCliente = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			daoCliente.addClienteNoRegistrado(cliente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los restaurantes que entran como parametro a la base de datos.
	 * <b> post: </b> se han agregado los restaurantes que entran como parametro
	 * @param restaurantes - objeto que modela una lista de restaurantes y se estos se pretenden agregar. restaurantes != null
	 * @throws Exception - cualquier error que se genera agregando los restaurantes
	 */
	public void addRestaurantes(List<Restaurante> restaurantes) throws Exception {
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoRestaurantes.setConn(conn);
			Iterator<Restaurante> it = restaurantes.iterator();
			while(it.hasNext())
			{
				daoRestaurantes.addRestaurante(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addEspacios(List<Espacio> espacios) throws Exception {
		DAOTablaEspacio daoEspacios = new DAOTablaEspacio();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoEspacios.setConn(conn);
			Iterator<Espacio> it = espacios.iterator();
			while(it.hasNext())
			{
				daoEspacios.addEspacio(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoEspacios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	

	public void addCondiciones(List<CondicionTecnica> condiciones) throws Exception {
		DAOTablaCondicionTecnica daoCondiciones = new DAOTablaCondicionTecnica();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCondiciones.setConn(conn);
			Iterator<CondicionTecnica> it = condiciones.iterator();
			while(it.hasNext())
			{
				daoCondiciones.addCondicion(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoCondiciones.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addIngredientes(List<Ingrediente> ingredientes) throws Exception {
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoIngredientes.setConn(conn);
			Iterator<Ingrediente> it = ingredientes.iterator();
			while(it.hasNext())
			{
				daoIngredientes.addIngrediente(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addMenus(List<Menu> menus) throws Exception {
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoMenus.setConn(conn);
			Iterator<Menu> it = menus.iterator();
			while(it.hasNext())
			{
				daoMenus.addMenu(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addCategorias(List<Categoria> categorias) throws Exception {
		DAOTablaCategoria daoCategorias = new DAOTablaCategoria();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCategorias.setConn(conn);
			Iterator<Categoria> it = categorias.iterator();
			while(it.hasNext())
			{
				daoCategorias.addCategoria(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoCategorias.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void addReservas(List<Reserva> reservas) throws Exception {
		DAOTablaReserva daoReservas = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoReservas.setConn(conn);
			Iterator<Reserva> it = reservas.iterator();
			while(it.hasNext())
			{
				daoReservas.addReserva(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoReservas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void addClientesRegistrados(List<ClienteRegistrado> clientes) throws Exception {
		DAOTablaClienteRegistrado daoTablaClientes = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaClientes.setConn(conn);
			Iterator<ClienteRegistrado> it = clientes.iterator();
			while(it.hasNext())
			{
				daoTablaClientes.addClienteRegistrado(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoTablaClientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addClientesNoRegistrados(List<ClienteNoRegistrado> clientes) throws Exception {
		DAOTablaClienteNoRegistrado daoTablaClientes = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaClientes.setConn(conn);
			Iterator<ClienteNoRegistrado> it = clientes.iterator();
			while(it.hasNext())
			{
				daoTablaClientes.addClienteNoRegistrado(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoTablaClientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	/**
	 * Metodo que modela la transaccion que actualiza el restaurante que entra como parametro a la base de datos.
	 * <b> post: </b> se ha actualizado el restaurante que entra como parametro
	 * @param restaurante - Restaurante a actualizar. restaurante != null
	 * @throws Exception - cualquier error que se genera actualizando los restaurantes
	 */
	public void updateRestaurante(Restaurante restaurante) throws Exception {
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			daoRestaurantes.updateRestaurante(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateEspacio(Espacio espacio) throws Exception {
		DAOTablaEspacio daoEspacios = new DAOTablaEspacio();
		try 
		{
			this.conn = darConexion();
			daoEspacios.setConn(conn);
			daoEspacios.updateEspacio(espacio);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspacios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateIngrediente(Ingrediente ingrediente) throws Exception {
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		try 
		{
			this.conn = darConexion();
			daoIngredientes.setConn(conn);
			daoIngredientes.updateIngrediente(ingrediente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateMenu(Menu menu) throws Exception {
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			daoMenus.setConn(conn);
			daoMenus.updateMenu(menu);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateCondicionesTecnicas(CondicionTecnica condiciones) throws Exception {
		DAOTablaCondicionTecnica daoCondiciones = new DAOTablaCondicionTecnica();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoCondiciones.setConn(conn);
			daoCondiciones.updateCondicion(condiciones);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCondiciones.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	

	//	public void updatePedido(Pedido pedido) throws Exception {
	//		DAOTablaPedido daoPedidos = new DAOTablaPedido();
	//		try 
	//		{
	//			this.conn = darConexion();
	//			daoPedidos.setConn(conn);
	//			daoPedidos.updatePedido(pedido);
	//
	//		} catch (SQLException e) {
	//			System.err.println("SQLException:" + e.getMessage());
	//			e.printStackTrace();
	//			throw e;
	//		} catch (Exception e) {
	//			System.err.println("GeneralException:" + e.getMessage());
	//			e.printStackTrace();
	//			throw e;
	//		} finally {
	//			try {
	//				daoPedidos.cerrarRecursos();
	//				if(this.conn!=null)
	//					this.conn.close();
	//			} catch (SQLException exception) {
	//				System.err.println("SQLException closing resources:" + exception.getMessage());
	//				exception.printStackTrace();
	//				throw exception;
	//			}
	//		}
	//	}


	public void updateReserva(Reserva reserva) throws Exception {
		DAOTablaReserva daoProductos = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			daoProductos.setConn(conn);
			daoProductos.updateReserva(reserva);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateUsuario(Usuario usuario) throws Exception {
		DAOTablaUsuario daoUsuarios = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuarios.setConn(conn);
			daoUsuarios.updateUsuario(usuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateClienteRegistrado(ClienteRegistrado cliente) throws Exception {
		DAOTablaClienteRegistrado daoClientes = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			daoClientes.setConn(conn);
			daoClientes.updateClienteRegistrado(cliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateZona(Zona zona) throws Exception {
		DAOTablaZona daoZona = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			daoZona.setConn(conn);
			daoZona.updateZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updatePreferenciaZona(PreferenciaZona preferenciaZona) throws Exception {
		DAOTablaPreferenciaZona daoTablaPreferencia = new DAOTablaPreferenciaZona();
		try 
		{
			this.conn = darConexion();
			daoTablaPreferencia.setConn(conn);
			daoTablaPreferencia.updatePreferencia(preferenciaZona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencia.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void updatePreferenciaTipoComida(PreferenciaTipoComida preferencia) throws Exception {
		DAOTablaPreferenciaTipoComida daoTablaPreferencia = new DAOTablaPreferenciaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoTablaPreferencia.setConn(conn);
			daoTablaPreferencia.updatePreferencia(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencia.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void updatePreferenciaCosto(PreferenciaPrecio preferencia) throws Exception {
		DAOTablaPreferenciaCosto daoTablaPreferencia = new DAOTablaPreferenciaCosto();
		try 
		{
			this.conn = darConexion();
			daoTablaPreferencia.setConn(conn);
			daoTablaPreferencia.updatePreferencia(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferencia.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateClienteNoRegistrado(ClienteNoRegistrado cliente) throws Exception {
		DAOTablaClienteNoRegistrado daoClientes = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			daoClientes.setConn(conn);
			daoClientes.updateClienteNoRegistrado(cliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClientes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void updateTipoComida(TipoComida tipoComida) throws Exception {
		DAOTablaTipoComida daoTipoComida = new DAOTablaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoTipoComida.setConn(conn);
			daoTipoComida.updateTipo_Comida(tipoComida);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTipoComida.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el restaurante que entra como parametro a la base de datos.
	 * <b> post: </b> se ha eliminado el restaurante que entra como parametro
	 * @param restaurante - Restaurante a eliminar. restaurante != null
	 * @throws Exception - cualquier error que se genera actualizando los restaurantes
	 */
	public void deleteRestaurante(Restaurante restaurante) throws Exception {
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		try 
		{
			//////transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			daoRestaurantes.deleteRestaurante(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	

	public void deleteCondicion(CondicionTecnica condicion) throws Exception {
		DAOTablaCondicionTecnica daoCondicion = new DAOTablaCondicionTecnica();
		try 
		{
			this.conn = darConexion();
			daoCondicion.setConn(conn);
			daoCondicion.deleteCondicion(condicion);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCondicion.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteEspacio(Espacio espacio) throws Exception {
		DAOTablaEspacio daoEspacio = new DAOTablaEspacio();
		try 
		{
			this.conn = darConexion();
			daoEspacio.setConn(conn);
			daoEspacio.deleteEspacio(espacio);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEspacio.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteIngrediente(Ingrediente ingrediente) throws Exception {
		DAOTablaIngrediente daoIngrediente = new DAOTablaIngrediente();
		DAOTablaIngrediente_Restaurante daoIn= new DAOTablaIngrediente_Restaurante();
		DAOTablaIngrediente_Producto dao= new DAOTablaIngrediente_Producto();
		try 
		{
			this.conn = darConexion();
			daoIngrediente.setConn(conn);
			daoIn.setConn(conn);
			dao.setConn(conn);

			dao.deleteRelacion(ingrediente.getIdIngrediente());
			daoIn.deleteRelacion(ingrediente.getIdIngrediente());
			daoIngrediente.deleteIngrediente(ingrediente);


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngrediente.cerrarRecursos();
				daoIn.cerrarRecursos();
				dao.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteMenu(Menu menu) throws Exception {
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		try 
		{
			this.conn = darConexion();
			daoMenu.setConn(conn);
			daoMenu.deleteMenu(menu);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenu.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void deleteReserva(Reserva reserva) throws Exception {
		DAOTablaReserva daoReserva = new DAOTablaReserva();
		try 
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.deleteReserva(reserva);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteUsuario(Usuario usuario) throws Exception {
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaClienteRegistrado daoTablaCliente = new DAOTablaClienteRegistrado();
		DAOTablaRestaurante daoRestaurante= new DAOTablaRestaurante();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoTablaCliente.setConn(conn);
			daoRestaurante.setConn(conn);

			if(usuario.getRol().equalsIgnoreCase("Cliente"))
				daoTablaCliente.deleteClienteRegistrado(daoTablaCliente.buscarClienteRegistradoPorId(usuario.getId()));
			else if(usuario.getRol().equalsIgnoreCase("Restaurante"))
				daoRestaurante.deleteRestaurante(daoRestaurante.buscarRestaurantePorNit(usuario.getId()));

			daoUsuario.deleteUsuario(usuario);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				daoTablaCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deleteClienteRegistrado(ClienteRegistrado cliente) throws Exception {
		DAOTablaClienteRegistrado daoCliente = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			daoCliente.deleteClienteRegistrado(cliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void deleteClienteNoRegistrado(ClienteNoRegistrado cliente) throws Exception {
		DAOTablaClienteNoRegistrado daoCliente = new DAOTablaClienteNoRegistrado();
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			daoCliente.deleteClienteNoRegistrado(cliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void deleteZona(Zona zona) throws Exception {
		DAOTablaZona daoZona = new DAOTablaZona();
		try 
		{
			this.conn = darConexion();
			daoZona.setConn(conn);
			daoZona.deleteZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deletePreferenciaZona(PreferenciaZona zona) throws Exception {
		DAOTablaPreferenciaZona daoTablaPreferenciaZona = new DAOTablaPreferenciaZona();
		try 
		{
			this.conn = darConexion();
			daoTablaPreferenciaZona.setConn(conn);
			daoTablaPreferenciaZona.deletePreferenciaZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPreferenciaZona.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void deletePreferenciaTipo(PreferenciaTipoComida preferencia) throws Exception {
		DAOTablaPreferenciaTipoComida daoTable = new DAOTablaPreferenciaTipoComida();
		try 
		{
			this.conn = darConexion();
			daoTable.setConn(conn);
			daoTable.deletePreferenciaTipoComida(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTable.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Mesa> darMesas() throws Exception {
		List<Mesa> mesas;
		DAOTablaMesa tabla = new DAOTablaMesa();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			tabla.setConn(conn);
			mesas = tabla.darMesas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tabla.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return mesas;
	}




	public Mesa buscarMesaPorId(Long id) throws Exception {
		Mesa mesa;
		DAOTablaMesa daoMesas = new DAOTablaMesa();
		try 
		{
			this.conn = darConexion();
			daoMesas.setConn(conn);
			mesa = daoMesas.buscarMesaPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMesas.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return mesa;
	}


	public void addMesa(Mesa mesa) throws Exception {
		DAOTablaMesa tabla = new DAOTablaMesa();
		try 
		{
			this.conn = darConexion();
			tabla.setConn(conn);
			tabla.addMesa(mesa);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				tabla.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void deletePreferenciaCosto(PreferenciaPrecio preferencia) throws Exception {
		DAOTablaPreferenciaCosto daoTable = new DAOTablaPreferenciaCosto();
		try 
		{
			this.conn = darConexion();
			daoTable.setConn(conn);
			daoTable.deletePreferenciaCosto(preferencia);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTable.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	public void addIngresarIngredienteRestaurante(IngresarIngrediente ingresoRestaurante) throws Exception {
		DAOTablaIngrediente daoIngredientes = new DAOTablaIngrediente();
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaIngrediente_Restaurante daoTablaIngrediente_Restaurante = new DAOTablaIngrediente_Restaurante();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoIngredientes.setConn(conn);
			daoUsuario.setConn(conn);
			daoTablaIngrediente_Restaurante.setConn(conn);
			Long idRes = ingresoRestaurante.getIdUsuarioRestaurante();
			if(daoUsuario.buscarUsuarioPorId(idRes)!=null){
				if(daoUsuario.verificarContraseniaUsuario(idRes).equalsIgnoreCase(ingresoRestaurante.getContrasenia())){
					if(daoUsuario.buscarUsuarioPorId(ingresoRestaurante.getIdUsuarioRestaurante()).getRol().equalsIgnoreCase("Restaurante")){
						System.out.println("Se verifico el restaurante");
						daoIngredientes.addIngrediente(ingresoRestaurante.getIngrediente());
						daoTablaIngrediente_Restaurante.addIngrediente_Restaurante(ingresoRestaurante.getIngrediente().getIdIngrediente(), idRes);

					}
					else
					{
						throw new Exception("El usuario no tiene nol de Restaurante, su rol es "+daoUsuario.buscarUsuarioPorId(ingresoRestaurante.getIdUsuarioRestaurante()).getRol());
					}
				}
				else{
					throw new Exception("Contraseña incorrecta :c");
				}
			}
			else
			{
				throw new Exception("No existe un usuario restaurante con la identificacion ingresada");
			}	
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoIngredientes.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				daoTablaIngrediente_Restaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	//REQUERIMIENTO 9
	public void addIngresoPedido(IngresarPedido ingresoPedido) throws Exception {
		DAOTablaPedido daoTablaPedido = new DAOTablaPedido();
		DAOTablaTiene_Pedidos daoTiene_Pedidos = new DAOTablaTiene_Pedidos();
		DAOTablaProducto_Venta daoProductoVenta = new DAOTablaProducto_Venta();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		DAOTablaProducto daoTablaProducto = new DAOTablaProducto();
		DAOTablaMenu daoTablaMenu =new DAOTablaMenu();
		DAOTablaTiene_Producto daoTablaTiene_Producto = new DAOTablaTiene_Producto();
		DAOTablaPedido_Personalizado daoTablaPedido_Personalizado = new  DAOTablaPedido_Personalizado();
		DAOTablaMesa daoMesa =new DAOTablaMesa();
		DAOTablaEquivalencias_Productos daoEquivalencias_Productos =new DAOTablaEquivalencias_Productos();

		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaPedido.setConn(conn);
			daoTiene_Pedidos.setConn(conn);
			daoProductoVenta.setConn(conn);
			daoUsuarios.setConn(conn);
			daoTablaProducto.setConn(conn);
			daoTablaMenu.setConn(conn);
			daoTablaTiene_Producto.setConn(conn);
			daoMesa.setConn(conn);
			daoTablaPedido_Personalizado.setConn(conn);
			daoEquivalencias_Productos.setConn(conn);			

			Integer cantSolicitada = ingresoPedido.getCantidadSolicitada();

			if(daoUsuarios.buscarUsuarioPorId(ingresoPedido.getIdCliente())!=null)
			{
				if(daoUsuarios.verificarContraseniaUsuario(ingresoPedido.getIdCliente()).equalsIgnoreCase(ingresoPedido.getContrasenia()))
				{

					if(daoUsuarios.buscarUsuarioPorId(ingresoPedido.getIdCliente()).getRol().equalsIgnoreCase("Cliente")){
						Long prodPedido= ingresoPedido.getIdProductoVenta();	
						Producto producto = daoTablaProducto.buscarProductoPorIdProductoVenta(prodPedido);
						if(daoProductoVenta.buscarProductoPorId(prodPedido)!=null){
							if(cantSolicitada>0){
								if(daoProductoVenta.buscarProductoPorId(prodPedido).getCantidad()>= cantSolicitada)
								{
									if(producto!=null){
										System.out.println("Es un producto normal");
										//CASO PEDIDO SIN MESA
										if(ingresoPedido.getMesa()==null){
											daoTablaPedido.addPedidoPorCliente(ingresoPedido.getNumeroOrden(), daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getPrecio(), ingresoPedido.getIdCliente(), ingresoPedido.getIdProductoVenta(), false, ingresoPedido.getCantidadSolicitada(), ingresoPedido.getMesa());
											daoTiene_Pedidos.addTiene_Pedido(daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getNit(), ingresoPedido.getNumeroOrden());
										}

										//CASO PEDIDO CON MESA
										else
										{
											if(daoMesa.buscarMesaPorId(ingresoPedido.getMesa())!=null)
											{
												Integer cantidadSillasDisponibles = daoMesa.buscarMesaPorId(ingresoPedido.getMesa()).getCantidadSillas();
												if(cantidadSillasDisponibles>=ingresoPedido.getCantidadSillas()){
													daoTablaPedido.addPedidoPorCliente(ingresoPedido.getNumeroOrden(), daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getPrecio(), ingresoPedido.getIdCliente(), ingresoPedido.getIdProductoVenta(), false, ingresoPedido.getCantidadSolicitada(), ingresoPedido.getMesa());
													daoTiene_Pedidos.addTiene_Pedido(daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getNit(), ingresoPedido.getNumeroOrden());
												}
												else{
													throw new Exception("El numero de sillas solicitadas no està disponible en la mesa, solo hay "+cantidadSillasDisponibles+" porfavor ingrese otra mesa.");														
												}

											}
											else{
												conn.rollback();
												throw new Exception("El numero ingresado no corresponde a ninguna mesa");														
											}
										}
									}
									else if(daoTablaMenu.buscarMenuPorIdProductoVenta(prodPedido)!=null){
										System.out.println("Es un menu");


										ArrayList<Long> productosMenu = daoTablaTiene_Producto.darProductosDelMenu(daoTablaMenu.buscarMenuPorIdProductoVenta(prodPedido).getId());										boolean menuDisponible=false;
										for (int i = 0; i <productosMenu.size(); i++) {
											Producto productoAct = daoTablaProducto.buscarProductoPorId(productosMenu.get(i));
																	
										}

										if(menuDisponible){
											System.out.println("SI ESTA DISPONIBLE???-->"+menuDisponible);

											if(ingresoPedido.isPersonalizado()){
												Long pc=ingresoPedido.getProductoCambiado();
												Long pn = ingresoPedido.getProductoNuevo();
												System.out.println("Ingresando pedido de un menu personalizado...");
												//CASO PEDIDO SIN MESA
													if(ingresoPedido.getMesa()==null){	
														Date fecha = new Date();
														Integer n = 1;
														Pedido p = new Pedido(ingresoPedido.getNumeroOrden(), daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getPrecio(), ingresoPedido.getIdCliente(), false, (java.sql.Date) fecha, ingresoPedido.getIdProductoVenta(), n, true, ingresoPedido.getCantidadSolicitada(), ingresoPedido.getMesa());

														daoTablaPedido.addPedidoPorCliente(p);
														daoTiene_Pedidos.addTiene_Pedido(daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getNit(), ingresoPedido.getNumeroOrden());
														daoTablaPedido_Personalizado.addPedido_PersonalizadoPorCliente(ingresoPedido.getNumeroOrden(), daoTablaMenu.buscarMenuPorIdProductoVenta(prodPedido).getId(), pc, pn);
													}
													//CASO PEDIDO CON MESA
													else
													{
														if(daoMesa.buscarMesaPorId(ingresoPedido.getMesa())!=null)
														{
															Integer cantidadSillasDisponibles = daoMesa.buscarMesaPorId(ingresoPedido.getMesa()).getCantidadSillas();
															if(cantidadSillasDisponibles>=ingresoPedido.getCantidadSillas()){
																daoTablaPedido.addPedidoPorCliente(ingresoPedido.getNumeroOrden(), daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getPrecio(), ingresoPedido.getIdCliente(), ingresoPedido.getIdProductoVenta(), true, ingresoPedido.getCantidadSolicitada(), ingresoPedido.getMesa());
																daoTiene_Pedidos.addTiene_Pedido(daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getNit(), ingresoPedido.getNumeroOrden());
																daoTablaPedido_Personalizado.addPedido_PersonalizadoPorCliente(ingresoPedido.getNumeroOrden(), daoTablaMenu.buscarMenuPorIdProductoVenta(prodPedido).getId(), pc, pn);
															}
															else{
																throw new Exception("El numero de sillas solicitadas no està disponible en la mesa, solo hay "+cantidadSillasDisponibles+" porfavor ingrese otra mesa.");														
															}

														}
														else{
															conn.rollback();
															throw new Exception("El numero ingresado no corresponde a ninguna mesa");														
														}
													}
											}
											else
											{
												System.out.println("Ingresando pedido de un menù sin personalizar...");
												daoTablaPedido.addPedidoPorCliente(ingresoPedido.getNumeroOrden(), daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getPrecio(), ingresoPedido.getIdCliente(), ingresoPedido.getIdProductoVenta(), false, ingresoPedido.getCantidadSolicitada(), ingresoPedido.getMesa());
												daoTiene_Pedidos.addTiene_Pedido(daoProductoVenta.buscarProductoPorId(ingresoPedido.getIdProductoVenta()).getNit(), ingresoPedido.getNumeroOrden());
											}
										}									
									}
									else
									{
										throw new Exception("No existe un producto en venta con el identificador "+prodPedido);		
									}

								}
								else {
									throw new Exception("Lo sentimos, en el momento no tenemos disponible la cantidad solicitada del producto, solo tenemos "+(daoProductoVenta.buscarProductoPorId(prodPedido)).getCantidad());	
								}
							}
							else {
								throw new Exception("La cantidad de producto a pedir debe ser mayor a 0, porfavor ingrese una cantidad vàlida");	
							}

						}
						else
						{
							throw new Exception("No existe un producto en venta con el identificador "+prodPedido);		
						}
					}

					else{
						throw new Exception("El identificador dado no le corresponde a un cliente. Su rol es " + daoUsuarios.buscarUsuarioPorId(ingresoPedido.getIdCliente()).getRol());
					}
				}
				else{
					throw new Exception("Contraseña incorrecta :c");
				}
			}
			else{
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");		
			}
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPedido.cerrarRecursos();
				daoTiene_Pedidos.cerrarRecursos();
				daoProductoVenta.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				daoTablaProducto.cerrarRecursos();
				daoTablaMenu.cerrarRecursos();
				daoTablaTiene_Producto.cerrarRecursos();
				daoMesa.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	//REQ 10 
	public void servirPedido(ServirPedido servirPedido)  throws Exception {
		DAOTablaUsuario daoTablaUsuario = new DAOTablaUsuario();
		DAOTablaPedido daoTablaPedido = new DAOTablaPedido();
		DAOTablaProducto_Venta daoTablaProductoVenta = new DAOTablaProducto_Venta();
		DAOTablaTiene_Pedidos daoTablaTiene_Pedidos = new DAOTablaTiene_Pedidos();
		DAOTablaMenu daoTablaMenu = new DAOTablaMenu();
		DAOTablaTiene_Producto daoTablaTiene_Producto = new DAOTablaTiene_Producto();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaUsuario.setConn(conn);
			daoTablaPedido.setConn(conn);
			daoTablaProductoVenta.setConn(conn);
			daoTablaTiene_Pedidos.setConn(conn);
			daoTablaTiene_Producto.setConn(conn);
			daoTablaMenu.setConn(conn);

			Long idRes = servirPedido.getIdUsuarioRestaurante();
			Long idProductoPedido= daoTablaPedido.buscarPedidoPorId(servirPedido.getIdPedido()).getIdProductoVenta();
			Pedido pedido = daoTablaPedido.buscarPedidoPorId(servirPedido.getIdPedido());
			Integer cantSol = daoTablaPedido.buscarPedidoPorId(servirPedido.getIdPedido()).getCantidadSolicitada();
			if(daoTablaUsuario.buscarUsuarioPorId(idRes)!=null)
			{
				if(daoTablaUsuario.verificarContraseniaUsuario(idRes).equalsIgnoreCase(servirPedido.getContrasenia()))
				{

					if(daoTablaUsuario.buscarUsuarioPorId(servirPedido.getIdUsuarioRestaurante()).getRol().equalsIgnoreCase("Restaurante"))
					{
						if(daoTablaTiene_Pedidos.verificarOrden(servirPedido.getIdPedido()).equals(idRes)){

							//pidio un menu o un producto? para actualizar cantidad
							if(daoTablaMenu.buscarMenuPorIdProductoVenta((pedido.getIdProductoVenta()))!=null)
							{
								System.out.println("Es un menu!");
								ArrayList<Long> productosMenu = daoTablaTiene_Producto.darProductosDelMenu((daoTablaMenu.buscarMenuPorIdProductoVenta(idProductoPedido)).getId());
								int j=0;

								for (int i = 0; i < productosMenu.size(); i++) {
									if(daoTablaProductoVenta.cantidadProducto(idProductoPedido)>=1){
										System.out.println("reduciendo productos"+idProductoPedido);
										daoTablaProductoVenta.reducirCantidadProductos(productosMenu.get(i), cantSol); 
										j++;
									}
									else{
										throw new Exception("No hay la cantidad suficiente del producto "+idProductoPedido+" en su inventario para cumplir con este pedido.");
									}

								}
								if(j==productosMenu.size()){
									Pedido act =daoTablaPedido.buscarPedidoPorId(servirPedido.getIdPedido());
									Pedido p = new Pedido(act.getNumeroOrden(), act.getTotalAPagar(), act.getIdCliente(), true, act.getFechaServicio(), act.getIdProductoVenta(), act.getDia(), act.isPersonalizado(), act.getCantidadSolicitada(), act.getMesa());
									daoTablaPedido.servirPedido(p);
									daoTablaProductoVenta.reducirCantidadProductos(idProductoPedido,cantSol);
									conn.commit();
								} 

							}
							else{
								System.out.println("Es un producto!");
								Pedido act =daoTablaPedido.buscarPedidoPorId(servirPedido.getIdPedido());
								Pedido p = new Pedido(act.getNumeroOrden(), act.getTotalAPagar(), act.getIdCliente(), true, act.getFechaServicio(), act.getIdProductoVenta(), act.getDia(), act.isPersonalizado(), act.getCantidadSolicitada(), act.getMesa());
								daoTablaPedido.servirPedido(p);
								daoTablaProductoVenta.reducirCantidadProductos(idProductoPedido, cantSol);
								conn.commit();
							}
						}
						else{
							throw new Exception("El numero de orden del pedido que usted ingreso, no corresponde a un pedido de su restaurante");
						}

					}
					else{
						throw new Exception("El identificador dado no le corresponde a un usuario restaurante. Su rol es " + daoTablaUsuario.buscarUsuarioPorId(servirPedido.getIdUsuarioRestaurante()).getRol());
					}
				} 
				else{
					throw new Exception("Contraseña incorrecta :c");
				}
			}
			else{
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema."); 
			}

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaPedido.cerrarRecursos();
				daoTablaUsuario.cerrarRecursos();;
				daoTablaProductoVenta.cerrarRecursos();
				daoTablaTiene_Pedidos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}




	public List<Usuario> darUsuariosClientes() throws Exception {
		List<Usuario> usuarios;
		DAOTablaUsuario  daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoUsuario.setConn(conn);
			usuarios = daoUsuario.darUsuariosCliente();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}



	public void addUsuario(Usuario usuario) throws Exception {
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoUsuario.setConn(conn);
			if(usuario.getContrasenia()==null || usuario.getContrasenia().equals(""))
				throw new Exception("La contrasenia ingresada no es valida. No puede ser un valor nulo.");

			else
				daoUsuario.addUsuario(usuario);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addUsuarioCliente(RegistroCliente cliente) throws Exception{
		DAOTablaClienteRegistrado daoClientes = new DAOTablaClienteRegistrado();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoClientes.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador())!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador()).getRol().equalsIgnoreCase("Administrador"))
				{
					if(daoUsuarios.verificarContraseniaUsuario(cliente.getIdAdministrador()).equals(cliente.getContraseniaAdministrador())){
						if(cliente.getUsuario().getRol().equalsIgnoreCase("Cliente")){
							if(cliente.getUsuario().getContrasenia()==null  || cliente.getUsuario().getContrasenia().equals(""))
								throw new Exception("Es necesario que ingrese una contraseña");
							else
							{
								Usuario user= new Usuario(cliente.getUsuario().getId(), cliente.getUsuario().getNombre(), cliente.getUsuario().getCorreoElectronico(), cliente.getUsuario().getRol(), cliente.getUsuario().getContrasenia());
								ClienteRegistrado cliente2= new ClienteRegistrado(cliente.getUsuario().getId(), cliente.getUsuario().getNombre(), cliente.getUsuario().getCorreoElectronico(), cliente.getBanco());

								daoClientes.addClienteRegistrado(cliente2);
								daoUsuarios.addUsuario(user);
							}

						}
						else{
							throw new Exception("Ingrese los datos correspondientes al rol que desea. Esta ingresando de "+ cliente.getUsuario().getRol()  +" y se esperaban datos de clientes " );
						}
					}
					else
						throw new Exception("La contraseña no es vàlida para el identificador dado.");
				}
				else
					throw new Exception("El identificador dado no le corresponde a un administrador. Su rol es " + daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador()).getRol());

				conn.commit();
			}
			else
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");



		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClientes.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addUsuarioRestaurante(RegistroRestaurante restaurante) throws Exception{
		DAOTablaRestaurante daoRestaurantes = new DAOTablaRestaurante();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoRestaurantes.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(restaurante.getIdAdministrador())!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(restaurante.getIdAdministrador()).getRol().equalsIgnoreCase("Administrador"))
				{
					if(daoUsuarios.verificarContraseniaUsuario(restaurante.getIdAdministrador()).equals(restaurante.getContraseniaAdministrador()))
					{
						if(restaurante.getUsuario().getRol().equalsIgnoreCase("Restaurante")){
							if(restaurante.getUsuario().getContrasenia()== null  || restaurante.getUsuario().getContrasenia().equals(""))
								throw new Exception("Debe ingresar una contraseña para el usuario restaurante.");

							else{

								Usuario user= new Usuario(restaurante.getUsuario().getId(), restaurante.getUsuario().getNombre(), restaurante.getUsuario().getCorreoElectronico(), restaurante.getUsuario().getRol(), restaurante.getUsuario().getContrasenia());
								Restaurante restaurante2= new Restaurante(restaurante.getUsuario().getId(), restaurante.getUsuario().getNombre(), restaurante.getEspecialidad(), restaurante.getTotalProductosDisponibles(), restaurante.getTipoComida(), restaurante.getPaginaWeb(), restaurante.getIdZona());

								daoRestaurantes.addRestaurante(restaurante2);
								daoUsuarios.addUsuario(user);
							}

						}
						else{
							throw new Exception("Ingrese los datos correspondientes al rol que desea. Esta ingresando de "+ restaurante.getUsuario().getRol()  +" y se esperaban datos de clientes " );
						}						
					}
					else
						throw new Exception("La contraseña ingresada para el administrador no es vàlida.");
				}
				else
					throw new Exception("El identificador dado no le corresponde a un administrador. Su rol es " + daoUsuarios.buscarUsuarioPorId(restaurante.getIdAdministrador()).getRol());

				conn.commit();
			}
			else
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addClienteAdministrador(IngresoClientesPorAdministrador cliente) throws Exception {
		DAOTablaClienteRegistrado daoClientes = new DAOTablaClienteRegistrado();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{

			conn.setAutoCommit(false);
			daoClientes.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador())!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador()).getRol().equalsIgnoreCase("Administrador"))
					if(daoUsuarios.verificarContraseniaUsuario(cliente.getIdAdministrador()).equals(cliente.getContraseniaAdministrador()))
					{
						daoClientes.addClienteRegistrado(cliente.getCliente());
					}
					else 
						throw new Exception("La contraseña ingresada no coincide con el identificador dado");
				else
					throw new Exception("El identificador dado no le corresponde a un administrador. Su rol es " + daoUsuarios.buscarUsuarioPorId(cliente.getIdAdministrador()).getRol());
				conn.commit();
			}
			else
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoClientes.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addRestauranteAdministrador(IngresoRestaurantesPorAdministrador elemento) throws Exception {
		DAOTablaRestaurante daoTablaRestaurante = new DAOTablaRestaurante();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoTablaRestaurante.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(elemento.getIdAdministrador())!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(elemento.getIdAdministrador()).getRol().equalsIgnoreCase("Administrador"))
					if(daoUsuarios.verificarContraseniaUsuario(elemento.getIdAdministrador()).equals(elemento.getContraseniaAdministrador()))
						daoTablaRestaurante.addRestaurante(elemento.getRestaurante());
					else 
						throw new Exception("La contraseña no es vàlida.");
				else
					throw new Exception("El identificador dado no le corresponde a un administrador. Su rol es " + daoUsuarios.buscarUsuarioPorId(elemento.getIdAdministrador()).getRol());
				conn.commit();
			}
			else
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaRestaurante.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addZonaAdministrador(IngresoZonaPorAdministrador zona) throws Exception {
		DAOTablaZona daoTablaZona = new DAOTablaZona();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoTablaZona.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(zona.getIdAdministrador())!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(zona.getIdAdministrador()).getRol().equalsIgnoreCase("Administrador"))
					if(daoUsuarios.verificarContraseniaUsuario(zona.getIdAdministrador()).equals(zona.getContraseniaAdministrador()))
						daoTablaZona.addZona(zona.getZona());
					else
						throw new Exception("La contraseña no es vàlida.");
				else
					throw new Exception("El identificador dado no le corresponde a un administrador. Su rol es " + daoUsuarios.buscarUsuarioPorId(zona.getIdAdministrador()).getRol());
				conn.commit();
			}
			else
				throw new Exception("El identificador dado no le pertenece a un usuario registrado en el sistema.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaZona.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void addClienteRegistrado(ClienteRegistrado cliente) throws Exception {
		DAOTablaClienteRegistrado daoCliente = new DAOTablaClienteRegistrado();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoCliente.setConn(conn);
			daoCliente.addClienteRegistrado(cliente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//ITERACION 3: METODOS

	public void addEquivalenciaIngredientes(IngresarEquivalenciaIngredientes ingredientes) throws Exception {
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		DAOTablaIngrediente daoTablaIngrediente = new DAOTablaIngrediente();
		DAOTablaEquivalencias_Ingredientes daoEquivalencias_Ingredientes = new DAOTablaEquivalencias_Ingredientes();
		DAOTablaIngrediente_Restaurante daoTablaIngrediente_Restaurante = new DAOTablaIngrediente_Restaurante();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoUsuario.setConn(conn);
			daoTablaIngrediente.setConn(conn);
			daoEquivalencias_Ingredientes.setConn(conn);
			daoTablaIngrediente_Restaurante.setConn(conn);
			Long idRes = ingredientes.getIdUsuarioRestaurante();
			Long idP1 = ingredientes.getId_ingrediente1();
			Long idP2 = ingredientes.getId_ingrediente2();

			if(daoUsuario.buscarUsuarioPorId(idRes)!= null){
				if(daoUsuario.verificarContraseniaUsuario(idRes).equalsIgnoreCase(ingredientes.getContrasenia())){
					if(daoUsuario.buscarUsuarioPorId(ingredientes.getIdUsuarioRestaurante()).getRol().equalsIgnoreCase("Restaurante")){

						if(daoTablaIngrediente.buscarIngredientePorId(idP1)!=null || daoTablaIngrediente.buscarIngredientePorId(idP2)!=null )
						{
							if(daoTablaIngrediente_Restaurante.buscarIngredientePorId(idP1).getNit_restaurante().equals(idRes) && daoTablaIngrediente_Restaurante.buscarIngredientePorId(idP2).getNit_restaurante().equals(idRes))
							{
								daoEquivalencias_Ingredientes.addEquivalencia_Ingredientes(idP1, idP2, idRes);
							}
							else
							{
								throw new Exception("Los ingredientes ingresados no pertenecen a su restaurante");
							}
						}	
						else
						{
							throw new Exception("Los ingredientes ingresados no existen en la base de datos");
						}		
					}
					else
					{
						conn.rollback();
						throw new Exception("El nit del restaurante asociado a la equivalencia, debe ser el mismo de quien lo ingresa");
					}
				}
				else
				{
					throw new Exception("El usuario no tiene nol de Restaurante, su rol es "+daoUsuario.buscarUsuarioPorId(idRes).getRol());
				}	

			}
			else{
				throw new Exception("Contraseña incorrecta :c");
			}

			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTablaIngrediente.cerrarRecursos();
				daoEquivalencias_Ingredientes.cerrarRecursos();
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	

	/**
	 * 
	 * @param idRes
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Producto> buscarProductoFiltros(Long idRes) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorFiltrosLosProductosServidosRotondAndes(idRes);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public ArrayList<Producto> buscarProductoFiltroCategoria(Long idCategoria) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorFiltroCategoria(idCategoria);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}


	public ArrayList<Producto> buscarProductoZona(Long idZona) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorZona(idZona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public ArrayList<Producto> buscarProductoRangoPrecio(Double minimo, Double maximo) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			if(minimo>maximo)
				throw new Exception("Ingrese un rango correcto.");
			else if(minimo<0 || maximo<0)
				throw new Exception("Los valores ingresados para los precios no pueden ser negativos.");

			productos = daoProducto.buscarPorFiltroRangoPrecio(minimo, maximo);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public Producto buscarProductoMayorMenu() throws Exception {
		Producto productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProductos.setConn(conn);
			productos = daoProductos.buscarProductoEncontradoMayorCantidadMenus();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;


	}

	public void surtirRestaurante(SurtirRestaurante restaurante) throws Exception {
		DAOTablaProducto_Venta daoProductos = new DAOTablaProducto_Venta();
		DAOTablaUsuario restaurantes= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoProductos.setConn(conn);
			restaurantes.setConn(conn);
			if(restaurante.getIdRestaurante()!= null)
			{
				if(restaurantes.buscarUsuarioPorId(restaurante.getIdRestaurante())!= null)
				{
					if(restaurantes.buscarUsuarioPorId(restaurante.getIdRestaurante()).getRol().equalsIgnoreCase("Restaurante"))
					{
						if(restaurantes.verificarContraseniaUsuario(restaurante.getIdRestaurante()).equals(restaurante.getContraseniaRestaurante()))
						{
							daoProductos.surtirProductos(restaurante.getIdRestaurante());
						}
						else
							throw new Exception("Los datos ingresados no son vàlidos. Intente de nuevo.");
					}
					else
						throw new Exception("Esta operaciòn solo puede ser realizada por un usuario Restaurante y se esta haciendo por un: " + restaurantes.buscarUsuarioPorId(restaurante.getIdRestaurante()).getRol());
				}
				else
					throw new Exception("El restaurante ingresado no se encuentra registrado en el sistema.");
			}
			else
				throw new Exception("El restaurante no tiene un identificador.");

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				restaurantes.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();

			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void cancelarPedido(CancelarPedido cancelacion) throws Exception {
		DAOTablaPedido pedidoAEliminar = new DAOTablaPedido();
		DAOTablaTiene_Pedidos daoTienePedido= new DAOTablaTiene_Pedidos();
		DAOTablaUsuario usuarios = new DAOTablaUsuario();
		DAOTablaPedido_Personalizado daoPersonalizado = new DAOTablaPedido_Personalizado();

		try 
		{
			this.conn = darConexion();
			pedidoAEliminar.setConn(conn);
			daoTienePedido.setConn(conn);
			usuarios.setConn(conn);
			daoPersonalizado.setConn(conn);

			Usuario usuario= usuarios.buscarUsuarioPorId(cancelacion.getIdRestaurante());
			if(usuario!= null)
			{
				if(usuario.getRol().equalsIgnoreCase("Restaurante"))
				{

					if(pedidoAEliminar.buscarPedidoPorId(cancelacion.getNumeroOrdenCancelada())!= null)
					{
						if(usuarios.verificarContraseniaUsuario(cancelacion.getIdRestaurante()).equals(cancelacion.getContraseniaRestaurante()))
						{
							if(daoTienePedido.verificarOrden(cancelacion.getNumeroOrdenCancelada())== cancelacion.getIdRestaurante())
							{
								boolean servido= pedidoAEliminar.buscarPedidoPorId(cancelacion.getNumeroOrdenCancelada()).isServido();
								if(pedidoAEliminar.buscarPedidoPorId(cancelacion.getNumeroOrdenCancelada())!= null)

									if(!servido)
									{
										if(pedidoAEliminar.buscarPedidoPorId(cancelacion.getNumeroOrdenCancelada()).isPersonalizado())
										{
											daoTienePedido.cancelarPedido(cancelacion.getNumeroOrdenCancelada());
											daoPersonalizado.cancelarPedido(cancelacion.getNumeroOrdenCancelada());;
											pedidoAEliminar.cancelarPedido(cancelacion.getNumeroOrdenCancelada());	
										}
										else
										{
											daoTienePedido.cancelarPedido(cancelacion.getNumeroOrdenCancelada());
											pedidoAEliminar.cancelarPedido(cancelacion.getNumeroOrdenCancelada());	
										}
									}
									else
										throw new Exception("El pedido que desea cancelar ya ha sido servido. No puede completar esta operaciòn.");
							}
							else
								throw new Exception("El restaurante dado no tiene permiso para realizar cambios sobre el pedido. Esto le pertenece a otro restaurante.");						

						}
						else
							throw new Exception("La informaciòn dada no es vàlida. Intentelo de nuevo.");
					}
					else 
						throw new Exception("El pedido a cancelar no existe");

				}
				else
					throw new Exception("Esta operaciòn solo puede realizarse por usuarios de tipo Restaurante. En su lugar es: " + usuario.getRol());
			}
			else 
				throw new Exception("El identificador ingresado no se encuentra registrado en el sistema");	

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				pedidoAEliminar.cerrarRecursos();
				daoTienePedido.cerrarRecursos();
				usuarios.cerrarRecursos();
				daoPersonalizado.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}


	public void confirmarPedidosServidosPorMesa(Long numeroMesa, ServirPedido servirPedido) throws Exception {
		boolean mesaCompletada= false;
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		DAOTablaUsuario usuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			usuarios.setConn(conn);
			if(usuarios.buscarUsuarioPorId(servirPedido.getIdUsuarioRestaurante())!= null)
			{
				if(usuarios.buscarUsuarioPorId(servirPedido.getIdUsuarioRestaurante()).getRol().equalsIgnoreCase("Restaurante"))
				{
					if(usuarios.verificarContraseniaUsuario(servirPedido.getIdUsuarioRestaurante()).equals(servirPedido.getContrasenia()))
					{
						if(daoPedidos.buscarPedidosMesa(numeroMesa)!= null)
						{
							for (int i = 0; i < daoPedidos.buscarPedidosMesa(numeroMesa).size(); i++) {
								boolean estaServido= daoPedidos.buscarPedidoPorId(daoPedidos.buscarPedidosMesa(numeroMesa).get(i)).isServido();
								if(estaServido)
									mesaCompletada= true;
								else
								{
									if(daoPedidos.buscarPedidoPorId(servirPedido.getIdPedido())!= null)
									{
										if(daoPedidos.confirmarMesaPedido(servirPedido.getIdPedido()).equals(numeroMesa))
											servirPedido(servirPedido);
										else
											throw new Exception("El pedido no corresponde a la mesa dada");
									}
									throw new Exception("La mesa aun tiene pedidos que no han sido servidos. Sera servido el producto con identificador " + servirPedido.getIdPedido());
								}
							}			
							if(mesaCompletada)
								throw new Exception("El pedido para la mesa se ha completado");
						}
						else 
							throw new Exception("No hay pedidos para la mesa dada");
					}
					else
						throw new Exception("La información ingresada no es válida.");
				}
				else
					throw new Exception("El usuario dado no tiene permisos para realizar esta operación.");
			}
			else
				throw new Exception("El usuario ingresado no existe en el sistema.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<ConsultaPedidos> darConsultaPedidos(Long id, String contrasenia) throws Exception {
		List<ConsultaPedidos> consulta;
		DAOTablaPedido  daoPedido = new DAOTablaPedido();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPedido.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(id)!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(id).getRol().equalsIgnoreCase("Administrador") || daoUsuarios.buscarUsuarioPorId(id).getRol().equalsIgnoreCase("Restaurante"))
				{
					if(daoUsuarios.verificarContraseniaUsuario(id).equals(contrasenia)){

						consulta = daoPedido.consultarPedidos();
					}
					else
						throw new Exception("Los datos ingresados no son válidos. Intente de nuevo.");
				}
				else
					throw new Exception("No se tiene el rol necesario para realizar esta operación.");
			}
			else
				throw new Exception("No existe un usuario con el identificador dado.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return consulta;
	}

	public List<ConsultaPedidosPorProducto> darConsultaPedidosProductos(Long id, String contrasenia) throws Exception {
		List<ConsultaPedidosPorProducto> consulta = null;
		DAOTablaPedido  daoPedido = new DAOTablaPedido();
		DAOTablaUsuario daoUsuarios= new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPedido.setConn(conn);
			daoUsuarios.setConn(conn);
			if(daoUsuarios.buscarUsuarioPorId(id)!=null)
			{
				if(daoUsuarios.buscarUsuarioPorId(id).getRol().equalsIgnoreCase("Administrador") || daoUsuarios.buscarUsuarioPorId(id).getRol().equalsIgnoreCase("Restaurante"))
				{
					if(daoUsuarios.verificarContraseniaUsuario(id).equals(contrasenia)){

						consulta = daoPedido.consultarPedidosPorProducto();
					}
					else
						throw new Exception("Los datos ingresados no son válidos. Intente de nuevo.");
				}
				else
					throw new Exception("No se tiene el rol necesario para realizar esta operación.");
			}
			else
				throw new Exception("No existe un usuario con el identificador dado.");


		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				daoUsuarios.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return consulta;
	}


	
	public void deleteEquivalencias(IngresarEquivalenciaProductos id) throws Exception {
		DAOTablaEquivalencias_Productos daoTabla = new DAOTablaEquivalencias_Productos();
		try 
		{
			this.conn = darConexion();
			daoTabla.setConn(conn);
			daoTabla.delete(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTabla.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void deleteEquivalenciasIngredientes(IngresarEquivalenciaIngredientes id) throws Exception {
		DAOTablaEquivalencias_Ingredientes daoTabla = new DAOTablaEquivalencias_Ingredientes();
		try 
		{
			this.conn = darConexion();
			daoTabla.setConn(conn);
			daoTabla.delete(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoTabla.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public ArrayList<Producto> buscarProductosConsumidosCliente(Long idCliente) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorProductosConsumidosPorCliente(idCliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}
	public ArrayList<Producto> buscarProductosConsumidosMenuCliente(Long idCliente) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorProductosMenuConsumidos(idCliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}
	public ArrayList<Producto> buscarProductosConsumidosMesaCliente(Long idCliente) throws Exception {
		ArrayList<Producto> productos;
		DAOTablaProducto daoProducto = new DAOTablaProducto();
		try 
		{
			this.conn = darConexion();
			daoProducto.setConn(conn);
			productos = daoProducto.buscarPorProductosMesaConsumidos(idCliente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProducto.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public List<Usuario> darConsumoClientes(Long idRestaurante, String fecha1, String fecha2) throws Exception  {
		ArrayList<Usuario> usuarios;
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			System.out.println(fecha1+" y "+fecha2);
			usuarios = daoUsuario.darUsuariosConsumoRestaurante(idRestaurante, fecha1, fecha2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}

	public List<Usuario> darNOConsumoClientes(Long idRestaurante, String fecha1, String fecha2) throws Exception  {
		ArrayList<Usuario> usuarios;
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			System.out.println(fecha1+" y "+fecha2);
			usuarios = daoUsuario.darUsuariosNOConsumoRestaurante(idRestaurante, fecha1, fecha2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}

	public List<ProductosDetalladosPedidos> darProductosMasMenosPedidos() throws Exception {
		List<ProductosDetalladosPedidos> productosDetallados = new ArrayList<>();

		DAOTablaPedido  daoPedido = new DAOTablaPedido();
		DAOTablaProducto  daoProducto = new DAOTablaProducto();
		DAOTablaProducto_Venta daoVenta = new DAOTablaProducto_Venta();
		DAOTablaMenu daoMenu = new DAOTablaMenu();

		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPedido.setConn(conn);
			daoProducto.setConn(conn);
			daoVenta.setConn(conn);
			daoMenu.setConn(conn);
			if(daoPedido.buscarProductosSegunCantidadPedidos()!= null)
			{
				List<ProductosPedidos> productos = daoPedido.buscarProductosSegunCantidadPedidos();
				for(int i=0; i<productos.size(); i++)
				{
					int dia= productos.get(i).getDia();
					Long id1= productos.get(i).getProductoMasPedido();
					
					Long id2= productos.get(i).getProductoMenosPedido();

					if(daoVenta.buscarProductoPorId(id1)!= null && daoVenta.buscarProductoPorId(id2)!= null)
					{
						if(daoProducto.buscarProductoPorId(id1)!= null && daoMenu.buscarMenuPorIdProductoVenta(id2)!= null)
						{						 
							ProductoMenu primero=  new ProductoMenu(daoProducto.buscarProductoPorId(id1).getNombre(), "Producto", daoVenta.buscarProductoPorId(id1));
							ProductoMenu segundo=  new ProductoMenu(daoMenu.buscarMenuPorIdProductoVenta(id2).getNombre(), "Menu", daoVenta.buscarProductoPorId(id2));

							ProductosDetalladosPedidos pDetallado	= new ProductosDetalladosPedidos(dia, primero, segundo);	
							productosDetallados.add(pDetallado);
						}
						else if(daoMenu.buscarMenuPorIdProductoVenta(id1)!= null && daoProducto.buscarProductoPorId(id2)!= null)
						{
							ProductoMenu segundo=  new ProductoMenu(daoProducto.buscarProductoPorId(id2).getNombre(), "Producto", daoVenta.buscarProductoPorId(id2));
							ProductoMenu primero=  new ProductoMenu(daoMenu.buscarMenuPorIdProductoVenta(id1).getNombre(), "Menu", daoVenta.buscarProductoPorId(id1));

							ProductosDetalladosPedidos pDetallado	= new ProductosDetalladosPedidos(dia, primero, segundo);	
							productosDetallados.add(pDetallado);

						}
						else if(daoProducto.buscarProductoPorId(id1)!= null && daoProducto.buscarProductoPorId(id2)!= null)
						{
							ProductoMenu primero=  new ProductoMenu(daoProducto.buscarProductoPorId(id1).getNombre(), "Producto", daoVenta.buscarProductoPorId(id1));
							ProductoMenu segundo=  new ProductoMenu(daoProducto.buscarProductoPorId(id2).getNombre(), "Producto", daoVenta.buscarProductoPorId(id2));

							ProductosDetalladosPedidos pDetallado	= new ProductosDetalladosPedidos(dia, primero, segundo);	
							productosDetallados.add(pDetallado);

						}				   
						else if(daoMenu.buscarMenuPorIdProductoVenta(id1)!= null && daoMenu.buscarMenuPorIdProductoVenta(id2)!= null)
						{
							ProductoMenu primero=  new ProductoMenu(daoMenu.buscarMenuPorIdProductoVenta(id1).getNombre(), "Menu", daoVenta.buscarProductoPorId(id1));
							ProductoMenu segundo=  new ProductoMenu(daoMenu.buscarMenuPorIdProductoVenta(id2).getNombre(), "Menu", daoVenta.buscarProductoPorId(id2));

							ProductosDetalladosPedidos pDetallado	= new ProductosDetalladosPedidos(dia, primero, segundo);	
							productosDetallados.add(pDetallado);

						} 
					}
					else
					{
						throw new Exception("Los identificadores no se encuentran registrados en los productos venta");
					}

				}

			}
			else 
				throw new Exception("No hay información para dar los resultados");

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				daoProducto.cerrarRecursos();
				daoVenta.cerrarRecursos();
				daoMenu.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productosDetallados;
	}
	
	public List<RestaurantesFrecuentados> darRestaurantesFrecuentes() throws Exception {
		List<RestaurantesFrecuentados> restaurantes = new ArrayList<>();

		DAOTablaPedido  daoPedido = new DAOTablaPedido();
		DAOTablaRestaurante daoRestaurante = new DAOTablaRestaurante();

		try 
		{
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoPedido.setConn(conn);
			daoRestaurante.setConn(conn);
			
			if(daoPedido.buscarRestaurantesSolicitados()!= null)
			{
				List<RestaurantesFrecuentadosDia> restaurantesFrecuentados = daoPedido.buscarRestaurantesSolicitados();
				
				for(int i=0; i<restaurantesFrecuentados.size(); i++)
				{
					int dia= restaurantesFrecuentados.get(i).getDia();
					Long id1= restaurantesFrecuentados.get(i).getRestauranteMasFrecuentado();
					
					Long id2= restaurantesFrecuentados.get(i).getRestauranteMenosFrecuentado();

					if(daoRestaurante.buscarRestaurantePorNit(id1)!= null && daoRestaurante.buscarRestaurantePorNit(id2)!= null)
					{					 
						
							Restaurante primero=  daoRestaurante.buscarRestaurantePorNit(id1);
							Restaurante segundo=  daoRestaurante.buscarRestaurantePorNit(id2);;

							RestaurantesFrecuentados pDetallado	= new RestaurantesFrecuentados(dia, primero, segundo);	
							restaurantes.add(pDetallado);												
					}
					else
					{
						throw new Exception("Los identificadores no se encuentran registrados en los restaurantes");
					}

				}

			}
			else 
				throw new Exception("No hay información para dar los resultados");

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				daoRestaurante.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}
	
	public List<Usuario> darBuenosClientes() throws Exception  {
		ArrayList<Usuario> usuarios;
		DAOTablaUsuario daoUsuario = new DAOTablaUsuario();
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuarios = daoUsuario.darBuenosClientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}
	
	public List<RentabilidadRestaurante> darRentabilidadRestauranteCategoria(Long idCategoria, String fecha1, String fecha2) throws Exception  {
		ArrayList<RentabilidadRestaurante> rentabilidad;
		DAOTablaPedido daoPedido = new DAOTablaPedido();
		try 
		{
			this.conn = darConexion();
			daoPedido.setConn(conn);
			System.out.println(fecha1+" y "+fecha2);
			rentabilidad = daoPedido.buscarRentabilidadPorCategoria(idCategoria, fecha1, fecha2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return rentabilidad;
	}
	
	public List<RentabilidadRestaurante> darRentabilidadRestauranteZona(Long idCategoria, String fecha1, String fecha2) throws Exception  {
		ArrayList<RentabilidadRestaurante> rentabilidad;
		DAOTablaPedido daoPedido = new DAOTablaPedido();
		try 
		{
			this.conn = darConexion();
			daoPedido.setConn(conn);
			System.out.println(fecha1+" y "+fecha2);
			rentabilidad = daoPedido.buscarRentabilidadPorZona(idCategoria, fecha1, fecha2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return rentabilidad;
	}
	
	public List<RentabilidadRestaurante> darRentabilidadRestauranteProducto(Long idProducto, String fecha1, String fecha2) throws Exception  {
		ArrayList<RentabilidadRestaurante> rentabilidad;
		DAOTablaPedido daoPedido = new DAOTablaPedido();
		try 
		{
			this.conn = darConexion();
			daoPedido.setConn(conn);
			System.out.println(fecha1+" y "+fecha2);
			rentabilidad = daoPedido.buscarRentabilidadPorProducto(idProducto, fecha1, fecha2);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedido.cerrarRecursos();
				if(this.conn!=null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return rentabilidad;
	}
}
