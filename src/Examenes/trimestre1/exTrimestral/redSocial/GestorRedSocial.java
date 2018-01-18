/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examenes.trimestre1.exTrimestral.redSocial;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Perig
 */
public class GestorRedSocial {
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost/redsocial";
    String user = "root", pass="";
    
    Connection connection;
    Scanner sc;
    
    public static void main(String[] args) {
        GestorRedSocial g = new GestorRedSocial();
        
        int opcion;
        try {
            while ((opcion = g.menu()) != 8) {
                switch (opcion) {
                    case 1: // Introducir nuevo lider
                        g.newLider();
                        break;
                    case 2: // Introducir nuevo seguidor
                        g.newSeguidor();
                        break;
                    case 3: // seguir a lider
                        g.follow();
                        break;
                    case 4: // anotar un me gusta
                        g.meGusta();
                        break;
                    case 5: // dejar de seguir
                        g.unfollow();
                        break;
                    case 6: // consulta larga 1 (seguidores país fecha)
                        g.query1();
                        break;
                    case 7: // consulta larga 2 (listado lideres seguidores)
                        g.query2();
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL");
            e.printStackTrace();
        }
    }

    public GestorRedSocial() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Excepción cargando el driver. Abortando ejecución");
            return;
        } catch(SQLException e) {
            System.out.println("Excepción SQL al intentar conectar con la BD. Abortando ejecución");
            return;
        }
        
    }
    
