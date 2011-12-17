package usuarios;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Clase que describe a un grupo de usuarios
 * @author Jaime Alonso Lorenzo
 *
 */
public class Grupo_usuarios extends Propietario {
	
	/**
	 * Lista de usuarios que pertenecen al grupo.
	 */
	private LinkedList<Usuario> usuarios = new LinkedList<Usuario>();
	
	/**
	 * Constructor del grupo de usuarios.
	 * @param nombre El nombre del grupo de usuarios.
	 */
	public Grupo_usuarios(String nombre){
		this.setNombre(nombre);
	}
	
	/**
	 * M�todo que permite conocer la lista de los usuarios pertenecientes a este grupo.
	 * @return Una lista con todos los usuarios que pertenecen a este grupo.
	 */
	public LinkedList<Usuario> getUsuarios(){
		return usuarios;
	}
	
	/**
	 * M�todo que permite a�adir un usuario al grupo.
	 * @param usuario Usuario que se desea a�adir.
	 */
	public void addUsuario(Usuario usuario){
		usuarios.add(usuario);
	}
	
	/**
	 * M�todo que permite eliminar el usuario seleccionado del grupo.
	 * @param usuario Usuario que se desea eliminar.
	 */
	public void delUsuario(Usuario usuario){
		usuarios.remove(usuario);
	}
	
	/**
	 * M�todo que permite a�adir una solicitud de admisi�n para este grupo.
	 * A�ade al grupo un usuario que es una copia del usuario especificado, pero con su nombre terminado en la etiqueta <b>&lt;T&gt;</b>.
	 * @param usuario Usuario que desea hacer la solicitud al grupo.
	 * @throws Exception Si no se soporta la clonaci�n del objeto usuario.
	 */
	public void addSolicitud(Usuario usuario) throws Exception{
		Usuario u = (Usuario) usuario.clone();
		u.setNombre(u.getNombre()+"<T>");
		addUsuario(u);
	}
	
	/**
	 * M�todo que permite a�adir una invitaci�n a entrar en este grupo.
	 * A�ade al grupo un usuario que es una copia del usuario especificado, pero con su nombre terminado en la etiqueta <b>&lt;I&gt;</b>.
	 * @param usuario Usuario que se desea invitar al grupo.
	 * @throws Exception Si no se soporta la clonaci�n del objeto usuario.
	 */
	public void addInvitacion(Usuario usuario) throws Exception{
		Usuario u = (Usuario) usuario.clone();
		u.setNombre(u.getNombre()+"<I>");
		addUsuario(u);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Grupo_usuarios))
			return false;
		Grupo_usuarios other = (Grupo_usuarios) obj;
		if (usuarios == null) {
			if (other.usuarios != null)
				return false;
		} else if (!usuarios.equals(other.usuarios))
			return false;
		return true;
	}

	/**
	 * M�todo que nos permite saber si el usuario indicado pertenece al grupo.
	 * @param usuario Usuario que se desea buscar.
	 * @return true si pertenece al grupo, false si no pertenece.
	 */
	public boolean estaUsuario(Usuario usuario){
		if(usuarios.contains(usuario)) 
			return true;
		else
			return false;
	}	
	
	/**
	 * M�todo que devuelve una representaci�n del grupo de usuarios con todos los usuarios que pertenecen al mismo.
	 * El convenio que se sigue es el que se explica en la especificaci�n del proyecto.
	 * @return Una cadena que caracteriza a este grupo.
	 */
	public String toStringTXT(){
		String retorno = getNombre()+": ";
		for(Iterator<Usuario> i = usuarios.iterator(); i.hasNext();){
			retorno += i.next().getNombre();
			if(i.hasNext()){
				retorno += ", ";
			}
		}
		return retorno;
	}

}
