package edu.metrostate.ics425.sa5213.prodmaint.controller;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import edu.metrostate.ics425.sa5213.prodmaint.model.ProductBean;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ContextListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		var context = sce.getServletContext();
		context.setAttribute("products", new ArrayList<ProductBean>());
		context.log("context initialized");
	}

}
