package view;

import java.io.File;

import controller.LibretaDirecciones;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class VistaPrincipalController {

	private LibretaDirecciones libretaDirecciones;
	
	public void setLibretaDirecciones(LibretaDirecciones libretaDirecciones) {
		this.libretaDirecciones = libretaDirecciones;
	}
	
	@FXML
	public void idiomaEs() {
		libretaDirecciones.cambiarIdioma("es");
	}
	
	@FXML
	public void idiomaEn() {
		libretaDirecciones.cambiarIdioma("en");
	}
	
	@FXML
	public void abrirXML() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("XML files (*.xml)","*.xml");
		
		fileChooser.getExtensionFilters().add(exFilter);
		
		File archivo = fileChooser.showOpenDialog(libretaDirecciones.getPrimaryStage());
		
		if(archivo != null) {
			libretaDirecciones.cargarPersonas(archivo);
		}
	}
	
	@FXML
	public void crearInforme() {
		libretaDirecciones.listarPersonas();
	}
	
	@FXML
	public void guardarComo() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter("XML files (*.xml)","*.xml");
		fileChooser.getExtensionFilters().add(exFilter);
		
		File archivo = fileChooser.showSaveDialog(libretaDirecciones.getPrimaryStage());
		
		if (archivo!=null) {
			if(!archivo.getPath().endsWith(".xml")) {
				archivo = new File(archivo.getPath() + ".xml");
			}
			
			try {
				libretaDirecciones.guardarPersonas(archivo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void llamarConsultaDinamica() {
		libretaDirecciones.consultaDinamica();
	}
	
	
	
}
