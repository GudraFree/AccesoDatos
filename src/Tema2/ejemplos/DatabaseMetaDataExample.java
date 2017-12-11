package Tema2.ejemplos;

import java.sql.*;

/**
 * <p>Comprehensive information <b>(Meta data) about the database as a whole</b>. 
 * 
 * <p>This interface is implemented by driver vendors to <b>let users know the capabilities of a Database
 *    Management System (DBMS) in combination with the driver based on JDBC� technology ("JDBC driver")
 *    that is used with it</b>:
 *    <ul>
 *      <li><p><b>Different relational DBMSs often support different features</b>, 
 *    implement features in different ways, and use different data types.
 *      <li><p><b>In addition, a driver may implement a feature on top of what the DBMS offers.</b>
 *      
 *    </ul>
 *    <p>nformation returned by methods in this interface applies to the <b>capabilities of a particular
 *    driver and a particular DBMS working together</b>. 
 *    Note that as used in this documentation, the term "database" is used generically to refer
 *    to both the driver and DBMS. 
 *    
 * <p>A user for this interface is commonly a tool that needs to discover how to deal with the
 *    underlying DBMS. This is especially true <b>for applications that are intended to be used with 
 *    more than one DBMS</b>.
 *    For example, a tool might use the method getTypeInfo to find out what data 
 *    types can be used in a CREATE TABLE statement. Or a user might call the method 
 *    supportsCorrelatedSubqueries to see if it is possible to use a correlated subquery or 
 *    supportsBatchUpdates to see if it is possible to use batch updates. 
 *    
 * <p>Some DatabaseMetaData methods return <b>lists of information in the form of ResultSet objects</b>. 
 *    Regular ResultSet methods, such as getString and getInt, can be used to retrieve the data 
 *    from these ResultSet objects. If a given form of metadata is not available,
 *    an empty ResultSet will be returned.
 *    Additional columns beyond the columns defined to be returned by the ResultSet object for a given
 *    method can be defined by the JDBC driver vendor and must be accessed by their column label. 
 *    
 * <p>Some DatabaseMetaData <b>methods take arguments that are String patterns</b>. 
 *    These arguments all have names such as fooPattern. Within a pattern String, <b>"%"</b> means match 
 *    any substring of 0 or more characters, and <b>"_"</b> means match any one character. 
 *    <b>Only metadata entries matching the search pattern are returned</b>. 
 *    <b>If a search pattern argument is set to null, that argument's criterion will be dropped from 
 *    the search</b>.

 * 
 *
 */



public class DatabaseMetaDataExample {

	//JDBC driver and URL of each DBMS
	
		//static String protocol="jdbc:"+"dbms";//<----change "dbms" for a real one (mysql, sqlite, ...)
		static String hostName="//localhost\\";
		static String urlFolder="C:\\Users\\migue\\Documents\\IES Fernando Aguilar - 2012-2013\\DAM\\AD\\tema2\\sqlite\\BD\\";
		//static String dbName="BDGestionEmpleados";
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
		static String dbName="./database/ejemplo.db";
		
