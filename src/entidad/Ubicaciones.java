package entidad;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Ubicaciones implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Ubicacion> ubicaciones;
	
	public Ubicaciones() {
		ubicaciones = new ArrayList<>();
	}
	
	public void agregar(Ubicacion ubicacion) {
		this.ubicaciones.add(ubicacion);
	}
	
	public Ubicacion getById(int i) {
		return ubicaciones.get(i);
	}

	@Override
	public String toString() {
		return "UbicacionesJSON [ubicaciones=" + ubicaciones + "]";
	}
	
	public int tamanio() {
		return ubicaciones.size();
	}
}
