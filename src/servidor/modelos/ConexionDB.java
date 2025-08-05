/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Johan
 */
public class ConexionDB {

    private static final String connectionUrl = "jdbc:mysql://localhost:3306/prueba_sistemasdistribuido";
    private static final String usuario = "root";
    private static final String contraseña = "";

    public static Connection obtenerConexion() throws SQLException {

            Connection conn = DriverManager.getConnection(connectionUrl, usuario, contraseña);
            System.out.println("Conexión a la base de datos exitosamente");
            return conn;
       
    }

}
