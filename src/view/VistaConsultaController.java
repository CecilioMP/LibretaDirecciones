package view;

import javafx.fxml.FXML;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.LibretaDirecciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Persona;
import javafx.scene.control.TableColumn;

public class VistaConsultaController {
	@FXML
	private TableView<Persona> tablaPersonas;
	@FXML
	private TableColumn<Persona, ?> nombreColumn;
	@FXML
	private TableColumn apellidosColumn;
	@FXML
	private TextField nombreField;
	@FXML
	private TextField apellidosField;
	@FXML
	private TextField direccionField;
	@FXML
	private TextField ciudadField;
	@FXML
	private TextField codigoPostalField;
	@FXML
	private TextField fechaDeNacimientoField;

	private LibretaDirecciones libretaDirecciones;
	private Stage escenarioEdicion;
	FilteredList<Persona> filteredData;
	SortedList<Persona> sortedData;
	
	public VistaConsultaController() {
	}
	
	public void initialize() {
	}
	
	public void setEscenarioEdicion(Stage escenarioEdicion) {
        this.escenarioEdicion = escenarioEdicion;
    }
	
	public void setLibretaDirecciones(LibretaDirecciones libretaDirecciones) {
        
        this.libretaDirecciones = libretaDirecciones;
        
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        
        filteredData = new FilteredList<>(libretaDirecciones.getDatosPersona(), p -> true);
        
        sortedData = new SortedList<>(filteredData);
        
        sortedData.comparatorProperty().bind(tablaPersonas.comparatorProperty());
        //Añado la lista obervable a la tabla
        tablaPersonas.setItems(sortedData);

        tablaPersonas.getSortOrder().setAll(Arrays.asList(nombreColumn));
    }
	
	/*
	public void buscarFiltro(String nombreBuscar , String apellidoBuscar , String direccionBuscar , String ciudadBuscar , String codigoBuscar, String fechaBuscar) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    filteredData.setPredicate(persona -> {
	        if (nombreBuscar.isEmpty() && apellidoBuscar.isEmpty() && direccionBuscar.isEmpty() && ciudadBuscar.isEmpty() && codigoBuscar.isEmpty() && fechaBuscar.isEmpty()) {
	            return true;
	        }
	        if (persona.getNombre().toLowerCase().contains(nombreBuscar.toLowerCase()) &&
	            persona.getApellidos().toLowerCase().contains(apellidoBuscar.toLowerCase()) &&
	            persona.getDireccion().toLowerCase().contains(direccionBuscar.toLowerCase()) &&
	            persona.getCiudad().toLowerCase().contains(ciudadBuscar.toLowerCase()) &&
	            Integer.toString(persona.getCodigoPostal()).contains(codigoBuscar) &&
	            persona.getFechaDeNacimiento().format(formatter).contains(fechaBuscar)
	        ) {
	            return true;
	        }
	        return false;
	    });
	}*/
	
	@FXML
	public void filtrarPersona() {
		String filtro = nombreField.getText().toLowerCase().trim();
	    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    filteredData.setPredicate(persona -> {
	        if (nombreField.getText().isEmpty()) {
	            return true;
	        }	        
	        Pattern pat = Pattern.compile(".*"+filtro+".*");
	        
	        Matcher mat = pat.matcher(persona.getNombre().toLowerCase().trim());
	        
	        return mat.matches();
	    });
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void crearPersona(ActionEvent event) {
		Persona temporal = new Persona();
        boolean guardarClicked = libretaDirecciones.muestraEditarPersona(temporal);
        if (guardarClicked) {
            libretaDirecciones.getDatosPersona().add(temporal);
        }
	}
	// Event Listener on Button.onAction
	@FXML
	public void editarPersona(ActionEvent event) {
		Persona seleccionada = (Persona) tablaPersonas.getSelectionModel().getSelectedItem();
		System.out.println(seleccionada);
        if (seleccionada != null) {
            boolean guardarClicked = libretaDirecciones.muestraEditarPersona(seleccionada);

        } else {
        	ResourceBundle resoruceBundle = libretaDirecciones.getBundle();
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            String titulo = resoruceBundle.getString("lbl.titulo");
            String header = resoruceBundle.getString("lbl.encabezado");
            String contenido = resoruceBundle.getString("lbl.contenidoAlerta");
            alerta.setTitle(titulo);
            alerta.setHeaderText(header);
            alerta.setContentText(contenido);
            alerta.showAndWait();
        }
	}
	// Event Listener on Button.onAction
	@FXML
	public void borrarPersona(ActionEvent event) {
		//Capturo el indice seleccionado y borro su item asociado de la tabla
        //int indiceSeleccionado = tablaPersonas.getSelectionModel().getSelectedIndex();
		Persona seleccionada = (Persona) tablaPersonas.getSelectionModel().getSelectedItem();
		
		int indiceSeleccionado = libretaDirecciones.getDatosPersona().indexOf(seleccionada);
		
        //System.out.println(indiceSeleccionado);
        if (indiceSeleccionado >= 0){
            //Borro item
           libretaDirecciones.getDatosPersona().remove(indiceSeleccionado);
            
        } else {
            //Muestro alerta  
        	ResourceBundle resoruceBundle = libretaDirecciones.getBundle(); //Le paso el idioma que tengo elegido en la LibretaDirecciones
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            String titulo = resoruceBundle.getString("lbl.titulo");
            String header = resoruceBundle.getString("lbl.encabezado");
            String contenido = resoruceBundle.getString("lbl.contenidoAlerta");
            alerta.setTitle(titulo);
            alerta.setHeaderText(header);
            alerta.setContentText(contenido);
                        
        }
	}
}
