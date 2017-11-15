/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema2.ejercicio03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Perig
 */
public class ListaAlfabetica {
    String driver = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:ejemplo.db";
    String user = "";
    String pass = "";
    
    public static void main(String[] args) {
        ListaAlfabetica la = new ListaAlfabetica();
    }

    public ListaAlfabetica() {
        String query = "select d.dnombre, d.loc, e.apellido, e.oficio from empleados e, departamentos d where e.dept_no==d.dept_no order by d.dnombre,e.apellido";
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            
            String currentDep = "";
            while (result.next()) {
                String dnombre = result.getString(1);
                if (!dnombre.equals(currentDep)) {
                    System.out.println(dnombre+", "+result.getString(2));
                    currentDep = dnombre;
                }
                System.out.println("\t"+result.getString(3)+", "+result.getString(4));
            }
            
            result.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error, clase no encontrada");
        } catch (SQLException e) {
            System.out.println("Error de SQL");
        }
    }
    
    
}
