/*
PARADIGMAS DE PROGRAMACIÓN
MI APLICACIÓN
GRUPO 412
GERARDO CORTEZ RAMOS
*/

package cubo_rubik.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class VentanaPrincipalRubik extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JMenu menuArchivo;
	private JMenuItem abrir;
	private JMenuItem guardar;
	private JMenuItem salir;
	
	private JMenu menuOperaciones;
	private JMenuItem catalogo;
	private JMenuItem consultas;
	
	private JMenu menuAyuda;
	private JMenuItem acercaDe;
	
	private JMenuBar barraMenu;
	
	// CONSTRUCTOR DE LA VENTANA PRINCIPAL
	public VentanaPrincipalRubik() {
		
		super("Cubo de Rubik");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/cubo_rubik/imagenes/logo.png")));
		
		// MEÚ ARCHIVO
		menuArchivo = new JMenu("Archivo");
		menuArchivo.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/archivo.png")));
		menuArchivo.setMnemonic(KeyEvent.VK_A);
		menuArchivo.setToolTipText("Manejo de arcivos");

		// ELEMENTOS DEL MENÚ ARCHIVO
		abrir = new JMenuItem("Abrir");
		abrir.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/abrir.png")));
		abrir.setMnemonic(KeyEvent.VK_R);
		abrir.setToolTipText("Buscar archivos");
		abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		abrir.addActionListener(new ManejoEventos());
		
		guardar = new JMenuItem("Guardar");
		guardar.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/guardar.png")));
		guardar.setMnemonic(KeyEvent.VK_G);
		guardar.setToolTipText("Salvar archivos");
		guardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
		guardar.addActionListener(new ManejoEventos());
		
		salir = new JMenuItem("Salir");
		salir.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/salir.png")));
		salir.setMnemonic(KeyEvent.VK_S);
		salir.setToolTipText("Salir de la aplicación");
		salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
		salir.addActionListener(new ManejoEventos());
		
		menuArchivo.add(abrir);
		menuArchivo.add(guardar);
		menuArchivo.addSeparator();
		menuArchivo.add(salir);
		
		// MENÚ OPERACIONES
		menuOperaciones = new JMenu("Operaciones");
		menuOperaciones.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/operaciones.png")));
		menuOperaciones.setMnemonic(KeyEvent.VK_O);
		menuOperaciones.setToolTipText("Manejo de cubos");

		// ELEMENTOS DEL MENÚ OPERACIONES
		catalogo = new JMenuItem("Catálogo");
		catalogo.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/catalogo.png")));
		catalogo.setMnemonic(KeyEvent.VK_T);
		catalogo.setToolTipText("Ver cubos");
		catalogo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,0));
		catalogo.addActionListener(new ManejoEventos());
		
		consultas = new JMenuItem("Consultas");
		consultas.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/consultas.png")));
		consultas.setMnemonic(KeyEvent.VK_N);
		consultas.setToolTipText("Buscar cubos");
		consultas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
		consultas.addActionListener(new ManejoEventos());
		
		menuOperaciones.add(catalogo);
		menuOperaciones.addSeparator();
		menuOperaciones.add(consultas);
		
		// MENÚ AYUDA
		menuAyuda = new JMenu("Ayuda");
		menuAyuda.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/ayuda.png")));
		menuAyuda.setMnemonic(KeyEvent.VK_Y);
		menuAyuda.setToolTipText("Soporte");

		// ELEMENTOS DEL MENÚ AYUDA
		acercaDe = new JMenuItem("Acerca de...");
		acercaDe.setIcon(new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/acercaDe.png")));
		acercaDe.setMnemonic(KeyEvent.VK_D);
		acercaDe.setToolTipText("Créditos");
		acercaDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0));
		acercaDe.addActionListener(new ManejoEventos());
				
		menuAyuda.add(acercaDe);
				
		// BARRA DE MENUS
		barraMenu = new JMenuBar();
		barraMenu.add(menuArchivo);
		barraMenu.add(menuOperaciones);
		barraMenu.add(menuAyuda);
		this.setJMenuBar(barraMenu);
		
		// CONFIGURACIÓN DE LA VENTANA
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		this.getContentPane().setLayout(new FlowLayout());
		JLabel fondo = new JLabel();
		ImageIcon imagenFondo = new ImageIcon(getClass().getResource(
				"/cubo_rubik/imagenes/fondo.jpg"));
		Image imagenEscalada = imagenFondo.getImage().getScaledInstance(-1,
				getSize().height - 100, Image.SCALE_SMOOTH);
		fondo.setIcon(new ImageIcon(imagenEscalada));
		this.getContentPane().add(fondo);
		this.getContentPane().setBackground(Color.WHITE);

		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// EVENTOS WINDOWLISTENER
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
				
			}
		});

		this.setVisible(true);
	}
	
	// ACTIONLISTENER PARA LOS BOTONES DEL MENU
	private class ManejoEventos implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(abrir)) {
				abrir();
			} else if (e.getSource().equals(guardar)) {
				guardar();
			} else if (e.getSource().equals(salir)) {
				salir();
			} else if (e.getSource().equals(catalogo)) {
				catalogo();
			} else if (e.getSource().equals(consultas)) {
				consultas();
			} else if (e.getSource().equals(acercaDe)) {
				acercaDe();
			}
			
		}
	}
	
	// MÉTODO EN ACERCA DE..
	private void acercaDe() {
		JOptionPane.showMessageDialog(this, "MiAplicación - Cubos de Rubik v.1.0" + "\n\n"
				+ "Desarrollada por:"
				+ "\nGerardo Cortez Ramos" + "\n\n"
				+ "Derechos reservados GERA #412 " + '\u00A9' + " 2024",
				"Acerca de... Cubo de Rubik", JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(getClass().getResource("/cubo_rubik/imagenes/logo.png")));
	}
	
	// METODOS DEL MENU ARCHIVO
	private void salir() {
		System.exit(0);
	}
	
	private void abrir() {
		
	}
	
	private void guardar() {
		
	}
	
	private void catalogo() {
		new DialogoConsultasRubik(this);
	}
	
	private void consultas() {
		
	}
		
}
