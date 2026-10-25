package cafeteria;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestorCafeteria gestor = new GestorCafeteria();
        int opcion = -1;

        while (opcion != 6) {
            mostrarMenu();
            opcion = leerEntero(sc, "Elige una opcion: ");

            if (opcion == 1) {
                registrarProducto(sc, gestor);
            } else if (opcion == 2) {
                listarProductos(gestor);
            } else if (opcion == 3) {
                reponerStock(sc, gestor);
            } else if (opcion == 4) {
                registrarPedido(sc, gestor);
            } else if (opcion == 5) {
                System.out.println(gestor.generarReporteVentas());
            } else if (opcion == 6) {
                System.out.println("Gracias por usar el sistema de El Buen Sabor.");
            } else {
                System.out.println("Opcion no valida, intenta de nuevo.");
            }
        }

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("");
        System.out.println("===== Cafeteria El Buen Sabor =====");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Reponer stock de un producto");
        System.out.println("4. Registrar pedido");
        System.out.println("5. Generar reporte de ventas del dia");
        System.out.println("6. Salir");
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        String texto = sc.nextLine();
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerDouble(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        String texto = sc.nextLine();
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void registrarProducto(Scanner sc, GestorCafeteria gestor) {
        System.out.print("Codigo del producto: ");
        String codigo = sc.nextLine().trim();
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine().trim();
        double precio = leerDouble(sc, "Precio: ");
        int stock = leerEntero(sc, "Stock inicial: ");

        if (precio < 0 || stock < 0) {
            System.out.println("El precio y el stock deben ser numeros validos.");
            return;
        }

        boolean exito = gestor.registrarProducto(codigo, nombre, precio, stock);
        if (exito) {
            System.out.println("Producto registrado correctamente.");
        } else {
            System.out.println("Ya existe un producto con ese codigo.");
        }
    }

    private static void listarProductos(GestorCafeteria gestor) {
        ArrayList<Producto> catalogo = gestor.listarProductos();
        if (catalogo.isEmpty()) {
            System.out.println("Todavia no hay productos registrados.");
            return;
        }
        System.out.println("");
        System.out.println("Catalogo de productos:");
        for (Producto p : catalogo) {
            System.out.println(p.toString());
        }
    }

    private static void reponerStock(Scanner sc, GestorCafeteria gestor) {
        System.out.print("Codigo del producto: ");
        String codigo = sc.nextLine().trim();
        int cantidad = leerEntero(sc, "Cantidad a reponer: ");

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a cero.");
            return;
        }

        boolean exito = gestor.reponerStock(codigo, cantidad);
        if (exito) {
            System.out.println("Stock actualizado correctamente.");
        } else {
            System.out.println("No existe un producto con ese codigo.");
        }
    }

    private static void registrarPedido(Scanner sc, GestorCafeteria gestor) {
        Pedido pedido = gestor.crearNuevoPedido();
        boolean seguirAgregando = true;

        while (seguirAgregando) {
            System.out.print("Codigo del producto a pedir: ");
            String codigo = sc.nextLine().trim();
            Producto producto = gestor.buscarProducto(codigo);

            if (producto == null) {
                System.out.println("Ese producto no existe.");
            } else {
                int cantidad = leerEntero(sc, "Cantidad: ");
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a cero.");
                } else if (producto.getStock() < cantidad) {
                    System.out.println("No hay stock suficiente. Stock disponible: " + producto.getStock());
                } else {
                    DetallePedido detalle = new DetallePedido(producto, cantidad);
                    pedido.agregarDetalle(detalle);
                    System.out.println("Producto agregado al pedido.");
                }
            }

            System.out.print("Agregar otro producto? (s/n): ");
            String respuesta = sc.nextLine().trim();
            if (!respuesta.equalsIgnoreCase("s")) {
                seguirAgregando = false;
            }
        }

        String error = gestor.registrarPedido(pedido);
        if (error == null) {
            System.out.println("Pedido registrado. Total a pagar: $" + String.format("%.2f", pedido.getTotal()));
        } else {
            System.out.println(error);
        }
    }
}
