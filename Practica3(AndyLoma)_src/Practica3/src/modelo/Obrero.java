package modelo;

/**
 * Obrero: persona con un código.
 * Cubre: RF-02 | Diseño: DIS-08
 */
public class Obrero extends Persona {

    private String codigo;

    public Obrero(String nombre, long cedula, String codigo) {
        super(nombre, cedula);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String informacion() {
        return "[Obrero] " + super.informacion() + " | Código: " + codigo;
    }
}
