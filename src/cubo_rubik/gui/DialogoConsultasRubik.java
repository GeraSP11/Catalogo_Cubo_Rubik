package cubo_rubik.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import com.toedter.calendar.JDateChooser;

import cubo_rubik.base_datos.CuboRubikBaseDatos;
import cubo_rubik.excepciones.BaseDatosException;
import cubo_rubik.utilerias.ResultSetTableModel;

public class DialogoConsultasRubik extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox casillaNombre;
	private JTextField nombre;

	private JCheckBox casillaModelo;
	private JTextField modelo;

	private JCheckBox casillaCaras;
	private JTextField noCaras;

	private JCheckBox casillaMarcas;
	private JComboBox<String> marcas;

	private JCheckBox casillaStickers;
	private JList<String> listaStickers;
	private JScrollPane scrollStickers;
	private DefaultListModel<String> defaultListaS;

	private JCheckBox casillaPrecio;
	private JSpinner precioInicio;
	private JSpinner precioFin;

	private JCheckBox casillaFecha;
	private JDateChooser fechaInicio;
	private JDateChooser fechaFin;

	private JButton botonConsultar;

	private JTable tablaCubos;

	private CuboRubikBaseDatos bd;

	private String CONSULTA_PREDETERMINADA = "SELECT cubos_rubik.nombreCubo AS Nombre, "
			+ "cubos_rubik.modeloCubo AS Modelo, " + "cubos_rubik.carasCubo AS 'No. Caras', "
			+ "marcas.nombreMarca AS Marca, " + "cubos_rubik.precioCubo AS Precio, "
			+ "cubos_rubik.materialCubo AS Material " + "FROM cubos_rubik "
			+ "JOIN marcas ON cubos_rubik.idMarca = marcas.idMarca";

	private ResultSetTableModel modeloTabla;

	public DialogoConsultasRubik(VentanaPrincipalRubik principal) {
		super(principal, "Consulta de Cubos Rubik", true);

		this.setLayout(new GridLayout(1, 2));
		this.setIconImage(principal.getIconImage());
		this.setSize(1000, 500);
		this.setLocationRelativeTo(principal);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		JPanel panelCriterios = new JPanel();
		panelCriterios.setLayout(new GridLayout(4, 2));
		JPanel panelAux;
		JPanel panel;

		// NOMBRE
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		this.casillaNombre = new JCheckBox("Nombre del cubo:");
		this.nombre = new JTextField();
		this.nombre.setPreferredSize(new Dimension(140, 20));
		panelAux.add(casillaNombre);
		panelAux.add(nombre);
		panelCriterios.add(panelAux);
		// MODELO
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		this.casillaModelo = new JCheckBox("Modelo:");
		this.modelo = new JTextField();
		this.modelo.setPreferredSize(new Dimension(140, 20));
		panelAux.add(this.casillaModelo);
		panelAux.add(this.modelo);
		panelCriterios.add(panelAux);
		// NO. CARAS
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		this.casillaCaras = new JCheckBox("Número de caras:");
		this.noCaras = new JTextField();
		this.noCaras.setPreferredSize(new Dimension(140, 20));
		panelAux.add(this.casillaCaras);
		panelAux.add(this.noCaras);
		panelCriterios.add(panelAux);
		// MARCA
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		this.casillaMarcas = new JCheckBox("Marca:");
		this.marcas = new JComboBox<String>();
		this.marcas.setPreferredSize(new Dimension(140, 20));
		this.marcas.setEditable(false);
		panelAux.add(this.casillaMarcas);
		panelAux.add(this.marcas);
		panelCriterios.add(panelAux);
		// STICKERS (OPCIONES NO EXCLUYENTES DINAMICAS)
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		this.casillaStickers = new JCheckBox("Stickers");
		panel.add(this.casillaStickers);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		this.defaultListaS = new DefaultListModel<>();
		this.listaStickers = new JList<>(this.defaultListaS);
		this.listaStickers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.scrollStickers = new JScrollPane(this.listaStickers);
		this.scrollStickers.setPreferredSize(new Dimension(120, 100));
		panel.add(this.scrollStickers);
		panelAux.add(panel);
		panelCriterios.add(panelAux);
		// PRECIO (NÚMERO CON RANGO)
		panelAux = new JPanel();
		panelAux.setLayout(new GridLayout(3, 1));
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		this.casillaPrecio = new JCheckBox("Precio");
		panel.add(this.casillaPrecio);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel rango = new JLabel("Inicial:");
		SpinnerNumberModel precioInicioModel = new SpinnerNumberModel(50, 50, 1500, 10);
		this.precioInicio = new JSpinner(precioInicioModel);
		this.precioInicio.setPreferredSize(new Dimension(100, 20));
		panel.add(rango);
		panel.add(this.precioInicio);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		rango = new JLabel("Máximo:");
		SpinnerNumberModel precioFinModel = new SpinnerNumberModel(1500, 50, 1500, 10);
		this.precioFin = new JSpinner(precioFinModel);
		this.precioFin.setPreferredSize(new Dimension(100, 20));
		panel.add(rango);
		panel.add(this.precioFin);
		panelAux.add(panel);
		panelCriterios.add(panelAux);
		// FECHA
		panelAux = new JPanel();
		panelAux.setLayout(new GridLayout(3, 1));
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		this.casillaFecha = new JCheckBox("Fecha de lanzamiento");
		panel.add(this.casillaFecha);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel rangoF = new JLabel("Fecha inicial:");
		this.fechaInicio = new JDateChooser();
		this.fechaInicio.setPreferredSize(new Dimension(100, 20));
		panel.add(rangoF);
		panel.add(this.fechaInicio);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		rangoF = new JLabel("Ultima fecha:");
		this.fechaFin = new JDateChooser();
		this.fechaFin.setPreferredSize(new Dimension(100, 20));
		panel.add(rangoF);
		panel.add(this.fechaFin);
		panelAux.add(panel);
		panelCriterios.add(panelAux);
		// BOTON CONSULTAR
		panelAux = new JPanel();
		panelAux.setLayout(new GridLayout(3, 1));
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		this.botonConsultar = new JButton("Consultar");
		this.botonConsultar.addActionListener(this);
		panel.add(this.botonConsultar);
		panelAux.add(panel);
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panelAux.add(panel);
		panelCriterios.add(panelAux);

		this.add(panelCriterios);

		try {
			bd = new CuboRubikBaseDatos();
			inicializar();
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}

		try {
			modeloTabla = new ResultSetTableModel(bd.getCONTROLADOR(), bd.getUrl(), CONSULTA_PREDETERMINADA);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Faltan controladores", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Extracción de Datos", JOptionPane.ERROR_MESSAGE);
		}

		this.tablaCubos = new JTable(modeloTabla);
		JScrollPane scroll = new JScrollPane(this.tablaCubos);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(450, 420));
		panel = new JPanel();
		panel.add(scroll);
		this.add(panel);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				modeloTabla.desconectar();
				dispose();
			}
		});

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(botonConsultar)) {
			consultar();
		}
	}

	private void inicializar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, Calendar.JANUARY, 1);
		this.fechaInicio.setDate(calendar.getTime());
		this.fechaFin.setDate(new Date());

		try {
			ArrayList<String> arregloAux = bd.consultarMarcas();
			for (String marca : arregloAux) {
				this.marcas.addItem(marca);
			}

			arregloAux = bd.consultarStickers();
			for (String sticker : arregloAux) {
				this.defaultListaS.addElement(sticker);
			}
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void consultar() {
		try {
			String consulta = CONSULTA_PREDETERMINADA + " WHERE 1=1 ";

			if (this.casillaNombre.isSelected()) {
				String nombre = this.nombre.getText().trim();
				consulta += "AND cubos_rubik.nombreCubo LIKE '%" + nombre + "%' ";
			}
			if (this.casillaModelo.isSelected()) {
				String modelo = this.modelo.getText().trim();
				consulta += "AND cubos_rubik.modeloCubo LIKE '%" + modelo + "%' ";
			}
			if (this.casillaCaras.isSelected()) {
				String caras = this.noCaras.getText().trim();
				consulta += "AND cubos_rubik.carasCubo LIKE '%" + caras + "%' ";
			}
			if (this.casillaMarcas.isSelected()) {
				String marca = this.marcas.getSelectedItem().toString().trim();
				int id = bd.consultarIdMarca(marca);
				consulta += "AND cubos_rubik.idMarca = " + id + " ";
			}
			if (this.casillaStickers.isSelected()) {
				ArrayList<String> stickers = new ArrayList<>(this.listaStickers.getSelectedValuesList());
				int id = 0;
				for(String sticker: stickers) {
					id = bd.consultarIdStickerAUX(sticker);
					if (id != 0){
						consulta += "AND cubos_rubik.idCubo IN (SELECT cuboStickers.idCubo FROM cuboStickers WHERE cuboStickers.idSticker = " + id + ") ";
					}
				}
			}
			if (this.casillaPrecio.isSelected()) {
				int precioInferior = (Integer) this.precioInicio.getValue();
				int precioSuperior = (Integer) this.precioFin.getValue();
				consulta += "AND cubos_rubik.precioCubo BETWEEN "+precioInferior+" AND "+precioSuperior+" ";
			}
			if (this.casillaFecha.isSelected()) {
				long fechaInferior = this.fechaInicio.getDate().getTime();
				long fechaSuperior = this.fechaFin.getDate().getTime();
				consulta += "AND cubos_rubik.fechaCreacion BETWEEN "+fechaInferior+" AND "+fechaSuperior+" ";
			}
			
			modeloTabla.establecerConsulta(consulta);
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Sucedio un problema a la hora de consultar en la base de datos.",
					"Extracción de Datos", JOptionPane.ERROR_MESSAGE);
		} catch (BaseDatosException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
		} catch (ClassCastException e) {
			JOptionPane.showMessageDialog(this, "Solo se permiten números enteros para el rango de precios",
					"Solo números", JOptionPane.WARNING_MESSAGE);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Por favor selecione o de formato a la fecha.",
					"Fecha descrita incorrecta", JOptionPane.WARNING_MESSAGE);
		}

	}
}
