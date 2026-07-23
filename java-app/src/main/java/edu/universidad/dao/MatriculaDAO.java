package edu.universidad.dao;

import edu.universidad.model.Matricula;
import edu.universidad.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MatriculaDAO — Acceso a datos para la entidad Matricula (JDBC).
 * Demuestra JOIN de 3 tablas, búsqueda, filtrado y colecciones.
 */
public class MatriculaDAO {

    private static final String SQL_BASE = """
            SELECT m.id, m.estudiante_id, m.curso_id, m.nota, m.fecha, m.estado,
                   p.nombre || ' ' || p.apellido AS nombre_estudiante,
                   e.codigo AS codigo_estudiante,
                   c.nombre AS nombre_curso,
                   c.codigo AS codigo_curso
            FROM matricula m
            JOIN estudiante e  ON m.estudiante_id = e.id
            JOIN persona p     ON e.id = p.id
            JOIN curso c       ON m.curso_id = c.id
            """;

    public List<Matricula> listarTodas() throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = SQL_BASE + " ORDER BY m.id DESC";
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Matricula> listarPorEstudiante(int estudianteId) throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = SQL_BASE + " WHERE m.estudiante_id = ? ORDER BY c.nombre";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Matricula> listarPorCurso(int cursoId) throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = SQL_BASE + " WHERE m.curso_id = ? ORDER BY p.apellido, p.nombre";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Matricula buscarPorId(int id) throws SQLException {
        String sql = SQL_BASE + " WHERE m.id = ?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /** Verifica si ya existe una matrícula para el par estudiante-curso. */
    public boolean existeMatricula(int estudianteId, int cursoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM matricula WHERE estudiante_id=? AND curso_id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId); ps.setInt(2, cursoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public int insertar(Matricula mat) throws SQLException {
        String sql = "INSERT INTO matricula(estudiante_id,curso_id,nota,fecha,estado) VALUES(?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mat.getEstudianteId());
            ps.setInt(2, mat.getCursoId());
            ps.setDouble(3, mat.getNota());
            ps.setDate(4, Date.valueOf(mat.getFecha() != null ? mat.getFecha() : LocalDate.now()));
            ps.setString(5, mat.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizarNota(int id, double nota) throws SQLException {
        String estado = nota >= Matricula.NOTA_APROBACION ? "Aprobada"
                      : nota > 0 ? "Desaprobada" : "Activa";
        String sql = "UPDATE matricula SET nota=?, estado=? WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nota); ps.setString(2, estado); ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM matricula WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contar() throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM matricula")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Promedio general del sistema. */
    public double promedioGeneral() throws SQLException {
        String sql = "SELECT AVG(nota) FROM matricula WHERE nota > 0";
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                double val = rs.getDouble(1);
                return Math.round(val * 100.0) / 100.0; // Math.round
            }
        }
        return 0.0;
    }

    /** Cuántas matrículas están aprobadas. */
    public int contarAprobadas() throws SQLException {
        String sql = "SELECT COUNT(*) FROM matricula WHERE estado='Aprobada'";
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Matricula mapear(ResultSet rs) throws SQLException {
        Date fecha = rs.getDate("fecha");
        Matricula m = new Matricula(
            rs.getInt("id"),
            rs.getInt("estudiante_id"),
            rs.getInt("curso_id"),
            rs.getDouble("nota"),
            fecha != null ? fecha.toLocalDate() : LocalDate.now(),
            rs.getString("estado")
        );
        m.setNombreEstudiante(rs.getString("nombre_estudiante"));
        m.setCodigoEstudiante(rs.getString("codigo_estudiante"));
        m.setNombreCurso(rs.getString("nombre_curso"));
        m.setCodigoCurso(rs.getString("codigo_curso"));
        return m;
    }
}
