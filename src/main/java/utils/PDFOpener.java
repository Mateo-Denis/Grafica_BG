package utils;

import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFOpener {
    public void openPDF(boolean isTraditionalBudget, boolean isClientWorkBudget, String folderDir, int budgetNumber, String clientName, String date) {
        File pdfFile = new File("");
        if(isTraditionalBudget){
            pdfFile = pdfSearch(false, folderDir, budgetNumber, clientName, date);
        }else if (isClientWorkBudget) {
            pdfFile = pdfSearch(true, folderDir, budgetNumber, clientName, date);
        } else {
            pdfFile = workPdfSearch(folderDir, budgetNumber, clientName, date);
        }

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
            System.out.println("LA CHINGADERA NI EXISTE");
        }
    }

    public File workPdfSearch(String folderDir, int billNumber, String clientName, String date) {
        String fileDir = System.getProperty("user.dir") + folderDir;
        String fileName = "presupuesto_interno_" + clientName + "_" + date + "_" + billNumber + ".pdf";
        String finalPath = fileDir + fileName;
        System.out.println("Final path del wero: " + finalPath);

        File pdfFile;

        //Search for copies (fileNames containing COPIA 1, COPIA 2, etc.)
        String regex = "presupuesto_interno_" + "_" + clientName + "_" + date + "_" + billNumber + "( - COPIA \\d+)?\\.pdf";
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

        System.out.println("PDF file path: " + finalPath);
        return pdfFile;
    }

    public File pdfSearch(boolean isClientWorkBudget, String folderDir, int budgetNumber, String clientName, String date) {
        String fileDir = System.getProperty("user.dir") + folderDir;
        String fileName = "";
        String regex = "";

        if (!isClientWorkBudget) {
            fileName = clientName + "_" + date + "_" + budgetNumber + ".pdf";
            regex = clientName + "_" + date + "_" + budgetNumber + "( - COPIA \\d+)?\\.pdf";
        } else {
            fileName = "presupuesto_cliente_" + clientName + "_" + date + "_" + budgetNumber + ".pdf";
            regex = "presupuesto_cliente_" + clientName + "_" + date + "_" + budgetNumber + "( - COPIA \\d+)?\\.pdf";
        }

        String finalPath = fileDir + fileName;

        File pdfFile;

        //Search for copies (fileNames containing COPIA 1, COPIA 2, etc)
        File dir = new File(fileDir);
        System.out.println("Searching in directory: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.

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

    public int getMaxCopyNumber(String folderDir, String copiesRegex) {
        int copyCounter = -1;
        // Itera en busca del archivo original o sus copias, buscando la copia con el número más alto. Puede estar intercalado con otros archivos.
        File dir = new File(System.getProperty("user.dir") + folderDir);
        System.out.println("Searching in directory for copies: " + dir.getAbsolutePath());
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(copiesRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.
        System.out.println("Found " + (matchingFiles != null ? matchingFiles.length : 0) + " matching files for copies.");
        if (matchingFiles != null && matchingFiles.length > 0) {
            for (File file : matchingFiles) {
                String fileName = file.getName();
                if (fileName.contains(" - COPIA ")) {
                    String[] parts = fileName.split(" - COPIA ");
                    if (parts.length == 2) {
                        String copyPart = parts[1].replace(".pdf", "").trim();
                        try {
                            int copyNumber = Integer.parseInt(copyPart);
                            if (copyNumber > copyCounter) {
                                copyCounter = copyNumber;
                            }
                        } catch (NumberFormatException e) {
                            // No es un número válido, ignorar
                        }
                    }
                } else {
                    // Archivo original sin "COPIA"
                    if (copyCounter < 0) {
                        copyCounter = 0;
                    }
                }
            }
        } else {
            copyCounter = -2; // No se encontraron archivos, por lo que no hay copias.
        }

        return copyCounter;
    }
}
