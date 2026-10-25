package cafeteria;

public class Producto {

    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void aumentarStock(int cantidad) {
        stock = stock + cantidad;
    }

    public void reducirStock(int cantidad) {
        stock = stock - cantidad;
    }

    public String aLineaArchivo() {
        return codigo + ";" + nombre + ";" + precio + ";" + stock;
    }

    public String toString() {
        return codigo + " - " + nombre + " - $" + precio + " - Stock: " + stock;
    }
}
