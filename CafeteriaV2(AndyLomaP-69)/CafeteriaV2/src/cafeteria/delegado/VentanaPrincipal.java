package cafeteria.delegado;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import cafeteria.modelo.GestorCafeteria;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String BIENVENIDA = "Bienvenida";
    private static final String PRODUCTOS = "Productos";
    private static final String PEDIDOS = "Pedidos";
    private static final String REPORTES = "Reportes";

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenido = new JPanel(cartas);
    private final PanelProductos panelProductos;
    private final PanelPedidos panelPedidos;
    private final PanelReportes panelReportes;

    public VentanaPrincipal(GestorCafeteria gestor) {
        super("Cafetería El Buen Sabor - Sistema de Gestión de Pedidos v2.0");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { salir(); }
        });

        panelProductos = new PanelProductos(gestor);
        panelPedidos = new PanelPedidos(gestor);
        panelReportes = new PanelReportes(gestor);

        contenido.add(crearBienvenida(), BIENVENIDA);
        contenido.add(panelProductos, PRODUCTOS);
        contenido.add(panelPedidos, PEDIDOS);
        contenido.add(panelReportes, REPORTES);

        setLayout(new BorderLayout());
        construirMenu();
        add(crearNavegacion(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);

        setSize(850, 540);
        setMinimumSize(new Dimension(750, 480));
        setLocationRelativeTo(null);

        if (gestor.getAdvertenciaCarga() != null) {
            JOptionPane.showMessageDialog(this, gestor.getAdvertenciaCarga(),
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel crearBienvenida() {
        JPanel p = new JPanel(new GridBagLayout());
        JPanel caja = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel t = new JLabel("Bienvenido a El Buen Sabor", SwingConstants.CENTER);
        t.setFont(new Font("SansSerif", Font.BOLD, 22));
        t.setForeground(new Color(0x8B0000));
        JLabel s = new JLabel("Seleccione un módulo en el panel de navegación o en el menú superior.",
                SwingConstants.CENTER);
        JLabel f = new JLabel("Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                SwingConstants.CENTER);
        f.setForeground(Color.GRAY);
        caja.add(t);
        caja.add(s);
        caja.add(f);
        p.add(caja);
        return p;
    }

    private JPanel crearNavegacion() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nav.add(crearBotonNav("Productos", PRODUCTOS));
        nav.add(Box.createVerticalStrut(8));
        nav.add(crearBotonNav("Pedidos", PEDIDOS));
        nav.add(Box.createVerticalStrut(8));
        nav.add(crearBotonNav("Reportes", REPORTES));
        nav.add(Box.createVerticalGlue());
        JButton salir = new JButton("Salir");
        salir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        salir.addActionListener(e -> salir());
        nav.add(salir);
        nav.setPreferredSize(new Dimension(150, 0));
        return nav;
    }

    private JButton crearBotonNav(String texto, String modulo) {
        JButton b = new JButton(texto);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        b.addActionListener(e -> mostrarModulo(modulo));
        return b;
    }

    private void construirMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu archivo = new JMenu("Archivo");
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.addActionListener(e -> salir());
        archivo.add(miSalir);

        JMenu mProd = new JMenu("Productos");
        JMenuItem miProd = new JMenuItem("Administrar productos");
        miProd.addActionListener(e -> mostrarModulo(PRODUCTOS));
        mProd.add(miProd);

        JMenu mPed = new JMenu("Pedidos");
        JMenuItem miPed = new JMenuItem("Nuevo pedido");
        miPed.addActionListener(e -> mostrarModulo(PEDIDOS));
        mPed.add(miPed);

        JMenu mRep = new JMenu("Reportes");
        JMenuItem miRep = new JMenuItem("Ver reportes");
        miRep.addActionListener(e -> mostrarModulo(REPORTES));
        mRep.add(miRep);

        JMenu ayuda = new JMenu("Ayuda");
        JMenuItem acerca = new JMenuItem("Acerca de");
        acerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema de Gestión de Pedidos v2.0\nCafetería El Buen Sabor\nPráctica 2 - Programación aplicada",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        ayuda.add(acerca);

        barra.add(archivo);
        barra.add(mProd);
        barra.add(mPed);
        barra.add(mRep);
        barra.add(ayuda);
        setJMenuBar(barra);
    }

    public void mostrarModulo(String nombre) {
        if (PRODUCTOS.equals(nombre)) {
            panelProductos.actualizarTabla();
        } else if (PEDIDOS.equals(nombre)) {
            panelPedidos.actualizarCombo();
        } else if (REPORTES.equals(nombre)) {
            panelReportes.generarReporte();
        }
        cartas.show(contenido, nombre);
    }

    private void salir() {
        int r = JOptionPane.showConfirmDialog(this, "¿Desea salir de la aplicación?",
                "Salir", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
