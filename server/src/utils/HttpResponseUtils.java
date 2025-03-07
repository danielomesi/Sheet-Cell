package utils;

import service_exceptions.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class HttpResponseUtils {
    public static String getUsernameBySessionOrUpdateResponse(HttpSession session, HttpServletResponse response) throws IOException {
        // Get existing session, don't create if it doesn't exist
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("No session found. Please log in.");
            return null;
        }

        // Retrieve the JSESSIONID
        String sessionId = session.getId();

        // Retrieve the username stored in the session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("No username found in session. Please log in.");
            return null;
        }
        return username;
    }

    public static void sendExceptionAsErrorToClient(Exception e, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType("text/plain");
        String errorBody = e instanceof ServiceException se ? se.toString() : e.getMessage();
        resp.getWriter().write(errorBody);
    }
}
