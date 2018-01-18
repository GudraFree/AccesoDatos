/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tema2.ejercicio08;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

/** -Eliminar departamento y todos sus empleados, dado el código de departamento
 *     delimiter //      
                CREATE OR REPLACE PROCEDURE eliminar_ej8 (IN ndept INT) 

               BEGIN 
               DELETE FROM departamentos WHERE dept_no=ndept; 
               END //
       delimiter ;
 * 
 * 
  -Obtener la suma de los salarios de un departamento dado su código
 *      CREATE OR REPLACE PROCEDURE sumaSal_ej8 (d INT, OUT sumaSal FLOAT)
 *      BEGIN 
 *          SELECT SUM(salario) into sumaSal FROM empleado WHERE dept_no=d; 
 *      END //
 *  delimiter;
  
  * -Cambiar la localización de un departamento, dado el código del departamento y la nueva ubucación Hay que devolver la 
   antigua localización y un valor booleano que indique si ha cambiado la localización
   * 
    delimiter //
    CREATE OR REPLACE PROCEDURE nuevaLoc_ej8 (d INT,nuevaLoc VARCHAR(15) , INOUT antiguaLoc VARCHAR(15))
       BEGIN 
           SELECT loc into antiguaLoc FROM departamentos WHERE dept_no=d; 
           
           UPDATE departamentos set loc=nuevaLoc;
       END //
   delimiter ;

   Utilizando objetos CallableStatement, realizar una aplicación en java que muestre un menú que permita realizar las acciones
   indicadas
 *
 * @author sergioelementary
 */
public class Procedures {
   //---------------PARA SQL--------------------------------//
	
	static String dbName="pruebaAD";
	static String parameters="";
	
	static String MySQL_jdbcDriver="com.mysql.jdbc.Driver";
	static String protocol="jdbc:"+"mysql:";
	static String hostName="localhost";
	
	//static parameters= "noAccessToPocedureBodies=true";
	static String MySQL_url="jdbc:mysql://localhost/"+dbName;
	
	
	
	
	
	//Actual DB parameters
	static String driver=MySQL_jdbcDriver;
        static String url=MySQL_url;	
        
	static String user="root";
	static String password="";
        
        //---------------------SQLITE---------------------
        /*
        static String sqlite_jdbd_driver="org.sqlite.JDBC";
    static String prefix="jdbc:"+"sqlite:";
    static String hostName="";
    static String urlFolder="/home/sergioelementary/ZEjemploBD_AD/";
    static String dbName="ejemplo1.db";
                
                
                
    static String driver=sqlite_jdbd_driver;

    static String user="";
    static String password="";

    static String url=prefix+hostName+urlFolder+dbName;
        */
        
       public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introduzca una opcion");
        System.out.println("1 Eliminar departamento y todos sus empleados, dado el código de departamento");
        System.out.println("2 Obtener la suma de los salarios de un departamento dado su código");
        System.out.println("3 Cambiar la localización de un departamento, dado el código del departamento y la nueva ubucación");
        
        int opc=sc.nextInt();
        
        switch (opc){
            case 1:
                    EliminarProcedureExample();
                break;
            case 2:
                    ObtenerSumaDepartamento();
                break;
            case 3:
                    CambiarLoc();
                break;
            default:
                System.out.println("No existe esa opcion.");
                break;
        
        }
        //procedureOutExample(args);

