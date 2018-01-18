package tema2.ejemplos;

import java.sql.*;

public class GestionEmpleadosPreparedStatements {

	//----------Add an external JAR file or create a user library with an specific JDBC driver
	
	//-----------------------JDBC driver and URL of each DBMS-------------------------
	
	//whatever DBMS
	//static String prefix="jdbc:"+"dbms";//<----change "dbms" for a real one (mysql, sqlite, ...)
	//static String hostName="\\localhost\\";
	//static String urlFolder="C:\\Users\\migue\\Documents\\IES Fernando Aguilar - 2012-2013\\DAM\\AD\\tema2\\sqlite\\BD\\";
	//static String dbName="BDGestionEmpleados";
	//static String url="jdbc:sqlite:D:/sqlite/BD/BDGestionEmpleados";
	
	//MySQL
	//static String MySQL_jdbcDriver="com.mysql.jdbc.Driver";
	//static String prefix="jdbc:"+"mysql:";
	//static String MySQL_url="jdbc:mysql://localhost/sqlite/BD/BDGestionEmpleados";
	
	//sqlite3
	static String sqlite_jdbd_driver="org.sqlite.JDBC";
	static String prefix="jdbc:"+"sqlite:";
	static String hostName="";
	static String urlFolder="/home/pub/miguelb/ad/tema2/dbms/sqlite3/";
	static String dbName="ejemplo.db";
	//static String url="jdbc:sqlite:/home/pub/miguelb/ad/tema2/dbms/sqlite3/ejemplo.db";
	static String url="jdbc:sqlite:ejemplo.db";
	
	//jdbcOdbc
	//static String Access_jdbcDriver="sun.jdbc.odbc.JdbcOdbcDriver";
	//static String prefix="jdbc:"+"odbc:";
	//static String SQLite_url="jdbc:odbc:D:/sqlite/BD/BDGestionEmpleados";
	
	
	//---------------------------Actual DB parameters-------------------------------
	static String driver=sqlite_jdbd_driver;
	//static String url=prefix+hostName+urlFolder+dbName;
	
	static String user="";     //static String user="user";
	static String password=""; //static String password="password";
	
	
	/**
	 * <p>The method <b><code>Class.forName(String)</b></code> is used to load the JDBC driver class. 
	 *    The line below causes the JDBC driver from some jdbc vendor to be loaded into the
	 *    application. (Some JVMs also require the class to be instantiated with .newInstance().).
	 *    In JDBC 4.0, it is no longer necessary to explicitly load JDBC drivers using Class.forName().
	 * 
	 * <p>When a Driver class is loaded, it creates an instance of itself and registers 
	 *    it with the DriverManager. This can be done by including the needed code in 
	 *    the driver class's static block. E.g., DriverManager.registerDriver(Driver driver)
	 *    
	 * <p><code>
	        Class.forName( "com.somejdbcvendor.TheirJdbcDriver" );
	      </code>   
	 *    
	 *     
	 * <p>Now when a connection is needed, one of the DriverManager.getConnection() 
	 *    methods is used to create a JDBC connection.
	 * <p>The URL used is dependent upon the particular JDBC driver.
	 *    It will always begin with the "jdbc:" protocol, but the rest is up to the particular vendor. 
	 *    
	 * <p><code>  
	 *    Connection conn = DriverManager.getConnection(
     *                      "jdbc:somejdbcvendor:other data needed by some jdbc vendor",
     *                      "myLogin",
     *                      "myPassword" );
     *    try {
     *         // you use the connection here 
     *    } finally {
     *           //It's important to close the connection when you are done with it
     *    try { conn.close(); } catch (Throwable ignore) { 
     *                          // Propagate the original exception
     *                          // instead of this one that you may want just logged  }
     *    }
     *    </code>
	 *    
	 *<p>Once a connection is established, a statement can be created.
	 *
	 *  <code>
	 *    Statement stmt = conn.createStatement();
     *    try {
     *         stmt.executeUpdate( "INSERT INTO MyTable( name ) VALUES ( 'my name' ) " );
     *    } finally {
     *    //It's important to close the statement when you are done with it
     *    try { stmt.close(); } catch (Throwable ignore) { 
     *                           // Propagate the original exception
     *                           //instead of this one that you may want just logged }
     *}
     * </code>
	 *    
	 *    
	 *    
	 *<p>Note that Connections, Statements, and ResultSets often tie up operating system 
	 *   resources such as sockets or file descriptors. 
	 *   In the case of Connections to remote database servers,
	 *   further resources are tied up on the server, e.g.,
	 *   cursors for currently open ResultSets. It is vital to close() any JDBC object as
	 *   soon as it has played its part; garbage collection should not be relied upon.
	 *   Forgetting to close() things properly results in spurious errors and misbehaviour.
	 *   The above try-finally construct is a recommended code pattern to use with JDBC objects.
	 *    
	 *<p>If a database operation fails, JDBC raises an SQLException. 
	 *   There is typically very little one can do to recover from such an error,
	 *   apart from logging it with as much detail as possible. 
	 *   It is recommended that the SQLException be translated into 
	 *   an application domain exception (an unchecked one) that eventually 
	 *   results in a transaction rollback and a notification to the user.
	 */
	
