package DatabaseService;

import frontend.Frontend;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
* Created by gumo on 17/03/14.
*/
public class RegistrationTest {

    @Test
    public void successRegistrationTest() throws IOException, ServletException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Frontend frontend = new Frontend();
        Connection con = frontend.getConnection();

        UserDataSet user = new UserDataSet("registrationTestLogin");
        UserDAO dao = new UserDAO(con);
        dao.delete(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        stringWriter.toString();

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("registrationTestLogin");
        when(request.getParameter("password")).thenReturn("registrationTestPassword");
        when(request.getRequestURI()).thenReturn("/regform");

        frontend.doPost(request, response);
        dao.delete(user);
        verify(response).sendRedirect("/authform");
    }



    @Test
    public void UnsuccessRegistrationTest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Frontend frontend = new Frontend();


        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        stringWriter.toString();

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getRequestURI()).thenReturn("/regform");

        frontend.doPost(request, response);
        verify(response).getWriter();
    }
}
