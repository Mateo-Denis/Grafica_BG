package PdfFormater.codingerror.service;

import PdfFormater.Row;
import PdfFormater.codingerror.model.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodingErrorPdfInvoiceCreator {
    Document document;
    PdfDocument pdfDocument;
    String pdfName;
    float threecol=190f;
    float twocol=285f;
    float twocol150=twocol+150f;
    float twocolumnWidth[]={twocol150,twocol};
    float threeColumnWidth[]={threecol, threecol, threecol, threecol, threecol, threecol};
    float[] fullwidth = {threecol*6}; //Cambiose

    public CodingErrorPdfInvoiceCreator(String pdfName){
        this.pdfName=pdfName;
    }
    
    public void createDocument() throws FileNotFoundException {
        PdfWriter pdfWriter=new PdfWriter(pdfName);
        pdfDocument=new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        this.document=new Document(pdfDocument);
    }

    public   void createTnc(List<String> TncList,Boolean lastPage,String imagePath) {
        if(lastPage) {
            float threecol = 190f;
            float fullwidth[] = {threecol * 3};
            Table tb = new Table(fullwidth);
            tb.addCell(new Cell().add(new Paragraph(/*"TERMS AND CONDITIONS\n"*/"")).setBold().setBorder(Border.NO_BORDER));
            for (String tnc : TncList) {
                tb.addCell(new Cell().add(new Paragraph(tnc)).setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
            }

            document.add(tb);
        }else {
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new MyFooter(document,TncList,imagePath));
        }

        document.close();
    }

    public void createProduct(List<Product> productList, double totalPrice) {
        Table productsTable = new Table(threeColumnWidth);

        productsTable.addCell(new Cell().add(new Paragraph("Nombre")).setBold().setFontColor(DeviceGray.WHITE).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        productsTable.addCell(new Cell().add(new Paragraph("Cantidad")).setBold().setFontColor(DeviceGray.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        productsTable.addCell(new Cell().add(new Paragraph("Observaciones")).setBold().setFontColor(DeviceGray.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        productsTable.addCell(new Cell().add(new Paragraph("Dimensiones")).setBold().setFontColor(DeviceGray.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        productsTable.addCell(new Cell().add(new Paragraph("Precio")).setBold().setFontColor(DeviceGray.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        productsTable.addCell(new Cell().add(new Paragraph("Total")).setBold().setFontColor(DeviceGray.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceGray.BLACK, 0.7f));
        for (Product product:productList)
        {
            productsTable.addCell(new Cell().add(new Paragraph(product.getPname().orElse(""))).setBorder(Border.NO_BORDER));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getQuantity()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getObservations()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getDimensions()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getPriceperpeice()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getTotal()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        }
        document.add(productsTable);
        document.add(new Paragraph("\n"));
        document.add(fullwidthDashedBorder(fullwidth));


        Table totalTable = new Table(threeColumnWidth);
        totalTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("Total")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalPrice))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        document.add(totalTable);

        document.add(fullwidthDashedBorder(fullwidth));
        document.add(new Paragraph("\n"));
        document.add(getDividerTable(fullwidth).setBorder(new SolidBorder(new DeviceGray(0.75f),1)).setMarginBottom(15f));
    }

    public List<Product> formatProductsToProductsList(ArrayList<Row> rows){
        List<Product> productList = new ArrayList<>();
        for(Row row:rows){
            productList.add(new Product(row.getProductName(),row.getQuantity(),row.getMeasures(), row.getObservations(), row.getPrice(),row.getTotal()));
        }
        return productList;
    }

    public void createAddress(AddressDetails addressDetails) {
        Table twoColTable=new Table(twocolumnWidth);
        twoColTable.addCell(getBillingandShippingCell(addressDetails.getBillingInfoText()));
        twoColTable.addCell(getBillingandShippingCell(addressDetails.getShippingInfoText()));
        document.add(twoColTable.setMarginBottom(12f));
        //iNFO FIRST ROW
        Table twoColTable2=new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft(addressDetails.getBillingCompanyText(),true));
        twoColTable2.addCell(getCell10fLeft(addressDetails.getShippingNameText(),true));
        twoColTable2.addCell(getCell10fLeft(addressDetails.getBillingCompany(),false));
        twoColTable2.addCell(getCell10fLeft(addressDetails.getShippingName(),false));
        document.add(twoColTable2);


        Table twoColTable3=new Table(twocolumnWidth);
        twoColTable3.addCell(getCell10fLeft(addressDetails.getBillingNameText(),true));
        twoColTable3.addCell(getCell10fLeft(addressDetails.getShippingAddressText(),true));
        twoColTable3.addCell(getCell10fLeft(addressDetails.getBillingName(),false));
        twoColTable3.addCell(getCell10fLeft(addressDetails.getShippingAddress(),false));
        document.add(twoColTable3);
        float oneColoumnwidth[]={twocol150};

        Table oneColTable1=new Table(oneColoumnwidth);
        oneColTable1.addCell(getCell10fLeft(addressDetails.getBillingAddressText(),true));
        oneColTable1.addCell(getCell10fLeft(addressDetails.getBillingAddress(),false));
        oneColTable1.addCell(getCell10fLeft(addressDetails.getBillingEmailText(),true));
        oneColTable1.addCell(getCell10fLeft(addressDetails.getBillingEmail(),false));
        document.add(oneColTable1.setMarginBottom(10f));
    }

    public void createHeader(HeaderDetails header, String imagePath) {
        ImageData imageData = null;
        try {
            imageData = ImageDataFactory.create(imagePath);
        } catch (MalformedURLException e) {
        }
        Image image = new Image(imageData);
        //float x = pdfDocument.getDefaultPageSize().getWidth();
        float y = pdfDocument.getDefaultPageSize().getHeight() ;
//        System.out.println("x= " + x + " y= " + y);
        image.setFixedPosition(70, y-110);
        //image.setOpacity(0.1f);
        document.add(image);
        //
        Table table=new Table(twocolumnWidth);
        table.addCell(new Cell().add(new Paragraph(header.getInvoiceTitle())).setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
        Table nestedtabe=new Table(new float[]{twocol/2,twocol/2});
        nestedtabe.addCell(getHeaderTextCell(header.getInvoiceNoText()));
        nestedtabe.addCell(getHeaderTextCellValue(header.getInvoiceNo()));
        nestedtabe.addCell(getHeaderTextCell(header.getInvoiceDateText()));
        nestedtabe.addCell(getHeaderTextCellValue(header.getInvoiceDate()));
        table.addCell(new Cell().add(nestedtabe).setBorder(Border.NO_BORDER));
        Border gb=new SolidBorder(header.getBorderColor(),2f);
        document.add(table);
        document.add(getNewLineParagraph());
        document.add(getDividerTable(fullwidth).setBorder(gb));
        document.add(getNewLineParagraph());
    }

    static  Table getDividerTable(float[] fullwidth)
    {
        return new Table(fullwidth);
    }
    static Table fullwidthDashedBorder(float[] fullwidth)
    {
        Table tableDivider2=new Table(fullwidth);
        Border dgb=new DashedBorder(DeviceGray.GRAY,0.5f);
        tableDivider2.setBorder(dgb);
        return tableDivider2;
    }
    static  Paragraph getNewLineParagraph()
    {
        return new Paragraph("\n");
    }
    static Cell getHeaderTextCell(String textValue)
    {

        return new Cell().add(new Paragraph(textValue)).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }
    static Cell getHeaderTextCellValue(String textValue)
    {


        return new Cell().add(new Paragraph(textValue)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }
    static Cell getBillingandShippingCell(String textValue)
    {

        return new Cell().add(new Paragraph(textValue)).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static  Cell getCell10fLeft(String textValue,Boolean isBold){
        Cell myCell=new Cell().add(new Paragraph(textValue)).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return  isBold ?myCell.setBold():myCell;

    }
}
