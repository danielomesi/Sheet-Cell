package servlets;

import com.google.gson.Gson;
import engine.Engine;
import http.dtos.EffectiveValuesInSpecificColRequestDTO;
import http.dtos.EffectiveValuesInSpecificColResponseDTO;
import http.dtos.FilterRequestDTO;
import http.dtos.FilterResponseDTO;
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

@WebServlet(name = "Distinct Values",urlPatterns = "/filter/distinct")
public class DistinctValuesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
            if (username == null) {
                return;
            }

            Gson gson = GsonInstance.getGson();
            EffectiveValuesInSpecificColRequestDTO effectiveValuesInSpecificColRequestDTO = gson.fromJson(request.getReader(), EffectiveValuesInSpecificColRequestDTO.class);

            Engine engine = (Engine) getServletContext().getAttribute("engine");
            List<Object> effectiveValues = engine.getEffectiveValuesInSpecificCol(
                    effectiveValuesInSpecificColRequestDTO.getSheetName(),effectiveValuesInSpecificColRequestDTO.getSelectedColName(),
                    effectiveValuesInSpecificColRequestDTO.getFromCellID(),effectiveValuesInSpecificColRequestDTO.getToCellID());

            EffectiveValuesInSpecificColResponseDTO effectiveValuesInSpecificColResponseDTO = new EffectiveValuesInSpecificColResponseDTO(effectiveValues);

            gson.toJson(effectiveValuesInSpecificColResponseDTO,response.getWriter());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            HttpResponseUtils.sendExceptionAsErrorToClient(e,response);
        }
    }
}
