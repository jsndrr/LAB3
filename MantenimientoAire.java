/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Subclase para Mantenimiento de Aire Acondicionado.
 */
public class MantenimientoAire extends OrdenServicio {
    private int btu;

    public MantenimientoAire(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int btu) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("Los mantenimientos de aire acondicionado solo están permitidos en la zona metropolitana.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("La capacidad en BTU debe estar entre 9,000 y 60,000.");
        }
        this.btu = btu;
    }

    @Override
    public double calcularCostoServicio() {
        double tarifaBase = 100.0; // Siempre Metropolitana
        double cobroHoras = horasEstimadas * 110.0;
        double cargoRefrigerante = 80.0;
        return tarifaBase + cobroHoras + cargoRefrigerante;
    }

    @Override
    public int obtenerTiempoAtencion() {
        return 2; // Siempre 2 días
    }

    @Override
    public boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.isCertificadaRefrigeracion();
    }

    @Override
    public int obtenerMaxVisitas() {
        return 1; // 1 sola visita debido a la reserva de gas refrigerante
    }

    public int getBtu() { return btu; }

    @Override
    public String toString() {
        return String.format("[Mantenimiento Aire] %s | BTU: %d | Costo: Q%.2f",
                super.toString(), btu, calcularCostoServicio());
    }
}