package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;

import java.io.IOException;

@WebServlet(name = "Sheet",urlPatterns = "/sheet")
public class SheetServlet extends HttpServlet {

    //sheet name is a must and also the version - a 0-indexed integer representing the version
    //if version is -1 it is considered the last version
    //query param names: [name], [version]
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
        if (username == null) {
            return;
        }

        // Retrieve query parameter values
        String sheetName = request.getParameter("name"); // Retrieve the sheet name
        String versionParam = request.getParameter("version"); // Retrieve the version parameter

        // Validate the sheet name
        if (sheetName == null || sheetName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sheet name is required");
            return;
        }

        int version = 0;
        if (versionParam != null) {
            try {
                version = Integer.parseInt(versionParam); // Convert version to an integer
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid version format. Must be an integer");
                return;
            }
        }

        Engine engine = (Engine) getServletContext().getAttribute("engine");
        DTOSheet sheet;
        if (version == -1) {
            sheet = (DTOSheet) engine.getSheet(sheetName);
        }
        else {
            sheet = (DTOSheet) engine.getSheet(sheetName, version);
        }

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseJson = gson.toJson(sheet);
        System.out.println("Sending: " + responseJson);
        response.getWriter().write(responseJson);
    }
}
