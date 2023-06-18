package entidad;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class UbicacionesTest {
	private Ubicacion ubi1, ubi2, ubi3;
	private UbicacionesJSON ubicaciones;

	@Before
	public void testSetup() {
		ubi1 = new Ubicacion("Bahia Blanca", "Buenos Aires", -12.54554, -32.1231321);
		ubi2 = new Ubicacion("General Acha", "La Pampa", -12.54554, -32.1231321);
		ubi3 = new Ubicacion("General Roca", "Rio Negro", -12.54554, -32.1231321);
		
		ubicaciones = new UbicacionesJSON();
		ubicaciones.agregar(ubi1);
		ubicaciones.agregar(ubi2);
		ubicaciones.agregar(ubi3);
	}
	
	@Test
	public void escribirYLeerArchivoTest() {
		escribirArchivo();
		String esperado = ubicaciones.toString();
		String obtenido = leerArchivo();
		assertEquals(esperado, obtenido);
		
		/* borrar */
		System.out.println(esperado);
		System.out.println(obtenido);
	}

	/* auxiliares */
	private void escribirArchivo() {
		try {
			FileOutputStream flujoSalidaArchivo = new FileOutputStream("ubicaciones.txt");
			ObjectOutputStream flujoSalidaObjeto = new ObjectOutputStream(flujoSalidaArchivo);
			flujoSalidaObjeto.writeObject(ubicaciones);
			flujoSalidaObjeto.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String leerArchivo() {
		UbicacionesJSON ubicaciones2 = null;
		
		try {
			FileInputStream fis = new FileInputStream("ubicaciones.txt");
			ObjectInputStream in = new ObjectInputStream(fis);
			ubicaciones2 = (UbicacionesJSON) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println("No se pudo cargar el archivo: " + e.getMessage());
		}
		
		return ubicaciones2 != null? ubicaciones2.toString() : ""; 
	}

}
