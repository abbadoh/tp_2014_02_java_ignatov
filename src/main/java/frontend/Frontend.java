package frontend;

import dao.UsersDAO;
import dataSet.UserDataSet;
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
            session.setAttribute("login", null);
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
        UsersDAO dao = new UsersDAO(con);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            if (request.getRequestURI().equals("/authform"))
            {
                if (dao.userExists(con, login, password))
                {
                    HttpSession session = request.getSession();
                    UserDataSet user = dao.getByName(login);
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
                if(!dao.userExists(con, login)) {
                    UserDataSet user= new UserDataSet(login, password);
                    dao.add(user);
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