    private int menu() {
        sc = new Scanner(System.in);
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nGestor de red social con BD. Por favor introduzca una opción");
            System.out.println("\t1. Introducir nuevo líder");
            System.out.println("\t2. Introducir nuevo seguidor");
            System.out.println("\t3. Seguidor sigue a líder");
            System.out.println("\t4. Seguidor da un 'Me gusta' a un líder");
            System.out.println("\t5. Seguidor deja de seguir a líder");
            System.out.println("\t6. Mostrar seguidores de un país en un determinado rango de fechas");
            System.out.println("\t7. Listar cada líder con su seguidor con más 'Me gusta' y con el con menos");
            System.out.println("\t8. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 8;
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
    
    private void newLider() throws SQLException {
        sc = new Scanner(System.in);
        // pedimos datos 
        System.out.println("Introduzca nombre líder: ");
        String nombre = sc.nextLine();
        
        // obtencion fecha
        Date fecha = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fechaAlta = df.format(fecha);
        
        // creamos PS y añadimos los datos
        PreparedStatement psLid = connection.prepareStatement("insert into lider (nombre, fechaAlta) values (?,?)");
        psLid.setString(1, nombre);
        psLid.setString(2, fechaAlta);
        
        //ejecutamos el insert del nuevo Lider
        psLid.execute();
        psLid.close();
    }
    
    private void newSeguidor() throws SQLException {
        sc = new Scanner(System.in);
        // pedimos datos 
        System.out.println("Introduzca nick seguidor: ");
        String nick = sc.nextLine();
        System.out.println("Introduzca correo seguidor: ");
        String correo = sc.nextLine();
        System.out.println("Introduzca país seguidor: ");
        String pais = sc.nextLine();
        
        // creamos PS y añadimos los datos
        PreparedStatement psSeg = connection.prepareStatement("insert into seguidor (nick, correo, pais) values (?,?,?)");
        psSeg.setString(1, nick);
        psSeg.setString(2, correo);
        psSeg.setString(3, pais);
        
        //ejecutamos el insert del nuevo Lider
        psSeg.execute();
        psSeg.close();
        
    }
    
    private void follow() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int idSeg=-1, idLid=-1;
        
        // obtener id seguidor
        boolean encontrado = false;
        do {
            System.out.println("Seleccione seguidor que seguirá a un líder:");
            String nSeg = sc.nextLine();
            PreparedStatement psSeg = connection.prepareStatement("select id from seguidor where nick=?");
            psSeg.setString(1, nSeg);
            ResultSet rsIdSeg = psSeg.executeQuery();
            if(rsIdSeg.next()) {
                idSeg = rsIdSeg.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, seguidor no encontrado");
            }
            rsIdSeg.close();
            psSeg.close();
        } while(!encontrado);
        
        //obtener id lider
        encontrado = false;
        do {
            System.out.println("Seleccione líder a seguir:");
            String nLid = sc.nextLine();
            PreparedStatement psLid = connection.prepareStatement("select id from lider where nombre=?");
            psLid.setString(1, nLid);
            ResultSet rsIdLid = psLid.executeQuery();
            if(rsIdLid.next()) {
                idLid = rsIdLid.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, líder no encontrado");
            }
            rsIdLid.close();
            psLid.close();
        } while(!encontrado);
        
        // obtencion fecha
        Date fecha = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fechaAlta = df.format(fecha);
        
        // creamos PS y añadimos los datos
        PreparedStatement psFollow = connection.prepareStatement("insert into lid_tiene_seg (idLid, idSeg, fechaAlta, nMG) values (?,?,?,?)");
        psFollow.setInt(1, idLid);
        psFollow.setInt(2, idSeg);
        psFollow.setString(3, fechaAlta);
        psFollow.setInt(4, 0); // número de MG a 0 por defecto
        
        //ejecutamos el insert del nuevo follow
        psFollow.execute();
        psFollow.close();
        
    }
    
    private void meGusta() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int idSeg=-1, idLid=-1;
        
        // obtener id seguidor
        boolean encontrado = false;
        do {
            System.out.println("Seleccione seguidor que dará un MG:");
            String nSeg = sc.nextLine();
            PreparedStatement psSeg = connection.prepareStatement("select id from seguidor where nick=?");
            psSeg.setString(1, nSeg);
            ResultSet rsIdSeg = psSeg.executeQuery();
            if(rsIdSeg.next()) {
                idSeg = rsIdSeg.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, seguidor no encontrado");
            }
            rsIdSeg.close();
            psSeg.close();
        } while(!encontrado);
        
        //obtener id lider
        encontrado = false;
        do {
            System.out.println("Seleccione líder a recibir el MG:");
            String nLid = sc.nextLine();
            PreparedStatement psLid = connection.prepareStatement("select l.id from lider l, lid_tiene_seg t where l.nombre=? and l.id=t.idLid and t.idSeg="+idSeg);
            psLid.setString(1, nLid);
            ResultSet rsIdLid = psLid.executeQuery();
            if(rsIdLid.next()) {
                idLid = rsIdLid.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, líder no seguido por este seguidor. Sígalo primero");
                return;
            }
            rsIdLid.close();
            psLid.close();
        } while(!encontrado);
        
        //obtenemos número de MG
        PreparedStatement psnMG = connection.prepareStatement("select nMG from lid_tiene_seg where idLid=? and idSeg=?");
        psnMG.setInt(1, idLid);
        psnMG.setInt(2, idSeg);
        ResultSet rsIdnMG = psnMG.executeQuery();
        rsIdnMG.next();
        int nMG = rsIdnMG.getInt(1);
        rsIdnMG.close();
        psnMG.close();
        
        // preparamos el statement del update
        PreparedStatement psMG = connection.prepareStatement("update lid_tiene_seg set nMG=? where idLid=? and idSeg=?");
        psMG.setInt(1, (nMG+1));
        psMG.setInt(2, idLid);
        psMG.setInt(3, idSeg);
        
        // lo ejecutamos
        psMG.execute();
        psMG.close();
        
    }
    
    private void unfollow() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int idSeg=-1, idLid=-1;
        
        // obtener id seguidor
        boolean encontrado = false;
        do {
            System.out.println("Seleccione seguidor que dará un MG:");
            String nSeg = sc.nextLine();
            PreparedStatement psSeg = connection.prepareStatement("select id from seguidor where nick=?");
            psSeg.setString(1, nSeg);
            ResultSet rsIdSeg = psSeg.executeQuery();
            if(rsIdSeg.next()) {
                idSeg = rsIdSeg.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, seguidor no encontrado");
            }
            rsIdSeg.close();
            psSeg.close();
        } while(!encontrado);
        
