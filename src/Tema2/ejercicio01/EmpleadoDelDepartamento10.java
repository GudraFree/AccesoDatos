/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema2.ejercicio01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Perig
 */
public class EmpleadoDelDepartamento10 {
    String driver = "org.sqlite.JDBC";
    String prefix = "jdbc:sqlite:";
    String host = "";
    String urlFolder = "";
    String dbName = "ejemplo.db";
    
    String url = prefix+host+urlFolder+dbName;
    String user = "";
    String pass = "";
    
    public static void main(String[] args) {
        EmpleadoDelDepartamento10 e = new EmpleadoDelDepartamento10();
    }

    public EmpleadoDelDepartamento10() {
        String query = "select apellido, oficio, salario from empleados where dept_no=10";
        
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()) {
                System.out.println(result.getString(1)+" "+result.getString(2)+" "+result.getFloat(3));
            }
            
            result.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error, clase no encontrada");
        } catch (SQLException e) {
            System.out.println("Error de SQL");
            e.printStackTrace();
        }
        
    }
    
    
}
