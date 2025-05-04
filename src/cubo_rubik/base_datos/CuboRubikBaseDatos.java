package cubo_rubik.base_datos;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cubo_rubik.dominio.CuboRubik;
import cubo_rubik.excepciones.BaseDatosException;
import cubo_rubik.excepciones.RubiksWarningsException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class CuboRubikBaseDatos extends BaseDatos {
	
	public CuboRubikBaseDatos() throws BaseDatosException {
		if (!validarBD()) {
			crearBD();
			llenarTablas();
		}
	}

	// VALIDAR QUE LA BASE DE DATOS SEA LA CORRECTA
	private boolean validarBD() throws BaseDatosException {
		realizarConexion();
		ArrayList<String> tablas = new ArrayList<>();
		tablas = consultarTablas();
		cerrarConexion();
		if (tablas.size() > 0) {
			for (String auxTablas : tablas) {
				if (auxTablas.compareTo("cubos_rubik") == 0) {
					return true;
				}
			}
			throw new BaseDatosException(BaseDatosException.NO_ES_LA_BASE);
		} else {
			return false;
		}
	}

	// CREAR LAS TABLAS DE LA BASE DE DATOS
	private void crearBD() throws BaseDatosException {
		try {
			realizarConexion();
			// CREAR TABLA MARCAS
			String accion = "CREATE TABLE marcas(" + "idMarca INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ "nombreMarca TEXT COLLATE NOCASE)";
			realizarAccion(accion);
			// CREAR TABLA PARA LAS IMAGENES DE LOS CUBOS
			accion = "CREATE TABLE cuboImagenes(idCubo INTEGER, "
					+ "imagen BLOB, " + "extension TEXT COLLATE NOCASE)";
			realizarAccion(accion);
			// CREAR TABLA STICKERS
			accion = "CREATE TABLE stickers(" + "idSticker INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ "nombreSticker TEXT COLLATE NOCASE)";
			realizarAccion(accion);
			// CREAR TABLA DE LOS CUBOS
			accion = "CREATE TABLE cubos_rubik("
					+ "idCubo INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ "nombreCubo TEXT COLLATE NOCASE, " 
					+ "modeloCubo TEXT COLLATE NOCASE, "
					+ "carasCubo INTEGER NOT NULL, " 
					+ "fechaCreacion INTEGER NOT NULL, " 
					+ "idMarca INTEGER NOT NULL, "
					+ "materialCubo TEXT COLLATE NOCASE, " 
					+ "mecanismosCubo TEXT COLLATE NOCASE, "
					+ "precioCubo REAL NOT NULL, "
					+ "FOREIGN KEY(idMarca) REFERENCES marcas(idMarca) ON DELETE CASCADE ON UPDATE CASCADE)";
			realizarAccion(accion);
			// CREAR TABLA DE ESTICKERS DEL CUBO
			accion = "CREATE TABLE cuboStickers(" + "idCubo INTEGER, " + "idSticker INTEGER, "
					+ "FOREIGN KEY(idCubo) REFERENCES cubos_rubik(idCubo) ON DELETE CASCADE ON UPDATE CASCADE, "
					+ "FOREIGN KEY(idSticker) REFERENCES stickers(idSticker) ON DELETE CASCADE ON UPDATE CASCADE)";
			realizarAccion(accion);
			cerrarConexion();
		} catch (BaseDatosException e) {
			throw new BaseDatosException(BaseDatosException.NO_SE_PUDO_CREAR_LA_TABLA);
		}
	}

	// INSERTAR REGISTROS INICIALES EN LAS TABLAS DE MARCAS Y STICKERS
	private void llenarTablas() throws BaseDatosException {
		realizarConexion();

		// INSERTAR MARCAS
		String[] marcas = { "Rubik´s", "GAN", "MoYu", "QiYi", "YJ (YoungJun)", "Yunix", "Dayan", "Moyu´s sub-brands",
				"MeiLong", "ShengShou" };
		for (String marca : marcas) {
			realizarAccion("INSERT INTO marcas VALUES(null, '" + marca + "')");
		}

		// INSERTAR STICKERS
		String[] stickers = { "Candy", "Fibra de carbono", "Vinilo", "Texturizado", "Metálicos", "Fluorescentes",
				"Personalizados" };
		for (String sticker : stickers) {
			realizarAccion("INSERT INTO stickers VALUES(null, '" + sticker + "')");
		}

		cerrarConexion();
	}

	// OBTENER CUBOS DE LA BASE DE DATOS
	public ArrayList<CuboRubik> consultarCubos() throws BaseDatosException, RubiksWarningsException {
		realizarConexion();
		ArrayList<CuboRubik> cubos = new ArrayList<>();
		String consulta = "SELECT idCubo, nombreCubo, modeloCubo, carasCubo, "
				+ "fechaCreacion, idMarca, materialCubo, mecanismosCubo, "
				+ "precioCubo FROM cubos_rubik ORDER BY nombreCubo ASC, carasCubo ASC";
		try (ResultSet resultado = realizarConsulta(consulta)) {
			while (resultado.next()) {

				CuboRubik cubo = new CuboRubik();

				cubo.setId(resultado.getInt("idCubo"));

				cubo.setNomCubo(resultado.getString("nombreCubo"));

				cubo.setModelo(resultado.getString("modeloCubo"));

				cubo.setCaras(resultado.getInt("carasCubo"));

				long longFecha = resultado.getLong("fechaCreacion");
				Date fecha = new Date(longFecha);
				cubo.setFechaCreación(fecha);
				
				ResultSet consultaMarca = realizarConsulta(
						"SELECT nombreMarca FROM marcas WHERE idMarca = " + resultado.getInt("idMarca"));
				consultaMarca.next();
				cubo.setMarca(consultaMarca.getString(1));

				cubo.setMaterial(resultado.getString("materialCubo"));
				
				ArrayList<String> listaStickers = new ArrayList<>();
				ResultSet consultaStickers = realizarConsulta(
						"SELECT idSticker FROM cuboStickers WHERE idCubo = " + resultado.getInt("idCubo"));
				while (consultaStickers.next()) {
					ResultSet stickerConsultado = realizarConsulta(
							"SELECT nombreSticker FROM stickers WHERE idSticker = "
									+ consultaStickers.getInt("idSticker"));
					stickerConsultado.next();
					listaStickers.add(stickerConsultado.getString("nombreSticker"));
				}
				cubo.setStikers(listaStickers);

				String mecanismos = resultado.getString("mecanismosCubo");
				String[] mecanismosLista = mecanismos.split(",");
				ArrayList<String> arregloMecanismos = new ArrayList<>(Arrays.asList(mecanismosLista));
				cubo.setMecanismos(arregloMecanismos);
				
				String extension = "";
				ResultSet consultaImagen = realizarConsulta(
						"SELECT extension FROM cuboImagenes WHERE idCubo = " + cubo.getId());
				if (consultaImagen.next()){
					extension = consultaImagen.getString("extension");
					if (!extension.trim().isEmpty() || extension != null) {
						cubo.setRutaImagen(cubo.getId() + extension);
					}
				}


				cubo.setPrecio(resultado.getFloat("precioCubo"));
				
				cubos.add(cubo);
			}
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_CONSULTA_CUBOS);
		}
		cerrarConexion();
		return cubos;
	}

	// OBTENER MARCAS DE LA BASE DE DATOS
	public ArrayList<String> consultarMarcas() throws BaseDatosException {
		realizarConexion();
		ArrayList<String> marcas = new ArrayList<>();
		String consulta = "SELECT nombreMarca FROM marcas ORDER BY nombreMarca ASC";
		try (ResultSet resultado = realizarConsulta(consulta)) {
			while (resultado.next()) {
				marcas.add(resultado.getString(1));
			}
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_CONSULTA_MARCAS);
		}
		cerrarConexion();
		return marcas;
	}

	// OBTENER STICKERS DE LA BASE DE DATOS
	public ArrayList<String> consultarStickers() throws BaseDatosException {
		realizarConexion();
		ArrayList<String> stickers = new ArrayList<>();
		String consulta = "SELECT nombreSticker FROM stickers ORDER BY nombreSticker ASC";
		try (ResultSet resultado = realizarConsulta(consulta)) {
			while (resultado.next()) {
				stickers.add(resultado.getString(1));
			}
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_CONSULTA_STICKERS);
		}
		cerrarConexion();
		return stickers;
	}

	// INSERTAR UN CUBO EN LA BASE DE DATOS
	public int manejarCubo(CuboRubik cubo, String accion, int idMarca) throws BaseDatosException {
		realizarConexion();
		int idCubo = 0;
		try {
			if (accion.equalsIgnoreCase("INSERT") || accion.equalsIgnoreCase("UPDATE")) {
				if (accion.equalsIgnoreCase("UPDATE")) {
					eliminarCubo(cubo.getId(), cubo.getRutaImagen());
				}
				long fecha = cubo.getFechaCreación().getTime();
				String mecanismos = String.join(",", cubo.getMecanismos());
				String sql = "INSERT INTO cubos_rubik VALUES(null, '" 
						+ cubo.getNomCubo() + "', '" 
						+ cubo.getModelo() + "', " 
						+ cubo.getCaras() + ", " 
						+ fecha + ", " 
						+ idMarca + ", '" 
						+ cubo.getMaterial() + "', '"
						+ mecanismos + "', " 
						+ cubo.getPrecio() + ")";
				realizarAccion(sql);
				idCubo = ultimoIdCubo();
				this.stickersParaElCubo(idCubo, cubo.getStikers());
			} else if (accion.equalsIgnoreCase("DELETE")) {
				try{
					eliminarCubo(cubo.getId(), cubo.getRutaImagen());
				} catch (BaseDatosException e) {
					throw new BaseDatosException(BaseDatosException.ERROR_EN_ELIMINAR_CUBO);
				}
			}
		} catch (BaseDatosException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_INSERTAR_CUBO);
		}
		cerrarConexion();
		return idCubo;
	}

	// METODO AUXILIAR PARA ELIMINAR EL CUBO DE LA BASE DE DATOS
	private void eliminarCubo(int idCubo, String ruta) throws BaseDatosException {
		realizarAccion("DELETE FROM cubos_rubik WHERE idCubo = " + idCubo);
		realizarAccion("DELETE FROM cuboStickers WHERE idCubo = " + idCubo);
		realizarAccion("DELETE FROM cuboImagenes WHERE idCubo = " + idCubo);
		System.out.println(ruta);
		File file = new File("imagen/"+ruta);
		if (file.exists()) {
			file.delete();
			System.out.println("Imagen eliminada");
		}
	}

	// METODO AUXILIAR PARA CONSULTAR ID DE LA MARCA
	public int consultarIdMarca(String marca) throws BaseDatosException {
		realizarConexion();
		int id = 0;
		try (ResultSet consultaID = realizarConsulta(
				"SELECT IdMarca FROM marcas WHERE nombreMarca = '" + marca + "'")) {
			consultaID.next();
			id = consultaID.getInt(1);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_BUSCAR_ID);
		}
		cerrarConexion();
		return id;
	}

	// INSERTAR UNA MARCA
	public void agregarMarca(String marca) throws BaseDatosException {
		realizarConexion();
		String sql = "INSERT INTO marcas VALUES(null,'" + marca + "')";
		try {
			realizarAccion(sql);
		} catch (BaseDatosException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_AGREGAR_MARCA);
		}
		cerrarConexion();
	}

	// METODO AUXILIAR PARA GUARDAR LOS REGISTROS DE STICKERS QUE LE PERTENECEN A UN
	// CUBO
	private void stickersParaElCubo(int idCubo, ArrayList<String> stickers) throws BaseDatosException {
		int idSticker = 0;
		for (String sticker : stickers) {
			idSticker = this.consultarIdSticker(sticker);
			realizarAccion("INSERT INTO cuboStickers VALUES(" + idCubo + "," + idSticker + ")");
		}
	}

	// METODO AUXILIAR PARA CONSULTAR ID DE UN STICKER
	private int consultarIdSticker(String sticker) throws BaseDatosException {
		int id = 0;
		try (ResultSet consultaID = realizarConsulta(
				"SELECT IdSticker FROM stickers WHERE nombreSticker = '" + sticker + "'")) {
			if (consultaID.next()){
				id = consultaID.getInt(1);
			}
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_BUSCAR_ID);
		}
		return id;
	}
	
	// METODO AUXILIAR PARA CONSULTAR ID DE UN STICKER DESDE OTRA CLASE
	public int consultarIdStickerAUX(String sticker) throws BaseDatosException {
		realizarConexion();
		int id = 0;
		try (ResultSet consultaID = realizarConsulta(
				"SELECT IdSticker FROM stickers WHERE nombreSticker = '" + sticker + "'")) {
			if (consultaID.next()){
				id = consultaID.getInt(1);
			}
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_BUSCAR_ID);
		}
		cerrarConexion();
		return id;
	}

	// METODO PARA GUARDAR IMAGENES EN LA BASE DE DATOS
	public void guardarImagen(String ruta, int idCubo) throws BaseDatosException {
		realizarConexion();
		String extension = ruta;
		extension = ruta.substring(ruta.lastIndexOf('.'));
		FileInputStream input = null;
		ByteArrayOutputStream output = null;
		String sql = "INSERT INTO cuboImagenes(idCubo, imagen, extension) VALUES( ?, ?, ?)";
		try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
			input = new FileInputStream(new File(ruta));
			byte[] buffer = new byte[1024];
			output = new ByteArrayOutputStream();
			int len = input.read(buffer);
			while (len != -1) {
				output.write(buffer, 0, len);
				len = input.read(buffer);
			}
			byte[] bytes = output.toByteArray();
			stmt.setInt(1, idCubo);
			stmt.setBytes(2, bytes);
			stmt.setString(3, extension);
			stmt.executeUpdate();
		} catch (SQLException | IOException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_GUARDAR_IMAGEN);
		}  finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		cerrarConexion();
	}

	// METODO PARA RECUPERAR UNA IMAGEN
	public void recuperarImagen(int idCubo, String ruta) throws BaseDatosException {
		realizarConexion();
		InputStream input = null;
		FileOutputStream output = null;
		String sql = "SELECT imagen FROM cuboImagenes WHERE idCubo = " + idCubo;
		try {
			ResultSet rs = realizarConsulta(sql);
			File file = new File(ruta);
			output = new FileOutputStream(file);

			if (rs.next()) {
				input = rs.getBinaryStream("imagen");
				byte[] buffer = new byte[1024];
				int len = input.read(buffer);
				while (len != -1) {
					output.write(buffer, 0, len);
					len = input.read(buffer);
				}
			}
		} catch (SQLException | IOException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_RECUPERAR_IMAGEN);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		cerrarConexion();
	}

	// INSERTAR UN STICKER EN LA BASE DE DATOS
	public int insertarSticker(String sticker) throws BaseDatosException {
		realizarConexion();
		int resultado = 0;
		try {
			resultado = realizarAccion("INSERT INTO stickers VALUES(null, '" + sticker + "')");
		} catch (BaseDatosException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_INSERTAR_STICKER);
		}
		cerrarConexion();
		return resultado;
	}

	// METODO AUXILIAR CONSULTAR EL ID DEL ULTIMO CUBO AGREGADO
	public int ultimoIdCubo() throws BaseDatosException {
		String sql = "SELECT last_insert_rowid()";
		int id = 0;
		try (ResultSet res = realizarConsulta(sql)) {
			res.next();
			id = res.getInt(1);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_BUSCAR_ID);
		}
		return id;
	}
	
	// METODO PARA GENERAR EL REPORTE PDF
	public void crearReporte(String ruta) throws BaseDatosException {
		realizarConexion();
		try {
			String jasper = "ReporteCubosRubik.jasper";
			JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(jasper);
			Map<String, Object> parametros = new HashMap<String,Object>();
			DateFormat formatoFecha = DateFormat.getDateInstance(DateFormat.LONG);
			parametros.put("fecha", formatoFecha.format(new Date()));
			parametros.put("nombre", "Cubos de Rubik");
			JasperPrint reporteLleno = JasperFillManager.fillReport(reporte, parametros, conexion);
			JasperExportManager.exportReportToPdfFile(reporteLleno, ruta);
			Desktop.getDesktop().open(new File(ruta));
		} catch (JRException | IOException e) {
			System.out.println("reporte " + e.getMessage());
		}
		cerrarConexion();
	}
}