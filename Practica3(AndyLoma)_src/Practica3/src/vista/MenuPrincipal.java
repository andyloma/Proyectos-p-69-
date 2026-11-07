package vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controlador.Controlador;

/**
 * Ventana principal con el menú: Administrativos / Obreros / Técnicos,
 * cada uno con Registrar, Consultar y Listar.
 * Cubre: RNF-05 | Diseño: DIS-06
 */
public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public MenuPrincipal() {
        super("Gestión de Personal - Práctica 3");
        Controlador controlador = new Controlador();

        FormAdministrativo formAdmin = new FormAdministrativo(this, controlador);
        FormObrero formObrero = new FormObrero(this, controlador);
        FormTecnico formTecnico = new FormTecnico(this, controlador);

        JMenuBar barra = new JMenuBar();
        barra.add(crearMenu("Administrativos", formAdmin));
        barra.add(crearMenu("Obreros", formObrero));
        barra.add(crearMenu("Técnicos", formTecnico));
        setJMenuBar(barra);

        add(new JLabel("Seleccione una opción del menú", SwingConstants.CENTER));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);
    }

    private JMenu crearMenu(String titulo, FormBase form) {
        JMenu menu = new JMenu(titulo);
        menu.add(crearItem("Registrar", form, FormBase.TAB_REGISTRAR));
        menu.add(crearItem("Consultar", form, FormBase.TAB_CONSULTAR));
        menu.add(crearItem("Listar", form, FormBase.TAB_LISTAR));
        return menu;
    }

    private JMenuItem crearItem(String texto, FormBase form, int pestana) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> form.abrir(pestana));
        return item;
    }

    /** Punto de entrada de la aplicación. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
