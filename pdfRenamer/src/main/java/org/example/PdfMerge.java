package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PdfMerge {

    public static void main(String[] args) {

        String folderPath = "C:/Users/kwong/Documents/InteliJ/pdf/INV";

        // List the PDF file names you want to merge
        List<String> pdfNames = Arrays.asList(
                "C1-01.pdf",
                "C2-01.pdf",
                "C1-04.pdf",
                "E509.pdf",
                "E5-09.pdf"
        );

        mergePdfs(folderPath, pdfNames);
    }

    private static void mergePdfs(String folderPath, List<String> pdfNames) {

        PDFMergerUtility merger = new PDFMergerUtility();

        try {
            for (String pdfName : pdfNames) {
                File file = new File(folderPath + "/" + pdfName);

                if (file.exists()) {
                    merger.addSource(file);
                } else {
                    System.out.println("File not found: " + pdfName);
                }
            }

            merger.setDestinationFileName(folderPath + "/merged.pdf");
            merger.mergeDocuments(null);

            System.out.println("PDFs merged successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}