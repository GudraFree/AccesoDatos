
package Tema2.ejemplos;

import java.sql.*;


/**
// * <h3> Stored Procedures <h3>
 * 
 * <p>A stored procedure is a group of SQL statements that form a logical unit and perform a particular task, 
 * and they are used to encapsulate a set of operations or queries to execute on a database server. 
 * For example, operations on an employee database (hire, fire, promote, lookup) could be coded as stored
 * procedures executed by application code. 
 * Stored procedures can be compiled and executed with different parameters and results, and they can have any combination of input, 
 * output, and input/output parameters. Note that stored procedures are supported by most DBMSs, 
 * but there is a fair amount of variation in their syntax and capabilities.
 * 
 * 
 * <h3> Granting  </h3>
 * <p>The user that connects to the database, must have privileges to execute procedures. 
 * For instance, in MySql the user must have privileges on mysql.proc system table (that holds information over all stored procedures
 * the data base. Therefore, if is necessary, execute the following SQL command on the SQL command line:
 * <p> <code> Grant SELECT ON mysql.proc TO 'usuario'@'maquina'  </code>
 * 
 * <p> It's too necessary set the parameter <i>noAccessToProcedureBodies</i> to <i>true</i> in the connection string to database
 * 
 * <p> "jdbc:mysql:/localhost/mysql/BD/BDGestionEmpleados?noAccessToProcedureBodies=true";
 * 
 * 
 * <h3> CallableStatement </h3>
 * 
 * <p>The interface used to execute SQL stored procedures. The JDBC API provides a stored procedure SQL escape syntax that allows
 *    stored procedures to be called in a standard way for all RDBMSs. This escape syntax has one form that includes a result parameter
 *    and one that does not. If used, the result parameter must be registered as an OUT parameter. The other parameters can be used for input, 
 *    output or both. Parameters are referred to sequentially, by number, with the first parameter being 1. 
 * 
 * 
 * <pre>
 * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
 * {call <procedure-name>[(<arg1>,<arg2>, ...)]}
 * </pre>
 * 
 * 
 * <p>  A CallableStatement can return one ResultSet object or multiple ResultSet objects. 
 * Multiple ResultSet objects are handled using operations inherited from Statement.
 * For maximum portability, a call's ResultSet objects and update counts should be processed prior to getting the values of output parameters.
 * 
 * 
 * <h3> java.sql.Types  </h3>
 * 
 * The class that defines the constants that are used to identify generic SQL types, called JDBC types. This class is never instantiated.
 * 
 *<h3>Parameter Modes</h3>
 *
 *<p>The parameter attributes IN (the default), OUT, and INOUT are parameter modes. They define the action of formal parameters.
 *   The following table summarizes the information about parameter modes.
 *   
 *   
 *<p><b>IN</b> parameter values are set using the <code>set</code> methods inherited from PreparedStatement.
 *<p><b>The type of all <b>OUT</b> parameters must be registered prior to executing the stored procedure; 
 *their values are retrieved after execution via the <code>get</code> methods provided here
 *   
 *   
 *   
 *   <table summary="This table summarizes parameter modes." border="1">

<tr>
<th id="h1">Characteristic of Parameter Mode</th>
<th id="h2">IN</th>
<th id="h3">OUT</th>
<th id="h4">INOUT</th>
</tr>

<tbody>
<tr>
<td headers="h1">
<p>Must it be specified in the stored procedure definition?</p>
</td>
<td headers="h2">
<p>No; if omitted, then the parameter mode of the formal parameter is <code>IN</code>.</p>
</td>
<td headers="h3">
<p>Must be specified.</p>
</td>
<td headers="h4">
<p>Must be specified.</p>
</td>
</tr>
<tr>
<td headers="h1">
<p>Does the parameter pass a value to the stored procedure or return a value?</p>
</td>
<td headers="h2">
<p>Passes values to a stored procedure.</p>
</td>
<td headers="h3">
<p>Returns values to the caller.</p>
</td>
<td headers="h4">
<p>Both; passes initial values to a stored procedure; returns updated values to the caller.</p>
</td>
</tr>
<tr>
<td headers="h1">
<p>Does the formal parameter act as a constant or a variable in the stored procedure?</p>
</td>
<td headers="h2">
<p>Formal parameter acts like a constant.</p>
</td>
<td headers="h3">
<p>Formal parameter acts like an uninitialized variable.</p>
</td>
<td headers="h4">
<p>Formal parameter acts like an initialized variable.</p>
</td>
</tr>
<tr>
<td headers="h1">
<p>Can the formal parameter be assigned a value in the stored procedure?</p>
</td>
<td headers="h2">
<p>Formal parameter cannot be assigned a value.</p>
</td>
<td headers="h3">
<p>Formal parameter cannot be used in an expression; must be assigned a value.</p>
</td>
<td headers="h4">
<p>Formal parameter must be assigned a value.</p>
</td>
</tr>
<tr>
<td headers="h1">
<p>What kinds of actual parameters (arguments) can be passed to the stored procedure?</p>
</td>
<td headers="h2">
<p>Actual parameter can be a constant, initialized variable, literal, or expression.</p>
</td>
<td headers="h3">
<p>Actual parameter must be a variable.</p>
</td>
<td headers="h4">
<p>Actual parameter must be a variable.</p>
</td>
</tr>
</tbody>
</table>
 *   
 *   
 *
 */

