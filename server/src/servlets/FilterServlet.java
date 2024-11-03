package servlets;

import com.google.gson.Gson;
import engine.Engine;
import http.dtos.FilterRequestDTO;
import http.dtos.FilterResponseDTO;
import http.dtos.SortRequestDTO;
import http.dtos.SortResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import json.GsonInstance;
import utils.HttpResponseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "Filter",urlPatterns = "/filter")
public class FilterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = GsonInstance.getGson();
            FilterRequestDTO filterRequestDTO = gson.fromJson(request.getReader(), FilterRequestDTO.class);

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            Set<Integer> sortedRowsOrder = engine.filter(filterRequestDTO.getSheetName(),filterRequestDTO.getSelectedColName(),
                    filterRequestDTO.getSelectedEffectiveValues(),filterRequestDTO.getFromCellID(),filterRequestDTO.getToCellID(),
                    filterRequestDTO.isIncludingEmptyCellsInFiltering());

            FilterResponseDTO filterResponseDTO = new FilterResponseDTO(sortedRowsOrder);

            gson.toJson(filterResponseDTO,response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
