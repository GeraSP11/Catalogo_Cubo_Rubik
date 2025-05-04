package cubo_rubik.gui;

// IMPORTA LIBRERIAS UTILIZADAS
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cubo_rubik.base_datos.CuboRubikBaseDatos;
import cubo_rubik.dominio.CuboRubik;
import cubo_rubik.excepciones.BaseDatosException;
import cubo_rubik.excepciones.RubiksWarningsException;
import cubo_rubik.utilerias.MiFocusTraversalPolicy;

import com.toedter.calendar.JDateChooser;

// CLASE DIALOGO DE LA VENTANA DE CONSULTA 
public class DialogoCatalogoRubik extends JDialog implements ItemListener {

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
	JLabel cubos;
	private JComboBox<CuboRubik> listaCubos;
	JLabel nombre;
	private JTextField campoNombre;
	JLabel caras;
	private JTextField campoCaras;
	JLabel modelo;
	private JTextField campoModelo;
	JLabel fecha;
	private JDateChooser campoFecha;
	JLabel marca;
	private JComboBox<String> listaMarcas;
	JLabel material;
	private JRadioButton plastico;
	private JRadioButton madera;
	private JRadioButton metal;
	private ButtonGroup grupoMat;
	JLabel mecanismo;
	private JList<String> listaMec;
	private JScrollPane scrollMecanismo;
	private DefaultListModel<String> tipoMecanismos;
	JLabel sticker;
	private JList<String> stickers;
	private JScrollPane scrollSticker;
	private DefaultListModel<String> listaStickers;
	private JComboBox<String> listaAuxStickers;
	JLabel precio;
	private JTextField campoPrecio;
	private JLabel areaImagen;
	private JButton agregar;
	private JButton quitar;
	private JButton seleccionar;
	private JButton cancelar;

	// VARIABLE PARA LA ACION DE MODIFICAR
	private boolean esNuevo;

	// VARIABLE PARA SABER SI LA IMAGEN EXISTE
	private String rutaImagen;
	private final String RUTA_IMG_DEFECTO = "/cubo_rubik/imagenes/imgCuboPD.png";

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

	// ACCIONES DE LOS BOTONES AUXILIARES
	private Action accionAgregar;
	private Action accionQuitar;
	private Action accionSeleccionar;

	// VARIABLE PARA LA BASE DE DATOS
	private CuboRubikBaseDatos cuboBD;

	// CONSTRUCTOR DEL DIALOGO DE CONSULTA
	public DialogoCatalogoRubik(JFrame principal) {
		super(principal, "Catálogo de Cubos de Rubik", true);

		try {
			this.cuboBD = new CuboRubikBaseDatos();
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}

		this.setLayout(new BorderLayout());
		JPanel vacio1 = new JPanel();
		JPanel vacio2 = new JPanel();
		vacio2.setPreferredSize(new Dimension(30, -1));
		vacio1.setPreferredSize(new Dimension(30, -1));
		this.add(vacio1, BorderLayout.WEST);
		this.add(vacio2, BorderLayout.EAST);

		// INICIO DE LOS ELEMENTOS DE LA VENTANA
		iniciarAcciones();
		iniciarMenu();
		iniciarCampos();
		iniciarBotones();
		establecerFoco();
		inicializar();

		// CONFIGURACIÓN DEL DIALOGO
		this.setIconImage(principal.getIconImage());
		this.setSize(900, 700);
		this.setLocationRelativeTo(principal);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}

