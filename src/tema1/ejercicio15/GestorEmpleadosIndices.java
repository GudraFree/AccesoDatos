/*
Índices: por código empleado y nombre-apellido

Menú
1- Alta empleado
2- Baja
3- Modificación
4- Consulta

Empleado: int id, string nombre, string apellidos, string departamento


*/
package tema1.ejercicio15;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Perig
 */
public class GestorEmpleadosIndices {
    static final byte ACTIVO=1, BORRADO=-1;
    static File ficheroEmpleados, indexId, indexNombre, gestorEmpleados, tmp;
    static RandomAccessFile raf;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    static TreeMap<Integer,Long> mapId;
    static TreeMap<String,ArrayList<Long>> mapNombre;
    static Scanner sc;
    public static void main(String[] args) {
        // inicializamos todo lo necesario
        gestorEmpleados = new File("gestorEmpleados");
        ficheroEmpleados=new File(gestorEmpleados,"empleadosRAF.dat");
        indexId = new File(gestorEmpleados,"indexId");
        indexNombre = new File(gestorEmpleados,"indexNombre");
        int opcion;
        while((opcion=menu())!=6) {
            switch(opcion) {
                case 1: // Alta
                    alta();
                    break;
                case 2: // Baja
                    baja();
                    break;
                case 3: // Modificación
                    modif();
                    break;
                case 4: // Consulta
                    consulta();
                    break;
                case 5: // Reconstruir fichero
                    reconstr();
                    break;
            }
        }
        
    }
    
    static int menu() {
        sc = new Scanner(System.in);
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\nGestor de empleados con índices. Por favor introduzca una opción");
            System.out.println("\t1. Alta");
            System.out.println("\t2. Baja");
            System.out.println("\t3. Modificación");
            System.out.println("\t4. Consulta");
            System.out.println("\t5. Reconstruir fichero");
            System.out.println("\t6. Salir");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 6;
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
    
    static int menuConsulta() {
        int opcion = 0; //valor 0 por defecto
        boolean opcionInvalida = true; // valor true por defecto, necesario para segunda iteración bucle si salta excepción
        do {
            System.out.println("\n¿Según qué campo quiere consultar?");
            System.out.println("\t1. ID de empleado");
            System.out.println("\t2. Apellidos, Nombre");
            System.out.println("\t3. Listar todos los empleados");
            try {
                opcion = sc.nextInt();
                opcionInvalida = opcion < 1 || opcion > 3;
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
    
    static void alta() {
        //obtenemos el ID del alta que vamos a realizar. Debe ser el del último empleado introducido incrementado en uno. Si es la primera alta, es 0
        int id=0;
        if (indexId.exists()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(indexId));
                id = (Integer)(((TreeMap)ois.readObject()).lastKey());
                id++;
                ois.close();
            } catch (IOException e) {
                System.out.println("Error de captura del ID");
            } catch (ClassNotFoundException e) {
                System.out.println("Error, objeto no es de clase TreeMap");
            }
        }
        
        
        //crea los ficheros si no existen
        try { 
            if(!gestorEmpleados.isDirectory()) gestorEmpleados.mkdir();
            if(!ficheroEmpleados.exists()) ficheroEmpleados.createNewFile();
            if(!indexId.exists()) {
                indexId.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(indexId));
                oos.close();
            }
            if(!indexNombre.exists()) {
                indexNombre.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream(indexNombre));
                oos.close();
            }
        } catch (IOException e) {
            System.out.println("Error en la creación de ficheros");
        }
        
        //creamos el objeto empleado y pedimos sus datos
        Empleado nuevoEmpleado = Empleado.pedirEmpleado(ACTIVO, id);
        
        try {
            //creo el RAF y escribo en el fichero, obteniendo primero la posición antes de escribir
            raf = new RandomAccessFile(ficheroEmpleados,"rw");
            raf.seek(ficheroEmpleados.length());
            long pointerPos = raf.getFilePointer();
            raf.writeByte(nuevoEmpleado.control);
            raf.writeInt(nuevoEmpleado.id);
            raf.writeUTF(nuevoEmpleado.nombre);
            raf.writeUTF(nuevoEmpleado.apellidos);
            raf.writeUTF(nuevoEmpleado.departamento);
            raf.close();
            
            // Operaciones del índice por ID
            // añadimos una clave al treemap, que leemos del archivo índice si se ha escrito previamente.
            ois = new ObjectInputStream(new FileInputStream(indexId)); 
            if(ois.available()>0) {
                mapId = (TreeMap)ois.readObject();
            } else mapId = new TreeMap();
            ois.close();
            mapId.put(nuevoEmpleado.id,pointerPos);
            
            //escribimos el Treemap en el archivo índice 
            oos = new ObjectOutputStream(new FileOutputStream(indexId, false)); //append en false, que sobreescriba
            oos.writeObject(mapId);
            oos.close();
            
            // Operaciones del índice por nombre y apellidos
            // añadimos una clave al treemap, que leemos del archivo índice si se ha escrito previamente.
            ois = new ObjectInputStream(new FileInputStream(indexNombre));
            if(ois.available()>0) {
                mapNombre = (TreeMap)ois.readObject();
            } else mapNombre = new TreeMap();
            ArrayList<Long> posicionesPorNombre = mapNombre.get(nuevoEmpleado.apellidos+" "+nuevoEmpleado.nombre);
            boolean nombreNuevo = posicionesPorNombre==null;
            if(nombreNuevo) posicionesPorNombre = new ArrayList();
            posicionesPorNombre.add(pointerPos);
            mapNombre.put(nuevoEmpleado.apellidos+" "+nuevoEmpleado.nombre,posicionesPorNombre);
            ois.close();
            
            //escribimos el Treemap en el archivo índice 
            oos = new ObjectOutputStream(new FileOutputStream(indexNombre, false)); //append en false, que sobreescriba
            oos.writeObject(mapNombre);
            oos.close();
            
        } catch(FileNotFoundException e) {
            System.out.println("Error, fichero no encontrado");
        } catch (ClassNotFoundException e) {
            System.out.println("Error, objeto no es de clase TreeMap");
        } catch (IOException e) {
            System.out.println("Error de E/S");
            e.printStackTrace();
        } 
    } // fin alta()
    
