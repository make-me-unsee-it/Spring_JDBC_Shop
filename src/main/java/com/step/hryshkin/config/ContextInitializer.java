package com.step.hryshkin.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class ContextInitializer implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);
    private static final String INIT_URL = PropertiesLoader.loadProperty("db.init");
    private static final String USER = PropertiesLoader.loadProperty("db.user");
    private static final String PASSWORD = PropertiesLoader.loadProperty("db.password");
    private static AnnotationConfigApplicationContext context;

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        try (Connection connection = DriverManager.getConnection(INIT_URL, USER, PASSWORD)) {
            LOGGER.info("Connection stable");
        } catch (SQLException e) {
            LOGGER.error("SQLEException on ContextInitializer " + e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
