/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Representa una cuadrilla de técnicos.
 */
public class Cuadrilla {
    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la cuadrilla no puede estar vacío.");
        }
        if (capacidadHoras <= 0) {
            throw new IllegalArgumentException("La capacidad de horas debe ser mayor a cero.");
        }
        this.codigo = codigo.trim();
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }

    @Override
    public String toString() {
        return String.format("Cuadrilla: %-8s | Capacidad: %4.1fh | Certificada Refrigeración: %s",
                codigo, capacidadHoras, (certificadaRefrigeracion ? "Sí" : "No"));
    }
}