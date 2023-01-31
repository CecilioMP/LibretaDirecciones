package util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Persona;

/**
 *
 * @author jairogarciarincon
 */
public class ConexionSql {
    
    private Connection conexion ;

    public Connection devolverConecxion(){
        return conexion;
    }

    public ConexionSql(String dbURL, String usuario, String password) throws SQLException {
        
        conexion = DriverManager.getConnection(dbURL, usuario, password);
        System.out.println("Base de datos conectada");
        
    }

    public void cerrar() throws SQLException {
        if (conexion != null) {
            conexion.close();
            System.out.println("Base de datos desconectada");
        }
    }

    public List getPersonas() throws SQLException{
        
        Statement stmnt = conexion.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM personas");

        List<Persona> personas = new ArrayList<>();
        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
            String direccion = rs.getString("direccion");
            String ciudad = rs.getString("ciudad");
            int codigoPostal = rs.getInt("codigoPostal");
            Date fechaDeNacimiento = rs.getDate("fechaDeNacimiento");
            Persona persona = new Persona(nombre, apellidos, direccion, ciudad, codigoPostal, fechaDeNacimiento.toLocalDate());
            personas.add(persona);
        }
        rs.close();
        stmnt.close();
        return personas;

    }
    
    public void putPersonas(List<Persona> personas) throws SQLException{
        
        
        //Borro todas
        Statement stmnt = conexion.createStatement();
        stmnt.executeUpdate("DELETE FROM personas");
        stmnt.close();
        //Preparo el statement
        String query = "INSERT INTO personas (nombre,apellidos,direccion,ciudad,codigoPostal,fechaDeNacimiento) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(query);
        //Añado cada persona
        for (Persona persona : personas){
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidos());
            ps.setString(3, persona.getDireccion());
            ps.setString(4, persona.getCiudad());
            ps.setInt(5, persona.getCodigoPostal());
            ps.setDate(6, java.sql.Date.valueOf(persona.getFechaDeNacimiento()));
            ps.execute();
        }
        ps.close();
        System.out.println("Datos guardados en Base de datos");
       
        
    }
    
}