package PdfFormater;

import PdfFormater.codingerror.model.AddressDetails;
import PdfFormater.codingerror.model.HeaderDetails;
import PdfFormater.codingerror.model.Product;
import PdfFormater.codingerror.model.ProductTableHeader;
import PdfFormater.codingerror.service.CodingErrorPdfInvoiceCreator;
import utils.Client;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PdfConverter implements IPdfConverter{
    public PdfConverter(){}
    @Override
    public void generateBill(boolean isPreview, Client client, int billNumber, Row[] tableContent, float total) throws FileNotFoundException {
        LocalDate ld= LocalDate.now();
        String pdfName;
        if(isPreview){
            pdfName = "temp_preview.pdf";
        }else {
            pdfName = billNumber+"_"+ ld +".pdf";
        }
        String imagePath="src/main/resources/BGLogo.png";
        CodingErrorPdfInvoiceCreator cepdf=new CodingErrorPdfInvoiceCreator(pdfName);
        cepdf.createDocument();

        //Create Header start
        HeaderDetails header=new HeaderDetails();
        header.setInvoiceNo(billNumber+"").
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
                //.setShippingName("Customer Name \n")
                .setShippingInfoText("")
                .setShippingNameText("")
                .setShippingAddressText("")
                //.setShippingAddress("Banglore Name sdss\n swjs\n")
                .build();

        cepdf.createAddress(addressDetails);
        //Address end

        //Product Start
        ProductTableHeader productTableHeader=new ProductTableHeader();
        cepdf.createTableHeader(productTableHeader);
        List<Product> productList=cepdf.formatProductsToProductsList(tableContent);
       // productList=cepdf.modifyProductList(productList);
        cepdf.createProduct(productList, total);
        //Product End

        //Term and Condition Start
        List<String> TncList=new ArrayList<>();
        TncList.add("Precio válido por 48hs.");
        //I left this as a sample, you can add more stuff here
        /*
        TncList.add("1. The Seller shall not be liable to the Buyer directly or indirectly for any loss or damage suffered by the Buyer.");
        TncList.add("2. The Seller warrants the product for one (1) year from the date of shipment");*/
        cepdf.createTnc(TncList,false,imagePath);
        // Term and condition end

    }
}
