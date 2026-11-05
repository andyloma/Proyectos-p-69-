import java.util.Scanner;

/**
 * Calculadora de Estadísticas de Notas - archivo único para Eclipse.
 *
 * Contiene dos clases dentro del mismo archivo:
 *   - GestorNotas          : lógica de cálculo (arreglo de notas)
 *   - EstadisticasNotas    : clase principal (menú y entrada de datos)
 *
 * Para ejecutarlo en Eclipse: crea un proyecto Java, pega este
 * archivo dentro de src/ (debe llamarse EstadisticasNotas.java) y
 * ejecútalo como "Java Application".
 */
public class EstadisticasNotas {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de estudiantes: ");
        int cantidad = leerEnteroValido(sc);

        while (cantidad <= 0) {
            System.out.print("La cantidad debe ser mayor a 0. Intente de nuevo: ");
            cantidad = leerEnteroValido(sc);
        }

        GestorNotas gestor = new GestorNotas(cantidad);

        for (int i = 0; i < cantidad; i++) {
            double nota = leerNotaValida(sc, i + 1);
            gestor.registrarNota(i, nota);
        }

        gestor.mostrarResultados();

        sc.close();
    }

    /**
     * Lee un entero desde consola, repitiendo la pregunta si el valor
     * ingresado no es un número válido.
     */
    private static int leerEnteroValido(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un número entero válido: ");
            sc.next();
        }
        return sc.nextInt();
    }

    /**
     * Lee la nota de un estudiante, validando que sea un número
     * entre 0 y 100.
     */
    private static double leerNotaValida(Scanner sc, int numeroEstudiante) {
        double nota;
        while (true) {
            System.out.print("Nota del estudiante " + numeroEstudiante + " (0 a 100): ");
            if (sc.hasNextDouble()) {
                nota = sc.nextDouble();
                if (nota >= 0 && nota <= 100) {
                    return nota;
                }
                System.out.println("La nota debe estar entre 0 y 100.");
            } else {
                System.out.println("Debe ingresar un número válido.");
                sc.next();
            }
        }
    }
}

/**
 * Administra el arreglo de notas y calcula las métricas del grupo:
 * promedio, nota máxima, nota mínima y cantidad de aprobados.
 */
class GestorNotas {

    private static final double NOTA_APROBACION = 60;

    private double[] notas;

    public GestorNotas(int cantidadEstudiantes) {
        notas = new double[cantidadEstudiantes];
    }

    public void registrarNota(int indice, double nota) {
        notas[indice] = nota;
    }

    /**
     * Calcula el promedio de todas las notas.
     */
    public double calcularPromedio() {
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        return suma / notas.length;
    }

    /**
     * Retorna la nota más alta del arreglo.
     */
    public double obtenerNotaMaxima() {
        double max = notas[0];
        for (double nota : notas) {
            if (nota > max) {
                max = nota;
            }
        }
        return max;
    }

    /**
     * Retorna la nota más baja del arreglo.
     */
    public double obtenerNotaMinima() {
        double min = notas[0];
        for (double nota : notas) {
            if (nota < min) {
                min = nota;
            }
        }
        return min;
    }

    /**
     * Cuenta cuántos estudiantes aprobaron (nota >= 60).
     */
    public int contarAprobados() {
        int contador = 0;
        for (double nota : notas) {
            if (nota >= NOTA_APROBACION) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Muestra en consola el resumen de las estadísticas calculadas.
     */
    public void mostrarResultados() {
        System.out.println("\n===== RESULTADOS =====");
        System.out.printf("Promedio del grupo: %.2f%n", calcularPromedio());
        System.out.printf("Nota más alta: %.2f%n", obtenerNotaMaxima());
        System.out.printf("Nota más baja: %.2f%n", obtenerNotaMinima());
        System.out.println("Estudiantes aprobados: " + contarAprobados() + " de " + notas.length);
    }
}
