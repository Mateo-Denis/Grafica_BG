package PdfFormater;

import utils.Client;

import java.io.FileNotFoundException;

public class SamplePDFCreation {

    public static void createWeirdAahPDF(){
        PdfConverter pdfConverter  = new PdfConverter();
        try {
            int biggabytes = 55;
            Row rowzilla = new Row("Xie huao piao piao",2,2,2,2);
            Row rowzilla1 = new Row("Standing on the Grassland Looking at Beijing",1,66,200,200);
            Row[] mamongas = new Row[biggabytes];

            mamongas[0] = rowzilla;
            mamongas[1]= rowzilla1;

            for(int i = 2;i<biggabytes;i++){
                Row rowcita = new Row("Bing chilling"+i,i,2,0,0);
                mamongas[i] = rowcita;
            }
            pdfConverter.generateBill(new Client("SuperIdol","De xiao rong", "dou mei ni de tien.", "101", true),
                    666,mamongas,100);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
