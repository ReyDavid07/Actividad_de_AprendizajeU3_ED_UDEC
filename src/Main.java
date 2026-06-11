import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorRestaurante gestor = new GestorRestaurante();

    public static void main(String[] args) {
        cargarDatosIniciales();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> registrarPedido();
                    case 2 -> gestor.mostrarTodos();
                    case 3 -> gestor.mostrarPendientes();
                    case 4 -> System.out.println("Procesado: " + gestor.procesarSiguiente());
                    case 5 -> gestor.mostrarHistorial();
                    case 6 -> buscarPorNumero();
                    case 7 -> buscarPorClienteStream();
                    case 8 -> filtrarElementosStream();
                    case 9 -> ordenarElementosStream();
                    case 10 -> verEstadisticas();
                    case 11 -> verAgrupamientos();
                    case 12 -> cancelarPedido();
                    case 13 -> System.out.println("Devuelto a pendientes: " + gestor.deshacerUltimoProcesamiento());
                    case 14 -> gestor.mostrarCantidades();
                    case 15 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 15);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== SISTEMA RESTAURANTE CON COLECCIONES JAVA =====");
        System.out.println("1. Registrar elemento");
        System.out.println("2. Ver todos los elementos registrados");
        System.out.println("3. Ver elementos pendientes");
        System.out.println("4. Procesar siguiente elemento");
        System.out.println("5. Ver historial de elementos procesados");
        System.out.println("6. Buscar elemento por identificador usando Map");
        System.out.println("7. Buscar elemento por otro criterio usando Stream");
        System.out.println("8. Filtrar elementos usando Stream");
        System.out.println("9. Ordenar elementos usando Stream");
        System.out.println("10. Ver estadisticas usando Stream y Map");
        System.out.println("11. Ver agrupamientos usando Stream y Map");
        System.out.println("12. Cancelar elemento pendiente");
        System.out.println("13. Deshacer ultimo procesamiento");
        System.out.println("14. Ver cantidad de elementos");
        System.out.println("15. Salir");
    }

    private static void registrarPedido() {
        String numero = leerTexto("Numero de pedido: ");
        String cliente = leerTexto("Cliente: ");
        String descripcion = leerTexto("Descripcion: ");
        String categoria = leerTexto("Categoria (Mesa/Domicilio/Para llevar): ");
        String prioridad = leerTexto("Prioridad (Alta/Media/Baja): ");
        double total = leerDouble("Total: ");
        gestor.registrarPedido(new Pedido(numero, cliente, descripcion, categoria, prioridad, total));
        System.out.println("Pedido registrado en List, Queue y Map.");
    }

    private static void buscarPorNumero() {
        String numero = leerTexto("Numero de pedido: ");
        Optional<Pedido> pedido = gestor.buscarPorNumeroMap(numero);
        System.out.println(pedido.map(Object::toString).orElse("No encontrado."));
    }

    private static void buscarPorClienteStream() {
        String cliente = leerTexto("Nombre del cliente: ");
        Optional<Pedido> pedido = gestor.buscarPorClienteStream(cliente);
        System.out.println(pedido.map(Object::toString).orElse("No encontrado por Stream."));
    }

    private static void filtrarElementosStream() {
        System.out.println("1. Filtrar por estado");
        System.out.println("2. Filtrar por categoria");
        int opcion = leerEntero("Seleccione: ");
        List<Pedido> resultado;
        if (opcion == 1) {
            String estado = leerTexto("Estado: ");
            resultado = gestor.filtrarPorEstado(estado);
        } else {
            String categoria = leerTexto("Categoria: ");
            resultado = gestor.filtrarPorCategoria(categoria);
        }
        resultado.forEach(System.out::println);
    }

    private static void ordenarElementosStream() {
        System.out.println("1. Ordenar por cliente ascendente");
        System.out.println("2. Ordenar por total descendente");
        int opcion = leerEntero("Seleccione: ");
        List<Pedido> resultado = opcion == 1 ? gestor.ordenarPorCliente() : gestor.ordenarPorTotalDescendente();
        resultado.forEach(System.out::println);
    }

    private static void verEstadisticas() {
        System.out.println("Conteo por estado: " + gestor.estadisticasPorEstado());
        System.out.println("Conteo por categoria: " + gestor.estadisticasPorCategoria());
        System.out.println("Total pendientes usando count(): " + gestor.contarPorEstado("PENDIENTE"));
        System.out.println("Clientes generados con map(): " + gestor.obtenerClientesConMap());
        System.out.println("Indice reconstruido con Collectors.toMap(): " + gestor.reconstruirIndiceConStream().keySet());
    }

    private static void verAgrupamientos() {
        Map<String, List<Pedido>> agrupado = gestor.agruparPorCategoria();
        agrupado.forEach((categoria, lista) -> {
            System.out.println("Categoria: " + categoria);
            lista.forEach(System.out::println);
        });
    }

    private static void cancelarPedido() {
        String numero = leerTexto("Numero del pedido pendiente a cancelar: ");
        gestor.cancelarPendiente(numero);
        System.out.println("Pedido cancelado. Sigue en List y Map, pero sale de Queue.");
    }

    private static void cargarDatosIniciales() {
        gestor.registrarPedido(new Pedido("P001", "Ana Torres", "Hamburguesa, papas y jugo", "Mesa", "Media", 28500));
        gestor.registrarPedido(new Pedido("P002", "Luis Mora", "Pizza personal y gaseosa", "Domicilio", "Alta", 32000));
        gestor.registrarPedido(new Pedido("P003", "Marta Rios", "Ensalada y limonada", "Para llevar", "Baja", 18500));
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.print("El dato no puede estar vacio. " + mensaje);
            texto = scanner.nextLine().trim();
        }
        return texto;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(scanner.nextLine().trim());
                if (valor < 0) {
                    System.out.println("El total no puede ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un valor numerico.");
            }
        }
    }
}