    static void baja() {
        //controlamos que hay al menos un empleado al que realizar la baja (el fichero se crea al realizar un alta)
        if(!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una baja");
            return;
        }
        
        //obtenemos el treemap que contiene el índice
        try {
            ois = new ObjectInputStream(new FileInputStream(indexId));
            mapId = (TreeMap)ois.readObject();
            ois.close();
        } catch (IOException e) {
            System.out.println("Error de E/S");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error, objeto no es de clase TreeMap");
        }

        //pedimos el índice que queremos buscar
        System.out.println("Introduzca el ID del empleado a dar de baja");
        int idABuscar = sc.nextInt();
        sc.nextLine(); //limpiamos scanner
        
        Iterator it = mapId.entrySet().iterator();
        int keyRemove=-1;
        boolean encontrado = false;
                
        while(it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            if((Integer)e.getKey()==idABuscar) {
                encontrado=true;
                try {
                    raf = new RandomAccessFile(ficheroEmpleados,"rw");
                    raf.seek((Long)e.getValue());
                    if(raf.readByte()!=BORRADO) {
                        raf.seek((Long)e.getValue());
                        raf.writeByte(BORRADO);
                        keyRemove = (Integer)e.getKey();
                    } else System.out.println("Error, el empleado #"+idABuscar+" no está dado de alta");
                    raf.close();
                } catch(FileNotFoundException ex) {
                    System.out.println("Error, fichero no encontrado");
                } catch (IOException ex) {
                    System.out.println("Error de E/S");
                }
            }
        }
        
        if(keyRemove!=-1) {
            mapId.remove(keyRemove);
            System.out.println("Baja realizada con éxito");
        }
        if(!encontrado) System.out.println("Error, ID introducido no es válido");
        
    } //fin baja
    
    static void modif() {
        //controlamos que hay al menos un empleado al que realizar la baja (el fichero se crea al realizar un alta)
        if(!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una modificación");
            return;
        }
        
        //obtenemos el treemap que contiene el índice
        try {
            ois = new ObjectInputStream(new FileInputStream(indexId));
            mapId = (TreeMap)ois.readObject();
            ois.close();
        } catch (IOException e) {
            System.out.println("Error de E/S");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error, objeto no es de clase TreeMap");
        }

        //pedimos el índice que queremos buscar
        System.out.println("Introduzca el ID del empleado a modificar");
        int idABuscar = sc.nextInt();
        sc.nextLine(); //limpiamos scanner
        
        Iterator it = mapId.entrySet().iterator();
        boolean encontrado = false;
                
        while(it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            if((Integer)e.getKey()==idABuscar) {
                
                
                try {
                    Empleado emp = null;
                    raf = new RandomAccessFile(ficheroEmpleados,"rw");
                    raf.seek((Long)e.getValue());
                    if(raf.readByte()!=BORRADO) {
                        //creamos el objeto empleado y pedimos sus datos
                        emp = Empleado.pedirEmpleado(ACTIVO, idABuscar);
                        
                        raf.seek((Long)e.getValue());
                        raf.writeByte(ACTIVO);
                        raf.writeInt(emp.id);
                        raf.writeUTF(emp.nombre);
                        raf.writeUTF(emp.apellidos);
                        raf.writeUTF(emp.departamento);
                        System.out.println("Modificación realizada con éxito");
                    } else System.out.println("Error, el empleado #"+idABuscar+" no está dado de alta");
                    raf.close();
                    encontrado = true;
                } catch(FileNotFoundException ex) {
                    System.out.println("Error, fichero no encontrado");
                } catch (IOException ex) {
                    System.out.println("Error de E/S");
                }
            }
        }
        
        if(!encontrado) System.out.println("Error, ID introducido no es válido");
        
    } //fin modif
    
