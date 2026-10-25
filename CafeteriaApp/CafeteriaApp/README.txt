PROYECTO: CafeteriaApp
=======================

Como importarlo en Eclipse:
1. Abrir Eclipse.
2. File > Import... > General > Existing Projects into Workspace.
3. En "Select root directory" elegir la carpeta CafeteriaApp (esta carpeta).
4. Click en Finish.
5. Abrir la clase Main.java (dentro del paquete cafeteria) y ejecutarla
   con Run As > Java Application.

Que hace el programa:
Es una aplicacion de consola para la cafeteria "El Buen Sabor" (Practica 1).
Permite:
1. Registrar productos en el catalogo (codigo, nombre, precio, stock).
2. Listar los productos y su stock actual.
3. Reponer el stock de un producto.
4. Registrar un pedido con uno o varios productos, validando que haya
   stock suficiente y calculando el total a pagar.
5. Generar un reporte de ventas del dia (total vendido y producto mas
   vendido).
6. Salir del programa.

Los datos se guardan automaticamente en dos archivos de texto que se
crean en la carpeta del proyecto (o donde se ejecute el programa):
- productos.txt: catalogo de productos. Formato: codigo;nombre;precio;stock
- pedidos.txt: historial de pedidos (una linea por cada producto
  vendido). Formato: idPedido;fecha;codigoProducto;nombreProducto;
  cantidad;precioUnitario;subtotal

Estructura de clases:
- Producto: representa un producto del catalogo.
- DetallePedido: representa un producto dentro de un pedido (con su
  cantidad y subtotal).
- Pedido: representa un pedido completo, con su lista de DetallePedido
  (ArrayList) y su total.
- GestorCafeteria: contiene el ArrayList de productos y de pedidos, y
  toda la logica (registrar, listar, reponer stock, registrar pedido,
  generar reporte, guardar y cargar los archivos).
- Main: muestra el menu de consola y conecta todo.
