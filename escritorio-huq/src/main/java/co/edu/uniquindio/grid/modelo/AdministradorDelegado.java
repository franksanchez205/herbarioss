/**
 * 
 */
package co.edu.uniquindio.grid.modelo;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import co.edu.uniquindio.grid.ejbs.AdminEJBRemote;
import co.edu.uniquindio.grid.entidades.Empleado;
import co.edu.uniquindio.grid.entidades.Persona;
import co.edu.uniquindio.grid.excesiones.ElementoNoEncontradoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Delegado que permite conectar la capa de negocio con la de presentación
 * 
 * @author EinerZG
 * @version 1.0
 */
public class AdministradorDelegado {

	/**
	 * instancia que representa el ejb remoto de administrador
	 */
	private AdminEJBRemote adminEJB;
	/**
	 * permite manejar una unica instancia para le manejo de delegados
	 */
	public static AdministradorDelegado administradorDelegado = instancia();

	/**
	 * constructor para conectar con la capa de negocio
	 */
	private AdministradorDelegado() {
		try {
			adminEJB = (AdminEJBRemote) new InitialContext().lookup(AdminEJBRemote.JNDI);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permite devolver una unica instancia de delegado
	 * 
	 * @return instancia unica del delegado
	 */
	private static AdministradorDelegado instancia() {

		if (administradorDelegado == null) {
			administradorDelegado = new AdministradorDelegado();
			return administradorDelegado;
		}
		return administradorDelegado;
	}

	/**
	 * pemite registar un nuevo empleado
	 * 
	 * @param empleado empleado a agregar
	 * @return devuelve true si el empleado fue eliminado
	 */
	public boolean registrarEmpleado(Empleado empleado) {
		try {
			return adminEJB.insertarEmpleado(empleado) != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * devuvel la lista de empleado que estan en la base de datos
	 * 
	 * @return todos los empleados
	 */
	public List<Empleado> listarEmpleado() {
		return adminEJB.listarEmpleados();
	}

	/**
	 * permite eliminar el empleado por cedula
	 * 
	 * @return empleado si fue eliminado
	 */
	public boolean eliminarEmpleado(Empleado empleado) {
		try {
			return adminEJB.eliminarPersona(empleado.getCedula()) != null;
		} catch (ElementoNoEncontradoException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * genera una lista de empleados observables
	 * 
	 * @return todos los empleados obsevables
	 */
	public ObservableList<EmpleadoObservable> listarEmpleadosObservables() {
		List<Empleado> empleados = listarEmpleado();
		ObservableList<EmpleadoObservable> empleadosObservables = FXCollections.observableArrayList();
		for (Persona persona : empleados) {
			empleadosObservables.add(new EmpleadoObservable(persona));
		}
		return empleadosObservables;
	}

}
