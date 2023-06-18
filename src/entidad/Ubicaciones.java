package entidad;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	public String generarJSONPretty() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String texto = gson.toJson(this);
		return texto;
	}

	public void guardarJSON(String texto, String nombreArchivo) {
		try {
			FileWriter writer = new FileWriter(nombreArchivo);
			writer.write(texto);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Ubicaciones leerJSON(String nombreArchivo) {
        Gson gson = new Gson();
        Ubicaciones ret = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
            ret = gson.fromJson(br, Ubicaciones.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

	@Override
	public String toString() {
		return "UbicacionesJSON [ubicaciones=" + ubicaciones + "]";
	}
	
	public int tamanio() {
		return ubicaciones.size();
	}
}
