package ir.caspiansoftware.caspianandroidapp.Report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ReportHelper {

    public static boolean alreadyGenerated(int num, Context context) {
        File pdfFile = new File(generateReportName(num, context));
        return pdfFile.exists();
    }

    private static String generateReportName(int num, Context context) {
        return context.getFilesDir().getPath() + "/pfroosh" + num + ".html";
    }

    public static final String FONT = "fonts/NotoNaskhArabic-Regular.ttf";

    // "السعر الاجمالي"
    public static final String ARABIC_TEXT = "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a";

    /*public static void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont arabicFont = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        doc.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        doc.setTextAlignment(TextAlignment.RIGHT);

        Paragraph paragraph = new Paragraph();
        paragraph.setBaseDirection(BaseDirection.RIGHT_TO_LEFT);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        paragraph.add(new Text(ARABIC_TEXT).setFont(arabicFont));
        doc.add(paragraph);

        doc.close();


    }*/
    public static void generatePDFPdfDocument(String path, Context context) {
        try {
            // Load Persian font from assets
            InputStream fontStream = context.getAssets().open(FONT);

            // Create a new PDF document
            PdfDocument pdfDocument = new PdfDocument();

            // Define a page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Get the canvas to draw on
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Set the font size and style
            paint.setTextSize(16);
            paint.setAntiAlias(true);

            // Draw Persian text
            String persianText = "السعر الاجمالي: 50.00 USD";
            canvas.drawText(persianText, 100, 100, paint);

            // Finish the page
            pdfDocument.finishPage(page);

            // Save the PDF to the file system
            File pdfFile = new File(path);
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();

            System.out.println("PDF saved to: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void generatePDF(String path, Context context) {
        try {
            // Load Persian font from assets
            InputStream fontStream = context.getAssets().open("fonts/Vazir.ttf");

            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Load the font
            PDType0Font font = PDType0Font.load(document, fontStream);

            // Shape Persian text
            String persianText = "السعر الاجمالي";
            String shapedText = shapeArabicText(persianText);

            // Add text to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(shapedText); // Add shaped Persian text
            contentStream.endText();
            contentStream.close();

            // Save the document
            File pdfFile = new File(path);
            document.save(pdfFile);
            document.close();
            System.out.println("PDF saved to: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // Method to shape Arabic/Persian text using ICU4J
    /*public static String shapeArabicText(String text) {
        try {
            ArabicShaping arabicShaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            return arabicShaping.shape(text);
        } catch (ArabicShapingException e) {
            e.printStackTrace();
            return text;
        }
    }*/

    public static String generateHtmlContent(String title, String content) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"fa\" dir=\"rtl\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>گزارش</title>\n"
                + "    <style>\n"
                + "        @page {\n"
                + "            size: A4;\n"
                + "            margin: 2cm;\n"
                + "        }\n"
                + "\n"
                + "        body {\n"
                + "            font-family: \"Tahoma\", sans-serif;\n"
                + "            margin: 0;\n"
                + "            padding: 30px;\n"
                + "            direction: rtl;\n"
                + "        }\n"
                + "\n"
                + "        .container {\n"
                + "            width: 100%;\n"
                + "            height: 100%;\n"
                + "            box-sizing: border-box;\n"
                + "        }\n"
                + "\n"
                + "        .header {\n"
                + "            text-align: center;\n"
                + "            margin-bottom: 20px;\n"
                + "        }\n"
                + "\n"
                + "        .header h1 {\n"
                + "            margin: 0;\n"
                + "            font-size: 24px;\n"
                + "        }\n"
                + "\n"
                + "        .header .fields {\n"
                + "            margin-top: 10px;\n"
                + "            text-align: right;\n"
                + "        }\n"
                + "\n"
                + "        .header .fields div {\n"
                + "            margin-bottom: 5px;\n"
                + "        }\n"
                + "\n"
                + "        .content {\n"
                + "            margin-top: 20px;\n"
                + "        }\n"
                + "\n"
                + "        .content table {\n"
                + "            width: 100%;\n"
                + "            border-collapse: collapse;\n"
                + "        }\n"
                + "\n"
                + "        .content table th, .content table td {\n"
                + "            border: 1px solid #000;\n"
                + "            padding: 8px;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "\n"
                + "        .footer {\n"
                + "            margin-top: 20px;\n"
                + "            text-align: right;\n"
                + "        }\n"
                + "\n"
                + "        .footer div {\n"
                + "            margin-bottom: 5px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <!-- Header Section -->\n"
                + "        <div class=\"header\">\n"
                + "            <h1>عنوان گزارش</h1>\n"
                + "            <div class=\"fields\">\n"
                + "                <div>نام: <span>علی محمدی</span></div>\n"
                + "                <div>تاریخ: <span>۱۴۰۳/۱۱/۰۷</span></div>\n"
                + "                <div>کد گزارش: <span>۱۲۳۴۵۶</span></div>\n"
                + "            </div>\n"
                + "        </div>\n"
                + "\n"
                + "        <!-- Content Section -->\n"
                + "        <div class=\"content\">\n"
                + "            <table>\n"
                + "                <thead>\n"
                + "                    <tr>\n"
                + "                        <th>ردیف</th>\n"
                + "                        <th>شرح</th>\n"
                + "                        <th>مقدار</th>\n"
                + "                        <th>تاریخ</th>\n"
                + "                    </tr>\n"
                + "                </thead>\n"
                + "                <tbody>\n"
                + "                    <tr>\n"
                + "                        <td>۱</td>\n"
                + "                        <td>شرح نمونه</td>\n"
                + "                        <td>۱۰۰</td>\n"
                + "                        <td>۱۴۰۳/۰۱/۰۱</td>\n"
                + "                    </tr>\n"
                + "                    <tr>\n"
                + "                        <td>۲</td>\n"
                + "                        <td>شرح نمونه ۲</td>\n"
                + "                        <td>۲۰۰</td>\n"
                + "                        <td>۱۴۰۳/۰۲/۰۲</td>\n"
                + "                    </tr>\n"
                + "                </tbody>\n"
                + "                <tfoot>\n"
                + "                    <tr>\n"
                + "                        <td colspan=\"2\">جمع کل</td>\n"
                + "                        <td colspan=\"2\">۳۰۰</td>\n"
                + "                    </tr>\n"
                + "                </tfoot>\n"
                + "            </table>\n"
                + "        </div>\n"
                + "\n"
                + "        <!-- Footer Section -->\n"
                + "        <div class=\"footer\">\n"
                + "            <div>تهیه‌کننده: <span>مریم رضایی</span></div>\n"
                + "            <div>امضاء: <span>__________________</span></div>\n"
                + "            <div>تاریخ چاپ: <span>۱۴۰۳/۱۱/۰۷</span></div>\n"
                + "        </div>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

    }

    public static File saveHtmlToFile(Context context, String htmlContent, String filePath) {
        try {
            // Define the file path
            File htmlFile = new File(filePath);

            // Write the HTML content to the file
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            outputStream.write(htmlContent.getBytes());
            outputStream.close();

            System.out.println("HTML saved to: " + htmlFile.getAbsolutePath());
            return htmlFile;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

  /*  public void openHtmlInBrowser(Context context, File htmlFile) {
        if (htmlFile != null && htmlFile.exists()) {
            // Get the URI for the file
            Uri fileUri = Uri.fromFile(htmlFile);

            // Create an intent to open the file in the browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "text/html");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Launch the browser
            context.startActivity(intent);
        } else {
            System.out.println("HTML file does not exist!");
        }
    }*/

    public static String generatePDF(int num, Context context) {
       // try {
            String filePath = generateReportName(num, context); // Path for saving the PDF file
            //generatePDF(filePath, context);
            saveHtmlToFile(context, generateHtmlContent("گزارش ماهیانه", "محتوای گزارش"), filePath);
            return filePath;
            //manipulatePdf(filePath);
            // Load the Persian font from assets

            /*try (InputStream fontStream = context.getAssets().open("fonts/BNazanin.ttf")) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fontStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] fontBytes = byteArrayOutputStream.toByteArray();

                // Create the font using the byte array
                persianFont = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            }*/


            // Create PDF writer, PDF document, and document layout
           /* try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(filePath));
                 Document document = new Document(pdfDocument)) {
                PdfFont persianFont = PdfFontFactory.createFont("assets/fonts/NotoNaskhArabic-Regular.ttf", PdfEncodings.IDENTITY_H);

                //document.setTextAlignment(TextAlignment.RIGHT);

                // === Add Header Section ===
                Paragraph header = new Paragraph( "This is auto detection: "); // "Monthly Financial Report" in Persian
                header.add(new Text("\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a").setFont(persianFont));
                header.add(new Text(": 50.00 USD"));

                document.add(header);*/
                // Add fields with values in the header
               /* Table headerTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
                headerTable.setTextAlignment(TextAlignment.RIGHT); // Enforce RTL for the table
                headerTable.addCell(new Cell().add(new Paragraph( "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("گزارش ماهانه").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("تهیه شده توسط:").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("مدیر سیستم").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("تاریخ تهیه:").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("۱۴۰۳/۱۱/۶").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

                document.add(header);
                document.add(headerTable);

                // Add a line separator after the header
                SolidLine solidLine = new SolidLine(1f); // Solid line with thickness of 1
                document.add(new LineSeparator(solidLine).setStrokeColor(ColorConstants.GRAY).setMarginTop(10).setMarginBottom(10));

                // === Add Table Section ===
                float[] columnWidths = {1, 2, 2}; // Define column widths
                Table table = new Table(columnWidths).useAllAvailableWidth();
                table.setHorizontalAlignment(HorizontalAlignment.RIGHT); // Enforce RTL for table content

                // Add table headers
                table.addHeaderCell(new Cell().add(new Paragraph("شناسه").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
                table.addHeaderCell(new Cell().add(new Paragraph("نام").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
                table.addHeaderCell(new Cell().add(new Paragraph("تاریخ").setFont(persianFont).setTextAlignment(TextAlignment.RIGHT).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));

                // Add rows to the table
                for (int i = 1; i <= 10; i++) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(i)).setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)));
                    table.addCell(new Cell().add(new Paragraph("کاربر " + i).setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)));
                    table.addCell(new Cell().add(new Paragraph("۱۴۰۳/۱۱/" + i).setFont(persianFont).setTextAlignment(TextAlignment.RIGHT)));
                }

                document.add(table); // Add the table to the document

                // Add a line separator before the footer
                document.add(new LineSeparator(solidLine).setStrokeColor(ColorConstants.GRAY).setMarginTop(10).setMarginBottom(10));

                // === Add Footer Section ===
                Paragraph footer = new Paragraph("این گزارش به صورت سیستمی تهیه شده است و نیازی به امضا ندارد.") // "This report is system-generated and does not require a signature." in Persian
                        .setFont(persianFont)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20);

                document.add(footer);*/
         /*   }

            System.out.println("PDF created at: " + filePath);*/
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
    }




    public static Intent shareOrHandlePDF(int num, Context context) {
        File pdfFile = new File(generateReportName(num, context));

        if (pdfFile.exists()) {
            // Get URI for the file
            Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

            // Create an intent
            /*Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/html");
            intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/

            // Create an intent to open the file in the browser
            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "text/html");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/

            // Show a chooser dialog
            //Intent chooser = Intent.createChooser(intent, "Choose an app to share the PDF");

            return ReportActivity.newIntent(context, generateReportName(num, context));

        } else {
            Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /*public static void openPDF(Context context, String filePath) {
        File pdfFile = new File(filePath);

        if (pdfFile.exists()) {
            // Get URI for the file
            Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

            // Create intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant temporary permission to the content URI

            // Check if an app exists to handle PDF
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
        }
    }*/
}