        //obtener id lider
        encontrado = false;
        do {
            System.out.println("Seleccione líder a recibir el MG:");
            String nLid = sc.nextLine();
            PreparedStatement psLid = connection.prepareStatement("select l.id from lider l, lid_tiene_seg t where l.nombre=? and l.id=t.idLid and t.idSeg="+idSeg);
            psLid.setString(1, nLid);
            ResultSet rsIdLid = psLid.executeQuery();
            if(rsIdLid.next()) {
                idLid = rsIdLid.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, líder no seguido por este seguidor. Sígalo primero");
                rsIdLid.close();
                psLid.close();
                return;
            }
            rsIdLid.close();
            psLid.close();
        } while(!encontrado);
        
        // preparamos el statement del delete
        PreparedStatement psUnfollow = connection.prepareStatement("delete from lid_tiene_seg where idLid=? and idSeg=?");
        psUnfollow.setInt(1, idLid);
        psUnfollow.setInt(2, idSeg);
        
        // lo ejecutamos
        psUnfollow.execute();
        psUnfollow.close();
        
        
    }
    
    private void query1() throws SQLException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Scanner sc = new Scanner(System.in);
        int idLid=-1;
        String nLid = "";
        
        //obtener id lider
        boolean encontrado = false;
        do {
            System.out.println("Seleccione líder cuyos seguidores quiere consultar:");
            nLid = sc.nextLine();
            PreparedStatement psLid = connection.prepareStatement("select id from lider where nombre=?");
            psLid.setString(1, nLid);
            ResultSet rsIdLid = psLid.executeQuery();
            if(rsIdLid.next()) {
                idLid = rsIdLid.getInt(1);
                encontrado = true;
            } else {
                System.out.println("Error, líder no encontrado");
                rsIdLid.close();
                psLid.close();
                return;
            }
            rsIdLid.close();
            psLid.close();
        } while(!encontrado);
        
        // introducir pais
        String pais = "";
        System.out.println("Introduzca país de los seguidores a consultar:");
        pais = sc.nextLine();
        
        //introducción de fechas
        String fechaIni = "", fechaFin = "";
        
        boolean fechaCorrecta= false;
        do {
            System.out.println("Introduzca fecha de inicio de rango:");
            fechaIni = sc.nextLine();
            try {
                Date d = df.parse(fechaIni); //probamos a parsear para saber si el formato es correcto
            } catch (ParseException e) {
                System.out.println("Error, el formato de fecha es 'yyyy-MM-dd'");
                continue;
            }
            fechaCorrecta = true;
        } while(!fechaCorrecta);
        
        fechaCorrecta= false;
        do {
            System.out.println("Introduzca fecha de final de rango:");
            fechaFin = sc.nextLine();
            try {
                Date d = df.parse(fechaFin);
            } catch (ParseException e) {
                System.out.println("Error, el formato de fecha es 'yyyy-MM-dd'");
                continue;
            }
            fechaCorrecta = true;
        } while(!fechaCorrecta);
        
        //Creación del statement e introducción de datos
        PreparedStatement psQ1 = connection.prepareStatement("select s.nick from seguidor s, lid_tiene_seg t where s.id=t.idSeg and t.fechaAlta>=? and t.fechaAlta<=? and t.idLid=? and s.pais=?");
        // fechaIni, fechaFin, idLid, pais
        psQ1.setString(1, fechaIni);
        psQ1.setString(2, fechaFin);
        psQ1.setInt(3, idLid);
        psQ1.setString(4, pais);
        
        // ejecutamos y mostramos por pantalla
        ResultSet rsQ1 = psQ1.executeQuery();
        System.out.println("Siguieron a "+nLid+" desde "+pais+" entre el "+fechaIni+" y el "+fechaFin);
        while(rsQ1.next()) {
            System.out.println("\t"+rsQ1.getString(1));
        }
        
        rsQ1.close();
        psQ1.close();
    }
    
    private void query2() throws SQLException {
        // comprobamos si el sistema soporta consultas correlacionadas
        DatabaseMetaData dbmd = connection.getMetaData();
        boolean soporta = dbmd.supportsCorrelatedSubqueries();
//        boolean soporta = false;
        
        
        if(soporta) {
            String subqueryMax = "(select s2.nick from seguidor s2, lid_tiene_seg t2 where "
                    + "t2.idLid=l.id and s2.id=t2.idSeg order by t2.nMG desc limit 1)"; //subselect dependiente (l.id)
            String subqueryMin= "(select s2.nick from seguidor s2, lid_tiene_seg t2 where "
                    + "t2.idLid=l.id and s2.id=t2.idSeg order by t2.nMG asc limit 1)";
            String query = "select l.nombre, "+subqueryMax+", "+subqueryMin+" from lider l order by l.nombre asc";
            PreparedStatement psQ2 = connection.prepareStatement(query);
            ResultSet rsQ2 = psQ2.executeQuery(); //tal que devuelve nombreLider, nombreSegMax, nombreSegMin
            while(rsQ2.next()) {
                String nLid = rsQ2.getString(1);
                String nSegMax = rsQ2.getString(2);
                String nSegMin = rsQ2.getString(3);
                if(nSegMax == null) nSegMax = "\n\tNadie sigue a "+nLid+" :("; //comprobar si se ha devuelto algo
                else nSegMax = "\n\tSu mayor seguidor, "+nSegMax;
                if(nSegMin == null) nSegMin = "";
                else nSegMin = "\n\tSu menor seguidor, "+nSegMin;
                System.out.println(nLid+":"+nSegMax+nSegMin);
            }
            
            rsQ2.close();
            psQ2.close();
        } else {
            String query = "select l.nombre, l.id from lider l order by l.nombre";
            String subqueryMax = "(select s2.nick, t2.nMG from seguidor s2, lid_tiene_seg t2 where "
                    + "t2.idLid=? and s2.id=t2.idSeg order by t2.nMG desc limit 1)";
            String subqueryMin= "(select s2.nick, t2.nMG from seguidor s2, lid_tiene_seg t2 where "
                    + "t2.idLid=? and s2.id=t2.idSeg order by t2.nMG asc limit 1)";
            PreparedStatement psQ2 = connection.prepareStatement(query);
            PreparedStatement psQ2Max = connection.prepareStatement(subqueryMax);
            PreparedStatement psQ2Min = connection.prepareStatement(subqueryMin);
            ResultSet rsQ2 = psQ2.executeQuery();
            boolean alguienLeSigue = false;
            String nLid = "";
            while(rsQ2.next()) {
                nLid = rsQ2.getString(1);
                int idLid = rsQ2.getInt(2);
                psQ2Max.setInt(1, idLid);
                psQ2Min.setInt(1, idLid);
                System.out.println(nLid+":");
                ResultSet rsQ2Max = psQ2Max.executeQuery();
                if(rsQ2Max.next()) {
                    alguienLeSigue = true;
                    System.out.println("\tSu mayor seguidor, "+rsQ2Max.getString(1)+", con "+rsQ2Max.getInt(2)+" MG");
                }
                rsQ2Max.close();
                ResultSet rsQ2Min = psQ2Min.executeQuery();
                if(rsQ2Min.next())
                    System.out.println("\tSu menor seguidor, "+rsQ2Min.getString(1)+", con "+rsQ2Min.getInt(2)+" MG");
                rsQ2Min.close();
            }
            if(!alguienLeSigue) 
                System.out.println("\n\tNadie sigue a "+nLid+" :(");
        }
        
        
    }
    
    
}
