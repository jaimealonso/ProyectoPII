package tareas;

import java.util.ArrayList;
import java.util.Iterator;

import usuarios.Grupo_usuarios;
import usuarios.Propietario;
import usuarios.Usuario;

/**
 * Clase que describe las propiedades gen�ricas de una tarea.
 * @author Jaime Alonso Lorenzo.
 * @see Tarea_simple
 * @see Tarea_con_plazo
 *
 */
public abstract class Tarea{

	//Atributos
	/**
	 * Descripci�n de la tarea.
	 */
	private String descripcion;
	
	/**
	 * Propietario de la tarea.
	 */
	private Propietario propietario;
	
	/**
	 * Identificador num�rico �nico asociado a la tarea.
	 */
	private int ID;
	
	/**
	 * Variable que indica si la tarea est� pendiente o no.
	 */
	private boolean pendiente;
	
	/**
	 * Lista de dependencias de la tarea.
	 */
	private ArrayList<Integer> dependencias = new ArrayList<Integer> ();
	
	/**
	 * Prioridad de la tarea.
	 */
	private int prioridad;
	
	/**
	 * Tipo de la tarea.
	 */
	private String tipo;
	
	/**
	 * Constructor de una tarea.
	 * @param descripcion Descripci�n de la tarea.
	 * @param propietario Propietario de la tarea.
	 * @param ID Identificador num�rico de la tarea.
	 * @param pendiente Si la tarea est� o no pendiente.
	 * @param dependencias Lista de dependencias de la tarea.
	 * @param prioridad Prioridad de la tarea.
	 */
	public Tarea(String descripcion, Propietario propietario, int ID,
			boolean pendiente, ArrayList<Integer> dependencias, int prioridad) {
		this.descripcion = descripcion;
		this.propietario = propietario;
		this.ID = ID;
		this.pendiente = pendiente;
		this.dependencias = dependencias;
		this.prioridad = prioridad;
	}
	
	//M�TODOS GETTERS Y SETTERS DE LA CLASE
	
	/**
	 * M�todo que permite conocer la descripci�n de la tarea.
	 * @return La descripci�n de la tarea.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * M�todo que permite modificar la descripci�n de la tarea.
	 * @param descripcion La nueva descripci�n de la tarea.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * M�todo que permite conocer el propietario de la tarea.
	 * @return El propietario de la tarea.
	 */
	public Propietario getPropietario() {
		return propietario;
	}

	/**
	 * M�todo que nos indica si la tarea est� o no pendiente.
	 * @return true si est� pendiente, false si est� terminada.
	 */
	public boolean isPendiente() {
		return pendiente;
	}

