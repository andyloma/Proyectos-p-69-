package cafeteria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Pedido {

    public static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private int idPedido;
    private Date fecha;
    private ArrayList<DetallePedido> detalles;
    private double total;

    public Pedido(int idPedido, Date fecha) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.detalles = new ArrayList<DetallePedido>();
        this.total = 0;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double suma = 0;
        for (DetallePedido d : detalles) {
            suma = suma + d.getSubtotal();
        }
        total = suma;
        return total;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    public double getTotal() {
        return total;
    }

    public String getFechaTexto() {
        return FORMATO_FECHA.format(fecha);
    }
}
