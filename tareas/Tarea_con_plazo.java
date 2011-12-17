package tareas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import usuarios.Propietario;

/**
 * Clase que define una Tarea con una fecha de plazo
 * @author Jaime Alonso Lorenzo
 *
 */
public class Tarea_con_plazo extends Tarea{
	
	/**
	 * Fecha de plazo de la tarea.
	 */
	private Date fecha;
	/**
	 * Formato de la fecha de forma <i>dd/MM/yyyy:hh:mm</i>
	 */
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
	/**
	 * Formato de la fecha de forma <i>dd/MM/yyyy</i>
	 */
	SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
	
	//CONSTRUCTORES DE LA CLASE
	
	/**
	 * Constructor de una tarea con plazo
	 * @param descripcion Descripción de la tarea.
	 * @param propietario Propietario de la tarea.
	 * @param ID Identificador numérico de la tarea (<i>Introducir -1 si se desea generar uno nuevo</i>)
	 * @param pendiente Si la tarea está o no pendiente.
	 * @param dependencias Lista de dependencias de la tarea.
	 * @param prioridad Prioridad de la tarea.
	 * @param fecha Fecha de plazo de la tarea.
	 * @throws Exception Si la tarea no es posterior a la fecha actual.
	 */
	public Tarea_con_plazo(String descripcion, Propietario propietario, int ID, boolean pendiente, ArrayList<Integer> dependencias, int prioridad, String fecha) throws Exception {
		super(descripcion, propietario, ID, pendiente, dependencias, prioridad);
		this.fecha = formato.parse(fecha);
		setTipo("con_plazo");
		if(pendiente && !isFutura()){
			throw new Exception("Error 2: La fecha debe ser posterior a la actual.");
		}
	}
	
	/**
	 * Constructor de una nueva tarea con plazo a partir de una tarea cualquiera.
	 * @param t Tarea a la que se le añade un plazo.
	 * @param fecha Fecha de plazo de la tarea.
	 * @throws Exception Si la tarea no es posterior a la fecha actual.
	 */
	public Tarea_con_plazo(Tarea t, String fecha) throws Exception{
		this(t.getDescripcion(), t.getPropietario(), t.getID(), t.isPendiente(), t.getDependencias(), t.getPrioridad(), fecha);
	}
	
	//MÉTODOS RELACIONADOS CON LA FECHA DE PLAZO
	
	/**
	 * Método que permite conseguir la fecha de plazo.
	 * @return La fecha de plazo de la tarea.
	 */
	public Date getFecha(){
		return fecha;
	}
	
	/**
	 * Compara la fecha de esta tarea con la de la tarea t.
	 * @param t Tarea con plazo que se desea comparar.
	 * @return 1 si la fecha de esta tarea es posterior a la tarea t, 0 si son iguales, -1 si es menor.
	 */
	public int compararFecha(Tarea t){
		if(t instanceof Tarea_simple)
			return 1;
		else
			return this.fecha.compareTo(((Tarea_con_plazo)t).getFecha());
	}

	/**
	 * Método que devuelve un String que represente la fecha de plazo.
	 * @return Una cadena que represente la fecha de plazo.
	 */
	public String toStringFecha() {
		return formato.format(fecha);
	}
	
	/**
	 * Método que devuelve un String que represente la fecha de plazo de forma compacta.
	 * @return Una cadena que representa la fecha de forma <i>dd/MM/yyyy</i>
	 */
	public String toStringFechaCompacta(){
		return formato2.format(fecha);
	}
	
	/**
	 * Método que permite saber los días que quedan para el plazo de la tarea.
	 * @return El número de días que quedan hasta el plazo.
	 */
	public int getDias(){
		Date actual = new Date();
		long milisegundos = getFecha().getTime() - actual.getTime();
		long dias = milisegundos/(1000*3600*24);
		return (int)dias;
	}
	
	//MÉTODOS TOSTRING SOBREESCRITOS
	
	@Override
	public String toStringLista() {
		String fecha = "Plazo: "+toStringFecha();
		return super.toStringLista()+fecha+"\n";
	}

	@Override
	public String toStringBuscarDescripcion(String cadena) {
		String fecha = "Plazo: "+toStringFecha();
		return super.toStringBuscarDescripcion(cadena)+fecha+"\n";
	}

	@Override
	public String toStringEmail() {
		String fecha = "<b>Plazo:</b> "+toStringFecha();
		return super.toStringEmail()+fecha;
	}
	
	/**
	 * Método que permite determinar si la fecha de la tarea está en el futuro.
	 * @return true si la tarea es futura, false si es pasada.
	 */
	private boolean isFutura(){
		return (getFecha().compareTo(new Date()) == 1);
	}

	@Override
	/**
	 * Método equals sobreescrito para la tarea con plazo.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Tarea_con_plazo))
			return false;
		Tarea_con_plazo other = (Tarea_con_plazo) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		return true;
	}


	

}