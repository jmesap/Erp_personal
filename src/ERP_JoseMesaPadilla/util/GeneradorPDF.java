package ERP_JoseMesaPadilla.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.IOException;

public class GeneradorPDF {

    public static void generarPresupuestoPDF(String filePath,
            String cliente,
            String contacto,
            String direccion,
            String telefono,
            String fecha,
            String descripcion,
            double total,
            String formaPago,
            String logoPath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDRectangle mediaBox = page.getMediaBox();
            float margin = 50;
            float offset = 226.8f;

            PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font helveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font helveticaOblique = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                // ===============================
                // LOGO
                // ===============================
                ClassLoader classLoader = GeneradorPDF.class.getClassLoader();
                logoPath = classLoader.getResource("ERP_JoseMesaPadilla/imagenes/logoDocumentos.png").getPath();
                PDImageXObject logo = PDImageXObject.createFromFile(logoPath, document);
                cs.drawImage(logo, 0, 657, 351, 185);

                // ===============================
                // CABECERA A LA DERECHA
                // ===============================
                float headerX = mediaBox.getWidth() - margin - 180;
                float headerY = 705;

                cs.setFont(helveticaBold, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX, headerY);
                cs.showText("Web: ");
                cs.endText();
                cs.setFont(helvetica, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX + 30, headerY);
                cs.showText("https://www.josemesa.es/");
                cs.endText();

                headerY -= 12;
                cs.setFont(helveticaBold, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX, headerY);
                cs.showText("Email: ");
                cs.endText();
                cs.setFont(helvetica, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX + 35, headerY);
                cs.showText("info@josemesa.es");
                cs.endText();

                headerY -= 12;
                cs.setFont(helveticaBold, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX, headerY);
                cs.showText("Tlf: ");
                cs.endText();
                cs.setFont(helvetica, 10);
                cs.beginText();
                cs.newLineAtOffset(headerX + 25, headerY);
                cs.showText("644 88 43 06");
                cs.endText();

                // ===============================
                // TÍTULO PRESUPUESTO
                // ===============================
                String title = "PRESUPUESTO";
                cs.setFont(helveticaBold, 22);
                cs.setNonStrokingColor(1f, 182f / 255f, 0f);
                float titleWidth = helveticaBold.getStringWidth(title) / 1000 * 22;
                float titleX = (mediaBox.getWidth() - titleWidth) / 2;
                float currentY = mediaBox.getHeight() - margin - offset - 30;
                cs.beginText();
                cs.newLineAtOffset(titleX, currentY);
                cs.showText(title);
                cs.endText();
                cs.setNonStrokingColor(0, 0, 0);

                 // ===============================
                // DATOS DEL CLIENTE
                // ===============================
                cs.setFont(helveticaBold, 12);
                currentY -= 30;
                cs.beginText();
                cs.newLineAtOffset(margin, currentY);
                cs.showText("Cliente:");
                cs.endText();

                cs.setFont(helvetica, 12);
                cs.beginText();
                cs.newLineAtOffset(margin + 70, currentY);
                cs.showText(cliente);
                cs.endText();

                currentY -= 15;
                cs.setFont(helveticaBold, 12);
                cs.beginText();
                cs.newLineAtOffset(margin, currentY);
                cs.showText("Nombre del contacto:");
                cs.endText();

                cs.setFont(helvetica, 12);
                cs.beginText();
                cs.newLineAtOffset(margin + 130, currentY);
                cs.showText(contacto);
                cs.endText();

                currentY -= 15;
                cs.setFont(helveticaBold, 12);
                cs.beginText();
                cs.newLineAtOffset(margin, currentY);
                cs.showText("Dirección de la empresa:");
                cs.endText();

                cs.setFont(helvetica, 12);
                cs.beginText();
                cs.newLineAtOffset(margin + 160, currentY);
                cs.showText(direccion);
                cs.endText();

                currentY -= 15;
                cs.setFont(helveticaBold, 12);
                cs.beginText();
                cs.newLineAtOffset(margin, currentY);
                cs.showText("Teléfono:");
                cs.endText();

                cs.setFont(helvetica, 12);
                cs.beginText();
                cs.newLineAtOffset(margin + 70, currentY);
                cs.showText(telefono);
                cs.endText();

                // ===============================
                // TABLA: DESCRIPCIÓN Y PRECIO (Encabezado amarillo)
                // ===============================
                currentY -= 30;

                cs.setNonStrokingColor(1f, 182f / 255f, 0f);
                cs.addRect(margin, currentY - 5, mediaBox.getWidth() - 2 * margin, 20);
                cs.fill();

                cs.setNonStrokingColor(0f, 0f, 0f);
                cs.setFont(helveticaBold, 12);
                cs.beginText();
                cs.newLineAtOffset(margin + 5, currentY);
                cs.showText("Descripción del servicio");
                cs.endText();

                float priceHeaderX = mediaBox.getWidth() - margin - 100;
                cs.beginText();
                cs.newLineAtOffset(priceHeaderX, currentY);
                cs.showText("Precio");
                cs.endText();

                cs.setNonStrokingColor(0, 0, 0);

                // ===============================
                // FILA CON DATOS DEL SERVICIO
                // ===============================
                currentY -= 20;
                cs.setFont(helvetica, 12);
                cs.beginText();
                cs.newLineAtOffset(margin, currentY);
                cs.showText(descripcion);
                cs.endText();

                cs.beginText();
                cs.newLineAtOffset(priceHeaderX, currentY);
                cs.showText(total + " €");
                cs.endText();

                // ===============================
                // FILA "TOTAL" (A la derecha)
                // ===============================
                currentY -= 20;
                cs.setFont(helveticaBold, 12);
                float totalLabelWidth = helveticaBold.getStringWidth("Total:") / 1000 * 12;
                float totalX = priceHeaderX - totalLabelWidth - 10;
                cs.beginText();
                cs.newLineAtOffset(totalX, currentY);
                cs.showText("Total:");
                cs.endText();

                cs.beginText();
                cs.newLineAtOffset(priceHeaderX, currentY);
                cs.showText(total + " €");
                cs.endText();

                // ===============================
                // FORMA DE PAGO (Centrado y en cursiva)
                // ===============================
                currentY -= 40;
                cs.setFont(helveticaOblique, 14);
                float formaPagoWidth = helveticaOblique.getStringWidth(formaPago) / 1000 * 14;
                float formaPagoX = (mediaBox.getWidth() - formaPagoWidth) / 2;
                cs.beginText();
                cs.newLineAtOffset(formaPagoX, currentY);
                cs.showText(formaPago);
                cs.endText();

                // ===============================
                // MENSAJE DE AGRADECIMIENTO (Centrado al final)
                // ===============================
                float footerY = margin + 30; // Para ubicarlo en la parte inferior
                String mensaje = "Gracias por confiar en nuestra empresa. Contáctenos para más información.";
                float mensajeWidth = helveticaBold.getStringWidth(mensaje) / 1000 * 12;
                float mensajeX = (mediaBox.getWidth() - mensajeWidth) / 2;

                cs.setFont(helveticaBold, 12);
                cs.beginText();
                cs.newLineAtOffset(mensajeX, footerY);
                cs.showText(mensaje);
                cs.endText();
            }

            document.save(filePath);
            System.out.println("PDF generado: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
