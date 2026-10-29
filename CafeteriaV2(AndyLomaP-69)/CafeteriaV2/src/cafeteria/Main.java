package cafeteria;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import cafeteria.delegado.VentanaPrincipal;
import cafeteria.modelo.GestorCafeteria;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        SwingUtilities.invokeLater(() -> {
            GestorCafeteria gestor = new GestorCafeteria();
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}
