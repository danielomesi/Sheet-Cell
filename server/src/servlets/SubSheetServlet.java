package servlets;

import com.google.gson.Gson;
import engine.Engine;
import entities.sheet.DTOSheet;
import http.dtos.CellUpdateDTO;
import http.dtos.SetSubSheetDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;
import utils.ServletLogicUtils;

import java.io.IOException;

@WebServlet(name = "Sub Sheet",urlPatterns = "/subsheet")
public class SubSheetServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            DTOSheet sheet;
            sheet = (DTOSheet) engine.getSubSheet(username);


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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = new Gson();
            SetSubSheetDTO subSheetDTO = gson.fromJson(request.getReader(), SetSubSheetDTO.class);

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.setSubSheet(subSheetDTO.getSheetName(),subSheetDTO.getTopLeftCellID(),
                    subSheetDTO.getBottomRightCellID(),username);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
