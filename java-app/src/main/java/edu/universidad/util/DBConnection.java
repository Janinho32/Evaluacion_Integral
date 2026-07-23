package edu.universidad.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection — Singleton para la conexión JDBC con H2 (base de datos embebida).
 * Patrón Singleton: asegura una única instancia de conexión por sesión.
 *
 * H2 almacena el archivo en el directorio del usuario (~/.universidad_db).
 * No requiere instalación de servidor externo.
 */
public class DBConnection {

    // URL de conexión H2 en modo archivo (persistente entre reinicios)
    private static final String URL      = "jdbc:h2:~/.universidad_db/universidad;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String USER     = "sa";
    private static final String PASSWORD = "";

    // ─── Instancia única (Singleton) ─────────────────────────────────────────
    private static DBConnection instancia;
    private Connection          conexion;

    // ─── Constructor privado ──────────────────────────────────────────────────
    private DBConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 no encontrado: " + e.getMessage());
        }
    }

    // ─── Obtener instancia Singleton ─────────────────────────────────────────
    public static synchronized DBConnection getInstance() throws SQLException {
        if (instancia == null || instancia.conexion.isClosed()) {
            instancia = new DBConnection();
        }
        return instancia;
    }

    // ─── Obtener conexión activa ──────────────────────────────────────────────
    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }

    // ─── Cerrar conexión ─────────────────────────────────────────────────────
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
