package cafeteria.modelo;

public class Producto {
    private final String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }

    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        stock += cantidad;
    }

    public void reducirStock(int cantidad) {
        if (cantidad <= 0 || cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + stock + " unidades.");
        }
        stock -= cantidad;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
