package PdfFormater;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import org.javatuples.Pair;
import utils.TextUtils;
import utils.databases.ClientsDatabaseConnection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static utils.TextUtils.truncateAndRound;

public class WorkBudgetPDFConverter {
    private static PdfFont FONT;
    private static final TextUtils textUtils = new TextUtils();
    private static final ClientsDatabaseConnection clientsDB = new ClientsDatabaseConnection();


    private static void initFonts() throws IOException {

        FontProgram arialNarrowProgram =
                FontProgramFactory.createFont("src/main/resources/fonts/arial_narrow_7.ttf");

        FONT = PdfFontFactory.createFont(
                arialNarrowProgram,
                PdfEncodings.IDENTITY_H
        );

    }

    private String getClientName(int clientID) {
        return clientsDB.getClientNameByID(clientID);
    }

    public void generateWorkBill(boolean modified,int billNumber,  int clientID, ArrayList<Pair<String,String>> strMaterials, Pair<String,String> logistics, Pair<String,String> placing,
                                        String deposit, String balance, String budgetCost, String finalCost) throws IOException {
        initFonts();

        ArrayList<NewRow> materials = textUtils.toTableRow(strMaterials);

        String outputPath = "presupuesto_trabajo_" + billNumber + ".pdf";
        if(modified) {
            outputPath = "presupuesto_trabajo_" + billNumber + "_modificado.pdf";
        }

        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf, PageSize.A4);
        String clientName = getClientName(clientID);

        doc.setMargins(20, 20, 20, 20);

        // ================= ENCABEZADO =================
        Table header = new Table(new float[]{2, 5});
        header.setWidth(UnitValue.createPercentValue(100));

        String logoPath = "src/main/resources/BGLogo.png"; // Ruta de la imagen del logo

        // Imagen (logo)
        Image logo = new Image(
                ImageDataFactory.create(logoPath) // imagen cualquiera
        ).scaleToFit(120, 60);

        Cell logoCell = new Cell()
                .add(logo)
                .setBorder(Border.NO_BORDER)
                .setMarginBottom(30);

        header.addCell(logoCell);

        Table datos = new Table(1);
        datos.setWidth(UnitValue.createPercentValue(100));
        datos.setMarginBottom(30);

// ===== FILA FECHA =====
        Table filaFecha = new Table(new float[]{3, 2});
        filaFecha.setWidth(UnitValue.createPercentValue(100));

        filaFecha.addCell(new Cell()
                .add(new Paragraph("Fecha:")
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER))
                .setFont(FONT);

        filaFecha.addCell(new Cell()
                .add(new Paragraph(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .setFontSize(10)
                        .setFont(FONT)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER));

        datos.addCell(new Cell()
                .add(filaFecha)
                .setBorder(new SolidBorder(1))
                .setPadding(6)
        );

        // ===== FILA NÚMERO PRESUPUESTO =====
        Table filaNumeroPresupuesto = new Table(new float[]{3, 2});
        filaNumeroPresupuesto.setWidth(UnitValue.createPercentValue(100));

        filaNumeroPresupuesto.addCell(new Cell()
                .add(new Paragraph("N° Presupuesto:")
                        .setFontSize(10))
                .setFont(FONT)
                .setBorder(Border.NO_BORDER));

        filaNumeroPresupuesto.addCell(new Cell()
                .add(new Paragraph(String.valueOf(billNumber))
                        .setFontSize(10)
                        .setFont(FONT)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER));

        datos.addCell(new Cell()
                .add(filaNumeroPresupuesto)
                .setBorder(new SolidBorder(1))
                .setPadding(6)
        );

// ===== FILA NOMBRE CLIENTE =====
        Table filaNombre = new Table(new float[]{3, 2});
        filaNombre.setWidth(UnitValue.createPercentValue(100));

        filaNombre.addCell(new Cell()
                .add(new Paragraph("Nombre del cliente:")
                        .setFontSize(10))
                        .setFont(FONT)
                .setBorder(Border.NO_BORDER));

        filaNombre.addCell(new Cell()
                .add(new Paragraph(clientName)
                        .setFontSize(10)
                        .setFont(FONT)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER));

        datos.addCell(new Cell()
                .add(filaNombre)
                .setBorder(new SolidBorder(1))
                .setPadding(6)
        );

        header.addCell(new Cell().add(datos).setBorder(Border.NO_BORDER));

        doc.add(header);
        doc.add(separador());

        // ================= MATERIALES =================
        doc.add(bloqueMateriales(materials));
        doc.add(espacio());

        // ================= LOGÍSTICA =================
        doc.add(bloqueLogistica(logistics.getValue0(), Double.parseDouble(logistics.getValue1())));
        doc.add(espacio());

        // ================= COLOCACIÓN =================
        doc.add(bloqueColocacion(placing.getValue0(), Double.parseDouble(placing.getValue1())));
        doc.add(espacio());

        // ================= PAGOS =================
        double doubleDepositValue = Double.parseDouble(truncateAndRound(deposit));
        double doubleBalanceValue = Double.parseDouble(truncateAndRound(balance));
        doc.add(bloquePagos(doubleDepositValue, doubleBalanceValue));
        doc.add(espacio());

        // ================= COSTO PRESUPUESTO =================
        double doubleBudgetCost = Double.parseDouble(truncateAndRound(finalCost));
        doc.add(bloquesSubtotalYTotal(doubleBudgetCost, "COSTO PRESUPUESTO"));
        doc.add(espacio());

        // ================= PRESUPUESTO FINAL =================
        double doubleFinalCost = Double.parseDouble(truncateAndRound(finalCost));
        doc.add(bloquesSubtotalYTotal(doubleFinalCost, "PRESUPUESTO FINAL"));
        doc.add(espacio());

        doc.close();
        openPDF(outputPath);
    }

