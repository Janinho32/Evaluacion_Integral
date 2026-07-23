package edu.universidad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase Estudiante — extiende Persona e implementa IEvaluable.
 * Demuestra: herencia, encapsulamiento, polimorfismo, uso de colecciones,
 * uso de Math (promedio, redondeo, abs) y String (validación de código).
 */
public class Estudiante extends Persona implements IEvaluable {

    // ─── Atributos propios (encapsulados) ─────────────────────────────────────
    private String codigo;
    private String carrera;
    private int    semestre;
    private int    creditosAcumulados;

    /**
     * Colección de notas — ArrayList para acceso por índice y orden dinámico.
     * Permite demostrar recorrido, búsqueda y ordenamiento.
     */
    private List<Double> notas;

    // ─── Constructores ─────────────────────────────────────────────────────────

    public Estudiante(int id, String nombre, String apellido, String email,
                      String telefono, String codigo, String carrera,
                      int semestre, int creditosAcumulados) {
        super(id, nombre, apellido, email, telefono);
        this.codigo              = codigo.trim().toUpperCase(); // String.toUpperCase
        this.carrera             = carrera.trim();
        this.semestre            = semestre;
        this.creditosAcumulados  = creditosAcumulados;
        this.notas               = new ArrayList<>();
    }

    public Estudiante(String nombre, String apellido, String email,
                      String telefono, String codigo, String carrera,
                      int semestre) {
        this(0, nombre, apellido, email, telefono, codigo, carrera, semestre, 0);
    }

    // ─── Implementación polimórfica (Persona) ─────────────────────────────────

    @Override
    public String getTipoPersona() {
        return "Estudiante";
    }

    // ─── Implementación de IEvaluable ─────────────────────────────────────────

    /**
     * Calcula el promedio usando Math.round para redondear a 2 decimales.
     */
    @Override
    public double calcularPromedio() {
        if (notas.isEmpty()) return 0.0;
        double suma = 0.0;
        for (double nota : notas) {
            suma += nota;
        }
        double prom = suma / notas.size();
        // Uso de Math.round para redondear a 2 decimales
        return Math.round(prom * 100.0) / 100.0;
    }

    /**
     * Categoría académica según el promedio (uso de Math.floor, comparaciones).
     */
    @Override
    public String getCategoria() {
        double prom = calcularPromedio();
        if (prom >= 17.0)      return "Excelente";
        else if (prom >= 14.0) return "Bueno";
        else if (prom >= 11.0) return "Regular";
        else if (prom > 0)     return "Deficiente";
        else                   return "Sin notas";
    }

    @Override
    public boolean aprueba() {
        return calcularPromedio() >= 11.0;
    }

    // ─── Métodos de colección (ArrayList) ─────────────────────────────────────

    /** Agrega una nota validando rango [0, 20] con Math.min y Math.max. */
    public void agregarNota(double nota) {
        double notaValida = Math.min(20.0, Math.max(0.0, nota)); // Math.min / Math.max
        notas.add(notaValida);
    }

    /** Retorna la nota máxima usando Math.max en recorrido. */
    public double getNotaMaxima() {
        if (notas.isEmpty()) return 0.0;
        double max = notas.get(0);
        for (double n : notas) {
            max = Math.max(max, n);  // Math.max
        }
        return max;
    }

    /** Retorna la nota mínima usando Math.min. */
    public double getNotaMinima() {
        if (notas.isEmpty()) return 0.0;
        double min = notas.get(0);
        for (double n : notas) {
            min = Math.min(min, n);  // Math.min
        }
        return min;
    }

    /** Ordena y retorna copia de la colección de notas. */
    public List<Double> getNotasOrdenadas() {
        List<Double> copia = new ArrayList<>(notas);
        Collections.sort(copia);
        return copia;
    }

    // ─── Validación con String ─────────────────────────────────────────────────

    /**
     * Valida que el código tenga formato correcto (String.matches, String.length).
     * Formato esperado: letras seguidas de dígitos, p.ej. "EST001"
     */
    public boolean codigoEsValido() {
        if (codigo == null || codigo.length() < 3) return false;
        return codigo.matches("[A-Z]{2,4}\\d{3,6}"); // String.matches con regex
    }

    // ─── Getters y Setters ─────────────────────────────────────────────────────

    public String getCodigo()                          { return codigo; }
    public void   setCodigo(String c)                  { this.codigo = c != null ? c.trim().toUpperCase() : ""; }

    public String getCarrera()                         { return carrera; }
    public void   setCarrera(String c)                 { this.carrera = c != null ? c.trim() : ""; }

    public int    getSemestre()                        { return semestre; }
    public void   setSemestre(int s)                   { this.semestre = Math.max(1, s); } // Math.max

    public int    getCreditosAcumulados()              { return creditosAcumulados; }
    public void   setCreditosAcumulados(int c)         { this.creditosAcumulados = Math.max(0, c); }

    public List<Double> getNotas()                     { return new ArrayList<>(notas); }
    public void         setNotas(List<Double> notas)   { this.notas = notas != null ? notas : new ArrayList<>(); }

    @Override
    public String toString() {
        return String.format("Estudiante[%s] %s | %s | Sem.%d | Prom.%.2f (%s)",
                getCodigo(), getNombreCompleto(), getCarrera(),
                getSemestre(), calcularPromedio(), getCategoria());
    }
}
