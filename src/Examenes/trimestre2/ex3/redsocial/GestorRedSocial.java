/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examenes.trimestre2.ex3.redsocial;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import Examenes.trimestre2.ex3.redsocial.pojos.Lider;
import Examenes.trimestre2.ex3.redsocial.pojos.Seguidor;
import Examenes.trimestre2.ex3.redsocial.util.HibernateUtil;

/**
 *
 * @author Perig
 */
public class GestorRedSocial {
    Scanner sc;
    
    public static void main(String[] args) {
        GestorRedSocial g = new GestorRedSocial();
        
        int opcion;
        while ((opcion = g.menu()) != 8) {
            switch (opcion) {
                case 1: // Introducir nuevo lider
                    g.newLider();
                    break;
                case 2: // Introducir nuevo seguidor
                    g.newSeguidor();
                    break;
                case 3: // seguir a lider
                    g.follow(null, null, null);
                    break;
                case 4: // anotar un me gusta
                    g.meGusta();
                    break;
                case 5: // dejar de seguir
                    g.unfollow();
                    break;
                case 6: // consulta larga 1 (seguidores país fecha)
                    g.queryRangoFechas();
                    break;
                case 7: // consulta larga 2 (listado lideres seguidores)
                    g.querySeguidoresEspanya();
                    break;
            }
        }
        System.exit(0);
    }