	/**
	 * M�todo que permite modificar el estado de la tarea.
	 * @param pendiente Nuevo estado de la tarea.
	 */
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}

	/**
	 * M�todo que permite a�adir una dependencia a la lista de dependencias.
	 * @param dependencia ID de la dependencia que se desea a�adir.
	 */
	public void addDependencia(int dependencia) {
		dependencias.add(dependencia);
	}
	
	/**
	 * M�todo que permite eliminar una dependencia.
	 * @param dependencia ID de la dependencia que se desea eliminar.
	 * @throws Exception Si la tarea no contiene esa dependencia.
	 */
	public void eliminarDependencia(int dependencia) throws Exception {
		if(!dependencias.contains(dependencia))
			throw new Exception("La tarea no depende de esa ID");
		dependencias.remove(dependencias.indexOf(dependencia));
	}
	
	/**
	 * M�todo que nos permite saber la lista de dependencias.
	 * @return La lista de dependencias.
	 */
	public ArrayList<Integer> getDependencias(){
		return dependencias;
	}
	
	/**
	 * M�todo que nos da una representaci�n de las dependencias.
	 * @return Una cadena que contiene las ID de las dependencias.
	 */
	public String toStringDependencias(){
		
		if(dependencias.size() == 0) //Si la lista esta vac�a, imprimo -
			return "-";
		
		//Si no esta vac�a, imprimo los ID
		else{
			String retorno = "";
			for(Iterator<Integer> i = dependencias.iterator(); i.hasNext();){
				retorno += i.next();
				if(i.hasNext())
					retorno += ",";
			}
			return retorno;
		}
	}

	/**
	 * M�todo que permite conocer la prioridad de la tarea.
	 * @return La prioridad de la tarea.
	 */
	public int getPrioridad() {
		return prioridad;
	}

	/**
	 * M�todo que permite modificar la prioridad de la tarea.
	 * @param prioridad La nueva prioridad.
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	/**
	 * M�todo que permite conocer la ID de la tarea.
	 * @return La ID de la tarea.
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * M�todo que nos permite saber el tipo de la tarea.
	 * @return El tipo de la tarea.
	 */
	public String getTipo(){
		return tipo;
	}
	
	/**
	 * M�todo que permite modificar el tipo de la tarea.
	 * @param tipo Tipo de la tarea.
	 */
	public void setTipo(String tipo){
		this.tipo = tipo;
	}
	
	/**
	 * M�todo que indica si la tarea pertenece al Propietario indicado.
	 * @param p Propietario que se desea buscar.
	 * @return true si pertenece a �l, false si no pertenece.
	 */
	public boolean perteneceA(Propietario p){
		if(p instanceof Grupo_usuarios)
			return p.equals(propietario);
		else{
			if(propietario instanceof Grupo_usuarios)
				return ((Grupo_usuarios) propietario).estaUsuario((Usuario)p);
			else
				return p.equals(propietario);
		}
	}

	//M�TODO DE COMPARAR Y M�TODO EQUALS
	
	public abstract int compararFecha(Tarea t);

	/**
	 * M�todo compareTo modificado para aceptar par�metros diferentes.
	 * @param t Tarea t a comparar.
	 * @param parametro Par�metro mediante el cual se desea comparar.
	 * @return 1 si esta tarea es mayor que la tarea t, 0 si son iguales, -1 si es menor.
	 */
	public int compareTo(Tarea t, String parametro){
		if(parametro.equals("fecha")){
			return this.compararFecha(t);
		}
		else if(parametro.equals("prioridad")){
			if(this.prioridad > t.prioridad)
				return 1;
			else if(this.prioridad == t.prioridad)
				return 0;
			else if(this.prioridad < t.prioridad)
				return -1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Tarea))
			return false;
		Tarea other = (Tarea) obj;
		if (ID != other.ID)
			return false;
		if (dependencias == null) {
			if (other.dependencias != null)
				return false;
		} else if (!dependencias.equals(other.dependencias))
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (pendiente != other.pendiente)
			return false;
		if (prioridad != other.prioridad)
			return false;
		if (propietario == null) {
			if (other.propietario != null)
				return false;
		} else if (!propietario.equals(other.propietario))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	//M�TODOS TOSTRING DEPENDIENDO DE LA OPERACI�N ESCOGIDA
	
	/**
	 * M�todo que devuelve una representaci�n de la tarea en el formato del archivo de texto.
	 * @return Una cadena que sigue el formato del archivo de texto.
	 */
	public String toStringTXT(){
		String estado = (pendiente) ? "pendiente" : "terminada";
		String fecha = (this instanceof Tarea_con_plazo) ? ((Tarea_con_plazo)this).toStringFecha() : "-";
		return tipo+"\r\n"+ID+"\r\n"+descripcion+"\r\n"+propietario+"\r\n"+prioridad+"\r\n"+estado+"\r\n"+fecha+"\r\n"+toStringDependencias();
	}
	
	/**
	 * M�todo que devuelve una representaci�n de la tarea en formato de lista.
	 * @return Una cadena que sigue el formato de lista.
	 */
	public String toStringLista(){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		String id = "ID: "+ID;
		String descr = "Descripci�n: "+descripcion;
		String type = "Tipo: "+tipo;
		String prop = "Propietario: "+propietario;
		String prior = "Prioridad: "+prioridad;
		return id+"\r\n"+descr+"\r\n"+type+"\r\n"+prop+"\r\n"+prior+"\r\n"+estado+"\r\n";
	}
	
	/**
	 * M�todo que devuelve una representaci�n de la tarea en el formato de buscar por fecha.
	 * @return Una cadena que sigue el formato de buscar por fecha.
	 */
	public String toStringBuscarFecha(){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		return "ID: "+ID+"\n"+"Descripci�n: "+descripcion+"\n"+estado;
	}
	
	/**
	 * M�todo que devuelve una representaci�n de la tarea en el formato de buscar por descripci�n.
	 * @return Una cadena que sigue el formato de buscar por descripci�n.
	 */
	public String toStringBuscarDescripcion(String cadena){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		String descr_resaltada = "Descripci�n: "+descripcion.replaceAll("(?i)"+cadena, cadena.toUpperCase());
		return "ID: "+ID+"\n"+descr_resaltada+"\n"+estado+"\n";
	}
	
	/**
	 * M�todo que devuelve una representaci�n de la tarea, lista para enviarse por correo electr�nico.
	 * @return Una cadena que representa la tarea.
	 */
	public String toStringEmail(){
		String estado = (pendiente) ? "<b>Estado:</b> pendiente" : "<b>Estado:</b> terminada";
		String descr = "<b>Descripci�n:</b> "+descripcion;
		String type = "<b>Tipo:</b> "+tipo;
		String prop = "<b>Propietario:</b> "+propietario;
		String prior = "<b>Prioridad:</b> "+prioridad;
		String depend = "<b>Dependencias:</b> "+toStringDependencias();
		return descr+"<br>"+type+"<br>"+prop+"<br>"+prior+"<br>"+estado+"<br>"+depend+"<br>";
	}
	
}
