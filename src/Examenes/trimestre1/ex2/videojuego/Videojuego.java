/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examenes.trimestre1.ex2.videojuego;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class Videojuego {

    Connection connection;
    Scanner sc;
    String user="root", pass="", url="jdbc:mysql://localhost/videojuego", driver="com.mysql.jdbc.Driver";
    
    public static void main(String[] args) {
        Videojuego v = new Videojuego();
    }

    public Videojuego() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("He petao cargando el driver");
            return;
        }
        try {
            connection = DriverManager.getConnection(url,user,pass);
            int opcion;
            while ((opcion = menu()) != 5) {
                switch (opcion) {
                    case 1: // Introducir nueva característica
                        nuevaCaracteristica();
                        break;
                    case 2: // Introducir nuevo personaje
                        nuevoPersonaje();
                        break;
                    case 3: // Top 3 personajes más poderosos de una categoría introducida
                        top3();
                        break;
                    case 4: // Mostrar todos los personajes de cada categoría
                        mostrarTodo();
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
            System.out.println("\nGestor de videojuegos con BD. Por favor introduzca una opción");
            System.out.println("\t1. Introducir nueva característica");
            System.out.println("\t2. Introducir nuevo personaje");
            System.out.println("\t3. Obtener los 3 personajes más poderosos de una características");
            System.out.println("\t4. Mostrar todos los personajes de cada categoría");
            System.out.println("\t5. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 5;
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
    
    private void nuevaCaracteristica() throws SQLException {
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre característica");
        String cnombre = sc.nextLine();
        System.out.println("Introduzca descripción característica");
        String cdesc = sc.nextLine();
        String[] categorias = {"normal","guerrero","superguerrero","malvado"};
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nIntroduzca la categoría de la característica");
            System.out.println("\t1. normal");
            System.out.println("\t2. guerrero");
            System.out.println("\t3. superguerrero");
            System.out.println("\t4. malvado");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 4;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcionInvalida);
        String categoria = categorias[opcion-1];
        sc.nextLine(); //limpiamos scanner
        
        
        //comprobamos que no exista la característica introducida para continuar con la operación
        PreparedStatement compruebaPS = connection.prepareStatement("select * from caracteristica where nombre=?");
        compruebaPS.setString(1, cnombre);
        ResultSet compruebaCar = compruebaPS.executeQuery();
        if (compruebaCar.next()) { // comprueba que hay al menos una tupla con ese nombre
            System.out.println("Error, la característica "+cnombre+" ya existe");
            return;
        }
        compruebaCar.close();
        
        // creamos el statement e introducimos los datos
        PreparedStatement ps = connection.prepareStatement("insert into caracteristica (nombre, descripcion, categoria) values (?, ?, ?)");
        
        ps.setString(1, cnombre);
        ps.setString(2, cdesc);
        ps.setString(3, categoria);
        
        //ejecutamos el insert
        ps.execute();
        ps.close();
    }
    
    private void nuevoPersonaje() throws SQLException {
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre personaje");
        String pnombre = sc.nextLine();
        System.out.println("Introduzca descripción personaje");
        String pdesc = sc.nextLine();
        
        float vida = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nIntroduzca porcentaje de vida (0-100)");
            try {
                vida = sc.nextFloat();
                opcionInvalida = vida < 0 || vida > 100;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número real");
                sc.nextLine();
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        
        int puntos = 0; //valor 0 por defecto
        opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nIntroduzca puntos del personaje");
            try {
                puntos = sc.nextInt();
                opcionInvalida = puntos < 0;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcionInvalida);
        sc.nextLine(); //limpiamos scanner
        
        // obtenemos las características para mostrarlas por pantalla
        String caractsAMostrar = "Introduzca una característica a introducir para el personaje (introduzca 0 para dejar de asignar categorías)";
        ArrayList<Integer> caracteristicas = new ArrayList();
        ResultSet caracts = connection.createStatement().executeQuery("select * from caracteristica");
        int opcionMax = 0;
        while(caracts.next()) {
            int idCar = caracts.getInt("id");
            String nombreCar = caracts.getString("nombre");
            caractsAMostrar+= "\n"+idCar+". "+nombreCar;
            ++opcionMax;
        }
        
        int opcion = 0;
        opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println(caractsAMostrar);
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion<0 || opcion > opcionMax;
                if (opcionInvalida) {
                    System.out.println("Error, opción introducida no válida\n");
                    continue;
                } else {
                    if(opcion!=0)caracteristicas.add(opcion);
                }
            } catch (InputMismatchException e) {
                System.out.println("Error, debe introducir un número entero");
                sc.nextLine();
            }
        } while (opcion!=0);
        
        
        //comprobamos que no exista el personaje introducido para continuar con la operación
        PreparedStatement compruebaPS = connection.prepareStatement("select * from personaje where nombre=?");
        compruebaPS.setString(1, pnombre);
        ResultSet compruebaPer = compruebaPS.executeQuery();
        if (compruebaPer.next()) { // comprueba que hay al menos una tupla con ese nombre
            System.out.println("Error, el personaje "+pnombre+" ya existe");
            return;
        }
        compruebaPer.close();
        
        // creamos el statement e introducimos los datos
        PreparedStatement psPer = connection.prepareStatement("insert into personaje (nombre, descripcion, vida, puntos) values (?, ?, ?, ?)");
        
        psPer.setString(1, pnombre);
        psPer.setString(2, pdesc);
        psPer.setFloat(3, vida);
        psPer.setInt(4, puntos);
        
        //ejecutamos el insert del personaje
        psPer.execute();
        psPer.close();
        
        //obtenemos el id del personaje que acabamos de insertar
        PreparedStatement compruebaPerId = connection.prepareStatement("select id from personaje where nombre=?");
        compruebaPerId.setString(1,pnombre);
        ResultSet rsId = compruebaPerId.executeQuery();
        rsId.next();
        int id = rsId.getInt(1);
        compruebaPerId.close();
        rsId.close();
        
        // introducimos por cada categoría elegida en la tabla correspondiente
        PreparedStatement introduceCaracts = connection.prepareStatement("insert into per_tiene_car (idPer, idCar) values (?, ?)");
        for (int car : caracteristicas) {
            introduceCaracts.setInt(1, id);
            introduceCaracts.setInt(2, car);
            introduceCaracts.execute();
        }
        introduceCaracts.close();
        
        
    }
    
    private void top3() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("select c.nombre, p.nombre avg(p.puntos) from personaje p, caracteristica c, per_tiene_car t where p.id==t.idPer and c.id==t.idCar order by c.nombre, p.vida limit 3");
        String currentDep = "";
        while (rs.next()) {
            String cnombre = rs.getString(1);
            if (!cnombre.equals(currentDep)) {
                System.out.println(cnombre+": media "+rs.getFloat(3));
                currentDep = cnombre;
            }
            System.out.println("\t"+rs.getString(2));
        }
    }
    
    private void mostrarTodo() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("select c.nombre, p.nombre from personaje p, caracteristica c, per_tiene_car t where p.id==t.idPer and c.id==t.idCar order by c.nombre");
        
        String currentDep = "";
        while (rs.next()) {
            String cnombre = rs.getString(1);
            if (!cnombre.equals(currentDep)) {
                System.out.println(cnombre);
                currentDep = cnombre;
            }
            System.out.println("\t"+rs.getString(2));
        }
    }
}
