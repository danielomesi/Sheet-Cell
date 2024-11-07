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

@WebServlet(name = "Is Exist Pending Permission Request",urlPatterns = "/permissions/is-pending")
public class IsExistPendingPermissionRequestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            // Retrieve query parameter values
            String sheetName = request.getParameter("name"); // Retrieve the sheet name
            boolean isWrite = request.getParameter("isWrite").equals("true");



            Engine engine = (Engine) getServletContext().getAttribute("engine");
            Boolean res = engine.isExistPendingPermissionRequest(sheetName, username, isWrite);

            Gson gson = GsonInstance.getGson();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String responseJson = gson.toJson(res);
            response.getWriter().write(responseJson);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}

