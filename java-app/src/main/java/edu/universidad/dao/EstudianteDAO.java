package edu.universidad.dao;

import edu.universidad.model.Estudiante;
import edu.universidad.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EstudianteDAO — Acceso a datos para la entidad Estudiante.
 * Implementa CRUD completo con JDBC (persistencia de datos).
 * Usa colecciones (ArrayList) para retornar listas de estudiantes.
 */
public class EstudianteDAO {

    // ─── Listar todos ─────────────────────────────────────────────────────────

    /**
     * Retorna todos los estudiantes. Demuestra uso de colecciones (ArrayList)
     * y recorrido con ResultSet (estructura repetitiva).
     */
    public List<Estudiante> listarTodos() throws SQLException {
        List<Estudiante> lista = new ArrayList<>(); // colección ArrayList
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   e.codigo, e.carrera, e.semestre, e.creditos_acumulados
            FROM persona p
            JOIN estudiante e ON p.id = e.id
            ORDER BY p.apellido, p.nombre
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {                       // estructura repetitiva
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ─── Buscar por ID ────────────────────────────────────────────────────────

    public Estudiante buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   e.codigo, e.carrera, e.semestre, e.creditos_acumulados
            FROM persona p
            JOIN estudiante e ON p.id = e.id
            WHERE p.id = ?
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ─── Buscar por código ────────────────────────────────────────────────────

    public Estudiante buscarPorCodigo(String codigo) throws SQLException {
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   e.codigo, e.carrera, e.semestre, e.creditos_acumulados
            FROM persona p
            JOIN estudiante e ON p.id = e.id
            WHERE e.codigo = ?
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ─── Buscar por texto (nombre, apellido, código) ──────────────────────────

    public List<Estudiante> buscar(String texto) throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String like = "%" + texto.toLowerCase() + "%"; // String.toLowerCase
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   e.codigo, e.carrera, e.semestre, e.creditos_acumulados
            FROM persona p
            JOIN estudiante e ON p.id = e.id
            WHERE LOWER(p.nombre) LIKE ?
               OR LOWER(p.apellido) LIKE ?
               OR LOWER(e.codigo) LIKE ?
               OR LOWER(e.carrera) LIKE ?
            ORDER BY p.apellido
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like); ps.setString(2, like);
            ps.setString(3, like); ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ─── Insertar ─────────────────────────────────────────────────────────────

    public int insertar(Estudiante est) throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        conn.setAutoCommit(false); // transacción JDBC
        try {
            // 1. Insertar en persona
            String sqlP = "INSERT INTO persona(nombre,apellido,email,telefono,tipo) VALUES(?,?,?,?,?)";
            int personaId;
            try (PreparedStatement ps = conn.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, est.getNombre());
                ps.setString(2, est.getApellido());
                ps.setString(3, est.getEmail());
                ps.setString(4, est.getTelefono());
                ps.setString(5, "Estudiante");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    personaId = rs.getInt(1);
                }
            }
            // 2. Insertar en estudiante
            String sqlE = "INSERT INTO estudiante(id,codigo,carrera,semestre,creditos_acumulados) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlE)) {
                ps.setInt(1, personaId);
                ps.setString(2, est.getCodigo());
                ps.setString(3, est.getCarrera());
                ps.setInt(4, est.getSemestre());
                ps.setInt(5, est.getCreditosAcumulados());
                ps.executeUpdate();
            }
            conn.commit();
            return personaId;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ─── Actualizar ───────────────────────────────────────────────────────────

    public void actualizar(Estudiante est) throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        conn.setAutoCommit(false);
        try {
            String sqlP = "UPDATE persona SET nombre=?,apellido=?,email=?,telefono=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlP)) {
                ps.setString(1, est.getNombre());  ps.setString(2, est.getApellido());
                ps.setString(3, est.getEmail());   ps.setString(4, est.getTelefono());
                ps.setInt(5, est.getId());
                ps.executeUpdate();
            }
            String sqlE = "UPDATE estudiante SET codigo=?,carrera=?,semestre=?,creditos_acumulados=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlE)) {
                ps.setString(1, est.getCodigo());  ps.setString(2, est.getCarrera());
                ps.setInt(3, est.getSemestre());   ps.setInt(4, est.getCreditosAcumulados());
                ps.setInt(5, est.getId());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ─── Eliminar ─────────────────────────────────────────────────────────────

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ─── Conteo ───────────────────────────────────────────────────────────────

    public int contar() throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM estudiante")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ─── Mapeo ResultSet → Estudiante ─────────────────────────────────────────

    private Estudiante mapear(ResultSet rs) throws SQLException {
        return new Estudiante(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("email"),
            rs.getString("telefono"),
            rs.getString("codigo"),
            rs.getString("carrera"),
            rs.getInt("semestre"),
            rs.getInt("creditos_acumulados")
        );
    }
}
