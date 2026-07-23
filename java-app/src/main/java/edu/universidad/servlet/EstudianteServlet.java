package edu.universidad.servlet;

import edu.universidad.dao.EstudianteDAO;
import edu.universidad.model.Estudiante;
import edu.universidad.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * EstudianteServlet — controlador principal para el módulo de Estudiantes.
 * Sigue el patrón MVC: Servlet = controlador, JSP = vista, Estudiante = modelo.
 */
@WebServlet(urlPatterns = {"/estudiantes", "/estudiantes/*"})
public class EstudianteServlet extends HttpServlet {

    private final EstudianteDAO dao = new EstudianteDAO();

    // ─── GET ─────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo(); // null | /nuevo | /editar | /eliminar
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // ── Listar / buscar ──────────────────────────────────────────
                String q = req.getParameter("q");
                List<Estudiante> lista;
                if (q != null && !q.trim().isEmpty()) {
                    lista = dao.buscar(q.trim());
                    req.setAttribute("busqueda", q.trim());
                } else {
                    lista = dao.listarTodos();
                }
                req.setAttribute("estudiantes", lista);
                req.setAttribute("total", dao.contar());
                forward(req, resp, "/WEB-INF/views/estudiantes/lista.jsp");

            } else if (pathInfo.equals("/nuevo")) {
                forward(req, resp, "/WEB-INF/views/estudiantes/formulario.jsp");

            } else if (pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                Estudiante est = dao.buscarPorId(id);
                if (est == null) { resp.sendRedirect(req.getContextPath() + "/estudiantes"); return; }
                req.setAttribute("estudiante", est);
                req.setAttribute("editar", true);
                forward(req, resp, "/WEB-INF/views/estudiantes/formulario.jsp");

            } else if (pathInfo.startsWith("/eliminar/")) {
                int id = Integer.parseInt(pathInfo.substring("/eliminar/".length()));
                dao.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/estudiantes?msg=eliminado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    // ─── POST ────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try {
            Estudiante est = leerFormulario(req);
            String[] errores = validar(est);

            if (errores.length > 0) {
                req.setAttribute("errores", errores);
                req.setAttribute("estudiante", est);
                if (pathInfo != null && pathInfo.startsWith("/editar/")) {
                    req.setAttribute("editar", true);
                }
                forward(req, resp, "/WEB-INF/views/estudiantes/formulario.jsp");
                return;
            }

            if (pathInfo != null && pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                est.setId(id);
                dao.actualizar(est);
                resp.sendRedirect(req.getContextPath() + "/estudiantes?msg=actualizado");
            } else {
                dao.insertar(est);
                resp.sendRedirect(req.getContextPath() + "/estudiantes?msg=creado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error al guardar: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Estudiante leerFormulario(HttpServletRequest req) {
        return new Estudiante(
            Validador.normalizarNombre(req.getParameter("nombre")),
            Validador.normalizarNombre(req.getParameter("apellido")),
            req.getParameter("email") != null ? req.getParameter("email").trim() : "",
            req.getParameter("telefono") != null ? req.getParameter("telefono").trim() : "",
            req.getParameter("codigo") != null ? req.getParameter("codigo").trim() : "",
            req.getParameter("carrera") != null ? req.getParameter("carrera").trim() : "",
            parseIntOrDefault(req.getParameter("semestre"), 1)
        );
    }

    private String[] validar(Estudiante est) {
        java.util.List<String> errores = new java.util.ArrayList<>();
        if (!Validador.noEsVacio(est.getNombre()))      errores.add("El nombre es obligatorio.");
        if (!Validador.noEsVacio(est.getApellido()))    errores.add("El apellido es obligatorio.");
        if (!Validador.esEmailValido(est.getEmail()))   errores.add("El email no tiene formato válido.");
        if (!Validador.noEsVacio(est.getCodigo()))      errores.add("El código es obligatorio.");
        if (!Validador.noEsVacio(est.getCarrera()))     errores.add("La carrera es obligatoria.");
        if (est.getSemestre() < 1 || est.getSemestre() > 12) errores.add("El semestre debe estar entre 1 y 12.");
        return errores.toArray(new String[0]);
    }

    private int parseIntOrDefault(String s, int def) {
        try { return Integer.parseInt(s != null ? s.trim() : ""); }
        catch (NumberFormatException e) { return def; }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
