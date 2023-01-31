package view;

import java.util.ResourceBundle;

import controller.LibretaDirecciones;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Persona;
import util.UtilidadDeFechas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class VistaPersonaController {
    
    @FXML
    private TableView tablaPersonas;
    @FXML
    private TableColumn nombreColumn;
    @FXML
    private TableColumn apellidosColumn;

    @FXML
    private Label nombreLabel;
    @FXML
    private Label apellidosLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label codigoPostalLabel;
    @FXML
    private Label ciudadLabel;
    @FXML
    private Label fechaDeNacimientoLabel;

    // Referencia a la clase principal
    private LibretaDirecciones libretaDirecciones;
    
    //El constructor es llamado ANTES del método initialize
    public VistaPersonaController() {
    }

    //Inicializa la clase controller y es llamado justo DESPUÉS de cargar el archivo FXML
    @FXML
    private void initialize() {
        
        //Inicializo la tabla con las dos primera columnas
        String nombre = "nombre";
        String apellidos = "apellidos";
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>(nombre));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>(apellidos));
        
        //Borro los detalles de la persona
        mostrarDetallesPersona(null);
        
        //Escucho cambios en la selección de la tabla y muestro los detalles en caso de cambio
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesPersona(newValue));
    }

    //Es llamado por la apliación principal para tener una referencia de vuelta de si mismo
    public void setLibretaDirecciones(LibretaDirecciones libretaDirecciones) {
        
        this.libretaDirecciones = libretaDirecciones;

        //Añado la lista obervable a la tabla
        tablaPersonas.setItems(libretaDirecciones.getDatosPersona());
    }
    
  //Muestra los detalles de la persona seleccionada
    private void mostrarDetallesPersona(Object newValue) {
        
        if (newValue != null) {
            //Relleno los labels desde el objeto persona
            nombreLabel.setText(((Persona) newValue).getNombre());
            apellidosLabel.setText(((Persona) newValue).getApellidos());
            direccionLabel.setText(((Persona) newValue).getDireccion());
            codigoPostalLabel.setText(Integer.toString(((Persona) newValue).getCodigoPostal()));
            ciudadLabel.setText(((Persona) newValue).getCiudad());
            //TODO: Tenemos que convertir la fecha de nacimiento en un String 
            fechaDeNacimientoLabel.setText(UtilidadDeFechas.formato(((Persona) newValue).getFechaDeNacimiento()));
        } else {
            //Persona es null, vacío todos los labels.
            nombreLabel.setText("");
            apellidosLabel.setText("");
            direccionLabel.setText("");
            codigoPostalLabel.setText("");
            ciudadLabel.setText("");
            fechaDeNacimientoLabel.setText("");
        }
    }
    
    //Borro la persona seleccionada cuando el usuario hace clic en el botón de Borrar
    @FXML
    private void borrarPersona() {
        //Capturo el indice seleccionado y borro su item asociado de la tabla
        int indiceSeleccionado = tablaPersonas.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0){
            //Borro item
            tablaPersonas.getItems().remove(indiceSeleccionado);           
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
    
  //Muestro el diálogo editar persona cuando el usuario hace clic en el botón de Crear
    @FXML
    private void crearPersona() {
        Persona temporal = new Persona();
        boolean guardarClicked = libretaDirecciones.muestraEditarPersona(temporal);
        if (guardarClicked) {
            libretaDirecciones.getDatosPersona().add(temporal);
        }
    }
    
    //Muestro el diálogo editar persona cuando el usuario hace clic en el botón de Editar
    @FXML
    private void editarPersona() {
        Persona seleccionada = (Persona) tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            boolean guardarClicked = libretaDirecciones.muestraEditarPersona(seleccionada);
            if (guardarClicked) {
                mostrarDetallesPersona(seleccionada);
            }

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
    
}