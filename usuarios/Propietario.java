package usuarios;

/**
 * Clase abstracta que define las caracter�sticas del propietario de una tarea.
 * @author Jaime Alonso Lorenzo
 *
 */
public abstract class Propietario {
	
	/**
	 * Nombre del propietario
	 */
	private String nombre;
	
	/**
	 * M�todo que permite al usuario saber el nombre del propietario.
	 * @return El nombre del propietario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * M�todo que permite cambiar el nombre del propietario
	 * @param nombre Nombre del propietario.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * M�todo que retorna una representaci�n del objeto.
	 * @return La representaci�n del objeto en una String.
	 */
	public String toString(){
		return nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Propietario))
			return false;
		Propietario other = (Propietario) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if(!this.getClass().isInstance(other))
			return false;
		return true;
	}
}