	public static void main(String[] args)
	{
		
		//-----------------Utilizando objetos PreparedStatement-------------
				
		preparedStatementQueryExample();
		//preparedStatementInsertExample();
		//preparedStatementDeleteExample();
		//preparedStatementUpadateExample();
		
		
	}
	
	
	/**
	 * <p>Typically it would be rare for a seasoned Java programmer
	 *    to code using Statement object. The usual practice would be to abstract 
	 *    the database logic into an entirely different class and to pass
	 *    preprocessed strings (perhaps derived themselves from a further 
	 *    abstracted class) containing SQL statements and the connection to 
	 *    the required methods. Abstracting the data model from the application
	 *    code makes it more likely that changes to the application and data model 
	 *    can be made independently.
	 *    
	 * <p>An example of a PreparedStatement query is  'preparedStatementQueryExample'
	 * 
	 * <p>Examples of PreparedStatement 'updates' are 'preparedStamentInsert/Delete/UpdateExample'
	 */
	
	
	
	public static void preparedStatementQueryExample()
	{
		try {
		     //Load the driver in RAM
		     Class.forName(driver);
					
	         //Connect to DB
		     Connection connection=DriverManager.getConnection(url,user,password);
		
		     PreparedStatement preparedStatement =
				connection.prepareStatement( "SELECT * FROM departamentos WHERE " +
						                                    "dept_no = ? OR dnombre=?" );
			
			    // In the SQL statement being prepared, each question mark is a placeholder
			    // that must be replaced with a value you provide through a "set" method invocation.
			    // The following two method calls replace the two placeholders; the first is
			    // replaced by a integer value, and the second by an string value.
		     
		        preparedStatement.setInt(1,10);
		        preparedStatement.setString(2, "VENTAS" );
			 
			    // The ResultSet, rs, conveys the result of executing the SQL statement.
			    // Each time you call rs.next(), an internal row pointer, or cursor,
			    // is advanced to the next row of the result.  The cursor initially is
			    // positioned before the first row.
			    ResultSet rs = preparedStatement.executeQuery();
			    try {
			         while ( rs.next() ) {
			            int numColumns = rs.getMetaData().getColumnCount();
			            for ( int i = 1 ; i <= numColumns ; i++ ) {
			                // Column numbers start at 1.
			                // Also there are many methods on the result set to return
			                // the column as a particular type. Refer to the Sun documentation
			                // for the list of valid conversions.
			                System.out.println( "COLUMN " + i + " = " + rs.getObject(i) );
			            } // for
			          System.out.println("---------------");
			        } // while
			     }finally {try { rs.close(); }
			               catch (Throwable ignore) {
                                      /* Propagate the original exception
			                          instead of this one that you may want just logged */ 
			              }finally {try { preparedStatement.close(); }
			                        catch (Throwable ignore) { 
			            	                     /* Propagate the original exception
			                                     instead of this one that you may want just logged */ 
			                        }
			                        }
		
		
			     }
		}catch(ClassNotFoundException cnfe){  
		      System.out.printf("Not found the jdbc driver %s\n", driver);
        }catch (SQLException sqle){
		       System.out.println("SQL Exception");
        }
		
		
	}
	
