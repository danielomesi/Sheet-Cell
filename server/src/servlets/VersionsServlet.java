package servlets;

import com.google.gson.Gson;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;

import java.io.IOException;

@WebServlet(name = "versions",urlPatterns = "/versions")
public class VersionsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            // Retrieve query parameter values
            String sheetName = request.getParameter("name"); // Retrieve the sheet name

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            Integer numOfVersions = engine.getNumOfVersions(sheetName);

            Gson gson = GsonInstance.getGson();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String responseJson = gson.toJson(numOfVersions);
            response.getWriter().write(responseJson);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
