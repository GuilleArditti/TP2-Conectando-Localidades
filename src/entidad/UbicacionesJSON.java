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

public class UbicacionesJSON implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Ubicacion> ubicaciones;
	
	public UbicacionesJSON() {
		ubicaciones = new ArrayList<>();
	}
	
	public void agregar(Ubicacion ubicacion) {
		this.ubicaciones.add(ubicacion);
	}

	public String generarJSONPretty() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
		return json;
	}

	public void guardarJSON(String jsonParaGuardar, String archivoDestino) {
		try {
			FileWriter writer = new FileWriter(archivoDestino);
			writer.write(jsonParaGuardar);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static UbicacionesJSON leerJSON(String archivo) {
        Gson gson = new Gson();
        UbicacionesJSON ret = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            ret = gson.fromJson(br, UbicacionesJSON.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

	@Override
	public String toString() {
		return "UbicacionesJSON [ubicaciones=" + ubicaciones + "]";
	}
}