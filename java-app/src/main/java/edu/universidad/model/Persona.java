package edu.universidad.model;

/**
 * Clase abstracta Persona — base de la jerarquía de herencia.
 * Encapsula los atributos comunes de toda persona en el sistema universitario
 * y obliga a subclases a implementar getTipoPersona() (polimorfismo).
 *
 * Uso de String: validación de email, formateo de nombres.
 */
public abstract class Persona {

    // ─── Atributos encapsulados ────────────────────────────────────────────────
    private int    id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    // ─── Constructores ─────────────────────────────────────────────────────────

    /** Constructor completo */
    public Persona(int id, String nombre, String apellido, String email, String telefono) {
        this.id       = id;
        this.nombre   = nombre.trim();
        this.apellido = apellido.trim();
        this.email    = email.trim().toLowerCase(); // String.toLowerCase()
        this.telefono = telefono != null ? telefono.trim() : "";
    }

    /** Constructor sin id (para inserciones nuevas) */
    public Persona(String nombre, String apellido, String email, String telefono) {
        this(0, nombre, apellido, email, telefono);
    }

    // ─── Método abstracto (polimorfismo) ───────────────────────────────────────

    /**
     * Cada subclase devuelve su tipo descriptivo.
     * Esto permite polimorfismo al recorrer colecciones de Persona.
     */
    public abstract String getTipoPersona();

    // ─── Métodos concretos con uso de String ──────────────────────────────────

    /**
     * Retorna nombre y apellido formateados (String.toUpperCase, concat).
     */
    public String getNombreCompleto() {
        // Uso de String: substring para capitalizar, concat para unir
        String n = capitalizarPalabra(nombre);
        String a = capitalizarPalabra(apellido);
        return n + " " + a;
    }

    /**
     * Valida formato básico de email usando String.contains y String.indexOf.
     */
    public boolean emailEsValido() {
        if (email == null || email.isEmpty()) return false;
        int arroba = email.indexOf('@');             // String.indexOf
        int punto  = email.lastIndexOf('.');         // String.lastIndexOf
        return arroba > 0 && punto > arroba + 1 && punto < email.length() - 1;
    }

    /**
     * Retorna las iniciales del nombre (uso de String.charAt, String.toUpperCase).
     */
    public String getIniciales() {
        String inicialN = String.valueOf(nombre.charAt(0)).toUpperCase();
        String inicialA = String.valueOf(apellido.charAt(0)).toUpperCase();
        return inicialN + inicialA;
    }

    // ─── Método auxiliar privado ──────────────────────────────────────────────

    private String capitalizarPalabra(String s) {
        if (s == null || s.isEmpty()) return s;
        // String.substring + String.toUpperCase + String.toLowerCase
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("[%s] ID=%d | %s | %s",
                getTipoPersona(), id, getNombreCompleto(), email);
    }

    // ─── Getters y Setters (encapsulamiento) ──────────────────────────────────

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public String getNombre()            { return nombre; }
    public void setNombre(String n)      { this.nombre = n != null ? n.trim() : ""; }

    public String getApellido()          { return apellido; }
    public void setApellido(String a)    { this.apellido = a != null ? a.trim() : ""; }

    public String getEmail()             { return email; }
    public void setEmail(String e)       { this.email = e != null ? e.trim().toLowerCase() : ""; }

    public String getTelefono()          { return telefono; }
    public void setTelefono(String t)    { this.telefono = t != null ? t.trim() : ""; }
}
