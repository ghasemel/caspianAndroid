package ir.caspiansoftware.caspianandroidapp.Report;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ir.caspiansoftware.caspianandroidapp.Setting;

public class ReportHelper {

    public static boolean alreadyGenerated(int num, Context context) {
        File pdfFile = new File(generateReportName(num, context));
        return pdfFile.exists();
    }

    private static String generateReportName(int num, Context context) {
        return context.getFilesDir().getPath() + "/pfroosh" + num + ".pdf";
    }

    public static void generatePDFWithTable(int num, Context context) {
        try {
            String filePath = generateReportName(num, context);
            // Create a PDF writer
            PdfWriter writer = new PdfWriter(new FileOutputStream(new File(filePath)));

            // Create a PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);

            // Create a document for layout
            Document document = new Document(pdfDocument);

            // Create a table with 3 columns
            float[] columnWidths = {1, 2, 2}; // Widths for each column
            Table table = new Table(columnWidths);

            // Add table header
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Name")));
            table.addHeaderCell(new Cell().add(new Paragraph("Date")));

            // Add rows to the table
            for (int i = 1; i <= 10; i++) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(i))));
                table.addCell(new Cell().add(new Paragraph("User " + i)));
                table.addCell(new Cell().add(new Paragraph("2025-01-" + i)));
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("PDF created at: " + filePath);
            openPDF(context, filePath); // Open the PDF
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareOrHandlePDF(int num, Context context) {
        File pdfFile = new File(generateReportName(num, context));

        if (pdfFile.exists()) {
            // Get URI for the file
            Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

            // Create an intent
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Show a chooser dialog
            Intent chooser = Intent.createChooser(intent, "Choose an app to share the PDF");
            context.startActivity(chooser);

        } else {
            Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openPDF(Context context, String filePath) {
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
    }
}
