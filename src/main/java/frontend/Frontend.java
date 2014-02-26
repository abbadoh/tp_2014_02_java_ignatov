package frontend;

import templater.PageGenerator;
import users.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
//import java.util.concurrent.atomic.AtomicLong;


public class Frontend extends HttpServlet {

   // private AtomicLong userIdGenerator = new AtomicLong();
    private HashMap<String, User> users = new HashMap<>();


    
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getRequestURI().equals("/authform"))
        {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if(userId != null) {
                pageVariables.put("login", findLogin(users, userId));
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
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (request.getRequestURI().equals("/authform"))
        {
            if (users.containsKey(login) && users.get(login).rightPassword(password))
            {
                HttpSession session = request.getSession();
                Long userId = users.get(login).getUserid();
                session.setAttribute("userId", userId);
                response.sendRedirect("/userId");
            } else
            {
                Map<String, Object> pageVariables = new HashMap<>();
                pageVariables.put("alert", "Wrong username or password");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            }
        } else if(request.getRequestURI().equals("/regform"))
        {
            if(!users.containsKey(login)) {
                Long userId = new Long(users.size());
                User user = new User(password, userId);
                users.put(login, user);
                response.sendRedirect("/authform");
            }  else {
                Map<String, Object> pageVariables = new HashMap<>();
                pageVariables.put("alert", "This username is already registred");
                response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
            }
        }
    }
    public static String findLogin(HashMap<String, User> map, Long userId) {

        for(String key : map.keySet()) {
            User user = map.get(key);
            if(user.getUserid() == userId) {
                return key;
            }
        }
        return null;
    }
}
