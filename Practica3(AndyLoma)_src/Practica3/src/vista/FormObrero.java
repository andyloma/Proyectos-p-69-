package vista;

import javax.swing.JFrame;

import controlador.Controlador;

/** Formulario de obreros. Cubre: RF-02 | Diseño: DIS-08 */
public class FormObrero extends FormBase {

    private static final long serialVersionUID = 1L;
    private final transient Controlador controlador;

    public FormObrero(JFrame owner, Controlador controlador) {
        super(owner, "Obreros", new String[] { "Código" });
        this.controlador = controlador;
    }

    @Override
    protected String registrar(String nombre, String cedula, String[] extras) {
        return controlador.registrarObrero(nombre, cedula, extras[0]);
    }

    @Override
    protected String consultar(String cedula) {
        return controlador.consultarObrero(cedula);
    }

    @Override
    protected String listar() {
        return controlador.listarObreros();
    }
}
