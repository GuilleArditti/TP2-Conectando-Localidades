package entidad;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UbicacionesJSON {
	ArrayList<Ubicacion> ubicaciones;

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
}
