/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenciasPreparadas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alumno
 */
public class Conexion {

    private static final String BbddName = "matricula";
    private static final String bbddUrl = "jdbc:mysql://localhost/" + BbddName;//se usa la base de datos america,facilitada por el script
    private static final String usuario = "root";
    private static final String clave = "";

    private static Connection conn = null;

    public static Connection conectar() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(bbddUrl, usuario, clave);
            System.out.println("Se ha conectado a la base de datos");

        } catch (Exception ex) {
            System.out.println("Error al conectar con la base de datos.\n"
                    + ex.getMessage().toString());
        }

        return conn;
    }

    //metodo para cerrar la conexion a la bbdd
    public static void cerrarConexion() {
        try {
            conn.close();
            System.out.println("Se ha cerrado la conexion a la base de datos");
        } catch (SQLException e) {
            System.out.println("la conexion no se ha cerrado");
            System.out.println(e.getMessage().toString());
        }
    }

}
