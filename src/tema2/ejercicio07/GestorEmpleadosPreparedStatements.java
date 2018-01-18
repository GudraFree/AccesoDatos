/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2.ejercicio07;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class GestorEmpleadosPreparedStatements {
    
    private Scanner sc;
    private String driver = "org.sqlite.JDBC", url = "jdbc:sqlite:ejemplo.db", user = "", password = "";
    Connection connection;
    
    public static void main(String[] args) {
        GestorEmpleadosPreparedStatements main = new GestorEmpleadosPreparedStatements();
        
        System.out.println("Programa terminado");
    }

    public GestorEmpleadosPreparedStatements() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("He petao cargando el driver");
            return;
        }
        try {
            connection = DriverManager.getConnection(url,user,password);
            int opcion;
            while ((opcion = menu()) != 13) {
                switch (opcion) {
                    case 1: // Alta
                        altaDep();
                        break;
                    case 2: // Baja
                        bajaDep();
                        break;
                    case 3: // Modificación
                        modifDep();
                        break;
                    case 4: // Consulta
                        consultaDep1();
                        break;
                    case 5: // Consulta
                        consultaDep2();
                        break;
                    case 6: // Consulta
                        consultaDep3();
                        break;
                    case 7: // Alta
                        altaEmp();
                        break;
                    case 8: // Baja
                        bajaEmp();
                        break;
                    case 9: // Modificación
                        modifEmp();
                        break;
                    case 10: // Consulta
                        consultaEmp1();
                        break;
                    case 11: // Consulta
                        consultaEmp2();
                        break;
                    case 12: // Consulta
                        consultaEmp3();
                        break;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("He petao por el SQL");
            e.printStackTrace();
        }
    }
    
    private int menu() {
        sc = new Scanner(System.in);
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nGestor de empleados con BD. Por favor introduzca una opción");
            System.out.println("Departamentos: ");
            System.out.println("\t1. Alta");
            System.out.println("\t2. Baja");
            System.out.println("\t3. Actualización");
            System.out.println("\tConsulta");
            System.out.println("\t\t4. Mostrar todos los departamentos");
            System.out.println("\t\t5. Mostrar todos los departamentos con un determinado nombre");
            System.out.println("\t\t6. Mostrar todos los departamentos de una determinada localización");
            System.out.println("Empleados: ");
            System.out.println("\t7. Alta");
            System.out.println("\t8. Baja");
            System.out.println("\t9. Actualización");
            System.out.println("\tConsulta");
            System.out.println("\t\t10. Mostrar todos los empleados");
            System.out.println("\t\t11. Mostrar todos los empleados de un departamento y una localización");
            System.out.println("\t\t12. Mostrar todos los empleados de una determinada localización");
            System.out.println("13. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 13;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        return opcion;
    }
    
    private void altaDep() throws SQLException {
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre departamento");
        String dnombre = sc.nextLine().toUpperCase();
        System.out.println("Introduzca localización departamento");
        String loc = sc.nextLine().toUpperCase();
        
        //comprobamos que no exista el departamento introducido para continuar con la operación
        PreparedStatement compruebaPS = connection.prepareStatement("select * from departamentos where dnombre=? and loc=?");
        compruebaPS.setString(1, dnombre);
        compruebaPS.setString(2, loc);
        ResultSet compruebaDep = compruebaPS.executeQuery();
        if (compruebaDep.next()) {
            System.out.println("Error, el departamento "+dnombre+" ya existe en "+loc);
            return;
        }
        compruebaDep.close();
        
        //obtenemos el último ID de departamento
        ResultSet resId = connection.createStatement().executeQuery("select dept_no from departamentos order by dept_no desc");
        int lastId = 0;
        if (resId.next()) lastId = resId.getInt(1);
        resId.close();
        
        // creamos el statement e introducimos los datos
        PreparedStatement ps = connection.prepareStatement("insert into departamentos values (?, ?, ?)");
        
        ps.setInt(1, (lastId+10));
        ps.setString(2, dnombre);
        ps.setString(3, loc);
        
        //ejecutamos el insert
        ps.execute();
        ps.close();
        
        System.out.println("Alta realizada con éxito");
    }
    
    private void bajaDep() throws SQLException {
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre departamento");
        String dnombre = sc.nextLine().toUpperCase();
        System.out.println("Introduzca localización departamento");
        String loc = sc.nextLine().toUpperCase();
        
        //comprobamos que exista el departamento introducido para continuar con la operación
        PreparedStatement compruebaPS = connection.prepareStatement("select * from departamentos where dnombre=? and loc=?");
        compruebaPS.setString(1, dnombre);
        compruebaPS.setString(2, loc);
        ResultSet compruebaDep = compruebaPS.executeQuery();
        if (!compruebaDep.next()) {
            System.out.println("Error, el departamento "+dnombre+" no existe en "+loc);
            return;
        }
        compruebaDep.close();
        
        // creamos el statement e introducimos los datos
        PreparedStatement ps = connection.prepareStatement("delete from departamentos where dnombre=? and loc=?");
        
        ps.setString(1, dnombre);
        ps.setString(2, loc);
        
        //ejecutamos el delete
        ps.execute();
        ps.close();
        
        System.out.println("Baja realizada con éxito");
    }
    
    private void modifDep() throws SQLException {
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre departamento");
        String dnombre = sc.nextLine().toUpperCase();
        System.out.println("Introduzca localización departamento");
        String loc = sc.nextLine().toUpperCase();
        
        //comprobamos que exista el departamento introducido para continuar con la operación
        PreparedStatement compruebaPS = connection.prepareStatement("select * from departamentos where dnombre=? and loc=?");
        compruebaPS.setString(1, dnombre);
        compruebaPS.setString(2, loc);
        ResultSet compruebaDep = compruebaPS.executeQuery();
        if (!compruebaDep.next()) {
            System.out.println("Error, el departamento "+dnombre+" no existe en "+loc);
            return;
        }
        compruebaDep.close();
        
        // pedimos nuevos datos
        System.out.println("Introduzca nuevo nombre departamento");
        String dnombreN = sc.nextLine().toUpperCase();
        System.out.println("Introduzca nueva localización departamento");
        String locN = sc.nextLine().toUpperCase();
        
        // creamos el statement e introducimos los datos
        PreparedStatement ps = connection.prepareStatement("update departamentos set dnombre=?, loc=? where dnombre=? and loc=?");
        
        ps.setString(1, dnombreN);
        ps.setString(2, locN);
        ps.setString(3, dnombre);
        ps.setString(4, loc);
        
        //ejecutamos el update
        ps.execute();
        ps.close();
    }
    
    private void consultaDep1() throws SQLException {
        String query = "select dnombre, loc from departamentos";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while(rs.next()) {
            System.out.println("Departamento "+rs.getString(1)+", "+rs.getString(2));
        }
        rs.close();
    }
    
    private void consultaDep2() throws SQLException {
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre departamento:");
        String dnombre = sc.nextLine().toUpperCase();
        String query = "select dnombre, loc from departamentos where dnombre='"+dnombre+"'";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while(rs.next()) {
            System.out.println("Departamento "+rs.getString(1)+", "+rs.getString(2));
        }
        rs.close();
        
    }
    
    private void consultaDep3() throws SQLException {
        sc = new Scanner(System.in);
        System.out.println("Introduzca localización departamento:");
        String loc = sc.nextLine().toUpperCase();
        String query = "select dnombre, loc from departamentos where loc='"+loc+"'";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while(rs.next()) {
            System.out.println("Departamento "+rs.getString(1)+", "+rs.getString(2));
        }
        rs.close();
        
    }
    
    private void altaEmp() throws SQLException {
        
    }
    
    private void bajaEmp() throws SQLException {
        
    }
    
    private void modifEmp() throws SQLException {
        
    }
    
    private void consultaEmp1() throws SQLException {
        
    }
    private void consultaEmp2() throws SQLException {
        
    }
    private void consultaEmp3() throws SQLException {
        
    }
    
    
}
