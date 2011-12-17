package listas;

import java.util.ArrayList;
import java.util.LinkedList;

import tareas.Tarea;
import tareas.Tarea_simple;
import tareas.Tarea_con_plazo;
import usuarios.Grupo_usuarios;
import usuarios.Usuario;

/**
 * Clase que incluye a todas las tareas.
 * Adem�s, contiene m�todos referidos a operaciones internas con las tareas.
 * @author Jaime Alonso Lorenzo
 *
 */
public class Lista_tareas {

	/**
	 * Lista en la que se ubicar�n todas las tareas
	 */
	private LinkedList<Tarea> tareas = new LinkedList<Tarea>();
	
	/**
	 * Constructor de la clase
	 * @param tareas Lista de tareas
	 */
	public Lista_tareas(LinkedList<Tarea> tareas){
		this.tareas = tareas;
	}

	/**
	 * M�todo de b�squeda de tareas seg�n su ID �nico.
	 * @param ID El ID num�rico �nico de la tarea.
	 * @return La tarea que se buscaba.
	 */
	public Tarea buscarID(int ID) throws Exception{
		
		for(Tarea t: tareas){
			if(ID == t.getID()){
				return t;
			}
		}
		throw new Exception("No se ha podido encontrar la tarea seleccionada.");
	}
	
	/**
	 * M�todo que permite recuperar la lista completa de tareas.
	 * @return La lista completa de tareas.
	 */
	public LinkedList<Tarea> getListaCompleta(){
		return tareas;
	}
	
	/**
	 * M�todo de b�squeda de tareas seg�n su fecha de plazo.
	 * @param fecha La fecha de plazo a buscar.
	 * @param u Usuario que desea buscar las tareas.
	 * @return Una lista de tareas que tienen la fecha buscada.
	 */
	public LinkedList<Tarea> buscarFecha(String fecha, Usuario u){
		
		LinkedList<Tarea> lista = new LinkedList<Tarea>();
		for(Tarea t: getTareasUsuario(u)){
			if(t instanceof Tarea_simple)
				continue;
			else if(fecha.equals(((Tarea_con_plazo)t).toStringFechaCompacta()))
				lista.add(t);
		}
		return lista;
	}
	
	/**
	 * M�todo de b�squeda de tareas seg�n una cadena contenida en su descripci�n.
	 * La comparaci�n se hace sin importar el uso de may�sculas o min�sculas.
	 * @param cadena Cadena a buscar.
	 * @param u Usuario que desea buscar las tareas.
	 * @return Una lista de tareas cuya descripci�n contiene la cadena buscada.
	 */
	public LinkedList<Tarea> buscarDescripcion(String cadena, Usuario u){
		String auxMinusculas;
		String cadenaMinusculas = cadena.toLowerCase();
		LinkedList<Tarea> lista = new LinkedList<Tarea>();
		for(Tarea t: getTareasUsuario(u)){
			auxMinusculas = t.getDescripcion().toLowerCase();
			if(auxMinusculas.contains(cadenaMinusculas)){
				lista.add(t);
			}
		}
		return lista;
		
	}
	
	/**
	 * M�todo que permite sustituir una tarea en la lista de tareas. Se usa cuando hay cambios importantes
	 * en una tarea, por ejemplo el cambio de una {@link tareas.Tarea_simple Tarea_simple} a una {@link 
	 * tareas.Tarea_con_plazo Tarea_con_plazo}.
	 * @param tarea Tarea que se desea introducir en la lista.
	 */
	public void setCambios(Tarea tarea){
		int indice;
		for(Tarea t: tareas){
			if(tarea.getID() == t.getID()){
				indice = tareas.indexOf(t);
				tareas.remove(t);
				tareas.add(indice,tarea);
			}
		}
	}
	
	/**
	 * M�todo que permite a�adir una tarea a la lista.
	 * @param t Tarea que se desea a�adir.
	 * @param u Usuario que la est� a�adiendo.
	 */
	public void addTarea(Tarea t, Usuario u){
		tareas.add(t);
	}
	
	/**
	 * M�todo que permite eliminar una tarea de la lista.
	 * @param t Tarea que se desea eliminar de la lista.
	 */
	public void eliminarTarea(Tarea t) throws Exception{
		tareas.remove(t);
	}
	
	/**
	 * M�todo que permite recuperar la lista completa de tareas pertenecientes a un usuario.
	 * @param usuario Usuario del que se desea recuperar la lista.
	 * @return Una lista de tareas pertenecientes al usuario.
	 */
	public LinkedList<Tarea> getTareasUsuario(Usuario usuario){
		LinkedList<Tarea> contenedor = new LinkedList<Tarea>();
		for(Tarea t: tareas){
			if(t.perteneceA(usuario))
				contenedor.add(t);
			}
		return contenedor;
	}
	
