package init;

import engine.Engine;
import engine.EngineImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import users.UserManager;

@WebListener
public class ContextInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Engine engine = new EngineImpl();
        UserManager userManager = new UserManager();
        sce.getServletContext().setAttribute("engine", engine);
        sce.getServletContext().setAttribute("userManager", userManager);
    }
}
