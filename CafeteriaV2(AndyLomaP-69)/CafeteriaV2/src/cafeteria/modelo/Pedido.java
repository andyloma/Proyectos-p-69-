package cafeteria.modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pedido {
    public static final double IVA = 0.15;
    public static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm";

    private final int idPedido;
    private Date fecha;
    private final ArrayList<DetallePedido> detalles = new ArrayList<>();

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.fecha = new Date();
    }

    public int getIdPedido() { return idPedido; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public ArrayList<DetallePedido> getDetalles() { return detalles; }

    public String getFechaTexto() {
        return new SimpleDateFormat(FORMATO_FECHA).format(fecha);
    }

    public String getSoloFecha() {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    /** Si el producto ya esta en el pedido, suma la cantidad; si no, agrega una linea. */
    public void agregarDetalle(DetallePedido nuevo) {
        for (DetallePedido d : detalles) {
            if (d.getProducto().getCodigo().equals(nuevo.getProducto().getCodigo())
                    && d.getPrecioUnitario() == nuevo.getPrecioUnitario()) {
                d.aumentarCantidad(nuevo.getCantidad());
                return;
            }
        }
        detalles.add(nuevo);
    }

    public void quitarDetalle(int indice) {
        if (indice >= 0 && indice < detalles.size()) {
            detalles.remove(indice);
        }
    }

    public int getCantidadDe(String codigo) {
        int total = 0;
        for (DetallePedido d : detalles) {
            if (d.getProducto().getCodigo().equals(codigo)) {
                total += d.getCantidad();
            }
        }
        return total;
    }

    public boolean estaVacio() { return detalles.isEmpty(); }

    public double calcularSubtotal() {
        double suma = 0;
        for (DetallePedido d : detalles) {
            suma += d.calcularSubtotal();
        }
        return suma;
    }

    public double calcularIva() { return calcularSubtotal() * IVA; }

    public double calcularTotal() { return calcularSubtotal() + calcularIva(); }
}
