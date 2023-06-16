package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import logica.LogicaAGM;
import entidad.Grafo;
import entidad.Arista;

public class LogicaAGMTest {

	private LogicaAGM logica = new LogicaAGM();;
	private Grafo grafo = new Grafo(5);

	@Test
	public void testTamanoAGMCorrecto() {
		int esperado = 5;
		
		// Agregamos aristas con pesos
		agregarAristas();

		Grafo agm = logica.algoritmoDePrim(grafo);
		assertEquals(esperado, agm.tamano());
	}

	@Test
	public void testPesoCorrecto() {
		int pesoEsperado = 14;
		int pesoObtenido = 0;

		agregarAristas();

		Grafo agm = logica.algoritmoDePrim(grafo);
		List<Arista> aristas = agm.getAristas();

		for (Arista arista : aristas)
			pesoObtenido += arista.getPeso();

		assertEquals(pesoEsperado, pesoObtenido);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGrafoVacio() {
		Grafo grafoVacio = new Grafo(0);
		logica.algoritmoDePrim(grafoVacio).tamano();
	}

	private void agregarAristas() {
		grafo.agregarArista(0, 1, 2);
		grafo.agregarArista(0, 3, 5);
		grafo.agregarArista(1, 2, 3);
		grafo.agregarArista(1, 3, 6);
		grafo.agregarArista(1, 4, 7);
		grafo.agregarArista(2, 4, 4);
		grafo.agregarArista(3, 4, 7);
	}
}
