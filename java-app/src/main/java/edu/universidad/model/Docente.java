package edu.universidad.model;

/**
 * Clase Docente — extiende Persona (herencia).
 * Representa a los docentes del sistema universitario.
 * Demuestra herencia, encapsulamiento y sobrescritura de métodos.
 */
public class Docente extends Persona {

    // ─── Atributos propios ────────────────────────────────────────────────────
    private String codigo;
    private String especialidad;
    private String categoria;  // Ordinario, Contratado, Invitado
    private int    horasCatedra;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Docente(int id, String nombre, String apellido, String email,
                   String telefono, String codigo, String especialidad,
                   String categoria, int horasCatedra) {
        super(id, nombre, apellido, email, telefono);
        this.codigo        = codigo.trim().toUpperCase();
        this.especialidad  = especialidad.trim();
        this.categoria     = categoria.trim();
        this.horasCatedra  = horasCatedra;
    }

    public Docente(String nombre, String apellido, String email,
                   String telefono, String codigo, String especialidad,
                   String categoria, int horasCatedra) {
        this(0, nombre, apellido, email, telefono, codigo, especialidad, categoria, horasCatedra);
    }

    // ─── Sobrescritura polimórfica ────────────────────────────────────────────

    /**
     * Sobrescribe getTipoPersona() — polimorfismo en acción.
     * Al iterar una colección List<Persona>, cada objeto muestra su tipo real.
     */
    @Override
    public String getTipoPersona() {
        return "Docente";
    }

    // ─── Métodos de negocio ───────────────────────────────────────────────────

    /**
     * Categoría formateada con uso de String.equalsIgnoreCase.
     */
    public String getCategoriaDescriptiva() {
        if ("ordinario".equalsIgnoreCase(categoria)) {
            return "Docente Ordinario (Tiempo Completo)";
        } else if ("contratado".equalsIgnoreCase(categoria)) {
            return "Docente Contratado (Tiempo Parcial)";
        } else if ("invitado".equalsIgnoreCase(categoria)) {
            return "Docente Invitado (Especial)";
        }
        return categoria;
    }

    /**
     * Carga horaria: horas remuneradas (Math.ceil para redondear hacia arriba).
     */
    public int getHorasRemuneradas() {
        double factor = "ordinario".equalsIgnoreCase(categoria) ? 1.0 : 0.8;
        return (int) Math.ceil(horasCatedra * factor); // Math.ceil
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public String getCodigo()                  { return codigo; }
    public void   setCodigo(String c)          { this.codigo = c != null ? c.trim().toUpperCase() : ""; }

    public String getEspecialidad()            { return especialidad; }
    public void   setEspecialidad(String e)    { this.especialidad = e != null ? e.trim() : ""; }

    public String getCategoria()               { return categoria; }
    public void   setCategoria(String c)       { this.categoria = c != null ? c.trim() : "Contratado"; }

    public int    getHorasCatedra()            { return horasCatedra; }
    public void   setHorasCatedra(int h)       { this.horasCatedra = Math.max(0, h); }

    @Override
    public String toString() {
        return String.format("Docente[%s] %s | %s | %s | %dh",
                getCodigo(), getNombreCompleto(), getEspecialidad(),
                getCategoria(), getHorasCatedra());
    }
}
