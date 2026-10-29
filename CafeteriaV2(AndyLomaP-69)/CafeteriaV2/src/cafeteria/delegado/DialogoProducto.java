package cafeteria.delegado;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import cafeteria.modelo.GestorCafeteria;
import cafeteria.modelo.Producto;
public class DialogoProducto extends JDialog {
    private static final long serialVersionUID = 1L;
    private final GestorCafeteria gestor;
    private final Producto editando; 
    private boolean guardado = false;
    private final JTextField txtCodigo = new JTextField(15);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtPrecio = new JTextField(15);
    private final JTextField txtStock = new JTextField(15);
    public DialogoProducto(Window owner, GestorCafeteria gestor, Producto editando) {
        super(owner, editando == null ? "Registrar producto" : "Editar producto",
                Dialog.ModalityType.APPLICATION_MODAL);
        this.gestor = gestor;
        this.editando = editando;
        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(new JLabel("Código:"));        form.add(txtCodigo);
        form.add(new JLabel("Nombre:"));        form.add(txtNombre);
        form.add(new JLabel("Precio ($):"));    form.add(txtPrecio);
        form.add(new JLabel("Stock inicial:")); form.add(txtStock);
        if (editando != null) {
            txtCodigo.setText(editando.getCodigo());
            txtCodigo.setEditable(false);   
            txtNombre.setText(editando.getNombre());
            txtPrecio.setText(String.format(Locale.US, "%.2f", editando.getPrecio()));
            txtStock.setText(String.valueOf(editando.getStock()));
            txtStock.setEditable(false);
        }
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnGuardar);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isGuardado() { return guardado; }

    private void guardar() {
        if (ValidadorFormulario.vacio(txtCodigo.getText()) || ValidadorFormulario.vacio(txtNombre.getText())
                || ValidadorFormulario.vacio(txtPrecio.getText())
                || (editando == null && ValidadorFormulario.vacio(txtStock.getText()))) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de guardar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double precio = ValidadorFormulario.parsearDecimal(txtPrecio.getText());
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            if (editando == null) {
                int stock = ValidadorFormulario.parsearEntero(txtStock.getText());
                gestor.registrarProducto(codigo, nombre, precio, stock);
            } else {
                gestor.editarProducto(codigo, nombre, precio);
            }
            guardado = true;
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio y el stock deben ser números válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
