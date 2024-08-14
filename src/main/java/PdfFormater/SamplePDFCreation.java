package PdfFormater;

import utils.Client;

import java.io.FileNotFoundException;

public class SamplePDFCreation {

    public static void createWeirdAahPDF(){
        PdfConverter pdfConverter  = new PdfConverter();
        try {
            int biggabytes = 4;
            Row rowzilla = new Row("Bandera 10x10",2,2,2,2);
            Row rowzilla1 = new Row("Remera XL sublimada",1,66,200,200);
            Row[] mamongas = new Row[biggabytes];

            mamongas[0] = rowzilla;
            mamongas[1]= rowzilla1;

            for(int i = 2;i<biggabytes;i++){
                Row rowcita = new Row("Ejemplo "+i, i,2,0,0);
                mamongas[i] = rowcita;
            }
            pdfConverter.generateBill(new Client("Pepito","Juan Molina", "Bahía Blanca", "123456789", false),
                    314,mamongas,99999);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