	public static void preparedStatementInsertExample()
	{
		try {
		     //Load the driver in RAM
		     Class.forName(driver);
					
	         //Connect to DB
		     Connection connection=DriverManager.getConnection(url,user,password);
		
		     PreparedStatement preparedStatement =
				connection.prepareStatement("INSERT INTO departamentos VALUES(?,?,?)" );
			
			    // In the SQL statement being prepared, each question mark is a placeholder
			    // that must be replaced with a value you provide through a "set" method invocation.
			    // The following two method calls replace the two placeholders; the first is
			    // replaced by a integer value, and the second by an string value.
		     
		        preparedStatement.setInt(1,60);
		        preparedStatement.setString(2, "VENTAS" );
		        preparedStatement.setString(3, "JAEN" );
			 
			    //
			    
			    try {
			    	int nrows;
			    	
			    	nrows=preparedStatement.executeUpdate(); 
			    	
			    	if (nrows>0)  //In this particular example a single row is inserted
			    	    System.out.println("Inserci�n realizada. N�mero de filas: "+ nrows);
			    	else //In this particular example a single row is inserted
			    		 //and if it fails, an SQLException will be thrown
			    		System.out.println("Ninguna fila insertada");
			     }finally {
			    	 try { preparedStatement.close(); }
                     catch (Throwable ignore) { 
         	                     /* Propagate the original exception
                                  instead of this one that you may want just logged */ 
                     }
                     }
		
		}catch(ClassNotFoundException cnfe){  
		      System.out.printf("Not found the jdbc driver %s\n", driver);
        }catch (SQLException sqle){
		       System.out.println("SQL Exception");
        }
		
		
	}
	
	public static void preparedStatementDeleteExample()
	{
		try {
		     //Load the driver in RAM
		     Class.forName(driver);
					
	         //Connect to DB
		     Connection connection=DriverManager.getConnection(url,user,password);
		
		     PreparedStatement preparedStatement =
				connection.prepareStatement("DELETE FROM departamentos WHERE dept_no =?" );
			
			    // In the SQL statement being prepared, each question mark is a placeholder
			    // that must be replaced with a value you provide through a "set" method invocation.
			    // The following two method calls replace the two placeholders; the first is
			    // replaced by a integer value, and the second by an string value.
		     
		        preparedStatement.setInt(1,60);
		  
			 
			    // The ResultSet, rs, conveys the result of executing the SQL statement.
			    // Each time you call rs.next(), an internal row pointer, or cursor,
			    // is advanced to the next row of the result.  The cursor initially is
			    // positioned before the first row.
			    
			    try {
			    	
                    int nrows;
			    	nrows=preparedStatement.executeUpdate(); 
			    	
			    	if (nrows>0) 
			    		System.out.println("Borrado realizado. Numero de filas: " + nrows);
			    	else 
			    		System.out.println("No se borr� ninguna fila");
			     }finally {
			    	 try { preparedStatement.close(); }
                     catch (Throwable ignore) { 
         	                     /* Propagate the original exception
                                  instead of this one that you may want just logged */ 
                     }
                     }
		
		}catch(ClassNotFoundException cnfe){  
		      System.out.printf("Not found the jdbc driver %s\n", driver);
        }catch (SQLException sqle){
		       System.out.println("SQL Exception");
        }
		
		
	}
	
	public static void preparedStatementUpadateExample()
	{
		try {
		     //Load the driver in RAM
		     Class.forName(driver);
					
	         //Connect to DB
		     Connection connection=DriverManager.getConnection(url,user,password);
		
		     PreparedStatement preparedStatement =
				connection.prepareStatement("UPDATE departamentos SET loc=? WHERE trim(loc) ='CADIZ'" );
			
			    // In the SQL statement being prepared, each question mark is a placeholder
			    // that must be replaced with a value you provide through a "set" method invocation.
			    // The following two method calls replace the two placeholders; the first is
			    // replaced by a integer value, and the second by an string value.
		     
		        preparedStatement.setString(1,"BARCELONA");
		  
			 
			    // The ResultSet, rs, conveys the result of executing the SQL statement.
			    // Each time you call rs.next(), an internal row pointer, or cursor,
			    // is advanced to the next row of the result.  The cursor initially is
			    // positioned before the first row.
			    
			    try {
			    	preparedStatement.executeUpdate(); 
			    	System.out.println("Actualizaci�n realizada");
			     }finally {
			    	 try { preparedStatement.close(); }
                     catch (Throwable ignore) { 
         	                     /* Propagate the original exception
                                  instead of this one that you may want just logged */ 
                     }
                     }
		
		}catch(ClassNotFoundException cnfe){  
		      System.out.printf("Not found the jdbc driver %s\n", driver);
        }catch (SQLException sqle){
		       System.out.println("SQL Exception");
        }
		
		
	}
}

