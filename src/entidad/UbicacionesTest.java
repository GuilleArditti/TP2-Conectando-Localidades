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
	private Ubicaciones ubicaciones;

	@Before
	public void testSetup() {
		ubi1 = new Ubicacion("Bahia Blanca", "Buenos Aires", -12.54554, -32.1231321);
		ubi2 = new Ubicacion("General Acha", "La Pampa", -12.54554, -32.1231321);
		ubi3 = new Ubicacion("General Roca", "Rio Negro", -12.54554, -32.1231321);
		
		ubicaciones = new Ubicaciones();
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
		System.out.println("Esperado: " + esperado);
		System.out.println("Obtenido: " + obtenido);
	}

	/* auxiliares */
	private void escribirArchivo() {
		try {
			FileOutputStream fos = new FileOutputStream("ubicaciones.txt");
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(ubicaciones);
			out.close();
		} catch (Exception e) {
			System.out.println("No se pudo escribir el archivo: " + e.getMessage());
		}
	}
	
	private String leerArchivo() {
		Ubicaciones ubicaciones2 = null;
		
		try {
			FileInputStream fis = new FileInputStream("ubicaciones.txt");
			ObjectInputStream in = new ObjectInputStream(fis);
			ubicaciones2 = (Ubicaciones) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println("No se pudo cargar el archivo: " + e.getMessage());
		}
		
		return ubicaciones2 != null? ubicaciones2.toString() : ""; 
	}

}
