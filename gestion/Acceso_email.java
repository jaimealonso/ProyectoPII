package gestion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import tareas.Generar_ID;
import tareas.Tarea;
import tareas.Tarea_con_plazo;
import tareas.Tarea_simple;
import usuarios.Propietario;

/**
 * Clase que contiene métodos para gestionar las tareas mediante correos electrónicos.
 * No almacena ningún dato, ya que los únicos datos usados (dirección de correo y contraseña) no se modifican ni
 * se guardan posteriormente, por lo tanto, es un simple conjunto de métodos estáticos.<br>
 * Se basa en gran parte en clases definidas en la librería JavaMail, contenida en Java <abbr title="Enterprise Edition">EE</abbr>.
 * En este caso, se ha incluido la librería en la carpeta del proyecto y en su correspondiente
 * archivo .classpath (<i>de modo que se importa automáticamente</i>), ya que está programado con Java <abbr title="Standard Edition">SE</abbr>.<br>
 * En cuanto a enviar tareas por correo electrónico, se puede usar una dirección válida de cualquier proveedor, sin embargo,
 * debido a simplificaciones de la implementación, a la hora de recibir tareas sólo es posible usar una cuenta del proveedor Gmail.<br>
 * Los métodos de esta clase usan por defecto la dirección de correo <i>pruebasproyectopii@gmail.com</i>. Es una cuenta habilitada
 * específicamente para las pruebas relacionadas con esta clase. Si se desea acceder a la misma, la contraseña es <i>proyectop2pruebas</i>.
 * @author Jaime Alonso Lorenzo
 */
public abstract class Acceso_email {
	
	/**
	 * Método que permite enviar tareas como emails. El funcionamiento consiste en enviar las tareas, desde la propia cuenta que especifica el usuario, a esa misma cuenta, a modo de recordatorio.
	 * El email se enviará desde la cuenta <i>pruebasproyectopii@gmail.com</i>, aunque el destinatario puede ser
	 * cualquier dirección de email que el usuario indique. A continuación se detallará un ejemplo el mensaje recibido.
	 * <br><br><b><i>EJEMPLO</i></b>
	 * <br><br><i>Asunto</i>:<br> <blockquote>RECORDATORIO: Prueba de tarea<br></blockquote>
	 * <i>Cuerpo del mensaje</i>:<br>
	 * <blockquote><b>Descripción:</b> Prueba de tarea<br>
		<b>Tipo:</b> con_plazo<br>
		<b>Propietario:</b> pedro_pazos<br>
		<b>Prioridad:</b> 6<br>
		<b>Estado:</b> pendiente<br>
		<b>Dependencias:</b> -<br>
		<b>Plazo:</b> 17/12/2011:10:30</blockquote>
	 * @param t Tarea que se desea enviar.
	 * @param direccion Dirección de correo a la que se envía el recordatorio.
	 * @throws Exception Si hay algún fallo a la hora de enviar el email.
	 */
	public static void enviarTareas(Tarea t, String direccion) throws Exception{
		
		Properties propiedades = new Properties();
		propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
		propiedades.setProperty("mail.smtp.starttls.enable", "true");
		propiedades.setProperty("mail.smtp.port","587");
		propiedades.setProperty("mail.smtp.user", "pruebasproyectopii@gmail.com");
		propiedades.setProperty("mail.smtp.auth", "true");
		
		Session sesion = Session.getDefaultInstance(propiedades);
		System.out.println("Espere mientras se envía el mensaje...");
		
		MimeMessage miMensaje = new MimeMessage(sesion);
		miMensaje.setFrom(new InternetAddress("pruebasproyectopii@gmail.com"));
		miMensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(direccion));
		miMensaje.setSubject("RECORDATORIO: "+t.getDescripcion());
		miMensaje.setText(t.toStringEmail(), "ISO-8859-1", "html");
		
		Transport transporte = sesion.getTransport("smtp");
		transporte.connect("pruebasproyectopii@gmail.com", "proyectop2pruebas");

