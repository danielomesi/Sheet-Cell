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

@WebServlet(name = "Cell Update",urlPatterns = "/update")
public class CellUpdateServlet extends HttpServlet {

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
        String expression = request.getParameter("expression"); // Retrieve the version parameter
        String cellID = request.getParameter("cellID");

        // Validate the sheet name
        if (sheetName == null || sheetName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sheet name is required");
            return;
        }

        Engine engine = (Engine) getServletContext().getAttribute("engine");
        engine.updateSpecificCell(cellID,expression,sheetName);
        DTOSheet sheet;
        sheet = (DTOSheet) engine.getSheet(sheetName);

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String responseJson = gson.toJson(sheet);
        System.out.println("Sending: " + responseJson);
        response.getWriter().write(responseJson);
    }
}
