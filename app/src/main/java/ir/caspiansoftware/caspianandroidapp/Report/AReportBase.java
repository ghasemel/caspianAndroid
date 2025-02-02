package ir.caspiansoftware.caspianandroidapp.Report;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import ir.caspiansoftware.caspianandroidapp.Vars;

public abstract class AReportBase {

    private final Context context;
    protected static final String ROW_DELIMITER = "=";

    protected abstract String getReportFileName();
    protected abstract String getReportTitle();

    protected abstract List<String> getTableColumnsName();

    protected abstract List<String> generateTableData(int dataId);
    protected abstract List<String> getReportHeaderLabels();
    protected abstract List<String> getReportHeaderValues();

    protected abstract List<String> getTableSummaryLabels();
    protected abstract List<String> getTableSummaryValues();

    public AReportBase(Context context) {
        this.context = context;
    }

    private boolean alreadyGenerated(int num) {
        File pdfFile = new File(generateReportName(num));
        return pdfFile.exists();
    }

    private String generateReportName(int dataId) {
        return context.getFilesDir().getPath() + "/" + getReportFileName() + dataId + ".html";
    }

    private String generateHtmlContent(String title, List<String> columns, List<String> tableRows) {
        return generateHtmlHeader(title)
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <!-- Header Section -->\n"
                + "        <div class=\"header\">\n"
                + "            <h1>" + Vars.YEAR.getCompany() + "</h1>\n"
                + "            <h2>" + title + "</h2>\n"
                + "            <div class=\"fields\">\n"
                +                   generateReportHeader(getReportHeaderLabels(), getReportHeaderValues())
                + "            </div>\n"
                + "        </div>\n"
                + "        <!-- Content Section -->\n"
                + "        <div class=\"content\">\n"
                + "            <table>\n"
                + "                <thead>\n"
                + "                    <tr>\n"
                +                           generateTableColumns(columns)
                + "                    </tr>\n"
                + "                </thead>\n"
                + "                <tbody>\n"
                +                           generateTableRows(tableRows)
                + "                </tbody>\n"
                + "                <tfoot>\n"
                +                       generateTableSummary(getTableSummaryLabels(), getTableSummaryValues())
                + "                </tfoot>\n"
                + "            </table>\n"
                + "        </div>\n"
                + "\n"
                +          generateFooter()
                + "    </div>\n"
                + "</body>\n"
                + "</html>";
    }


    private String generateTableSummary(List<String> labels, List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < labels.size() && i < values.size(); i++) {
            sb
                    .append("                    <tr>\n")
                    .append("                        <td colspan=\"2\">").append(labels.get(i)).append("</td>\n")
                    .append("                        <td colspan=\"2\">").append(PersianConvert.ConvertDigitsToPersian(values.get(i))).append("</td>\n")
                    .append("                    </tr>\n");
        }
        return sb.toString();
    }

    private String generateReportHeader(List<String> labels, List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < labels.size() && i < values.size(); i++) {
            sb.append("                ")
                    .append("<div>")
                    .append(labels.get(i))
                    .append(": <span>")
                    .append(PersianConvert.ConvertDigitsToPersian(values.get(i)))
                    .append("</span></div>\n");
        }
        return sb.toString();
    }

    private String generateTableRows(List<String> tableRows) {
        StringBuilder sb = new StringBuilder();
        tableRows.forEach(row -> {
            if (row == null || row.trim().isEmpty())
                return;

            String[] values = row.split(ROW_DELIMITER);
            if (values.length == 0)
                return;

            sb.append("                    <tr>\n");
            Arrays.stream(values).forEach(v -> {
                sb.append("                        ")
                        .append("<td>")
                        .append(PersianConvert.ConvertNumbersToPersian(v))
                        .append("</td>\n");
            });

            sb.append("                    </tr>\n");
        });
        return sb.toString();
    }

    private String generateTableColumns(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        columns.forEach(col -> {
            sb.append("                        <th>").append(col).append("</th>\n");
        });
        return sb.toString();
    }

    private String generateFooter() {
        return "<!-- Footer Section -->\n"
                + "        <div class=\"footer\">\n"
                + "            <div>کد بازاریاب: <span>" + PersianConvert.ConvertDigitsToPersian(String.valueOf(Vars.USER.getVisitorCode())) + "</span></div>\n"
                + "            <div>آدرس: <span>__________________</span></div>\n"
                + "            <div>تاریخ چاپ: <span>" + PersianConvert.ConvertDigitsToPersian(PersianDate.getFullNow()) + "</span></div>\n"
                + "        </div>\n";

    }

    private String generateHtmlHeader(String title) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"fa\" dir=\"rtl\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>" + title + "</title>\n"
                + "    <style>\n"
                + "        @page {\n"
                + "            size: 300mm auto;\n"
                + "            margin: 0;\n"
                + "        }\n"
                + "\n"
                + "        body {\n"
                + "            font-family: \"MONOSPACE\", sans-serif;\n"
                + "            margin: 0;\n"
                + "            padding: 10px;\n"
                + "            direction: rtl;\n"
                + "            font-size: 32px;\n"
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
                + "            margin-bottom: 10px;\n"
                + "        }\n"
                + "\n"
                + "        .header h1 {\n"
                + "            margin: 0;\n"
                + "            font-size: 42px;\n"
                + "        }\n"
                + "\n"
                + "        .header h2 {\n"
                + "            margin: 0;\n"
                + "            font-size: 38px;\n"
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
                + "            page-break-before: avoid;\n"
                + "            page-break-after: avoid;\n"
                + "            page-break-inside: avoid;\n"
                + "            display: block;\n"
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
                + "            page-break-before: avoid;\n"
                + "            page-break-after: avoid;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n";
    }

    private File saveHtmlToFile(String htmlContent, String filePath) {
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

    public String generateReport(int dataId) {
        // try {
        String filePath = generateReportName(dataId); // Path for saving the PDF file
        List<String> tableData = generateTableData(dataId);
        saveHtmlToFile(generateHtmlContent(getReportTitle(), getTableColumnsName(), tableData), filePath);
        return filePath;
    }

    protected Intent shareOrHandlePDF(int num) {
        File pdfFile = new File(generateReportName(num));

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

            return ReportActivity.newIntent(context, generateReportName(num));

        } else {
            Toast.makeText(context, "PDF file not found", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}

