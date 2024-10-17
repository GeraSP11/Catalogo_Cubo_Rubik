/**
 * 
 */
/**
 * @author GERARDO CORTEZ RAMOS
 *
 */
package cubo_rubik.dominio;

import java.util.Date;
import java.util.ArrayList;

public class CuboRubik {
	// 1. NÚMERO LIBRE
	private int caras;
	// 2. NÚMERO CON RANGO
    private float precio; 
    // 3. TEXTO LIBRE
    private String nomCubo;
    // 4. TEXTO CON FORMATO
    private String modelo; 
    // 5. FECHA
    private Date fechaCreación;
    // 6. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES FIJAS: PLÁSTICO ABS, MADERA, METAL 
    private String material;
    // 7. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES DINÁMICAS.
    private String marca;
    // 8. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES FIJAS:
    //CÚBICOS, CUBOIDES, MORPHIX, MINX, SKEWB, SQUARE, CURVY, ENGRANAJES, MIXUP.
    private ArrayList<String> mecanismos;
    // 9. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES DINÁMICAS.
    private ArrayList<String> stickers;
    // 10. RUTA DE IMAGEN
    private String rutaImagen; 
    
    // CONSTRUCTOR SIN PARÁMETROS CON VALORES POR DEFECTOS 
    public CuboRubik() {
        this.caras = 0;
        this.precio = 0.0F;
        this.nomCubo = "";
        this.modelo = "";
        this.fechaCreación = null;
        this.material = "";
        this.marca = "";
        this.mecanismos = null;
        this.stickers = null;
        this.rutaImagen = "";
    }

    // MÉTODOS GET POR DEFECTO PARA CADA VARIABLE
    public int getCaras() {
        return caras;
    }

    public float getPrecio() {
        return precio;
    }

    public String getNomCubo() {
        return nomCubo;
    }

    public String getModelo() {
        return modelo;
    }

    public Date getFechaCreación() {
        return fechaCreación;
    }

    public String getMaterial() {
        return material;
    }

    public String getMarca() {
        return marca;
    }

    public ArrayList<String> getMecanismos() {
        return mecanismos;
    }

    public ArrayList<String> getStikers() {
        return stickers;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    // MÉTODOS SET PARA VARIABLES 1 Y 2
    public void setCaras(String caras) {
    	int carasInt = Integer.parseInt(caras.trim());
    	setCaras(carasInt);

    }

    public void setCaras(int caras) {
    	if (caras > 3) {
    		this.caras = caras;
    	} else {
    		throw new IllegalArgumentException();
    	}
    }

    public void setPrecio(String precio) {
    	float precioFloat = Float.parseFloat(precio.trim());
    	setPrecio(precioFloat);
    }

    public void setPrecio(float precio) {
        // VALIDACIÓN DEL RANGO DE PRECIOS
        if (precio < 50.00F || precio > 1500.00F) {
            throw new IllegalArgumentException();
        }
        this.precio = precio;
    }

    // MÉTODO SET PARA VARIABLE 3
    public void setNomCubo(String nomCubo) {
        // VALIDAR QUE EL NOMBRE NO ESTE VACÍO
        if (nomCubo.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.nomCubo = nomCubo.trim();
    }

    // MÉTODO SET PARA VARIABLE 4
    public void setModelo(String modelo) {
        // ELIMINA ESPACIOS DE LOS EXTREMOS
    	modelo = modelo.trim();
    	// FORMATO VALIDO : nxn o nxnxn, DONDE n ES UN NÚMERO MAYÓR O IGUAL 1 Y MENOR O IGUAL A 5
        if (modelo.matches("^[1-5]x[1-5](x[1-5])?$")) {
        	this.modelo = modelo;
        } else {
        	throw new IllegalArgumentException();
        }
        
    }

    // MÉTODOS SET PARA VARIABLES 5-10
    public void setFechaCreación(Date fechaCreación) {
        this.fechaCreación = fechaCreación;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setMecanismos(ArrayList<String> mecanismos) {
        this.mecanismos = mecanismos;
    }

    public void setStikers(ArrayList<String> stikers) {
        this.stickers = stikers;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    // MÉTODO toString
    public String toString() {
        return nomCubo + " " + modelo;
    }

}