package usuarios;

/**
 * Clase abstracta que define las características del propietario de una tarea.
 * @author Jaime Alonso Lorenzo
 *
 */
public abstract class Propietario {
	
	/**
	 * Nombre del propietario
	 */
	private String nombre;
	
	/**
	 * Método que permite al usuario saber el nombre del propietario.
	 * @return El nombre del propietario.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Método que permite cambiar el nombre del propietario
	 * @param nombre Nombre del propietario.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Método que retorna una representación del objeto.
	 * @return La representación del objeto en una String.
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
