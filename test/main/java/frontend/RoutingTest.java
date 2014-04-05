package frontend;

import DatabaseService.DatabaseService;
import MessageSystem.MessageSystem;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gumo on 16/03/14.
 */
public class RoutingTest {
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    Frontend frontend;
    StringWriter stringWriter;
    PrintWriter writer;
    String page404;
    MessageSystem ms;

    @Before
    public void init() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        ms = new MessageSystem();

        frontend = new Frontend(ms);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        frontend.make404page(response);
        page404 = stringWriter.toString();
        stringWriter.flush();

        DatabaseService databaseService1 = new DatabaseService(ms);
        DatabaseService databaseService2 = new DatabaseService(ms);
        (new Thread(frontend)).start();
        (new Thread(databaseService1)).start();
        (new Thread(databaseService2)).start();
    }

    @Test
    public void authformTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/authform");
        when(session.getAttribute("userId")).thenReturn(null);

        frontend.doGet(request, response);

        Assert.assertFalse(response.getWriter().toString().equals(page404));
    }

    @Test
    public void regformTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/regform");

        frontend.doGet(request, response);
        Assert.assertFalse(response.getWriter().toString().equals(page404));
    }

    @Test
    public void loggedInUserIdTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/userId");
        when(session.getAttribute("userId")).thenReturn((long)1);

        frontend.doGet(request, response);
        Assert.assertFalse(response.getWriter().toString().equals(page404));
    }

    @Test
    public void NotLoggedInUserIdTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/userId");
        when(session.getAttribute("userId")).thenReturn(null);

        frontend.doGet(request, response);
        Assert.assertFalse(response.getWriter().toString().equals(page404));
    }
}
