package edu.universidad.servlet;

import edu.universidad.dao.CursoDAO;
import edu.universidad.dao.EstudianteDAO;
import edu.universidad.dao.MatriculaDAO;
import edu.universidad.model.Matricula;
import edu.universidad.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MatriculaServlet — controlador MVC para el módulo de Matrículas.
 * Demuestra relaciones entre entidades, validación cruzada y polimorfismo.
 */
@WebServlet(urlPatterns = {"/matriculas", "/matriculas/*"})
public class MatriculaServlet extends HttpServlet {

    private final MatriculaDAO  matDao  = new MatriculaDAO();
    private final EstudianteDAO estDao  = new EstudianteDAO();
    private final CursoDAO      curDao  = new CursoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                req.setAttribute("matriculas", matDao.listarTodas());
                req.setAttribute("total",      matDao.contar());
                req.setAttribute("promedio",   matDao.promedioGeneral());
                req.setAttribute("aprobadas",  matDao.contarAprobadas());
                forward(req, resp, "/WEB-INF/views/matriculas/lista.jsp");

            } else if (pathInfo.equals("/nueva")) {
                req.setAttribute("estudiantes", estDao.listarTodos());
                req.setAttribute("cursos",      curDao.listarTodos());
                forward(req, resp, "/WEB-INF/views/matriculas/formulario.jsp");

            } else if (pathInfo.startsWith("/nota/")) {
                // Formulario de registro de nota
                int id = Integer.parseInt(pathInfo.substring("/nota/".length()));
                Matricula m = matDao.buscarPorId(id);
                if (m == null) { resp.sendRedirect(req.getContextPath() + "/matriculas"); return; }
                req.setAttribute("matricula", m);
                forward(req, resp, "/WEB-INF/views/matriculas/nota.jsp");

            } else if (pathInfo.startsWith("/eliminar/")) {
                int id = Integer.parseInt(pathInfo.substring("/eliminar/".length()));
                matDao.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/matriculas?msg=eliminado");
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
            if (pathInfo != null && pathInfo.startsWith("/nota/")) {
                // Actualizar nota
                int id      = Integer.parseInt(pathInfo.substring("/nota/".length()));
                double nota = parseDouble(req.getParameter("nota"), 0.0);
                if (!Validador.enRango(nota, 0, 20)) {
                    req.setAttribute("error", "La nota debe estar entre 0 y 20.");
                    req.setAttribute("matricula", matDao.buscarPorId(id));
                    forward(req, resp, "/WEB-INF/views/matriculas/nota.jsp");
                    return;
                }
                matDao.actualizarNota(id, nota);
                resp.sendRedirect(req.getContextPath() + "/matriculas?msg=nota_actualizada");

            } else {
                // Nueva matrícula
                int estudianteId = parseInt(req.getParameter("estudianteId"), 0);
                int cursoId      = parseInt(req.getParameter("cursoId"), 0);

                List<String> errores = new ArrayList<>();
                if (estudianteId <= 0) errores.add("Selecciona un estudiante.");
                if (cursoId <= 0)      errores.add("Selecciona un curso.");

                if (errores.isEmpty() && matDao.existeMatricula(estudianteId, cursoId)) {
                    errores.add("El estudiante ya está matriculado en ese curso.");
                }

                if (!errores.isEmpty()) {
                    req.setAttribute("errores",     errores.toArray(new String[0]));
                    req.setAttribute("estudiantes", estDao.listarTodos());
                    req.setAttribute("cursos",      curDao.listarTodos());
                    forward(req, resp, "/WEB-INF/views/matriculas/formulario.jsp");
                    return;
                }

                Matricula m = new Matricula(estudianteId, cursoId);
                matDao.insertar(m);
                resp.sendRedirect(req.getContextPath() + "/matriculas?msg=creado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error al guardar: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    private int    parseInt(String s, int d)      { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return d; } }
    private double parseDouble(String s, double d) { try { return Double.parseDouble(s.trim()); } catch (Exception e) { return d; } }
    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException { req.getRequestDispatcher(path).forward(req, resp); }
}
