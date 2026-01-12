package utils;

import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFOpener {
    public void openPDF(int budgetNumber, String clientName, String date) {
        File pdfFile = pdfSearch(budgetNumber, clientName, date);
        if (pdfFile.exists()) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Awt Desktop is not supported!");
            }
        } else {
            System.out.println("File does not exist!");
        }
    }

    public File pdfSearch(int budgetNumber, String clientName, String date) {
        String fileDir = System.getProperty("user.dir") + "/PresupuestosPDF/";
        String fileName = clientName + "_" + date + "_" + budgetNumber + ".pdf";
        String finalPath = fileDir + fileName;

        File pdfFile;

        //Search for copies (fileNames containing COPIA 1, COPIA 2, etc)
        String regex = clientName + "_" + date + "_" + budgetNumber + "( - COPIA \\d+)?\\.pdf";
        File dir = new File(fileDir);
        System.out.println("Searching in directory: " + fileDir);
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(regex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files.");
            //Analyze matching files to find the latest copy
            File latestFile = matchingFiles[0];
            for (File file : matchingFiles) {
                if (file.lastModified() > latestFile.lastModified()) {
                    latestFile = file;
                    }
                }
            pdfFile = latestFile;
            } else {
            pdfFile = new File(finalPath);
            }

        System.out.println("PDF file path: " + pdfFile.getAbsolutePath());
        return pdfFile;
        }
    }
