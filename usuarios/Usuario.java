package usuarios;

import java.util.LinkedList;

/**
 * Clase que describe a un Usuario.
 * @author Jaime Alonso Lorenzo.
 */
public class Usuario extends Propietario {
	
	/**
	 * Lista que contiene los grupos a los que pertenece el usuario en cuesti�n.
	 */
	private LinkedList<Grupo_usuarios> grupos = new LinkedList<Grupo_usuarios>();

	/**
	 * Direcci�n de email asociada al usuario.
	 */
	private String email;
	
	/**
	 * M�todo que construye a un usuario, mediante su nombre.
	 * @param nombre Nombre del usuario.
	 */
	public Usuario(String nombre){
		this.setNombre(nombre);
	}
	
	/**
	 * M�todo que permite a�adir un grupo a la lista de grupos del usuario.
	 * @param g Grupo de usuarios que se desea a�adir.
	 */
	public void addGrupo(Grupo_usuarios g){
		this.grupos.add(g);
	}
	
	/**
	 * M�todo que elimina un grupo de la lista de grupos del usuario.
	 * @param g Grupo de usuarios que se desea eliminar.
	 */
	public void delGrupo(Grupo_usuarios g){
		this.grupos.remove(g);
	}
	
	/**
	 * M�todo que devuelve los grupos a los que pertenece el usuario.
	 * @return Una lista de los grupos a los que pertenece el usuario.
	 */
	public LinkedList<Grupo_usuarios> getGrupos(){
		return grupos;
	}

	/**
	 * M�todo que permite modificar la direcci�n de email del Usuario.
	 * @param email Una cadena que contiene la direcci�n de email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * M�todo que permite conocer la direcci�n de email del Usuario.
	 * @return Una cadena que contiene la direcci�n de email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * M�todo que devuelve una representaci�n en forma de cadena del usuario, lista para introducir en el archivo de usuarios.
	 * @return Una cadena con los par�metros del usuario.
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
