package entidad;

public class Planificador {
	private int costoPorKilometro;
	private int porcentajeDeAumento;
	private int costoDistintaProvincia;
		
	public Planificador(int costoPorKilometro,int porcentajeDeAumento, int costoDistintaProvincia) {
		this.costoPorKilometro=costoPorKilometro;
		this.porcentajeDeAumento=porcentajeDeAumento;
		this.costoDistintaProvincia=costoDistintaProvincia;
	}

	public int getCostoPorKilometro() {
		return costoPorKilometro;
	}


	public void setCostoPorKilometro(int costoPorKilometro) {
		this.costoPorKilometro = costoPorKilometro;
	}


	public int getPorcentajeDeAumento() {
		return porcentajeDeAumento;
	}


	public void setPorcentajeDeAumento(int porcentajeDeAumento) {
		this.porcentajeDeAumento = porcentajeDeAumento;
	}


	public int getCostoDistintaProvincia() {
		return costoDistintaProvincia;
	}


	public void setCostoDistintaProvincia(int costoDistintaProvincia) {
		this.costoDistintaProvincia = costoDistintaProvincia;
	}
	
	public int calcularCostoEntrelocalidades(Ubicacion ubicacion1, Ubicacion ubicacion2) {
		double distancia= obtenerDistanciaEnKms(ubicacion1.getLatitud(), ubicacion1.getLongitud(),ubicacion2.getLatitud(),ubicacion2.getLongitud());
		if (distancia > 300 && ubicacion1.getProvincia().equals(ubicacion2.getProvincia())) {
			return (int) (distancia * (getCostoPorKilometro()
					+ getCostoPorKilometro() * (getPorcentajeDeAumento() * 0.01)));
		}
		if (distancia < 300 && ubicacion1.getProvincia().equals(ubicacion2.getProvincia())) {
			return (int) (distancia * getCostoPorKilometro());
		}
		if (distancia > 300 && !ubicacion1.getProvincia().equals(ubicacion2.getProvincia())) {
			return (int) (distancia* (getCostoPorKilometro()+ (getCostoPorKilometro() * (getPorcentajeDeAumento() * 0.01)))
							+ getCostoDistintaProvincia());
		}
		if (!ubicacion1.getProvincia().equals(ubicacion2.getProvincia())) {
			return (int) (distancia * getCostoPorKilometro()) + getCostoDistintaProvincia();
		}
		return 0;
	}
	
	private static double obtenerDistanciaEnKms(double lat1, double lng1, double lat2, double lng2) {
		double radioTierra = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double va1 = Math.pow(sindLat, 2)
				+ Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
		double distancia = radioTierra * va2;

		return distancia;
	}
	
	

}