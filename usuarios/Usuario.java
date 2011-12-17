package usuarios;

import java.util.LinkedList;

/**
 * Clase que describe a un Usuario.
 * @author Jaime Alonso Lorenzo.
 */
public class Usuario extends Propietario {
	
	/**
	 * Lista que contiene los grupos a los que pertenece el usuario en cuestión.
	 */
	private LinkedList<Grupo_usuarios> grupos = new LinkedList<Grupo_usuarios>();

	/**
	 * Dirección de email asociada al usuario.
	 */
	private String email;
	
	/**
	 * Método que construye a un usuario, mediante su nombre.
	 * @param nombre Nombre del usuario.
	 */
	public Usuario(String nombre){
		this.setNombre(nombre);
	}
	
	/**
	 * Método que permite añadir un grupo a la lista de grupos del usuario.
	 * @param g Grupo de usuarios que se desea añadir.
	 */
	public void addGrupo(Grupo_usuarios g){
		this.grupos.add(g);
	}
	
	/**
	 * Método que elimina un grupo de la lista de grupos del usuario.
	 * @param g Grupo de usuarios que se desea eliminar.
	 */
	public void delGrupo(Grupo_usuarios g){
		this.grupos.remove(g);
	}
	
	/**
	 * Método que devuelve los grupos a los que pertenece el usuario.
	 * @return Una lista de los grupos a los que pertenece el usuario.
	 */
	public LinkedList<Grupo_usuarios> getGrupos(){
		return grupos;
	}

	/**
	 * Método que permite modificar la dirección de email del Usuario.
	 * @param email Una cadena que contiene la dirección de email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Método que permite conocer la dirección de email del Usuario.
	 * @return Una cadena que contiene la dirección de email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Método que devuelve una representación en forma de cadena del usuario, lista para introducir en el archivo de usuarios.
	 * @return Una cadena con los parámetros del usuario.
	 */
	public String toStringTXT(){
		return getNombre()+": "+email;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object obj = new Usuario(getNombre());
		((Usuario)obj).setEmail(getEmail());
		return obj;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		String grupos = new String();
		for(Grupo_usuarios g : this.grupos){
			grupos += g.getNombre();
		}
		String grupos_other = new String();
		for(Grupo_usuarios g : other.grupos){
			grupos_other += g.getNombre();
		}
		if(!grupos.equals(grupos_other))
			return false;
		return true;
	}
}
