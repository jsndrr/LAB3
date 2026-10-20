/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Subclase para órdenes de Diagnóstico.
 */
public class Diagnostico extends OrdenServicio {

    public Diagnostico(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException("Las horas estimadas de un Diagnóstico no pueden superar 1.5 horas.");
        }
    }

    @Override
    public double calcularCostoServicio() {
        return (zona == Zona.METROPOLITANA) ? 150.0 : 225.0;
    }

    @Override
    public int obtenerTiempoAtencion() {
        return (zona == Zona.METROPOLITANA) ? 1 : 2;
    }

    @Override
    public boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla) {
        return true; // Compatible con cualquier cuadrilla
    }

    @Override
    public int obtenerMaxVisitas() {
        return 3;
    }

    @Override
    public String toString() {
        return String.format("[Diagnóstico] %s | Costo: Q%.2f | Tiempo Est: %d día(s)",
                super.toString(), calcularCostoServicio(), obtenerTiempoAtencion());
    }
}