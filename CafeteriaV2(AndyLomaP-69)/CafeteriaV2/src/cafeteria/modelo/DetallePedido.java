package cafeteria.modelo;

public class DetallePedido {
    private final Producto producto;
    private final String nombreProducto;
    private final double precioUnitario;
    private int cantidad;

    /** Detalle nuevo: toma el precio actual del producto. */
    public DetallePedido(Producto producto, int cantidad) {
        this(producto, cantidad, producto.getPrecio());
    }

    /** Detalle cargado desde archivo: conserva el precio historico. */
    public DetallePedido(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.nombreProducto = producto.getNombre();
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() { return producto; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void aumentarCantidad(int extra) { cantidad += extra; }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }
}
