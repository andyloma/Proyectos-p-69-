package vista;

import javax.swing.JFrame;

import controlador.Controlador;

/** Formulario de administrativos. Cubre: RF-01 | Diseño: DIS-07 */
public class FormAdministrativo extends FormBase {

    private static final long serialVersionUID = 1L;
    private final transient Controlador controlador;

    public FormAdministrativo(JFrame owner, Controlador controlador) {
        super(owner, "Administrativos", new String[] { "Cargo" });
        this.controlador = controlador;
    }

    @Override
    protected String registrar(String nombre, String cedula, String[] extras) {
        return controlador.registrarAdministrativo(nombre, cedula, extras[0]);
    }

    @Override
    protected String consultar(String cedula) {
        return controlador.consultarAdministrativo(cedula);
    }

    @Override
    protected String listar() {
        return controlador.listarAdministrativos();
    }
}
