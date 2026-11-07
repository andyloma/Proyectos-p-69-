package modelo;

/**
 * Administrativo: persona con un cargo.
 * Cubre: RF-01 | Diseño: DIS-07
 */
public class Administrativo extends Persona {

    private String cargo;

    public Administrativo(String nombre, long cedula, String cargo) {
        super(nombre, cedula);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String informacion() {
        return "[Administrativo] " + super.informacion() + " | Cargo: " + cargo;
    }
}
