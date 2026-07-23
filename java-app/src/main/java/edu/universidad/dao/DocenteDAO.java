package edu.universidad.dao;

import edu.universidad.model.Docente;
import edu.universidad.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DocenteDAO — Acceso a datos para la entidad Docente (JDBC).
 */
public class DocenteDAO {

    public List<Docente> listarTodos() throws SQLException {
        List<Docente> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   d.codigo, d.especialidad, d.categoria, d.horas_catedra
            FROM persona p
            JOIN docente d ON p.id = d.id
            ORDER BY p.apellido, p.nombre
            """;
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Docente buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT p.id, p.nombre, p.apellido, p.email, p.telefono,
                   d.codigo, d.especialidad, d.categoria, d.horas_catedra
            FROM persona p
            JOIN docente d ON p.id = d.id
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

    public int insertar(Docente doc) throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        conn.setAutoCommit(false);
        try {
            String sqlP = "INSERT INTO persona(nombre,apellido,email,telefono,tipo) VALUES(?,?,?,?,?)";
            int personaId;
            try (PreparedStatement ps = conn.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, doc.getNombre());
                ps.setString(2, doc.getApellido());
                ps.setString(3, doc.getEmail());
                ps.setString(4, doc.getTelefono());
                ps.setString(5, "Docente");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    personaId = rs.getInt(1);
                }
            }
            String sqlD = "INSERT INTO docente(id,codigo,especialidad,categoria,horas_catedra) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlD)) {
                ps.setInt(1, personaId);
                ps.setString(2, doc.getCodigo());
                ps.setString(3, doc.getEspecialidad());
                ps.setString(4, doc.getCategoria());
                ps.setInt(5, doc.getHorasCatedra());
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

    public void actualizar(Docente doc) throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        conn.setAutoCommit(false);
        try {
            String sqlP = "UPDATE persona SET nombre=?,apellido=?,email=?,telefono=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlP)) {
                ps.setString(1, doc.getNombre()); ps.setString(2, doc.getApellido());
                ps.setString(3, doc.getEmail());  ps.setString(4, doc.getTelefono());
                ps.setInt(5, doc.getId());
                ps.executeUpdate();
            }
            String sqlD = "UPDATE docente SET codigo=?,especialidad=?,categoria=?,horas_catedra=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sqlD)) {
                ps.setString(1, doc.getCodigo());      ps.setString(2, doc.getEspecialidad());
                ps.setString(3, doc.getCategoria());   ps.setInt(4, doc.getHorasCatedra());
                ps.setInt(5, doc.getId());
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

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id=?";
        Connection conn = DBConnection.getInstance().getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contar() throws SQLException {
        Connection conn = DBConnection.getInstance().getConexion();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM docente")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        return new Docente(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("email"),
            rs.getString("telefono"),
            rs.getString("codigo"),
            rs.getString("especialidad"),
            rs.getString("categoria"),
            rs.getInt("horas_catedra")
        );
    }
}
