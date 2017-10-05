package Tema1.ejercicio11;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Float;
import java.lang.Character;

public class GestionEmpleadosFicheroTextoSecuencial {

    static File employeeFile;
    static FileWriter employeeFileWriter;
    static FileReader employeeFileReader;

    static public class EmployeeRAMRecord {

        public int code;
        public String name;
        public String surname;
        public String birthDate;
        public float salary;
        public String carId;

        void show() {
            System.out.println("Code:       " + code);
            System.out.println("Name:       " + name);
            System.out.println("Surname:    " + surname);
            System.out.println("Birth Date: " + birthDate);
            System.out.println("Salary:     " + salary);
            System.out.println("Car Id:     " + carId);
        }
    }

    static EmployeeRAMRecord employeeRAMrecord;

    //Employee Secondary Memory record:
    static private class EmployeeSM {

        final static String EOFIELD = "|";
        final static String EORECORD = System.getProperty("line.separator");

        //A String containing the field or record terminating characters is not allowed
        final static boolean isValidString(String s) {
            if (s.contains(EOFIELD) || s.contains(EORECORD)) {
                return false;
            } else {
                return true;
            }
        }
    }

    static Scanner scanner = new Scanner(System.in);

    private static class Menu {

        final static int MAXOPCION = 8; //It should be the ordinal of the last enum's value

        //Warning: The sequence of the values in the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu
        enum opcionesMenu {
            create, open, add, erase, change, show, close, end
        }

        static opcionesMenu[] arrayOpcionesMenu = opcionesMenu.values();

        private static opcionesMenu menu() {

            //Scanner cadena= new Scanner(System.in); 
            int opcion;

            do {

                System.out.println("________________________________________________________________________________________________________________________");

                System.out.println();

                System.out.println("                                     An example of managament of a sequential access text file");

                System.out.println();

                System.out.println("Write the number of the desired option and press intro: \n");

                System.out.println();

                System.out.println("--------------------Define the characteristic of the system-dependent process that will be created---------------------");

                System.out.println("01-Create a new employee file, deleting the previous one if it exists");

                System.out.println("02-Open the Employee file");

                System.out.println("03-Add a new employee");

                System.out.println("04-Erase an employee");

                System.out.println("05-Change data of an employee");

                System.out.println("06-Show the data of an employee");

                System.out.println("07-Close the Employee file");

                System.out.println("08-Quit");

                System.out.println("___________________________________________________________________________________________________________________________");

                System.out.println("");

                opcion = scanner.nextInt();

            } while (opcion < 1 || opcion > MAXOPCION);

            scanner.nextLine();//We consume all the characters left in the actual line (almost, it will be a 'carriage return' because we have read something yet)

            return arrayOpcionesMenu[opcion - 1]; //Warning: The sequence of the values of the enum 'opcionesMenu' must be THE SAME as the integer values showed in the Menu

        }

    }

