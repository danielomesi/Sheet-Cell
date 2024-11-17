package servlets;


import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.Sheet;
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

@WebServlet(name = "Sheet XML Servlet",urlPatterns = "/sheet/xml")
public class SheetXMLServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }
            String sheetName = request.getParameter("name"); // Retrieve the sheet name
            String fileName = request.getParameter("file");
            Engine engine = (Engine) getServletContext().getAttribute("engine");
            String xml = engine.getXMLOfSheet(sheetName,fileName);

            Gson gson = GsonInstance.getGson();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            String responseJson = gson.toJson(xml);
            response.getWriter().write(responseJson);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }
}
