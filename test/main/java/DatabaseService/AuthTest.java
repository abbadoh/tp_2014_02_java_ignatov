package DatabaseService;

import MessageSystem.MessageSystem;
import MessageSystem.Messages.MsgGetUserid;
import  frontend.Frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by gumo on 14/03/14.
 */
public class AuthTest {
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    Frontend frontend;
    MessageSystem ms;

    @Before
    public void init(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        ms = new MessageSystem();
        frontend = new Frontend(ms);
        DatabaseService databaseService1 = new DatabaseService(ms);
        DatabaseService databaseService2 = new DatabaseService(ms);
        (new Thread(frontend)).start();
        (new Thread(databaseService1)).start();
        (new Thread(databaseService2)).start();
    }

    @Test
    public void successAuthTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getRequestURI()).thenReturn("/authform");
        when(request.getSession().getId()).thenReturn("1");

        frontend.doPost(request, response);
        verify(response).sendRedirect("/userId");
//        verify(ms).sendMessage(any(MsgGetUserid.class));
    }

    @Test
    public void UnsuccessAuthTest() throws IOException, ServletException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("false");
        when(request.getRequestURI()).thenReturn("/authform");
        when(request.getSession().getId()).thenReturn("1");

        frontend.doPost(request, response);

        verify(response).sendRedirect("/userId");
//        todo
    }
}
