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
        String query = "select d.dept_no, d.dnombre, count(e.apellido) from empleados e, departamentos d where e.dept_no == d.dept_no group by e.dept_no";
        try {
            Class.forName(driver);
            
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement st = connection.createStatement();
            
            ResultSet res = st.executeQuery(query);
            
            while(res.next()) {
                System.out.println(res.getString(2)+":");
                String dept_no = res.getString(1);
                int count = Integer.parseInt(res.getString(3));
                boolean q1 = false, q2 = false, q3 = false;
                String subquery = "select apellido, salario from empleados where dept_no="+dept_no+" order by salario asc";
                int contador = 0;
                Statement subst = connection.createStatement();
                ResultSet subres = subst.executeQuery(subquery);
                while(subres.next()) {
                    if(contador>=(count/4.0f) && !q1) {
                        System.out.println("\tQ1: "+subres.getString(1)+", "+subres.getString(2));
                        q1 = true;
                    }
                    if(contador>=(count/2.0f) && !q2) {
                        System.out.println("\tQ2: "+subres.getString(1)+", "+subres.getString(2));
                        q2 = true;
                    }
                    if(contador>=(3*count/4.0f) && !q3) {
                        System.out.println("\tQ3: "+subres.getString(1)+", "+subres.getString(2));
                        q3 = true;
                    }
                    ++contador;
                }
            }
        } catch (Exception e) {
            System.out.println("He petao");
            e.printStackTrace();
        }
    }
    
    
}