		static String url="jdbc:sqlite:ejemplo.db";
		//static String Access_jdbcDriver="sun.jdbc.odbc.JdbcOdbcDriver";
		//static String protocol="jdbc:"+"odbc:";
		//static String SQLite_url="jdbc:odbc:D:/sqlite/BD/BDGestionEmpleados";
		
		
		//Actual DB parameters
		static String driver=SQLite_jdbcDriver;
		//static String url=protocol+hostName+urlFolder+dbName+parameters;
		static String user="";
		static String password="";
	
	
	public static void main (String [] args){
		
		metadataDBMSandDriverExample();
		
		metadataDBMSFeaturesExample();
		
		metadataDBTablesExample();
		
		metadataDBGetColumnsOfaTableExample();
		
		metadataDBGetPrymaryKeyOfaTableExample();
		
		metadataDBGetExportedKeyOfaTableExample();
		
		metadataDBGetImportedKeyOfaTableExample();
		
		metadataDBGetProceduresExample();
		
		metadataDBResultSetMetadataExample();
	}
	
	
private static void metadataDBMSandDriverExample(){
		
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
	//----->Show general information related to this DBMS and JDBC driver
			
			String nombre  = dbmd.getDatabaseProductName(); //DB engine
			String driver  = dbmd.getDriverName();          //JDBC driver
			String url	   = dbmd.getURL();                 //protocolo://IP/ruta/nombreBD
			String usuario = dbmd.getUserName();			//usuario@m�quina
			
			System.out.println("");
			System.out.println("INFORMACI�N GEN�RICA SOBRE EL SGBD y el driver JDBC");
			System.out.println("===================================================");
			System.out.println("Nombre : " + nombre);
			System.out.println("Driver : " + driver);
			System.out.println("URL    : " + url);
			System.out.println("Usuario: " + usuario);
			
	
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	

    public static void metadataDBMSFeaturesExample()
    {
    	try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
    //----->Show Features of this DBMS
			
			Boolean supportsGroupBy  = dbmd.supportsGroupBy();
			Boolean supportsOuterJoins= dbmd.supportsOuterJoins();
			Boolean supportsMultipleTransactions=dbmd.supportsMultipleTransactions();
			Boolean supportsTransactions=dbmd.supportsTransactions() ;
			Boolean supportsStoredProcedures=dbmd.supportsStoredProcedures();
			
			System.out.println("");
			System.out.println("CARACTER�STICAS SOPORTADAS POR EL SGBD");
			System.out.println("======================================");
			System.out.println("�Soporta 'Group By'?   : " + supportsGroupBy);
			System.out.println("�Soporta 'Outer Join'? : " + supportsOuterJoins);	
			System.out.println("�Soporta Transacciones?: " + supportsTransactions);
			if (supportsTransactions) //Si no soporta transacciones, tampoco permitir� m�ltiples
			   System.out.println("�Soporta Multiples transacciones (sobre diferentes conexiones)? : " 
			                      + supportsMultipleTransactions);	
			System.out.println("�Soporta Procedimientos almacenados? : " + supportsStoredProcedures);
			
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
    }





	
	
	/**
	 * <h3>dbmd.getTables</h3> 
	 * 
	 * ResultSet getTables(String catalog,
	 *                     String schemaPattern,
	 *                     String tableNamePattern,
	 *                     String[] types)
	 *           throws SQLException
	 *         
	 *<p>Parameters:
	 *   <ul> 
	 *    <li>catalog: must match the catalog name as it is stored in the database; 
	 *        "" retrieves those without a catalog; 
	 *        null means that the catalog name should not be used to narrow the searchschemaPattern
	 *    <li>schemaPattern: must match the schema name as it is stored in the database; 
	 *        "" retrieves those without a schema; 
	 *        null means that the schema name should not be used to narrow the searchtableNamePattern
	 *    <li>tableNamePattern: must match the table name as it is stored in the databasetypes
	 *    <li>types: a list of table types, which must be from the list of table types returned from 
	 *        getTableTypes(),to include;
	 *        null returns all types     
	 *   </ul>
	 *       
	 *<p> Returns: ResultSet - each row is a table description
	 *         
	 * <p> Retrieves a description of the tables available in the given catalog.
	 *     Only table descriptions matching the catalog, schema, table name and type criteria are returned.
	 *     They are ordered by TABLE_TYPE, TABLE_CAT, TABLE_SCHEM and TABLE_NAME. 
	 *     
	 *  <p>Each table description has the following columns:
	 *     
	 *     <pre>
	 *      1.TABLE_CAT String => table catalog (may be null) 
			2.TABLE_SCHEM String => table schema (may be null) 
			3.TABLE_NAME String => table name 
			4.TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM". 
			5.REMARKS String => explanatory comment on the table 
			6.TYPE_CAT String => the types catalog (may be null) 
			7.TYPE_SCHEM String => the types schema (may be null) 
			8.TYPE_NAME String => type name (may be null) 
			9.SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null) 
			10.REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)   
	 *     </pre>
	 *           
	 *           
	 *           
	 */
	private static void metadataDBTablesExample(){
		
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about each table or view in this DB
			                                           
			ResultSet result=dbmd.getTables(null, dbName, null, null); //All kind of tables
			
			//String[] elementos= new String[1];
			//elementos[0]="TABLE";
			//ResultSet result=dbmd.getTables(null, dbName, "d%", elementos); //Only relational tables
			                                                                  //Whose name start with 'd'
			
			System.out.println("");
			System.out.println("TABLAS ALMACENADAS EN LA BASE DE DATOS");
			System.out.println("======================================");
			
			while (result.next()){
				String catalogo = result.getString(1); 
				//String catalogo = result.getString("TABLE_CAT");
				
				//String esquema = result.getString(2);
				  String esquema = result.getString("TABLE_SCHEM");
				  
				  String tabla = result.getString(3);    
		        //String tabla = result.getString("TABLE_NAME");
				  
				//String tipo = result.getString(4);     
				  String tipo = result.getString("TABLE_TYPE");
						
			    System.out.println(tipo + "   Catalogo: " + catalogo +
			    		                  "   Esquema : " + esquema  +
			    		                  "   Nombre  : " + tabla    +
			    		                  "   Tipo    : " + tipo);	
			}
			
			
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	
	/**<h3>getColumns</h3>
	 * 
	 * <p>ResultSet getColumns(String catalog, 
	 *                         String schemaPattern, 
	 *                         String tableNamePattern,
	 *                         String columnNamePattern)
	 *              throws SQLException
	 * <p>Retrieves a description of table columns available in the specified catalog. 
	 * <p>Only column descriptions matching the catalog, schema, table and column name criteria 
	 *    are returned. They are ordered by TABLE_CAT,TABLE_SCHEM, TABLE_NAME, and ORDINAL_POSITION.
	 *    
	 * <p>Parameters:
	 * <ul>
	 *    <li><p>catalog - a catalog name; must match the catalog name as it is stored in the database;
	 *           "" retrieves those without a catalog; null means that the catalog name should not be used
	 *            to narrow the searchschemaPattern - a schema name pattern; must match the schema name
	 *            as it is stored in the database; "" retrieves those without a schema;
	 *            null means that the schema name should not be used to narrow the searchtable
	 *    <li><p>NamePattern - a table name pattern; must match the table name as it is stored 
	 *           in the database
	 *    <li><p>columnNamePattern - a column name pattern; must match the column name as it is stored
	 *           in the database
	 * </ul>
	 *  
	 * <p> Returns: ResultSet - each row is a column description
	 *    
	 * <p>Each column description has the following columns: 
	 * <pre>
1.TABLE_CAT String => table catalog (may be null) 
2.TABLE_SCHEM String => table schema (may be null) 
3.TABLE_NAME String => table name 
4.COLUMN_NAME String => column name 
5.DATA_TYPE int => SQL type from java.sql.Types 
6.TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified 
7.COLUMN_SIZE int => column size. 
8.BUFFER_LENGTH is not used. 
9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable. 
10.NUM_PREC_RADIX int => Radix (typically either 10 or 2) 
11.NULLABLE int => is NULL allowed.
                    � columnNoNulls - might not allow NULL values 
					� columnNullable - definitely allows NULL values 
					� columnNullableUnknown - nullability unknown 

12.REMARKS String => comment describing column (may be null) 
13.COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) 
14.SQL_DATA_TYPE int => unused 
15.SQL_DATETIME_SUB int => unused 
16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column 
17.ORDINAL_POSITION int => index of column in table (starting at 1) 
18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column. 
					� YES --- if the column can include NULLs 
					� NO --- if the column cannot include NULLs 
					� empty string --- if the nullability for the column is unknown 

19.SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) 
20.SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
21.SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
23.IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
					� YES --- if the column is auto incremented 
					� NO --- if the column is not auto incremented 
					� empty string --- if it cannot be determined whether the column is auto incremented 

24.IS_GENERATEDCOLUMN String => Indicates whether this is a generated column 
					� YES --- if this a generated column 
					� NO --- if this not a generated column 
					� empty string --- if it cannot be determined whether this is a generated column
      </pre>
	 * 
	 * <p> The COLUMN_SIZE column specifies the column size for the given column.
	 *     For numeric data, this is the maximum precision. For character data, 
	 *     this is the length in characters. For datetime datatypes, this is the length in characters 
	 *     of the String representation (assuming the maximum allowed precision of the fractional seconds
	 *     component). For binary data, this is the length in bytes. For the ROWID datatype, 
	 *     this is the length in bytes. Null is returned for data types where the column size is not applicable.
	 */
	
	private static void metadataDBGetColumnsOfaTableExample()
	{
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about a table or view in this DB
			
			String tableName="empleados";
			String columnName="d%"; //Columns whose tame start with 'd'
			ResultSet columnas=dbmd.getColumns(null, dbName, tableName, columnName); //Informaci�n sobre columnas
			                                                           //null means al columns
			 			                                                   
			
			System.out.println("");
			System.out.println("COLUMNAS DE TABLA(S) DE LA BASE DE DATOS");
			System.out.println("=========================================");
			
			while (columnas.next()){
				//String nombreTabla=columnas.getString(3);
				  String nombreTabla=columnas.getString("TABLE_NAME");
				  
				  String nombreColumna = columnas.getString(4); 
			    //String nombreColumna = columnas.getString("COLUMN_NAME");
				  
				//String tipoColumna = columnas.getString(6);
				  String tipoColumna = columnas.getString("TYPE_NAME");
				  
				  String tamanoColumna = columnas.getString(7);    
			   // String tamanoColumna = columnas.getString("COLUMN_SIZE");
				  
				//String nula = columnas.getString(18);     
				  String nula = columnas.getString("IS_NULLABLE");
						
			    System.out.println("   Tabla:       : " + nombreTabla   +
			    		           "   Columna      : " + nombreColumna +
			    		           "   Tipo         : " + tipoColumna   +
			    		           "   Tama�o       : " + tamanoColumna +
			    		           "   Admite nulos : " + nula);	
			}
					
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * <h3>getPrimaryKeys</h3>
	 * 
	 * 
	 * <p>ResultSet getPrimaryKeys(String catalog,
	 *                          String schema,
	 *                          String table)
	 *                         throws SQLException
	 *                         
	 *                         
	 *  <p><b>Retrieves a description of the given table's primary key columns</b>. They are ordered by COLUMN_NAME.
	 *  <p>Each primary key column description has the following columns: 
	 *  <pre>
1.TABLE_CAT String => table catalog (may be null) 
2.TABLE_SCHEM String => table schema (may be null) 
3.TABLE_NAME String => table name 
4.COLUMN_NAME String => column name 
5.KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key). 
6.PK_NAME String => primary key name (may be null)

 </pre>
 
<p> Parameters:catalog - a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the searchschema - a schema name; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the searchtable - a table name; must match the table name as it is stored in the databaseReturns:ResultSet - each row is a primary key column description
	 */
	private static void metadataDBGetPrymaryKeyOfaTableExample()
	{
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about a given table primary key's columns
			
			String tableName="empleados";
			ResultSet primaryKeyColumn=dbmd.getPrimaryKeys(null, dbName, tableName); //Columnas que forma la clave primaria
			                                               //null means all catalogs
			 			                                                   
			
			System.out.println("");
			System.out.println("CLAVE PRIMARIA DE TABLA(S) DE LA BASE DE DATOS");
			System.out.println("==============================================");
			
			String primaryKey="";
			String separador="";
			while (primaryKeyColumn.next()){
				
				//System.out.println("Tabla :::::" + primaryKeyColumn.getString("TABLE_NAME"));
				//System.out.println("-->Nombre de la Clave Primaria: " + primaryKeyColumn.getString("PK_NAME"));
				  primaryKey= primaryKey + separador + primaryKeyColumn.getString("COLUMN_NAME"); //getString(4)
				//System.out.println("Sequence number" + primaryKeyColumn.getString("KEY_SEQ"));
				  separador="+";
			}
			
			System.out.println("Tabla :" + tableName);
						
			System.out.println("-->Campos que componen la Clave Primaria: " + primaryKey);	
					
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	
	}
	
	/**
	 * <h3>getExportedKeys</h3>
	 * 
	 * ResultSet getExportedKeys(String catalog,     
	 *                           String schema,
	 *                           String table)
	 *           throws SQLException
	 *           
	 *<p><b>Retrieves a description of the foreign key columns that reference the given 
	 *      table's primary key columns (the foreign keys exported by a table)</b>.
	 *     They are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ. 
	 *     
	 *<p>Each foreign key column description has the following columns:
	 *
	 *<pre>
1.PKTABLE_CAT String => primary key table catalog (may be null) 
2.PKTABLE_SCHEM String => primary key table schema (may be null) 
3.PKTABLE_NAME String => primary key table name 
4.PKCOLUMN_NAME String => primary key column name 
5.FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null) 
6.FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null) 
7.FKTABLE_NAME String => foreign key table name being exported 
8.FKCOLUMN_NAME String => foreign key column name being exported 
9.KEY_SEQ short => sequence number within foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). 
10.UPDATE_RULE short => What happens to foreign key when primary is updated: 
		� importedNoAction - do not allow update of primary key if it has been imported 
		� importedKeyCascade - change imported key to agree with primary key update 
		� importedKeySetNull - change imported key to NULL if its primary key has been updated 
		� importedKeySetDefault - change imported key to default values if its primary key has been updated 
		� importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 

11.DELETE_RULE short => What happens to the foreign key when primary is deleted. 
		� importedKeyNoAction - do not allow delete of primary key if it has been imported 
		� importedKeyCascade - delete rows that import a deleted key 
		� importedKeySetNull - change imported key to NULL if its primary key has been deleted 
		� importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
		� importedKeySetDefault - change imported key to default if its primary key has been deleted 

12.FK_NAME String => foreign key name (may be null) 
13.PK_NAME String => primary key name (may be null) 
14.DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit 
		� importedKeyInitiallyDeferred - see SQL92 for definition 
		� importedKeyInitiallyImmediate - see SQL92 for definition 
		� importedKeyNotDeferrable - see SQL92 for definition 
*</pre>
*
*<p>Parameters:
*
*<ul>
*<li>catalog - a catalog name; must match the catalog name as it is stored in this database;
*   "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
* <li>schema - a schema name; must match the schema name as it is stored in the database;
*     "" retrieves those without a schema; 
*     null means that the schema name should not be used to narrow the search
* <li>table - a table name; must match the table name as it is stored in this database
	 *
*</ul>
*
*<p>Returns:a ResultSet object in which each row is a foreign key column 
*   descriptionThrows:SQLException - if a database access error occurs
	 * 
	 */
	
	private static void metadataDBGetExportedKeyOfaTableExample()
	{
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about the 'Exported keys' by a given table
			
			String tableName="departamentos";
			//String tableName="empleados"; //No exporta ninguna clave
			//Columnas que 'Exporta' la tabla (son refernciadas desde otra tabla).
			//De cada 'exportaci�n' se puede obtener a que otra(s) tabla(s) se exporta 
			//(en esa(s) otra(s) tabla(s) ser�(n) clave ajena)
			ResultSet ExportedKeyColumn=dbmd.getExportedKeys(null, dbName, tableName); 
			                                               //null means all catalogs
			 			                                                   
			
			System.out.println("");
			System.out.println("CLAVES EXPORTADAS POR LA TABLA " + tableName);
			System.out.println("================================================================");
			
			
			while (ExportedKeyColumn.next()){
			
				String fk_name=ExportedKeyColumn.getString("FKCOLUMN_NAME");
				String fk_tablename=ExportedKeyColumn.getString("FKTABLE_NAME");
				
				String pk_name=ExportedKeyColumn.getString("PKCOLUMN_NAME");
				String pk_tablename=ExportedKeyColumn.getString("PKTABLE_NAME");
				
				
				System.out.println("Tabla PK (tabla que exporta): " + pk_tablename + ", Clave primaria: " + pk_name);
				System.out.println("Tabla FK: " + fk_tablename + ", Clave ajena: " + fk_name);
				System.out.println("");
			}			
			    	
					
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	
	}
	
	
	private static void metadataDBGetImportedKeyOfaTableExample()
	{
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about a table or view in this DB
			
			String tableName="empleados"; //'Importa' una columna: clave ajena respecto a departamento
			//String tableName="departamentos"; //No tiene claves ajenas
			
			//Columnas que 'Importa' la tabla (son refernciadas desde otra tabla).
			//De cada 'exportaci�n' se puede obtener a que otra(s) tabla(s) se exporta 
			//(en esa(s) otra(s) tabla(s) ser�(n) clave ajena)
			ResultSet foreignKeyColumn=dbmd.getImportedKeys(null, dbName, tableName); //Columnas que forma la clave primaria
			                                               //null means all catalogs
			 			                                                   
			
			System.out.println("");
			System.out.println("CLAVE(S) AJENA(S) DE LA TABLA " + tableName);
			System.out.println("==============================================");
			
			
			while (foreignKeyColumn.next()){
				
				String fk_name=foreignKeyColumn.getString("FKCOLUMN_NAME");
				String fk_tablename=foreignKeyColumn.getString("FKTABLE_NAME");
				
				String pk_name=foreignKeyColumn.getString("PKCOLUMN_NAME");
				String pk_tablename=foreignKeyColumn.getString("PKTABLE_NAME");
				
				
				System.out.println("Tabla PK: " + pk_tablename + ", Clave primaria: " + pk_name);
				System.out.println("Tabla FK (tabla que importa): " + fk_tablename + ", Clave ajena: " + fk_name);
				System.out.println("");
				
			}			
			    	
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}
	
	private static void metadataDBGetProceduresExample()
	{
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
	//----->Get a DatabaseMetaData object related to this DB
			DatabaseMetaData dbmd=conexion.getMetaData();
			
			
			
	//----->Show information about a table or view in this DB
			
			String tableName="empleados"; //'Importa' una columna: clave ajena respecto a departamento
			//String tableName="departamentos"; //No tiene claves ajenas
			
			//Columnas que 'Importa' la tabla (son refernciadas desde otra tabla).
			//De cada 'exportaci�n' se puede obtener a que otra(s) tabla(s) se exporta 
			//(en esa(s) otra(s) tabla(s) ser�(n) clave ajena)
			ResultSet procedimientos=dbmd.getProcedures(null, dbName, null);
			                                               //null means all
			 			                                                   
			
			System.out.println("");
			System.out.println("PROCEDIMIENTOS ALMACENADOS EN LA BD");
			System.out.println("====================================");
			
			
			while (procedimientos.next()){
				
				String proc_name=procedimientos.getString("PROCEDURE_NAME");
				String proc_type=procedimientos.getString("PROCEDURE_TYPE");
				
				System.out.println("Nombre de procedimiento: " + proc_name + "-Tipo: " + proc_type);
				System.out.println("");
				
			}			
			    	
			conexion.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	
	}
	
	/**
	 * public <h3>interface ResultSetMetaData<h3> extends Wrapper
     * 
	 * <p><b>An object that can be used to get information about the types and properties of the columns
	 *       in a ResultSet object</b>.
	 *   
	 * <p>The following code fragment creates the ResultSet object rs, 
	 *    creates the ResultSetMetaData object rsmd, and uses rsmd to find out how many columns rs has 
	 *    and whether the first column in rs can be used in a WHERE clause. 
	 *    <pre>
     ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM TABLE2");
     ResultSetMetaData rsmd = rs.getMetaData();
     int numberOfColumns = rsmd.getColumnCount();
     boolean b = rsmd.isSearchable(1);
     *
     *</pre>
	 * 
	 * 
	 * 
	 */
	
	private static void metadataDBResultSetMetadataExample()
	{
		String query="SELECT * FROM departamentos";
		
		try{
			
			 //Load the driver in RAM
			Class.forName(driver);
		
			//Connect to DB
			Connection conexion= DriverManager.getConnection(url,user,password);
			
			System.out.println("");
			System.out.println("Metadatos obtenidos de un ResultSet");
			System.out.println("====================================");
			
			
			//Execute the query and get a ResultSet
			Statement statement=conexion.createStatement();
			ResultSet rs=statement.executeQuery(query);
			 
			//Get a ResultSetMetadata on the ResultSet object
			ResultSetMetaData rsmd=rs.getMetaData();
			 
			int nColumnas=rsmd.getColumnCount(); //N�mero de columnas de cada tupla del resultSet
			String nula;
			System.out.println("N�mero de columas de las tuplas de la consulta: " + nColumnas);
			
			 //Interrogamos al objeto ResultSetMetaData, para cada una de las columnas
			
			for(int i=1;i<=nColumnas;i++){
				System.out.println("Columna " + i + ":");
				System.out.println("Nombre :" + rsmd.getColumnName(i));
				System.out.println("Tipo : " + rsmd.getColumnTypeName(i));
				
				if (rsmd.isNullable(i)==0) nula="NO"; else nula="SI";
				System.out.println("�Puede ser nula: " + nula);
				
				System.out.println("M�ximo ancho de la coluimna: " + rsmd.getColumnDisplaySize(i));
			}
			 
			 rs.close(); //close ResultSet
			 statement.close();//close Statement
			 conexion.close();//close Connection
						
		 }catch(ClassNotFoundException cnfe){  
				System.out.printf("Not found the jdbc driver %s\n", driver);
		 }catch (SQLException sqle){
				System.out.println("SQL Exception");
		 }

	
	}
	
}
