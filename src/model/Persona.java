package model;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Persona {
    
    private final StringProperty nombre;
    private final StringProperty apellidos;
    private final StringProperty direccion;
    private final StringProperty ciudad;
    private final IntegerProperty codigoPostal;
    private final ObjectProperty<LocalDate> fechaDeNacimiento;
    
    public Persona() {
		this(null, null, null, null, 0, null);
	}
    
//    public Persona(String nombre, String apellidos) {
//        
//        this.nombre = new SimpleStringProperty(nombre);
//        this.apellidos = new SimpleStringProperty(apellidos);
//
//        this.direccion = new SimpleStringProperty("Mi dirección");
//        this.ciudad = new SimpleStringProperty("some city");
//        this.codigoPostal = new SimpleIntegerProperty(28440);
//        this.fechaDeNacimiento = new SimpleObjectProperty(LocalDate.of(1974, 6, 15));
//        
//    }
    
    public Persona(String nombre, String apellidos, String direccion, String ciudad, Integer codigoPostal, LocalDate fechaDeNacimiento) {
	      this.nombre = new SimpleStringProperty(nombre);
	      this.apellidos = new SimpleStringProperty(apellidos);
	
	      this.direccion = new SimpleStringProperty(direccion);
	      this.ciudad = new SimpleStringProperty(ciudad);
	      this.codigoPostal = new SimpleIntegerProperty(codigoPostal);
	      this.fechaDeNacimiento = new SimpleObjectProperty(fechaDeNacimiento);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public StringProperty apellidosProperty() {
        return apellidos;
    }

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public StringProperty direccionProperty() {
        return direccion;
    }
    
    public String getCiudad() {
        return ciudad.get();
    }

    public void setCiudad(String ciudad) {
        this.ciudad.set(ciudad);
    }

    public StringProperty ciudadProperty() {
        return ciudad;
    }

    public int getCodigoPostal() {
        return codigoPostal.get();
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal.set(codigoPostal);
    }

    public IntegerProperty codigoPostalProperty() {
        return codigoPostal;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento.get();
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento.set(fechaDeNacimiento);
    }

    public ObjectProperty fechaDeNacimientoProperty() {
        return fechaDeNacimiento;
    }

}