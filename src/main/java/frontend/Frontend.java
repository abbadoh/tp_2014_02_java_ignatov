package frontend;

import MessageSystem.Messages.MsgGetUserid;
import MessageSystem.Messages.MsgRegUser;
import Utilities.TimeHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import MessageSystem.MessageSystem;
import MessageSystem.Address;
import MessageSystem.Subscriber;


public class Frontend extends HttpServlet implements Runnable, Subscriber {

    private MessageSystem ms;
    private Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();


    public Frontend(MessageSystem ms){
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setFrontend(address);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        switch (request.getRequestURI()) {
            case "/authform":
                makeAuthPage(request, response);
                break;
            case "/regform":
                makeRegistrationPage(request, response);
                break;
            case "/logout":
                doLogout(request, response);
                break;
            case "/userId":
                makeUserIdPage(request, response);
                break;
            default:
                make404page(response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        switch (request.getRequestURI()) {
            case "/authform":
                doAuth(request, response);
                break;
            case "/regform":
                doRegistration(request, response);
                break;
            default:
                make404page(response);
        }
    }

    public void makeRegistrationPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if(userSession == null || userSession.getRegState() == 2){
            pageVariables.put("needReg", true);
        }
        else if(userSession.getUserId() != null){
            pageVariables.put("userState", "you are already registered");
        }
        else if(userSession.getRegState() == 0) {
            pageVariables.put("userState", "wait for registration");
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", TimeHelper.getTime());
        }
        else if(userSession.getRegState() == -1) {
            pageVariables.put("userState", "this username is already taken");
            userSession.setRegState(2);
            pageVariables.put("refreshPeriod", "1000");
        }
        else if(userSession.getRegState() == 1) {
            pageVariables.put("userState", "you are registered!");
        }
        if(userSession != null && userSession.getServerState()) {
            pageVariables.clear();
            pageVariables.put("serverIsDown", true);
            pageVariables.put("needReg", true);
            userSession.setServerState(false);
        }
        if(pageVariables.get("refreshPeriod") == null){ pageVariables.put("refreshPeriod", "100000"); }
        response.getWriter().println(PageGenerator.getPage("regform.tml", pageVariables));
    }


    public void makeAuthPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession != null && userSession.getUserId() != null && userSession.getUserId() != -1) {
            pageVariables.put("login", userSession.getName());
        }
        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public void makeUserIdPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();


        UserSession userSession = sessionIdToUserSession.get(session.getId());
        if (userSession == null) {
            pageVariables.put("userState", "Auth error");
            pageVariables.put("refreshPeriod", "100000");
        }
        else if (userSession.getUserId() == null) {
            pageVariables.put("userState", "wait for authorization");
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("lock",true);
        }
        else if (userSession.getUserId() == -1) {
            pageVariables.put("userState", "wrong username or password");
            pageVariables.put("refreshPeriod", "100000");
        }
        else {
            pageVariables.put("userState","login : " + userSession.getName() + "</br> id : " + userSession.getUserId());
            pageVariables.put("refreshPeriod", "100000");
        }
        if(userSession !=null && userSession.getServerState()) {
            pageVariables.clear();
            pageVariables.put("serverIsDown", true);
            userSession.setServerState(false);
            pageVariables.put("refreshPeriod", "100000");
            doLogout(session.getId());
        }
        pageVariables.put("serverTime", TimeHelper.getTime());
         response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
    }

    public static void make404page(HttpServletResponse response) throws IOException {
        response.getWriter().println(PageGenerator.getPage("404page.tml"));
    }

    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        sessionIdToUserSession.remove(session.getId());
        response.sendRedirect("/authform");
    }

    public void doLogout(String sessionId) throws IOException {
        sessionIdToUserSession.remove(sessionId);
    }

    public void doRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String sessionId = request.getSession().getId();
        UserSession  userSession = sessionIdToUserSession.get(sessionId);
        if(userSession == null) {
            userSession = new UserSession(sessionId,ms.getAddressService());
            sessionIdToUserSession.put(sessionId, userSession);
        }
        else {
            userSession.setRegState(0);
        }

        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgRegUser(frontendAddress, accountServiceAddress, login, password, sessionId));
        response.sendRedirect("/regform");
    }

    public void doAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String sessionId = request.getSession().getId();
        UserSession  userSession = sessionIdToUserSession.get(sessionId);
        if(userSession == null) {
            userSession = new UserSession(sessionId,login,ms.getAddressService());
            sessionIdToUserSession.put(sessionId, userSession);
        }
        else {
            userSession.setName(login);
        }
        Address frontendAddress = getAddress();
        Address accountServiceAddress = userSession.getAccountService();

        ms.sendMessage(new MsgGetUserid(frontendAddress, accountServiceAddress, login, password, sessionId));
        response.sendRedirect("/userId");
    }

    public  void run(){
        while(true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(100);
        }
    }

    public void serverIsDown(String sessionId){
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setServerState(true);
    }


    public void setId(String sessionId, Long userId){
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
    }

    public void regComplete(String sessionId, Integer state){
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setRegState(state);
    }

    public Address getAddress(){
        return this.address;
    }

}
