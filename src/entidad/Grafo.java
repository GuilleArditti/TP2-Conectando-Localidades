package entidad;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Grafo {
	private int[][] matriz;
	private List<Arista> aristas;

	public Grafo(int vertices) {
		matriz = new int[vertices][vertices];
		aristas = new ArrayList<>();
	}

	public void agregarArista(int vertice1, int vertice2, int peso) {
		verificarDistintos(vertice1, vertice2);
		verificarVertice(vertice1);
		verificarVertice(vertice2);
		matriz[vertice1][vertice2] = peso;
		aristas.add(new Arista(vertice1, vertice2, peso));
	}
	
	/*
	 * Recibe una arista y la agrega al Grafo.
	 * Es utilizada por algoritmoDePrim() de la clase LogicaAGM
	 * cuando se genera el AGM. No es usado desde la interfaz.
	 */
	public void agregarArista(Arista arista) {
		verificarDistintos(arista.getOrigen(), arista.getDestino());
		verificarVertice(arista.getOrigen());
		verificarVertice(arista.getDestino());
		matriz[arista.getOrigen()][arista.getDestino()] = arista.getPeso();
		aristas.add(arista);
	}

	public void eliminarArista(int i, int j) {
		verificarVertice(i);
		verificarVertice(j);
		verificarDistintos(i, j);
		
		if (!existeArista(i, j))
			return;
		
		aristas.remove(new Arista(i, j, matriz[i][j]));

		matriz[i][j] = 0;
		matriz[j][i] = 0;
	}

	public boolean existeArista(int i, int j) {
		verificarVertice(i);
		verificarVertice(j);
		verificarDistintos(i, j);

		return matriz[i][j] != 0;
	}

	public int getPesoArista(int i, int j) {
		verificarVertice(i);
		verificarVertice(j);
		verificarDistintos(i, j);

		return matriz[i][j];
	}

	public int tamano() {
		return matriz.length;
	}

	public Set<Integer> vecinos(int i) {
		verificarVertice(i);
		Set<Integer> vecinos = new HashSet<>();

		for (int j = 0; j < this.tamano(); ++j) {
			if (i != j) {
				if (this.existeArista(i, j))
					vecinos.add(j);
			}
		}
		return vecinos;
	}

	public void mostrarVecinos(int i) {
		verificarVertice(i);
		for (int j = 0; j < this.tamano(); ++j) {
			if (i != j) {
				if (this.existeArista(i, j))
					System.out.print(j);
			}
		}
	}

	private void verificarVertice(int i) {
		if (i < 0)
			throw new IllegalArgumentException("El vertice no puede ser negativo: " + i);

		if (i >= matriz.length)
			throw new IllegalArgumentException("Los vertices deben estar entre 0 y |V|-1: " + i);
	}

	private void verificarDistintos(int i, int j) {
		if (i == j)
			throw new IllegalArgumentException("No se permiten loops: (" + i + ", " + j + ")");
	}
	
	public List<Arista> getAristas() {
		return this.aristas;
	}
}
