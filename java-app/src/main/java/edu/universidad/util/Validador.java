package edu.universidad.util;

import java.util.regex.Pattern;

/**
 * Validador — clase utilitaria con métodos estáticos para validación y
 * procesamiento de datos. Demuestra el uso extenso de clases nativas
 * Math y String del curso.
 */
public class Validador {

    // ─── Constantes ───────────────────────────────────────────────────────────
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private Validador() { /* clase utilitaria, no instanciable */ }

    // ─── Validaciones con String ──────────────────────────────────────────────

    /** Valida email usando String y Pattern. */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
    }

    /** Valida que el texto no esté vacío (String.trim + isEmpty). */
    public static boolean noEsVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Valida que un código tenga el formato LETRAS+NUMEROS.
     * Uso de String.matches (expresión regular).
     */
    public static boolean esCodigoValido(String codigo) {
        if (codigo == null || codigo.isEmpty()) return false;
        return codigo.trim().toUpperCase().matches("[A-Z]{2,5}\\d{2,6}");
    }

    /** Verifica longitud mínima con String.length. */
    public static boolean longitudMinima(String texto, int min) {
        return texto != null && texto.trim().length() >= min;
    }

    /** Normaliza un nombre: recorta, capitaliza cada palabra. */
    public static String normalizarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return "";
        String[] partes = nombre.trim().split("\\s+"); // String.split
        StringBuilder sb = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                // String.substring + String.toUpperCase + String.toLowerCase
                sb.append(parte.substring(0, 1).toUpperCase())
                  .append(parte.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim(); // String.trim
    }

    /**
     * Genera código automático a partir de las iniciales + número.
     * Uso de String.charAt, String.toUpperCase, String.format.
     */
    public static String generarCodigo(String prefijo, int numero) {
        String pref = prefijo.trim().toUpperCase().replaceAll("[^A-Z]", ""); // String ops
        return String.format("%s%03d", pref.substring(0, Math.min(3, pref.length())), numero);
    }

    // ─── Cálculos con Math ────────────────────────────────────────────────────

    /** Redondea a N decimales usando Math.round. */
    public static double redondear(double valor, int decimales) {
        double factor = Math.pow(10, decimales); // Math.pow
        return Math.round(valor * factor) / factor; // Math.round
    }

    /** Porcentaje con tope de 100 usando Math.min. */
    public static double porcentaje(double parte, double total) {
        if (total <= 0) return 0.0;
        return Math.min(100.0, Math.round((parte / total) * 10000.0) / 100.0); // Math.min, Math.round
    }

    /** Valor absoluto de diferencia (Math.abs). */
    public static double diferencia(double a, double b) {
        return Math.abs(a - b); // Math.abs
    }

    /** Califica nota: usa Math.floor para obtener entero. */
    public static String calificarNota(double nota) {
        int n = (int) Math.floor(nota); // Math.floor
        if (n >= 18) return "AD (Sobresaliente)";
        if (n >= 14) return "A (Bueno)";
        if (n >= 11) return "B (Regular)";
        if (n >=  0) return "C (Deficiente)";
        return "Sin nota";
    }

    /**
     * Estadísticas de un arreglo de notas usando Math.
     * @return double[] con {promedio, min, max, desviacion}
     */
    public static double[] estadisticas(double[] notas) {
        if (notas == null || notas.length == 0) return new double[]{0, 0, 0, 0};

        double suma = 0, min = notas[0], max = notas[0];

        // Estructura repetitiva: for-each
        for (double n : notas) {
            suma += n;
            min   = Math.min(min, n);  // Math.min
            max   = Math.max(max, n);  // Math.max
        }
        double prom = suma / notas.length;

        // Desviación estándar con Math.sqrt y Math.pow
        double varianza = 0;
        for (double n : notas) {
            varianza += Math.pow(n - prom, 2); // Math.pow
        }
        double desv = Math.sqrt(varianza / notas.length); // Math.sqrt

        return new double[]{
            redondear(prom, 2),
            redondear(min, 2),
            redondear(max, 2),
            redondear(desv, 2)
        };
    }

    /** Valida rango numérico con Math.min/max. */
    public static boolean enRango(double valor, double min, double max) {
        return valor >= Math.min(min, max) && valor <= Math.max(min, max);
    }
}
