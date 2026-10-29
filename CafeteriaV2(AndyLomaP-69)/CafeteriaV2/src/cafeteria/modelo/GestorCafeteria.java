package cafeteria.modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import cafeteria.util.UtilGenerico;

/** Fachada del Modelo: unica clase que el delegado necesita conocer. */
public class GestorCafeteria {
    private final ArrayList<Producto> catalogo;
    private final ArrayList<Pedido> pedidos;
    private final GestorArchivos archivos = new GestorArchivos();

    public GestorCafeteria() {
        boolean habiaArchivo = archivos.existeProductos();
        catalogo = archivos.cargarProductos();
        if (!habiaArchivo && catalogo.isEmpty()) {
            cargarCatalogoInicial();
        }
        pedidos = archivos.cargarPedidos(catalogo);
    }

    public String getAdvertenciaCarga() { return archivos.getAdvertencia(); }

    private void cargarCatalogoInicial() {
        UtilGenerico.agregarVarios(catalogo,
                new Producto("P001", "Café americano", 1.50, 20),
                new Producto("P002", "Sándwich de pollo", 2.00, 15),
                new Producto("P003", "Jugo natural", 1.75, 12),
                new Producto("P004", "Brownie", 1.25, 8));
        try {
            archivos.guardarProductos(catalogo);
        } catch (IllegalStateException e) {
            // la aplicacion sigue funcionando aunque no se pueda guardar
        }
    }

    public ArrayList<Producto> listarProductos() { return new ArrayList<>(catalogo); }

    public Producto buscarProducto(String codigo) {
        for (Producto p : catalogo) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        }
        return null;
    }

    public void registrarProducto(String codigo, String nombre, double precio, int stock) {
        if (buscarProducto(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese código.");
        }
        if (precio < 0 || stock < 0) {
            throw new IllegalArgumentException("El precio y el stock deben ser números válidos.");
        }
        catalogo.add(new Producto(codigo, nombre, precio, stock));
        archivos.guardarProductos(catalogo);
    }

    public void editarProducto(String codigo, String nombre, double precio) {
        Producto p = buscarProducto(codigo);
        if (p == null) throw new IllegalArgumentException("El producto no existe.");
        if (precio < 0) throw new IllegalArgumentException("El precio y el stock deben ser números válidos.");
        p.setNombre(nombre);
        p.setPrecio(precio);
        archivos.guardarProductos(catalogo);
    }

    public void reponerStock(String codigo, int cantidad) {
        Producto p = buscarProducto(codigo);
        if (p == null) throw new IllegalArgumentException("El producto no existe.");
        p.aumentarStock(cantidad);
        archivos.guardarProductos(catalogo);
    }

    public Pedido crearNuevoPedido() {
        int max = 0;
        for (Pedido p : pedidos) {
            if (p.getIdPedido() > max) max = p.getIdPedido();
        }
        return new Pedido(max + 1);
    }

    /** Valida stock, descuenta, guarda el pedido y actualiza los archivos. */
    public void registrarPedido(Pedido pedido) {
        if (pedido == null || pedido.estaVacio()) {
            throw new IllegalArgumentException("Agregue al menos un producto al pedido.");
        }
        for (DetallePedido d : pedido.getDetalles()) {
            Producto p = buscarProducto(d.getProducto().getCodigo());
            if (p == null || d.getCantidad() > p.getStock()) {
                int disp = (p == null) ? 0 : p.getStock();
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + disp + " unidades.");
            }
        }
        for (DetallePedido d : pedido.getDetalles()) {
            d.getProducto().reducirStock(d.getCantidad());
        }
        pedido.setFecha(new Date());
        pedidos.add(pedido);
        archivos.guardarProductos(catalogo);
        archivos.guardarPedidos(pedidos);
    }

    private ArrayList<Pedido> pedidosDeHoy() {
        String hoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        ArrayList<Pedido> lista = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getSoloFecha().equals(hoy)) lista.add(p);
        }
        return lista;
    }

    /** Reporte de ventas del dia (texto con saltos de linea). */
    public String generarReporteVentas() {
        ArrayList<Pedido> hoy = pedidosDeHoy();
        if (hoy.isEmpty()) {
            return "Pedidos registrados hoy: 0\nTotal vendido hoy: $0.00\nProducto más vendido: (sin ventas)";
        }
        double subtotal = 0, iva = 0, total = 0;
        Double[] totales = new Double[hoy.size()];
        Map<String, Integer> unidades = new LinkedHashMap<>();
        for (int i = 0; i < hoy.size(); i++) {
            Pedido p = hoy.get(i);
            subtotal += p.calcularSubtotal();
            iva += p.calcularIva();
            total += p.calcularTotal();
            totales[i] = p.calcularTotal();
            for (DetallePedido d : p.getDetalles()) {
                unidades.merge(d.getNombreProducto(), d.getCantidad(), Integer::sum);
            }
        }
        // Metodo generico con Integer
        Integer mayorCantidad = UtilGenerico.obtenerMayor(unidades.values().toArray(new Integer[0]));
        String masVendido = "";
        for (Map.Entry<String, Integer> e : unidades.entrySet()) {
            if (e.getValue().equals(mayorCantidad)) { masVendido = e.getKey(); break; }
        }
        // Metodo generico con Double
        Double pedidoMayor = UtilGenerico.obtenerMayor(totales);

        return "Pedidos registrados hoy: " + hoy.size() + "\n"
                + String.format(Locale.US, "Total vendido hoy: $%.2f (subtotal $%.2f + IVA $%.2f)", total, subtotal, iva) + "\n"
                + "Producto más vendido: " + masVendido + " (" + mayorCantidad + " unidades)\n"
                + String.format(Locale.US, "Pedido de mayor valor del día: $%.2f", pedidoMayor);
    }

    /** Ranking de productos por unidades vendidas (historial completo). Fila: posicion, producto, unidades. */
    public ArrayList<String[]> productosMasDemandados() {
        Map<String, Integer> unidades = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            for (DetallePedido d : p.getDetalles()) {
                unidades.merge(d.getNombreProducto(), d.getCantidad(), Integer::sum);
            }
        }
        ArrayList<Map.Entry<String, Integer>> orden = new ArrayList<>(unidades.entrySet());
        orden.sort((a, b) -> b.getValue() - a.getValue());
        ArrayList<String[]> filas = new ArrayList<>();
        int pos = 1;
        for (Map.Entry<String, Integer> e : orden) {
            filas.add(new String[] { String.valueOf(pos++), e.getKey(), String.valueOf(e.getValue()) });
        }
        return filas;
    }
}
