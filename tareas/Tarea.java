package tareas;

import java.util.ArrayList;
import java.util.Iterator;

import usuarios.Grupo_usuarios;
import usuarios.Propietario;
import usuarios.Usuario;

/**
 * Clase que describe las propiedades genéricas de una tarea.
 * @author Jaime Alonso Lorenzo.
 * @see Tarea_simple
 * @see Tarea_con_plazo
 *
 */
public abstract class Tarea{

	//Atributos
	/**
	 * Descripción de la tarea.
	 */
	private String descripcion;
	
	/**
	 * Propietario de la tarea.
	 */
	private Propietario propietario;
	
	/**
	 * Identificador numérico único asociado a la tarea.
	 */
	private int ID;
	
	/**
	 * Variable que indica si la tarea está pendiente o no.
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
	 * @param descripcion Descripción de la tarea.
	 * @param propietario Propietario de la tarea.
	 * @param ID Identificador numérico de la tarea.
	 * @param pendiente Si la tarea está o no pendiente.
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
	
	//MÉTODOS GETTERS Y SETTERS DE LA CLASE
	
	/**
	 * Método que permite conocer la descripción de la tarea.
	 * @return La descripción de la tarea.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Método que permite modificar la descripción de la tarea.
	 * @param descripcion La nueva descripción de la tarea.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Método que permite conocer el propietario de la tarea.
	 * @return El propietario de la tarea.
	 */
	public Propietario getPropietario() {
		return propietario;
	}

	/**
	 * Método que nos indica si la tarea está o no pendiente.
	 * @return true si está pendiente, false si está terminada.
	 */
	public boolean isPendiente() {
		return pendiente;
	}

	/**
	 * Método que permite modificar el estado de la tarea.
	 * @param pendiente Nuevo estado de la tarea.
	 */
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}

	/**
	 * Método que permite añadir una dependencia a la lista de dependencias.
	 * @param dependencia ID de la dependencia que se desea añadir.
	 */
	public void addDependencia(int dependencia) {
		dependencias.add(dependencia);
	}
	
	/**
	 * Método que permite eliminar una dependencia.
	 * @param dependencia ID de la dependencia que se desea eliminar.
	 * @throws Exception Si la tarea no contiene esa dependencia.
	 */
	public void eliminarDependencia(int dependencia) throws Exception {
		if(!dependencias.contains(dependencia))
			throw new Exception("La tarea no depende de esa ID");
		dependencias.remove(dependencias.indexOf(dependencia));
	}
	
	/**
	 * Método que nos permite saber la lista de dependencias.
	 * @return La lista de dependencias.
	 */
	public ArrayList<Integer> getDependencias(){
		return dependencias;
	}
	
	/**
	 * Método que nos da una representación de las dependencias.
	 * @return Una cadena que contiene las ID de las dependencias.
	 */
	public String toStringDependencias(){
		
		if(dependencias.size() == 0) //Si la lista esta vacía, imprimo -
			return "-";
		
		//Si no esta vacía, imprimo los ID
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
	 * Método que permite conocer la prioridad de la tarea.
	 * @return La prioridad de la tarea.
	 */
	public int getPrioridad() {
		return prioridad;
	}

	/**
	 * Método que permite modificar la prioridad de la tarea.
	 * @param prioridad La nueva prioridad.
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	/**
	 * Método que permite conocer la ID de la tarea.
	 * @return La ID de la tarea.
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Método que nos permite saber el tipo de la tarea.
	 * @return El tipo de la tarea.
	 */
	public String getTipo(){
		return tipo;
	}
	
	/**
	 * Método que permite modificar el tipo de la tarea.
	 * @param tipo Tipo de la tarea.
	 */
	public void setTipo(String tipo){
		this.tipo = tipo;
	}
	
	/**
	 * Método que indica si la tarea pertenece al Propietario indicado.
	 * @param p Propietario que se desea buscar.
	 * @return true si pertenece a él, false si no pertenece.
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

	//MÉTODO DE COMPARAR Y MÉTODO EQUALS
	
	public abstract int compararFecha(Tarea t);

	/**
	 * Método compareTo modificado para aceptar parámetros diferentes.
	 * @param t Tarea t a comparar.
	 * @param parametro Parámetro mediante el cual se desea comparar.
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

	//MÉTODOS TOSTRING DEPENDIENDO DE LA OPERACIÓN ESCOGIDA
	
	/**
	 * Método que devuelve una representación de la tarea en el formato del archivo de texto.
	 * @return Una cadena que sigue el formato del archivo de texto.
	 */
	public String toStringTXT(){
		String estado = (pendiente) ? "pendiente" : "terminada";
		String fecha = (this instanceof Tarea_con_plazo) ? ((Tarea_con_plazo)this).toStringFecha() : "-";
		return tipo+"\r\n"+ID+"\r\n"+descripcion+"\r\n"+propietario+"\r\n"+prioridad+"\r\n"+estado+"\r\n"+fecha+"\r\n"+toStringDependencias();
	}
	
	/**
	 * Método que devuelve una representación de la tarea en formato de lista.
	 * @return Una cadena que sigue el formato de lista.
	 */
	public String toStringLista(){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		String id = "ID: "+ID;
		String descr = "Descripción: "+descripcion;
		String type = "Tipo: "+tipo;
		String prop = "Propietario: "+propietario;
		String prior = "Prioridad: "+prioridad;
		return id+"\r\n"+descr+"\r\n"+type+"\r\n"+prop+"\r\n"+prior+"\r\n"+estado+"\r\n";
	}
	
	/**
	 * Método que devuelve una representación de la tarea en el formato de buscar por fecha.
	 * @return Una cadena que sigue el formato de buscar por fecha.
	 */
	public String toStringBuscarFecha(){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		return "ID: "+ID+"\n"+"Descripción: "+descripcion+"\n"+estado;
	}
	
	/**
	 * Método que devuelve una representación de la tarea en el formato de buscar por descripción.
	 * @return Una cadena que sigue el formato de buscar por descripción.
	 */
	public String toStringBuscarDescripcion(String cadena){
		String estado = (pendiente) ? "Estado: pendiente" : "Estado: terminada";
		String descr_resaltada = "Descripción: "+descripcion.replaceAll("(?i)"+cadena, cadena.toUpperCase());
		return "ID: "+ID+"\n"+descr_resaltada+"\n"+estado+"\n";
	}
	
	/**
	 * Método que devuelve una representación de la tarea, lista para enviarse por correo electrónico.
	 * @return Una cadena que representa la tarea.
	 */
	public String toStringEmail(){
		String estado = (pendiente) ? "<b>Estado:</b> pendiente" : "<b>Estado:</b> terminada";
		String descr = "<b>Descripción:</b> "+descripcion;
		String type = "<b>Tipo:</b> "+tipo;
		String prop = "<b>Propietario:</b> "+propietario;
		String prior = "<b>Prioridad:</b> "+prioridad;
		String depend = "<b>Dependencias:</b> "+toStringDependencias();
		return descr+"<br>"+type+"<br>"+prop+"<br>"+prior+"<br>"+estado+"<br>"+depend+"<br>";
	}
	
}
