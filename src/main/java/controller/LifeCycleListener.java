package controller;

import db.DBFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LifeCycleListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DBFactory.getReservationDB().close();
    }
}
