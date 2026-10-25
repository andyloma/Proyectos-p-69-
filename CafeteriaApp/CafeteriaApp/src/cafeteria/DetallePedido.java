package cafeteria;

public class DetallePedido {

    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String toString() {
        return producto.getNombre() + " x" + cantidad + " = $" + String.format("%.2f", subtotal);
    }
}
