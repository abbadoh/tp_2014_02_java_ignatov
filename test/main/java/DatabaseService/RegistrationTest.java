package DatabaseService;

import MessageSystem.MessageSystem;
import frontend.Frontend;
import org.junit.Before;
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

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    MessageSystem ms;
    Frontend frontend;

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
    public void successRegistrationTest() throws IOException, ServletException, SQLException {
        Connection con = DatabaseService.getConnection();
        UserDataSet user = new UserDataSet("registrationTestLogin");
        UserDAO dao = new UserDAO(con);
        dao.delete(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("registrationTestLogin");
        when(request.getParameter("password")).thenReturn("registrationTestPassword");
        when(request.getRequestURI()).thenReturn("/regform");

        frontend.doPost(request, response);
        verify(response).sendRedirect("/regform");
//        todo
    }



    @Test
    public void unsuccessRegistrationTest() throws IOException, ServletException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("login")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getRequestURI()).thenReturn("/regform");

        frontend.doPost(request, response);
        verify(response).sendRedirect("/regform");
//??        todo
    }
}
