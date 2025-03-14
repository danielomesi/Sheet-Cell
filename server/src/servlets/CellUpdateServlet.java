package servlets;

import com.google.gson.Gson;
import engine.Engine;
import http.dtos.CellUpdateDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;

import java.io.IOException;

@WebServlet(name = "Cell Update",urlPatterns = "/update")
public class CellUpdateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = GsonInstance.getGson();
            CellUpdateDTO cellUpdateDTO = gson.fromJson(request.getReader(), CellUpdateDTO.class);


            String sheetName = cellUpdateDTO.getSheetName();
            String expression = cellUpdateDTO.getExpression();
            String cellID = cellUpdateDTO.getCellName();

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            engine.updateSpecificCell(cellID,expression,sheetName,username);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }

    }
}
