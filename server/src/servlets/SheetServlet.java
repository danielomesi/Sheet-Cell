package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;
import utils.ServletLogicUtils;

import java.io.IOException;

@WebServlet(name = "Sheet",urlPatterns = "/sheet")
public class SheetServlet extends HttpServlet {

    //sheet name is a must and also the version - a 0-indexed integer representing the version
    //if version is -1 it is considered the last version
    //query param names: [name], [version]
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            // Retrieve query parameter values
            String sheetName = request.getParameter("name"); // Retrieve the sheet name
            String versionParam = request.getParameter("version"); // Retrieve the version parameter

            int version = ServletLogicUtils.parseOrDefault(versionParam,-1);

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
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
