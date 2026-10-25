package cafeteria;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class GestorCafeteria {

    private ArrayList<Producto> catalogo;
    private ArrayList<Pedido> pedidos;
    private int siguienteIdPedido;

    private final String ARCHIVO_PRODUCTOS = "productos.txt";
    private final String ARCHIVO_PEDIDOS = "pedidos.txt";

    public GestorCafeteria() {
        catalogo = new ArrayList<Producto>();
        pedidos = new ArrayList<Pedido>();
        siguienteIdPedido = 1;
        cargarDatos();
    }

    public ArrayList<Producto> listarProductos() {
        return catalogo;
    }

    public Producto buscarProducto(String codigo) {
        for (Producto p : catalogo) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public boolean registrarProducto(String codigo, String nombre, double precio, int stock) {
        if (buscarProducto(codigo) != null) {
            return false;
        }
        Producto nuevo = new Producto(codigo, nombre, precio, stock);
        catalogo.add(nuevo);
        guardarDatos();
        return true;
    }

    public boolean reponerStock(String codigo, int cantidad) {
        Producto p = buscarProducto(codigo);
        if (p == null) {
            return false;
        }
        p.aumentarStock(cantidad);
        guardarDatos();
        return true;
    }

    public Pedido crearNuevoPedido() {
        Pedido pedido = new Pedido(siguienteIdPedido, new Date());
        siguienteIdPedido = siguienteIdPedido + 1;
        return pedido;
    }

    public String registrarPedido(Pedido pedido) {
        if (pedido.getDetalles().isEmpty()) {
            return "El pedido no tiene productos, no se registro.";
        }

        for (DetallePedido d : pedido.getDetalles()) {
            if (d.getProducto().getStock() < d.getCantidad()) {
                return "No hay stock suficiente de " + d.getProducto().getNombre() + " para completar el pedido.";
            }
        }

        for (DetallePedido d : pedido.getDetalles()) {
            d.getProducto().reducirStock(d.getCantidad());
        }

        pedido.calcularTotal();
        pedidos.add(pedido);
        guardarDatos();
        return null;
    }

    public String generarReporteVentas() {
        String hoy = Pedido.FORMATO_FECHA.format(new Date()).substring(0, 10);
        double totalVendidoHoy = 0;
        int pedidosHoy = 0;
        ArrayList<String> codigosProductos = new ArrayList<String>();
        ArrayList<String> nombresProductos = new ArrayList<String>();
        ArrayList<Integer> cantidadesVendidas = new ArrayList<Integer>();

        for (Pedido pedido : pedidos) {
            String fechaPedido = pedido.getFechaTexto().substring(0, 10);
            if (fechaPedido.equals(hoy)) {
                pedidosHoy = pedidosHoy + 1;
                totalVendidoHoy = totalVendidoHoy + pedido.getTotal();

                for (DetallePedido d : pedido.getDetalles()) {
                    String codigo = d.getProducto().getCodigo();
                    int indice = codigosProductos.indexOf(codigo);
                    if (indice == -1) {
                        codigosProductos.add(codigo);
                        nombresProductos.add(d.getProducto().getNombre());
                        cantidadesVendidas.add(d.getCantidad());
                    } else {
                        int cantidadActual = cantidadesVendidas.get(indice);
                        cantidadesVendidas.set(indice, cantidadActual + d.getCantidad());
                    }
                }
            }
        }

        if (pedidosHoy == 0) {
            return "Todavia no se han registrado pedidos hoy.";
        }

        int indiceMasVendido = 0;
        for (int i = 1; i < cantidadesVendidas.size(); i++) {
            if (cantidadesVendidas.get(i) > cantidadesVendidas.get(indiceMasVendido)) {
                indiceMasVendido = i;
            }
        }

        String reporte = "Pedidos registrados hoy: " + pedidosHoy + "\n";
        reporte = reporte + "Total vendido hoy: $" + String.format("%.2f", totalVendidoHoy) + "\n";
        reporte = reporte + "Producto mas vendido: " + nombresProductos.get(indiceMasVendido)
                + " (" + cantidadesVendidas.get(indiceMasVendido) + " unidades)";
        return reporte;
    }

    public void guardarDatos() {
        guardarProductosEnArchivo();
        guardarPedidosEnArchivo();
    }

    public void cargarDatos() {
        cargarProductosDesdeArchivo();
        cargarPedidosDesdeArchivo();
    }

    private void guardarProductosEnArchivo() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PRODUCTOS, false));
            for (Producto p : catalogo) {
                bw.write(p.aLineaArchivo());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar productos.txt: " + e.getMessage());
        }
    }

    private void guardarPedidosEnArchivo() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_PEDIDOS, false));
            for (Pedido pedido : pedidos) {
                for (DetallePedido d : pedido.getDetalles()) {
                    String linea = pedido.getIdPedido() + ";" + pedido.getFechaTexto() + ";"
                            + d.getProducto().getCodigo() + ";" + d.getProducto().getNombre() + ";"
                            + d.getCantidad() + ";" + d.getProducto().getPrecio() + ";"
                            + String.format("%.2f", d.getSubtotal());
                    bw.write(linea);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar pedidos.txt: " + e.getMessage());
        }
    }

    private void cargarProductosDesdeArchivo() {
        File archivo = new File(ARCHIVO_PRODUCTOS);
        if (!archivo.exists()) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String codigo = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    int stock = Integer.parseInt(partes[3]);
                    catalogo.add(new Producto(codigo, nombre, precio, stock));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al leer productos.txt: " + e.getMessage());
        }
    }

    private void cargarPedidosDesdeArchivo() {
        File archivo = new File(ARCHIVO_PEDIDOS);
        if (!archivo.exists()) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(";");
                if (partes.length == 7) {
                    int idPedido = Integer.parseInt(partes[0]);
                    String fechaTexto = partes[1];
                    String codigoProducto = partes[2];
                    String nombreProducto = partes[3];
                    int cantidad = Integer.parseInt(partes[4]);
                    double precioUnitario = Double.parseDouble(partes[5]);

                    Pedido pedido = buscarPedido(idPedido);
                    if (pedido == null) {
                        Date fecha;
                        try {
                            fecha = Pedido.FORMATO_FECHA.parse(fechaTexto);
                        } catch (ParseException e) {
                            fecha = new Date();
                        }
                        pedido = new Pedido(idPedido, fecha);
                        pedidos.add(pedido);
                    }

                    Producto producto = buscarProducto(codigoProducto);
                    if (producto == null) {
                        producto = new Producto(codigoProducto, nombreProducto, precioUnitario, 0);
                    }

                    DetallePedido detalle = new DetallePedido(producto, cantidad);
                    pedido.agregarDetalle(detalle);
                    pedido.calcularTotal();

                    if (idPedido >= siguienteIdPedido) {
                        siguienteIdPedido = idPedido + 1;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error al leer pedidos.txt: " + e.getMessage());
        }
    }

    private Pedido buscarPedido(int idPedido) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == idPedido) {
                return p;
            }
        }
        return null;
    }
}
