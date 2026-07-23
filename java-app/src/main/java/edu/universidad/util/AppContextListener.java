package edu.universidad.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * AppContextListener — se ejecuta al iniciar y al detener la aplicación web.
 * Inicializa la base de datos (crea tablas y carga datos de ejemplo).
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[AppContextListener] Iniciando Sistema Universitario...");
        DBInitializer.inicializar();
        System.out.println("[AppContextListener] Sistema listo.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DBConnection.getInstance().cerrar();
        } catch (Exception e) {
            System.err.println("[AppContextListener] Error cerrando DB: " + e.getMessage());
        }
        System.out.println("[AppContextListener] Sistema detenido.");
    }
}
