/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema3.ejercicio1;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.hibernate.HibernateException;
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
                    altaDep();
                    break;
                case 2: // Baja
                    bajaDep();
                    break;
                case 3: // Modificación
                    modifDep();
                    break;
                case 4: // Alta
                    altaEmp();
                    break;
                case 5: // Baja
                    bajaEmp();
                    break;
                case 6: // Modificación
                    modifEmp();
                    break;
            }
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
        System.out.println("Introduzca nombre departamento");
        nombre = sc.nextLine().toUpperCase();

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
    
    public void altaEmp(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String nombre=""; String apellido=""; float salario=0;

        try{
            tx = session.beginTransaction();
            Empleado dep = new Empleado(nombre, apellido, salario);
            
            session.save(dep);
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
}
