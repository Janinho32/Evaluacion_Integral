package edu.universidad.servlet;

import edu.universidad.dao.*;
import edu.universidad.util.Validador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * DashboardServlet — página principal con estadísticas del sistema.
 * Usa Math para calcular porcentajes y String para formatear valores.
 */
@WebServlet(urlPatterns = {"", "/", "/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final EstudianteDAO estDao = new EstudianteDAO();
    private final DocenteDAO    docDao = new DocenteDAO();
    private final CursoDAO      curDao = new CursoDAO();
    private final MatriculaDAO  matDao = new MatriculaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            int totalEst  = estDao.contar();
            int totalDoc  = docDao.contar();
            int totalCur  = curDao.contar();
            int totalMat  = matDao.contar();
            int aprobadas = matDao.contarAprobadas();
            double prom   = matDao.promedioGeneral();

            // Uso de Math y Validador para calcular métricas
            double pctAprobacion = Validador.porcentaje(aprobadas, totalMat);  // Math.min, Math.round
            double promRedondeado = Validador.redondear(prom, 2);               // Math.round

            req.setAttribute("totalEstudiantes", totalEst);
            req.setAttribute("totalDocentes",    totalDoc);
            req.setAttribute("totalCursos",      totalCur);
            req.setAttribute("totalMatriculas",  totalMat);
            req.setAttribute("matriculasAprobadas", aprobadas);
            req.setAttribute("pctAprobacion",    pctAprobacion);
            req.setAttribute("promedioGeneral",  promRedondeado);
            req.setAttribute("calificacion",     Validador.calificarNota(prom)); // uso de Math.floor

            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Error cargando datos: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