        //procedureInOutExample();
        //functionExample(args);
    }
    
    public static void EliminarProcedureExample()
	{
				
	 try{	
		 Scanner sc2=new Scanner (System.in);
		 
	
	 	 //Load the driver in RAM
		 Class.forName(driver);
					
	     //Connect to DB
		 Connection connection=DriverManager.getConnection(url,user,password);
					
		 //Get command line parameters and create a call statement string
		 System.out.println("Introduzca el cod de departamento");
                 
		 int dept=sc2.nextInt();
                 //es un procedimiento porque no tiene nada que devolver.
                 
		 String sql="{ call eliminar_ej8 (?)}";
		 
		 //Create the CallableStatement object
		 
		 CallableStatement call= connection.prepareCall(sql);
		 
		 //Set values to parameters 
		 
		 call.setInt(1, dept);
		 
		 
		 
		 //Execute procedure
		 
		int numeroDeFilasBorradas=call.executeUpdate();
		 
		 
		System.out.println("Eliminacion realizada");
		
		call.close();
		connection.close();
                
                System.out.println("Filas borradas = "+numeroDeFilasBorradas);
		 
		 
					
	 }catch(ClassNotFoundException cnfe){  
			System.out.printf("Not found the jdbc driver %s\n", driver);
	 }catch (SQLException sqle){
			System.out.println("SQL Exception");
	 }					
	}
    
     public static void ObtenerSumaDepartamento(){
         
          try{	
		 Scanner sc2=new Scanner (System.in);
		 
	
	 	 //Load the driver in RAM
		 Class.forName(driver);
					
	     //Connect to DB
		 Connection connection=DriverManager.getConnection(url,user,password);
					
		 //Get command line parameters and create a call statement string
		 System.out.println("Introduzca el cod de departamento");
                 
		 int dept=sc2.nextInt();
                 //es un procedimiento porque no tiene nada que devolver.
                 
                          // hacer la variable que vamos a devolver INOUT para poder recogerlo
			 String sql="{ call sumaSal_ej8 (?,?)}";
                         
			 
			 //Create the CallableStatement object
			 
			 CallableStatement call= connection.prepareCall(sql);
			 
			 //Register OUT parameters and set IN parameters 
                         
                                        // ¡IMPORTANTE!
			
			 call.setInt(1,dept);
                         // los INOUT no hace falta ponerle valor y actuan como out.
                         call.setDouble(2, 0.0);
			 call.registerOutParameter(2, Types.DOUBLE);	 
			 
			//Execute function
			 
			call.executeUpdate();
                         
                         
                       
			 
			double sumaSalarioEmpleados=call.getDouble(2);
		 
		 
		System.out.println("Consulta realizada\n");
		
		call.close();
		connection.close();
                
                System.out.println("Suma del salario de los empleados de ese departamento = "+sumaSalarioEmpleados);
		 
		 
					
	 }catch(ClassNotFoundException cnfe){  
			System.out.printf("Not found the jdbc driver %s\n", driver);
	 }catch (SQLException sqle){
			System.out.println("SQL Exception");
	 }
     
     
     }
     
     public static void CambiarLoc(){
      try{	
		 Scanner sc2=new Scanner (System.in);
		 
	
	 	 //Load the driver in RAM
		 Class.forName(driver);
					
	     //Connect to DB
		 Connection connection=DriverManager.getConnection(url,user,password);
					
		 //Get command line parameters and create a call statement string
		 System.out.println("Introduzca el cod de dep departamento");
                 
		 int dept=sc2.nextInt();
                 
                    System.out.println("Introduzca el localizacion dep departamento");
                    sc2.nextLine();
                    String localizacion=sc2.nextLine();
                    
                 //es un procedimiento porque no tiene nada que devolver.
                 
                          // hacer la variable que vamos a devolver INOUT para poder recogerlo
			 String sql="{call nuevaLoc_ej8 (?,?,?)}";
                         
			 
			 //Create the CallableStatement object
			 
			 CallableStatement call= connection.prepareCall(sql);
			 
			 //Register OUT parameters and set IN parameters 
                         
                                        // ¡IMPORTANTE!
			
			 call.setInt(1,dept);
                         call.setString(2, localizacion);
                         // los INOUT no hace falta ponerle valor y actuan como out.
                         //call.setString(3,"");
			 call.registerOutParameter(3, Types.VARCHAR);	 
			 
			//Execute function
			 
			int numeroFilasAfectadas=call.executeUpdate();
                         
                         
                       
			 
			String antiguaLoc=call.getString(3);
                        
                        boolean actualizacionRealizada=false;
                        if(numeroFilasAfectadas>0){actualizacionRealizada=true;}
		 
		System.out.println("Actualizacion realizada= "+actualizacionRealizada+"\n");
		
		call.close();
		connection.close();
                
                System.out.println("Antigua loc= "+antiguaLoc);
                
                
		 
		 
					
	 }catch(ClassNotFoundException cnfe){  
			System.out.printf("Not found the jdbc driver %s\n", driver);
	 }catch (SQLException sqle){
			System.out.println("SQL Exception");
	 }
     
     
     }
}