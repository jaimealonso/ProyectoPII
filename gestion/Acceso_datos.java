package gestion;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

import listas.Lista_tareas;

import tareas.Generar_ID;
import tareas.Tarea;
import tareas.Tarea_con_plazo;
import usuarios.Grupo_usuarios;
import usuarios.Propietario;
import usuarios.Usuario;

/**
 * Clase que encapsula los datos con los que se trabaja en la ejecuci�n del programa.
 * Adem�s, contiene m�todos para manipularlos desde fuera.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Acceso_datos {

	/**
	 * Lista de todas las tareas disponibles.
	 */
	private Lista_tareas l;
	
	/**
	 * Lista de todos los usuarios.
	 */
	private LinkedList<Usuario> users;
	
	/**
	 * Lista de todos los grupos disponibles.
	 */
	private LinkedList<Grupo_usuarios> grupos;
	
	/**
	 * Usuario que se usa en la ejecuci�n del programa.
	 */
	private Usuario u;
	
	/**
	 * Mecanismo de entrada de datos por teclado.
	 */
	private Scanner teclado = new Scanner(System.in);
	
	//CONSTRUCTOR DE LA CLASE
	
	/**
	 * Constructor de un objeto Acceso_datos.
	 * Como se ve, su �nico par�metro es la lista de usuarios, ya que para obtener los dem�s atributos del objeto se depende
	 * de la propia lista de usuarios.
	 * @param users Lista de usuarios disponibles.
	 */
	protected Acceso_datos(LinkedList<Usuario> users){
		this.users = users;
	}
	
	//M�TODOS GETTERS Y SETTERS DE LA CLASE
	
	/**
	 * M�todo que permite establecer el usuario que se utiliza.
	 * Si es un usuario nuevo, lo a�ade a la lista de usuarios (<i>para luego poder guardarlo en su
	 * fichero correspondiente</i>).
	 * @param u Usuario actual.
	 * @param nuevo Variable que indica si es un nuevo usuario o no.
	 */
	public void setUsuario(Usuario u, boolean nuevo){
		if(nuevo){
			users.add(u);
		}
		this.u = u;
	}
	
	/**
	 * M�todo que devuelve el usuario con el que se est� trabajando.
	 * @return Usuario actual.
	 */
	public Usuario getUsuarioActual(){
		return u;
	}
	
	/**
	 * M�todo que permite establecer la lista de grupos disponibles.
	 * @param grupos Lista de grupos.
	 */
	public void setGrupos(LinkedList<Grupo_usuarios> grupos){
		this.grupos = grupos;
	}
	
	/**
	 * M�todo que permite establecer la lista de todas las tareas.
	 * @param l Lista de tareas.
	 */
	public void setLista(Lista_tareas l){
		this.l = l;
	}
	
	/**
	 * M�todo que devuelve un generador de IDs basado en la lista de tareas.
	 * @return Un Generador de IDs �nicas.
	 */
	public Generar_ID getGenerador(){
		return new Generar_ID(l);
	}
	
	/**
	 * M�todo que devuelve la lista completa de tareas.
	 * @return Una lista que contiene todas las tareas.
	 */
	public LinkedList<Tarea> getTareas(){
		return l.getListaCompleta();
	}
	
	/**
	 * M�todo que devuelve la lista de todos los usuarios disponibles.
	 * @return Una lista con todos los usuarios.
	 */
	public LinkedList<Usuario> getUsers(){
		return users;
	}
	
	/**
	 * M�todo que devuelve la lista de todos los grupos.
	 * @return Una lista con todos los grupos.
	 */
	public LinkedList<Grupo_usuarios> getGrupos(){
		return grupos;
	}
	
	/**
	 * M�todo que permite obtener un {@link usuarios.Usuario Usuario} a partir de su nombre.
	 * @param usuario Nombre del usuario que se desea obtener.
	 * @return El Usuario cuyo nombre es igual al especificado.
	 * @throws RuntimeException Si no existe el usuario especificado.
	 */
	public Usuario getUsuario(String usuario) throws Exception{
		if(users.isEmpty() || users == null){
			throw new RuntimeException("No existe el usuario especificado.");
		}
		for(Usuario u : users){
			if(u.getNombre().equals(usuario)){
				return u;
			}
		}
		throw new RuntimeException("No existe el usuario especificado.");
	}
	
	/**
	 * M�todo que permite obtener un {@link usuarios.Grupo_usuarios Grupo_usuarios} a partir de su nombre.
	 * @param grupo Cadena que se desea analizar.
	 * @return El grupo correspondiente a esa cadena, si existe.
	 * @throws RuntimeException Si no existe el grupo especificado.
	 */
	public Grupo_usuarios getGrupo(String grupo){
		if(grupos.isEmpty() || grupos == null){
			throw new RuntimeException("No existe el grupo especificado.");
		}
		for(Grupo_usuarios g : grupos){
			if(g.getNombre().equals(grupo))
				return g;
		}
		throw new RuntimeException("No existe el grupo especificado.");
	}
	
	//M�TODOS QUE SE UTILIZAN AL PRINCIPIO DEL PROGRAMA
	
	/**
	 * M�todo que comprueba si el usuario que ejecuta el programa ha sido invitado para unirse a alg�n grupo.
	 * Las invitaciones a un grupo se indicar�n si en la lista de usuarios del grupo, aparece su nombre seguido de la etiqueta
	 * <b>&lt;I&gt;</b>.
	 * El m�todo imprimir� por pantalla cada una de las invitaciones por separado, y permitir� al usuario aceptarlas o no.
	 * En caso afirmativo, modifica la lista de grupos para que el usuario quede incluido en el grupo con todos los derechos
	 * (crear y modificar tareas del grupo, invitar a usuarios al grupo y aceptar o rechazar solicitudes de admisi�n).
	 */
	public void compruebaInvitacion(){
		Usuario aux = null;
		LinkedList<Grupo_usuarios> grupos_restantes = new LinkedList<Grupo_usuarios>();
		grupos_restantes.addAll(grupos);
		grupos_restantes.removeAll(u.getGrupos());
		for(Grupo_usuarios g : grupos_restantes){
			boolean si = false;
			for(Usuario us : g.getUsuarios()){
				if(us.getNombre().equals(u.getNombre()+"<I>")){
					System.out.println("Ha sido invitado para unirse al grupo "+g.getNombre()+".");
					System.out.print("�Desea aceptar la invitaci�n? (s/n) ");
					if("s".equals(teclado.nextLine())){
						si = true;
						u.addGrupo(g);
					}
					aux = us;
				}
			}
			if(si)
				g.addUsuario(u);
			if(aux != null)
				g.delUsuario(aux);
		}
	}
	
	/**
	 * M�todo que comprueba si existen solicitudes de admisi�n a un grupo en el que est� el usuario seleccionado.
	 * Las solicitudes se indicar�n cuando existe un usuario en el grupo cuyo nombre termina en <b>&lt;T&gt;</b>.
	 * Si existen solicitudes, le preguntar� al usuario si desea aceptarlas. En caso afirmativo, modificar� la
	 * lista de grupos de tal forma que el usuario que haya hecho la solicitud quede incluido en el grupo.
	 * En el caso contrario, eliminar� cualquier referencia al usuario que haya hecho la solicitud dentro del grupo.
	 * Por lo tanto, si se deniega la solicitud, el usuario que haya solicitado la admisi�n en el grupo no
	 * pertenecer� al mismo, pero podr� solicitar su admisi�n cuantas veces quiera.
	 * @throws Exception Si el usuario especificado no existe.
	 */
	public void compruebaSolicitud() throws Exception{
		LinkedList<Usuario> eliminar;
		LinkedList<Usuario> anadir;
		for(Grupo_usuarios g : u.getGrupos()){
			eliminar = new LinkedList<Usuario>();
			anadir = new LinkedList<Usuario>();
			for(Usuario us : g.getUsuarios()){
				if(us.getNombre().endsWith("<T>")){
					String nombre = us.getNombre().replace("<T>", "");
					System.out.println("El usuario "+nombre+" ha solicitado unirse a su grupo "+g.getNombre()+".");
					System.out.print("�Desea aceptar la solicitud? (s/n) ");
					if("s".equals(teclado.nextLine())){
						Usuario usr = getUsuario(nombre);
						anadir.add(usr);
						usr.addGrupo(g);
					}
					eliminar.add(us);
				}
			}
			g.getUsuarios().addAll(anadir);
			g.getUsuarios().removeAll(eliminar);
		}
	}
	
	/**
	 * M�todo de lectura de dependencias a partir de una l�nea que las contenga.
	 * @param linea L�nea a analizar.
	 * @return Una lista de IDs de dependencias.
	 */
	public ArrayList<Integer> leerDependencias(String linea){
		ArrayList<Integer> dependencias = new ArrayList<Integer>();
		if(linea.equals("-"))
			return dependencias;
		String [] depend = linea.split(",");
		for(String d : depend){
			dependencias.add(Integer.parseInt(d));
		}
		return dependencias;
	}

	//M�TODOS REFERIDOS A LA CREACI�N Y ELIMINACI�N DE TAREAS
	
	/**
	 * M�todo que indica si una tarea est� duplicada. 
	 * No es equivalente a un m�todo equals, ya que no compara todos los atributos, sino solo 
	 * aquellos necesarios para considerar una tarea como duplicada.
	 * @param tarea Tarea que se desea analizar.
	 * @return true si la tarea est� duplicada, false si no lo est�.
	 */
	public boolean isDuplicada(Tarea tarea){
		for(Tarea t : l.getTareasUsuario(u)){
			if(tarea.getDescripcion().equals(t.getDescripcion()) && tarea.getTipo().equals(t.getTipo()) && tarea.getPropietario().equals(t.getPropietario())){
				if(tarea instanceof Tarea_con_plazo && t instanceof Tarea_con_plazo){
					if(((Tarea_con_plazo)tarea).getFecha().equals(((Tarea_con_plazo)t).getFecha()))
							return true;
					else
						return false;
				}
				else
					return true;
			}
		}
		return false;
	}
	
	/**
	 * M�todo que devuelve un {@link usuarios.Propietario Propietario} escogido por el Usuario.
	 * Al usuario se le presentar�n por pantalla distintos Propietarios: �l mismo y todos los grupos a los que �l pertenece.
	 * @return El {@link usuarios.Propietario Propietario} escogido por el Usuario.
	 * @throws Exception Si la elecci�n escogida no es correcta.
	 */
	public Propietario getPropietario() throws Exception{
		System.out.println("Propietario: ");
		System.out.println("\t 1) Usted mismo");
		int i = 1;
		for(Grupo_usuarios g : u.getGrupos()){
			i++;
			System.out.println("\t "+i+") Grupo "+g.toString());
		}
		int prop = teclado.nextInt();
		Propietario p = null;
		if(prop == 1)
			p = u;
		else if(!u.getGrupos().isEmpty() && prop > 1){
			p = u.getGrupos().get(prop-2);
		}
		else{
			throw new Exception("La elecci�n no es correcta.");
		}
		
		return p;
	}
	
	/**
	 * M�todo que comprueba que todas las dependencias contenidas en la lista existan.
	 * @param dependencias Lista de dependencias que se desean analizar.
	 * @throws Exception Si alguna de las dependencias no es correcta.
	 */
	public void comprobarDependencias(ArrayList<Integer> dependencias) throws Exception{
		for(Integer in: dependencias){
			try{
				l.buscarID(in);
			}
			catch(Exception e){
				throw new Exception("Error 3: Alguna de las dependencias indicadas es incorrecta.");
			}
		}
	}
	
	/**
	 * M�todo que permite a�adir una tarea a la {@link listas.Lista_tareas Lista_tareas}.
	 * No la a�adir� si la tarea est� duplicada, y para comprobarlo usar� el m�todo {@link #isDuplicada(Tarea)}.
	 * @param t Tarea que se desea a�adir.
	 * @throws Exception Si la tarea est� duplicada.
	 */
	public void addTarea(Tarea t) throws Exception{
		if(isDuplicada(t))
			throw new Exception("Error 1: Tarea ya existente.");
		else
			l.addTarea(t, u);

	}
	
	/**
	 * M�todo que permite eliminar una tarea de la lista de tareas.
	 * @param ID ID de la tarea que se desea eliminar.
	 * @throws Exception Si la tarea no pertenece al Usuario {@link #u}. 
	 * @throws Exception Si hay otras tareas que dependen de la tarea que se desea eliminar.
	 */
	public void delTarea(int ID) throws Exception{
		Tarea t = l.buscarID(ID);
		if(!t.perteneceA(u))
			throw new Exception("Error 5: Operaci�n no permitida sobre esta tarea.");
		else if(!l.getDependientes(ID).isEmpty())
			throw new Exception("Error 4: Otras tareas dependen de esta tarea.");
		else
			l.eliminarTarea(t);

	}
	
	//M�TODOS DE MODIFICAR PAR�METROS DE LAS TAREAS
	
	/**
	 * M�todo que permite obtener una {@link tareas.Tarea Tarea} dada su ID.
	 * @param operacion Operaci�n que se desea hacer. Sus valores usuales son "modificar" o "enviar".
	 * @return La tarea que se buscaba.
	 * @throws Exception Si la tarea especificada no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 */
	public Tarea getTarea(String operacion) throws Exception{
		System.out.print("Indique el ID de la tarea a "+operacion+": ");
		int id = teclado.nextInt();
		teclado.nextLine();
		System.out.println("");
		Tarea t = l.buscarID(id);
		if(!t.perteneceA(u)){
			throw new Exception("Error 5: Operaci�n no permitida sobre esta tarea.");
		}
		return t;
	}
	
	/**
	 * M�todo que permite modificar la prioridad de la {@link tareas.Tarea Tarea}.
	 * @param t Tarea que se desea modificar.
	 * @param variacion Variaci�n que se quiere aplicar a la prioridad.
	 */
	public void modPrioridad(Tarea t, int variacion){
		int nueva_prioridad = t.getPrioridad() + variacion;
		if(nueva_prioridad < 1)
			nueva_prioridad = 1;
		else if(nueva_prioridad > 10)
			nueva_prioridad = 10;
		t.setPrioridad(nueva_prioridad);
	}
	
	/**
	 * M�todo que permite modificar el estado de la {@link tareas.Tarea Tarea}.
	 * @param t Tarea que se desea modificar.
	 * @throws Exception Si la tarea no puede cambiar de estado por cualquier motivo.
	 */
	public void modEstado(Tarea t) throws Exception{
		if(l.puedeCambiarEstado(t)){
			t.setPendiente(!t.isPendiente());
		}
	}
	
	/**
	 * M�todo que permite modificar la fecha de plazo de la {@link tareas.Tarea Tarea}.
	 * @param aux {@link tareas.Tarea_con_plazo Tarea_con_plazo} auxiliar.
	 */
	public void modFecha(Tarea_con_plazo aux){
		l.setCambios(aux);
	}
	
	/**
	 * M�todo que permite a�adir una dependencia a la {@link tareas.Tarea Tarea}.
	 * @param t Tarea que se desea modificar.
	 * @param dependenciaID ID de la dependencia que se desea a�adir.
	 * @throws Exception Si se intenta a�adir a una tarea terminada, una tarea pendiente como dependencia.
	 * @throws Exception Si la tarea dependencia no existe.
	 * @throws Exception Si la tarea a modificar no existe.
	 */
	public void addDependencia(Tarea t, int dependenciaID) throws Exception {
		
		if(!t.isPendiente() && l.buscarID(dependenciaID).isPendiente()){
			throw new Exception("Error 10: Una tarea terminada no puede tener tareas previas pendientes.");
		}
		if(t.getDependencias().contains(dependenciaID))
			return;
		
		@SuppressWarnings("unused")
		Tarea dependencia = l.buscarID(dependenciaID); //Para revisar que exista
		t.addDependencia(dependenciaID);
	}
	
	/**
	 * M�todo que permite eliminar una dependencia.
	 * @param t Tarea que se desea modificar.
	 * @param dependenciaID ID de la dependencia que se desea eliminar.
	 * @throws Exception Si la dependencia que se intenta eliminar no existe.
	 */
	public void eliminarDependencia(Tarea t, int dependenciaID) throws Exception {
		t.eliminarDependencia(dependenciaID);		
	}
	
	//M�TODOS DE IMPRESI�N DE TAREAS SEG�N LA OPERACI�N ESCOGIDA
	
	/**
	 * M�todo que permite ordenar la lista seg�n un par�metro y un orden.
	 * @param l Lista de las tareas que se desean ordenar.
	 * @param parametro Par�metro seg�n el cual se desea ordenar.
	 * @param orden Orden de la lista (ascendente o descendente).
	 */
	public void ordenar(LinkedList<Tarea> l, final String parametro, String orden){
		final int signo = (orden.equals("ascendente")) ? 1 : -1;
		Collections.sort(l,new Comparator<Tarea>(){
			
			public int compare(Tarea t1, Tarea t2) {
				int retorno = signo*t1.compareTo(t2, parametro);
				if(retorno == 0){
					retorno = signo*t1.getDescripcion().compareTo(t2.getDescripcion());
				}
				return retorno;
			}
			
		});
	}
	
	/**
	 * M�todo que imprime una lista de tareas.
	 * @param destino Destino de la impresi�n de la lista.
	 * @param estado Estado que deben tener la tareas de la lista.
	 * @param param Par�metro seg�n el cual se desea ordenar la lista.
	 * @param orden Orden que debe seguir la lista ordenada.
	 * @throws Exception Si no se puede escribir en el archivo por cualquier motivo.
	 */
	public void printLista(String destino, String estado, String param, String orden) throws Exception{
		
		String titulo = "Listado de tareas "+estado+" ordenado por "+param+" en sentido "+orden;
		
		LinkedList<Tarea> lista = null;
		if("totales".equals(estado)){
			lista = l.getTareasUsuario(u);
		}
		else{
			boolean pendiente = ("pendientes".equals(estado)) ? true : false;
			lista = l.getTareasEstado(pendiente, u);
		}
		
		ordenar(lista, param, orden);
		
		if("pantalla".equals(destino)){
			System.out.println(titulo);
			for(Tarea t: lista){
				System.out.println(t.toStringLista());
			}
		}
		else{
			File fichero = new File(destino);
			if(!fichero.canWrite()){
				if(!fichero.createNewFile())
					throw new Exception("Error 9: No se pudo almacenar la informaci�n en el fichero.");
			}
			PrintWriter salida = new PrintWriter (fichero);
			salida.println(titulo);
			for(Tarea t: lista){
				salida.println(t.toStringLista());
			}
			salida.close();
		}
	}
	
	/**
	 * M�todo que imprime una lista de tareas que coinciden en plazo.
	 * @param fecha Fecha que deben compartir las tareas que se mostrar�n.
	 * @throws Exception Si la lista esta vac�a.
	 */
	public void printBuscarFecha(String fecha) throws Exception{
		
		LinkedList<Tarea> lista = l.buscarFecha(fecha, u);
		
		if(lista.isEmpty())
			throw new Exception("No hay ninguna tarea que venza en esa fecha.");
		else{
			for(Tarea t: lista){
				System.out.println("");
				System.out.println(t.toStringBuscarFecha());
			}
		}
	}
	
	/**
	 * M�todo que imprime la lista de tareas cuya descripci�n contiene la misma cadena.
	 * @param cadena Cadena que se desea buscar.
	 * @throws Exception Si no hay ninguna tarea que contenga esa cadena.
	 */
	public void printBuscarDescripcion(String cadena) throws Exception{
		
		LinkedList<Tarea> lista = l.buscarDescripcion(cadena, u);
		
		if(lista.isEmpty())
			throw new Exception("No hay ninguna tarea que tenga esa cadena.");
		else{
			for(Tarea t: lista){
				System.out.println("");
				System.out.println(t.toStringBuscarDescripcion(cadena));
			}
		}
	}
	
	/**
	 * M�todo que imprime una tarea determinada.
	 * @param ID ID de la tarea a imprimir.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 */
	public void printVerTarea(int ID) throws Exception{
		Tarea t = l.buscarID(ID);

		if(!t.perteneceA(u))
			throw new Exception("Error 5: Operaci�n no permitida sobre esta tarea.");
		
		String estado = (t.isPendiente()) ? "pendiente" : "terminada";
		System.out.println("ID: "+t.getID());
		System.out.println("Descripci�n: "+t.getDescripcion());
		System.out.println("Tipo: "+t.getTipo());
		System.out.println("Propietario: "+t.getPropietario());
		System.out.println("Prioridad: "+t.getPrioridad());
		System.out.println("Estado: "+estado);
		if(t instanceof Tarea_con_plazo){
			System.out.println("Plazo: "+((Tarea_con_plazo)t).toStringFecha());
			if(t.isPendiente())
				System.out.println("D�as restantes: "+((Tarea_con_plazo)t).getDias());
		}
		System.out.println("Depende directamente de: "+t.getDependencias());
		ArrayList<Integer> aux = l.getDependenciasIndirectas(ID);
		aux.removeAll(t.getDependencias());
		System.out.println("Depende indirectamente de: "+aux);
		System.out.println("Dependen directamente de ella: "+l.getDependientes(ID));
		aux = l.getDependientesIndirectos(ID);
		aux.removeAll(l.getDependientes(ID));
		System.out.println("Dependen indirectamente de ella: "+aux);
	}
	
	//M�TODOS DE GESTI�N DE GRUPOS Y USUARIOS
	
	/**
	 * M�todo que permite modificar el email del usuario.
	 * @param email La nueva direcci�n de email que desea tener el usuario.
	 */
	public void modEmail(String email){
		u.setEmail(email);
	}

	/**
	 * M�todo que permite al usuario dejar de pertenecer a uno de sus grupos.
	 * Si es el �ltimo integrante del grupo, el grupo se eliminar� y con �l, todas sus tareas.
	 * @throws Exception Si el usuario no pertenece a ning�n grupo.
	 * @throws Exception Si la elecci�n hecha cuando se le pregunta qu� grupo desea abandonar, es incorrecta.
	 */
	public void delGrupo() throws Exception{
		if(u.getGrupos().isEmpty())
			throw new Exception("No pertenece a ning�n grupo.");
		System.out.print("\t Seleccione el grupo al que desea dejar de pertenecer:");
		int i = 0;
		for(Grupo_usuarios g : u.getGrupos()){
			i++;
			System.out.println("\t "+i+") Grupo "+g.toString());
		}
		System.out.println("\t 0) Cancelar");
		int prop = teclado.nextInt();
		teclado.nextLine();
		if(prop > u.getGrupos().size())
			throw new Exception("Elecci�n incorrecta.");
		else if(prop == 0)
			return;
		Grupo_usuarios g = u.getGrupos().get(prop-1);
		if(g.getUsuarios().size() == 1){
			System.out.println("\t S�lo pertenece usted al grupo. Si renuncia, el grupo dejar� de existir, as� como todas sus tareas, y ser�n eliminadas de la lista de dependencias de otras tareas.");
			System.out.print("\t �Est� seguro de su opci�n? (s/n) ");
			String op = teclado.nextLine();
			if("s".equalsIgnoreCase(op)){
				for(Tarea t : l.getTareasGrupo(g)){
					 for(Integer d : l.getDependientes(t.getID())){
						 Tarea aux = l.buscarID(d);
						 aux.eliminarDependencia(t.getID());
					 }
					 l.eliminarTarea(t);
				}
				g.delUsuario(u);
				u.delGrupo(g);
				grupos.remove(g);
			}
		}
		else{
			g.delUsuario(u);
			u.delGrupo(g);
		}
	}
	
	/**
	 * M�todo que permite al usuario solicitar el acceso para un grupo al que no pertenece.
	 * Esta solicitud tendr� que ser aceptada o rechazada por cualquier usuario que pertenezca al otro grupo.
	 * @throws Exception Si la elecci�n hecha cuando se le pregunta a qu� grupo desea unirse, es incorrecta.
	 * @see #compruebaSolicitud()
	 */
	public void solicitarAcceso() throws Exception{
		LinkedList<Grupo_usuarios> grupos_restantes = new LinkedList<Grupo_usuarios>();
		grupos_restantes.addAll(grupos);
		grupos_restantes.removeAll(u.getGrupos());
		
		LinkedList<Grupo_usuarios> gr_sol = new LinkedList<Grupo_usuarios>();
		for(Grupo_usuarios aux : grupos_restantes){
			for(Usuario usr : aux.getUsuarios()){
				String nombre_s = u.getNombre()+"<T>";
				if(nombre_s.equals(usr.getNombre()))
					gr_sol.add(aux);
			}
		}
		
		grupos_restantes.removeAll(gr_sol);
		
		if(grupos_restantes.isEmpty()){
			System.out.println("\t Ya pertenece a todos los grupos, o bien no queda ninguno para el que no haya hecho solicitud.");
			return;
		}
		
		System.out.print("\t Seleccione el grupo al que desea pertenecer:");
		int i = 0;
		for(Grupo_usuarios aux : grupos_restantes){
			i++;
			System.out.println("\t "+i+") Grupo "+aux.toString());
		}
		System.out.println("\t 0) Cancelar");
		int prop = teclado.nextInt();
		teclado.nextLine();
		if(prop > grupos_restantes.size())
			throw new Exception("Elecci�n incorrecta.");
		else if(prop == 0)
			return;
		grupos_restantes.get(prop-1).addSolicitud(u);
	}

	/**
	 * M�todo que permite al usuario invitar a otro usuario a uno de sus grupos.
	 * Esta invitaci�n tendr� que ser aceptada o rechazada por el usuario invitado en cuanto inicie sesi�n en el programa.
	 * @throws Exception Si alguna de las elecciones (Usuario a invitar, y Grupo) es incorrecta.
	 * @see #compruebaInvitacion()
	 */
	public void invitarUsuario() throws Exception{
		System.out.println("\t Seleccione el usuario que desea invitar: ");
		int i = 0;
		LinkedList<Usuario> new_users = new LinkedList<Usuario>();
		new_users.addAll(users);
		new_users.remove(u);
		for(Usuario usr : new_users){
			i++;
			System.out.println("\t "+i+") "+usr.toString());
		}
		System.out.println("\t 0) Cancelar");
		int prop = teclado.nextInt();
		teclado.nextLine();
		if(prop > new_users.size())
			throw new Exception("Elecci�n incorrecta.");
		else if(prop == 0)
			return;
		Usuario other = new_users.get(prop-1);
		LinkedList<Grupo_usuarios> grup_disp = new LinkedList<Grupo_usuarios>();
		for(Grupo_usuarios aux : u.getGrupos()){
			if(!aux.estaUsuario(other)){
				grup_disp.add(aux);
			}
		}
		
		LinkedList<Grupo_usuarios> gr_inv = new LinkedList<Grupo_usuarios>();
		for(Grupo_usuarios aux : grup_disp){
			for(Usuario usr : aux.getUsuarios()){
				if(usr.getNombre().equals(other.getNombre()+"<I>"))
					gr_inv.add(aux);
			}
		}
		grup_disp.removeAll(gr_inv);
		
		if(grup_disp.isEmpty()){
			System.out.println("\t Ya pertenece a todos los grupos, o bien no queda ninguno para el que no haya sido invitado.");
			return;
		}
		
		
		
		System.out.println("\t Seleccione el grupo al que desea invitarle: ");
		i = 0;
		for(Grupo_usuarios aux : grup_disp){
			i++;
			System.out.println("\t "+i+") Grupo "+aux.toString());
		}
		System.out.println("\t 0) Cancelar");
		prop = teclado.nextInt();
		teclado.nextLine();
		if(prop > grup_disp.size())
			throw new Exception("Elecci�n incorrecta.");
		else if(prop == 0)
			return;
		grup_disp.get(prop-1).addInvitacion(other);
	}
	
	/**
	 * M�todo que permite crear un nuevo grupo de usuarios.
	 * En principio, el �nico integrante del nuevo grupo ser� el usuario que lo crea. Para a�adir nuevos integrantes,
	 * se tendr� que hacer o bien por medio de invitaci�n, o bien por medio de solicitud.
	 * @param nombre_grupo Nombre del grupo que se desea crear.
	 * @throws Exception Si el grupo que se desea crear ya existe.
	 */
	public void crearGrupo(String nombre_grupo) throws Exception{
		for(Grupo_usuarios gr : grupos){
			if(gr.getNombre().equals(nombre_grupo))
				throw new Exception("El grupo especificado ya existe.");
		}
		Grupo_usuarios nuevo = new Grupo_usuarios(nombre_grupo);
		nuevo.addUsuario(u);
		u.addGrupo(nuevo);
		grupos.add(nuevo);
		System.out.println("\t Grupo creado satisfactoriamente.");
	}
	
	//M�TODO EQUALS
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Acceso_datos))
			return false;
		Acceso_datos other = (Acceso_datos) obj;
		if (grupos == null) {
			if (other.grupos != null)
				return false;
		} else if (!grupos.equals(other.grupos))
			return false;
		if (l == null) {
			if (other.l != null)
				return false;
		} else if (!l.equals(other.l))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
