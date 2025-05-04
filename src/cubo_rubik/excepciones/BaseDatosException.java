package cubo_rubik.excepciones;

public class BaseDatosException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	// MENSAJES PARA PROBLEMAS GENERALES
	public static final String[] BD_NO_ENCONTRADA = {
			"La base de datos del sistema no se pudo encontrar.",
			"Base de datos no encontrada"};
	
	public static final String[] NO_SE_ENCONTRO_DRIVER = {
			"No se encontraron los controladores para la conexión de la base de datos.",
			"Controladores no encontrados"};
	
	public static final String[] DESCONEXION = {
			"Ya se ha desconectado de la base de datos.",
			"Base de datos desconectada"};
	
	public static final String[] ERROR_EN_CONSULTA = {
			"La consulta no se pudo realizar en la base de datos del sistema.",
			"Consulta fallida"};
	
	public static final String[] ERROR_EN_ACCION = {
			"La operación no se pudo realizar en la base de datos del sistema.",
			"Accion fallida"};
	
	public static final String[] NO_SE_PUDO_CREAR_LA_TABLA = {
			"Las tablas de la base de datos no se lograron crear.",
			"Creacion de tablas fallida"};
	
	public static final String[] NO_ES_LA_BASE = {
			"La base de datos a la que se conectó no tiene los datos del sistema.",
			"Base de datos incorrecta"};
	
	public static final String[] PROBLEMA_BD = {
			"La base de datos no se puede usar.",
			"Base de datos ocupada"};
	
	// MENSAJES PARA LAS CONSULTAS
	public static final String[] ERROR_EN_CONSULTA_MARCAS = {
			"La consulta de marcas no se pudo realizar.",
			"Consultar marcas"};
	
	public static final String[] ERROR_EN_CONSULTA_STICKERS = {
			"La consulta de stickers no se pudo realizar.",
			"Consultar stickers"};
	
	public static final String[] ERROR_EN_CONSULTA_CUBOS = {
			"La consulta de cubos no se pudo realizar.",
			"Consultar cubos"};
	
	public static final String[] ERROR_EN_BUSCAR_ID = {
			"No se logro encontrar el elemento necesario.",
			"Consulta fallida"};
	
	// MENSAJES PARA ACCIONES
	public static final String[] ERROR_EN_INSERTAR_CUBO = {
			"No fue posible agregar el cubo a la base de datos.",
			"Error al agregar el cubo a la base de datos"};
	
	public static final String[] ERROR_EN_ELIMINAR_CUBO = {
			"No fue posible eliminar el cubo a la base de datos.",
			"Error al eliminar cubo"};
	
	public static final String[] ERROR_EN_INSERTAR_STICKER = {
			"No se pudo agregar el nuevo sticker, por favor intente de nuevo.",
			"Error en la base de datos al agregar sticker"};
	
	public static final String[] ERROR_EN_AGREGAR_MARCA = {
			"No se pudo agregar la marca a la base de datos.",
			"Error en la base de datos al agregar la marca"};
	
	public static final String[] ERROR_EN_GUARDAR_IMAGEN = {
			"No se pudo guardar la imagen en la base de datos.",
			"Error guardar imagen"};
	
	public static final String[] ERROR_EN_RECUPERAR_IMAGEN = {
			"Ocurrio un problema la recuperar la imagen de la base de datos.",
			"Recuperar imagen"};
	
	public String titulo;
	
	public BaseDatosException(String[] msg) {
		super(msg[0]);
		setTitulo(msg[1]);
	}
	
	public String getTitulo() {
		return titulo;
	}

	private void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}

