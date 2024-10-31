package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import users.UserManager;

import java.io.IOException;


@WebServlet(name = "Login",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");

        synchronized (getServletContext()) {
            if (!userManager.isUserExists(username)) {
                userManager.addUser(username);
                userManager.connectUser(username);
                login(request,response,username);
            }
            else {
                if (!userManager.isUserActive(username)) {
                    userManager.connectUser(username);
                    login(request,response,username);
                }
                else {
                    response.getWriter().println("This user is already logged in");
                    response.setStatus(HttpServletResponse.SC_CONFLICT); // Optionally set a conflict status
                }
            }
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response, String username) {
        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
