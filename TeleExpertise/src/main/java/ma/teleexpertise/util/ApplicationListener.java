package ma.teleexpertise.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) { JpaUtil.getFactory(); }
    public void contextDestroyed(ServletContextEvent event) { JpaUtil.close(); }
}
