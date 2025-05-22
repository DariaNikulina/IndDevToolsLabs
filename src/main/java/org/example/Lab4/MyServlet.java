package org.example.Lab4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.lab3.WordCountService;
import org.example.lab3.database.WordResultsInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@WebServlet("/search")
public class MyServlet extends HttpServlet {
    private WordCountService service;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        service = WordCountService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String filename = req.getParameter("filename");
        if (filename == null || filename.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан обязательный параметр - имя файла");
            return;
        }
        File file = service.downloadFile(filename);
        resp.setContentType(getServletContext().getMimeType(file.getName()));
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        JsonNode node = mapper.readTree(req.getReader());
        String word = node.path("word").asText(null);
        if (word == null || word.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан обязательный параметр - слово для поиска");
            return;
        }
        List<WordResultsInfo> results = service.searchByWord(word);
        resp.setContentType("application/json; charset=UTF-8");
        mapper.writeValue(resp.getWriter(), results);
    }
}