    static void consulta() {
        //controlamos que hay al menos un empleado al que realizar la consulta (el fichero se crea al realizar un alta)
        if(!ficheroEmpleados.exists()) {
            System.out.println("Error, debe realizar un alta antes de realizar una consulta");
            return;
        }
        
        Iterator it;
        
        switch(menuConsulta()) {
            case 1: // consulta por ID
                
                //obtenemos el treemap que contiene el índice
                try {
                    ois = new ObjectInputStream(new FileInputStream(indexId));
                    mapId = (TreeMap)ois.readObject();
                    ois.close();
                } catch (IOException e) {
                    System.out.println("Error de E/S");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("Error, objeto no es de clase TreeMap");
                }
                
                //pedimos el índice que queremos buscar
                System.out.println("Introduzca el ID del empleado a consultar");
                int idABuscar = sc.nextInt();
                sc.nextLine(); //limpiamos scanner
                
                //recorremos todo el mapa para encontrar el ID que estamos buscando y su posición en el fichero
                long posicion = -1;
                
                it = mapId.entrySet().iterator();
                
                while(it.hasNext()) {
                    Map.Entry e = (Map.Entry)it.next();
                    if((Integer)e.getKey()==idABuscar) {
                        posicion = (Long)e.getValue();
                    }
                }
                
                //comprobamos si ha encontrado el id. Si no, posicion sigue valiendo el valor con el que se inicializó (-1) y sale del método
                if(posicion==-1) {
                    System.out.println("Error, ID introducido no válido\n");
                    return;
                }
                
                //obtenemos el empleado en la posición adecuada
                Empleado empleado = new Empleado();
                try {
                    raf = new RandomAccessFile(ficheroEmpleados, "r");
                    raf.seek(posicion);
                    empleado.control = raf.readByte();
                    empleado.id = raf.readInt();
                    empleado.nombre = raf.readUTF();
                    empleado.apellidos = raf.readUTF();
                    empleado.departamento = raf.readUTF();
                    raf.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Error, fichero no encontrado");
                } catch (IOException e) {
                    System.out.println("Error de E/S");
                }
                
                //mostramos el empleado leído
                if(empleado.control==ACTIVO) empleado.mostrarEmpleado();
                else System.out.println("Error, el empleado #"+empleado.id+" no está dado de alta");
                
                break;
            case 2: // consulta por apellidos, nombre
                //obtenemos el treemap que contiene el índice
                try {
                    ois = new ObjectInputStream(new FileInputStream(indexNombre));
                    mapNombre = (TreeMap)ois.readObject();
                    ois.close();
                } catch (IOException e) {
                    System.out.println("Error de E/S");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("Error, objeto no es de clase TreeMap");
                }
                
                //pedimos el índice que queremos buscar
                System.out.println("Introduzca el nombre del empleado a consultar");
                String nombre = sc.nextLine();
                System.out.println("Introduzca los apellidos del empleado a consultar");
                String apellidos = sc.nextLine();
                
                //recorremos todo el mapa para encontrar el ID que estamos buscando y su posición en el fichero
                ArrayList<Long> posiciones = new ArrayList();
                
                it = mapNombre.entrySet().iterator(); 
                
                while(it.hasNext()) { //iteramos todas las entradas
                    Map.Entry e = (Map.Entry)it.next();
                    if(((String)e.getKey()).equals(apellidos+" "+nombre)) { 
                        // iteramos el array de posiciones que está en el campo value del entry
                        for (Long p : (ArrayList<Long>)e.getValue()) posiciones.add(p);
                    }
                }
                
                //comprobamos si ha encontrado coincidencias. Si no, sale del método
                if(posiciones.isEmpty()) {
                    System.out.println("Error, no hay empleados con ese nombre y apellidos\n");
                    return;
                }
                
                //obtenemos los empleados en las posiciones obtenidas
                for(Long posi : posiciones) {
                    Empleado emp = new Empleado();
                    try {
                        raf = new RandomAccessFile(ficheroEmpleados, "r");
                        raf.seek(posi);
                        emp.control = raf.readByte();
                        emp.id = raf.readInt();
                        emp.nombre = raf.readUTF();
                        emp.apellidos = raf.readUTF();
                        emp.departamento = raf.readUTF();
                        raf.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Error, fichero no encontrado");
                    } catch (IOException e) {
                        System.out.println("Error de E/S");
                    }

                    //mostramos el empleado leído
                    if(emp.control==ACTIVO) emp.mostrarEmpleado();
                }

                break;
                
            case 3: // listar todos los empleados
                try {
                    //leemos el índice de ids
                    ois = new ObjectInputStream(new FileInputStream(indexId));
                    mapId = (TreeMap)ois.readObject();
                    ois.close();
                    
                    //abrimos el flujo del RAF
                    raf = new RandomAccessFile(ficheroEmpleados,"r");
                    
                    //obtenemos el iterador del índice de ids, y lo recorremos
                    it = mapId.entrySet().iterator();
                    while(it.hasNext()) {
                        Map.Entry e = (Map.Entry)it.next();
                        //por cada entrada del índice, creamos un empleado cuyo id es la key y cuya info está en la posición guardada como value, y lo mostramos
                        Empleado emp = new Empleado(); 
                        raf.seek((Long)e.getValue());
                        emp.control = raf.readByte();
                        emp.id = raf.readInt();
                        emp.nombre = raf.readUTF();
                        emp.apellidos = raf.readUTF();
                        emp.departamento = raf.readUTF();
                        
                        if(emp.control==ACTIVO) emp.mostrarEmpleado();
                    }
                    
                } catch (FileNotFoundException e) {
                    System.out.println("Error, fichero no encontrado");
                } catch (IOException e) {
                    System.out.println("Error de E/S");
                } catch (ClassNotFoundException e) {
                    System.out.println("Error, el objeto no es un TreeMap");
                }
                break;
        }
    } // fin consulta
    
