/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenciasPreparadas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

/**
 *
 * @author Morad
 */
public class Main {

    public static void main(String[] args) {

        //usamos la clase conexion auxiliar para poder conectarnos a la base de datos
        Connection con = Conexion.conectar();
        //para poder meter datos por teclado
        Scanner sc = new Scanner(System.in);

        //menu para seleccionar las opciones
        int opcion;
        do {
            System.out.println("");
            System.out.println("Tarea 5 - BBDD y JAVA");
            System.out.println("1. Crear un nuevo alumno y almacenarlo en BD");
            System.out.println("2. Busca al alumno nuevo y muestra sus datos por pantalla");
            System.out.println("3. Borrar un alumno existente");
            System.out.println("4. Matricular al alumno nuevo en varias asignaturas");
            System.out.println("5. Consultar las asignaturas para las que está matriculado el nuevo alumno y mostrarlas por pantalla");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    //Crear un nuevo alumno y almacenarlo en BD
                    insertarRegistro(con, sc);
                    break;
                case 2:
                    //Busca al alumno nuevo y muestra sus datos por pantalla
                    mostrarUnAlumno(con, sc);
                    break;
                case 3:
                    //Borrar un alumno existente
                    eliminarRegistro(con, sc);
                    break;
                case 4:
                    //Matricular al alumno nuevo en varias asignaturas
                    matricular(con, sc);
                    break;
                case 5:
                    //Consultar las asignaturas para las que está matriculado el nuevo alumno y mostrarlas por pantalla
                    mostrarAlumno_Asignaturas(con, sc);
                    break;
                case 6:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (opcion != 6);
        
        //cerramos la conexion a la base de datos
        Conexion.cerrarConexion();

    }

    //Dar de alta un alumno: se deberán proporcionar los datos del alumno
    public static void insertarRegistro(Connection con, Scanner sc) {

        System.out.println("Introduce un id de alumno");
        int id_alumno = sc.nextInt();
        System.out.println("Introduce apellidos");
        String apellidos = sc.next();
        System.out.println("Introduce nombre");
        String nombre = sc.next();
        System.out.println("Introduce curso");
        int curso = sc.nextInt();
        System.out.println("Introduce titulacion");
        int titulacion = sc.nextInt();

        String query = "INSERT INTO alumnos(id_alumno,apellidos,nombre, curso,titulacion) VALUES (?,?,?,?,?)";

        PreparedStatement psInsert = null;

        try {

            psInsert = con.prepareStatement(query);

            psInsert.setInt(1, id_alumno);
            psInsert.setString(2, apellidos);
            psInsert.setString(3, nombre);
            psInsert.setInt(4, curso);
            psInsert.setInt(5, titulacion);

            psInsert.executeUpdate();
            System.out.println("Alumno insertado exitosamente");
            psInsert.close();

        } catch (SQLException e) {
            System.out.println("Error al insertar registro");

        }
    }
    
    //Busca al alumno nuevo y muestra sus datos por pantalla
    public static void mostrarUnAlumno(Connection con, Scanner sc) {
        System.out.println("Introduce un id de alumno");
        int id_alumno = sc.nextInt();

        String query = "SELECT * FROM alumnos where id_alumno=?";
        PreparedStatement psMostrar = null;

        try {
            psMostrar = con.prepareStatement(query);
            psMostrar.setInt(1, id_alumno);

            ResultSet rs = psMostrar.executeQuery();
            if (rs.next()) {
                System.out.println("Datos del alumno");
                id_alumno = rs.getInt("id_alumno");
                String apellidos = rs.getString("apellidos");
                String nombre = rs.getString("nombre");
                int curso = rs.getInt("curso");
                int titulacion = rs.getInt("titulacion");

                System.out.println("id_alumno: " + id_alumno + " Apellidos: "
                        + apellidos + " Nombre: " + nombre + " Curso: " + curso
                        + " Titulacion: " + titulacion);

                psMostrar.close();
                rs.close();
            } else {
                System.out.println("No se encontraron datos del alumno con id: " + id_alumno);

            }

        } catch (SQLException e) {
            System.out.println("Error al obtener las asignaturas del alumno");

        }

    }

    //Eliminar un alumno: se deberá proporcionar el id del alumno
    public static void eliminarRegistro(Connection con, Scanner sc) {

        System.out.println("Introduce un id de alumno");
        int id_alumno = sc.nextInt();

        String query = "delete from alumnos where id_alumno=? ";

        PreparedStatement psDelete = null;

        try {

            psDelete = con.prepareStatement(query);

            psDelete.setInt(1, id_alumno);

            psDelete.executeUpdate();
            System.out.println("Alumno eliminado correctamente");
            psDelete.close();

        } catch (SQLException e) {
            System.out.println("Error al eliminar registro");

        }
    }

    //Matricular a un alumno en asignaturas: se identificará al alumno y a las asignaturas
    public static void matricular(Connection con, Scanner sc) {
        System.out.println("Introduce un id de alumno");
        int id_alumno = sc.nextInt();

        System.out.print("Ingrese el ID de la asignatura: ");
        int id_asignatura = sc.nextInt();

        String query = "INSERT INTO alumnos_asignaturas (id_alumno,id_asignatura,cursada) VALUES (?,?,true)";
        PreparedStatement psMAtricular = null;

        try {

            psMAtricular = con.prepareStatement(query);

            psMAtricular.setInt(1, id_alumno);
            psMAtricular.setInt(2, id_asignatura);
            psMAtricular.executeUpdate();
            System.out.println("Alumno matriculado exitosamente");
            psMAtricular.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("El alumno ya esta matriculado en la asignatura o el alumno o asignatura no existen.");

        } catch (SQLException e) {
            System.out.println("Error al matricular alumno");

        }

    }

    //Ver las asignaturas asociadas a un alumno, mostrando las que ha cursado y las que no. Se mostrarán todos los datos del alumno y de las asignaturas.
    public static void mostrarAlumno_Asignaturas(Connection con, Scanner sc) {

        System.out.println("Introduce un id de alumno");
        int id_alumno = sc.nextInt();

        String query = "SELECT * FROM alumnos where id_alumno=?";
        PreparedStatement psMostrar = null;

        try {
            psMostrar = con.prepareStatement(query);
            psMostrar.setInt(1, id_alumno);

            ResultSet rs = psMostrar.executeQuery();
            if (rs.next()) {
                System.out.println("Datos del alumno");
                id_alumno = rs.getInt("id_alumno");
                String apellidos = rs.getString("apellidos");
                String nombre = rs.getString("nombre");
                int curso = rs.getInt("curso");
                int titulacion = rs.getInt("titulacion");

                System.out.println("id_alumno: " + id_alumno + " Apellidos: "
                        + apellidos + " Nombre: " + nombre + " Curso: " + curso
                        + " Titulacion: " + titulacion);

            } else {
                System.out.println("No se encontraron datos del alumno con id: " + id_alumno);

            }

            //obtenemos las asignaturas del alumno
            query = "SELECT asignaturas.*, alumnos_asignaturas.cursada from alumnos_asignaturas "
                    + " inner join asignaturas on alumnos_asignaturas.id_asignatura = asignaturas.id_asignatura "
                    + " where alumnos_asignaturas.id_alumno= ?";
            psMostrar = con.prepareStatement(query);
            psMostrar.setInt(1, id_alumno);
            rs = psMostrar.executeQuery();
            System.out.println("Asignaturas del alumno:");

            while (rs.next()) {
                int id_asignatura = rs.getInt("id_asignatura");
                String tipo = rs.getString("tipo");
                String nombre = rs.getString("nombre");
                String creditos = rs.getString("creditos");

                System.out.println("id_asignatura: " + id_asignatura + " Tipo: " + tipo + " Nombre: " + nombre + " Creditos: " + creditos);

            }
            psMostrar.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error al obtener las asignaturas del alumno");

        }

    }
}

