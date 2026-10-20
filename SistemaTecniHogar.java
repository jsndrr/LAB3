import java.util.ArrayList;

/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Clase controlador de dominio para la gestión central del negocio.
 */
public class SistemaTecniHogar {
    private ArrayList<OrdenServicio> ordenes;
    private ArrayList<Cuadrilla> cuadrillas;
    private ArrayList<Jornada> jornadas;
    private double ingresosRegistro;
    private double ingresosVisitasAdicionales;

    public SistemaTecniHogar() {
        this.ordenes = new ArrayList<>();
        this.cuadrillas = new ArrayList<>();
        this.jornadas = new ArrayList<>();
        this.ingresosRegistro = 0.0;
        this.ingresosVisitasAdicionales = 0.0;
    }

    // --- REGISTROS Y BÚSQUEDAS ---

    public boolean registrarOrden(OrdenServicio orden) {
        if (orden == null || buscarOrden(orden.getCodigo()) != null) {
            return false;
        }
        ordenes.add(orden);
        ingresosRegistro += orden.calcularCostoServicio(); // Se cobra el monto completo al registrar
        return true;
    }

    public boolean registrarCuadrilla(Cuadrilla cuadrilla) {
        if (cuadrilla == null || buscarCuadrilla(cuadrilla.getCodigo()) != null) {
            return false;
        }
        return cuadrillas.add(cuadrilla);
    }

    public OrdenServicio buscarOrden(String codigo) {
        if (codigo == null) return null;
        for (OrdenServicio o : ordenes) {
            if (o.getCodigo().equalsIgnoreCase(codigo.trim())) return o;
        }
        return null;
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        if (codigo == null) return null;
        for (Cuadrilla c : cuadrillas) {
            if (c.getCodigo().equalsIgnoreCase(codigo.trim())) return c;
        }
        return null;
    }

    public Jornada buscarJornada(String codigo) {
        if (codigo == null) return null;
        for (Jornada j : jornadas) {
            if (j.getCodigoJornada().equalsIgnoreCase(codigo.trim())) return j;
        }
        return null;
    }

    public String cotizarOrden(OrdenServicio orden) {
        if (orden == null) return "Orden inválida para cotización.";
        return String.format("COTIZACIÓN DE SERVICIO\nTipo: %s\nCliente: %s\nCosto Total Estimado: Q%.2f\nTiempo Estimado de Atención: %d día(s) hábil(es)\n(Esta cotización no altera los ingresos)",
                orden.getClass().getSimpleName(), orden.getCliente(), orden.calcularCostoServicio(), orden.obtenerTiempoAtencion());
    }

    // --- GESTIÓN DE JORNADAS ---

    public boolean crearJornada(String codigoJornada, String codigoCuadrilla) {
        Cuadrilla c = buscarCuadrilla(codigoCuadrilla);
        if (c == null || buscarJornada(codigoJornada) != null) {
            return false;
        }
        // Verificar que la cuadrilla no tenga otra jornada sin cerrar
        for (Jornada j : jornadas) {
            if (j.getCuadrilla().getCodigo().equalsIgnoreCase(codigoCuadrilla) && j.getEstado() != EstadoJornada.CERRADA) {
                return false;
            }
        }
        return jornadas.add(new Jornada(codigoJornada, c));
    }

    public boolean asignarOrdenAJornada(String codigoJornada, String codigoOrden) {
        Jornada j = buscarJornada(codigoJornada);
        OrdenServicio o = buscarOrden(codigoOrden);
        if (j == null || o == null) return false;
        return j.agregarOrden(o);
    }

    public boolean retirarOrdenDeJornada(String codigoJornada, String codigoOrden) {
        Jornada j = buscarJornada(codigoJornada);
        if (j == null) return false;
        return j.retirarOrden(codigoOrden);
    }

    public boolean iniciarJornada(String codigoJornada) {
        Jornada j = buscarJornada(codigoJornada);
        if (j == null) return false;
        double recargoVisitas = j.iniciarJornada();
        if (recargoVisitas >= 0) {
            ingresosVisitasAdicionales += recargoVisitas;
            return true;
        }
        return false;
    }

    public boolean registrarResultadoVisita(String codigoJornada, String codigoOrden, boolean completada) {
        Jornada j = buscarJornada(codigoJornada);
        OrdenServicio o = buscarOrden(codigoOrden);

        if (j == null || o == null || j.getEstado() != EstadoJornada.EN_ATENCION || o.getEstado() != EstadoOrden.EN_ATENCION) {
            return false;
        }

        if (completada) {
            o.setEstado(EstadoOrden.COMPLETADA);
        } else {
            if (o.getVisitasRealizadas() >= o.obtenerMaxVisitas()) {
                o.setEstado(EstadoOrden.CANCELADA); // Agotó visitas permitidas
            } else {
                o.setEstado(EstadoOrden.PENDIENTE); // Vuelve a pendiente
            }
        }

        // Si todas las órdenes de la jornada tienen resultado final, cerrar la jornada
        j.cerrarJornada();
        return true;
    }

    // --- REPORTES ---

    public void generarReporteOrdenes() {
        System.out.println("\n========================================================");
        System.out.println("            REPORTE GENERAL DE ÓRDENES                  ");
        System.out.println("========================================================");
        if (ordenes.isEmpty()) {
            System.out.println("No hay órdenes registradas.");
            return;
        }
        for (OrdenServicio o : ordenes) {
            System.out.println(o.toString());
        }
    }

    public void generarReporteFinanciero() {
        System.out.println("\n========================================================");
        System.out.println("            REPORTE FINANCIERO Y DE INGRESOS            ");
        System.out.println("========================================================");
        System.out.printf("Ingresos por Registros de Orden:   Q%.2f\n", ingresosRegistro);
        System.out.printf("Ingresos por Visitas Adicionales: Q%.2f\n", ingresosVisitasAdicionales);
        System.out.println("--------------------------------------------------------");
        System.out.printf("INGRESOS TOTALES ACUMULADOS:       Q%.2f\n", (ingresosRegistro + ingresosVisitasAdicionales));
        System.out.println("========================================================\n");
    }

    public void generarReporteJornadasAbiertas() {
        System.out.println("\n========================================================");
        System.out.println("            JORNADAS ABIERTAS Y EN ATENCIÓN             ");
        System.out.println("========================================================");
        boolean hayAbiertas = false;
        for (Jornada j : jornadas) {
            if (j.getEstado() != EstadoJornada.CERRADA) {
                hayAbiertas = true;
                System.out.printf("Jornada: %-6s | Cuadrilla: %-6s | Horas Asignadas: %4.1fh | Disponible: %4.1fh | Estado: %s\n",
                        j.getCodigoJornada(), j.getCuadrilla().getCodigo(), j.calcularHorasAsignadas(), j.calcularCapacidadDisponible(), j.getEstado());
            }
        }
        if (!hayAbiertas) {
            System.out.println("No hay jornadas abiertas en preparación ni en atención.");
        }
        System.out.println("========================================================\n");
    }

    public ArrayList<OrdenServicio> getOrdenes() { return ordenes; }
    public ArrayList<Cuadrilla> getCuadrillas() { return cuadrillas; }
    public ArrayList<Jornada> getJornadas() { return jornadas; }
}