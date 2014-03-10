package frontend;

import dao.UsersDAO;
import dataSet.UserDataSet;
import executor.TExecutor;
import handlers.TResultHandler;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


import java.sql.Connection;
import java.sql.SQLException;
import executor.SimpleExecutor;


public class Frontend extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getRequestURI().equals("/authform"))
        {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if(userId != null) {
                pageVariables.put("login", session.getAttribute("login"));
            }
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
        if (request.getRequestURI().equals("/regform"))
        {
            response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
        }
        if (request.getRequestURI().equals("/logout"))
        {
            HttpSession session = request.getSession();
            session.setAttribute("userId", null);
            response.sendRedirect("/authform");
        }
        else if (request.getRequestURI().equals("/userId"))
        {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if(userId == null) {
                response.sendRedirect("/authform");
            } else {
                pageVariables.put("userId", userId);
            }
            response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Connection con = getConnection();
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            if (request.getRequestURI().equals("/authform"))
            {
                if (userExists(con, login, password))
                {
                    HttpSession session = request.getSession();
                    UsersDAO dao = new UsersDAO(con);
                    UserDataSet user = dao.get(login);
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("login", user.getLogin());
                    response.sendRedirect("/userId");
                } else
                {
                    Map<String, Object> pageVariables = new HashMap<>();
                    pageVariables.put("alert", "Wrong username or password");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
            } else if(request.getRequestURI().equals("/regform"))
            {
                if(!userExists(con, login)) {
                    SimpleExecutor exec = new SimpleExecutor();
                    exec.execUpdate(con, "insert into users (login, password) values ('"+login+"', '"+password+"')");
                    response.sendRedirect("/authform");
                }  else {
                    Map<String, Object> pageVariables = new HashMap<>();
                    pageVariables.put("alert", "This username is already registred");
                    response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
                }
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }


    public boolean userExists(Connection con, String login) throws  SQLException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login + "'", new TResultHandler<Boolean>(){
            public Boolean handle(ResultSet result) throws SQLException {
                return result.next();
            }
        });
    }

    public boolean userExists(Connection con, String login, String password) throws  SQLException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login +
                "' and password='" + password + "'", new TResultHandler<Boolean>(){
            public Boolean handle(ResultSet result) throws SQLException {
                return result.next();
            }
        });
    }


    public static Connection getConnection() {
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            String url = "jdbc:mysql://127.0.0.1:3306/java_test?user=user&password=user";
            return DriverManager.getConnection(url);
        } catch (SQLException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
