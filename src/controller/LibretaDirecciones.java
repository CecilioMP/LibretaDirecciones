package controller;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Persona;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import util.ConexionSql;
import view.EditarPersonaController;
import view.VistaConsultaController;
import view.VistaPersonaController;
import view.VistaPrincipalController;

public class LibretaDirecciones extends Application {
    
    private ObservableList<Persona> datosPersona = FXCollections.observableArrayList();
    private Stage escenarioPrincipal;
    private BorderPane layoutPrincipal;
    private AnchorPane vistaPersona;
    private ResourceBundle vistaBundle = ResourceBundle.getBundle("i18n/idioma_es"); //Para poner por defecto el idioma del programa en ESPAÑOL
    private ConexionSql conexionSql;
    
    //Datos de ejemplo
    public LibretaDirecciones(){
        
//        datosPersona.add(new Persona("Jairo", "García Rincón"));
//        datosPersona.add(new Persona("Juan", "Pérez Martínez"));
//        datosPersona.add(new Persona("Andrea", "Chenier López"));
//        datosPersona.add(new Persona("Emilio", "González Pla"));
//        datosPersona.add(new Persona("Mónica", "de Santos Sánchez"));
    }
    //Método para devolver los datos como lista observable de personas
    public ObservableList<Persona> getDatosPersona() {
        return datosPersona;
    }
    
