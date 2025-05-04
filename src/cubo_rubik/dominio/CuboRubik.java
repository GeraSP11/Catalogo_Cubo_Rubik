/*
 * @author GERARDO CORTEZ RAMOS
 *
 */
package cubo_rubik.dominio;

import java.util.Date;

import cubo_rubik.excepciones.RubiksWarningsException;

import java.util.ArrayList;

public class CuboRubik {
	// 1. N�MERO LIBRE
	private int caras;
	// 2. N�MERO CON RANGO
    private float precio; 
    // 3. TEXTO LIBRE
    private String nomCubo;
    // 4. TEXTO CON FORMATO
    private String modelo; 
    // 5. FECHA
    private Date fechaCreaci�n;
    // 6. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES FIJAS: PL�STICO ABS, MADERA, METAL 
    private String material;
    // 7. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES DIN�MICAS.
    private String marca;
    // 8. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES FIJAS:
    //C�BICOS, CUBOIDES, MORPHIX, MINX, SKEWB, SQUARE, CURVY, ENGRANAJES, MIXUP.
    private ArrayList<String> mecanismos;
    // 9. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES DIN�MICAS. STICKERS
    private ArrayList<String> stickers;
    // 10. RUTA DE IMAGEN
    private String rutaImagen; 
    // IDENTIFICADOR DEL REGISTRO
    private int id;
    
    // CONSTRUCTOR SIN PAR�METROS CON VALORES POR DEFECTOS 
    public CuboRubik() {
        this.caras = 0;
        this.precio = 0.0F;
        this.nomCubo = "";
        this.modelo = "";
        this.fechaCreaci�n = null;
        this.material = "";
        this.marca = "";
        this.mecanismos = null;
        this.stickers = null;
        this.rutaImagen = "";
        this.id = 0;
    }

    // M�TODOS GET POR DEFECTO PARA CADA VARIABLE
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

    public Date getFechaCreaci�n() {
        return fechaCreaci�n;
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

    // M�TODOS SET PARA VARIABLES 1 Y 2
    public void setCaras(String caras) throws RubiksWarningsException {
    	try{
    		int carasInt = Integer.parseInt(caras.trim());
        	setCaras(carasInt);
    	} catch (NumberFormatException e) {
    		throw new RubiksWarningsException(RubiksWarningsException.CARAS_FORMATO);
    	}

    }

    public void setCaras(int caras) throws RubiksWarningsException{
    	if (caras > 3 && caras < 13) {
    		this.caras = caras;
    	} else {
    		throw new RubiksWarningsException(RubiksWarningsException.CARAS_RANGO);
    	}
    }

    public void setPrecio(String precio) throws RubiksWarningsException{
    	try{
    		float precioFloat = Float.parseFloat(precio.trim());
    		setPrecio(precioFloat);
    	} catch (NumberFormatException e) {
    		throw new RubiksWarningsException(RubiksWarningsException.PRECIO_FORMATO);
    	}
    }

    public void setPrecio(float precio) throws RubiksWarningsException{
        // VALIDACI�N DEL RANGO DE PRECIOS
        if (precio < 50.00F || precio > 1500.00F) {
            throw new RubiksWarningsException(RubiksWarningsException.PRECIO_RANGO);
        }
        this.precio = precio;
    }

    // M�TODO SET PARA VARIABLE 3
    public void setNomCubo(String nomCubo) throws RubiksWarningsException{
        // VALIDAR QUE EL NOMBRE NO ESTE VAC�O
        if (nomCubo.trim().isEmpty()) {
            throw new RubiksWarningsException(RubiksWarningsException.NOMBRE_OBLIGATORIO);
        }
        this.nomCubo = nomCubo.trim();
    }

    // M�TODO SET PARA VARIABLE 4
    public void setModelo(String modelo) throws RubiksWarningsException{
        // ELIMINA ESPACIOS DE LOS EXTREMOS
    	modelo = modelo.trim();
    	// FORMATO VALIDO : nxn o nxnxn, DONDE n ES UN N�MERO MAY�R O IGUAL 1 Y MENOR O IGUAL A 7
        if(!modelo.isEmpty()) {
            if (modelo.matches("^[1-7]x[1-7](x[1-7])?$")) {
            	this.modelo = modelo;
            } else {
            	throw new RubiksWarningsException(RubiksWarningsException.MODELO_FORMATO);
            }
        } else {
        	throw new RubiksWarningsException(RubiksWarningsException.MODELO_OBLIGATORIO);
        }
        
    }

    // M�TODOS SET PARA VARIABLES 5-10
    public void setFechaCreaci�n(Date fechaCreaci�n) {
        this.fechaCreaci�n = fechaCreaci�n;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setMarca(String marca) throws RubiksWarningsException{
    	if(marca.trim().isEmpty()) {
    		throw new RubiksWarningsException(RubiksWarningsException.MARCA_VACIA);
    	}
        this.marca = marca;
    }

    public void setMecanismos(ArrayList<String> mecanismos) throws RubiksWarningsException{
    	if(mecanismos.isEmpty()) {
    		throw new RubiksWarningsException(RubiksWarningsException.MECANISMO_OBLIGATORIO);
    	}
        this.mecanismos = mecanismos;
    }

    public void setStikers(ArrayList<String> stikers) {
        this.stickers = stikers;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// M�TODO toString
    public String toString() {
        return nomCubo + " " + modelo;
    }
    
}