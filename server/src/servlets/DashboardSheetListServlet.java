
package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import entities.sheet.SheetMetaData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;
import utils.ServletLogicUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Dashboard Sheet List",urlPatterns = "/dashboard/sheets")
public class DashboardSheetListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            List<SheetMetaData> allSheetsMetaData = engine.getAllSheetsMetaData();

            Gson gson = GsonInstance.getGson();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String responseJson = gson.toJson(allSheetsMetaData);
            System.out.println("Sending: " + responseJson);
            response.getWriter().write(responseJson);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}

