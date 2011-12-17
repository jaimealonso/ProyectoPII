package tareas;

import java.util.ArrayList;

import listas.Lista_tareas;

/**
 * Clase que hace las operaciones necesarias para generar IDs �nicas.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Generar_ID {

	/**
	 * N�mero de ID
	 */
	private int numID;
	
	/**
	 * Lista de todas las tareas disponibles. Se usar� para determinar las IDs libres.
	 */
	private Lista_tareas l;
	
	/**
	 * M�todo constuctor del generador de IDs
	 * @param l Lista de todas las tareas disponibles.
	 */
	public Generar_ID(Lista_tareas l){
		this.l = l;
	}
	
	/**
	 * M�todo que devuelve una ID nueva.
	 * El m�todo recorre la lista de IDs de las tareas creadas, y devuelve el primer n�mero que se encuentra libre.
	 * @return Una ID �nica para identificar a una tarea.
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
