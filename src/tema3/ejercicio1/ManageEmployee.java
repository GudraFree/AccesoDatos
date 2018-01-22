/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema3.ejercicio1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import tema3.ejercicio1.pojos.InformacionFinancieraDepartamento;
import tema3.ejercicio1.pojos.Direccion;
import tema3.ejercicio1.pojos.Empleado;
import tema3.ejercicio1.pojos.Departamento;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import tema3.ejercicio1.pojos.Proyecto;
import tema3.ejercicio1.util.HibernateUtil;

/**
 *
 * @author Perig
 */
public class ManageEmployee {
    final static int OPCIONES_MENU = 12;
    Scanner sc;
    public static void main(String[] args) {
        ManageEmployee me = new ManageEmployee();
        int opcion;
        while ((opcion = me.menu()) != OPCIONES_MENU) {
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
                    me.bajaEmp();
                    break;
                case 6: // Modificación
                    me.modifEmp();
                    break;
                case 7: //hacer jefe
                    me.hacerJefe();
                    break;
                case 8: // crear proyecto
                    me.crearProyecto();
                    break;
                case 9: // añadir empleados a proyecto
                    me.asignarEmpleados();
                    break;
                case 10: // añadir responsable de un proyecto
                    me.asignarResponsable();
                    break;
                case 11: //listado
                    me.listar();
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
            System.out.println("\t7. Hacer jefe de departamento a algún empleado");
            System.out.println("Proyectos: ");
            System.out.println("\t8. Crear proyecto");
            System.out.println("\t9. Asignar empleados a un proyecto");
            System.out.println("\t10. Asignar responsable de un proyecto");
            System.out.println("11. Listar todos los departamentos y sus empleados");
            System.out.println("12. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > OPCIONES_MENU;
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
            Integer dirId = introducirDireccion(session, tx);
            Direccion dir = (Direccion) session.get(Direccion.class, dirId);
            Departamento dep = new Departamento(codigo, nombre, dir);
            introducirInfoFinanciera(dep, session, tx);
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
        sc.nextLine();

        try{
            tx = session.beginTransaction();
            Integer dirId = introducirDireccion(session, tx);
            Direccion dir = (Direccion) session.get(Direccion.class, dirId);
            Empleado emp = new Empleado(nombre, apellido, salario, dir);
            
            Departamento dep = (Departamento)session.get(Departamento.class, idDep);
            Set<Empleado> empleados = dep.getEmpleados();
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
            query.setParameter("apellido", apellido);
            List<Empleado> list = query.list();
            Empleado emp = list.get(0);
            
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
        String nombre="", apellido=""; float salario = 0; int idDep = 0;
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
            query.setParameter("apellido", apellido);
            List<Empleado> list = query.list();
            Empleado emp = list.get(0);
            if(emp!= null) {
                System.out.println("Introduzca nombre empleado");
                nombre = sc.nextLine();
                System.out.println("Introduzca apellido empleado");
                apellido = sc.nextLine();
                System.out.println("Introduzca salario empleado");
                salario = sc.nextFloat();
                sc.nextLine();
                System.out.println("Introduzca departamento empleado (código)");
                idDep = sc.nextInt();
                sc.nextLine();
                
                emp.setNombre(nombre);
                emp.setApellido(apellido);
                emp.setSalario(salario);
                
                query = session.createQuery("from Departamento");
                List<Departamento> list2 = query.list();
                for (Departamento d : list2) {
                    Set<Empleado> empleados = d.getEmpleados(), nuevosEmpleados = new HashSet();
                    for(Empleado e : empleados) { 
                        if(e.getId() != emp.getId()) { // recorriendo todos los empleados de un departamento, solo añado al nuevo set los que no son 
                            nuevosEmpleados.add(e); //    el empleado al que estoy quitando de ese departamento
                        }
                    }
                    if(d.getId() == idDep) { // si el departamento es el nuevo al que quiero añadir el empleado, lo hago
                        nuevosEmpleados.add(emp);
                    }
                    d.setEmpleados(nuevosEmpleados); // así se actualiza la lista de empleados del departamento
                    session.update(d);
                }
                
                session.update(emp);
            }
            tx.commit();
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 

    }
    
    public void hacerJefe() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String nombre="", apellido=""; float salario = 0; int idDep = 0;
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
            query.setParameter("apellido", apellido);
            List<Empleado> list = query.list();
            Empleado emp = list.get(0);
            
            // obtengo el id del departamento
            query = session.createQuery("from Departamento");
            List<Departamento> list2 = query.list();
            for (Departamento d : list2) {
                Set<Empleado> empleados = d.getEmpleados();
                for(Empleado e : empleados) {
                    if(e.getId() == emp.getId()) {
                        idDep = d.getId();
                    }
                }
            }
                
            Departamento dep = (Departamento)session.get(Departamento.class, idDep);
            
            System.out.println("¿Quiere hacer a "+nombre+" "+apellido+" jefe del departamento de "+dep.getDnombre()+"? (s/n)");
            String respuesta = sc.nextLine();
            switch (respuesta) {
                case "s":
                    dep.setJefe(emp);
                    session.update(dep);
                    tx.commit();
                    System.out.println(nombre+" "+apellido+" es jefe de departamento");
                    break;
                case "n":
                    System.out.println("No se ha asignado jefe");
                    break;
                default:
                    System.out.println("Error, opción introducida no válida");
            }
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
                e.printStackTrace();
        }finally {
            session.close();
        } 
    }
    
    public void crearProyecto() {
        String nombre, descripcion;
        Date fechaInicio, fechaFin;
        int idDep;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        
        System.out.println("Introduzca el nombre del proyecto");
        nombre = sc.nextLine();
        System.out.println("Introduzca la descripción del proyecto");
        descripcion = sc.nextLine();
        System.out.println("Introduzca fecha inicio del proyecto (dd-mm-aaaa)");
        try {
            fechaInicio = df.parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Error de introducción de fecha");
            return;
        }
        System.out.println("Introduzca fecha fin del proyecto (dd-mm-aaaa)");
        try {
            fechaFin = df.parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Error de introducción de fecha");
            return;
        }
        System.out.println("Introduzca código departamento al que asignará el proyecto");
        idDep = sc.nextInt();
        sc.nextLine();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Departamento dep = (Departamento)session.get(Departamento.class, idDep);
            Proyecto proy = new Proyecto(nombre, descripcion, fechaInicio, fechaFin, dep);
            session.save(proy);
            tx.commit();
        } catch (HibernateException e) {
            if(tx!=null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void asignarEmpleados() {
        String nProyecto="", nEmp="", aEmp="";
        
        System.out.println("Introduzca nombre del proyecto");
        nProyecto = sc.nextLine();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Proyecto where nombre = :nombre");
            query.setParameter("nombre", nProyecto);
            List<Proyecto> list = query.list();
            if(list.size()>0) {
                Proyecto proy = list.get(0);
                Set<Empleado> empleados = proy.getEmpleados();
                if(empleados==null) empleados = new HashSet();
                Departamento dep = proy.getDepartamento();
                Set<Empleado> empsDep = dep.getEmpleados();
                if(empsDep!=null) {
                    do {
                        System.out.println("Introduzca nombre empleado (Enter para terminar)");
                        nEmp = sc.nextLine();
                        if (!nEmp.equals("")) {
                            System.out.println("Introduzca apellido empleado");
                            aEmp = sc.nextLine();
                            query = session.createQuery("from Empleado where nombre = :nombre and apellido = :apellido ");
                            query.setParameter("nombre", nEmp);
                            query.setParameter("apellido", aEmp);
                            List<Empleado> list3 = query.list();
                            if(list3.size()>0) {
                                Empleado emp = list3.get(0);
                                    if(empsDep.contains(emp)) {
                                        empleados.add(emp);
                                        proy.setEmpleados(empleados);
                                        session.save(proy);
                                    } else System.out.println("Error, el empleado introducido no pertenece al departamento asignado al proyecto");
                            } else System.out.println("Error, el empleado introducido no existe");
                        }
                    } while(!nEmp.equals(""));
                } else System.out.println("Error, el departamento asginado al proyecto no tiene empleados");
            } else System.out.println("Error, el proyecto introducido no existe");
            tx.commit(); 
            
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void asignarResponsable() {
        String nProyecto="", nEmp="", aEmp="";
        
        System.out.println("Introduzca nombre del proyecto");
        nProyecto = sc.nextLine();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("from Proyecto where nombre = :nombre");
            query.setParameter("nombre", nProyecto);
            List<Proyecto> list = query.list();
            if(list.size()>0) {
                Proyecto proy = list.get(0);
                Set<Empleado> empleados = proy.getEmpleados();
                if(empleados.size()>0) {
                    System.out.println("Introduzca nombre empleado");
                    nEmp = sc.nextLine();
                    System.out.println("Introduzca apellido empleado");
                    aEmp = sc.nextLine();
                    query = session.createQuery("from Empleado where nombre = :nombre and apellido = :apellido ");
                    query.setParameter("nombre", nEmp);
                    query.setParameter("apellido", aEmp);
                    List<Empleado> list2 = query.list();
                    if(list2.size()>0) {
                        Empleado emp = list2.get(0);
                        if(empleados.contains(emp)) {
                            proy.setResponsable(emp);
                            session.save(proy);
                            System.out.println("Responsable asignado con éxito");
                        } else System.out.println("Error, el empleado introducido no pertenece al proyecto");
                    } else System.out.println("Error, el empleado introducido no existe");
                } else System.out.println("Error, el proyecto no tiene empleados");
            } else System.out.println("Error, el proyecto introducido no existe");
            tx.commit(); 
            
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void listar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
        
        try {
//            tx = session.beginTransaction();
            Query q = session.createQuery("from Departamento order by id");
            List<Departamento> departamentos = q.list();
            for (Departamento dep : departamentos) {
                System.out.println(dep.getId()+". "+dep.getDnombre());
                Set<Empleado> empleados = dep.getEmpleados();
                for(Empleado e: empleados) {
                    System.out.println(e.getNombre()+" "+e.getApellido()+", salario de "+e.getSalario()+"€");
                    e.getDireccion().mostrarDireccion();
                }
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private Integer introducirDireccion(Session session, Transaction tx) throws HibernateException {
        System.out.println("Introduzca dirección:");
        System.out.println("Calle:");
        String calle = sc.nextLine();
        System.out.println("Número:");
        int numero = sc.nextInt();
        sc.nextLine();
        System.out.println("Código postal:");
        String cpostal = sc.nextLine();
        System.out.println("Provincia:");
        String provincia = sc.nextLine();
        
        Integer dirId = 0;
        try{
            Query q = session.createQuery("from Direccion where calle = :calle and numero = :numero and cpostal = :cpostal and provincia = :provincia");
            q.setParameter("calle", calle);
            q.setParameter("numero", numero);
            q.setParameter("cpostal", cpostal);
            q.setParameter("provincia", provincia);
            List l = q.list();
            Direccion dir=null;
            if(l.size()>0) dir = (Direccion) l.get(0);
            if(dir==null) dir = new Direccion(calle,numero,cpostal,provincia);
            
            dirId = (Integer) session.save(dir);
        }catch (HibernateException e) { 
            throw e;
        }
        
        return dirId;
    }
    
    private void introducirInfoFinanciera(Departamento dep, Session session, Transaction tx) throws HibernateException {
        float presupuesto, ingresos, gastos;
        System.out.println("Introduzca presupuesto departamento: ");
        presupuesto = sc.nextFloat();
        sc.nextLine();
        System.out.println("Introduzca ingresos departamento");
        ingresos = sc.nextFloat();
        sc.nextLine();
        System.out.println("Introduzca gastos departamento");
        gastos = sc.nextFloat();
        sc.nextLine();
        
        try {
            InformacionFinancieraDepartamento ifd = new InformacionFinancieraDepartamento(dep, presupuesto, ingresos, gastos);
            session.save(ifd);
        } catch (HibernateException e) {
            throw e;
        }
    }
}
