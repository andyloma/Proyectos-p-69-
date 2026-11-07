package vista;

import javax.swing.JFrame;

import controlador.Controlador;

/** Formulario de técnicos. Cubre: RF-03 | Diseño: DIS-09 */
public class FormTecnico extends FormBase {

    private static final long serialVersionUID = 1L;
    private final transient Controlador controlador;

    public FormTecnico(JFrame owner, Controlador controlador) {
        super(owner, "Técnicos", new String[] { "Código", "Clave de acceso" });
        this.controlador = controlador;
    }

    @Override
    protected String registrar(String nombre, String cedula, String[] extras) {
        return controlador.registrarTecnico(nombre, cedula, extras[0], extras[1]);
    }

    @Override
    protected String consultar(String cedula) {
        return controlador.consultarTecnico(cedula);
    }

    @Override
    protected String listar() {
        return controlador.listarTecnicos();
    }
}