    public GestorRedSocial() {
        
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
            System.out.println("\t4. Dar un 'Me gusta' a un líder");
            System.out.println("\t5. Seguidor deja de seguir a líder");
            System.out.println("\t6. Mostrar seguidores de un país de los líderes dados de alta en un determinado rango de fechas");
            System.out.println("\t7. Listar cada líder en orden alfabético con sus seguidores franceses");
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
    
    private void newLider() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        sc = new Scanner(System.in);
        // pedimos datos 
        System.out.println("Introduzca nombre líder: ");
        String nombre = sc.nextLine();
        
        // obtencion fecha
        Date fecha = new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String fechaAlta = df.format(fecha);
        
        //comenzamos transacción
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Lider lid = new Lider(nombre, fecha);
            session.save(lid);
            tx.commit();
            System.out.println("Líder guardado");
        } catch (HibernateException e) {
            if(tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        
    }
    
    private void newSeguidor() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        sc = new Scanner(System.in);
        // pedimos datos 
        System.out.println("Introduzca nick seguidor: ");
        String nick = sc.nextLine();
        System.out.println("Introduzca correo seguidor: ");
        String correo = sc.nextLine();
        System.out.println("Introduzca país seguidor: ");
        String pais = sc.nextLine();
        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Seguidor seg = new Seguidor(nick, correo, pais);
            follow(seg, session, tx);
            tx.commit();
            System.out.println("Seguidor guardado");
        } catch (HibernateException e) {
            if(tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private void follow(Seguidor seguidor, Session session, Transaction tx) throws HibernateException {
        if(session==null) session = HibernateUtil.getSessionFactory().openSession();
        Scanner sc = new Scanner(System.in);
        boolean transaccionEnCurso = tx!=null;
        
        // obtener seguidor si se ha pasado como null
        boolean encontrado = false;
        if(seguidor==null) {
            do {
                System.out.println("Seleccione seguidor que seguirá a un líder:");
                String nSeg = sc.nextLine();
                
                Query query = session.createQuery("from Seguidor where nick = :nick");
                query.setParameter("nick", nSeg);
                List<Seguidor> list = query.list();
                if(list.size()>0) {
                    seguidor = list.get(0);
                    encontrado = true;
                } else {
                    System.out.println("Error, seguidor no encontrado, introduzca de nuevo");
                    encontrado = false;
                }
            } while(!encontrado);
        }
        
        //obtener lider y añadir seguidor
        encontrado = false;
        try {
            if(!transaccionEnCurso) tx = session.beginTransaction();
            do {
                System.out.println("Seleccione líder a seguir:");
                String nLid = sc.nextLine();
                if(nLid.equals("")) throw new HibernateException("");
                Query query = session.createQuery("from Lider where nombre = :nombre");
                query.setParameter("nombre", nLid);
                List<Lider> list = query.list();
                if(list.size()>0) {
                    Lider lid = list.get(0);
                    Set<Seguidor> seguidores = lid.getSeguidores();
                    seguidores.add(seguidor);
                    lid.setSeguidores(seguidores);
                    session.update(lid);
                    System.out.println("Seguido a "+nLid);
                    encontrado = true;
                    if(!transaccionEnCurso) tx.commit();
                } else {
                    System.out.println("Error, líder no encontrado, introduzca de nuevo");
                    encontrado = false;
                }

            } while(!encontrado);
        } catch(HibernateException e) {
            if(!transaccionEnCurso) {
                tx.rollback();
            }
            throw e;
        } finally {
            if(!transaccionEnCurso) {
                session.close();
            }
        }
    }
    
    private void meGusta() {
        Scanner sc = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();
        //obtener id lider
        boolean encontrado = false;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            do {
                System.out.println("Seleccione líder a recibir el MG:");
                String nLid = sc.nextLine();
                Query query = session.createQuery("from Lider where nombre = :nombre");
                query.setParameter("nombre", nLid);
                List<Lider> list = query.list();
                if(list.size()>0) {
                    Lider lid = list.get(0);
                    int nMG = lid.getnMG();
                    nMG++;
                    lid.setnMG(nMG);
                    session.update(lid);
                    encontrado = true;
                    tx.commit();
                    System.out.println("Recibido un Me Gusta");
                } else {
                    System.out.println("Error, líder no encontrado, introduzca de nuevo");
                    encontrado = false;
                }

            } while(!encontrado);
            
        } catch (HibernateException e) {
            if(tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        
    }
    
    private void unfollow() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner sc = new Scanner(System.in);
        
        // obtener seguidor
        boolean encontrado = false;
        Seguidor seguidor = null;
        do {
            System.out.println("Seleccione seguidor que dejará de seguir a un líder:");
            String nSeg = sc.nextLine();
                
                Query query = session.createQuery("from Seguidor where nick = :nick");
                query.setParameter("nick", nSeg);
                List<Seguidor> list = query.list();
                if(list.size()>0) {
                    seguidor = list.get(0);
                    encontrado = true;
                } else {
                    System.out.println("Error, seguidor no encontrado, introduzca de nuevo");
                    encontrado = false;
                }
            
            
        } while(!encontrado);
        
        //obtener lider
        encontrado = false;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            do {
                System.out.println("Seleccione líder a dejar de seguir:");
                String nLid = sc.nextLine();
                Query query = session.createQuery("from Lider where nombre = :nombre");
                query.setParameter("nombre", nLid);
                List<Lider> list = query.list();
                if(list.size()>0) {
                    Lider lid = list.get(0);
                    Set<Seguidor> seguidores = lid.getSeguidores();
                    seguidores.remove(seguidor);
                    lid.setSeguidores(seguidores);

                    session.update(lid);
                    encontrado = true;
                    tx.commit();
                    System.out.println("Dejado de seguir a "+nLid);
                } else {
                    System.out.println("Error, líder no encontrado, introduzca de nuevo");
                    encontrado = false;
                }


            } while(!encontrado);
        } catch (HibernateException e) {
            if(tx!=null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        
        
        
    }
    
    private void queryRangoFechas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Scanner sc = new Scanner(System.in);
        
        
        // introducir pais
        String pais = "";
        System.out.println("Introduzca país de los seguidores a consultar:");
        pais = sc.nextLine();
        
        //introducción de fechas
        Date fechaIni = null, fechaFin = null;
        
        boolean fechaCorrecta= false;
        do {
            System.out.println("Introduzca fecha de inicio de rango:");
            String fecha = sc.nextLine();
            try {
                fechaIni = df.parse(fecha); //probamos a parsear para saber si el formato es correcto
            } catch (ParseException e) {
                System.out.println("Error, el formato de fecha es 'yyyy-MM-dd'");
                continue;
            }
            fechaCorrecta = true;
        } while(!fechaCorrecta);
        
        fechaCorrecta= false;
        do {
            System.out.println("Introduzca fecha de final de rango:");
            String fecha = sc.nextLine();
            try {
                fechaFin = df.parse(fecha);
            } catch (ParseException e) {
                System.out.println("Error, el formato de fecha es 'yyyy-MM-dd'");
                continue;
            }
            fechaCorrecta = true;
        } while(!fechaCorrecta);
        
        Query query = session.createQuery("from Lider l right join l.seguidores s where l.fechaAlta > :fechaIni and l.fechaAlta < :fechaFin and s.pais = :pais order by l.nombre asc");
        query.setDate("fechaIni", fechaIni);
        query.setDate("fechaFin", fechaFin);
        query.setString("pais",pais);
        List<Object[]> list = query.list();
        Lider liderActual = null;
        if(list.size()>0) {
            for(Object[] ob : list) {
                Lider lid = (Lider) ob[0];
                Seguidor seg = (Seguidor) ob[1];
                if(lid != liderActual) {
                    System.out.println("Seguidores de "+lid.getNombre()+" provenientes de "+pais+":");
                    liderActual = lid;
                }
                System.out.println("\t"+seg.getNick());
            }
        } else {
            System.out.println("Error, no se encontraron líderes en ese rango de fechas");
        }
    }
    
    private void querySeguidoresEspanya() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Query query = session.createQuery("from Lider l right join l.seguidores s where s.pais = :pais order by l.nombre asc");
        query.setString("pais","Francia");
        List<Object[]> list = query.list();
        Lider liderActual = null;
        if(list.size()>0) {
            for(Object[] ob : list) {
                Lider lid = (Lider) ob[0];
                Seguidor seg = (Seguidor) ob[1];
                if(lid != liderActual) {
                    System.out.println("Seguidores franceses de "+lid.getNombre()+":");
                    liderActual = lid;
                }
                System.out.println("\t"+seg.getNick());
            }
        } else {
            System.out.println("Error, no se encontraron líderes");
        }
    }
}
