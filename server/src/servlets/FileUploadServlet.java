package servlets;

import com.google.gson.Gson;
import engine.Engine;
import engine.EngineImpl;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import utils.HttpResponseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "UploadFile",urlPatterns = "/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = HttpResponseUtils.getUsernameBySessionOrUpdateResponse(session,response);
        if (username == null) {
            return;
        }
        System.out.println("Username from session: " + username);

        StringBuilder allContent = new StringBuilder();
        response.setContentType("text/plain");
        PrintStream consoleOut = System.out;

        // Get all parts from the request
        Collection<Part> parts = request.getParts();
        consoleOut.println("Total parts: " + parts.size());

        // Process each part
        for (Part part : parts) {
            printPartDetails(part, consoleOut);
            String content = readFromInputStream(part.getInputStream());
            allContent.append(content);
            printPartContent(content, consoleOut);
        }

        try {
            Engine engine = new EngineImpl();
            engine.loadSheetFromXMLString(allContent.toString(),username);
            Sheet sheet = engine.getUserSheets(username).getLast().getSheetVersions().getLast();
            SheetMetaData sheetMetaData = new SheetMetaData(sheet.getName(),username,sheet.getNumOfRows(),sheet.getNumOfCols());
            Gson gson = new Gson();
            gson.toJson(sheetMetaData, response.getWriter());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPartDetails(Part part, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type: ").append(part.getContentType()).append("\n")
                .append("Size: ").append(part.getSize()).append("\n")
                .append("Part Headers:\n");

        // Print all headers
        part.getHeaderNames().forEach(header ->
                sb.append(header).append(": ").append(part.getHeader(header)).append("\n")
        );

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printPartContent(String content, PrintStream out) {
        out.println("Part Content:");
        out.println(content);
    }

}
