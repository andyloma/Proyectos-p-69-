package cafeteria.modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/** Persistencia en archivos de texto plano (mismo formato que la version 1.0). */
public class GestorArchivos {
    public static final String ARCHIVO_PRODUCTOS = "productos.txt";
    public static final String ARCHIVO_PEDIDOS = "pedidos.txt";

    private String advertencia = null;

    public String getAdvertencia() { return advertencia; }

    public boolean existeProductos() { return new File(ARCHIVO_PRODUCTOS).exists(); }

    public void guardarProductos(ArrayList<Producto> productos) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(new File(ARCHIVO_PRODUCTOS), StandardCharsets.UTF_8))) {
            for (Producto p : productos) {
                bw.write(p.getCodigo() + ";" + p.getNombre() + ";"
                        + String.format(Locale.US, "%.2f", p.getPrecio()) + ";" + p.getStock());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar productos.txt: " + e.getMessage());
        }
    }

    public void guardarPedidos(ArrayList<Pedido> pedidos) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(new File(ARCHIVO_PEDIDOS), StandardCharsets.UTF_8))) {
            for (Pedido ped : pedidos) {
                for (DetallePedido d : ped.getDetalles()) {
                    bw.write(ped.getIdPedido() + ";" + ped.getFechaTexto() + ";"
                            + d.getProducto().getCodigo() + ";" + d.getNombreProducto() + ";"
                            + d.getCantidad() + ";"
                            + String.format(Locale.US, "%.2f", d.getPrecioUnitario()) + ";"
                            + String.format(Locale.US, "%.2f", d.calcularSubtotal()));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar pedidos.txt: " + e.getMessage());
        }
    }

    public ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        File f = new File(ARCHIVO_PRODUCTOS);
        if (!f.exists()) {
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { linea = linea.replace("\uFEFF", ""); primera = false; }
                if (linea.trim().isEmpty()) continue;
                String[] c = linea.split(";");
                try {
                    if (c.length < 4) throw new NumberFormatException();
                    lista.add(new Producto(c[0].trim(), c[1].trim(),
                            Double.parseDouble(c[2].trim().replace(',', '.')),
                            Integer.parseInt(c[3].trim())));
                } catch (NumberFormatException ex) {
                    advertencia = "Se omitieron lineas invalidas en productos.txt.";
                }
            }
        } catch (IOException e) {
            advertencia = "No se pudo leer productos.txt: " + e.getMessage();
        }
        return lista;
    }

    public ArrayList<Pedido> cargarPedidos(ArrayList<Producto> catalogo) {
        ArrayList<Pedido> lista = new ArrayList<>();
        File f = new File(ARCHIVO_PEDIDOS);
        if (!f.exists()) {
            return lista;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Pedido.FORMATO_FECHA);
        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { linea = linea.replace("\uFEFF", ""); primera = false; }
                if (linea.trim().isEmpty()) continue;
                String[] c = linea.split(";");
                try {
                    if (c.length < 7) throw new NumberFormatException();
                    int id = Integer.parseInt(c[0].trim());
                    Pedido ped = null;
                    for (Pedido p : lista) {
                        if (p.getIdPedido() == id) { ped = p; break; }
                    }
                    if (ped == null) {
                        ped = new Pedido(id);
                        ped.setFecha(sdf.parse(c[1].trim()));
                        lista.add(ped);
                    }
                    Producto prod = null;
                    for (Producto p : catalogo) {
                        if (p.getCodigo().equals(c[2].trim())) { prod = p; break; }
                    }
                    double precio = Double.parseDouble(c[5].trim().replace(',', '.'));
                    if (prod == null) { // el producto ya no existe en el catalogo
                        prod = new Producto(c[2].trim(), c[3].trim(), precio, 0);
                    }
                    ped.getDetalles().add(new DetallePedido(prod, Integer.parseInt(c[4].trim()), precio));
                } catch (NumberFormatException | ParseException ex) {
                    advertencia = "Se omitieron lineas invalidas en pedidos.txt.";
                }
            }
        } catch (IOException e) {
            advertencia = "No se pudo leer pedidos.txt: " + e.getMessage();
        }
        return lista;
    }
}
