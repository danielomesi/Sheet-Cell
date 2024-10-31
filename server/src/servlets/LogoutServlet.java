package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import users.UserManager;
import utils.HttpResponseUtils;
import utils.ServletLogicUtils;

import java.io.IOException;

@WebServlet(name = "Logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) return;
            if (session != null) session.invalidate();

            UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
            userManager.disconnectUser(username);

            Cookie sessionCookie = new Cookie("JSESSIONID", "");
            sessionCookie.setPath("/"); // Ensure the cookie applies to the entire app
            sessionCookie.setMaxAge(0); // Delete the cookie
            response.addCookie(sessionCookie);

            response.setStatus(HttpServletResponse.SC_OK);

        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
