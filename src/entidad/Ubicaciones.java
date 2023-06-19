package entidad;

import java.util.HashSet;
import java.io.Serializable;

public class Ubicaciones implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashSet<Ubicacion> ubicaciones;
	
	public Ubicaciones() {
		ubicaciones = new HashSet<Ubicacion>();
	}
	
	public void agregar(Ubicacion ubicacion) {
		this.ubicaciones.add(ubicacion);
	}

	@Override
	public String toString() {
		return "UbicacionesJSON [ubicaciones=" + ubicaciones + "]";
	}
	
	public int tamanio() {
		return ubicaciones.size();
	}
	
	public HashSet<Ubicacion> getUbicaciones(){
		return this.ubicaciones;
	}
}
