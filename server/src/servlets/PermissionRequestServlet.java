package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.permission.PermissionType;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import exceptions.ServiceException;
import http.dtos.CellUpdateDTO;
import http.dtos.RequestPermissionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;

import java.io.IOException;
import java.rmi.ServerException;

@WebServlet(name = "Permission Request",urlPatterns = "/permissions/request")
public class PermissionRequestServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = new Gson();
            RequestPermissionDTO requestPermissionDTO = gson.fromJson(request.getReader(), RequestPermissionDTO.class);


            String sheetName = requestPermissionDTO.getSheetName();
            PermissionType permissionType = requestPermissionDTO.getPermissionType();

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.requestPermission(username, sheetName, permissionType);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }
}
