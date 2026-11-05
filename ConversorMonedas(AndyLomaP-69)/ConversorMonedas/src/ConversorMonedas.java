import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Conversor de Monedas con Historial - archivo único para Eclipse.
 *
 * Contiene tres clases dentro del mismo archivo:
 *   - ConversorDivisas       : lógica de conversión (tasas fijas)
 *   - HistorialConversiones  : registro de operaciones (lista + archivo historial.txt)
 *   - ConversorMonedas       : clase principal (menú de consola)
 *
 * Para ejecutarlo en Eclipse: crea un proyecto Java, pega este
 * archivo dentro de src/ (debe llamarse ConversorMonedas.java) y
 * ejecútalo como "Java Application". El archivo historial.txt se
 * creará en la carpeta raíz del proyecto.
 */
public class ConversorMonedas {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ConversorDivisas conversor = new ConversorDivisas();
        HistorialConversiones historial = new HistorialConversiones();

        int opcion;

        do {
            System.out.println("\n===== CONVERSOR DE MONEDAS =====");
            System.out.println("1. Dolares (USD) -> Euros (EUR)");
            System.out.println("2. Euros (EUR) -> Dolares (USD)");
            System.out.println("3. Dolares (USD) -> Moneda local");
            System.out.println("4. Moneda local -> Dolares (USD)");
            System.out.println("5. Euros (EUR) -> Moneda local");
            System.out.println("6. Moneda local -> Euros (EUR)");
            System.out.println("7. Ver historial de conversiones");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEnteroValido(sc);

            if (opcion >= 1 && opcion <= 6) {
                System.out.print("Ingrese el monto a convertir: ");
                double monto = leerMontoValido(sc);

                double resultado = 0;
                String origen = "";
                String destino = "";

                switch (opcion) {
                    case 1:
                        resultado = conversor.usdAEur(monto);
                        origen = "USD"; destino = "EUR";
                        break;
                    case 2:
                        resultado = conversor.eurAUsd(monto);
                        origen = "EUR"; destino = "USD";
                        break;
                    case 3:
                        resultado = conversor.usdALocal(monto);
                        origen = "USD"; destino = "LOCAL";
                        break;
                    case 4:
                        resultado = conversor.localAUsd(monto);
                        origen = "LOCAL"; destino = "USD";
                        break;
                    case 5:
                        resultado = conversor.eurALocal(monto);
                        origen = "EUR"; destino = "LOCAL";
                        break;
                    case 6:
                        resultado = conversor.localAEur(monto);
                        origen = "LOCAL"; destino = "EUR";
                        break;
                }

                System.out.printf("Resultado: %.2f %s = %.2f %s%n", monto, origen, resultado, destino);
                historial.registrarConversion(origen, destino, monto, resultado);

            } else if (opcion == 7) {
                historial.mostrarHistorial();
            } else if (opcion == 8) {
                System.out.println("Saliendo del sistema...");
            } else {
                System.out.println("Opcion no valida.");
            }

        } while (opcion != 8);

        sc.close();
    }

    private static int leerEnteroValido(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un numero entero valido: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static double leerMontoValido(Scanner sc) {
        double monto;
        while (true) {
            if (sc.hasNextDouble()) {
                monto = sc.nextDouble();
                if (monto >= 0) {
                    return monto;
                }
                System.out.print("El monto no puede ser negativo. Ingrese nuevamente: ");
            } else {
                System.out.print("Debe ingresar un numero valido. Ingrese nuevamente: ");
                sc.next();
            }
        }
    }
}

/**
 * Realiza las conversiones entre dolares (USD), euros (EUR) y
 * moneda local, usando tasas de cambio fijas.
 */
class ConversorDivisas {

    // Tasas fijas de referencia (ejemplo educativo)
    private static final double TASA_EUR_POR_USD = 0.92;   // 1 USD = 0.92 EUR
    private static final double TASA_LOCAL_POR_USD = 1.00; // 1 USD = 1.00 moneda local (ej. dolarizacion)

    public double usdAEur(double monto) {
        return monto * TASA_EUR_POR_USD;
    }

    public double eurAUsd(double monto) {
        return monto / TASA_EUR_POR_USD;
    }

    public double usdALocal(double monto) {
        return monto * TASA_LOCAL_POR_USD;
    }

    public double localAUsd(double monto) {
        return monto / TASA_LOCAL_POR_USD;
    }

    public double eurALocal(double monto) {
        double enUsd = eurAUsd(monto);
        return usdALocal(enUsd);
    }

    public double localAEur(double monto) {
        double enUsd = localAUsd(monto);
        return usdAEur(enUsd);
    }
}

/**
 * Guarda el registro de cada conversión realizada, tanto en memoria
 * (ArrayList) como en el archivo de texto historial.txt, junto con
 * la fecha y hora de la transacción.
 */
class HistorialConversiones {

    private static final String NOMBRE_ARCHIVO = "historial.txt";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private ArrayList<String> registros;

    public HistorialConversiones() {
        registros = new ArrayList<String>();
    }

    /**
     * Registra una conversión en la lista y la agrega al archivo
     * historial.txt (modo append, sin borrar lo anterior).
     */
    public void registrarConversion(String origen, String destino, double monto, double resultado) {
        String fecha = LocalDateTime.now().format(FORMATO_FECHA);
        String linea = String.format("[%s] %.2f %s -> %.2f %s",
                fecha, monto, origen, resultado, destino);

        registros.add(linea);
        guardarEnArchivo(linea);
    }

    /**
     * Escribe una línea al final del archivo historial.txt.
     * Si el archivo no existe, se crea automáticamente.
     */
    private void guardarEnArchivo(String linea) {
        try (FileWriter writer = new FileWriter(NOMBRE_ARCHIVO, true)) {
            writer.write(linea + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("No se pudo guardar en el archivo historial.txt: " + e.getMessage());
        }
    }

    /**
     * Muestra en consola todas las conversiones realizadas en esta
     * ejecución (también quedan almacenadas en historial.txt).
     */
    public void mostrarHistorial() {
        System.out.println("\n===== HISTORIAL DE CONVERSIONES =====");
        if (registros.isEmpty()) {
            System.out.println("Aun no se ha realizado ninguna conversion.");
            return;
        }
        for (String registro : registros) {
            System.out.println(registro);
        }
    }
}
