package cubo_rubik.utilerias;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import cubo_rubik.base_datos.CuboRubikBaseDatos;
import cubo_rubik.excepciones.BaseDatosException;

public class ReporteCubosRubik {
	
	public ReporteCubosRubik() {
		String ruta = this.seleccionarRutaReporte();
		if (!ruta.trim().isEmpty()) {
			try {
				CuboRubikBaseDatos bd = new CuboRubikBaseDatos();
				bd.crearReporte(ruta);
				JOptionPane.showMessageDialog(null, "El reporte ha sido generado exitosamente.", "Generar reporte", JOptionPane.INFORMATION_MESSAGE);
			} catch (BaseDatosException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), e.getTitulo(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private String seleccionarRutaReporte() {
		String ruta = "";
		JFileChooser generarPDF = new JFileChooser();
		generarPDF.setDialogTitle("Seleccione la ruta para guardar el archivo pdf");
		generarPDF.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filtroPDF = new FileNameExtensionFilter("Archivo PDF", "pdf");
		generarPDF.setFileFilter(filtroPDF);
		generarPDF.setAcceptAllFileFilterUsed(false);
		generarPDF.setCurrentDirectory(new File(System.getProperty("user.dir")));
		generarPDF.setMultiSelectionEnabled(false);
		generarPDF.setApproveButtonText("Guardar");
		generarPDF.setApproveButtonToolTipText("Selecione el directorio para guardar el archivo pdf.");

		int opcion = generarPDF.showOpenDialog(null);

		if (opcion == JFileChooser.APPROVE_OPTION) {
			File archivoPDF = generarPDF.getSelectedFile();
			ruta = archivoPDF.getAbsolutePath();
			if (!archivoPDF.getName().toLowerCase().endsWith(".pdf")) {
				ruta = archivoPDF.getAbsolutePath() + ".pdf";
				archivoPDF = new File(ruta);
			}
			if (archivoPDF.exists()) {
				int continuar = JOptionPane.showConfirmDialog(null,
						"El archivo PDF que quiere guardar ya existe,\n"
						+ "si desea guardarlo se sobreescribirá\n"
						+ "y perderá la información anterior.",
						"¿Desea sobreescribir el archivo?", JOptionPane.YES_NO_OPTION);
				if (continuar != JOptionPane.YES_OPTION) {
					return "";
				}
			}

		}
		
		return ruta;
	}
	
}