    // ================= UTILIDADES =================

    private static Table bloquesSubtotalYTotal (double cost, String titulo) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(50)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph(titulo)
                .setBold()
                .setFontSize(10)
                .setFont(FONT)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        // Agregar el valor del costo subtotal, pegado al fondo del bloque, sin etiqueta
        bloque.add(new Paragraph("").setHeight(10));
        bloque.add(new Paragraph(String.format("%.2f", cost))
                .setBold()
                .setFontSize(10)
                .setFont(FONT)
                .setTextAlignment(TextAlignment.LEFT));

        t.addCell(bloque);
        return t;
    }

    private static Table bloquePagos(double deposit, double balance) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(50)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        // Tabla interna para Seña y Saldo en horizontal
        Table fila = new Table(new float[]{3, 2});
        fila.setWidth(UnitValue.createPercentValue(100));

        fila.addCell(new Cell()
                .add(new Paragraph("Seña: $" + String.format("%.2f", deposit))
                        .setBold()
                        .setFontSize(10))
                .setFont(FONT)
                .setBorder(Border.NO_BORDER));

        fila.addCell(new Cell()
                .add(new Paragraph("Saldo: $" + String.format("%.2f", balance))
                        .setBold()
                        .setFontSize(10))
                .setFont(FONT)
                .setBorder(Border.NO_BORDER));

        // Empuja la fila hacia abajo del bloque
        bloque.add(new Paragraph("").setHeight(10));
        bloque.add(fila);

        t.addCell(bloque);
        return t;
    }

    private static Table bloqueMateriales(ArrayList<NewRow> materials) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setMinHeight(20)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph("MATERIALES")
                .setBold()
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        // Tabla interna para los materiales (2 columnas)
        Table materialesTable = new Table(new float[]{4, 2});
        materialesTable.setWidth(UnitValue.createPercentValue(100));
        for (NewRow row : materials) {
            materialesTable.addCell(new Cell()
                    .add(new Paragraph(row.getProductDescription())
                            .setFontSize(10))
                    .setFont(FONT)
                    .setBorder(Border.NO_BORDER));

            materialesTable.addCell(new Cell()
                    .add(new Paragraph("$" + String.format("%.2f", row.getTotal()))
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setFont(FONT)
                    .setBorder(Border.NO_BORDER));
        }

        bloque.add(materialesTable);
        t.addCell(bloque);
        return t;
    }

    private static Table bloqueLogistica(String description, double price) {

        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setMinHeight(20) // ← mínimo, no fijo
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph("LOGÍSTICA")
                .setBold()
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        Table fila = new Table(new float[]{3, 2});
        fila.setWidth(UnitValue.createPercentValue(100));

        Paragraph descTitle = new Paragraph("Descripción: ")
                .setBold()
                .setFontSize(10);

        Paragraph descInfo = autoShrinkParagraph(description, 10);

        Paragraph desc = new Paragraph("");

        desc.add(descTitle);
        desc.add(descInfo);

        fila.addCell(new Cell()
                .add(desc)
                .setFont(FONT)
                .setBorder(Border.NO_BORDER))
                .setMaxHeight(500); // ← para que se expanda si es necesario

        fila.addCell(new Cell()
                .add(new Paragraph("Precio: $" + String.format("%.2f", price))
                        .setBold()
                        .setFontSize(10))
                .setFont(FONT)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT));

        bloque.add(fila);
        t.addCell(bloque);

        return t;
    }

    private static LineSeparator separador() {
        return new LineSeparator(new SolidLine());
    }

    private static Paragraph espacio() {
        return new Paragraph("").setMarginBottom(10);
    }

    private static Table bloqueColocacion(String colocatorName, double price) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(50)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph("COLOCACIÓN")
                .setBold()
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        // Tabla interna para Colocador y Precio en horizontal
        Table fila = new Table(new float[]{3, 2});
        fila.setWidth(UnitValue.createPercentValue(100));

        fila.addCell(new Cell()
                .add(new Paragraph("Colocador: " + colocatorName)
                        .setBold()
                        .setFontSize(10))
                        .setFont(FONT)
                        .setBorder(Border.NO_BORDER));

        fila.addCell(new Cell()
                .add(new Paragraph("Precio: $" + String.format("%.2f", price))
                        .setBold()
                        .setFontSize(10))
                        .setFont(FONT)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(Border.NO_BORDER));

        // Empuja la fila hacia abajo del bloque
        bloque.add(new Paragraph("").setHeight(10));
        bloque.add(fila);

        t.addCell(bloque);
        return t;
    }

    private static void openPDF(String finalPath) {
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

    private static Paragraph autoShrinkParagraph(String text, float baseSize) {
        float size = baseSize;

        if (text.length() > 250) size -= 0.5;
        if (text.length() > 500) size -= 0.5;
        if (text.length() > 800) size -= 0.5;
        if (size < 7) size = 7;

        return new Paragraph(text)
                .setFont(FONT)
                .setFontSize(size);
    }
}

