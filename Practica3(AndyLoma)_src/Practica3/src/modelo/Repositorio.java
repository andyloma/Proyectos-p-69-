package modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Clase genérica que centraliza registrar, buscar y listar empleados.
 * Cubre: RNF-01, RNF-02, RNF-03, RF-01..RF-05
 * Diseño: DIS-01, DIS-02, DIS-03, DIS-04
 *
 * @param <T> tipo de empleado (Administrativo, Obrero o Tecnico)
 */
public class Repositorio<T extends Persona> {

    // RNF-02: ArrayList<T> en lugar de arreglos fijos
    private final ArrayList<T> empleados = new ArrayList<>();

    // Cédulas registradas, guardadas como Long (RNF-03)
    private final HashSet<Long> cedulas = new HashSet<>();

    /** DIS-02: método genérico de registro. */
    public void registrar(T empleado) {
        empleados.add(empleado);
        Long cedulaObj = empleado.getCedula(); // BOXING: long -> Long
        cedulas.add(cedulaObj);
    }

    /** DIS-03: método genérico de búsqueda por cédula. Devuelve null si no existe. */
    public T buscarPorCedula(long cedula) {
        for (T e : empleados) {
            if (e.getCedula() == cedula) {
                return e;
            }
        }
        return null;
    }

    /** DIS-04: método genérico de listado. */
    public ArrayList<T> listar() {
        return empleados;
    }

    /** Indica si una cédula ya está registrada en este repositorio. */
    public boolean existe(long cedula) {
        return cedulas.contains(cedula); // BOXING: long -> Long al llamar contains
    }

    /** Cantidad de empleados registrados. */
    public int cantidad() {
        Integer total = empleados.size(); // BOXING: int -> Integer
        int resultado = total;            // UNBOXING: Integer -> int
        return resultado;
    }

    /**
     * Método genérico estático: arma un texto con la información de cualquier
     * lista de empleados (RNF-01).
     */
    public static <E extends Persona> String listadoTexto(List<E> lista) {
        if (lista.isEmpty()) {
            return "No hay empleados registrados.";
        }
        StringBuilder sb = new StringBuilder();
        int numero = 1;
        for (E e : lista) {
            sb.append(numero++).append(". ").append(e.informacion()).append("\n");
        }
        return sb.toString();
    }
}
