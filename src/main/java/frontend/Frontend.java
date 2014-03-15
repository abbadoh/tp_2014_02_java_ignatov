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
        switch(request.getRequestURI()){
            case "/authform":
                makeAuthPage(request,response);
            break;
            case "/regform":
                makeRegistrationPage(response);
                break;
            case "/logout":
                doLogout(request,response);
                break;
            case "/userId":
                makeUserIdPage(request,response);
                break;
            default:
                make404page(response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            switch(request.getRequestURI()){
                case "/authform":
                    doAuth(request,response);
                    break;
                case "/regform":
                    doRegistration(request,response);
                    break;
                default:
                    make404page(response);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public static void doLogout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("userId", null);
        session.setAttribute("login", null);
        response.sendRedirect("/authform");
    }

    public static void makeRegistrationPage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
    }

    public static void makeAuthPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if(userId != null) {
            pageVariables.put("login", session.getAttribute("login"));
        }
        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public static void makeUserIdPage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        Map<String, Object> pageVariables = new HashMap<>();
        if(userId == null) {
            response.sendRedirect("/authform");
        } else {
            pageVariables.put("userId", userId);
        }
        response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
    }

    public static void make404page(HttpServletResponse response) throws IOException {
        response.getWriter().println(PageGenerator.getPage("404page.tml"));
    }

    public static void doRegistration(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Connection con = getConnection();
        UsersDAO dao = new UsersDAO(con);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if(!dao.isUserExists(con, login)) {
            UserDataSet user= new UserDataSet(login, password);
            dao.add(user);
            response.sendRedirect("/authform");
        }  else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("alert", "This username is already registred");
            response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
        }
    }
    public static void doAuth(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Connection con = getConnection();
        UsersDAO dao = new UsersDAO(con);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (dao.isUserExists(con, login, password))
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
