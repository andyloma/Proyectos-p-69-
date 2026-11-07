package modelo;

/**
 * Técnico: obrero con clave de acceso.
 * Cubre: RF-03 | Diseño: DIS-09
 */
public class Tecnico extends Obrero {

    private String claveAcceso;

    public Tecnico(String nombre, long cedula, String codigo, String claveAcceso) {
        super(nombre, cedula, codigo);
        this.claveAcceso = claveAcceso;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    @Override
    public String informacion() {
        return "[Técnico] Nombre: " + getNombre() + " | Cédula: " + getCedula()
                + " | Código: " + getCodigo() + " | Clave de acceso: " + claveAcceso;
    }
}
