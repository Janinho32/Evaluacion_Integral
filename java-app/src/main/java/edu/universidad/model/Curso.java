package edu.universidad.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Curso — representa una asignatura del sistema universitario.
 * Usa colecciones internas, Math y String para reglas de negocio.
 */
public class Curso {

    // ─── Atributos encapsulados ───────────────────────────────────────────────
    private int     id;
    private String  codigo;
    private String  nombre;
    private int     creditos;
    private int     cupoMaximo;
    private int     docenteId;
    private String  nombreDocente;

    /** Colección de IDs de estudiantes matriculados (ArrayList). */
    private List<Integer> estudiantesMatriculados;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Curso(int id, String codigo, String nombre, int creditos,
                 int cupoMaximo, int docenteId) {
        this.id                      = id;
        this.codigo                  = codigo.trim().toUpperCase();
        this.nombre                  = nombre.trim();
        this.creditos                = Math.max(1, creditos);  // Math.max
        this.cupoMaximo              = Math.max(1, cupoMaximo);
        this.docenteId               = docenteId;
        this.estudiantesMatriculados = new ArrayList<>();
    }

    public Curso(String codigo, String nombre, int creditos, int cupoMaximo, int docenteId) {
        this(0, codigo, nombre, creditos, cupoMaximo, docenteId);
    }

    // ─── Métodos de negocio ───────────────────────────────────────────────────

    /**
     * Determina si hay cupo disponible.
     * @param matriculadosActuales número actual de matriculados
     */
    public boolean hayCupo(int matriculadosActuales) {
        return matriculadosActuales < cupoMaximo;
    }

    /**
     * Porcentaje de ocupación usando Math.min (tope en 100%).
     */
    public int getPorcentajeOcupacion(int matriculadosActuales) {
        if (cupoMaximo == 0) return 0;
        double pct = (double) matriculadosActuales / cupoMaximo * 100.0;
        return (int) Math.min(100.0, Math.round(pct)); // Math.min + Math.round
    }

    /**
     * Código normalizado: String.toUpperCase + String.replaceAll.
     */
    public String getCodigoNormalizado() {
        return codigo.toUpperCase().replaceAll("\\s+", "-"); // String.replaceAll
    }

    /**
     * Nombre truncado para vistas compactas (String.substring).
     */
    public String getNombreCorto(int maxLen) {
        if (nombre.length() <= maxLen) return nombre;
        return nombre.substring(0, maxLen - 3) + "..."; // String.substring
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public int     getId()                                { return id; }
    public void    setId(int id)                          { this.id = id; }

    public String  getCodigo()                            { return codigo; }
    public void    setCodigo(String c)                    { this.codigo = c != null ? c.trim().toUpperCase() : ""; }

    public String  getNombre()                            { return nombre; }
    public void    setNombre(String n)                    { this.nombre = n != null ? n.trim() : ""; }

    public int     getCreditos()                          { return creditos; }
    public void    setCreditos(int c)                     { this.creditos = Math.max(1, c); }

    public int     getCupoMaximo()                        { return cupoMaximo; }
    public void    setCupoMaximo(int c)                   { this.cupoMaximo = Math.max(1, c); }

    public int     getDocenteId()                         { return docenteId; }
    public void    setDocenteId(int d)                    { this.docenteId = d; }

    public String  getNombreDocente()                     { return nombreDocente; }
    public void    setNombreDocente(String n)             { this.nombreDocente = n; }

    public List<Integer> getEstudiantesMatriculados()                    { return new ArrayList<>(estudiantesMatriculados); }
    public void          setEstudiantesMatriculados(List<Integer> lista) { this.estudiantesMatriculados = lista != null ? lista : new ArrayList<>(); }

    @Override
    public String toString() {
        return String.format("Curso[%s] %s | %d créditos | Cupo: %d",
                codigo, nombre, creditos, cupoMaximo);
    }
}
