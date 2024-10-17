package cubo_rubik.gui;

// IMPORTA LIBRERIAS UTILIZADAS
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import cubo_rubik.dominio.CuboRubik;
import cubo_rubik.utilerias.MiFocusTraversalPolicy;

// CLASE DIALOGO DE LA VENTANA DE CONSULTA 
public class DialogoConsultasRubik extends JDialog implements ItemListener{
	
	private static final long serialVersionUID = 1L;
	
	// VARIABLES DEL MENU SUPERIOR IZQUIERDO
	private JMenu operacionMenu;
	private JMenuItem nuevoMenu;
	private JMenuItem modificarMenu;
	private JMenuItem guardarMenu;
	private JMenuItem eliminarMenu;
	private JMenuItem cancelarMenu;
	private JMenuBar barraMenu;
	
	// VARIABLES DEL CONTENIDO CENTRAL DE LA VENTANA
	private JLabel cubos;
	private JComboBox<CuboRubik> campoCubos;
	private JLabel nombre;
	private JTextField campoNombre;
	private JLabel caras;
	private JTextField campoCaras;
	private JLabel modelo;
	private JTextField campoModelo;
	private JLabel fecha;
	private JTextField campoFecha;
	private JLabel marca;
	private JComboBox<String> campoMarca;
	private JLabel material;
	private JRadioButton plastico;
	private JRadioButton madera;
	private JRadioButton metal;
	private ButtonGroup grupoMat;
	private JLabel mecanismo;
	private JList<String> listaMec;
	private JScrollPane scrollMecanismo;
	private DefaultListModel<String> tipoMecanismos;
	private JLabel sticker;
	private JList<String> listaStic;
	private JScrollPane scrollSticker;
	private JComboBox<String> campoStickers;
	private JLabel precio;
	private JTextField campoPrecio;
	private JTextField campoIMG;
	private JButton agregar;
	private JButton quitar;
	private JButton seleccionar;
	private JButton cancelar;
	
	// VARIABLES DE LOS BOTONES INFERIORES 
	private JButton nuevo;
	private JButton modificar;
	private JButton guardar;
	private JButton eliminar;
	
	// ACCIONES DE LOS BOTONES INFERIORES	
	private Action accionNuevo;
	private Action accionMod;
	private Action accionGuardar;
	private Action accionEliminar;
	private Action accionCancelar;
	
