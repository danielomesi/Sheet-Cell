package servlets;

import engine.Engine;
import engine.EngineImpl;
import entities.sheet.Sheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "UploadFile",urlPatterns = "/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session, don't create if it doesn't exist
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("No session found. Please log in.");
            return;
        }

        // Retrieve the JSESSIONID
        String sessionId = session.getId();
        System.out.println("JSESSIONID: " + sessionId);

        // Retrieve the username stored in the session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("No username found in session. Please log in.");
            return;
        }

        System.out.println("Username from session: " + username);

        StringBuilder allContent = new StringBuilder();
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        // Get all parts from the request
        Collection<Part> parts = request.getParts();
        out.println("Total parts: " + parts.size());

        // Process each part
        for (Part part : parts) {
            printPartDetails(part, out);
            String content = readFromInputStream(part.getInputStream());
            allContent.append(content);
            printPartContent(content, out);
        }

        System.out.println("Received: \n" + allContent.toString());

        try {
            Engine engine = new EngineImpl();
            engine.loadSheetFromXMLString(allContent.toString(),username);
            Sheet sheet = engine.getUserSheets(username).getLast().getSheetVersions().getLast();
            out.println("Sheet size is " + sheet.getNumOfRows() + ", " + sheet.getNumOfCols());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPartDetails(Part part, PrintWriter out) {
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

    private void printPartContent(String content, PrintWriter out) {
        out.println("Part Content:");
        out.println(content);
    }

}
