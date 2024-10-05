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
            if (userManager.isUserExists(username)) {
                // Inform the client that the username is taken
                response.getWriter().println("This user is already in use. Please choose a different user");
                response.setStatus(HttpServletResponse.SC_CONFLICT); // Optionally set a conflict status
            } else {
                // Register the user or update the user state as needed
                userManager.addUser(username); // Assuming you have a method to add the user

                // Create a session and set the username
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Create a cookie for the session ID
                Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
                sessionCookie.setPath("/"); // Make it accessible to the entire application
                response.addCookie(sessionCookie); // Add the cookie to the response

                // Send a success message back to the client
                response.getWriter().println("User " + username + " has been registered successfully.");
                response.setStatus(HttpServletResponse.SC_OK); // Optionally set OK status
            }
        }
    }
}