public class CallableExample {

	
	//JDBC driver and URL of each DBMS
	
	//static String protocol="jdbc:"+"dbms";//<----change "dbms" for a real one (mysql, sqlite, ...)
	static String hostName="//localhost\\";
	static String urlFolder="C:\\Users\\migue\\Documents\\IES Fernando Aguilar - 2012-2013\\DAM\\AD\\tema2\\sqlite\\BD\\";
	static String dbName="BDGestionEmpleados";
	static String parameters="";
	
	//static String MySQL_jdbcDriver="com.mysql.jdbc.Driver";
	//static String protocol="jdbc:"+"mysql:";
	//static String hostName="//localhost/";
	//static String url=sqlite/BD/BDGestionEmpleados;
	//static parameters= "noAccessToPocedureBodies=true";
	//static String MySQL_url="jdbc:mysql://localhost/sqlite/BD/BDGestionEmpleados";
	
	static String SQLite_jdbcDriver="org.sqlite.JDBC";
	static String protocol="jdbc:"+"sqlite:";
	//static String SQLite_url="jdbc:sqlite:D:/sqlite/BD/BDGestionEmpleados";
	
	//static String Access_jdbcDriver="sun.jdbc.odbc.JdbcOdbcDriver";
	//static String protocol="jdbc:"+"odbc:";
	//static String SQLite_url="jdbc:odbc:D:/sqlite/BD/BDGestionEmpleados";
	
	
	//Actual DB parameters
	static String driver=SQLite_jdbcDriver;
	static String url=protocol+hostName+urlFolder+dbName+parameters;
	static String user="user";
	static String password="password";
	
	
	public static void main(String[] args)
	{
		
		//procedureOutExample(args);
		
		//procedureInOutExample();
		
		functionExample(args);
	}
	
	
	public static void procedureOutExample(String [] args)
	{
				
	 try{	
		 if (args.length!=2){ 
			 System.out.println("Help: CallableExample codDept Incremento; por ejemplo: CallableExample 30 200");
			 return;
		 }
		 
		 
	 	 //Load the driver in RAM
		 Class.forName(driver);
					
	     //Connect to DB
		 Connection connection=DriverManager.getConnection(url,user,password);
					
		 //Get command line parameters and create a call statement string
		 
		 String dep=args[0];
		 String increase=args[1];
		 String sql="{ call subida_sal (?,?)}";
		 
		 //Create the CallableStatement object
		 
		 CallableStatement call= connection.prepareCall(sql);
		 
		 //Set values to parameters 
		 
		 call.setInt(1, Integer.parseInt(dep));
		 call.setFloat(2, Float.parseFloat(increase));
		 
		 
		 //Execute procedure
		 
		call.executeUpdate();
		 
		 
		System.out.println("Subida realizada");
		
		call.close();
		connection.close();
		 
		 
					
	 }catch(ClassNotFoundException cnfe){  
			System.out.printf("Not found the jdbc driver %s\n", driver);
	 }catch (SQLException sqle){
			System.out.println("SQL Exception");
	 }					
	}
	
	
	
	public static void functionExample(String[] args)
	{
		try{	
			 if (args.length!=1){ 
				 System.out.println("Help: CallableExample codDept; por ejemplo: CallableExample 30");
				 return;
			 }
			 
			 
		 	 //Load the driver in RAM
			 Class.forName(driver);
						
		     //Connect to DB
			 Connection connection=DriverManager.getConnection(url,user,password);
						
			 //Get command line parameters and create a call statement string
			 
			 String dep=args[0];
	
			 String sql="{ ? = call nombre_dep (?,?)}";
			 
			 //Create the CallableStatement object
			 
			 CallableStatement call= connection.prepareCall(sql);
			 
			 //Register OUT parameters an set IN parameters 
			 
			 call.registerOutParameter(1, Types.VARCHAR);
			 call.setInt(2, Integer.parseInt(dep));
			 call.registerOutParameter(3, Types.VARCHAR);
			 
			 
			//Execute function
			 
			call.executeUpdate();
			 
			 
			System.out.println("Nombre Dep:" + call.getString(1) + " Localidad: " + call.getString(3));
			
			call.close();
			connection.close();
			 
			 
						
		 }catch(ClassNotFoundException cnfe){  
				System.out.printf("Not found the jdbc driver %s\n", driver);
		 }catch (SQLException sqle){
				System.out.println("SQL Exception");
		 }			
	}
	
}
