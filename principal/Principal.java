package principal;

import java.util.Scanner;
import gestion.Acceso_ficheros;
import menu.Menu;

/**
 * Clase que contiene el método principal del programa. Construye el objeto que contiene los nombres de los ficheros
 * que se utilizan, lee el nombre del usuario y da paso a la ejecución del Menú.<br>
 * Este programa implementa todos los puntos básicos detallados en la especificación del proyecto, así como otras mejoras
 * a mayores. En general, estas mejoras son:
 * <ul>
 * <li>Gestión de errores mediante excepciones. Además de los errores especificados, se tratan algunos más.</li>
 * <li>Gestión mejorada de usuarios y de grupos. Es posible modificar los integrantes de los grupos, así como crear nuevos grupos.
 * La modificación de los integrantes del grupo es posible hacerla mediante tres mecanismos: el usuario decide abandonar un grupo al que
 * pertenece, el usuario solicita el acceso a un grupo al que no pertenece, o el usuario invita a otro usuario a acceder a un grupo al que
 * pertenece. Para más información, ver la documentación del método {@link menu.Menu#gestionarUsuarios() gestionarUsuarios()}.</li>
 * <li>Sistema de envío de tareas por email a modo de recordatorio, así como un sistema de recepción de nuevas tareas por email.
 * Para ello, cada Usuario tiene asignada una cuenta de correo (<i>que por defecto es pruebasproyectopii@gmail.com para todos</i>).
 * El sistema se detalla en la documentación de la clase {@link gestion.Acceso_email Acceso_email}.
 * </li>
 * </ul>
 * @author Jaime Alonso Lorenzo
 *
 */
public class Principal {

	/**
	 * Método main del programa.
	 * @param args Argumentos que se le pasen por línea de comandos.
	 * @throws Exception Si hay algún fallo al leer los archivos.
	 */
	public static void main(String[] args) throws Exception{
		
		Scanner teclado = new Scanner(System.in);
		
		System.out.println("Bienvenido al sistema de gestión de tareas.");
		
		String usuario;
		do{
			System.out.print("Introduzca su usuario: ");
			usuario = teclado.nextLine();
			if(usuario.endsWith("<T>") || usuario.endsWith("<I>")){
				System.out.println("");
				System.out.println("El nombre contiene una cadena reservada del sistema.");
				System.out.println("");
			}
		}while(usuario.endsWith("<T>") || usuario.endsWith("<I>"));
		
		Acceso_ficheros ficheros = new Acceso_ficheros("usuarios.txt", "grupos.txt", "tareas.txt");

		new Menu(ficheros, usuario);
	}
	
	
}
