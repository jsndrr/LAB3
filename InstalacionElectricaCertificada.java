/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Subclase especializada para Instalaciones Eléctricas Certificadas.
 */
public class InstalacionElectricaCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String cliente, String direccion, 
                                            double horasEstimadas, Zona zona, int puntos, double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);
        if (valorMateriales < 0) {
            throw new IllegalArgumentException("El valor declarado de materiales no puede ser negativo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCostoServicio() {
        double costoInstalacionBase = super.calcularCostoServicio();
        double costoCertificado = 120.0;
        double costoGarantiaMateriales = valorMateriales * 0.05;
        return costoInstalacionBase + costoCertificado + costoGarantiaMateriales;
    }

    @Override
    public int obtenerTiempoAtencion() {
        return super.obtenerTiempoAtencion() + 2; // 2 días más que una instalación normal
    }

    @Override
    public int obtenerMaxVisitas() {
        return 2; // Máximo 2 visitas permitidas
    }

    public double getValorMateriales() { return valorMateriales; }

    @Override
    public String toString() {
        return String.format("[Instalación Eléctrica Certificada] %s | Mat: Q%.2f | Costo Total: Q%.2f",
                super.toString(), valorMateriales, calcularCostoServicio());
    }
}