package entidad;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class GrafoTest {

	@Test
	public void testConstructor() {
		Grafo g = new Grafo(3);
		int[][] esperado = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
		assertArrayEquals(esperado, g.matriz);
	}

	@Test
	public void testAgregarArista() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 2);
		assertTrue(grafo.existeArista(0, 1));
	}
	
	@Test
	public void testPesoCorrecto() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 2);
		assertEquals(2, grafo.matriz[0][1]);
	}

	@Test
	public void testEliminarArista() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 2);
		grafo.eliminarArista(0, 1);
		assertFalse(grafo.existeArista(0, 1));
	}

	@Test
	public void testExisteArista() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 2);
		assertTrue(grafo.existeArista(0, 1));
	}
	
	@Test
	public void testNoExisteArista() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 2);
		assertFalse(grafo.existeArista(0, 2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarAristaVerticeInvalido() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 5, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEliminarAristaVerticeInvalido() {
		Grafo grafo = new Grafo(3);
		grafo.eliminarArista(0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAristaInvalida() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 0, 3);
	}

	@Test
	public void verticesVecinosTest() {
		Grafo grafo = new Grafo(4);
		grafo.agregarArista(1, 0, 1);
		grafo.agregarArista(1, 2, 1);
		grafo.agregarArista(1, 3, 1);

		Set<Integer> esperado= new HashSet<>();
		esperado.add(0);
		esperado.add(2);
		esperado.add(3);
		assertEquals(esperado, grafo.vecinos(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void vecinosVerticeNegativoTest() {
		Grafo grafo = new Grafo(5);
		grafo.vecinos(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void vecinosVerticeExcedidoTest() {
		Grafo grafo = new Grafo(5);
		grafo.vecinos(6);
	}

}