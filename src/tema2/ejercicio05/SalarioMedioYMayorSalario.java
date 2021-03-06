/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2.ejercicio05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Perig
 */
public class SalarioMedioYMayorSalario {
    String driver = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:ejemplo.db";
    String user = "";
    String pass = "";
    
    public static void main(String[] args) {
        SalarioMedioYMayorSalario la = new SalarioMedioYMayorSalario();
    }

    public SalarioMedioYMayorSalario() {
        String query = "select d.dnombre, avg(e.salario), e.apellido, max(e.salario) from departamentos d, empleados e where d.dept_no=e.dept_no group by e.dept_no";
        
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()) {
                System.out.println(result.getString(1)+" "+result.getFloat(2)+", "+result.getString(3)+" "+result.getFloat(4));
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
