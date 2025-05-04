 package cubo_rubik.base_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cubo_rubik.excepciones.BaseDatosException;

public class BaseDatos {
	protected Connection conexion;
	private final String CONTROLADOR = "org.sqlite.JDBC";
	private final String URL = "jdbc:sqlite:" + "CubosRubik.db";

	protected BaseDatos() {}

	protected void realizarConexion() throws BaseDatosException {
		try {
			Class.forName(CONTROLADOR);
			this.conexion = DriverManager.getConnection(this.URL);
		} catch (ClassNotFoundException e) {
			throw new BaseDatosException(BaseDatosException.NO_SE_ENCONTRO_DRIVER);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.BD_NO_ENCONTRADA);
		}
	}

	protected void cerrarConexion() throws BaseDatosException {
		try {
			this.conexion.close();
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.DESCONEXION);
		}
	}

	protected ResultSet realizarConsulta(String consulta) throws BaseDatosException {
		try {
			Statement instruccion;
			instruccion = this.conexion.createStatement();
			return instruccion.executeQuery(consulta);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_CONSULTA);
		}
	}

	protected int realizarAccion(String accion) throws BaseDatosException {
		try {
			Statement instruccion;
			instruccion = this.conexion.createStatement();
			return instruccion.executeUpdate(accion);
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.ERROR_EN_ACCION);
		}
	}
	
	// METODO PARA CONSULTAR LAS TABLAS DE LA BASE DE DATOS
	protected ArrayList<String> consultarTablas() throws BaseDatosException {
		try {
			String[] tipoTabla = { "TABLE" };
			ResultSet nomTablas;
			nomTablas = this.conexion.getMetaData().getTables(null, null, null, tipoTabla);
			ArrayList<String> tablas = new ArrayList<>();
			while (nomTablas.next()) {
				tablas.add(nomTablas.getString(3));
			}
			return tablas;
		} catch (SQLException e) {
			throw new BaseDatosException(BaseDatosException.PROBLEMA_BD);
		}
	}

	public String getCONTROLADOR() {
		return this.CONTROLADOR;
	}

	public String getUrl() {
		return this.URL;
	}
}
