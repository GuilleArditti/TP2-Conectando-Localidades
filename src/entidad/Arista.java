package entidad;

public class Arista {
	private int origen;
	private int destino;
	private int peso;

	public Arista(int origen, int destino, int peso) {
		this.origen = origen;
		this.destino = destino;
		this.peso = peso;
	}

	public int getOrigen() {
		return origen;
	}

	public void setOrigen(int origen) {
		this.origen = origen;
	}

	public int getDestino() {
		return destino;
	}

	public void setDestino(int destino) {
		this.destino = destino;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Arista [origen=" + origen + ", destino=" + destino + ", peso=" + peso + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Arista)) {
			return false;
		}
		Arista other = (Arista) obj;
		return destino == other.destino && origen == other.origen && peso == other.peso;
	}
}