	// EVENTO ITEM AL SELECIONAR UN ITEM DE LA LISTA PRINCPAL
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(listaCubos)) {
			seleccionarCubo();
		}
	}

	// METODO PARA INICIALIZAR LAS ACCIONES DE LOS BOTONES Y MENUS
	private void iniciarAcciones() {
		// ASIGNACION DE EVENTOS A LOS BOTONES PRINCIPALES Y ELEMNTOS DEL MENÚ
		accionNuevo = new AbstractAction("Nuevo",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/bnuevo.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				nuevo();
			}
		};
		accionNuevo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		accionNuevo.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
		accionNuevo.putValue(Action.SHORT_DESCRIPTION, "Agregar nuevo cubo.");

		accionMod = new AbstractAction("Modificar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/bmodificar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				modificar();
			}
		};
		accionMod.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		accionMod.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
		accionMod.putValue(Action.SHORT_DESCRIPTION, "Modificar un cubo.");

		accionGuardar = new AbstractAction("Guardar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/bsave.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		};
		accionGuardar.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		accionGuardar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
		accionGuardar.putValue(Action.SHORT_DESCRIPTION, "Salvar cubo.");

		accionEliminar = new AbstractAction("Eliminar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/beliminar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		};
		accionEliminar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
		accionEliminar.putValue(Action.SHORT_DESCRIPTION, "Eliminar cubo.");

		accionCancelar = new AbstractAction("Cancelar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/bcancelar.png"))) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		};
		accionCancelar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		accionCancelar.putValue(Action.SHORT_DESCRIPTION, "Cancelar acción.");

		// ACCIONES PARA LOS BOTONES AUXILARES
		accionAgregar = new AbstractAction("Sumar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/agregar.png"))) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				agregar();
			}
		};
		accionAgregar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
		accionAgregar.putValue(Action.SHORT_DESCRIPTION, "Agregar Sticker.");

		accionQuitar = new AbstractAction("Quitar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/menos.png"))) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				quitar();
			}
		};
		accionQuitar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_Q));
		accionQuitar.putValue(Action.SHORT_DESCRIPTION, "Quitar sticker.");

		accionSeleccionar = new AbstractAction("Seleccionar",
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/imagen.png"))) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				seleccionarImagen();
			}
		};
		accionSeleccionar.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
		accionSeleccionar.putValue(Action.SHORT_DESCRIPTION, "Buscar imagen.");
	}

	// METODO DE LOS CAMPOS DE CONFIGURACION DE LOS CUBOS
	private void iniciarCampos() {
		JPanel panelSuperior = new JPanel();
		JPanel panelCentral = new JPanel();
		JPanel panelAux;
		JPanel panelAux2;

		panelSuperior.setLayout(new FlowLayout());

		// ETIQUETA CUBOS
		cubos = new JLabel("Cubo Rubik:");
		cubos.setDisplayedMnemonic(KeyEvent.VK_U);
		listaCubos = new JComboBox<CuboRubik>();
		listaCubos.setPreferredSize(new Dimension(450, 20));
		listaCubos.setToolTipText("Cubos existentes en el catalogo.");
		cubos.setLabelFor(listaCubos);
		listaCubos.addItemListener(this);

		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux.add(cubos);
		panelAux.add(listaCubos);
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
		campoModelo.setToolTipText("Formato del modelo nxn, nxnxn, donde n es un numero entre 1 y 7.");
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
		campoFecha = new JDateChooser();
		campoFecha.setPreferredSize(new Dimension(90, 20));
		campoFecha.getCalendarButton().setToolTipText("Fecha en la que el cubo salió a la venta.");
		campoFecha.getDateEditor().getUiComponent().setToolTipText("DD/MM/AAAA");
		fecha.setLabelFor(campoFecha);
		panelAux.add(fecha);
		panelAux.add(campoFecha);
		panelCentral.add(panelAux);

		// MARCA -- 7. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES DINÁMICAS.
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		marca = new JLabel("Marca: ");
		marca.setDisplayedMnemonic(KeyEvent.VK_R);
		listaMarcas = new JComboBox<String>();
		listaMarcas.setPreferredSize(new Dimension(170, 20));
		listaMarcas.setEditable(true);
		listaMarcas.setToolTipText("Nombre del fabricante del cubo.");
		marca.setLabelFor(listaMarcas);
		panelAux.add(marca);
		panelAux.add(listaMarcas);
		panelCentral.add(panelAux);

		// MATERIAL -- 6. DATO OBTENIDO DE OPCIONES MUTUAMENTE EXCLUYENTES FIJAS:
		// PLÁSTICO, MADERA, METAL
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		material = new JLabel("Material: ");
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
		panelAux2.setLayout(new GridLayout(2, 1));
		sticker = new JLabel("Stickers: ");
		sticker.setDisplayedMnemonic(KeyEvent.VK_K);
		sticker.setLabelFor(listaAuxStickers);
		listaStickers = new DefaultListModel<>();
		stickers = new JList<>(listaStickers);
		stickers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaAuxStickers = new JComboBox<String>();
		listaAuxStickers.setPreferredSize(new Dimension(200, 20));
		listaAuxStickers.setToolTipText("Stickers personalizados para el cubo.");
		listaAuxStickers.setEditable(true);
		sticker.setLabelFor(listaAuxStickers);
		scrollSticker = new JScrollPane(stickers);
		JPanel vacio2 = new JPanel();
		vacio2.setPreferredSize(new Dimension(25, -1));
		panelAux.add(vacio2, BorderLayout.EAST);
		panelAux2.add(sticker);
		panelAux2.add(scrollSticker);
		panelAux.add(panelAux2, BorderLayout.CENTER);
		panelCentral.add(panelAux);

		// MECANISMOS -- 8. DATO MULTIVALORADO DE OPCIONES NO EXCLUYENTES FIJAS:
		// CÚBICOS, CUBOIDES, MORPHIX, MINX, SKEWB, SQUARE, CURVY, ENGRANAJES, MIXUP
		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		panelAux2 = new JPanel();
		panelAux2.setLayout(new GridLayout(2, 1));
		mecanismo = new JLabel("Mecanismos: ");
		mecanismo.setDisplayedMnemonic(KeyEvent.VK_I);
		tipoMecanismos = new DefaultListModel<>();
		listaMec = new JList<>(tipoMecanismos);
		listaMec.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listaMec.setToolTipText("Forma del cubo.");
		mecanismo.setLabelFor(listaMec);
		scrollMecanismo = new JScrollPane(listaMec);
		JPanel vacio = new JPanel();
		vacio.setPreferredSize(new Dimension(20, -1));
		panelAux.add(vacio, BorderLayout.EAST);
		panelAux2.add(mecanismo);
		panelAux2.add(scrollMecanismo);
		panelAux.add(panelAux2, BorderLayout.CENTER);
		panelCentral.add(panelAux);

		// IMAGEN -- 10. RUTA IMAGEN
		panelAux = new JPanel();
		panelAux.setLayout(new BorderLayout());
		JLabel imagen = new JLabel("Imagen:");
		areaImagen = new JLabel();
		areaImagen.setHorizontalAlignment(SwingConstants.CENTER);
		areaImagen.setToolTipText("Imagen ilustrativa del cubo.");
		areaImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		vacio = new JPanel();
		vacio.setPreferredSize(new Dimension(20, -1));
		panelAux.add(vacio, BorderLayout.WEST);
		panelAux.add(imagen, BorderLayout.NORTH);
		panelAux.add(areaImagen, BorderLayout.CENTER);
		panelCentral.add(panelAux);

		// LISTA DESPLEGABLE Y BOTONES AUXILIARES DEL CAMPO DE STICKERS
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux2 = new JPanel();
		panelAux2.setLayout(new GridLayout(2, 1));
		vacio2 = new JPanel();
		vacio2.setLayout(new FlowLayout());
		vacio2.add(listaAuxStickers);
		agregar = new JButton(accionAgregar);
		quitar = new JButton(accionQuitar);
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
		panelAux2.setLayout(new GridLayout(2, 1));
		panelAux2.add(vacio);
		panelAux2.add(panelAux);
		panelCentral.add(panelAux2);

		// BOTON AUXILIAR DEL CAMPO DE IMAGEN
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		seleccionar = new JButton(accionSeleccionar);
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
				.put((KeyStroke) accionNuevo.getValue(Action.ACCELERATOR_KEY), "BotonNuevo");
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
		guardar = new JButton(accionGuardar);
		guardar.getActionMap().put("BotonGuardar", accionGuardar);
		guardar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put((KeyStroke) accionGuardar.getValue(Action.ACCELERATOR_KEY), "BotonGuardar");
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(guardar);
		panelBotones.add(panelAuxB);
		// BOTON ELIMINAR
		eliminar = new JButton(accionEliminar);
		panelAuxB = new JPanel();
		panelAuxB.setLayout(new FlowLayout());
		panelAuxB.add(eliminar);
		panelBotones.add(panelAuxB);
		// BOTON CANCELAR
		cancelar = new JButton(accionCancelar);
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
		operacionMenu.setIcon(new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/operaciones.png")));
		operacionMenu.setToolTipText("Operaciones disponibles para trabajar con los cubos.");

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

	// METODO QUE MUESTRA LA INFORACIÓN DE UN CUBO AL SER SELECCIONADO EN LA LISTA
	// PRINCIPAL
	private void seleccionarCubo() {
		CuboRubik cubo = (CuboRubik) listaCubos.getSelectedItem();
		// OBTENER EL NOMBRE DEL CUBO
		this.campoNombre.setText(cubo.getNomCubo());
		// OBTENER EL MODELO DEL CUBO
		this.campoModelo.setText(cubo.getModelo());
		// OBTENER EL NÚMERO DE CARAS DEL CUBO
		this.campoCaras.setText("" + cubo.getCaras());
		// OBTENER SU FECHA DE CREACIÓN DEL CUBO
		this.campoFecha.setDate(cubo.getFechaCreación());

		// OBTENER LA MARCA DEL CUBO
		this.listaMarcas.setSelectedItem(cubo.getMarca());

		// OBTENER EL MATERIAL DEL CUBO
		if (cubo.getMaterial().equals(this.plastico.getText())) {
			this.plastico.setSelected(true);
		} else if (cubo.getMaterial().equals(this.madera.getText())) {
			this.madera.setSelected(true);
		} else {
			this.metal.setSelected(true);
		}

		// OBTENER LOS STICKERS DEL CUBO
		this.listaStickers.clear(); // LIMPIAR LA LISTA PARA QUE NO SE AMONTONEN LOS STICKERS DE CUBOS ANTERIORMENTE
									// SELECCIONADOS.
		for (String sticker : cubo.getStikers()) {
			this.listaStickers.addElement(sticker);
			;
		}

		// OBTENER LA LISTA DE LOS MECANISMOS DEL CUBO
		int n = cubo.getMecanismos().size(); // TAMAÑO DEL ARRAY DE MECANISMOS
		int[] indices = new int[n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < this.tipoMecanismos.getSize(); j++) {
				if (this.tipoMecanismos.getElementAt(j).equals(cubo.getMecanismos().get(i))) {
					indices[i] = j;
				}
			}
		}
		this.listaMec.setSelectedIndices(indices);

		// OBTENER LA RUTA DE LA IMAGEN DEL CUBO Y MOSTRAR LA IMAGEN
		this.rutaImagen = cubo.getRutaImagen();
		if (!this.rutaImagen.trim().isEmpty()) {
			this.rutaImagen = "imagen/" + this.rutaImagen;
			try {
				cuboBD.recuperarImagen(cubo.getId(), this.rutaImagen);
			} catch (BaseDatosException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			this.rutaImagen = this.RUTA_IMG_DEFECTO;
		}
		muestraImagen();

		// ONTENER EL PRECIO DEL CUBO
		this.campoPrecio.setText("" + cubo.getPrecio());
	}

	// METODOS PARA LAS ACCIONES DE LOS BOTONES
	// METODO PARA NEUVO
	private void nuevo() {
		this.esNuevo = true;
		this.habilitarCampos(true);
		this.limpiarCampos();
		this.habilitarBotones(false, false, true, false, true);
		this.listaCubos.setEnabled(false);
	}

	// METODO AUXILIAR PARA HABILITAR/DESABILITAR BOTONES Y MENUs
	private void habilitarBotones(boolean nuevo, boolean modificar, boolean guardar, boolean eliminar,
			boolean cancelar) {
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

	// METODO PARA MODIFICAR UN CUBO DE LA LISTA PRINCIPAL
	private void modificar() {
		this.esNuevo = false;
		this.habilitarCampos(true);
		this.habilitarBotones(false, false, true, false, true);
		this.listaCubos.setEnabled(false);
	}

	// METODO PARA GUARDAR
	private void guardar() {
		try {
			// VALIDA SI LA CARPETA IMAGEN EXISTE, SI NO EXISTE LA CREA
			File carpetaImagen = new File("imagen");
			if (!carpetaImagen.exists()) {
				carpetaImagen.mkdir();
			}

			// CREACIÓN DE LA ENTIDAD
			CuboRubik cubo;
			if (this.esNuevo) {
				cubo = new CuboRubik();
			} else {
				cubo = (CuboRubik) this.listaCubos.getSelectedItem();
			}

			// GUARDA EL NOMBRE DEL CUBO
			cubo.setNomCubo(campoNombre.getText());

			// GUARDA EL MODELO
			cubo.setModelo(campoModelo.getText());

			// GUARDA EL NÚMERO DE CARAS DEL CUBO
			cubo.setCaras(campoCaras.getText());

			// GUARDA LA FECHA
			cubo.setFechaCreación(campoFecha.getDate());

			// GUARDA LA MARCA
			String marca = this.listaMarcas.getEditor().getItem().toString().trim(); // OBTENER LA MARCA DEL CAMPO
			int idMarca = cuboBD.consultarIdMarca(this.listaMarcas.getSelectedItem().toString()); // SELECIONADO O
																									// EDITADO
			boolean encontrado = false; // BANDERA PARA COMPARAR CON LA LISTA DEL COMBO
			ArrayList<String> marcasBD = cuboBD.consultarMarcas();
			for (String marcaBD : marcasBD) {
				if (marcaBD.equalsIgnoreCase(marca)) {
					this.listaMarcas.setSelectedItem(marcaBD);
					idMarca = cuboBD.consultarIdMarca(marcaBD);
					marca = marcaBD;
					encontrado = true;
					break;
				}
			}
			if (!encontrado && !marca.isEmpty()) { // SI NO SE ENCONTRO EN LA LISTA Y NO ESTA VACIO SE AGREGA Y SE
													// SELECCIONA EN EL COMBO.
				cuboBD.agregarMarca(marca);
				idMarca = cuboBD.consultarIdMarca(marca);
				this.iniciarListaMarcas();
				this.listaMarcas.setSelectedItem(marca);
			}
			cubo.setMarca(marca);

			// GUARDA MATERIAL
			if (this.plastico.isSelected()) {
				cubo.setMaterial(this.plastico.getText());
			} else if (this.madera.isSelected()) {
				cubo.setMaterial(this.madera.getText());
			} else {
				cubo.setMaterial(this.metal.getText());
			}

			// GUARDAR LOS STICKERS
			// UTILIZO EL METODO COLLECTIONS.LIST() PORQUE ES UNA FORMA MAS DIRECTA QUE
			// UTILIZAR UN CICLO FOR PARA INGREZAR ELEMENTO POR ELEMNTO AL ARRAYLIST
			// CON EL COLLECTIONS.LIST() SE HACE LA ENNUMERACION DE MI LISTA Y SE PASA COMO
			// PARAMETRO AL ARRAY PARA INICIALIZARLO
			ArrayList<String> stickersSeleccionados = new ArrayList<>(Collections.list(this.listaStickers.elements()));
			cubo.setStikers(stickersSeleccionados);

			// GUARDA LOS MECANISMOS
			ArrayList<String> mecanismosSeleccionados = new ArrayList<>(this.listaMec.getSelectedValuesList());
			cubo.setMecanismos(mecanismosSeleccionados);

			// GUARDA EL PRECIO
			cubo.setPrecio(campoPrecio.getText());

			// AÑADE EL CUBO GUARDADO A LA LISTA PRINCIPAL SI ES NUEVO
			int idCubo = 0;
			if (this.esNuevo) {
				idCubo = cuboBD.manejarCubo(cubo, "INSERT", idMarca);
				if (!this.rutaImagen.equalsIgnoreCase(this.RUTA_IMG_DEFECTO)) {
					cuboBD.guardarImagen(this.rutaImagen, idCubo);
				}
				this.iniciarListaCubos();
				// MUESTRA QUE EL CUBO NUEVO SE GUARDO
				JOptionPane.showMessageDialog(this, "Cubo guardado con éxito.", "Cubo guardado",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				idCubo = cuboBD.manejarCubo(cubo, "UPDATE", idMarca);
				if (!this.rutaImagen.equals(this.RUTA_IMG_DEFECTO)) {
					cuboBD.guardarImagen(this.rutaImagen, idCubo);
				}
				this.iniciarListaCubos();
				// MUESTRA QUE EL CUBO MODIFICADO SE GUARDO
				JOptionPane.showMessageDialog(this, "Cubo modificado con éxito.", "Cubo modificado",
						JOptionPane.INFORMATION_MESSAGE);
			}
			// TERMINA EL GUARDADO Y SE ENCARGA DE LIMPIAR LOS CAMPOS
			this.cancelar();
		} catch (RubiksWarningsException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.WARNING_MESSAGE);
			return;
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	// METODO DEL BOTON SUMAR PARA EL PUNTO 9
	private void agregar() {
		String sticker = this.listaAuxStickers.getEditor().getItem().toString().trim(); // OBTENER EL STICKER DEL CAMPO
																						// SELECIONADO O EDITADO
		if (sticker.isEmpty()) {
			return;
		}

		try {
			// -- CONFIRMAMOS SI SE ENCUENTRA O NO EN EL COMBOBOX
			boolean encontradoEnComboBox = false; // BANDERA PARA COMPARAR CON LA LISTA DEL COMBO
			ArrayList<String> stickersBD = cuboBD.consultarStickers();
			for (String stickerBD : stickersBD) {
				if (sticker.equalsIgnoreCase(stickerBD)) {
					this.listaAuxStickers.setSelectedItem(stickerBD);
					sticker = stickerBD;
					encontradoEnComboBox = true;
					break;
				}
			}

			if (!encontradoEnComboBox && !sticker.isEmpty()) { // SI NO SE ENCONTRO EN LA LISTA Y NO ESTA VACIO SE
																// AGREGA Y
																// SE SELECCIONA EN EL COMBO Y EN EL JLIST.
				if (cuboBD.insertarSticker(sticker) == 1) {
					this.iniciarListaStickers();
					this.listaAuxStickers.setSelectedItem(sticker);
					this.listaStickers.addElement(sticker);
					JOptionPane.showMessageDialog(this, "Sticker agregado.", "Agregar",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "El sticker no se agrego por un error en la base de datos.",
							"Error al agregar sticker", JOptionPane.INFORMATION_MESSAGE);
				}

				return; // COMO NO SE ENCONTRO EN LA LISTA AUXILIAR SE ENTIENDE QUE ES UN NUEVO STICKER
						// POR TANTO HASTA AQUI LLEGA EL METODO AGREGAR
				// EN CASO CONTRARIO CONTINUA
			}
			// -- CONFIRMAMOS SI SE ENCUENTRA O NO EN EL JLIST
			boolean encontradoEnJList = false;
			for (int i = 0; i < this.listaStickers.getSize(); i++) {
				if (this.listaStickers.getElementAt(i).equalsIgnoreCase(sticker)) {
					encontradoEnJList = true;
					JOptionPane.showMessageDialog(this,
							"El sticker que intenta agregar ya se encuentra registrado en la lista.",
							"El sticker ya exsiste", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
			if (!encontradoEnJList && !sticker.isEmpty()) { // SI NO SE ENCONTRO EN EL JLIST SE AGREGA
				this.listaStickers.addElement(sticker);
			}
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	// METODO DEL BOTON QUITAR PARA EL PUNTO 9
	private void quitar() {
		int indice = this.stickers.getSelectedIndex();
		if (indice != -1) { // SE COMPRUEBA QUE ESTE SELECCIONADO UN ELEMNTO
			this.listaStickers.remove(indice);
		} else {
			JOptionPane.showMessageDialog(this, "No se selecciono algún sticker en la lista.",
					"Ningun sticker selecionado", JOptionPane.WARNING_MESSAGE);
		}
	}

	// METODO PARA EL BOTON ELIMINAR
	private void eliminar() {
		int eliminar = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el cubo?", "Eliminar cubo",
				JOptionPane.YES_NO_OPTION);
		if (eliminar == JOptionPane.YES_OPTION) {
			CuboRubik cubo = (CuboRubik) this.listaCubos.getSelectedItem();
			if (cubo != null) {
				this.listaCubos.removeItemListener(this);
				try {
					this.cuboBD.manejarCubo(cubo, "DELETE", 0);
					this.listaCubos.removeItem(cubo);
					JOptionPane.showMessageDialog(null, "El cubo " + cubo.toString() + " fue eliminado correctamente.",
							"Eliminar", JOptionPane.INFORMATION_MESSAGE);
				} catch (BaseDatosException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
				}
				this.listaCubos.addItemListener(this);
				cancelar();
			}
		}
	}

	// METODO PARA CANCELAR
	private void cancelar() {
		this.habilitarCampos(false);
		this.limpiarCampos();
		this.habilitarBotones(true, true, false, true, false);
		this.verificarLista();
	}

	// METODO PARA EL BOTON SELECCIONAR IMAGEN
	private void seleccionarImagen() {
		JFileChooser seleccionarIMG = new JFileChooser();
		// TÍTULO DE LA VENTANA
		seleccionarIMG.setDialogTitle("Seleccione una imagen para el cubo");
		// PERMITIR SOLO SELECIONAR ARCHIVOS
		seleccionarIMG.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// FILTROS
		FileNameExtensionFilter filtroJPG = new FileNameExtensionFilter("Archivo de imagen JPG", "jpg");
		FileNameExtensionFilter filtroPNG = new FileNameExtensionFilter("Archivo de imagen PNG", "png");
		FileNameExtensionFilter filtroGIF = new FileNameExtensionFilter("Archivo de imagen GIF", "gif");
		seleccionarIMG.setFileFilter(filtroJPG);
		seleccionarIMG.addChoosableFileFilter(filtroPNG);
		seleccionarIMG.addChoosableFileFilter(filtroGIF);
		// NO PERMITIR LA SELECCIÓN DE CUALQUIER TIPO DE ARCHIVO
		seleccionarIMG.setAcceptAllFileFilterUsed(false);
		// ESTABLECER EL DIRECTORIO ACTUAL COMO DEFECTO
		seleccionarIMG.setCurrentDirectory(new File(System.getProperty("user.dir")));
		// NO PERMITIR LA SELECCIÓN MULTIPLE
		seleccionarIMG.setMultiSelectionEnabled(false);
		// PERSONALIZAR EL BOTON "ACEPTAR"
		seleccionarIMG.setApproveButtonText("Seleccionar");
		seleccionarIMG.setApproveButtonToolTipText("Seleccione la imagen para el cubo.");

		int opcion = seleccionarIMG.showOpenDialog(this);

		if (opcion == JFileChooser.APPROVE_OPTION) {
			File imagen = seleccionarIMG.getSelectedFile();
			if (imagen.exists()) {
				this.rutaImagen = imagen.getAbsolutePath();
				muestraImagen();
			} else {
				JOptionPane.showMessageDialog(this, "La imagen no existe en el equipo.", "Imagen inexistente",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	// METODO PARA MOSTRAR LA IMAGEN
	private void muestraImagen() {
		ImageIcon imagenCubo;
		if (this.rutaImagen.equals(RUTA_IMG_DEFECTO)) {
			imagenCubo = new ImageIcon(getClass().getResource(this.rutaImagen));
		} else {
			imagenCubo = new ImageIcon(this.rutaImagen);
		}
		// OBTENER LAS DIMENCIONES DE LA IMAGEN
		int anchoOriginal = imagenCubo.getIconWidth();
		int altoOriginal = imagenCubo.getIconHeight();
		// ANALIZAR
		double escalaAncho = (double) 274 / anchoOriginal;
		double escalaAlto = (double) 112 / altoOriginal;
		double escala;
		if (escalaAlto < escalaAncho) {
			escala = escalaAlto;
		} else {
			escala = escalaAncho;
		}
		// NUEVAS DIMENCIONES AJUSTADAS AL JLABEL
		int nuevoAncho = (int) (anchoOriginal * escala);
		int nuevoAlto = (int) (altoOriginal * escala);
		// ESCALAR LA IMAGEN
		Image imagenEscalada = imagenCubo.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
		// AGREGAR LA IMAGEN AL JLABEL
		this.areaImagen.setIcon(new ImageIcon(imagenEscalada));
	}

	// METODO PARA LIMPIAR CAMPOS Y ESTABLECER VALORES POR DEFECTO
	private void limpiarCampos() {
		this.campoNombre.setText("");
		this.campoModelo.setText("");
		this.campoCaras.setText("");
		this.campoFecha.setDate(new Date());
		this.listaMarcas.setSelectedIndex(0);
		this.plastico.setSelected(true);
		this.listaMec.clearSelection();
		this.listaStickers.clear();
		this.listaAuxStickers.setSelectedIndex(0);
		this.rutaImagen = this.RUTA_IMG_DEFECTO;
		muestraImagen();
		this.campoPrecio.setText("");
	}

	// METODO PARA HABILITAR/DESABILITAR CAMPOS
	private void habilitarCampos(boolean habilitar) {
		this.campoNombre.setEditable(habilitar);
		this.campoModelo.setEditable(habilitar);
		this.campoCaras.setEditable(habilitar);
		this.campoFecha.setEnabled(habilitar);
		this.listaMarcas.setEnabled(habilitar);
		this.plastico.setEnabled(habilitar);
		this.madera.setEnabled(habilitar);
		this.metal.setEnabled(habilitar);
		this.stickers.setEnabled(habilitar);
		this.scrollSticker.setEnabled(habilitar);
		this.listaAuxStickers.setEnabled(habilitar);
		this.agregar.setEnabled(habilitar);
		this.quitar.setEnabled(habilitar);
		this.listaMec.setEnabled(habilitar);
		this.scrollMecanismo.setEnabled(habilitar);
		this.seleccionar.setEnabled(habilitar);
		this.campoPrecio.setEditable(habilitar);
	}

	// METODO PARA VERIFICAR LA LISTA PRINCPAL
	private void verificarLista() {
		boolean verifica = this.listaCubos.getItemCount() > 0;

		this.modificar.setEnabled(verifica);
		this.eliminar.setEnabled(verifica);
		this.modificarMenu.setEnabled(verifica);
		this.eliminarMenu.setEnabled(verifica);

		this.listaCubos.setEnabled(verifica);

		if (verifica) {
			listaCubos.setSelectedIndex(0);
			seleccionarCubo();
		}
	}

	// METODO INICIALIZAR
	private void inicializar() {
		this.habilitarCampos(false);
		this.habilitarBotones(true, true, false, true, false);
		// AGREGAR ELEMNTOS A LA LISTA DE LAS OPCIONES NO EXCLUYENTES FIJAS.
		// PUNTO 8
		this.tipoMecanismos.addElement("Cúbico");
		this.tipoMecanismos.addElement("Morphix");
		this.tipoMecanismos.addElement("Minx");
		this.tipoMecanismos.addElement("Skewb");
		this.tipoMecanismos.addElement("Square");
		this.tipoMecanismos.addElement("Curvy");
		this.tipoMecanismos.addElement("Engranajes");
		this.tipoMecanismos.addElement("Mixup");

		this.iniciarListaMarcas();
		this.iniciarListaStickers();
		this.iniciarListaCubos();

		this.verificarLista();

	}

	// METODO AUXILIAR PARA LLENAR LA LISTA PRINCIPAL
	private void iniciarListaCubos() {
		try {
			ArrayList<CuboRubik> cubos = cuboBD.consultarCubos();
			this.listaCubos.removeItemListener(this);
			this.listaCubos.removeAllItems();
			for (CuboRubik cubo : cubos) {
				this.listaCubos.addItem(cubo);
			}
			this.listaCubos.addItemListener(this);
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		} catch (RubiksWarningsException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}
	}

	// METODO AUXILIAR PARA LLENAR LA LISTA DE MARCAS
	private void iniciarListaMarcas() {
		try {
			ArrayList<String> marcasBD = cuboBD.consultarMarcas();
			this.listaMarcas.removeAllItems();
			for (String marca : marcasBD) {
				this.listaMarcas.addItem(marca);
			}
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}
	}

	// METODO AUXILIAR PARA LLENAR LA LISTA DE STICKERS
	private void iniciarListaStickers() {
		try {
			ArrayList<String> stickersBD = cuboBD.consultarStickers();
			this.listaAuxStickers.removeAllItems();
			for (String sticker : stickersBD) {
				this.listaAuxStickers.addItem(sticker);
			}
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}
	}

	// METODO PARA LA POLÍTICA DEL FOCO
	private void establecerFoco() {
		Vector<Component> componentes = new Vector<>();
		componentes.add(this.nuevo);
		componentes.add(this.modificar);
		componentes.add(this.guardar);
		componentes.add(this.eliminar);
		componentes.add(this.cancelar);
		componentes.add(this.listaCubos);
		componentes.add(this.campoNombre);
		componentes.add(this.campoModelo);
		componentes.add(this.campoCaras);
		componentes.add(this.campoFecha);
		componentes.add(this.plastico);
		componentes.add(this.scrollSticker);
		componentes.add(this.agregar);
		componentes.add(this.quitar);
		componentes.add(this.scrollMecanismo);
		componentes.add(this.seleccionar);
		componentes.add(this.campoPrecio);
		MiFocusTraversalPolicy politicaFoco = new MiFocusTraversalPolicy(componentes);
		this.setFocusTraversalPolicy(politicaFoco);
	}

}
