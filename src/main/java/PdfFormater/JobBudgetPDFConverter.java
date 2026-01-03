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
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;

import java.io.IOException;
import java.util.Random;

public class JobBudgetPDFConverter {
    private static int counter;
    private static final Random Random = new Random();
    private static PdfFont FONT;
/*    private static PdfFont FONT_BOLD;*/

    private static void initFonts() throws IOException {

        FontProgram arialNarrowProgram =
                FontProgramFactory.createFont("src/main/resources/fonts/arial_narrow_7.ttf");

/*        FontProgram arialNarrowBoldProgram =
                FontProgramFactory.createFont("src/main/resources/fonts/ArialNarrow-Bold.ttf");*/

        FONT = PdfFontFactory.createFont(
                arialNarrowProgram,
                PdfEncodings.IDENTITY_H
        );

/*        PdfFont arialNarrowBold = PdfFontFactory.createFont(
                arialNarrowBoldProgram,
                PdfEncodings.IDENTITY_H
        );*/
    }

    public static void generarFactura() throws IOException {
        initFonts();
        counter = Random.nextInt(1000, 9999); // Generar un número aleatorio para el nombre del archivo
        PdfWriter writer = new PdfWriter("factura" + counter + ".pdf");
        counter++;
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
        doc.add(seccion("MATERIALES", 120).setFont(FONT));
        doc.add(espacio());

        // ================= LOGÍSTICA =================
        doc.add(seccion("LOGÍSTICA", 80).setFont(FONT));
        doc.add(espacio());

        // ================= COLOCACIÓN =================
        doc.add(bloqueColocacion());
        doc.add(espacio());

        // ================= PAGOS =================
        doc.add(bloquePagos());
        doc.add(espacio());

        // ================= COSTO PRESUPUESTO =================
        doc.add(seccion("COSTO DE PRESUPUESTO", 50).setFont(FONT));
        doc.add(espacio());

        // ================= PRESUPUESTO FINAL =================
        doc.add(seccion("PRESUPUESTO FINAL", 70).setFont(FONT));

        doc.close();
    }

    // ================= UTILIDADES =================

    private static Paragraph label(String text) {
        return new Paragraph(text).setFontSize(10).setBold();
    }

    private static Paragraph value(String text) {
        return new Paragraph(text).setFontSize(10);
    }

    private static Table bloquePagos() {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(70)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        // Tabla interna para Seña / Saldo en horizontal
        Table fila = new Table(new float[]{1, 1});
        fila.setWidth(UnitValue.createPercentValue(100));

        fila.addCell(new Cell()
                .add(new Paragraph("Seña:")
                        .setBold()
                        .setFontSize(10))
                .setFont(FONT)
                .setBorder(Border.NO_BORDER));

        fila.addCell(new Cell()
                .add(new Paragraph("Saldo:")
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

    private static LineSeparator separador() {
        return new LineSeparator(new SolidLine());
    }

    private static Table seccion(String titulo, float altura) {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(altura)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        bloque.add(new Paragraph(titulo)
                .setBold()
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(4));

        t.addCell(bloque);
        return t;
    }

    private static Paragraph espacio() {
        return new Paragraph("").setMarginBottom(10);
    }

    private static Cell celdaLibre(float altura) {
        return new Cell()
                .setHeight(altura)
                .setBorder(new SolidBorder(1));
    }

    private static Cell celdaSeccion(String titulo) {
        return new Cell()
                .add(new Paragraph(titulo).setBold())
                .setBorder(new SolidBorder(1));
    }

    private static Table bloqueColocacion() {
        Table t = new Table(1);
        t.setWidth(UnitValue.createPercentValue(100));

        Cell bloque = new Cell()
                .setHeight(80)
                .setBorder(new SolidBorder(1))
                .setPadding(6);

        // Título
        bloque.add(new Paragraph("COLOCACIÓN")
                .setFont(FONT)
                .setBold()
                .setFontSize(10)
                .setMarginBottom(6));

        // Fila horizontal: Colocador / Precio
        Table fila = new Table(new float[]{1, 1});
        fila.setWidth(UnitValue.createPercentValue(100));

        fila.addCell(new Cell()
                .add(new Paragraph("Colocador:")
                        .setFont(FONT)
                        .setBold()
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        fila.addCell(new Cell()
                .add(new Paragraph("Precio:")
                        .setFont(FONT)
                        .setBold()
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Empujar hacia abajo dentro del bloque
        bloque.add(new Paragraph("").setHeight(18));
        bloque.add(fila);

        t.addCell(bloque);
        return t;
    }
}

