package cafeteria.delegado;

/** Validaciones de los formularios (Vista). */
public class ValidadorFormulario {

    private ValidadorFormulario() { }

    public static boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /** Acepta punto o coma decimal. Lanza NumberFormatException si no es valido o es negativo. */
    public static double parsearDecimal(String texto) {
        double v = Double.parseDouble(texto.trim().replace(',', '.'));
        if (v < 0 || Double.isNaN(v) || Double.isInfinite(v)) throw new NumberFormatException();
        return v;
    }

    /** Entero no negativo. Lanza NumberFormatException si no es valido. */
    public static int parsearEntero(String texto) {
        int v = Integer.parseInt(texto.trim());
        if (v < 0) throw new NumberFormatException();
        return v;
    }
}
