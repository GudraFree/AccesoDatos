/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema3.ejercicio1;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import tema3.ejercicio1.util.HibernateUtil;

/**
 *
 * @author Perig
 */
public class ManageEmployee {
    Scanner sc;
    public static void main(String[] args) {
        ManageEmployee me = new ManageEmployee();
        int opcion;
        while ((opcion = me.menu()) != 7) {
            switch (opcion) {
                case 1: // Alta
                    me.altaDep();
                    break;
                case 2: // Baja
                    me.bajaDep();
                    break;
                case 3: // Modificación
                    me.modifDep();
                    break;
                case 4: // Alta
                    me.altaEmp();
                    break;
                case 5: // Baja
//                    me.bajaEmp();
                    break;
                case 6: // Modificación
//                    me.modifEmp();
                    break;
            }
        }
        System.exit(0);
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
            System.out.println("Empleados: ");
            System.out.println("\t4. Alta");
            System.out.println("\t5. Baja");
            System.out.println("\t6. Actualización");
            System.out.println("7. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 7;
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
    
    public void altaDep(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int codigo=0; String nombre="";
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca código departamento");
        codigo = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduzca nombre departamento");
        nombre = sc.nextLine();

        try{
            tx = session.beginTransaction();
            Departamento dep = new Departamento(codigo, nombre);
            
            session.save(dep);
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void bajaDep(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int codigo=0;
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca código departamento");
        codigo = sc.nextInt();
        sc.nextLine();

        try{
            tx = session.beginTransaction();
            Departamento dep = (Departamento)session.get(Departamento.class, codigo);
            
            if(dep!=null) session.delete(dep);
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void modifDep(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int codigo=0; String nombre="";
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca código departamento");
        codigo = sc.nextInt();
        sc.nextLine();
        System.out.println("Introduzca nuevo nombre departamento");
        nombre = sc.nextLine();

        try{
            tx = session.beginTransaction();
            Departamento dep = (Departamento)session.get(Departamento.class, codigo);
            if(dep!=null) {
                dep.setDnombre(nombre);
                session.update(dep);
            }
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void altaEmp(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String nombre=""; String apellido=""; float salario=0; int idDep=0;
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre empleado");
        nombre = sc.nextLine();
        System.out.println("Introduzca apellido empleado");
        apellido = sc.nextLine();
        System.out.println("Introduzca salario empleado");
        salario = sc.nextFloat();
        sc.nextLine();
        System.out.println("Introduzca departamento empleado (código)");
        idDep = sc.nextInt();

        try{
            tx = session.beginTransaction();
            Empleado emp = new Empleado(nombre, apellido, salario);
            
            Departamento dep = (Departamento)session.get(Departamento.class, idDep);
            Set empleados = dep.getEmpleados();
            if(empleados==null) empleados = new HashSet();
            empleados.add(emp);
            dep.setEmpleados(empleados);
            
            session.save(emp);
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void bajaEmp(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String nombre="", apellido="";
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre empleado");
        nombre = sc.nextLine();
        System.out.println("Introduzca apellido empleado");
        apellido = sc.nextLine();

        try{
            tx = session.beginTransaction();
            
            Query query = session.createQuery("from Empleado where nombre = :nombre and apellido = :apellido ");
            query.setParameter("nombre", nombre);
            query.setParameter("code", apellido);
            List list = query.list();
            Empleado emp = (Empleado)list.get(0);
            
            if(emp!=null) {
                session.delete(emp);
            }
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void modifEmp(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String nombre="", apellido="";
        // pedimos datos
        sc = new Scanner(System.in);
        System.out.println("Introduzca nombre empleado");
        nombre = sc.nextLine();
        System.out.println("Introduzca apellido empleado");
        apellido = sc.nextLine();

        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("from Empleado where nombre = :nombre and apellido = :apellido ");
            query.setParameter("nombre", nombre);
            query.setParameter("code", apellido);
            List list = query.list();
            Empleado emp = (Empleado)list.get(0);
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
}
