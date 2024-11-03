package servlets;

import com.google.gson.Gson;
import engine.Engine;
import http.dtos.AddRangeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;

import java.io.IOException;

@WebServlet(name = "Range",urlPatterns = "/range")
public class RangeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = GsonInstance.getGson();
            AddRangeDTO addRangeDTO = gson.fromJson(request.getReader(), AddRangeDTO.class);


            String sheetName = addRangeDTO.getSheetName();
            String fromCellID = addRangeDTO.getBottomRightCellID();
            String toCellID = addRangeDTO.getTopLeftCellID();
            String rangeName = addRangeDTO.getRangeName();

            if (sheetName == null || sheetName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sheet name is required");
                return;
            }

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.addRange(sheetName, rangeName, fromCellID, toCellID);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            String sheetName = request.getParameter("sheet-name");
            String rangeName = request.getParameter("range-name");
            if (sheetName == null || sheetName.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sheet name is required");
                return;
            }

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.deleteRange(sheetName, rangeName);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}

