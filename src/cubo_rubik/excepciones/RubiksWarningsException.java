/*
 * @author Gera
 * CLASE DE EXCEPCIONES
 *
 */
package cubo_rubik.excepciones;

public class RubiksWarningsException extends Exception {

	private static final long serialVersionUID = 1L;

	// MENSAJES PARA EL NÚMERO DE CARAS
	public static final String[] CARAS_FORMATO = {
			"El número de caras debe de ser un entero.\n"
			+ "Evite ingresar caracteres no numericos\n"
			+ "y no dejar el campo vacío o en blanco.", 
			"Formato de caras inválido" };

	public static final String[] CARAS_RANGO = { 
			"El número de caras debe de ser un número entero positivo,\n"
			+ "entre un rango de 4 a 12.",
			"Número de caras inválido" };
	
	// MENSAJES PARA EL PRECIO
	public static final String[] PRECIO_FORMATO = {
			"El precio del cubo debe ser un número decimal.\n"
			+ "Ejemplo: 75.30, 1250.70, 150.00.",
			"Formato de precio inválido"};
	
	public static final String[] PRECIO_RANGO = {
			"El precio del cubo debe estar en el rango de.\n"
			+ "entre $50.00 y $1,500.00",
			"Precio del cubo fuera de rango"};
	
	// MENASJE PARA EL NOMBRE
	public static final String[] NOMBRE_OBLIGATORIO = {
			"El nombre del cubo es obligatorio,\n"
			+ "por favor no deje el campo vacío o en blanco.",
			"Nombre del cubo obligatorio"};
	
	// MENSAJE PARA EL MODELO
	public static final String[] MODELO_OBLIGATORIO = {
			"El modelo del cubo es obligatorio,\n"
			+ "por favor no deje el campo vacío o en blanco.",
			"Modelo del cubo obligatorio"};
	
	public static final String[] MODELO_FORMATO = {
			"El formato del modelo es incorrecto,\n"
			+ "ingrese el modelo con el formato: nxn o nxnxn.\n"
			+ "Donde n debe ser un número del 1 al 7.",
			"Formato del modelo inválido"};

	// MENSAJE PARA LA MARCA
	public static final String[] MARCA_VACIA = {
			"Por favor seleccione o escriba una nueva marca,\n"
			+ "no deje el campo vacío.",
			"Marca vacía"};
	
	// MENSAJE PARA EL MECANISMO
	public static final String[] MECANISMO_OBLIGATORIO = {
			"Por favor seleccione al menos un mecanismo.",
			"Mecanismo obligatorio"};
	
	public String titulo;

	public RubiksWarningsException(String[] msg) {
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