	// CONSTRUCTOR DEL DIALOGO DE CONSULTA
	public DialogoConsultasRubik(JFrame principal) {
		super(principal,"Catálogo de Cubos de Rubik", true);
		
		this.setLayout(new BorderLayout());
		JPanel vacio1 = new JPanel();
		JPanel vacio2 = new JPanel();
		vacio2.setPreferredSize(new Dimension(30,-1));
		vacio1.setPreferredSize(new Dimension(30,-1));
		this.add(vacio1, BorderLayout.WEST);
		this.add(vacio2, BorderLayout.EAST);
		
		// ASIGNACION DE EVENTOS A LOS BOTONES Y ELEMNTOS DEL MENÚ
		accionNuevo = new AbstractAction("Nuevo", new ImageIcon(
				getClass().getResource("/cubo_rubik/imagenes/bnuevo.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				nuevo();
			}
		};
		accionNuevo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		accionNuevo.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
		accionNuevo.putValue(Action.SHORT_DESCRIPTION,"Agregar nuevo cubo");
		
		accionMod = new AbstractAction("Modificar", new ImageIcon(
				getClass().getResource("/cubo_rubik/imagenes/bmodificar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				modificar();
			}
		};
		accionMod.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		accionMod.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
		accionMod.putValue(Action.SHORT_DESCRIPTION,"Modificar un cubo");
		
		accionGuardar = new AbstractAction("Guardar", new ImageIcon(
				getClass().getResource("/cubo_rubik/imagenes/bsave.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		};
		accionGuardar.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		accionGuardar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
		accionGuardar.putValue(Action.SHORT_DESCRIPTION,"Salvar cubo");
		
		accionEliminar = new AbstractAction("Eliminar", new ImageIcon(
				getClass().getResource("/cubo_rubik/imagenes/beliminar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		};
		accionEliminar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
		accionEliminar.putValue(Action.SHORT_DESCRIPTION,"Eliminar cubo");
		
		accionCancelar = new AbstractAction("Cancelar", new ImageIcon(
				getClass().getResource("/cubo_rubik/imagenes/bcancelar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		};
		accionCancelar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		accionCancelar.putValue(Action.SHORT_DESCRIPTION,"Cancelar acción");
		
		// INICIO DE LOS ELEMENTOS DE LA VENTANA
		iniciarMenu();
		iniciarCampos();
		iniciarBotones();
		establecerFoco();
		inicializar();
		
		// CONFIGURACIÓN DEL DIALOGO
		this.setIconImage(principal.getIconImage());
		this.setSize(900,700);
		this.setLocationRelativeTo(principal);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.setVisible(true);
	}
	
    public void itemStateChanged(ItemEvent e) {
            CuboRubik cubo = (CuboRubik)campoCubos.getSelectedItem();
            this.campoNombre.setText(cubo.getNomCubo());
            this.campoModelo.setText(cubo.getModelo());
            this.campoCaras.setText(""+cubo.getCaras());
            this.campoPrecio.setText(""+cubo.getPrecio());
            
            if(cubo.getMaterial().equals(this.plastico.getText())) {
            	this.plastico.setSelected(true);
            } else if(cubo.getMaterial().equals(this.madera.getText())) {
            	this.madera.setSelected(true);
            } else {
            	this.metal.setSelected(true);
            }
    }
	
	// ACTIONLISTENER PARA LOS BOTONES AUXILIARES
	private class EventosBoton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(seleccionar)) {
				seleccionarimg();
			} else if (e.getSource().equals(agregar)) {
				agregar();
			} else if (e.getSource().equals(quitar)) {
				quitar();
			}
		
		}
	}
	
	// METODO DE LOS CAMPOS DE CONFIGURACION DE LOS CUBOS
	private void iniciarCampos() {
		JPanel panelSuperior = new JPanel();
		JPanel panelCentral = new JPanel();
		JPanel panelAux;
		JPanel panelAux2;
		
		panelSuperior.setLayout(new FlowLayout());
		
		// ETIQUETA CUBOS
		cubos =  new JLabel("Cubo Rubik:");
		cubos.setDisplayedMnemonic(KeyEvent.VK_U);
		campoCubos = new JComboBox<CuboRubik>();
		campoCubos.setPreferredSize(new Dimension(450,20));
		campoCubos.setToolTipText("Cubos existentes en el catalogo.");
		cubos.setLabelFor(campoCubos);
		campoCubos.addItemListener(this);
		
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux.add(cubos);
		panelAux.add(campoCubos);
		panelSuperior.add(panelAux);
		
		this.add(panelSuperior, BorderLayout.NORTH);
		
		panelCentral.setLayout(new GridLayout(4, 3));
		
		// NOMBRE -- 3. TEXTO LIBRE
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		nombre = new JLabel("Nombre: ");
		nombre.setDisplayedMnemonic(KeyEvent.VK_B);
		campoNombre = new JTextField(15);
		campoNombre.setEditable(true);
		campoNombre.setToolTipText("Nombre con el que se identifica al cubo.");
		nombre.setLabelFor(campoNombre);
		panelAux.add(nombre);
		panelAux.add(campoNombre);
		panelCentral.add(panelAux);
		
		// MODELO -- 4. TEXTO CON FORMATO
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		modelo = new JLabel("Modelo: ");
		modelo.setDisplayedMnemonic(KeyEvent.VK_L);
		campoModelo = new JTextField(15);
		campoModelo.setEditable(true);
		campoModelo.setToolTipText("Formato del modelo nxn, nxnxn, donde n es un numero entre 1 y 5.");
		modelo.setLabelFor(campoModelo);
		panelAux.add(modelo);
		panelAux.add(campoModelo);
		panelCentral.add(panelAux);
		
		// CARAS -- 1. NÚMERO LIBRE
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		caras = new JLabel("Número de caras: ");
		caras.setDisplayedMnemonic(KeyEvent.VK_D);
		campoCaras = new JTextField(15);
		campoCaras.setEditable(true);
		campoCaras.setToolTipText("Número de caras que tiene el cubo.");
		caras.setLabelFor(campoCaras);
		panelAux.add(caras);
		panelAux.add(campoCaras);
		panelCentral.add(panelAux);
				
		// FECHA DE LANZAMIENTO -- 5. FECHA
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		fecha = new JLabel("Fecha de lanzamiento: ");
		fecha.setDisplayedMnemonic(KeyEvent.VK_H);
		campoFecha = new JTextField(15);
		campoFecha.setEditable(true);
		campoFecha.setToolTipText("Fecha en la que el cubo salio a la venta.");
		fecha.setLabelFor(campoFecha);
		panelAux.add(fecha);
		panelAux.add(campoFecha);
		panelCentral.add(panelAux);
		
		// MARCA -- 7. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES DINÁMICAS.
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		marca =  new JLabel("Marca: ");
		marca.setDisplayedMnemonic(KeyEvent.VK_R);
		campoMarca = new JComboBox<String>();
		campoMarca.setPreferredSize(new Dimension(170,20));
		campoMarca.setEditable(true);
		campoMarca.setToolTipText("Nombre del fabricante del cubo.");
		marca.setLabelFor(campoMarca);
		panelAux.add(marca);
		panelAux.add(campoMarca);
		panelCentral.add(panelAux);
		
		// MATERIAL -- 6. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES FIJAS: 
		//PLÁSTICO, MADERA, METAL 
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		material =  new JLabel("Material: ");
		material.setDisplayedMnemonic(KeyEvent.VK_T);
		plastico = new JRadioButton("Plástico");
		plastico.setToolTipText("Piezas y estructura de plastico.");
		madera = new JRadioButton("Madera");
		madera.setToolTipText("Piezas y estructura de madera.");
		metal = new JRadioButton("Metal");
		metal.setToolTipText("Piezas de metal ligero y estructura de imanes.");
		grupoMat = new ButtonGroup();
		grupoMat.add(plastico);
		grupoMat.add(madera);
		grupoMat.add(metal);
		panelAux.add(material);
		panelAux.add(plastico);
		panelAux.add(madera);
		panelAux.add(metal);
		material.setLabelFor(plastico);
		panelCentral.add(panelAux);
		
		// STICKERS -- 9. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES DINÁMICAS.
		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux2 = new JPanel();
		panelAux2.setLayout(new GridLayout(2,1));
		sticker =  new JLabel("Stickers: ");
		sticker.setDisplayedMnemonic(KeyEvent.VK_K);
		sticker.setToolTipText("Stickers personalizados para el cubo.");
		String[] stickers = {
				"Candy", "Fibra de carbono", "Vinilo", "Texturizado",
				"Metálicos", "Fluorecentes", "Personalizados"
				};
		listaStic = new JList<>(stickers);
		listaStic.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sticker.setLabelFor(listaStic);
		scrollSticker = new JScrollPane(listaStic);
		JPanel vacio2 = new JPanel();
		vacio2.setPreferredSize(new Dimension(25,-1));
		panelAux.add(vacio2, BorderLayout.EAST);
		panelAux2.add(sticker);
		panelAux2.add(scrollSticker);
		panelAux.add(panelAux2, BorderLayout.CENTER);
		panelCentral.add(panelAux);
		
		// MECANISMOS -- 8. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES FIJAS: 
		//CÚBICOS, CUBOIDES, MORPHIX, MINX, SKEWB, SQUARE, CURVY, ENGRANAJES, MIXUP
		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux2 = new JPanel();
		panelAux2.setLayout(new GridLayout(2,1));
		mecanismo =  new JLabel("Mecanismos: ");
		mecanismo.setDisplayedMnemonic(KeyEvent.VK_I);
		mecanismo.setToolTipText("Forma del cubo.");
		tipoMecanismos = new DefaultListModel<>();
		listaMec = new JList<>(tipoMecanismos);
		listaMec.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mecanismo.setLabelFor(listaMec);
		scrollMecanismo = new JScrollPane(listaMec);
		JPanel vacio = new JPanel();
		vacio.setPreferredSize(new Dimension(20,-1));
		panelAux.add(vacio, BorderLayout.EAST);
		panelAux2.add(mecanismo);
		panelAux2.add(scrollMecanismo);
		panelAux.add(panelAux2, BorderLayout.CENTER);
		panelCentral.add(panelAux);
		
		// IMAGEN -- 10. RUTA IMAGEN
		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		JLabel imagen = new JLabel("Imagen:");
		imagen.setDisplayedMnemonic(KeyEvent.VK_A);
		imagen.setToolTipText("Imagen ilustrativa del cubo.");
		campoIMG = new JTextField(10);
		campoIMG.setEditable(false);
		seleccionar = new JButton("Seleccionar");
		seleccionar.setToolTipText("Buscar imagen del cubo");
		seleccionar.setIcon(new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/imagen.png")));
		seleccionar.addActionListener(new EventosBoton());
		imagen.setLabelFor(seleccionar);
		vacio = new JPanel();
		vacio.setPreferredSize(new Dimension(20,-1));
		panelAux.add(vacio, BorderLayout.WEST);
		panelAux.add(imagen, BorderLayout.NORTH);
		panelAux.add(campoIMG, BorderLayout.CENTER);
		panelCentral.add(panelAux);
		
		// LISTA DESPLEGABLE Y BOTONES AUXILIARES DEL CAMPO DE STICKERS
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux2 =  new JPanel();
		panelAux2.setLayout(new GridLayout(2,1));
		campoStickers = new JComboBox<String>();
		campoStickers.setPreferredSize(new Dimension(200,20));
		vacio2 = new JPanel();
		vacio2.setLayout(new FlowLayout());
		vacio2.add(campoStickers);
		agregar = new JButton("Sumar");
		agregar.setToolTipText("Agregar Sticker");
		agregar.setIcon(new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/agregar.png")));
		agregar.setMnemonic(KeyEvent.VK_S);
		agregar.addActionListener(new EventosBoton());
		quitar = new JButton("Quitar");
		quitar.setToolTipText("Quitar Sticker");
		quitar.setIcon(new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/menos.png")));
		quitar.setMnemonic(KeyEvent.VK_Q);
		quitar.addActionListener(new EventosBoton());
		panelAux.add(agregar);
		panelAux.add(quitar);
		panelAux2.add(vacio2);
		panelAux2.add(panelAux);
		panelCentral.add(panelAux2);
		
		// PRECIO -- 2. NÚMERO CON RANGO
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		precio = new JLabel("Precio: ");
		precio.setDisplayedMnemonic(KeyEvent.VK_P);
		campoPrecio = new JTextField(15);
		campoPrecio.setEditable(true);
		campoPrecio.setToolTipText("Valor del cubo en pesos mexicanos.");
		precio.setLabelFor(campoPrecio);
		panelAux.add(precio);
		panelAux.add(campoPrecio);
		panelAux2 = new JPanel();
		panelAux2.setLayout(new GridLayout(2,1));
		panelAux2.add(vacio);
		panelAux2.add(panelAux);
		panelCentral.add(panelAux2);
		
		// BOTON AUXILIAR DEL CAMPO DE IMAGEN
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux.add(seleccionar);
		panelCentral.add(panelAux);
		
		this.add(panelCentral, BorderLayout.CENTER);
		
	}

	// METODO DE LOS BOTONES INFERIORES
	private void iniciarBotones() {
		
		JPanel panelBotones = new JPanel();
		JPanel panelAuxB;
		
		panelBotones.setLayout(new FlowLayout());
		
		// BOTON NUEVO
		nuevo = new JButton(accionNuevo);
		nuevo.getActionMap().put("BotonNuevo", accionNuevo);
		nuevo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put((KeyStroke) accionNuevo.getValue(Action.ACCELERATOR_KEY),"BotonNuevo" );
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(nuevo);
		panelBotones.add(panelAuxB);
		// BOTON MODIFICAR
		modificar = new JButton(accionMod);
		modificar.getActionMap().put("BotonModificar", accionMod);
		modificar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put((KeyStroke) accionMod.getValue(Action.ACCELERATOR_KEY), "BotonModificar");
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(modificar);
		panelBotones.add(panelAuxB);
		// BOTON GUARDAR
		guardar =  new JButton(accionGuardar);
		guardar.getActionMap().put("BotonGuardar", accionGuardar);
		guardar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put((KeyStroke) accionGuardar.getValue(Action.ACCELERATOR_KEY), "BotonGuardar");
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(guardar);	
		panelBotones.add(panelAuxB);
		// BOTON ELIMINAR
		eliminar =  new JButton(accionEliminar);
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(eliminar);
		panelBotones.add(panelAuxB);
		// BOTON CANCELAR
		cancelar =  new JButton(accionCancelar);
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(cancelar);
		panelBotones.add(panelAuxB);
		
		this.add(panelBotones, BorderLayout.SOUTH);
		
	}
	
	// METODO DEL MENU SUPERIOR IZQUIERDO
	private void iniciarMenu() {
		
		// MENÚ OPERACIONES
		operacionMenu = new JMenu("Operaciones");
		operacionMenu.setMnemonic(KeyEvent.VK_O);
		operacionMenu.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/operaciones.png")));
		operacionMenu.setToolTipText("Manejo de entidades");
		
		nuevoMenu = new JMenuItem(accionNuevo);
		
		modificarMenu = new JMenuItem(accionMod);
		
		guardarMenu = new JMenuItem(accionGuardar);
		
		eliminarMenu = new JMenuItem(accionEliminar);
		
		cancelarMenu = new JMenuItem(accionCancelar);
		
		operacionMenu.add(nuevoMenu);
		operacionMenu.add(modificarMenu);
		operacionMenu.add(guardarMenu);
		operacionMenu.add(eliminarMenu);
		operacionMenu.add(cancelarMenu);
				
		barraMenu = new JMenuBar();
		barraMenu.add(operacionMenu);
		this.setJMenuBar(barraMenu);
	}
	
	// METODOS PARA LAS ACCIONES DE LOS BOTONES
	// METODO PARA NEUVO
	private void nuevo() {
		this.habilitarCampos(true);
		this.limpiarCampos();
		this.habilitarBotones(false, false, true, false, true);
		this.campoCubos.setEnabled(false);
	}
	
	// METODO AUXILIAR PARA HABILITAR/DESABILITAR BOTONES Y MENUs
	private void habilitarBotones(boolean nuevo, boolean modificar, boolean guardar, boolean eliminar, boolean cancelar) {
		this.nuevo.setEnabled(nuevo);
		this.nuevoMenu.setEnabled(nuevo);
		this.modificar.setEnabled(modificar);
		this.modificarMenu.setEnabled(modificar);
		this.guardar.setEnabled(guardar);
		this.guardarMenu.setEnabled(guardar);
		this.eliminar.setEnabled(eliminar);
		this.eliminarMenu.setEnabled(eliminar);
		this.cancelar.setEnabled(cancelar);
		this.cancelarMenu.setEnabled(cancelar);
	}
	
	private void modificar() {
		JOptionPane.showMessageDialog(this, "Cubo modificado","Modificar", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// METODO PARA GUARDAR
	private void guardar() {
		CuboRubik cubo = new CuboRubik();
		cubo.setCaras(campoCaras.getText());
		cubo.setPrecio(campoPrecio.getText());
		cubo.setNomCubo(campoNombre.getText());
		cubo.setModelo(campoModelo.getText());
		
		if(this.plastico.isSelected()) {
			cubo.setMaterial(this.plastico.getText());
		} else if(this.madera.isSelected()) {
			cubo.setMaterial(this.madera.getText());
		} else {
			cubo.setMaterial(this.metal.getText());
		}
		
		this.campoCubos.addItem(cubo);
		
		JOptionPane.showMessageDialog(this, "Cubo guardado con éxito","Guardar", JOptionPane.INFORMATION_MESSAGE);
		
		this.cancelar();
	}
	
	private void eliminar() {
		JOptionPane.showMessageDialog(this, "Cubo eliminado","Eliminar", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// METODO PARA CANCELAR
	private void cancelar() {
		this.habilitarCampos(false);
		this.limpiarCampos();
		this.habilitarBotones(true, true, false, true, false);
		this.verificarLista();
	}
	
	private void seleccionarimg() {
		JOptionPane.showMessageDialog(this, "Imagen seleccionada","Selecionar imagen", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void agregar() {
		JOptionPane.showMessageDialog(this, "Sticker agregado","Agregar", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void quitar() {
		JOptionPane.showMessageDialog(this, "Sticker quitado","Quitar", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// METODO PARA LIMPIAR CAMPOS Y ESTABLECER VALORES POR DEFECTO
	private void limpiarCampos() {
		this.campoNombre.setText("");
		this.campoModelo.setText("");
		this.campoFecha.setText("");
		this.campoCaras.setText("");
		this.plastico.setSelected(true);
		this.listaMec.setSelectedIndex(0);
		this.campoPrecio.setText("");
	}
	
	// METODO PARA HABILITAR/DESABILITAR CAMPOS
	private void habilitarCampos(boolean habilitar) {
		this.campoNombre.setEditable(habilitar);
		this.campoModelo.setEditable(habilitar);
		this.campoFecha.setEditable(habilitar);
		this.campoCaras.setEditable(habilitar);
		this.campoMarca.setEnabled(habilitar);
		this.plastico.setEnabled(habilitar);
		this.madera.setEnabled(habilitar);
		this.metal.setEnabled(habilitar);
		this.listaStic.setEnabled(habilitar);
		this.campoStickers.setEnabled(habilitar);
		this.agregar.setEnabled(habilitar);
		this.quitar.setEnabled(habilitar);
		this.listaMec.setEnabled(habilitar);
		this.seleccionar.setEnabled(habilitar);
		this.campoPrecio.setEditable(habilitar);
	}

	// METODO PARA VERIFICAR LA LISTA PRINCPAL
	private void verificarLista() {
		boolean verifica = this.campoCubos != null 
				&& this.campoCubos.getItemCount() > 0;
		this.modificar.setEnabled(verifica);
		this.eliminar.setEnabled(verifica);
		this.modificarMenu.setEnabled(verifica);
		this.modificarMenu.setEnabled(verifica);
		this.campoCubos.setEnabled(verifica);
	}
	
	// METODO INICIALIZAR
	private void inicializar() {
		this.habilitarCampos(false);
		this.habilitarBotones(true, true, false, true, false);
		this.tipoMecanismos.addElement("Cubico");
		this.tipoMecanismos.addElement("Morphix");
		this.tipoMecanismos.addElement("Minx");
		this.tipoMecanismos.addElement("Skewb");
		this.tipoMecanismos.addElement("Square");
		this.tipoMecanismos.addElement("Curvy");
		this.tipoMecanismos.addElement("Engranajes");
		this.tipoMecanismos.addElement("Mixup");
		this.verificarLista();
		
	}
	// METODO PARA LA POLÍTICA DEL FOCO
	private void establecerFoco() {
		Vector<Component> componentes = new Vector<>();
		componentes.add(this.nuevo);
		componentes.add(this.modificar);
		componentes.add(this.guardar);
		componentes.add(this.eliminar);
		componentes.add(this.cancelar);
		componentes.add(this.campoCubos);
		componentes.add(this.campoNombre);
		componentes.add(this.campoModelo);
		componentes.add(this.campoCaras);
		componentes.add(this.campoFecha);
		componentes.add(this.campoMarca);
		componentes.add(this.plastico);
		componentes.add(this.scrollSticker);
		componentes.add(this.campoStickers);
		componentes.add(this.agregar);
		componentes.add(this.quitar);
		componentes.add(this.scrollMecanismo);
		componentes.add(this.seleccionar);
		componentes.add(this.campoPrecio);
		MiFocusTraversalPolicy politicaFoco = new MiFocusTraversalPolicy(componentes);
		this.setFocusTraversalPolicy(politicaFoco);
	} 
	
}
