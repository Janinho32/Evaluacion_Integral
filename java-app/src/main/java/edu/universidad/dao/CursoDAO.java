package edu.universidad.dao;

import edu.universidad.model.Curso;
import edu.universidad.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CursoDAO — Acceso a datos para la entidad Curso (JDBC).
 */
public class CursoDAO {

    public List<Curso> listarTodos() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = """
            SELECT c.id, c.codigo, c.nombre, c.creditos, c.cupo_maximo, c.docente_id,
                   COALESCE(p.nombre || ' ' || p.apellido, 'Sin asignar') AS nombre_docente
            FROM curso c
            LEFT JOIN docente d ON c.docente_id = d.id
            LEFT JOIN persona p ON d.id = p.id
            ORDER BY c.codigo
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Curso buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT c.id, c.codigo, c.nombre, c.creditos, c.cupo_maximo, c.docente_id,
                   COALESCE(p.nombre || ' ' || p.apellido, 'Sin asignar') AS nombre_docente
            FROM curso c
            LEFT JOIN docente d ON c.docente_id = d.id
            LEFT JOIN persona p ON d.id = p.id
            WHERE c.id = ?
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

    public int insertar(Curso curso) throws SQLException {
        String sql = "INSERT INTO curso(codigo,nombre,creditos,cupo_maximo,docente_id) VALUES(?,?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getNombre());
            ps.setInt(3, curso.getCreditos());
            ps.setInt(4, curso.getCupoMaximo());
            if (curso.getDocenteId() > 0) ps.setInt(5, curso.getDocenteId());
            else ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void actualizar(Curso curso) throws SQLException {
        String sql = "UPDATE curso SET codigo=?,nombre=?,creditos=?,cupo_maximo=?,docente_id=? WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getNombre());
            ps.setInt(3, curso.getCreditos());
            ps.setInt(4, curso.getCupoMaximo());
            if (curso.getDocenteId() > 0) ps.setInt(5, curso.getDocenteId());
            else ps.setNull(5, Types.INTEGER);
            ps.setInt(6, curso.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM curso WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contar() throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM curso")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Cuenta cuántos estudiantes están matriculados en un curso dado. */
    public int contarMatriculados(int cursoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM matricula WHERE curso_id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cursoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Curso mapear(ResultSet rs) throws SQLException {
        Curso c = new Curso(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getInt("creditos"),
            rs.getInt("cupo_maximo"),
            rs.getInt("docente_id")
        );
        c.setNombreDocente(rs.getString("nombre_docente"));
        return c;
    }
}
