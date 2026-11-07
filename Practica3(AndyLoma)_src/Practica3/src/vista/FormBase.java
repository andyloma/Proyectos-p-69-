package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Formulario base con tres pestañas: Registrar, Consultar y Listar.
 * Las subclases definen los campos propios del tipo y llaman al controlador.
 * Cubre: RNF-05 | Diseño: DIS-07..DIS-11
 */
public abstract class FormBase extends JDialog {

    private static final long serialVersionUID = 1L;

    public static final int TAB_REGISTRAR = 0;
    public static final int TAB_CONSULTAR = 1;
    public static final int TAB_LISTAR = 2;

    private final JTabbedPane pestanas = new JTabbedPane();
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtCedula = new JTextField(20);
    private final JTextField[] txtExtra;
    private final JLabel lblMensaje = new JLabel(" ");
    private final JTextField txtCedulaBuscar = new JTextField(15);
    private final JTextArea areaResultado = new JTextArea(4, 40);
    private final JTextArea areaListado = new JTextArea(12, 40);

    protected FormBase(JFrame owner, String titulo, String[] etiquetasExtra) {
        super(owner, titulo, false);
        txtExtra = new JTextField[etiquetasExtra.length];

        pestanas.addTab("Registrar", crearPanelRegistro(etiquetasExtra));
        pestanas.addTab("Consultar", crearPanelConsulta());
        pestanas.addTab("Listar", crearPanelListado());

        add(pestanas);
        pack();
        setLocationRelativeTo(owner);
    }

    // ---- Métodos que implementa cada formulario ----
    protected abstract String registrar(String nombre, String cedula, String[] extras);

    protected abstract String consultar(String cedula);

    protected abstract String listar();

    public void abrir(int pestana) {
        pestanas.setSelectedIndex(pestana);
        if (pestana == TAB_LISTAR) {
            areaListado.setText(listar());
        }
        setVisible(true);
    }

    // ---- Construcción de paneles ----
    private JPanel crearPanelRegistro(String[] etiquetasExtra) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel campos = new JPanel(new GridLayout(2 + etiquetasExtra.length, 2, 5, 8));
        campos.add(new JLabel("Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Cédula:"));
        campos.add(txtCedula);
        for (int i = 0; i < etiquetasExtra.length; i++) {
            txtExtra[i] = new JTextField(20);
            campos.add(new JLabel(etiquetasExtra[i] + ":"));
            campos.add(txtExtra[i]);
        }

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        JPanel sur = new JPanel(new GridLayout(2, 1, 5, 5));
        sur.add(btnGuardar);
        sur.add(lblMensaje);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelConsulta() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel norte = new JPanel();
        norte.add(new JLabel("Cédula:"));
        norte.add(txtCedulaBuscar);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        norte.add(btnBuscar);

        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaResultado), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnListar = new JButton("Listar");
        btnListar.addActionListener(e -> areaListado.setText(listar()));

        areaListado.setEditable(false);

        panel.add(btnListar, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaListado), BorderLayout.CENTER);
        return panel;
    }

    // ---- Acciones ----
    private void guardar() {
        String[] extras = new String[txtExtra.length];
        for (int i = 0; i < txtExtra.length; i++) {
            extras[i] = txtExtra[i].getText();
        }
        try {
            String msg = registrar(txtNombre.getText(), txtCedula.getText(), extras);
            lblMensaje.setText(msg);
            limpiar();
        } catch (IllegalArgumentException ex) {
            lblMensaje.setText("Error: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            areaResultado.setText(consultar(txtCedulaBuscar.getText()));
        } catch (IllegalArgumentException ex) {
            areaResultado.setText("Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtCedula.setText("");
        for (JTextField t : txtExtra) {
            t.setText("");
        }
    }
}
