package com.example.Cinema.service.PDFGenerator;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;


@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(Map<String, Object> data, String templateName) throws IOException {
        Context context = new Context();
        context.setVariables(data);

        String html = templateEngine.process(templateName, context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            renderer.createPDF(out);
            return out.toByteArray();
        }
    }
}
