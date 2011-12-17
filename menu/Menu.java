package menu;

import gestion.Acceso_datos;
import gestion.Acceso_email;
import gestion.Acceso_ficheros;

import java.util.ArrayList;
import java.util.Scanner;


import tareas.Generar_ID;
import tareas.Tarea;
import tareas.Tarea_con_plazo;
import tareas.Tarea_simple;
import usuarios.Propietario;
import usuarios.Usuario;

/**
 * Clase que engloba todas las operaciones relacionadas con la interacci�n del programa con el usuario.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Menu {

	/**
	 * Mecanismo de entrada de datos a trav�s del teclado.
	 */
	Scanner teclado = new Scanner(System.in);
	
	/**
	 * Datos que se usar�n en el programa.
	 */
	private Acceso_datos datos;
	
	/**
	 * Ficheros que se usan. Este objeto solo se usa una vez finalizada la ejecuci�n de la parte principal del programa.
	 */
	private Acceso_ficheros ficheros;

	/**
	 * Generador de identificadores num�ricos �nicos para las tareas.
	 */
	private Generar_ID generador;
	
	/**
	 * Constructor del Men� a partir de los datos le�dos en los ficheros.
	 * @param ficheros Ficheros a partir de los cuales se est� leyendo.
	 * @param usuario Usuario que ha iniciado sesi�n en el programa.
	 * @throws Exception Si hay alg�n error a la hora de leer los ficheros.
	 */
	public Menu(Acceso_ficheros ficheros, String usuario) throws Exception{
		
		this.ficheros = ficheros;
		
		datos = ficheros.getDatos();
		
		Usuario u;
		
		try{
			u = datos.getUsuario(usuario);
			datos.setUsuario(u, false);
		}
		catch(RuntimeException e){
			System.out.println(e.getMessage()+" Se crear� a continuaci�n.");
			System.out.print("Introduzca su email: ");
			u = new Usuario(usuario);
			u.setEmail(teclado.nextLine());
			datos.setUsuario(u, true);
		}
		
		datos.compruebaInvitacion();
		datos.compruebaSolicitud();

		generador = datos.getGenerador();
		
		
		menu();
	}
	
	
	/**
	 * M�todo men� que engloba todas las opciones del programa.
	 * Permite al usuario escoger lo que quiere hacer. El men� continuar� mostr�ndose hasta que el usuario escoja la opci�n de salir
	 * del programa.
	 */
	public void menu(){
		
		int opcion;
		do{
			opcion = printMenu();
			try{
			switch(opcion){
				case 1:
					crear();
					break;
				case 2:
					eliminar();
					break;
				case 3:
					vertarea();
					break;
				case 4:
					verlista();
					break;
				case 5:
					buscar();
					break;
				case 6:
					cambiarestado();
					break;
				case 7:
					cambiarfecha();
					break;
				case 8:
					cambiarprioridad();
					break;
				case 9:
					modificardependencia();
					break;
				case 10:
					gestionarUsuarios();
					break;
				case 11:
					enviarEmail();
					break;
				case 12:
					recibirEmail();
					break;
				case 0:
					salir();
					break;
				default:
					//Pone el men� de nuevo
				}
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}while(true);
	}
	

	/**
	 * Imprime la lista de opciones del men� por pantalla.
	 * @return La opci�n escogida por el usuario.
	 */
	public int printMenu(){
		System.out.println("1) Crear");
		System.out.println("2) Eliminar");
		System.out.println("3) Ver tarea");
		System.out.println("4) Ver lista");
		System.out.println("5) Buscar");
		System.out.println("6) Cambiar estado");
		System.out.println("7) Cambiar fecha");
		System.out.println("8) Cambiar prioridad");
		System.out.println("9) Modificar dependencia");
		System.out.println("10) Gesti�n de usuarios");
		System.out.println("11) Enviar tareas por email");
		System.out.println("12) Recibir tareas por email");
		System.out.println("0) Salir");
		System.out.println("");
		System.out.print("�Siguiente operaci�n?: ");
		int opcion = teclado.nextInt();
		teclado.nextLine();
		return opcion;
	}
	
	/**
	 * M�todo que nos permite crear una tarea, pregunt�ndole al usuario los par�metros con los que se
	 * desea crear la nueva tarea.
	 * @throws Exception Si la elecci�n de Propietario es incorrecta.
	 * @throws Exception Si alguna de las dependencias no es correcta.
	 * @throws Exception Si la fecha introducida no es futura.
	 * @throws Exception Si la tarea est� duplicada.
	 */
	private void crear() throws Exception{
		Tarea aux = null;
		System.out.print("Tipo de tarea (simple o con_plazo): ");
		String tipo = teclado.nextLine();
		System.out.print("Descripci�n: ");
		String descripcion = teclado.nextLine();
		Propietario p = datos.getPropietario();
		System.out.print("Prioridad: ");
		int prioridad = teclado.nextInt();
		teclado.nextLine();
		System.out.print("Estado (pendiente o terminada): ");
		String estado = teclado.nextLine();
		boolean pendiente = (estado.equalsIgnoreCase("pendiente")) ? true : false;
		System.out.print("Dependencias (IDs separadas por comas, o gui�n en caso de no haberlas): ");
		ArrayList<Integer> dependencias = datos.leerDependencias(teclado.nextLine());
		datos.comprobarDependencias(dependencias);
		
		if(tipo.equals("con_plazo")){
			System.out.print("Introduzca la fecha de plazo (dd/MM/yyyy:HH:mm): ");
			String fecha = teclado.nextLine();
			try{
				aux = new Tarea_con_plazo(descripcion, p, generador.getID(), pendiente, dependencias, prioridad, fecha);
			}
			catch(Exception e){
				System.err.println(e.getMessage());
				return;
			}
		}
		else if(tipo.equals("simple"))
			aux = new Tarea_simple(descripcion, p, generador.getID(), pendiente, dependencias, prioridad);
		
		datos.addTarea(aux);
	}
	
	/**
	 * M�todo que permite al usuario eliminar una de <u>sus tareas</u>, introduciendo el ID, y siempre que le pertenezca.
	 * @throws Exception Si la tarea no existe, si no pertenece al usuario, o si otras tareas dependen de ella.
	 */
	private void eliminar() throws Exception{
		System.out.print("Introduzca ID a eliminar: ");
		int ID = teclado.nextInt();
		datos.delTarea(ID);
	}
	
	/**
	 * M�todo que permite visualizar una de <u>sus tareas</u>, introduciendo el ID que la caracteriza.
	 * @throws Exception Si la tarea no existe, o si no pertenece al usuario.
	 */
	private void vertarea() throws Exception{
		System.out.print("Introduzca el ID de la tarea: ");
		int ID = teclado.nextInt();
		datos.printVerTarea(ID);
	}
	
	/**
	 * M�todo que imprime una lista de tareas, siguiendo el orden que le indique el usuario, imprimiendo bien por pantalla,
	 * bien por fichero.
	 * @throws Exception Si se intenta escribir en un fichero usado por el sistema.
	 * @throws Exception Si por cualquier motivo, no es posible escribir en el fichero.
	 */
	private void verlista() throws Exception{
		System.out.println("\t 1) Pendientes");
		System.out.println("\t 2) Terminadas");
		System.out.println("\t 3) Todas");
		System.out.println("\t 0) Cancelar");
		System.out.println("");
		System.out.println("\t �Tareas a presentar?: ");
		int opcion = teclado.nextInt();
		String estado = "";
		if(opcion == 0)
			return;
		if(opcion != 3){
			estado = (opcion == 1) ? "pendientes" : "terminadas";
		}
		else{
			estado = "totales";
		}
		
		
		System.out.println("\t 1) Por prioridad ascendente");
		System.out.println("\t 2) Por prioridad descendente");
		System.out.println("\t 3) Por plazo ascendente");
		System.out.println("\t 4) Por plazo descendente");
		System.out.println("\t 0) Cancelar");
		System.out.println("");
		System.out.println("\t �Criterio de ordenaci�n?: ");
		opcion = teclado.nextInt();
		if(opcion == 0)
			return;
		String parametro = (opcion == 1 || opcion == 2) ? "prioridad" : "fecha";
		String orden = (opcion == 1 || opcion == 3) ? "ascendente" : "descendente";
		System.out.println("\t 1) Por pantalla");
		System.out.println("\t 2) A fichero");
		System.out.println("\t 0) Cancelar");
		opcion = teclado.nextInt();
		if(opcion == 0)
			return;
		if(opcion == 1){
			datos.printLista("pantalla", estado, parametro, orden);
		}
		else if (opcion == 2){
			System.out.print("\t Especifique el nombre del fichero: ");
			String archivo = teclado.next();
			if(archivo.equals("tareas.txt") || archivo.equals("grupos.txt") || archivo.equals("usuarios.txt"))
				throw new Exception("Error 9: No se pudo almacenar la informaci�n en el fichero.");
			datos.printLista(archivo, estado, parametro, orden);
		}
	}
	

	/**
	 * M�todo permite al usuario buscar una de <u>sus tareas</u>.
	 * @throws Exception Si no se encuentran tareas que coincidan con los t�rminos de b�squeda.
	 */
	private void buscar() throws Exception{
		System.out.println("\t 1) Por fecha");
		System.out.println("\t 2) Por descripci�n");
		System.out.println("\t 0) Cancelar");
		System.out.println("");
		System.out.print("\t �Criterio de b�squeda?");
		int opcion = teclado.nextInt();
		teclado.nextLine();
		switch(opcion){
			case 1:
				System.out.println("\t �Qu� fecha desea buscar? (dd/MM/aa)");
				String fecha = teclado.next();
				datos.printBuscarFecha(fecha);
				break;
			case 2:
				System.out.println("\t �Qu� fragmento de texto desea buscar?");
				String cadena = teclado.nextLine();
				if(cadena.equals("") || cadena.equals(" "))
					throw new Exception("Introduzca una cadena correcta.");
				datos.printBuscarDescripcion(cadena);
				break;
			case 0:
			default:
				break;
		}
	}
	

	
	/**
	 * M�todo que permite al usuario cambiar el estado de una de <u>sus tareas</u>.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 * @throws Exception Si la tarea no puede cambiar de estado por cualquier motivo.
	 */
	private void cambiarestado() throws Exception{
		Tarea t = datos.getTarea("modificar");
		
		System.out.println("Descripci�n: "+t.getDescripcion());
		String estado = (t.isPendiente()) ? "Estado: pendiente" : "Estado: terminada";
		System.out.println(estado);
		System.out.println("");
		System.out.println("�Desea cambiar el estado de la tarea? (s/n)");
		String eleccion = teclado.nextLine();
		if("s".equalsIgnoreCase(eleccion))
			datos.modEstado(t);
	}
	
	/**
	 * M�todo que permite al usuario cambiar la fecha de plazo de una de <u>sus tareas</u> o, en el caso de que sea simple, a�ad�rselo.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 * @throws Exception Si la tarea est� terminada.
	 * @throws Exception Si la fecha no es posterior a la actual.
	 */
	private void cambiarfecha() throws Exception {
		Tarea t = datos.getTarea("modificar");
		
		if(!t.isPendiente()){
			throw new Exception("Error 8: La tarea ya est� terminada.");
		}
		
		System.out.println("Descripci�n: "+t.getDescripcion());
		System.out.println("Tipo: "+t.getTipo());
		String fecha = (t instanceof Tarea_con_plazo) ? "Plazo: "+((Tarea_con_plazo)t).toStringFecha() : "";
		System.out.println(fecha);
		System.out.println("Introduzca la nueva fecha: (dd/MM/aa:HH:mm)");
		String dato = teclado.nextLine();
		Tarea_con_plazo aux = new Tarea_con_plazo(t, dato);
		datos.modFecha(aux);
	}

	/**
	 * M�todo que permite al usuario cambiar la prioridad de una de <u>sus tareas</u>.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 */
	private void cambiarprioridad() throws Exception {
		Tarea t = datos.getTarea("modificar");
		
		System.out.println("Descripci�n: "+t.getDescripcion());
		System.out.println("Prioridad: "+t.getPrioridad());
		System.out.println("");
		System.out.print("Indique el valor de variaci�n: ");
		int variacion = teclado.nextInt();
		datos.modPrioridad(t, variacion);
		
	}
	
	/**
	 * M�todo que permite al usuario modificar las dependencias de una de <u>sus tareas</u>.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si la tarea no pertenece al usuario.
	 * @throws Exception Si se le intentan a�adir dependencias pendientes a una tarea terminada.
	 * @throws Exception Si la dependencia que se intenta eliminar no est� en la lista de dependencias.
	 */
	private void modificardependencia() throws Exception {
		Tarea t = datos.getTarea("modificar");
		
		System.out.println("Esta tarea depende de: ");
		System.out.println(t.toStringDependencias());
		System.out.println("");
		System.out.println("1) A�adir dependencia");
		System.out.println("2) Eliminar dependencia");
		System.out.println("0) Cancelar");
		System.out.println("");
		System.out.println("�Operaci�n sobre dependencias?: ");
		int opcion = teclado.nextInt();
		if (opcion == 1){
			System.out.print("Indique el ID de la tarea que desea a�adir: ");
			int dependencia = teclado.nextInt();
			datos.addDependencia(t, dependencia);
		}
		if(opcion == 2){
			System.out.println("Esta tarea depende de: ");
			System.out.println(t.toStringDependencias());
			System.out.print("Indique el ID de la tarea que desea eliminar: ");
			int dependencia = teclado.nextInt();
			datos.eliminarDependencia(t, dependencia);
		}
		else
			return;
	}
	
	/**
	 * M�todo que permite gestionar los usuarios. Las operaciones disponibles en este m�todo son:
	 * <ul>
	 * <li>Modificar la direcci�n de email asociada al usuario.</li>
	 * <li>Abandonar uno de los grupos asociados al usuario.</li>
	 * <li>Solicitar el acceso a un grupo de usuarios al que no pertenece. La solicitud tendr� que
	 * ser aceptada por un miembro del grupo al que se solicita el acceso.</li>
	 * <li>Invitar a un usuario a pertenecer a un grupo al que pertenece el usuario que ejecuta el programa. Aceptando la invitaci�n,
	 * el usuario invitado se incorpora al grupo.</li>
	 * <li>Crear un grupo nuevo de usuarios. En principio, el �nico integrante ser� el usuario que lo crea.
	 * Si hay otros usuarios que quieren ser parte del nuevo grupo, deber�n entrar por medio de solicitud, o bien por invitaci�n.</li>
	 * </ul>
	 * @see gestion.Acceso_datos#compruebaInvitacion() compruebaInvitacion()
	 * @see gestion.Acceso_datos#compruebaSolicitud() compruebaSolicitud()
	 * @throws Exception Si hay alg�n fallo en alguno de los m�todos de gesti�n de usuarios.
	 */
	private void gestionarUsuarios() throws Exception {
		System.out.println("\t 1) Modificar mi direcci�n de email.");
		System.out.println("\t 2) Dejar de pertenecer a uno de mis grupos.");
		System.out.println("\t 3) Solicitar el acceso a un grupo al que no pertenezco.");
		System.out.println("\t 4) Invitar a otro usuario a unirse a uno de mis grupos.");
		System.out.println("\t 5) Crear un nuevo grupo de usuarios.");
		System.out.println("");
		System.out.println("\t 0) Cancelar.");
		int opcion = teclado.nextInt();
		teclado.nextLine();
		switch(opcion){
			case 1:
				System.out.println("Su direcci�n actual "+datos.getUsuarioActual().getEmail());
				System.out.print("\t Introduzca la nueva direcci�n: ");
				datos.modEmail(teclado.nextLine());
				break;
			case 2:
				datos.delGrupo();
				break;
			case 3:
				datos.solicitarAcceso();
				break;
			case 4:
				datos.invitarUsuario();
				break;
				
			case 5:
				System.out.println("\t Escriba el nombre del grupo que desea crear: ");
				String nombre_grupo = teclado.nextLine();
				datos.crearGrupo(nombre_grupo);
				break;
			case 0:
			default:
				
		}

	}

	/**
	 * M�todo que proporciona una interfaz de usuario para poder enviar tareas como recordatorio por medio de emails. 
	 * El usuario podr� enviar las tareas a la direcci�n que tiene asociada, o sino a otra que �l mismo especifique.
	 * <i>La cuenta de correo puede ser de cualquier proveedor</i>.
	 * @throws Exception Si la tarea especificada no existe.
	 * @throws Exception Si la tarea especificada no pertenece al usuario.
	 * @throws Exception Si hay alg�n error a la hora de enviar el email.
	 */
	private void enviarEmail() throws Exception {
		Tarea t = datos.getTarea("enviar");

		System.out.println("\t 1) Enviar a mi direcci�n de correo electr�nico: "+datos.getUsuarioActual().getEmail());
		System.out.println("\t 2) Enviar a otra direcci�n de correo electr�nico.");
		System.out.println("");
		System.out.println("\t 0) Cancelar");
		int opcion = teclado.nextInt();
		teclado.nextLine();
		String direccion;
		switch(opcion){
			case 1:
				direccion = datos.getUsuarioActual().getEmail();
				break;
			case 2:
				System.out.print("\t Indique la direcci�n de correo a la cual desea enviar la tarea: ");
				direccion = teclado.nextLine();
				break;
			case 0:
			default:
				return;
		}
		if(opcion == 1 || opcion == 2)
			Acceso_email.enviarTareas(t, direccion);
	}
	
	/**
	 * M�todo que permite al usuario recibir tareas por medio de emails, y a�adirlas a la lista de tareas.
	 * El usuario podr� recibir las tareas recibir las tareas a partir de la cuenta de correo que tenga asociada, o
	 * a partir de otra que especifique. <i>La cuenta de correo debe ser de Gmail</i>.
	 * @throws Exception Si la cuenta especificada no es de Gmail.
	 * @throws Exception Si hay alg�n error al recibir los emails.
	 * @throws Exception Si hay alguna tarea repetida.
	 */
	private void recibirEmail() throws Exception {

		System.out.println("\t 1) Recibir desde mi direcci�n de correo electr�nico: "+datos.getUsuarioActual().getEmail());
		System.out.println("\t 2) Recibir desde otra direcci�n de correo electr�nico.");
		System.out.println("");
		System.out.println("\t 0) Cancelar");
		int opcion = teclado.nextInt();
		teclado.nextLine();
		String direccion, password;
		switch(opcion){
			case 1:
				direccion = datos.getUsuarioActual().getEmail();
				System.out.print("\t Introduzca su contrase�a: ");
				password = teclado.nextLine();
				break;
			case 2:
				System.out.print("\t Indique la direcci�n de la cual desea recibir tareas: ");
				direccion = teclado.nextLine();
				System.out.print("\t Introduzca su contrase�a: ");
				password = teclado.nextLine();
				break;
			case 0:
			default:
				return;
		}
		Acceso_email.recibirTareas(direccion, password, datos, generador);
	}

	/**
	 *	M�todo que ejecuta las operaciones necesarias para salir del programa.
	 *	Si detecta cambios en el objeto Acceso_datos actual con respecto al que se genera leyendolo de los ficheros,
	 *	se le muestra al usuario un men� mediante el cual podr� guardar los cambios que se han hecho, o bien descartarlos.
	 *  La opci�n de Guardar o Rechazar los cambios se aplica a los tres archivos del sistema en conjunto.
	 * @throws Exception Si hay alg�n tipo de error al escribir en el fichero.
	 */
	private void salir() throws Exception {
		if(ficheros.hayCambios(datos)){
			System.out.println("\t 1) Guardar cambios");
			System.out.println("\t 2) Descartar cambios");
			System.out.println("\t 0) Cancelar");
			System.out.println("\t Aviso: al guardar los cambios, tambi�n se guardan los cambios realizados en los usuarios y sus grupos.");
			System.out.println("");
			System.out.print("\t �Opci�n?: ");
			int opcion = teclado.nextInt();
			if(opcion == 1){
				ficheros.escribirTareas(datos);
				ficheros.escribirGrupos(datos);
				ficheros.escribirUsuarios(datos);
			}
			if (opcion == 0){
				return;
			}
		}
		System.exit(0);
		
	}

	
}