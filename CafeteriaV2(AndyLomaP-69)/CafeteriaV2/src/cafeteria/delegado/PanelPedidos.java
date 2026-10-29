package cafeteria.delegado;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import cafeteria.modelo.DetallePedido;
import cafeteria.modelo.GestorCafeteria;
import cafeteria.modelo.Pedido;
import cafeteria.modelo.Producto;
import cafeteria.util.UtilGenerico;

public class PanelPedidos extends JPanel {
    private static final long serialVersionUID = 1L;

    private final GestorCafeteria gestor;
    private Pedido pedidoActual;

    private final JLabel lblTitulo = new JLabel();
    private final JComboBox<Producto> cmbProductos = new JComboBox<>();
    private final JTextField txtCantidad = new JTextField("1", 5);
    private final DefaultTableModel modeloDetalle;
    private final JTable tablaDetalle;
    private final JLabel lblSubtotal = new JLabel();
    private final JLabel lblIva = new JLabel();
    private final JLabel lblTotal = new JLabel();

    public PanelPedidos(GestorCafeteria gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarProducto());
        JPanel seleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seleccion.add(new JLabel("Producto:"));
        seleccion.add(cmbProductos);
        seleccion.add(new JLabel("Cantidad:"));
        seleccion.add(txtCantidad);
        seleccion.add(btnAgregar);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        seleccion.setAlignmentX(LEFT_ALIGNMENT);
        norte.add(lblTitulo);
        norte.add(Box.createVerticalStrut(5));
        norte.add(seleccion);
        add(norte, BorderLayout.NORTH);

        ArrayList<String> columnas = UtilGenerico.crearLista("Producto", "Cant.", "P. unit.", "Subtotal");
        modeloDetalle = new DefaultTableModel(columnas.toArray(new String[0]), 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        JButton btnQuitar = new JButton("Quitar seleccionado");
        JButton btnCancelar = new JButton("Cancelar pedido");
        JButton btnConfirmar = new JButton("Confirmar pedido");
        btnQuitar.addActionListener(e -> quitarProducto());
        btnCancelar.addActionListener(e -> cancelarPedido());
        btnConfirmar.addActionListener(e -> confirmarPedido());

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        izquierda.add(btnQuitar);
        izquierda.add(btnCancelar);

        JPanel totales = new JPanel(new GridLayout(3, 1));
        totales.setBorder(BorderFactory.createTitledBorder("Totales"));
        totales.add(lblSubtotal);
        totales.add(lblIva);
        totales.add(lblTotal);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        JPanel derecha = new JPanel(new BorderLayout(5, 5));
        derecha.add(totales, BorderLayout.CENTER);
        derecha.add(btnConfirmar, BorderLayout.SOUTH);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(izquierda, BorderLayout.WEST);
        sur.add(derecha, BorderLayout.EAST);
        add(sur, BorderLayout.SOUTH);

        nuevoPedido();
    }

    private void nuevoPedido() {
        pedidoActual = gestor.crearNuevoPedido();
        lblTitulo.setText("Nuevo pedido N.º " + pedidoActual.getIdPedido());
        txtCantidad.setText("1");
        actualizarCombo();
        actualizarTabla();
    }

    /** Recarga los productos del JComboBox (se llama al mostrar el modulo). */
    public void actualizarCombo() {
        Producto previo = (Producto) cmbProductos.getSelectedItem();
        cmbProductos.removeAllItems();
        for (Producto p : gestor.listarProductos()) {
            cmbProductos.addItem(p);
        }
        if (previo != null) {
            for (int i = 0; i < cmbProductos.getItemCount(); i++) {
                if (cmbProductos.getItemAt(i).getCodigo().equals(previo.getCodigo())) {
                    cmbProductos.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void actualizarTabla() {
        modeloDetalle.setRowCount(0);
        for (DetallePedido d : pedidoActual.getDetalles()) {
            modeloDetalle.addRow(new Object[] { d.getNombreProducto(), d.getCantidad(),
                    String.format(Locale.US, "%.2f", d.getPrecioUnitario()),
                    String.format(Locale.US, "%.2f", d.calcularSubtotal()) });
        }
        lblSubtotal.setText(String.format(Locale.US, "Subtotal:  $%.2f", pedidoActual.calcularSubtotal()));
        lblIva.setText(String.format(Locale.US, "IVA (15%%):  $%.2f", pedidoActual.calcularIva()));
        lblTotal.setText(String.format(Locale.US, "Total a pagar:  $%.2f", pedidoActual.calcularTotal()));
    }

    private void agregarProducto() {
        Producto p = (Producto) cmbProductos.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ValidadorFormulario.vacio(txtCantidad.getText())) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de guardar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero mayor a cero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int acumulada = pedidoActual.getCantidadDe(p.getCodigo()) + cantidad;
        if (acumulada > p.getStock()) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + p.getStock() + " unidades.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pedidoActual.agregarDetalle(new DetallePedido(p, cantidad));
        txtCantidad.setText("1");
        actualizarTabla();
    }

    private void quitarProducto() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del pedido para quitarlo.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pedidoActual.quitarDetalle(fila);
        actualizarTabla();
    }

    private void cancelarPedido() {
        if (pedidoActual.estaVacio()) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Desea descartar el pedido en curso?",
                "Cancelar pedido", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            nuevoPedido();
        }
    }

    private void confirmarPedido() {
        if (pedidoActual.estaVacio()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto al pedido.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double total = pedidoActual.calcularTotal();
            gestor.registrarPedido(pedidoActual);
            JOptionPane.showMessageDialog(this,
                    String.format(Locale.US, "Pedido registrado. Total a pagar: $%.2f", total),
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            nuevoPedido();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            actualizarCombo();
        }
    }
}
