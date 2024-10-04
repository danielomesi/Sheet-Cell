package init;

import engine.Engine;
import engine.EngineImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Engine engine = new EngineImpl();
        sce.getServletContext().setAttribute("engine", engine);
    }
}
