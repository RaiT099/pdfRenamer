package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfRenameApp {

    public static void main(String[] args) {

        String folderPath = "C:/Users/kwong/Documents/InteliJ/pdf";   // CHANGE THIS
        File folder = new File(folderPath);

        File[] pdfFiles = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null || pdfFiles.length == 0) {
            System.out.println("No PDF files found.");
            return;
        }

        for (File pdfFile : pdfFiles) {
            processPdf(pdfFile);
        }
    }

    private static void processPdf(File pdfFile) {

        String detectedValue = null;   // store match here

        // -------- STEP 1: Read PDF and extract text --------
        try (PDDocument document = PDDocument.load(pdfFile)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Detect pattern like A-01-05
            Pattern pattern = Pattern.compile("[A-Za-z]-\\d{1,2}-\\d{1,2}");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                detectedValue = matcher.group().replace("-", "");  // remove dash
            }

            // Add others condition sample
            if (detectedValue == null) {
                Pattern pattern1 = Pattern.compile("[A-Za-z]-\\d{1,2}-\\d{1,2}");
                Matcher matcher1 = pattern.matcher(text);

                if (matcher1.find()) {
                    detectedValue = matcher.group().replace("-", "");  // remove dash
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading: " + pdfFile.getName());
            e.printStackTrace();
            return;
        }

        // -------- STEP 2: Rename AFTER document is closed --------
        if (detectedValue != null) {

            try {
                Path source = pdfFile.toPath();
                Path target = source.resolveSibling(detectedValue + ".pdf");

                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Renamed: " + pdfFile.getName()
                        + " → " + detectedValue + ".pdf");

            } catch (IOException e) {
                System.out.println("Error renaming: " + pdfFile.getName());
                e.printStackTrace();
            }

        } else {
            System.out.println("Pattern not found in: " + pdfFile.getName());
        }
    }
}

