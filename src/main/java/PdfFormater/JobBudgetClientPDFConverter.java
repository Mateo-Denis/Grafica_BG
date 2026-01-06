package PdfFormater;

import PdfFormater.codingerror.model.AddressDetails;
import PdfFormater.codingerror.model.HeaderDetails;
import PdfFormater.codingerror.service.CodingErrorPdfInvoiceCreator;
import com.itextpdf.layout.element.Paragraph;
import utils.Client;
import utils.NewProduct;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JobBudgetClientPDFConverter {
    String pdfName = "Hola.pdf";
    CodingErrorPdfInvoiceCreator cepdf =new CodingErrorPdfInvoiceCreator(pdfName);
/*
    String pdfName = "job_budget_client.pdf";
*/

    public void generateBill(boolean isPreview, Client client, int billNumber, ArrayList<NewRow> tableContent, double total) throws FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld = LocalDate.now();
        String formattedDate = ld.format(formatter);
        LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);

        String pdfName;

        // Generate PDF file name
        if(isPreview){
            pdfName = "temp_preview.pdf";
        }else {
            pdfName = client.getName() +"_"+ parsedDate + "_" + billNumber + ".pdf";
        }

        String imagePath="src/main/resources/BGLogo.png"; // Path to your logo image

        CodingErrorPdfInvoiceCreator cepdf =new CodingErrorPdfInvoiceCreator(pdfName);
        String finalPath = cepdf.createDocument();

        //Create Header start
        HeaderDetails header=new HeaderDetails();
        header.setInvoiceNo(billNumber+""). // Set invoice number
                setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .setInvoiceTitle("")
                .setInvoiceNoText("N° presupuesto")
                .setInvoiceDateText("Fecha:")
                .build();
        cepdf.createHeader(header, imagePath);
        //Header End

        //Create Address start
        AddressDetails addressDetails=new AddressDetails();
        addressDetails
                .setBillingInfoText("Cliente")
                .setBillingCompanyText("Nombre")
                .setBillingCompany(client.getName())
                .setBillingNameText("Dirección")
                .setBillingName(client.getAddress())
                .setBillingAddressText("Localidad")
                .setBillingAddress(client.getCity())
                .setBillingEmailText("Teléfono")
                .setBillingEmail(client.getPhone())
                .setShippingInfoText("")
                .setShippingNameText("")
                .setShippingAddressText("")
                .build();

        cepdf.createAddress(addressDetails);
        //Address end

        //Product Start
        List<NewProduct> productList = cepdf.formatNewProductsToProductsList(tableContent);
        cepdf.createNewProduct(productList, total);
        //Product End

        //Term and Condition Start
        Paragraph tncLine1 = new Paragraph("Presupuesto válido por 48 hs. \n ").setItalic();
        Paragraph tncLine2 = new Paragraph("Modalidad de pago: 50% al confirmar y 50% al finalizar. \n ").setItalic();
        Paragraph tncLine3 = new Paragraph("\n Por cualquier consulta, no dude en comunicarse con nosotros. \n ").setUnderline();
        List<Paragraph> TncList=new ArrayList<>();
        TncList.add(tncLine1);
        TncList.add(tncLine2);
        TncList.add(tncLine3);
        cepdf.createTnc(TncList,false,imagePath);
        // Term and condition end

        try {
            File pdf = new File(finalPath);
            if (pdf.exists()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(pdf); // Opens with system default viewer
            } else {
                System.out.println("File not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
