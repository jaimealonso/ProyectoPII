package tareas;

import java.util.ArrayList;

import usuarios.Propietario;

/**
 * Clase que describe una tarea simple.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Tarea_simple extends Tarea{
	
	/**
	 * Constructor de una tarea simple.
	 * @param descripcion Descripci�n de la tarea.
	 * @param propietario Propietario de la tarea.
	 * @param ID Identificador num�rico de la tarea (<i>Introducir -1 si se desea generar uno nuevo</i>)
	 * @param pendiente Si la tarea est� o no pendiente.
	 * @param dependencias Lista de dependencias de la tarea.
	 * @param prioridad Prioridad de la tarea.
	 */
	public Tarea_simple(String descripcion, Propietario propietario, int ID, boolean pendiente, ArrayList<Integer> dependencias, int prioridad) {
		super(descripcion, propietario, ID, pendiente, dependencias, prioridad);
		setTipo("simple");
	}

	/**
	 * M�todo que implementa el m�todo compararFecha() de su clase padre ({@link tareas.Tarea Tarea}).
	 * Se implementa para que, a la hora de ordenar las tareas por plazo, las tareas simples se consideren
	 * "menores" a las tareas con plazo, y as� tener una lista mejor ordenada.
	 * @param t Tarea que se desea comparar.
	 * @return -1 si t es una tarea con plazo, 0 si t es una tarea simple.
	 */
	public int compararFecha(Tarea t) {
		if(t instanceof Tarea_con_plazo)
			return -1;
		else
			return 0;
	}

}
