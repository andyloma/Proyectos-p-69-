import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Gestor de Inventario Simple - archivo único para Eclipse.
 *
 * Contiene tres clases, cada una en su propio bloque, dentro del
 * mismo archivo:
 *   - Producto       : modelo de datos de un producto
 *   - Inventario     : lógica de negocio (ArrayList de productos)
 *   - InventarioGUI  : interfaz gráfica (Swing) - clase principal
 *
 * Para ejecutarlo en Eclipse: crea un proyecto Java, pega este
 * archivo dentro de src/ (debe llamarse InventarioGUI.java) y
 * ejecútalo como "Java Application".
 */
public class InventarioGUI extends JFrame {

    private Inventario inventario;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public InventarioGUI() {
        inventario = new Inventario();

        setTitle("Gestor de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotonesInferior(), BorderLayout.SOUTH);
    }

    /**
     * Panel superior con los campos de texto para capturar los datos
     * del producto y el botón de agregar.
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 5, 5, 5));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel("Nombre:"));
        panel.add(new JLabel("Precio:"));
        panel.add(new JLabel("Stock:"));
        panel.add(new JLabel(""));

        panel.add(txtId);
        panel.add(txtNombre);
        panel.add(txtPrecio);
        panel.add(txtStock);

        JButton btnAgregar = new JButton("Agregar producto");
        btnAgregar.addActionListener(e -> agregarProducto());
        panel.add(btnAgregar);

        return panel;
    }

    /**
     * Tabla central donde se listan los productos del inventario.
     */
    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Precio", "Stock"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);
    }

    /**
     * Panel inferior con las acciones de búsqueda, actualización y listado.
     */
    private JPanel crearPanelBotonesInferior() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));

        JButton btnBuscar = new JButton("Buscar por ID");
        btnBuscar.addActionListener(e -> buscarProducto());

        JButton btnActualizar = new JButton("Actualizar stock");
        btnActualizar.addActionListener(e -> actualizarStock());

        JButton btnStockBajo = new JButton("Stock bajo (<5)");
        btnStockBajo.addActionListener(e -> refrescarTabla(inventario.listarStockBajo()));

        JButton btnListarTodos = new JButton("Listar todos");
        btnListarTodos.addActionListener(e -> refrescarTabla(inventario.listarTodos()));

        panel.add(btnBuscar);
        panel.add(btnActualizar);
        panel.add(btnStockBajo);
        panel.add(btnListarTodos);

        return panel;
    }

    private void agregarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto nuevo = new Producto(id, nombre, precio, stock);
            boolean agregado = inventario.agregarProducto(nuevo);

            if (agregado) {
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
                limpiarCampos();
                refrescarTabla(inventario.listarTodos());
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con el ID " + id,
                        "ID repetido", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, precio y stock deben ser numéricos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            Producto encontrado = inventario.buscarPorId(id);

            if (encontrado != null) {
                ArrayList<Producto> resultado = new ArrayList<Producto>();
                resultado.add(encontrado);
                refrescarTabla(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un producto con el ID " + id,
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico en el campo ID.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarStock() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            int nuevoStock = Integer.parseInt(txtStock.getText().trim());

            boolean actualizado = inventario.actualizarStock(id, nuevoStock);

            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Stock actualizado correctamente.");
                refrescarTabla(inventario.listarTodos());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un producto con el ID " + id,
                        "Sin resultados", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y stock deben ser numéricos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Vuelve a llenar la tabla con la lista de productos recibida.
     */
    private void refrescarTabla(ArrayList<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getStock()});
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            InventarioGUI ventana = new InventarioGUI();
            ventana.setVisible(true);
        });
    }
}

/**
 * Representa un producto del inventario de la tienda.
 */
class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Nombre: " + nombre
                + " | Precio: $" + precio
                + " | Stock: " + stock;
    }
}

/**
 * Administra la colección de productos de la tienda.
 * No depende de consola ni de GUI: solo contiene la lógica del negocio.
 */
class Inventario {

    private ArrayList<Producto> productos;

    public Inventario() {
        productos = new ArrayList<Producto>();
    }

    /**
     * Agrega un producto si el ID no está repetido.
     * Retorna true si se agregó, false si el ID ya existía.
     */
    public boolean agregarProducto(Producto p) {
        if (buscarPorId(p.getId()) != null) {
            return false;
        }
        productos.add(p);
        return true;
    }

    /**
     * Busca un producto por su ID. Retorna null si no lo encuentra.
     */
    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Actualiza el stock de un producto existente.
     * Retorna true si se actualizó, false si el ID no existe.
     */
    public boolean actualizarStock(int id, int nuevoStock) {
        Producto p = buscarPorId(id);
        if (p == null) {
            return false;
        }
        p.setStock(nuevoStock);
        return true;
    }

    /**
     * Retorna una lista con los productos cuyo stock es menor a 5 unidades.
     */
    public ArrayList<Producto> listarStockBajo() {
        ArrayList<Producto> resultado = new ArrayList<Producto>();
        for (Producto p : productos) {
            if (p.getStock() < 5) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Retorna todos los productos del inventario.
     */
    public ArrayList<Producto> listarTodos() {
        return productos;
    }
}
