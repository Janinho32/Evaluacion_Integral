package edu.universidad.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBInitializer — Crea el esquema de tablas y carga datos iniciales de ejemplo.
 * Se ejecuta al iniciar la aplicación (desde el AppContextListener).
 *
 * Aplica estructuras de control (if/for) para verificar si ya existe datos.
 */
public class DBInitializer {

    public static void inicializar() {
        try {
            Connection conn = DBConnection.getInstance().getConexion();
            crearTablas(conn);
            if (!hayDatosIniciales(conn)) {
                cargarDatosEjemplo(conn);
            }
            System.out.println("[DBInitializer] Base de datos lista.");
        } catch (SQLException e) {
            System.err.println("[DBInitializer] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─── Creación de tablas ───────────────────────────────────────────────────

    private static void crearTablas(Connection conn) throws SQLException {
        String[] ddl = {
            // Tabla persona (base de la jerarquía)
            """
            CREATE TABLE IF NOT EXISTS persona (
                id        INT AUTO_INCREMENT PRIMARY KEY,
                nombre    VARCHAR(100) NOT NULL,
                apellido  VARCHAR(100) NOT NULL,
                email     VARCHAR(200) NOT NULL UNIQUE,
                telefono  VARCHAR(20),
                tipo      VARCHAR(20) NOT NULL
            )
            """,
            // Tabla estudiante (extiende persona)
            """
            CREATE TABLE IF NOT EXISTS estudiante (
                id                  INT PRIMARY KEY REFERENCES persona(id) ON DELETE CASCADE,
                codigo              VARCHAR(20) NOT NULL UNIQUE,
                carrera             VARCHAR(150) NOT NULL,
                semestre            INT DEFAULT 1,
                creditos_acumulados INT DEFAULT 0
            )
            """,
            // Tabla docente (extiende persona)
            """
            CREATE TABLE IF NOT EXISTS docente (
                id            INT PRIMARY KEY REFERENCES persona(id) ON DELETE CASCADE,
                codigo        VARCHAR(20) NOT NULL UNIQUE,
                especialidad  VARCHAR(150) NOT NULL,
                categoria     VARCHAR(50) DEFAULT 'Contratado',
                horas_catedra INT DEFAULT 0
            )
            """,
            // Tabla curso
            """
            CREATE TABLE IF NOT EXISTS curso (
                id           INT AUTO_INCREMENT PRIMARY KEY,
                codigo       VARCHAR(20) NOT NULL UNIQUE,
                nombre       VARCHAR(200) NOT NULL,
                creditos     INT NOT NULL DEFAULT 3,
                cupo_maximo  INT DEFAULT 30,
                docente_id   INT REFERENCES docente(id) ON DELETE SET NULL
            )
            """,
            // Tabla matricula
            """
            CREATE TABLE IF NOT EXISTS matricula (
                id              INT AUTO_INCREMENT PRIMARY KEY,
                estudiante_id   INT NOT NULL REFERENCES estudiante(id) ON DELETE CASCADE,
                curso_id        INT NOT NULL REFERENCES curso(id) ON DELETE CASCADE,
                nota            DOUBLE DEFAULT 0,
                fecha           DATE,
                estado          VARCHAR(20) DEFAULT 'Activa',
                UNIQUE(estudiante_id, curso_id)
            )
            """
        };

        try (Statement st = conn.createStatement()) {
            for (String sql : ddl) {
                st.execute(sql);
            }
        }
    }

    // ─── Verificar si ya hay datos ────────────────────────────────────────────

    private static boolean hayDatosIniciales(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM persona")) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // ─── Datos de ejemplo ─────────────────────────────────────────────────────

    private static void cargarDatosEjemplo(Connection conn) throws SQLException {
        // --- Docentes ---
        int[] docenteIds = new int[3];
        String[][] docentes = {
            {"Ana",    "Torres",   "ana.torres@universidad.edu",   "987001001", "DOC001", "Ingeniería de Software",  "Ordinario",  "20"},
            {"Carlos", "Mendoza",  "carlos.mendoza@universidad.edu","987001002", "DOC002", "Matemática Aplicada",    "Ordinario",  "16"},
            {"Lucía",  "Paredes",  "lucia.paredes@universidad.edu", "987001003", "DOC003", "Bases de Datos",         "Contratado", "12"}
        };

        String sqlPers  = "INSERT INTO persona(nombre,apellido,email,telefono,tipo) VALUES(?,?,?,?,?)";
        String sqlDoc   = "INSERT INTO docente(id,codigo,especialidad,categoria,horas_catedra) VALUES(?,?,?,?,?)";

        for (int i = 0; i < docentes.length; i++) {
            String[] d = docentes[i];
            try (PreparedStatement ps = conn.prepareStatement(sqlPers, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, d[0]); ps.setString(2, d[1]);
                ps.setString(3, d[2]); ps.setString(4, d[3]); ps.setString(5, "Docente");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) docenteIds[i] = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlDoc)) {
                ps.setInt(1, docenteIds[i]); ps.setString(2, d[4]);
                ps.setString(3, d[5]); ps.setString(4, d[6]); ps.setInt(5, Integer.parseInt(d[7]));
                ps.executeUpdate();
            }
        }

        // --- Cursos ---
        String[][] cursos = {
            {"POO101", "Programación Orientada a Objetos", "4", "35", String.valueOf(docenteIds[0])},
            {"MAT201", "Cálculo Diferencial",              "4", "40", String.valueOf(docenteIds[1])},
            {"BD301",  "Bases de Datos Relacionales",      "3", "30", String.valueOf(docenteIds[2])},
            {"ALG101", "Algoritmos y Estructuras de Datos","3", "35", String.valueOf(docenteIds[0])},
            {"FIS101", "Física General",                   "4", "40", String.valueOf(docenteIds[1])}
        };

        String sqlCurso = "INSERT INTO curso(codigo,nombre,creditos,cupo_maximo,docente_id) VALUES(?,?,?,?,?)";
        int[] cursoIds  = new int[cursos.length];

        for (int i = 0; i < cursos.length; i++) {
            String[] c = cursos[i];
            try (PreparedStatement ps = conn.prepareStatement(sqlCurso, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c[0]); ps.setString(2, c[1]);
                ps.setInt(3, Integer.parseInt(c[2]));
                ps.setInt(4, Integer.parseInt(c[3]));
                ps.setInt(5, Integer.parseInt(c[4]));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) cursoIds[i] = rs.getInt(1);
                }
            }
        }

        // --- Estudiantes ---
        String[][] estudiantes = {
            {"María",   "García",    "maria.garcia@estudiante.edu",   "911001001", "EST001", "Ingeniería de Sistemas", "3"},
            {"Pedro",   "Quispe",    "pedro.quispe@estudiante.edu",   "911001002", "EST002", "Ingeniería de Sistemas", "3"},
            {"Sofía",   "Ramos",     "sofia.ramos@estudiante.edu",    "911001003", "EST003", "Ingeniería Industrial",  "2"},
            {"Diego",   "Huanca",    "diego.huanca@estudiante.edu",   "911001004", "EST004", "Ciencias de la Computación","4"},
            {"Valentina","Cruz",     "valentina.cruz@estudiante.edu", "911001005", "EST005", "Ingeniería de Sistemas", "1"},
            {"Rodrigo", "Flores",    "rodrigo.flores@estudiante.edu", "911001006", "EST006", "Matemáticas",            "2"},
            {"Camila",  "Vargas",    "camila.vargas@estudiante.edu",  "911001007", "EST007", "Ingeniería Industrial",  "3"}
        };

        String sqlEstPers = "INSERT INTO persona(nombre,apellido,email,telefono,tipo) VALUES(?,?,?,?,?)";
        String sqlEst     = "INSERT INTO estudiante(id,codigo,carrera,semestre,creditos_acumulados) VALUES(?,?,?,?,?)";
        int[]  estIds     = new int[estudiantes.length];

        for (int i = 0; i < estudiantes.length; i++) {
            String[] e = estudiantes[i];
            try (PreparedStatement ps = conn.prepareStatement(sqlEstPers, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, e[0]); ps.setString(2, e[1]);
                ps.setString(3, e[2]); ps.setString(4, e[3]); ps.setString(5, "Estudiante");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) estIds[i] = rs.getInt(1);
                }
            }
            int creditos = Integer.parseInt(e[6]) * 18; // aprox. créditos por semestre
            try (PreparedStatement ps = conn.prepareStatement(sqlEst)) {
                ps.setInt(1, estIds[i]); ps.setString(2, e[4]);
                ps.setString(3, e[5]); ps.setInt(4, Integer.parseInt(e[6]));
                ps.setInt(5, creditos);
                ps.executeUpdate();
            }
        }

        // --- Matrículas de ejemplo ---
        double[][] notas = {
            {cursoIds[0], estIds[0], 16.5},
            {cursoIds[0], estIds[1], 13.0},
            {cursoIds[0], estIds[2], 18.0},
            {cursoIds[1], estIds[0], 14.5},
            {cursoIds[1], estIds[3], 10.5},
            {cursoIds[2], estIds[1], 15.0},
            {cursoIds[2], estIds[4], 17.5},
            {cursoIds[3], estIds[2],  9.0},
            {cursoIds[3], estIds[5], 12.0},
            {cursoIds[4], estIds[6], 16.0},
        };

        String sqlMat = "INSERT INTO matricula(estudiante_id,curso_id,nota,fecha,estado) VALUES(?,?,?,CURRENT_DATE,?)";
        for (double[] m : notas) {
            double nota  = m[2];
            String estado = nota >= 11.0 ? "Aprobada" : (nota > 0 ? "Desaprobada" : "Activa");
            try (PreparedStatement ps = conn.prepareStatement(sqlMat)) {
                ps.setInt(1, (int) m[1]); ps.setInt(2, (int) m[0]);
                ps.setDouble(3, nota); ps.setString(4, estado);
                ps.executeUpdate();
            }
        }

        System.out.println("[DBInitializer] Datos de ejemplo cargados correctamente.");
    }
}