    static void reconstr() {
        tmp = new File(gestorEmpleados,"tmp");
        ArrayList<Empleado> empleados = new ArrayList();
        try {
            raf = new RandomAccessFile(ficheroEmpleados,"r");
            while(raf.getFilePointer()<ficheroEmpleados.length()) {
                Empleado emp = new Empleado();
                emp.control = raf.readByte();
                emp.id = raf.readInt();
                emp.nombre = raf.readUTF();
                emp.apellidos = raf.readUTF();
                emp.departamento = raf.readUTF();
                empleados.add(emp);
            }
            raf.close();
            
            //si ya existe un fichero temporal lo borro para crearlo en blanco de nuevo
            if(tmp.exists()) tmp.delete();
            tmp.createNewFile();
            
            //creo los nuevos índices
            mapId = new TreeMap();
            mapNombre = new TreeMap();
            
            //abro el flujo y escribo cada empleado de la lista
            raf = new RandomAccessFile(tmp,"rw");
            for(Empleado emp : empleados) {
                long pointerPos = raf.getFilePointer();
                raf.writeByte(emp.control);
                raf.writeInt(emp.id);
                raf.writeUTF(emp.nombre);
                raf.writeUTF(emp.apellidos);
                raf.writeUTF(emp.departamento);
                
                // Operaciones del índice por ID
                // añadimos una clave al treemap
                mapId.put(emp.id,pointerPos);

                // Operaciones del índice por nombre y apellidos
                // añadimos una clave al treemap
                ArrayList<Long> posicionesPorNombre = mapNombre.get(emp.apellidos+" "+emp.nombre);
                boolean nombreNuevo = posicionesPorNombre==null;
                if(nombreNuevo) posicionesPorNombre = new ArrayList();
                posicionesPorNombre.add(pointerPos);
                mapNombre.put(emp.apellidos+" "+emp.nombre,posicionesPorNombre);
            }

            //escribimos el Treemap en el archivo índice 
            oos = new ObjectOutputStream(new FileOutputStream(indexId, false)); //append en false, que sobreescriba
            oos.writeObject(mapId);
            oos.close();

            //escribimos el Treemap en el archivo índice 
            oos = new ObjectOutputStream(new FileOutputStream(indexNombre, false)); //append en false, que sobreescriba
            oos.writeObject(mapNombre);
            oos.close();
            
            //cerramos flujo del RAF
            raf.close();
            
            
            
            tmp.renameTo(ficheroEmpleados);
            tmp.delete();
        } catch (FileNotFoundException e) {
            System.out.println("Error, fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Error de E/S");
        }
    }
    
}
