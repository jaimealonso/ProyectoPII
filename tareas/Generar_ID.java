package tareas;

import java.util.ArrayList;

import listas.Lista_tareas;

/**
 * Clase que hace las operaciones necesarias para generar IDs únicas.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Generar_ID {

	/**
	 * Número de ID
	 */
	private int numID;
	
	/**
	 * Lista de todas las tareas disponibles. Se usará para determinar las IDs libres.
	 */
	private Lista_tareas l;
	
	/**
	 * Método constuctor del generador de IDs
	 * @param l Lista de todas las tareas disponibles.
	 */
	public Generar_ID(Lista_tareas l){
		this.l = l;
	}
	
	/**
	 * Método que devuelve una ID nueva.
	 * El método recorre la lista de IDs de las tareas creadas, y devuelve el primer número que se encuentra libre.
	 * @return Una ID única para identificar a una tarea.
	 */
	public int getID(){
		numID = 1;
		ArrayList<Integer> ID = l.getIDs();
		while(ID.contains(numID)){
			numID++;
		}
		return numID;
	}
}
