/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Clase abstracta base para las órdenes de servicio técnico.
 */
public abstract class OrdenServicio {
    protected String codigo;
    protected String cliente;
    protected String direccion;
    protected double horasEstimadas;
    protected Zona zona;
    protected EstadoOrden estado;
    protected int visitasRealizadas;

    public OrdenServicio(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la orden no puede estar vacío.");
        }
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas estimadas deben ser mayores a cero.");
        }
        this.codigo = codigo.trim();
        this.cliente = cliente != null ? cliente.trim() : "";
        this.direccion = direccion != null ? direccion.trim() : "";
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
    }

    // --- MÉTODOS ABSTRACTOS / POLIMÓRFICOS ---
    public abstract double calcularCostoServicio();
    public abstract int obtenerTiempoAtencion();
    public abstract boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla);
    public abstract int obtenerMaxVisitas();

    // --- MÉTODOS CONCRETOS DE ESTADO ---
    public void incrementarVisita() {
        this.visitasRealizadas++;
    }

    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getDireccion() { return direccion; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public Zona getZona() { return zona; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }

    @Override
    public String toString() {
        return String.format("Código: %-8s | Cliente: %-15s | Horas Est: %4.1fh | Zona: %-13s | Estado: %-11s | Visitas: %d/%d",
                codigo, cliente, horasEstimadas, zona, estado, visitasRealizadas, obtenerMaxVisitas());
    }
}