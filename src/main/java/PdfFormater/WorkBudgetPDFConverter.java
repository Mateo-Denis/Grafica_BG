/*
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WorkBudgetPDFConverter {
    private static int counter;
    private static final Random Random = new Random();
    private static PdfFont FONT;
    private static final TextUtils textUtils = new TextUtils();


    private static void initFonts() throws IOException {

        FontProgram arialNarrowProgram =
                FontProgramFactory.createFont("src/main/resources/fonts/arial_narrow_7.ttf");

        FONT = PdfFontFactory.createFont(
                arialNarrowProgram,
                PdfEncodings.IDENTITY_H
        );

    }

    public void generateWorkBill(ArrayList<Pair<String,String>> strMaterials, Pair<String,String> logistics, Pair<String,String> placing,
                                        ArrayList<Double> depositAndBalanceList, double budgetCost, double finalCost) throws IOException {
        initFonts();
        ArrayList<NewRow> materials = textUtils.toTableRow(strMaterials);
        counter = Random.nextInt(1000, 9999); // Generar un número aleatorio para el nombre del archivo
        PdfWriter writer = new PdfWriter("factura" + counter + ".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf, PageSize.A4);

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
                .add(new Paragraph("01/01/2026")
                        .setFontSize(10)
                        .setFont(FONT)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER));

        datos.addCell(new Cell()
                .add(filaFecha)
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
                .add(new Paragraph("Cliente BD")
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
        double deposit = depositAndBalanceList.get(0);
        double balance = depositAndBalanceList.get(1);
        doc.add(bloquePagos(deposit, balance));
        doc.add(espacio());

        // ================= COSTO PRESUPUESTO =================
        doc.add(bloquesSubtotalYTotal(budgetCost, "COSTO PRESUPUESTO"));
        doc.add(espacio());

        // ================= PRESUPUESTO FINAL =================
        doc.add(bloquesSubtotalYTotal(finalCost, "PRESUPUESTO FINAL"));
        doc.add(espacio());

        doc.close();
        openPDF("factura" + counter + ".pdf");
    }

    // ================= UTILIDADES =================

    private static Table bloquesSubtotalYTotal (double cost, String titulo) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(70)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph(titulo)
                .setBold()
                .setFontSize(10)
                .setFont(FONT)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        // Agregar el valor del costo subtotal, pegado al fondo del bloque, sin etiqueta
        bloque.add(new Paragraph("").setHeight(25));
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
                .setHeight(70)
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
        bloque.add(new Paragraph("").setHeight(25));
        bloque.add(fila);

        t.addCell(bloque);
        return t;
    }

    private static Table bloqueMateriales(ArrayList<NewRow> materials) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(120)
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
                .setMinHeight(70) // ← mínimo, no fijo
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

        Paragraph descInfo = new Paragraph(description)
                .setFontSize(10);

        Paragraph desc = new Paragraph("");

        desc.add(descTitle);
        desc.add(descInfo);

        fila.addCell(new Cell()
                .add(desc)
                .setFont(FONT)
                .setBorder(Border.NO_BORDER))
                        .setMinHeight(70); // ← mínimo, no fijo

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
                .setHeight(70)
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
        bloque.add(new Paragraph("").setHeight(25));
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
}

*/

package PdfFormater;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class WorkBudgetPDFConverter {

    private static PdfFont FONT;

    // ======================= PUBLIC API =======================
    public static void generate(
            String clientName,
            String logisticsDescription,
            double logisticsPrice,
            double cost,
            double total
    ) throws IOException {

        initFonts();

        String outputPath = "factura" + System.currentTimeMillis() + ".pdf";
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20);

        // TABLA CONTENEDORA (CLAVE PARA 1 A4)
        Table layout = new Table(1);
        layout.setWidth(UnitValue.createPercentValue(100));
        layout.setKeepTogether(true);

        layout.addCell(bloqueCliente(clientName));
        layout.addCell(bloqueLogistica(logisticsDescription, logisticsPrice));
        layout.addCell(bloqueValor("COSTO PRESUPUESTO", cost));
        layout.addCell(bloqueValor("PRESUPUESTO FINAL", total));

        document.add(layout);
        document.close();

        abrirPDF(outputPath);
    }

    // ======================= BLOQUES =======================

    private static Cell bloqueCliente(String nombre) {
        Cell c = baseBlock();

        c.add(titulo("CLIENTE"));

        c.add(autoShrinkParagraph(nombre, 10));

        return c;
    }

    private static Cell bloqueLogistica(String desc, double price) {
        Cell c = baseBlock();

        c.add(titulo("LOGÍSTICA"));

        Table fila = new Table(new float[]{3, 2});
        fila.setWidth(UnitValue.createPercentValue(100));

        Cell descCell = new Cell().setBorder(Border.NO_BORDER);
        descCell.setProperty(Property.SPLIT_CHARACTERS, new DefaultSplitCharacters());
        descCell.add(autoShrinkParagraph("Descripción: " + desc, 10));

        fila.addCell(descCell);

        fila.addCell(new Cell()
                .add(autoShrinkParagraph("Precio: $" + format(price), 10))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT));

        c.add(fila);
        return c;
    }

    private static Cell bloqueValor(String titulo, double valor) {
        Cell c = baseBlock();

        c.add(titulo(titulo));

        c.add(autoShrinkParagraph("$" + format(valor), 12)
                .setTextAlignment(TextAlignment.CENTER));

        return c;
    }

    // ======================= HELPERS =======================

    private static Cell baseBlock() {
        return new Cell()
                .setBorder(new SolidBorder(1))
                .setPadding(8)
                .setKeepTogether(true);
    }

    private static Paragraph titulo(String text) {
        return new Paragraph(text)
                .setFont(FONT)
                .setBold()
                .setFontSize(11)
                .setMarginBottom(6);
    }

    /**
     * 🔥 Auto-shrink real: reduce tamaño si el texto es muy largo
     */
    private static Paragraph autoShrinkParagraph(String text, float baseSize) {
        float size = baseSize;

        if (text.length() > 250) size -= 2;
        if (text.length() > 500) size -= 2;
        if (text.length() > 800) size -= 2;
        if (size < 7) size = 7;

        return new Paragraph(text)
                .setFont(FONT)
                .setFontSize(size);
    }

    private static String format(double v) {
        return String.format("%.2f", v);
    }

    private static void initFonts() throws IOException {

        FontProgram arialNarrowProgram =
                FontProgramFactory.createFont("src/main/resources/fonts/arial_narrow_7.ttf");

        FONT = PdfFontFactory.createFont(
                arialNarrowProgram,
                PdfEncodings.IDENTITY_H
        );
    }

    private static void abrirPDF(String path) {
        try {
            File pdf = new File(path);
            if (pdf.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdf);
            }
        } catch (Exception ignored) {}
    }
}

