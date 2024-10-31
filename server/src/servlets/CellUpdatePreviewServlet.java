package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import exceptions.ServiceException;
import http.dtos.CellUpdateDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;
import utils.ServletLogicUtils;

import java.io.IOException;
import java.rmi.ServerException;

@WebServlet(name = "Cell Update Preview",urlPatterns = "/update/preview")
public class CellUpdatePreviewServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }
            String sheetName = request.getParameter("name"); // Retrieve the sheet name
            String originalExpression = request.getParameter("originalExpression");
            String cellID = request.getParameter("cellID");
            String versionParam = request.getParameter("version"); // Retrieve the version parameter
            int version = ServletLogicUtils.parseOrDefault(versionParam,-1);

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            Sheet sheet = engine.previewSpecificUpdateOnCell(cellID,originalExpression,sheetName,version,username);

            Gson gson = new Gson();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String responseJson = gson.toJson(sheet);
            response.getWriter().write(responseJson);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }
}
