package edu.universidad.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase Matricula — registra la inscripción de un Estudiante en un Curso.
 * Usa String (formateo de fechas) y Math (cálculo de estado según nota).
 */
public class Matricula {

    // ─── Constantes ───────────────────────────────────────────────────────────
    public static final double NOTA_APROBACION = 11.0;
    public static final double NOTA_MAXIMA     = 20.0;

    // ─── Atributos encapsulados ───────────────────────────────────────────────
    private int        id;
    private int        estudianteId;
    private int        cursoId;
    private double     nota;
    private LocalDate  fecha;
    private String     estado; // Activa, Aprobada, Desaprobada, Retirada

    // Desnormalizados para visualización
    private String nombreEstudiante;
    private String codigoEstudiante;
    private String nombreCurso;
    private String codigoCurso;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Matricula(int id, int estudianteId, int cursoId, double nota,
                     LocalDate fecha, String estado) {
        this.id           = id;
        this.estudianteId = estudianteId;
        this.cursoId      = cursoId;
        this.nota         = Math.min(NOTA_MAXIMA, Math.max(0.0, nota)); // Math
        this.fecha        = fecha != null ? fecha : LocalDate.now();
        this.estado       = estado != null ? estado : "Activa";
    }

    public Matricula(int estudianteId, int cursoId) {
        this(0, estudianteId, cursoId, 0.0, LocalDate.now(), "Activa");
    }

    // ─── Métodos de negocio ───────────────────────────────────────────────────

    /**
     * Actualiza el estado según la nota usando estructuras selectivas.
     */
    public void actualizarEstado() {
        if ("Retirada".equalsIgnoreCase(estado)) return; // ya retirado, no cambiar
        if (nota >= NOTA_APROBACION) {
            estado = "Aprobada";
        } else if (nota > 0) {
            estado = "Desaprobada";
        } else {
            estado = "Activa";
        }
    }

    /**
     * Fecha formateada para visualización (String + DateTimeFormatter).
     */
    public String getFechaFormateada() {
        if (fecha == null) return "Sin fecha";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(fmt); // String resultado
    }

    /**
     * Retorna la nota como String con 2 decimales (String.format).
     */
    public String getNotaFormateada() {
        if (nota <= 0.0) return "—";
        return String.format("%.2f", nota); // String.format
    }

    /**
     * Calcula la diferencia entre la nota y el mínimo aprobatorio (Math.abs).
     */
    public double getDiferenciaAprobacion() {
        return Math.abs(nota - NOTA_APROBACION); // Math.abs
    }

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public int       getId()                          { return id; }
    public void      setId(int id)                    { this.id = id; }

    public int       getEstudianteId()                { return estudianteId; }
    public void      setEstudianteId(int e)           { this.estudianteId = e; }

    public int       getCursoId()                     { return cursoId; }
    public void      setCursoId(int c)                { this.cursoId = c; }

    public double    getNota()                        { return nota; }
    public void      setNota(double n) {
        this.nota = Math.min(NOTA_MAXIMA, Math.max(0.0, n));
        actualizarEstado();
    }

    public LocalDate getFecha()                       { return fecha; }
    public void      setFecha(LocalDate f)            { this.fecha = f; }

    public String    getEstado()                      { return estado; }
    public void      setEstado(String e)              { this.estado = e; }

    public String    getNombreEstudiante()             { return nombreEstudiante; }
    public void      setNombreEstudiante(String n)    { this.nombreEstudiante = n; }

    public String    getCodigoEstudiante()             { return codigoEstudiante; }
    public void      setCodigoEstudiante(String c)    { this.codigoEstudiante = c; }

    public String    getNombreCurso()                 { return nombreCurso; }
    public void      setNombreCurso(String n)         { this.nombreCurso = n; }

    public String    getCodigoCurso()                 { return codigoCurso; }
    public void      setCodigoCurso(String c)         { this.codigoCurso = c; }

    @Override
    public String toString() {
        return String.format("Matricula[ID=%d] Est.%d → Curso %d | Nota: %.2f | %s | %s",
                id, estudianteId, cursoId, nota, estado, getFechaFormateada());
    }
}
