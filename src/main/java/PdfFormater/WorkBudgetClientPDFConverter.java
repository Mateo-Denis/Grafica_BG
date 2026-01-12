package PdfFormater;

import PdfFormater.codingerror.model.AddressDetails;
import PdfFormater.codingerror.model.HeaderDetails;
import PdfFormater.codingerror.service.CodingErrorPdfInvoiceCreator;
import com.itextpdf.layout.element.Paragraph;
import org.javatuples.Pair;
import utils.Client;
import utils.NewProduct;
import utils.TextUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static utils.TextUtils.truncateAndRound;

public class WorkBudgetClientPDFConverter {
    String pdfName = "Hola.pdf";
    CodingErrorPdfInvoiceCreator cepdf =new CodingErrorPdfInvoiceCreator(pdfName);
    private final TextUtils textUtils = new TextUtils();
/*
    String pdfName = "job_budget_client.pdf";
*/

    public void generateBill(boolean isModification, Client client, int billNumber, ArrayList<Pair<String, String>> baseTableContent, String budgetTotal) throws FileNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld = LocalDate.now();
        String formattedDate = ld.format(formatter);
        LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);

        String pdfName;

        // Generate PDF file name
        if(isModification){
            pdfName = client.getName() +"_"+ parsedDate + "_" + billNumber + "_MOD.pdf";
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
        ArrayList<NewRow> tableContent = textUtils.toTableRow(baseTableContent);
        List<NewProduct> productList = cepdf.formatNewProductsToProductsList(tableContent);
        double total = Double.parseDouble(truncateAndRound(budgetTotal));
        cepdf.createNewProduct(productList, total);
        //Product End

        //Term and Condition Start
        Paragraph tncLine1 = new Paragraph("Presupuesto válido por 48 hs. \n ").setItalic();
        Paragraph tncLine2 = new Paragraph("Modalidad de pago: 50% al confirmar y 50% al finalizar. \n ").setItalic();
        Paragraph tncLine3 = new Paragraph("En caso de necesitar factura, al precio se le agregará el IVA. \n").setBold();
        Paragraph tncLine4 = new Paragraph("\n Por cualquier consulta, no dude en comunicarse con nosotros. \n ").setUnderline();
        List<Paragraph> TncList=new ArrayList<>();
        TncList.add(tncLine1);
        TncList.add(tncLine2);
        TncList.add(tncLine3);
        TncList.add(tncLine4);
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
