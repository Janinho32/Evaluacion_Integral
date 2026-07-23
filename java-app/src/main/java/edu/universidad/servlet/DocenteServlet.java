package edu.universidad.servlet;

import edu.universidad.dao.DocenteDAO;
import edu.universidad.model.Docente;
import edu.universidad.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DocenteServlet — controlador MVC para el módulo de Docentes.
 */
@WebServlet(urlPatterns = {"/docentes", "/docentes/*"})
public class DocenteServlet extends HttpServlet {

    private final DocenteDAO dao = new DocenteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Docente> lista = dao.listarTodos();
                req.setAttribute("docentes", lista);
                req.setAttribute("total", dao.contar());
                forward(req, resp, "/WEB-INF/views/docentes/lista.jsp");

            } else if (pathInfo.equals("/nuevo")) {
                forward(req, resp, "/WEB-INF/views/docentes/formulario.jsp");

            } else if (pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                Docente doc = dao.buscarPorId(id);
                if (doc == null) { resp.sendRedirect(req.getContextPath() + "/docentes"); return; }
                req.setAttribute("docente", doc);
                req.setAttribute("editar", true);
                forward(req, resp, "/WEB-INF/views/docentes/formulario.jsp");

            } else if (pathInfo.startsWith("/eliminar/")) {
                int id = Integer.parseInt(pathInfo.substring("/eliminar/".length()));
                dao.eliminar(id);
                resp.sendRedirect(req.getContextPath() + "/docentes?msg=eliminado");
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
            Docente doc = leerFormulario(req);
            String[] errores = validar(doc);

            if (errores.length > 0) {
                req.setAttribute("errores", errores);
                req.setAttribute("docente", doc);
                if (pathInfo != null && pathInfo.startsWith("/editar/")) req.setAttribute("editar", true);
                forward(req, resp, "/WEB-INF/views/docentes/formulario.jsp");
                return;
            }

            if (pathInfo != null && pathInfo.startsWith("/editar/")) {
                int id = Integer.parseInt(pathInfo.substring("/editar/".length()));
                doc.setId(id);
                dao.actualizar(doc);
                resp.sendRedirect(req.getContextPath() + "/docentes?msg=actualizado");
            } else {
                dao.insertar(doc);
                resp.sendRedirect(req.getContextPath() + "/docentes?msg=creado");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error al guardar: " + e.getMessage());
            forward(req, resp, "/WEB-INF/views/error.jsp");
        }
    }

    private Docente leerFormulario(HttpServletRequest req) {
        return new Docente(
            Validador.normalizarNombre(req.getParameter("nombre")),
            Validador.normalizarNombre(req.getParameter("apellido")),
            req.getParameter("email") != null ? req.getParameter("email").trim() : "",
            req.getParameter("telefono") != null ? req.getParameter("telefono").trim() : "",
            req.getParameter("codigo") != null ? req.getParameter("codigo").trim() : "",
            req.getParameter("especialidad") != null ? req.getParameter("especialidad").trim() : "",
            req.getParameter("categoria") != null ? req.getParameter("categoria").trim() : "Contratado",
            parseInt(req.getParameter("horasCatedra"), 0)
        );
    }

    private String[] validar(Docente doc) {
        List<String> errores = new ArrayList<>();
        if (!Validador.noEsVacio(doc.getNombre()))       errores.add("El nombre es obligatorio.");
        if (!Validador.noEsVacio(doc.getApellido()))     errores.add("El apellido es obligatorio.");
        if (!Validador.esEmailValido(doc.getEmail()))    errores.add("El email no tiene formato válido.");
        if (!Validador.noEsVacio(doc.getCodigo()))       errores.add("El código es obligatorio.");
        if (!Validador.noEsVacio(doc.getEspecialidad())) errores.add("La especialidad es obligatoria.");
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