    @Override
    public void start(Stage escenarioPrincipal){
    	
    	try {
			conexionSql = new ConexionSql("jdbc:mysql://127.0.0.1:3306/dam?useSSL=false", "dam", "dam");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//Cargo personas de la base de datos (borrando las anteriores)
    	datosPersona.clear();
    	try {
			datosPersona.addAll(conexionSql.getPersonas());
			conexionSql.cerrar();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        //Debo hacerlo para que luego me funcione en l carga de escenas
        this.escenarioPrincipal = escenarioPrincipal;
        
        //Establezco el título
        this.escenarioPrincipal.setTitle("Libreta de direcciones");
        
        //Establezco el icono de aplicación
        this.escenarioPrincipal.getIcons().add(new Image("file:img/libretaDirecciones.png"));
        
        //Inicializo el layout principal
        initLayoutPrincipal();
        
        //Muestro la vista persona
        muestraVistaPersona();
        
        escenarioPrincipal.setOnCloseRequest(event -> {
        	String btnSi = vistaBundle.getString("lbl.btn.si");
            String btnNo = vistaBundle.getString("lbl.btn.no");
            ButtonType si = new ButtonType(btnSi, ButtonData.OK_DONE);
            ButtonType no = new ButtonType(btnNo, ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(AlertType.CONFIRMATION,"",si,no);

            String titulo = vistaBundle.getString("lbl.exit");
            String headerText = vistaBundle.getString("lbl.exit2");

            alert.setTitle(titulo);
            alert.setHeaderText(headerText);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == no)
               event.consume();
        }); 

        
    }
    
    //Cambiar el idioma del programa
    public void cambiarIdioma(String idioma) {
    	vistaBundle = ResourceBundle.getBundle("i18n/idioma_"+idioma);
    	
    	initLayoutPrincipal();
    	muestraVistaPersona();
    }
    
    public void initLayoutPrincipal(){
        
        //Cargo el layout principal a partir de la vista VistaPrincipal.fxml
        FXMLLoader loader = new FXMLLoader();
        URL location = LibretaDirecciones.class.getResource("/view/VistaPrincipal.fxml");
        loader.setResources(vistaBundle);
        loader.setLocation(location);
        try {
            layoutPrincipal = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Cargo y muestro la escena que contiene ese layout principal
        Scene escena = new Scene(layoutPrincipal);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
        
        VistaPrincipalController controller = loader.getController();
        controller.setLibretaDirecciones(this);
    }
    
	public void muestraVistaPersona(){
	        
	        //Cargo la vista persona a partir de VistaPersona.fxml
	        FXMLLoader loader = new FXMLLoader();
	        URL location = LibretaDirecciones.class.getResource("/view/VistaPersona.fxml");
	        loader.setResources(vistaBundle);
	        loader.setLocation(location);
	        try {
	            vistaPersona = loader.load();
	        } catch (IOException ex) {
	            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	        //Añado la vista al centro del layoutPrincipal
	        layoutPrincipal.setCenter(vistaPersona);
	        
	        //Doy acceso al controlador VistaPersonaCOntroller a LibretaDirecciones
	        VistaPersonaController controller = loader.getController();
	        controller.setLibretaDirecciones(this);
	        
	}
	
	//Vista editarPersona
    public boolean muestraEditarPersona(Persona persona) {
        
        //Cargo la vista persona a partir de VistaPersona.fxml
        FXMLLoader loader = new FXMLLoader();
        URL location = LibretaDirecciones.class.getResource("/view/EditarPersona.fxml");
        loader.setResources(vistaBundle);
        loader.setLocation(location);
        Parent editarPersona; // creada porque me daba error en editarPersona!!!!!!!!
        try {
            editarPersona = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        //Creo el escenario de edición (con modal) y establezco la escena
        Stage escenarioEdicion = new Stage();
        escenarioEdicion.setTitle("Editar Persona");
        escenarioEdicion.initModality(Modality.WINDOW_MODAL);
        escenarioEdicion.initOwner(escenarioPrincipal);
        Scene escena = new Scene(editarPersona);
        escenarioEdicion.setScene(escena);
        
        //Asigno el escenario de edición y la persona seleccionada al controlador
        EditarPersonaController controller = loader.getController();
        controller.setEscenarioEdicion(escenarioEdicion);
        controller.setPersona(persona);

        //Muestro el diálogo ahjsta que el ussuario lo cierre
        escenarioEdicion.showAndWait();
        
        //devuelvo el botón pulsado
        return controller.isGuardarClicked();
    
    }
    //Vista VistaConsulta
    public void consultaDinamica() {
    	//Cargo la vista persona a partir de VistaConsulta.fxml
        FXMLLoader loader = new FXMLLoader();
        URL location = LibretaDirecciones.class.getResource("/view/VistaConsulta.fxml");
        loader.setResources(vistaBundle);
        loader.setLocation(location);
        Parent VistaConsulta = null; // creada porque me daba error en editarPersona!!!!!!!!
        try {
        	VistaConsulta = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Creo el escenario de edición (con modal) y establezco la escena
        Stage escenarioConsulta = new Stage();
        escenarioConsulta.setTitle("Consulta Dinamica");
        escenarioConsulta.initModality(Modality.WINDOW_MODAL);
        escenarioConsulta.initOwner(escenarioPrincipal);
        Scene escena = new Scene(VistaConsulta);
        escenarioConsulta.setScene(escena);
        
        //Asigno el escenario de edición
        VistaConsultaController controller = loader.getController();
        controller.setEscenarioEdicion(escenarioConsulta);
        controller.setLibretaDirecciones(this);

        //Muestro el diálogo ahjsta que el ussuario lo cierre
        escenarioConsulta.showAndWait();
    }
    
    public void cargarPersonas (File archivo) {
    	datosPersona.clear();
    	
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(archivo.getAbsolutePath());
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Persona person = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an item element, we create a new item
                        String elementName = startElement.getName().getLocalPart();
                        switch (elementName) {
                        case "persona":
                            person = new Persona();
                            break;
                        case "nombre":
                            event = eventReader.nextEvent();
                            person.setNombre(event.asCharacters().getData());
                            break;
                        case "apellidos":
                            event = eventReader.nextEvent();
                            person.setApellidos(event.asCharacters().getData());
                            break;
                        case "direccion":
                            event = eventReader.nextEvent();
                            person.setDireccion(event.asCharacters().getData());
                            break;
                        case "codigoPostal":
                            event = eventReader.nextEvent();
                            person.setCodigoPostal(Integer.parseInt(event.asCharacters().getData()));
                            break;
                        case "ciudad":
                            event = eventReader.nextEvent();
                            person.setCiudad(event.asCharacters().getData());
                            break;
                        case "fechaDeNacimiento":
                            event = eventReader.nextEvent();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            person.setFechaDeNacimiento(LocalDate.parse(event.asCharacters().getData(), formatter));
                            break;
                        }
                }
                    // If we reach the end of an item element, we add it to the list
                    if (event.isEndElement()) {
                        EndElement endElement = event.asEndElement();
                        if (endElement.getName().getLocalPart().equals("persona")) {
                            datosPersona.add(person);
                        }
                    }

            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public void guardarPersonas(File archivo){

        List<Persona> listaPersonas = getDatosPersona();



        try {
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            StringWriter stringWriter = new StringWriter();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(new FileOutputStream(archivo));

            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("personas");


            for(Persona p : listaPersonas) {
                xMLStreamWriter.writeStartElement("persona");
                xMLStreamWriter.writeStartElement("fechaDeNacimiento");
                xMLStreamWriter.writeCharacters(String.valueOf(p.getFechaDeNacimiento()));
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("ciudad");
                xMLStreamWriter.writeCharacters(p.getCiudad());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("nombre");
                xMLStreamWriter.writeCharacters(p.getNombre());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("apellidos");
                xMLStreamWriter.writeCharacters(p.getApellidos());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("codigoPostal");
                xMLStreamWriter.writeCharacters(String.valueOf(p.getCodigoPostal()));
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("direccion");
                xMLStreamWriter.writeCharacters(p.getDireccion());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();
            }

            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();
            //setRutaArchivoPersonas(archivo);

        }catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void listarPersonas() {
    	String reportFuente = "C:\\Users\\Ceci\\JaspersoftWorkspace\\MyReports\\Leaf_Red.jrxml";
    	String reportPDF = "src/informes/Informe.pdf";
    	
    	Map<String, Object> parametros = new HashMap<String, Object>();
    	/*
		parametros.put("titulo", "LISTADO DE DEPARTAMENTOS.");
		parametros.put("autor", "Jose");
		parametros.put("fecha", (new java.util.Date()).toString());*/
    	
    	try {
    		 //Compilar la plantilla
    		 JasperReport jasperReportCompilado = JasperCompileManager.compileReport(reportFuente);

             conexionSql = new ConexionSql("jdbc:mysql://127.0.0.1:3306/dam?useSSL=false", "dam", "dam");

    		 Connection conexion = conexionSql.devolverConecxion();
    		 //Metodo para rellenar de datos el informe, genera un fichero .jrprint
    		 //parametros (nombre objeto Jaspert Report, parametros de informe, conexion a BD)
    		 JasperPrint MiInforme = JasperFillManager.fillReport(jasperReportCompilado, parametros, conexion);

             conexion.close();
    		 /*Exportar el fichero JasperPrint*/
    		 // Visualizar en pantalla(consola)
    		 JasperViewer.viewReport(MiInforme);
    		 // Convertir a PDF(generar informe en PDF)
    		 JasperExportManager.exportReportToPdfFile(MiInforme, reportPDF);
    		 System.out.println("ARCHIVOS CREADOS");
    		 
    		 } catch (SQLException e) {
    		 System.out.println("Error al ejecutar sentencia SQL");
    		 } catch (JRException ex) {
    		 System.out.println("Error Jasper.");
    		 ex.printStackTrace();
    		 }
    		 
    }
    
    //Invoco el método getPrimaryStage para que devuelva mi escenario pñrincipal
    public Stage getPrimaryStage() {
        return escenarioPrincipal;
    }

    //Método main
    public static void main(String[] args) {
        launch(args);
    }
    
    public ResourceBundle getBundle() {
    	return this.vistaBundle;
    }
    
}