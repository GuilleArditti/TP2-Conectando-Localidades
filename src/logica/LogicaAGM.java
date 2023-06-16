package logica;

import java.util.ArrayList;
import java.util.List;

import entidad.Arista;
import entidad.Grafo;
import entidad.Planificador;
import entidad.Ubicacion;

public class LogicaAGM {

	private Planificador planificador;
	private List<Ubicacion> ubicaciones;
	private Grafo agm;

	public LogicaAGM() {
		planificador = new Planificador();
		ubicaciones = new ArrayList<>();
	}

	public void agregarUbicacion(Ubicacion ubicacion) {
		if (!ubicaciones.contains(ubicacion))
			ubicaciones.add(ubicacion);
		else
			throw new RuntimeException("No se puede agregar esa ubicación porque ya fue agregada anteriormente.");
	}
	
	/*
	 * Crea un Grafo original con todas las ubicaciones agregadas desde la interfaz.
	 * Le pasa el Grafo original al Algoritmo de Prim.
	 * Recibe un Grafo AGM.
	 * Devuelve un String informando la ruta de menor costo y el costo total.
	 */
	
	public String darSolucionAGM() {
		Grafo grafoOrigen = volcarListaAGrafo();
		agm = algoritmoDePrim(grafoOrigen);
		int costoTotal = calcularCostoTotal();
		String solucionTxt = escribirSolucion(costoTotal);
		return solucionTxt;
	}
	
	public Grafo algoritmoDePrim(Grafo grafo) {
		if (grafo == null)
			throw new IllegalArgumentException("El Grafo es null.");
		if (grafo.tamano() == 0)
			throw new IllegalArgumentException("Grafo sin nodos, no se puede generar el AGM.");
		
		int n = grafo.tamano();
		boolean[] verticesVisitados = new boolean[n]; 							// Para marcar los vertices visitados
		List<Arista> pendientes = new ArrayList<>(); 							// iniciamos con una lista de aristas vacias
		Grafo agm = new Grafo(n);	 											// Arbol minimo que sera retornado

		verticesVisitados[0] = true; 											// nos paramos sobre un vertice del grafo
		for (int i = 1; i < n; i++) {
			if (grafo.existeArista(0, i)) {
				pendientes.add(new Arista(0, i, grafo.getPesoArista(0, i)));
			}
		}

		while (!pendientes.isEmpty()) {
			ordenarAristasPorPeso(pendientes);
			Arista arista = tomarPrimerAristaYQuitarla(pendientes);
			
			if (verticeVisitado(verticesVisitados, arista))						// Si el vertice ya fue visitado, pasamos a tomar el siguiente
				continue;
			
			marcarVisitado(verticesVisitados, arista);
			
			agm.agregarArista(arista);
			int destino = arista.getDestino();
			for (int j = 0; j < n; j++) {
				if (!verticesVisitados[j] && grafo.existeArista(destino, j)) {
					pendientes.add(new Arista(destino, j, grafo.getPesoArista(destino, j)));
				}
			}
		}
		return agm;
	}
	
	private int calcularCostoTotal() {
		int costoTotal = 0;
		for (Arista arista : agm.getAristas())
			costoTotal += arista.getPeso();
		return costoTotal;
	}
	
	private String escribirSolucion(int costoTotal) {
		StringBuilder solucionTxt = new StringBuilder();
		
		for (Arista arista : agm.getAristas())
			solucionTxt.append(ubicaciones.get(arista.getOrigen()) + " -> "
					+ ubicaciones.get(arista.getDestino()) + ". Costo: $ " + arista.getPeso() + "\n");
		
		solucionTxt.append("Costo total de las instalaciones: $ " + costoTotal);
		return solucionTxt.toString();
	}

	private void marcarVisitado(boolean[] verticesVisitados, Arista arista) {
		verticesVisitados[arista.getDestino()] = true;
	}

	private boolean verticeVisitado(boolean[] verticesVisitados, Arista arista) {
		return verticesVisitados[arista.getDestino()];
	}

	private void ordenarAristasPorPeso(List<Arista> aristas) {
		int n = aristas.size();
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (aristas.get(j).getPeso() > aristas.get(j + 1).getPeso()) {
					Arista temp = aristas.get(j);
					aristas.set(j, aristas.get(j + 1));
					aristas.set(j + 1, temp);
				}
			}
		}
	}

	private Arista tomarPrimerAristaYQuitarla(List<Arista> listaDeAristas) {
		Arista seleccionada = listaDeAristas.get(0);
		listaDeAristas.remove(0);
		return seleccionada;
	}

	private Grafo volcarListaAGrafo() {
		Grafo grafo = new Grafo(ubicaciones.size());
		for (int i = 0; i < grafo.tamano(); i++) {
			for (int j = 0; j < grafo.tamano(); j++) {
				if (i != j)
					grafo.agregarArista(i, j,
							planificador.calcularCostoEntrelocalidades(ubicaciones.get(i), ubicaciones.get(j)));
			}
		}
		return grafo;
	}
	
	public List<Ubicacion> getUbicaciones() {
		return this.ubicaciones;
	}
	
	public List<Arista> getAristasAGM() {
	    if (agm == null)
	      throw new RuntimeException("Todavia no se ha generado el Arbol");
	    
	    return agm.getAristas();
	  }

}