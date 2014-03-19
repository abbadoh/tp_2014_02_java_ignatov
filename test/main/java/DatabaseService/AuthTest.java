package DatabaseService;

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

    @Before
    public void init(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        frontend = new Frontend();
    }

    @Test
    public void successAuthTest() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getRequestURI()).thenReturn("/authform");

        frontend.doPost(request, response);
        verify(response).sendRedirect("/userId");
        verify(session).setAttribute("userId", (long) 5);
        verify(session).setAttribute("login", "test");
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

        frontend.doPost(request, response);
        verify(response).getWriter();
    }
}
