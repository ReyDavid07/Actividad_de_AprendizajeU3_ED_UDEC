import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class GestorRestaurante {
    private final List<Pedido> pedidos;
    private final Queue<Pedido> pendientes;
    private final Deque<Pedido> historial;
    private final Map<String, Pedido> indicePorNumero;

    public GestorRestaurante() {
        this.pedidos = new ArrayList<>();
        this.pendientes = new LinkedList<>();
        this.historial = new ArrayDeque<>();
        this.indicePorNumero = new LinkedHashMap<>();
    }

    public void registrarPedido(Pedido pedido) {
        validarPedido(pedido);
        if (indicePorNumero.containsKey(pedido.getNumeroPedido())) {
            throw new IllegalArgumentException("Ya existe un pedido con ese numero.");
        }
        pedido.setEstado("PENDIENTE");
        pedidos.add(pedido);
        pendientes.offer(pedido);
        indicePorNumero.put(pedido.getNumeroPedido(), pedido);
    }

    public void mostrarTodos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        pedidos.forEach(System.out::println);
    }

    public void mostrarPendientes() {
        if (pendientes.isEmpty()) {
            System.out.println("No hay pedidos pendientes.");
            return;
        }
        pendientes.forEach(System.out::println);
        System.out.println("Total pendientes: " + pendientes.size());
        System.out.println("Siguiente en cola: " + pendientes.peek());
    }

    public Pedido procesarSiguiente() {
        Pedido procesado = pendientes.poll();
        if (procesado == null) {
            throw new IllegalStateException("No hay pedidos pendientes.");
        }
        procesado.setEstado("PROCESADO");
        historial.push(procesado);
        return procesado;
    }

    public void mostrarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos procesados.");
            return;
        }
        historial.forEach(System.out::println);
        System.out.println("Ultimo procesado: " + historial.peek());
        System.out.println("Total procesados en historial: " + historial.size());
    }

    public Optional<Pedido> buscarPorNumeroMap(String numero) {
        if (!indicePorNumero.containsKey(numero)) {
            return Optional.empty();
        }
        return Optional.of(indicePorNumero.get(numero));
    }

    public Optional<Pedido> buscarPorClienteStream(String cliente) {
        return pedidos.stream()
                .filter(p -> p.getCliente().equalsIgnoreCase(cliente))
                .findFirst();
    }

    public List<Pedido> filtrarPorEstado(String estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public List<Pedido> filtrarPorCategoria(String categoria) {
        return pedidos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }

    public List<Pedido> ordenarPorCliente() {
        return pedidos.stream()
                .sorted(Comparator.comparing(Pedido::getCliente))
                .toList();
    }

    public List<Pedido> ordenarPorTotalDescendente() {
        return pedidos.stream()
                .sorted(Comparator.comparing(Pedido::getTotal).reversed())
                .toList();
    }

    public List<String> obtenerClientesConMap() {
        return pedidos.stream()
                .map(Pedido::getCliente)
                .distinct()
                .sorted()
                .toList();
    }

    public long contarPorEstado(String estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public Map<String, Long> estadisticasPorEstado() {
        return pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getEstado, Collectors.counting()));
    }

    public Map<String, Long> estadisticasPorCategoria() {
        return pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getCategoria, Collectors.counting()));
    }

    public Map<String, List<Pedido>> agruparPorCategoria() {
        return pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getCategoria));
    }

    public Map<String, Pedido> reconstruirIndiceConStream() {
        return pedidos.stream()
                .collect(Collectors.toMap(
                        Pedido::getNumeroPedido,
                        p -> p,
                        (existente, repetido) -> existente,
                        LinkedHashMap::new
                ));
    }

    public boolean existePendienteConStream() {
        return pedidos.stream().anyMatch(p -> p.getEstado().equalsIgnoreCase("PENDIENTE"));
    }

    public boolean todosTienenNumero() {
        return pedidos.stream().allMatch(p -> p.getNumeroPedido() != null && !p.getNumeroPedido().isBlank());
    }

    public boolean noHayCancelados() {
        return pedidos.stream().noneMatch(p -> p.getEstado().equalsIgnoreCase("CANCELADO"));
    }

    public void cancelarPendiente(String numero) {
        Pedido pedido = indicePorNumero.get(numero);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con ese numero.");
        }
        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes.");
        }
        pedido.setEstado("CANCELADO");
        pendientes.removeIf(p -> p.getNumeroPedido().equalsIgnoreCase(numero));
    }

    public Pedido deshacerUltimoProcesamiento() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("No hay pedidos procesados para deshacer.");
        }
        Pedido ultimo = historial.pop();
        ultimo.setEstado("PENDIENTE");
        pendientes.offer(ultimo);
        return ultimo;
    }

    public void mostrarCantidades() {
        System.out.println("Total en List: " + pedidos.size());
        System.out.println("Total en Queue pendientes: " + pendientes.size());
        System.out.println("Total en Deque historial: " + historial.size());
        System.out.println("Total en Map indice: " + indicePorNumero.size());
        System.out.println("Conteo por estado: " + estadisticasPorEstado());
        System.out.println("Hay pendientes: " + existePendienteConStream());
        System.out.println("Todos tienen numero: " + todosTienenNumero());
        System.out.println("No hay cancelados: " + noHayCancelados());
    }

    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isBlank()) {
            throw new IllegalArgumentException("El numero de pedido es obligatorio.");
        }
        if (pedido.getCliente() == null || pedido.getCliente().isBlank()) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }
        if (pedido.getTotal() < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo.");
        }
    }
}
