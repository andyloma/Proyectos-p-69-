package cafeteria.delegado;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import cafeteria.modelo.GestorCafeteria;
import cafeteria.modelo.Producto;
import cafeteria.util.UtilGenerico;

public class PanelProductos extends JPanel {
    private static final long serialVersionUID = 1L;

    private final GestorCafeteria gestor;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public PanelProductos(GestorCafeteria gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Catálogo de productos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));
        add(titulo, BorderLayout.NORTH);

        ArrayList<String> columnas = UtilGenerico.crearLista("Código", "Nombre", "Precio ($)", "Stock");
        modelo = new DefaultTableModel(columnas.toArray(new String[0]), 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnRegistrar = new JButton("Registrar producto");
        JButton btnEditar = new JButton("Editar seleccionado");
        JButton btnReponer = new JButton("Reponer stock");
        btnRegistrar.addActionListener(e -> registrar());
        btnEditar.addActionListener(e -> editar());
        btnReponer.addActionListener(e -> reponerStock());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnRegistrar);
        botones.add(btnEditar);
        botones.add(btnReponer);
        add(botones, BorderLayout.SOUTH);

        actualizarTabla();
    }

    public void actualizarTabla() {
        modelo.setRowCount(0);
        for (Producto p : gestor.listarProductos()) {
            modelo.addRow(new Object[] { p.getCodigo(), p.getNombre(),
                    String.format(Locale.US, "%.2f", p.getPrecio()), p.getStock() });
        }
    }

    private Producto seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return gestor.buscarProducto((String) modelo.getValueAt(fila, 0));
    }

    private void registrar() {
        DialogoProducto d = new DialogoProducto(SwingUtilities.getWindowAncestor(this), gestor, null);
        d.setVisible(true);
        actualizarTabla();
    }

    private void editar() {
        Producto p = seleccionado();
        if (p == null) return;
        DialogoProducto d = new DialogoProducto(SwingUtilities.getWindowAncestor(this), gestor, p);
        d.setVisible(true);
        actualizarTabla();
    }

    private void reponerStock() {
        Producto p = seleccionado();
        if (p == null) return;
        String txt = JOptionPane.showInputDialog(this,
                "Cantidad a reponer de " + p.getNombre() + ":", "Reponer stock", JOptionPane.QUESTION_MESSAGE);
        if (txt == null) return; // cancelado
        try {
            int cant = Integer.parseInt(txt.trim());
            if (cant <= 0) throw new NumberFormatException();
            gestor.reponerStock(p.getCodigo(), cant);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Stock actualizado. Nuevo stock: " + p.getStock(),
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad entera mayor a cero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