		transporte.sendMessage(miMensaje, miMensaje.getAllRecipients());
		System.out.println("¡Mensaje enviado!");
		transporte.close();
	}
	
	/**
	 * Método que permite recibir tareas a partir de emails enviados a la cuenta especificada.
	 * <br><br>
	 * <b><i><u>IMPORTANTE</u></i></b>
	 * <br>
	 * Para leer correctamente las tareas a partir de email, hay que tener en cuenta varias consideraciones.
	 * <br>
	 * <ul>
	 * <li>Sólo se tendrán en cuenta emails <b>no leídos</b> cuyo asunto sea <b>[NUEVA TAREA]</b></li>
	 * <li>El contenido del email se supone que es correcto, por lo tanto si no lo es, el método puede tener fallos. Para evitar eso, se detallará aquí la sintaxis que se debe utilizar.</li>
	 * <li>Una vez que se haya leído una tarea, el comportamiento por defecto es marcar el mensaje como leído (para que sucesivas ejecuciones del método no retornen una tarea repetida).</li>
	 * <li>El usuario sólo podrá recibir tareas de las cual él sea propietario. Si el programa lee alguna tarea que no le pertenezca al usuario especificado, se <b>ignorará</b>, procediendo a la lectura de las siguientes tareas.
	 * Si se da este caso, el mensaje leído (que no pertenece al usuario) se marcará como "no leído", para que su correspondiente propietario sea capaz de leerlo en otro momento.</li>
	 * <li>Las tareas enviadas con el método {@link #enviarTareas(Tarea,String)} no serán leídas por este método.</li>
	 * </ul>
	 * <br><br>
	 * <b><i><u>SINTAXIS</u></i></b><br>
	 * Para empezar, el email debe estar escrito en <b>texto plano sin formato</b>. No es posible leer a partir de emails formateados en HTML. Teniendo este importante detalle en cuenta, el resto de la sintaxis es de la siguiente forma:
	 * <blockquote>atributo: valor</blockquote>
	 * Se escribirá primero el nombre del atributo, seguido de dos puntos, y a continuación, el valor asignado. El orden en el que se deben escribir los atributos es el siguiente:
	 * <ol>
	 * <li>Descripción</li>
	 * <li>Tipo</li>
	 * <li>Propietario</li>
	 * <li>Prioridad</li>
	 * <li>Estado</li>
	 * <li>Dependencias (<i>Separadas por comas, o un guión en caso de no haberlas</i>)</li>
	 * <li>Plazo (<i>en el caso de haberlo</i>)</li>
	 * </ol>
	 * A continuación, se detallarán dos ejemplos de emails correctos, para cada uno de los dos tipos.<br><br>
	 * <u>Tarea simple</u>
	 * <blockquote>Descripción: Prueba de tarea nueva<br>
		Tipo: simple<br>
		Propietario: pedro_pazos<br>
		Prioridad: 4<br>
		Estado: pendiente<br>
		Dependencias: 4,5</blockquote>
	 * <u>Tarea con plazo</u>
	 * <blockquote>Descripción: Prueba de tarea nueva<br>
		Tipo: con_plazo<br>
		Propietario: pedro_pazos<br>
		Prioridad: 4<br>
		Estado: pendiente<br>
		Dependencias: -<br>
		Plazo: 17/12/2011:20:30</blockquote>
	 * @param direccion Dirección de la que recibir las nuevas tareas.
	 * @param password Contraseña de la cuenta de correo.
	 * @param datos Datos usados en el programa.
	 * @param generador Generador de IDs.
	 * @throws Exception Si hay algun fallo a la hora de recibir el email.
	 * @throws Exception Si la dirección de correo no es de Gmail.
	 */
	public static void recibirTareas(String direccion, String password, Acceso_datos datos, Generar_ID generador) throws Exception{
		
		if(!direccion.endsWith("@gmail.com")){
			throw new Exception("La dirección de correo eléctronico debe ser de Gmail.");
		}
		
		Properties propiedades = new Properties();
		propiedades.setProperty("mail.store.protocol", "imaps");
		
		Session sesion = Session.getDefaultInstance(propiedades);
		System.out.println("Espere mientras se reciben las nuevas tareas...");
		Store st = sesion.getStore("imaps");
		st.connect("imap.gmail.com", direccion, password);
		
		Folder carpeta = st.getFolder("Inbox");
		carpeta.open(Folder.READ_WRITE);
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		
		Message [] mensajes_nuevos = carpeta.search(ft);
		
		boolean nuevas = false;
		
		for(Message m : mensajes_nuevos){
			if("[NUEVA TAREA]".equals(m.getSubject())){
				String contenido = (String) m.getContent();
				String [] lineas = contenido.split("\r\n");
				LinkedList<String []> list = new LinkedList<String []>();
				for(String l : lineas){
					list.add(l.split(":\\s*"));
				}
				String descripcion = list.get(0)[1];
				String tipo = list.get(1)[1];
				String propietario = list.get(2)[1];
				Propietario p = null;
				try{
					p = datos.getGrupo(propietario);
				}
				catch(RuntimeException e){
					try{
						p = datos.getUsuario(propietario);
					}
					catch(RuntimeException ex){
						m.setFlags(new Flags(Flags.Flag.SEEN), false);
						System.err.println(ex.getMessage());
						return;
					}
				}
				int prioridad = Integer.parseInt(list.get(3)[1]);
				boolean pendiente = ("pendiente".equals(list.get(4)[1])) ? true : false;
				ArrayList<Integer> dependencias = datos.leerDependencias(list.get(5)[1]);
				if("con_plazo".equals(tipo)){
					String fecha = list.get(6)[1];
					Tarea_con_plazo aux = new Tarea_con_plazo(descripcion, p, generador.getID(), pendiente, dependencias, prioridad, fecha);
					if(!aux.perteneceA(datos.getUsuarioActual()))
						continue;
					else
						datos.addTarea(aux);
				}
				else{
					Tarea_simple aux = new Tarea_simple(descripcion, p, generador.getID(), pendiente, dependencias, prioridad);
					if(!aux.perteneceA(datos.getUsuarioActual())){
						m.setFlags(new Flags(Flags.Flag.SEEN), false);
						continue;
					}
					else
						datos.addTarea(aux);
				System.out.println("¡Nueva tarea recibida! "+descripcion);
				nuevas = true;
				}
				
			}
		}
		carpeta.close(true);
		if(!nuevas){
			System.out.println("No se han recibido nuevas tareas.");
		}
	}
}
