/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tema2.ejercicio06;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Perig
 */
public class Cuartiles {
    public static void main(String[] args) {
        Cuartiles c = new Cuartiles();
    }
    
    String driver = "org.sqlite.JDBC";
    String url = "jdbc:sqlite:ejemplo.db";
    String user = "";
    String pass = "";

    public Cuartiles() {
        String query = "";
        try {
            Class.forName(driver);
            
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(query);
            
            
        } catch (Exception e) {}
    }
    
    
}
