package modelo;

/**
 * Clase base de todos los empleados.
 * Diseño: jerarquía de empleados (Persona).
 */
public class Persona {

    private String nombre;
    private long cedula;

    public Persona(String nombre, long cedula) {
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public void print(String texto) {
        System.out.println(texto);
    }

    /** Se sobrescribe en cada subclase. */
    public String informacion() {
        return "Nombre: " + nombre + " | Cédula: " + cedula;
    }
}
