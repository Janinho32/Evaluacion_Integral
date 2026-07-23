package edu.universidad.servlet;

import edu.universidad.dao.CursoDAO;
import edu.universidad.dao.DocenteDAO;
import edu.universidad.model.Curso;
import edu.universidad.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CursoServlet — controlador MVC para el módulo de Cursos.
 */
@WebServlet(urlPatterns = {"/cursos", "/cursos/*"})
public class CursoServlet extends HttpServlet {

    private final CursoDAO   cursoDao   = new CursoDAO();
    private final DocenteDAO docenteDao = new DocenteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Curso> lista = cursoDao.listarTodos();
                // Agregar conteo de matriculados a cada curso
                for (Curso c : lista) {
                    int mat = cursoDao.contarMatriculados(c.getId());
                    c.getEstudiantesMatriculados(); // acceso al getter
                    req.setAttribute("mat_" + c.getId(), mat);
                }
                req.setAttribute("cursos", lista);
                req.setAttribute("total", cursoDao.contar());
                forward(req, resp, "/WEB-INF/views/cursos/lista.jsp");

            } else if (pathInfo.equals("/nuevo")) {
                req.setAttribute("docentes", docenteDao.listarTodos());
                forward(req, resp, "/WEB-INF/views/cursos/formulario.jsp");

            } else if (pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                Curso curso = cursoDao.buscarPorId(id);
                if (curso == null) { resp.sendRedirect(req.getContextPath() + "/cursos"); return; }
                req.setAttribute("curso", curso);
                req.setAttribute("docentes", docenteDao.listarTodos());
                req.setAttribute("editar", true);
                forward(req, resp, "/WEB-INF/views/cursos/formulario.jsp");

            } else if (pathInfo.startsWith("/eliminar/")) {
                int id = Integer.parseInt(pathInfo.substring("/eliminar/".length()));
                cursoDao.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/cursos?msg=eliminado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            Curso curso = leerFormulario(req);
            String[] errores = validar(curso);

            if (errores.length > 0) {
                req.setAttribute("errores", errores);
                req.setAttribute("curso", curso);
                req.setAttribute("docentes", docenteDao.listarTodos());
                if (pathInfo != null && pathInfo.startsWith("/editar/")) req.setAttribute("editar", true);
                forward(req, resp, "/WEB-INF/views/cursos/formulario.jsp");
                return;
            }

            if (pathInfo != null && pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                curso.setId(id);
                cursoDao.actualizar(curso);
                resp.sendRedirect(req.getContextPath() + "/cursos?msg=actualizado");
            } else {
                cursoDao.insertar(curso);
                resp.sendRedirect(req.getContextPath() + "/cursos?msg=creado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error al guardar: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    private Curso leerFormulario(HttpServletRequest req) {
        return new Curso(
            req.getParameter("codigo") != null ? req.getParameter("codigo").trim() : "",
            req.getParameter("nombre") != null ? req.getParameter("nombre").trim() : "",
            parseInt(req.getParameter("creditos"), 3),
            parseInt(req.getParameter("cupoMaximo"), 30),
            parseInt(req.getParameter("docenteId"), 0)
        );
    }

    private String[] validar(Curso curso) {
        List<String> errores = new ArrayList<>();
        if (!Validador.noEsVacio(curso.getCodigo())) errores.add("El código es obligatorio.");
        if (!Validador.noEsVacio(curso.getNombre())) errores.add("El nombre es obligatorio.");
        if (curso.getCreditos() < 1 || curso.getCreditos() > 10) errores.add("Los créditos deben estar entre 1 y 10.");
        if (curso.getCupoMaximo() < 1)                            errores.add("El cupo máximo debe ser mayor a 0.");
        return errores.toArray(new String[0]);
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s != null ? s.trim() : ""); }
        catch (NumberFormatException e) { return def; }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
