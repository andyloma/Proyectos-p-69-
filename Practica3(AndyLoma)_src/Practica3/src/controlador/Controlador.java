package controlador;

import modelo.Administrativo;
import modelo.Obrero;
import modelo.Persona;
import modelo.Repositorio;
import modelo.Tecnico;

/**
 * Controlador: conecta las vistas con los repositorios (la vista no accede al modelo).
 * Cubre: RF-01..RF-06, RNF-04, RNF-06
 */
public class Controlador {

    private final Repositorio<Administrativo> administrativos = new Repositorio<>();
    private final Repositorio<Obrero> obreros = new Repositorio<>();
    private final Repositorio<Tecnico> tecnicos = new Repositorio<>();

    // ---------- Registro ----------

    public String registrarAdministrativo(String nombre, String cedulaTxt, String cargo) {
        long cedula = validarBase(nombre, cedulaTxt);
        exigir(cargo, "El cargo");
        administrativos.registrar(new Administrativo(nombre.trim(), cedula, cargo.trim()));
        return "Administrativo registrado correctamente.";
    }

    public String registrarObrero(String nombre, String cedulaTxt, String codigo) {
        long cedula = validarBase(nombre, cedulaTxt);
        exigir(codigo, "El código");
        obreros.registrar(new Obrero(nombre.trim(), cedula, codigo.trim()));
        return "Obrero registrado correctamente.";
    }

    public String registrarTecnico(String nombre, String cedulaTxt, String codigo, String clave) {
        long cedula = validarBase(nombre, cedulaTxt);
        exigir(codigo, "El código");
        exigir(clave, "La clave de acceso");
        tecnicos.registrar(new Tecnico(nombre.trim(), cedula, codigo.trim(), clave.trim()));
        return "Técnico registrado correctamente.";
    }

    // ---------- Consulta ----------

    public String consultarAdministrativo(String cedulaTxt) {
        return mostrar(administrativos.buscarPorCedula(parsearCedula(cedulaTxt)));
    }

    public String consultarObrero(String cedulaTxt) {
        return mostrar(obreros.buscarPorCedula(parsearCedula(cedulaTxt)));
    }

    public String consultarTecnico(String cedulaTxt) {
        return mostrar(tecnicos.buscarPorCedula(parsearCedula(cedulaTxt)));
    }

    // ---------- Listado ----------

    public String listarAdministrativos() {
        return Repositorio.listadoTexto(administrativos.listar());
    }

    public String listarObreros() {
        return Repositorio.listadoTexto(obreros.listar());
    }

    public String listarTecnicos() {
        return Repositorio.listadoTexto(tecnicos.listar());
    }

    // ---------- Validaciones (RF-06) ----------

    /** Valida nombre y cédula; devuelve la cédula ya convertida. */
    private long validarBase(String nombre, String cedulaTxt) {
        exigir(nombre, "El nombre");
        long cedula = parsearCedula(cedulaTxt);
        if (administrativos.existe(cedula) || obreros.existe(cedula) || tecnicos.existe(cedula)) {
            throw new IllegalArgumentException("La cédula " + cedula + " ya está registrada.");
        }
        return cedula;
    }

    private void exigir(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
    }

    private long parsearCedula(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria.");
        }
        String t = texto.trim();
        if (!t.matches("\\d{10}")) {
            throw new IllegalArgumentException("La cédula debe ser numérica y tener exactamente 10 dígitos.");
        }
        return Long.parseLong(t);
    }

    private String mostrar(Persona p) {
        if (p == null) {
            return "Empleado no encontrado.";
        }
        return p.informacion();
    }
}
