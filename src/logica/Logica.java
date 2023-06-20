package logica;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import entidad.Arista;
import entidad.Grafo;
import entidad.Planificador;
import entidad.Ubicacion;
import entidad.Ubicaciones;

public class Logica {

	private Planificador planificador;
	private List<Ubicacion> ubicaciones;
	private Ubicaciones persistenciaDeUbicaciones;			
	private Grafo agm;

	public Logica() {
		persistenciaDeUbicaciones= new Ubicaciones();
		ubicaciones = new ArrayList<>();
		cargarUbicacionesDesdeTXT();
	}

	public Planificador definirCostos(int costoKM, int costoKM300KM, int costoDistintaProvincia) {
		planificador= new Planificador(costoKM,costoKM300KM,costoDistintaProvincia);
		return planificador;
	}
	
	public void agregarUbicacion(Ubicacion ubicacion) {
		if (!ubicaciones.contains(ubicacion)) {
			ubicaciones.add(ubicacion);
			persistenciaDeUbicaciones.agregar(ubicacion);
			guardaUbicacionesEnTXT();
		}
		else
			throw new RuntimeException("No se puede agregar esa ubicación porque ya fue agregada anteriormente.");
	}
	
	public void eliminarUbicacion(Ubicacion ubicacion) {
		ubicaciones.remove(ubicacion);
	}
	
	/*
	 * Crea un Grafo original con todas las ubicaciones agregadas desde la interfaz.
	 * Le pasa el Grafo original al Algoritmo de Prim.
	 * Recibe un Grafo AGM.
	 */
	
	public void generarSolucion() {
		Grafo grafoOrigen = volcarListaAGrafo();
		agm = algoritmoDePrim(grafoOrigen);
	}
	
	public String generarStringResultado() {
		int costoTotal = calcularCostoTotal();
		return escribirSolucion(costoTotal);
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
					+ ubicaciones.get(arista.getDestino()) + ". Costo: $ " + arista.getPeso() + "\n\n");
		
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
		if(ubicaciones.size()<2) {
			throw new RuntimeException("Para generar una conexión debe haber al menos 2 localidades ingresadas!");
		}
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

	/* Método que devuelve las coordenadas correspondientes a la solucion,
	 * para luego representarlas en el mapa de la interfaz. 
	 */
	
	public ArrayList<ArrayList<Coordinate>> coordenadasSolucion() {
		ArrayList<ArrayList<Coordinate>> conjuntoSolucion = new ArrayList<ArrayList<Coordinate>>();

		Ubicacion ubiOrigen;
		Ubicacion ubiDestino;

		Coordinate coord1;
		Coordinate coord2;

		for (Arista arista : agm.getAristas()) {
			ubiOrigen = ubicaciones.get(arista.getOrigen());
			ubiDestino = ubicaciones.get(arista.getDestino());

			coord1 = new Coordinate(ubiOrigen.getLatitud(), ubiOrigen.getLongitud());
			coord2 = new Coordinate(ubiDestino.getLatitud(), ubiDestino.getLongitud());

			ArrayList<Coordinate> conexion = new ArrayList<Coordinate>();
			conexion.add(coord1);
			conexion.add(coord2);
			conexion.add(coord2);

			conjuntoSolucion.add(conexion);
		}
		return conjuntoSolucion;
	}
	
	private void guardaUbicacionesEnTXT() {
		
		try {
			FileOutputStream fis = new FileOutputStream("ubicaciones.txt");
			ObjectOutputStream out = new ObjectOutputStream(fis);
			out.writeObject(persistenciaDeUbicaciones);
			out.close();
		} catch (Exception e) {
			System.out.println("No se pudo escribir el archivo: " + e.getMessage());
		}
	}
	
	public String cargarUbicacionesDesdeTXT() {
		try {
			FileInputStream fis = new FileInputStream("ubicaciones.txt");
			ObjectInputStream in = new ObjectInputStream(fis);
			persistenciaDeUbicaciones = (Ubicaciones) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println("No se pudo cargar el archivo: " + e.getMessage());
		}
		
		return persistenciaDeUbicaciones != null? persistenciaDeUbicaciones.toString() : ""; 
	}
	
	public ArrayList<Ubicacion> getHistorialDeUbicaciones(){
		ArrayList<Ubicacion> ubicacionesConocidas= new ArrayList<Ubicacion>();
		for(Ubicacion ubicacion: persistenciaDeUbicaciones.getUbicaciones()) {
			ubicacionesConocidas.add(ubicacion);
		}
		return ubicacionesConocidas;
	}
	
	public void ingresarUbicacionConocida(Ubicacion ubicacionConocida) {
		for(Ubicacion ubicacion: persistenciaDeUbicaciones.getUbicaciones()) {
			if(ubicacion.equals(ubicacionConocida)) {
				agregarUbicacion(ubicacion);
			}
		}		
	}

}