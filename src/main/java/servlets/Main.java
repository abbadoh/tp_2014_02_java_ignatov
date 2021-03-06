package servlets;

import DatabaseService.DatabaseService;
import MessageSystem.MessageSystem;
import frontend.Frontend;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        MessageSystem ms = new MessageSystem();
        Frontend frontend = new Frontend(ms);
        DatabaseService databaseService1 = new DatabaseService(ms);
        DatabaseService databaseService2 = new DatabaseService(ms);
        (new Thread(frontend)).start();
        (new Thread(databaseService1)).start();
        (new Thread(databaseService2)).start();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(frontend), "/*");
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
