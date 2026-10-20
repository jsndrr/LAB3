import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * CC2008 - Laboratorio 3: TecniHogar
 * Driver program con menú de consola interactivo y precarga inicial.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaTecniHogar sistema = new SistemaTecniHogar();

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN TÉCNICA - TECNIHOGAR ===");
        precargarDatosIniciales();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> cotizarOAgregarOrdenMenu();
                case 2 -> registrarCuadrillaMenu();
                case 3 -> gestionarJornadasMenu();
                case 4 -> sistema.generarReporteOrdenes();
                case 5 -> sistema.generarReporteFinanciero();
                case 6 -> sistema.generarReporteJornadasAbiertas();
                case 7 -> {
                    salir = true;
                    System.out.println("Cerrando el sistema de TecniHogar... ¡Hasta luego!");
                }
                default -> System.out.println("Opción fuera de rango (1-7).");
            }
        }
        scanner.close();
    }

    private static void precargarDatosIniciales() {
        try {
            // Cuadrillas (al menos 3: 1 certificada, 2 no certificadas)
            sistema.registrarCuadrilla(new Cuadrilla("CUA-01", 8.0, true));  // Certificada en refrigeración
            sistema.registrarCuadrilla(new Cuadrilla("CUA-02", 6.0, false)); // No certificada
            sistema.registrarCuadrilla(new Cuadrilla("CUA-03", 7.5, false)); // No certificada

            // Órdenes (Al menos 2 por categoría)
            // Diagnóstico
            sistema.registrarOrden(new Diagnostico("DIAG-01", "Carlos López", "Zona 10", 1.0, Zona.METROPOLITANA));
            sistema.registrarOrden(new Diagnostico("DIAG-02", "Ana Gómez", "Mixco", 1.5, Zona.PERIFERICA));

            // Instalación (Incluye una donde horas por puntos superen horas estimadas: 5 puntos * 0.75 = 3.75h -> 4h vs 2.5h)
            sistema.registrarOrden(new Instalacion("INST-01", "Mario Rossi", "Zona 14", 2.5, Zona.METROPOLITANA, 5));
            sistema.registrarOrden(new Instalacion("INST-02", "Pedro Ruiz", "Carretera a El Salvador", 4.0, Zona.PERIFERICA, 3));

            // Instalación Eléctrica Certificada
            sistema.registrarOrden(new InstalacionElectricaCertificada("ELEC-01", "Lucía Morales", "Zona 15", 3.0, Zona.METROPOLITANA, 4, 2000.0));
            sistema.registrarOrden(new InstalacionElectricaCertificada("ELEC-02", "Jorge Ramos", "Villa Nueva", 2.0, Zona.PERIFERICA, 2, 1500.0));

            // Mantenimiento Aire (Solo Metropolitana)
            sistema.registrarOrden(new MantenimientoAire("AIRE-01", "Hospital Central", "Zona 11", 2.0, Zona.METROPOLITANA, 18000));
            sistema.registrarOrden(new MantenimientoAire("AIRE-02", "Oficinas Edificio 1", "Zona 4", 3.5, Zona.METROPOLITANA, 24000));

            System.out.println(">> Precarga completada exitosamente: 3 cuadrillas y 8 órdenes de servicio creadas.\n");
        } catch (Exception e) {
            System.out.println("Error en precarga: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Cotizar / Registrar Orden de Servicio");
        System.out.println("2. Registrar Cuadrilla");
        System.out.println("3. Gestión de Jornadas (Crear, Asignar, Retirar, Iniciar, Resultados)");
        System.out.println("4. Consultar Reporte de Órdenes");
        System.out.println("5. Consultar Reporte Financiero");
        System.out.println("6. Consultar Jornadas Abiertas");
        System.out.println("7. Salir");
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int val = scanner.nextInt();
                scanner.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un entero válido.");
                scanner.nextLine();
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double val = scanner.nextDouble();
                scanner.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un valor decimal válido.");
                scanner.nextLine();
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // --- OPCIONES SUBMENÚS ---

    private static void cotizarOAgregarOrdenMenu() {
        System.out.println("\n--- REGISTRO / COTIZACIÓN DE ÓRDENES ---");
        System.out.println("Categorías: 1. Diagnóstico | 2. Instalación | 3. Inst. Eléctrica Certificada | 4. Mantenimiento Aire");
        int tipo = leerEntero("Seleccione categoría (1-4): ");

        String codigo = leerTexto("Código único de orden: ");
        String cliente = leerTexto("Nombre del cliente: ");
        String direccion = leerTexto("Dirección: ");
        double horas = leerDouble("Horas estimadas (> 0): ");

        System.out.println("Zona: 1. Metropolitana | 2. Periférica");
        int zOpt = leerEntero("Seleccione zona: ");
        Zona zona = (zOpt == 2) ? Zona.PERIFERICA : Zona.METROPOLITANA;

        try {
            OrdenServicio nuevaOrden = null;
            switch (tipo) {
                case 1 -> nuevaOrden = new Diagnostico(codigo, cliente, direccion, horas, zona);
                case 2 -> {
                    int pts = leerEntero("Cantidad de puntos a instalar (> 0): ");
                    nuevaOrden = new Instalacion(codigo, cliente, direccion, horas, zona, pts);
                }
                case 3 -> {
                    int pts = leerEntero("Cantidad de puntos a instalar (> 0): ");
                    double mat = leerDouble("Valor declarado de materiales (Q): ");
                    nuevaOrden = new InstalacionElectricaCertificada(codigo, cliente, direccion, horas, zona, pts, mat);
                }
                case 4 -> {
                    int btu = leerEntero("Capacidad en BTU (9,000 - 60,000): ");
                    nuevaOrden = new MantenimientoAire(codigo, cliente, direccion, horas, zona, btu);
                }
                default -> {
                    System.out.println("Categoría inválida.");
                    return;
                }
            }

            System.out.println("\n" + sistema.cotizarOrden(nuevaOrden));
            System.out.print("¿Desea confirmar el registro y realizar el cobro? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                if (sistema.registrarOrden(nuevaOrden)) {
                    System.out.println("¡Orden REGISTRADA exitosamente!");
                } else {
                    System.out.println("Error: Ya existe una orden con ese código.");
                }
            } else {
                System.out.println("Cotización descartada. No se modificaron los ingresos.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        }
    }

    private static void registrarCuadrillaMenu() {
        System.out.println("\n--- REGISTRO DE CUADRILLA ---");
        String cod = leerTexto("Código de cuadrilla: ");
        double cap = leerDouble("Capacidad máxima de horas por jornada (> 0): ");
        System.out.print("¿Está certificada en refrigeración? (s/n): ");
        boolean cert = scanner.nextLine().trim().equalsIgnoreCase("s");

        try {
            if (sistema.registrarCuadrilla(new Cuadrilla(cod, cap, cert))) {
                System.out.println("¡Cuadrilla registrada exitosamente!");
            } else {
                System.out.println("Error: Código duplicado.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void gestionarJornadasMenu() {
        System.out.println("\n--- GESTIÓN DE JORNADAS ---");
        System.out.println("1. Crear Jornada");
        System.out.println("2. Asignar Orden a Jornada");
        System.out.println("3. Retirar Orden de Jornada");
        System.out.println("4. Iniciar Jornada");
        System.out.println("5. Registrar Resultado de Visita");
        int sub = leerEntero("Opción: ");

        switch (sub) {
            case 1 -> {
                String codJ = leerTexto("Código de jornada: ");
                String codC = leerTexto("Código de cuadrilla: ");
                if (sistema.crearJornada(codJ, codC)) System.out.println("Jornada creada en preparación.");
                else System.out.println("Error al crear la jornada (verifique la cuadrilla y jornadas abiertas).");
            }
            case 2 -> {
                String codJ = leerTexto("Código de jornada: ");
                String codO = leerTexto("Código de orden: ");
                if (sistema.asignarOrdenAJornada(codJ, codO)) System.out.println("Orden asignada a la jornada.");
                else System.out.println("Error: Incompatibilidad de cuadrilla, exceso de capacidad o estado no permitido.");
            }
            case 3 -> {
                String codJ = leerTexto("Código de jornada: ");
                String codO = leerTexto("Código de orden: ");
                if (sistema.retirarOrdenDeJornada(codJ, codO)) System.out.println("Orden retirada de la jornada.");
                else System.out.println("Error: La jornada no está en preparación o la orden no pertenecía a ella.");
            }
            case 4 -> {
                String codJ = leerTexto("Código de jornada: ");
                if (sistema.iniciarJornada(codJ)) System.out.println("Jornada iniciada. Las órdenes pasaron a estar en atención.");
                else System.out.println("Error: La jornada está vacía o no está en estado 'En Preparación'.");
            }
            case 5 -> {
                String codJ = leerTexto("Código de jornada: ");
                String codO = leerTexto("Código de orden: ");
                System.out.print("¿El servicio se completó con éxito? (s/n): ");
                boolean ok = scanner.nextLine().trim().equalsIgnoreCase("s");
                if (sistema.registrarResultadoVisita(codJ, codO, ok)) System.out.println("Resultado registrado.");
                else System.out.println("Error: La jornada u orden no están en atención.");
            }
        }
    }
}