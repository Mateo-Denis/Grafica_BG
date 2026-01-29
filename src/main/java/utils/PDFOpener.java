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
            pdfFile = pdfSearch(folderDir, budgetNumber, clientName, date);
        }else if (isClientWorkBudget) {
            pdfFile = workClientPdfSearch(folderDir, budgetNumber, clientName, date);
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
        String fileName = "p_interno_" + billNumber + "_" + clientName + "_" + date + ".pdf";

        //Regex para buscar una copia con otra fecha
        String regex = "p_interno_" + billNumber + "_" + clientName + "_" + "\\d{4}-\\d{2}-\\d{2}\\.pdf";

         //Search for copies (fileNames containing different dates)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of internal works: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.
        String finalPath = fileDir + fileName;

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files.");
            //Analyze matching files to find previous dates files, and delete them
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (File file : matchingFiles) {
                String fileDateStr = file.getName().split("_")[4].replace(".pdf", "");
                LocalDate fileDate = LocalDate.parse(fileDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println("Found file with date: " + fileDate);
                System.out.println("Target date: " + targetDate + " is after file date: " + fileDate + ". Deleting file.");
                if (fileDate.isBefore(targetDate)) {
                    if (file.delete()) {
                        System.out.println("Deleted old file: " + file.getName());
                    } else {
                        System.out.println("Failed to delete old file: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("No matching files found for different dates.");
        }

        File pdfFile = new File(finalPath);
        System.out.println("PDF file path: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    public File workClientPdfSearch(String folderDir, int billNumber, String clientName, String date) {
        String fileDir = System.getProperty("user.dir") + folderDir;
        String fileName = "p_cliente_" + billNumber + "_" + clientName + "_" + date + ".pdf";

        //Regex para buscar una copia con otra fecha
        String regex = "p_cliente_" + billNumber + "_" + clientName + "_" + "\\d{4}-\\d{2}-\\d{2}\\.pdf";

         //Search for copies (fileNames containing different dates)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of client works: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.
        String finalPath = fileDir + fileName;

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files.");
            //Analyze matching files to find previous dates files, and delete them
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (File file : matchingFiles) {
                String fileDateStr = file.getName().split("_")[4].replace(".pdf", "");
                LocalDate fileDate = LocalDate.parse(fileDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println("Found file with date: " + fileDate);
                System.out.println("Target date: " + targetDate + " is after file date: " + fileDate + ". Deleting file.");
                if (fileDate.isBefore(targetDate)) {
                    if (file.delete()) {
                        System.out.println("Deleted old file: " + file.getName());
                    } else {
                        System.out.println("Failed to delete old file: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("No matching files found for different dates.");
        }

        File pdfFile = new File(finalPath);
        System.out.println("PDF file path: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    public File pdfSearch(String folderDir, int budgetNumber, String clientName, String date) {
        String fileDir = System.getProperty("user.dir") + folderDir;
        String fileName = "";
        String regex = "";
        fileName = clientName + "_" + date + "_" + budgetNumber +  ".pdf";
        regex = clientName + "_" + date + "_" + budgetNumber + "( - COPIA \\d+)?\\.pdf";


        String finalPath = fileDir + fileName;

        File pdfFile;

        //Search for copies (fileNames containing COPIA 1, COPIA 2, etc)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of clients: " + fileDir);
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
            System.out.println("No matching files found. Using original file name: " + finalPath);
            pdfFile = new File(finalPath);
        }

        System.out.println("PDF file path: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    public void clientUpdateDeletePDFS(String folderDir, String clientName) {
        String fileDir = System.getProperty("user.dir") + folderDir;

        //Regex para buscar una copia con cualquier fecha y cualquier budget number
        String regex = clientName + "_" + "\\d{4}-\\d{2}-\\d{2}_" + "\\d+" + ".pdf";
         //Search for copies (fileNames containing different dates)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of clients for deletion: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files for deletion.");
            for (File file : matchingFiles) {
                if (file.delete()) {
                    System.out.println("Deleted file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        } else {
            System.out.println("No matching files found for deletion.");
        }
    }

    public void clientUpdateDeleteWorkPDFS(String folderDir, String clientName) {
        String fileDir = System.getProperty("user.dir") + folderDir;

        //Regex para buscar una copia con cualquier fecha y cualquier budget number
        String regex = "p_interno_" + "\\d+" + "_" + clientName + "_" + "\\d{4}-\\d{2}-\\d{2}\\.pdf";
         //Search for copies (fileNames containing different dates)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of client works for deletion: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files for deletion.");
            for (File file : matchingFiles) {
                if (file.delete()) {
                    System.out.println("Deleted file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        } else {
            System.out.println("No matching files found for deletion.");
        }
    }

    public void clientUpdateDeleteClientWorkPDFs(String folderDir, String clientName) {
        String fileDir = System.getProperty("user.dir") + folderDir;

        //Regex para buscar una copia con cualquier fecha y cualquier budget number
        String regex = "p_cliente_" + "\\d+" + "_" + clientName + "_" + "\\d{4}-\\d{2}-\\d{2}\\.pdf";
         //Search for copies (fileNames containing different dates)
        File dir = new File(fileDir);
        System.out.println("Searching in directory of client works for deletion: " + fileDir);
        String finalRegex = regex;
        File[] matchingFiles = dir.listFiles((d, name) -> name.matches(finalRegex)); // Filter files matching the regex. "d" is the directory, "name" is the file name.

        if (matchingFiles != null && matchingFiles.length > 0) {
            System.out.println("Found " + matchingFiles.length + " matching files for client work budget deletion.");
            for (File file : matchingFiles) {
                if (file.delete()) {
                    System.out.println("Deleted file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        } else {
            System.out.println("No matching files found for deletion.");
        }
    }
}