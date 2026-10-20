import java.util.ArrayList;

/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Modela la jornada laboral de una cuadrilla.
 */
public class Jornada {
    private String codigoJornada;
    private Cuadrilla cuadrilla;
    private ArrayList<OrdenServicio> ordenes;
    private EstadoJornada estado;

    public Jornada(String codigoJornada, Cuadrilla cuadrilla) {
        if (codigoJornada == null || codigoJornada.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la jornada no puede estar vacío.");
        }
        if (cuadrilla == null) {
            throw new IllegalArgumentException("La jornada requiere una cuadrilla asignada.");
        }
        this.codigoJornada = codigoJornada.trim();
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.estado = EstadoJornada.EN_PREPARACION;
    }

    public double calcularHorasAsignadas() {
        double total = 0;
        for (OrdenServicio o : ordenes) {
            total += o.getHorasEstimadas(); // Para capacidad se usan las horas estimadas
        }
        return total;
    }

    public double calcularCapacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - calcularHorasAsignadas();
    }

    public boolean agregarOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            return false;
        }
        if (!orden.esCompatibleConCuadrilla(cuadrilla)) {
            return false;
        }
        if (orden.getHorasEstimadas() > calcularCapacidadDisponible()) {
            return false;
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            return false;
        }

        ordenes.add(orden);
        orden.setEstado(EstadoOrden.ASIGNADA);
        return true;
    }

    public boolean retirarOrden(String codigoOrden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            return false;
        }
        for (OrdenServicio o : ordenes) {
            if (o.getCodigo().equalsIgnoreCase(codigoOrden.trim())) {
                ordenes.remove(o);
                o.setEstado(EstadoOrden.PENDIENTE);
                return true;
            }
        }
        return false;
    }

    /**
     * Inicia la jornada, pasa las órdenes a EN_ATENCION, incrementa las visitas
     * y retorna los ingresos por visitas adicionales (Q40 desde la 2da visita).
     */
    public double iniciarJornada() {
        if (estado != EstadoJornada.EN_PREPARACION || ordenes.isEmpty()) {
            return -1.0; // No se puede iniciar
        }

        estado = EstadoJornada.EN_ATENCION;
        double ingresosVisitasAdicionalesJornada = 0.0;

        for (OrdenServicio o : ordenes) {
            o.setEstado(EstadoOrden.EN_ATENCION);
            o.incrementarVisita();
            
            // Si es la segunda visita o posterior, genera un recargo de Q40
            if (o.getVisitasRealizadas() > 1) {
                ingresosVisitasAdicionalesJornada += 40.0;
            }
        }

        return ingresosVisitasAdicionalesJornada;
    }

    public boolean cerrarJornada() {
        if (estado != EstadoJornada.EN_ATENCION) {
            return false;
        }
        // Verificar que todas las órdenes tengan resultado (COMPLETADA o PENDIENTE/CANCELADA tras fallo)
        for (OrdenServicio o : ordenes) {
            if (o.getEstado() == EstadoOrden.EN_ATENCION) {
                return false; // Aún hay órdenes pendientes de resultado
            }
        }
        estado = EstadoJornada.CERRADA;
        return true;
    }

    public String getCodigoJornada() { return codigoJornada; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public ArrayList<OrdenServicio> getOrdenes() { return ordenes; }
    public EstadoJornada getEstado() { return estado; }

    @Override
    public String toString() {
        return String.format("Jornada: %-8s | Cuadrilla: %-8s | Asignadas: %4.1fh/%4.1fh | Estado: %s",
                codigoJornada, cuadrilla.getCodigo(), calcularHorasAsignadas(), cuadrilla.getCapacidadHoras(), estado);
    }
}