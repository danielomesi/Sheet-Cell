package servlets;

import com.google.gson.Gson;
import engine.Engine;
import http.dtos.SortRequestDTO;
import http.dtos.SortResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.HttpResponseUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Sort",urlPatterns = "/sort")
public class SortServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = new Gson();
            SortRequestDTO sortRequestDTO = gson.fromJson(request.getReader(), SortRequestDTO.class);

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            List<Integer> sortedRowsOrder = engine.sort(sortRequestDTO.getSheetName(),sortRequestDTO.getColsToSort(),
                    sortRequestDTO.getFromCellID(),sortRequestDTO.getToCellID(),sortRequestDTO.isFirstRowSelected());

            SortResponseDTO sortResponseDTO = new SortResponseDTO(sortedRowsOrder);

            gson.toJson(sortResponseDTO,response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