    public static void main(String[] args) {

        System.out.println(System.getProperty("line.separator"));
        Menu.opcionesMenu opcion;

        while ((opcion = Menu.menu()) != Menu.opcionesMenu.end) {

            switch (opcion) {

                case create: {
                    String route;

                    do {
                        System.out.println("Write the file's route");
                        route = scanner.nextLine();
                    } while (route.equals("")); //We ensure that 'route' is not empty

                    employeeFile = new File(route);

                    if (employeeFile.isFile()) {
                        employeeFile.delete();
                    }

                    //Nota: en Java NO es necesario 'crear' el fichero como hacemos aqu� (creamos un fichero vacio)
                    //Cuando se cree el objeto 'FileWriter' para escribir datos (y cerr�ndolo despu�s con '.close()') se crear� el fichero
                    try {
                        employeeFileWriter = new FileWriter(employeeFile);
                        employeeFileWriter.close();
                    } catch (IOException ioe) {
                        System.err.println("I/O error: Is not posible to create the file");
                    }

                }
                break;

                case open: {
                    String route;
                    //Nota: En Java NO hay un m�todo 'abrir' un archivo; aqu� simplemente creamos un objeto 'File' que luego se usar�
                    //      para crear un objeto 'FileReader' o 'FileWriter'
                    //      El fichero se abrir� cuando se cree correctamente el objeto 'FileReader' o 'FileWriter' 
                    //      (y no se haya lanzado IOException)

                    do {
                        System.out.println("Write the file's route");
                        route = scanner.nextLine();
                    } while (route.equals("")); //We ensure that 'route' is not empty

                    employeeFile = new File(route);

                    if (!employeeFile.isFile()) {
                        System.err.println("There isn't exists the file " + route);
                    }

                }
                break;

                case add: {
                    System.out.println("We append a new record AT THE END of the sequential access file");

                    //1-We create the employee record in RAM
                    employeeRAMrecord = new EmployeeRAMRecord();

                    System.out.println("Write the employee code (as an positive integer between 1 and 9999)");
                    employeeRAMrecord.code = scanner.nextInt();
                    scanner.nextLine(); //We consume '\n'
                    System.out.println("Write the employee name");
                    employeeRAMrecord.name = scanner.nextLine();
                    System.out.println("Write the employee surname");
                    employeeRAMrecord.surname = scanner.nextLine();
                    System.out.println("Write the employee birth date (format: 'dd-mm-yyyy')");
                    employeeRAMrecord.birthDate = scanner.nextLine();
                    System.out.println("Write the employee salary");
                    employeeRAMrecord.salary = scanner.nextFloat();
                    System.out.println("Write the employee car ID (write 'no car' if the employee has no car)");
                    scanner.nextLine();//We consume '\n'
                    employeeRAMrecord.carId = scanner.nextLine();

                    //2-We check the correctness of each field in the RAM record
                    //Nota: Hubiera sido mejor depurar la entrada para evitar que aparezcan los caracteres
                    //      prohibidos de 'fin de campo' y 'fin de registro'
                    if (employeeRAMrecord.code < 1 || employeeRAMrecord.code > 9999) {
                        System.err.println("The code is out of range [1-9999]: " + employeeRAMrecord.name);
                        //�*! We should ensure the unicity of each employee's code, for example: another file with not-used codes
                        break;
                    }
                    if (!EmployeeSM.isValidString(employeeRAMrecord.name)) {
                        System.err.println("An invalid character found in " + employeeRAMrecord.name);
                        break;
                    }
                    if (!EmployeeSM.isValidString(employeeRAMrecord.surname)) {
                        System.err.println("An invalid character found in " + employeeRAMrecord.surname);
                        break;
                    }
                    if (!EmployeeSM.isValidString(employeeRAMrecord.birthDate)) {
                        System.err.println("An invalid character found in " + employeeRAMrecord.birthDate);
                        //�*! We should ensure the field format correctness (dd-mm-yyyy)
                        break;
                    }
                    if (employeeRAMrecord.salary < 0 || employeeRAMrecord.salary > 99999) {
                        System.err.println("The salary is out of range [0-99999] " + employeeRAMrecord.salary);
                        break;
                    }

                    if (!EmployeeSM.isValidString(employeeRAMrecord.carId)) {
                        System.err.println("An invalid character founded in " + employeeRAMrecord.carId);
                        //�*! We should ensure the field format correctness ('nnnnlll' or 'llnnnnll')
                        break;
                    }
                    if (employeeRAMrecord.carId.equals("no car")) {
                        employeeRAMrecord.carId = ""; //If the employee has no car, this file should be empty
                    }

                    //3-We write the employee record from RAM to file
                    /*
	                           System.out.println("Size of code field: "      + new Integer(employeeRAMrecord.code).toString().length()                 +"\n"         +
	                        		              "Size of name field: "      + employeeRAMrecord.name.length()                                         +"\n"         +
	                        		              "Size of surname field: "   + employeeRAMrecord.surname.length()                                      +"\n"         +
	                        		              "Size of birthDate field: " + employeeRAMrecord.birthDate.length()                                    +"\n"         +
	                        		              "Size of salary field: "    + new Float(employeeRAMrecord.salary).toString().length()                 +"\n"         +
	                        		              "Size of carId field: "     + employeeRAMrecord.carId.length()                                        +"\n"    
	                        		             );
                     */
                    try {
                        //3a-We create the 'FileWriter', and therefore, we 'open' the file
                        employeeFileWriter = new FileWriter(employeeFile, true);//the second argument 'true' allows append the new record at the end of the file
                        //(if it's false or is not present, the content of the file is erased

                        //3b-We write to the file through the 'FileWrite' object 
                        employeeFileWriter.append(new Integer(employeeRAMrecord.code).toString());
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(employeeRAMrecord.name);
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(employeeRAMrecord.surname);
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(employeeRAMrecord.birthDate);
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(new Float(employeeRAMrecord.salary).toString());
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(employeeRAMrecord.carId);
                        employeeFileWriter.append(EmployeeSM.EOFIELD);
                        employeeFileWriter.append(EmployeeSM.EORECORD);

                        //3c-We close the 'FileWriter' object, and therefore, we 'close' the file
                        employeeFileWriter.close();
                    } catch (IOException ioe) {
                        System.err.println("An I/O error occurs and no employee was added");
                    }

                }
                break;

                case erase: {
                    System.out.println("We search starting from the first record of the sequential access file and copy each record to a temporary file "
                            + "Except the record to be erased. Then, the original file is erased and the temporary file renamed");

                    // 1-We ask the user as many fields as fields should match the file
                    employeeRAMrecord = new EmployeeRAMRecord();

                    System.out.println("Write the employee code (as an positive integer between 1 and 9999)");
                    int code = scanner.nextInt();
                    scanner.nextLine(); // We consume '\n'

                    // �*!Se podr�a crear un registro con aquellos campos que deben
                    // coincidir con el/los buscado(s)
                    // Aqu� vamos a seguir el siguiente criterio: solo se pide el
                    // c�digo del empleado y se buscar� solo ese registro en el
                    // fichero
                    // 2-We check the correctness of each field in the RAM record
                    if (code < 1 || code > 9999) {
                        System.err.println("The code is out of range [1-9999]: " + employeeRAMrecord.name);
                        // �*! We should ensure the unicity of each employee's code,
                        // for example: another file with not-used codes
                        break;
                    }

                    // 3-We search the employee record from file to RAM, processing
                    // the records from the first up to the target record (or the
                    // end of the field if no record match)
                    employeeRAMrecord = new EmployeeRAMRecord();

                    try {
                        // 3a-We create the 'FileReader', and therefore, we 'open' the file
                        employeeFileReader = new FileReader(employeeFile);

                        File employeeFileTmp = new File(employeeFile.getAbsolutePath() + ".tmp");

                        //   We create the 'FileWriter', and therefore, we 'open' the file
                        /*----->*/ employeeFileWriter = new FileWriter(employeeFileTmp, true);//the second argument 'true' allows append the new record at the end of the file
                        //(if it's false or is not present, the content of the file is erased

                        String RAMfield;
                        int character;

                        boolean found = false;
                        while ((character = employeeFileReader.read()) != -1) {
                            // 3b-We read from the file to the RAM through the 'FileRead' object
                            RAMfield = "";
                            // character=employeeFileReader.read(); Ya se ha llamado a 'read()' en la condici�n del bucle while
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.code = Integer.decode(RAMfield); // Convertimos la cadena 'RAMfield' en un entero

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString()
                                    .equals(EmployeeSM.EOFIELD)) { // �character ==
                                // 'fin de
                                // campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.name = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString()
                                    .equals(EmployeeSM.EOFIELD)) { // �character ==
                                // 'fin de
                                // campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.surname = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString()
                                    .equals(EmployeeSM.EOFIELD)) { // �character ==
                                // 'fin de
                                // campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.birthDate = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString()
                                    .equals(EmployeeSM.EOFIELD)) { // �character ==
                                // 'fin de
                                // campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.salary = Float.parseFloat(RAMfield);

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString()
                                    .equals(EmployeeSM.EOFIELD)) { // �character ==
                                // 'fin de
                                // campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.carId = RAMfield;

                            // Para leer el fin de registro, que pueden ser mas de un byte
                            character = employeeFileReader.read();
                            String finRegistro = new Character((char) character).toString();
                            while (!finRegistro.toString().equals(EmployeeSM.EORECORD)) { // �character == 'fin de registro' ?
                                character = employeeFileReader.read();
                                finRegistro = finRegistro + (char) character;
                            }

                            //If the record is the target record, it is not copied to temporary file
                            if (employeeRAMrecord.code != code) {

                                //3b-We write to the file through the 'FileWrite' object 
                                employeeFileWriter.append(new Integer(employeeRAMrecord.code).toString());
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(employeeRAMrecord.name);
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(employeeRAMrecord.surname);
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(employeeRAMrecord.birthDate);
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(new Float(employeeRAMrecord.salary).toString());
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(employeeRAMrecord.carId);
                                employeeFileWriter.append(EmployeeSM.EOFIELD);
                                employeeFileWriter.append(EmployeeSM.EORECORD);
                            } else {
                                found = true;
                            }
                        }
                        // 3c-We close the 'FileWriter' and 'FileReader' objects, and therefore, we 'close' the files
                        employeeFileReader.close();
                        employeeFileWriter.close();

                        /*------>*/ boolean b = employeeFile.delete();
                        /*------>*/ boolean b2 = employeeFileTmp.renameTo(employeeFile);

                        if (!found) {
                            System.err.printf("Employee record with code %s not found\n", code);
                        }

                    } catch (IOException ioe) {
                        System.err.println("An I/O error occurs and no employee was deleted");
                    }
                }
                break;

                case change: //pedir datos, y seguir leer registros del fichero y copiarlos todos al fichero temporal, excepto el modificado, que se copia el nuevo
                    break;

                case show: {
                    System.out.println("We search starting from the first record of the sequential access file");

                    //1-We ask the user as many fields as fields should match in the file
                    employeeRAMrecord = new EmployeeRAMRecord();

                    System.out.println("Write the employee code (as an positive integer between 1 and 9999)");
                    int code = scanner.nextInt();
                    scanner.nextLine(); //We consume '\n'

                    //�*!Se podr�a crear un registro con aquellos campos que deben coincidir con el/los buscado(s)
                    //Aqu� vamos a seguir el siguiente criterio: solo se pide el c�digo del empleado y se buscar� solo ese registro en el fichero
                    //2-We check the correctness of each field in the RAM record
                    if (code < 1 || code > 9999) {
                        System.err.println("The code is out of range [1-9999]: " + employeeRAMrecord.name);
                        //�*! We should ensure the unicity of each employee's code, for example: another file with not-used codes
                        break;
                    }
                    //3-We search the employee record from file to RAM, processing the records from the first up to the target record (or the end of the field
                    //  if no record match)

                    employeeRAMrecord = new EmployeeRAMRecord();

                    try {
                        //3a-We create the 'FileReader', and therefore, we 'open' the file
                        employeeFileReader = new FileReader(employeeFile);
                        String RAMfield;
                        int character;

                        boolean found = false;
                        while ((!found && (character = employeeFileReader.read()) != -1)) {
                            //3b-We read from the file to the RAM through the 'FileRead' object 
                            RAMfield = "";
                            //character=employeeFileReader.read(); Ya se ha llamado a 'read()' en la condici�n del bucle while
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.code = Integer.decode(RAMfield);  //Convertimos la cadena 'RAMfield' en un entero

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.name = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.surname = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.birthDate = RAMfield;

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.salary = Float.parseFloat(RAMfield);

                            RAMfield = "";
                            character = employeeFileReader.read();
                            while (!new Character((char) character).toString().equals(EmployeeSM.EOFIELD)) { // �character == 'fin de campo' ?
                                RAMfield = RAMfield + (char) character;
                                character = employeeFileReader.read();
                            }
                            employeeRAMrecord.carId = RAMfield;

                            //Para leer el fin de registro, que pueden ser mas de un byte
                            character = employeeFileReader.read();
                            String finRegistro = new Character((char) character).toString();
                            while (!finRegistro.toString().equals(EmployeeSM.EORECORD)) { // �character == 'fin de registro' ?
                                character = employeeFileReader.read();
                                finRegistro = finRegistro + (char) character;
                            }

                            if (employeeRAMrecord.code == code) {
                                employeeRAMrecord.show();
                                found = true; //Una vez encontrado el registro, acaba la b�squeda, saliendo del bucle
                            }
                        }
                        //3c-We close the 'FileWriter' object, and therefore, we 'close' the file
                        employeeFileReader.close();

                        if (!found) {
                            System.err.printf("Employee record with code %s not found\n", code);
                        }

                    } catch (IOException ioe) {
                        System.err.println("An I/O error occurs and no employee was added");
                    }
                }
                break;

                case close: //Nota: En Java se 'cierra' un archivo cuando se cierra el flujo hacia ese archivo (FileWriter.close(), etc)
                    break;

                case end:
                    break;

            }

        }
    }

}
