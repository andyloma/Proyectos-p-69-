package cafeteria.util;

import java.util.ArrayList;

/**
 * Clase utilitaria con metodos genericos y parametros de longitud variable (varargs).
 */
public class UtilGenerico {

    private UtilGenerico() { }

    /** Crea un ArrayList con los elementos recibidos. */
    @SafeVarargs
    public static <T> ArrayList<T> crearLista(T... elementos) {
        ArrayList<T> lista = new ArrayList<>();
        if (elementos != null) {
            for (T e : elementos) {
                lista.add(e);
            }
        }
        return lista;
    }

    /** Agrega varios elementos de golpe a una lista existente. */
    @SafeVarargs
    public static <T> void agregarVarios(ArrayList<T> lista, T... elementos) {
        if (lista == null || elementos == null) {
            return;
        }
        for (T e : elementos) {
            lista.add(e);
        }
    }

    /** Devuelve el mayor de los elementos (null si no hay elementos). */
    @SafeVarargs
    public static <T extends Comparable<T>> T obtenerMayor(T... elementos) {
        if (elementos == null || elementos.length == 0) {
            return null;
        }
        T mayor = elementos[0];
        for (T e : elementos) {
            if (e.compareTo(mayor) > 0) {
                mayor = e;
            }
        }
        return mayor;
    }
}
