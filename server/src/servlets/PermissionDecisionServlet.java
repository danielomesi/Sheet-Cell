package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.permission.PermissionType;
import http.dtos.RequestPermissionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;

import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "Permission Decision",urlPatterns = "/permissions/decide")
public class PermissionDecisionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            String access = request.getParameter("access");
            String accessor = request.getParameter("accessor"); //the username of the user who wishes to get access
            boolean isAccessAllowed = Objects.equals(access, "true");

            Gson gson = GsonInstance.getGson();
            RequestPermissionDTO requestPermissionDTO  = gson.fromJson(request.getReader(), RequestPermissionDTO.class);


            String sheetName = requestPermissionDTO.getSheetName();
            PermissionType permissionType = requestPermissionDTO.getPermissionType();

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.applyAccessDecision(accessor,sheetName,permissionType,isAccessAllowed);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }
}
