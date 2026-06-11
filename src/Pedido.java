import java.util.Objects;

public class Pedido {
    private String numeroPedido;
    private String cliente;
    private String descripcion;
    private String categoria;
    private String prioridad;
    private String estado;
    private double total;

    public Pedido(String numeroPedido, String cliente, String descripcion, String categoria, String prioridad, double total) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.total = total;
        this.estado = "PENDIENTE";
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido='" + numeroPedido + "'" +
                ", cliente='" + cliente + "'" +
                ", descripcion='" + descripcion + "'" +
                ", categoria='" + categoria + "'" +
                ", prioridad='" + prioridad + "'" +
                ", estado='" + estado + "'" +
                ", total=" + total +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pedido)) {
            return false;
        }
        Pedido pedido = (Pedido) obj;
        return Objects.equals(numeroPedido, pedido.numeroPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }
}
