package principal;

import java.util.Scanner;
import gestion.Acceso_ficheros;
import menu.Menu;

/**
 * Clase que contiene el m�todo principal del programa. Construye el objeto que contiene los nombres de los ficheros
 * que se utilizan, lee el nombre del usuario y da paso a la ejecuci�n del Men�.<br>
 * Este programa implementa todos los puntos b�sicos detallados en la especificaci�n del proyecto, as� como otras mejoras
 * a mayores. En general, estas mejoras son:
 * <ul>
 * <li>Gesti�n de errores mediante excepciones. Adem�s de los errores especificados, se tratan algunos m�s.</li>
 * <li>Gesti�n mejorada de usuarios y de grupos. Es posible modificar los integrantes de los grupos, as� como crear nuevos grupos.
 * La modificaci�n de los integrantes del grupo es posible hacerla mediante tres mecanismos: el usuario decide abandonar un grupo al que
 * pertenece, el usuario solicita el acceso a un grupo al que no pertenece, o el usuario invita a otro usuario a acceder a un grupo al que
 * pertenece. Para m�s informaci�n, ver la documentaci�n del m�todo {@link menu.Menu#gestionarUsuarios() gestionarUsuarios()}.</li>
 * <li>Sistema de env�o de tareas por email a modo de recordatorio, as� como un sistema de recepci�n de nuevas tareas por email.
 * Para ello, cada Usuario tiene asignada una cuenta de correo (<i>que por defecto es pruebasproyectopii@gmail.com para todos</i>).
 * El sistema se detalla en la documentaci�n de la clase {@link gestion.Acceso_email Acceso_email}.
 * </li>
 * </ul>
 * @author Jaime Alonso Lorenzo
 *
 */
public class Principal {

	/**
	 * M�todo main del programa.
	 * @param args Argumentos que se le pasen por l�nea de comandos.
	 * @throws Exception Si hay alg�n fallo al leer los archivos.
	 */
	public static void main(String[] args) throws Exception{
		
		Scanner teclado = new Scanner(System.in);
		
		System.out.println("Bienvenido al sistema de gesti�n de tareas.");
		
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
