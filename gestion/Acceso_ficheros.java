package gestion;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import listas.Lista_tareas;

import tareas.Tarea;
import tareas.Tarea_con_plazo;
import tareas.Tarea_simple;
import usuarios.Grupo_usuarios;
import usuarios.Propietario;
import usuarios.Usuario;

/**
 * Clase que contiene m�todos relativos a la Entrada/Salida por medio de ficheros.
 * Estos ficheros est�n especificados por sus atributos, los cuales representan los nombres de los ficheros que se usar�n.
 * Los nombres de ficheros usados en el programa son, por defecto: <b>tareas.txt</b>, <b>grupos.txt</b>, y por �ltimo,
 * un archivo adicional adem�s de los indicados en la especificaci�n del proyecto, <b>usuarios.txt</b>.<br>
 * La sintaxis utilizada a la hora de leer los archivos, se considera correcta. Es la misma que se detalla
 * en la especificaci�n del proyecto, y en el caso del fichero adicional que detalla los usuarios, su sintaxis
 * se explica en la documentaci�n del m�todo {@link #leerUsuarios()}.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Acceso_ficheros {

	/**
	 * Nombre del fichero a partir del cual se leer�n los usuarios.
	 */
	private String usuarios;
	
	/**
	 * Nombre del fichero a partir del cual se leer�n los grupos.
	 */
	private String grupos;
	
	/**
	 * Nombre del fichero a partir del cual se leer�n las tareas.
	 */
	private String tareas;
	
	/**
	 * Constructor de un objeto Acceso_ficheros seg�n los nombres de los ficheros a los que acceder�.
	 * @param usuarios Fichero de usuarios.
	 * @param grupos Fichero de grupos.
	 * @param tareas Fichero de tareas.
	 */
	public Acceso_ficheros(String usuarios, String grupos, String tareas){
		this.usuarios = usuarios;
		this.grupos = grupos;
		this.tareas = tareas;
	}
	
	//M�TODOS DE LECTURA DE FICHEROS
	
	/**
	 * M�todo que lee una lista de Tareas del archivo especificado por {@link tareas}.
	 * El convenio mediante el cual se leen es el explicado en la especificaci�n del proyecto.
	 * @param datos Los datos que se usan en el programa.
	 * @return La lista de tareas le�das del fichero.
	 * @throws Exception Si no se encuentra el fichero especificado.
	 */
	public LinkedList<Tarea> leerTareas(Acceso_datos datos) throws Exception{
		LinkedList<Tarea> lista = new LinkedList<Tarea>();
		File fich = new File(tareas);
		Scanner entrada = new Scanner(fich);
		String tipo, descripcion, propietario, fecha = "", estado;
		int ID, prioridad;
		ArrayList<Integer> dependencias;
		boolean pendiente;
		Propietario p;
			
		while(entrada.hasNext()){
			tipo = entrada.nextLine();
			tipo = tipo.equals("*") ? entrada.nextLine() : tipo;
			ID = entrada.nextInt();
			entrada.nextLine();
			descripcion = entrada.nextLine();
			propietario = entrada.nextLine();
			p = null;
			try{
				p = datos.getGrupo(propietario);
			}
			catch(RuntimeException e){
				try{
					p = datos.getUsuario(propietario);
				}
				catch(RuntimeException ex){
					System.err.println(ex.getMessage());
					System.exit(1);
				}
			}
			prioridad = entrada.nextInt();
			entrada.nextLine();
			estado = entrada.nextLine();
			pendiente = (estado.equals("pendiente")) ? true : false;
			if(!entrada.hasNext("-")){
				fecha = entrada.nextLine();
			}
			else{
				entrada.nextLine();
			}
			dependencias = datos.leerDependencias(entrada.nextLine());
			if(tipo.equals("simple")){
				Tarea_simple aux = new Tarea_simple(descripcion, p, ID, pendiente, dependencias, prioridad);
				lista.add(aux);
			}
			else if(tipo.equals("con_plazo")){
				lista.add(new Tarea_con_plazo(descripcion, p, ID, pendiente, dependencias, prioridad, fecha));
			}
		}
		
		return lista;
	}
	
	/**
	 * M�todo que lee los usuarios de un fichero de texto. Lee el nombre que los identifica, as� como sus respectivos emails
	 * con los que podr�n enviar y recibir tareas. La sintaxis del archivo a partir del cual se leen se considera correcta.
	 * Se escribir�n los usuarios de la siguiente forma:
	 * <blockquote>nombre_usuario: nombre_email@dominio.com</blockquote>
	 * <br><b><i>EJEMPLO</i></b>
	 * <blockquote>juan_perez: juan@gmail.com<br>
	 * ana_gomez: ana@gmail.com<br>
	 * pedro_pazos: pedro@gmail.com</blockquote>
	 * @return Una lista de Usuarios.
	 * @throws Exception Si no se encuentra el fichero especificado.
	 */
	public LinkedList<Usuario> leerUsuarios() throws Exception{
		LinkedList<Usuario> usuarios = new LinkedList<Usuario>();
		File fich = new File(this.usuarios);
		Scanner entrada = new Scanner(fich);
		String cadena;
		while(entrada.hasNext()){
			cadena = entrada.nextLine();
			String [] u = cadena.split(":\\s*");
			Usuario aux = new Usuario(u[0]);
			aux.setEmail(u[1]);
			usuarios.add(aux);
		}
		return usuarios;
	}
	
	/**
	 * M�todo que lee los grupos a partir de un fichero de texto, con la sintaxis detallada en la especificaci�n del proyecto.
	 * Si se encuentra un usuario cuyo nombre termine en las etiquetas &lt;T&gt; o &lt;I&gt;, los a�ade al grupo pero no
	 * a�ade el grupo en cuesti�n a la lista de grupos del usuario, en espera de que se acepte la solicitud o la invitaci�n, respectivamente.
	 * @param datos Los datos que se usan en el programa.
	 * @return Una lista de Grupos de usuario.
	 * @throws Exception Si no existe el fichero especificado.
	 */
	public LinkedList<Grupo_usuarios> leerGrupos(Acceso_datos datos) throws Exception{
		LinkedList<Grupo_usuarios> grupos = new LinkedList<Grupo_usuarios>();
		File fich = new File(this.grupos);
		Scanner entrada = new Scanner(fich);
		String cadena;
		while(entrada.hasNext()){
			entrada.useDelimiter("\\s*:\\s*");
			Grupo_usuarios aux = new Grupo_usuarios(entrada.next());
			entrada.skip("\\s*:\\s*");
			cadena = entrada.nextLine();
			String [] usuarios = cadena.split("\\s*,\\s*");
			for(String u : usuarios){
				Usuario user;
				if(u.endsWith("<T>")){
					String nombre = u.replace("<T>", "");
					user = (Usuario) datos.getUsuario(nombre).clone();
					user.setNombre(user.getNombre()+"<T>");
				}
				else if(u.endsWith("<I>")){
					String nombre = u.replace("<I>", "");
					user = (Usuario) datos.getUsuario(nombre).clone();
					user.setNombre(user.getNombre()+"<I>");
				}
				else{
					user = datos.getUsuario(u);
					user.addGrupo(aux);
				}
				aux.addUsuario(user);
			}
			grupos.add(aux);
		}
		return grupos;
	}
	
	/**
	 * M�todo que permite obtener un objeto Acceso_datos generado a partir de los datos le�dos de los ficheros.
	 * @return Un objeto Acceso_datos con todos los datos que se usar�n en el programa.
	 * @throws Exception Si hay alg�n error a la hora de leer los ficheros.
	 */
	public Acceso_datos getDatos() throws Exception{
		LinkedList<Usuario> users = leerUsuarios();
		Acceso_datos datos = new Acceso_datos(users);
		
		LinkedList<Grupo_usuarios> grupos = null;
		try{
			grupos = leerGrupos(datos);
		}
		catch(RuntimeException e){
			System.err.println("Alg�n usuario contenido en el fichero de grupos no existe en el fichero de usuarios.");
			System.exit(1);
		}
		datos.setGrupos(grupos);
		
		Lista_tareas l = null;
		try{
			l = new Lista_tareas(leerTareas(datos));
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
		datos.setLista(l);
		
		return datos;
	}
	
	//M�TODOS DE ESCRITURA EN FICHEROS
	
	/**
	 * M�todo que escribe una lista de tareas en el archivo especificado por {@link #tareas} siguiendo el convenio explicado en la especificaci�n del proyecto.
	 * @param datos Los datos que se usan en el programa.
	 * @throws Exception Si no se puede almacenar la informaci�n en el fichero.
	 */
	public void escribirTareas(Acceso_datos datos) throws Exception{
		File fichero = new File(tareas);
		if(!fichero.canWrite()){
			throw new Exception("Error 9: No se pudo almacenar la informaci�n en el fichero.");
		}
		PrintWriter salida = new PrintWriter (fichero);
		
		LinkedList<Tarea> lista = datos.getTareas();
		
		for(Iterator<Tarea> i = lista.iterator(); i.hasNext();){
			salida.println(i.next().toStringTXT());
			if(i.hasNext())
				salida.println("*");
		}
		
		salida.close();
	}
	
	/**
	 * M�todo que escribe los grupos de usuarios en el archivo especificado por {@link #grupos} siguiendo el convenio que se muestra en la especificaci�n.
	 * @param datos Los datos que se usan en el programa.
	 * @throws Exception Si hay alg�n tipo de error al escribir en el fichero.
	 */
	public void escribirGrupos(Acceso_datos datos) throws Exception{
		File fichero = new File(grupos);
		if(!fichero.canWrite()){
			throw new Exception("Error 9: No se pudo almacenar la informaci�n en el fichero.");
		}
		PrintWriter salida = new PrintWriter(fichero);
		
		LinkedList<Grupo_usuarios> lista_grupos = datos.getGrupos();
		for(Grupo_usuarios g : lista_grupos){
			salida.println(g.toStringTXT());
		}
		
		salida.close();
	}
	
	/**
	 * M�todo que escribe una lista de usuarios en el archivo especificado por {@link #usuarios}.
	 * El convenio que se sigue est� detallado en la descripci�n de leerUsuarios().
	 * @see #leerUsuarios()
	 * @param datos Los datos que se usan en el programa.
	 * @throws Exception
	 */
	public void escribirUsuarios(Acceso_datos datos) throws Exception{
		File fichero = new File(usuarios);
		if(!fichero.canWrite()){
			throw new Exception("Error 9: No se pudo almacenar la informaci�n en el fichero.");
		}
		PrintWriter salida = new PrintWriter(fichero);
		
		LinkedList<Usuario> users = datos.getUsers();
		
		for(Usuario u : users){
			salida.println(u.toStringTXT());
		}
		
		salida.close();
	}
	
	//M�TODO DE CAMBIOS
	
	/**
	 * M�todo que permite saber si hay cambios entre los datos que se obtienen a partir de los ficheros, y
	 * los datos que se han usado en la ejecuci�n del programa.
	 * @param datos Los datos con los que ha trabajado el programa.
	 * @return true si hay cambios, false si no los hay.
	 * @throws Exception Si alguno de los ficheros no existe.
	 */
	public boolean hayCambios(Acceso_datos datos) throws Exception{
		Acceso_datos datos_originales = getDatos();
		return !datos_originales.equals(datos);
	}
}
