package cafeteria.delegado;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import cafeteria.modelo.GestorCafeteria;
import cafeteria.util.UtilGenerico;

public class PanelReportes extends JPanel {
    private static final long serialVersionUID = 1L;

    private final GestorCafeteria gestor;
    private final JTextArea areaReporte = new JTextArea(6, 40);
    private final DefaultTableModel modeloDemanda;

    public PanelReportes(GestorCafeteria gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel t1 = new JLabel("Reporte de ventas del día");
        t1.setFont(t1.getFont().deriveFont(Font.BOLD, 15f));
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollArea = new JScrollPane(areaReporte);
        scrollArea.setPreferredSize(new Dimension(400, 120));

        JPanel arriba = new JPanel(new BorderLayout(5, 5));
        arriba.add(t1, BorderLayout.NORTH);
        arriba.add(scrollArea, BorderLayout.CENTER);

        JLabel t2 = new JLabel("Productos de mayor demanda");
        t2.setFont(t2.getFont().deriveFont(Font.BOLD, 15f));
        ArrayList<String> columnas = UtilGenerico.crearLista("Posición", "Producto", "Unidades vendidas");
        modeloDemanda = new DefaultTableModel(columnas.toArray(new String[0]), 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaDemanda = new JTable(modeloDemanda);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(t2, BorderLayout.NORTH);
        centro.add(new JScrollPane(tablaDemanda), BorderLayout.CENTER);

        JButton btnGenerar = new JButton("Generar reporte");
        btnGenerar.addActionListener(e -> generarReporte());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnGenerar);

        add(arriba, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        generarReporte();
    }

    public void generarReporte() {
        // Las lineas del reporte se arman con los metodos genericos (tipo String)
        ArrayList<String> lineas = UtilGenerico.crearLista();
        UtilGenerico.agregarVarios(lineas, gestor.generarReporteVentas().split("\n"));

        StringBuilder sb = new StringBuilder();
        for (String l : lineas) {
            sb.append(l).append("\n");
        }
        areaReporte.setText(sb.toString());

        modeloDemanda.setRowCount(0);
        for (String[] fila : gestor.productosMasDemandados()) {
            modeloDemanda.addRow(fila);
        }
    }
}