	/**
	 * M�todo que permite obtener una lista de todas las tareas pertenecientes al grupo especificado.
	 * @param g Grupo de usuarios para el cual se desean obtener las tareas.
	 * @return Una lista de todas las tareas pertenecientes al grupo indicado.
	 */
	public LinkedList<Tarea> getTareasGrupo(Grupo_usuarios g){
		LinkedList<Tarea> contenedor = new LinkedList<Tarea>();
		for(Tarea t: tareas){
			if(t.perteneceA(g))
				contenedor.add(t);
			}
		return contenedor;
	}
	
	/**
	 * M�todo que junta en una lista los IDs de todas las tareas disponibles.
	 * @return Una lista con todos los IDs.
	 */
	public ArrayList<Integer> getIDs(){
		ArrayList<Integer> ID = new ArrayList<Integer>();
		for(Tarea t: tareas){
			ID.add(t.getID());
		}
		return ID;
	}
	
	/**
	 * M�todo que recupera la lista de las tareas que dependen de otra tarea.
	 * @param ID ID de la tarea que se desea buscar.
	 * @return Una lista de IDs de tareas dependientes.
	 */
	public ArrayList<Integer> getDependientes(int ID){
		ArrayList<Integer> lista = new ArrayList<Integer>();
		for(Tarea t: tareas){
			if(t.getDependencias().contains(ID))
				lista.add(t.getID());
		}
		return lista;
	}
	
	/**
	 * M�todo que recupera la lista de tareas de las que depende otra tarea indirectamente.
	 * @param ID ID de la tarea que se desea buscar.
	 * @return Una lista de IDs de dependencias indirectas.
	 * @throws Exception Si la tarea no existe.
	 */
	public ArrayList<Integer> getDependenciasIndirectas(int ID) throws Exception{
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Tarea t = buscarID(ID);
		lista.addAll(t.getDependencias());
		for(Integer d : t.getDependencias()){
			lista.addAll(getDependenciasIndirectas((int)d));
		}
		
		return lista;
	}
	
	/**
	 * M�todo que recupera la lista de tareas que dependen indirectamente de otra tarea.
	 * @param ID ID de la tarea que se desea buscar.
	 * @return Una lista de IDs de tareas dependientes indirectamente.
	 */
	public ArrayList<Integer> getDependientesIndirectos(int ID){
		ArrayList<Integer> lista = new ArrayList<Integer>();	
		lista.addAll(getDependientes(ID));
		for(Integer d : getDependientes(ID)){
			lista.addAll(getDependientesIndirectos((int)d));
		}
		return lista;
	}
	
	/**
	 * M�todo que recupera la lista de tareas seg�n el estado que tengan.
	 * @param pendiente Estado que se desea buscar.
	 * @param u Usuario que desea buscar la lista de tareas.
	 * @return Una lista de tareas, pertenecientes al usuario especificado, que tienen el estado escogido.
	 */
	public LinkedList<Tarea> getTareasEstado(boolean pendiente, Usuario u){
		LinkedList<Tarea> lista = new LinkedList<Tarea>();
		for(Tarea t: getTareasUsuario(u)){
			if(t.isPendiente() == pendiente){
				lista.add(t);
			}
		}
		return lista;
	}
	
	/**
	 * M�todo que indica si una tarea puede modificar su estado.
	 * @param t Tarea que se desea modificar.
	 * @return true si la tarea puede cambiar su estado.
	 * @throws Exception Si la tarea no existe.
	 * @throws Exception Si alguna tarea previa no est� terminada.
	 * @throws Exception Si alguna tarea posterior ya est� terminada.
	 */
	public boolean puedeCambiarEstado(Tarea t) throws Exception{
		if(t.isPendiente()){
			for(Integer i : t.getDependencias()){
				if(buscarID(i).isPendiente())
					throw new Exception("Error 6: Alguna tarea previa no est� terminada.");
			}
		}
		else{
			for(Integer i : getDependientes(t.getID())){
				if(!buscarID(i).isPendiente())
					throw new Exception("Error 7: Alguna tarea posterior ya est� terminada.");
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Lista_tareas))
			return false;
		Lista_tareas other = (Lista_tareas) obj;
		if (tareas == null) {
			if (other.tareas != null)
				return false;
		} else if (!tareas.equals(other.tareas))
			return false;
		return true;
	}

}
