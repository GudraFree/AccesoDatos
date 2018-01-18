/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2.ejercicio02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Perig
 */
public class EmpleadoMayorSalario {
    String driver = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:ejemplo.db";
    String user = "";
    String password = "";
    
    public static void main(String[] args) {
        EmpleadoMayorSalario em = new EmpleadoMayorSalario();
    }

    public EmpleadoMayorSalario() {
        String query = "select apellido, salario from empleados order by salario desc";
        
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            result.next();
            System.out.println(result.getString(1)+" "+result.getFloat(2));
        } catch (ClassNotFoundException e) {
            System.out.println("Error, clase no encontrada");
            
        } catch (SQLException e) {
            System.out.println("Error de SQL");
        }
    }
    
    
}
