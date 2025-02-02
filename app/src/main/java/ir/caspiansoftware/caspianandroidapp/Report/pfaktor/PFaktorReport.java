package ir.caspiansoftware.caspianandroidapp.Report.pfaktor;

import android.content.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Report.AReportBase;

public class PFaktorReport extends AReportBase {

    private final PFaktorBLL pFaktorBLL;
    private List<String> reportHeaderValues = new ArrayList<>();
    private List<String> tableSummaryValues = new ArrayList<>();

    public PFaktorReport(Context context) {
        super(context);
        pFaktorBLL = new PFaktorBLL(context);
    }

    @Override
    protected String getReportFileName() {
        return "pfaktor";
    }

    @Override
    protected String getReportTitle() {
        return "پیش فاکتور";
    }

    @Override
    protected List<String> getTableColumnsName() {
        return new ArrayList<>(List.of(
                "نام کالا",
                "مقدار",
                "قیمت واحد",
                "قیمت کل"
        ));
    }

    @Override
    protected List<String> getReportHeaderLabels() {
        return List.of("شماره پیش فاکتور", "تاریخ پیش فاکتور", "نام خریدار");
    }

    @Override
    protected List<String> getReportHeaderValues() {
        return reportHeaderValues;
    }

    @Override
    protected List<String> getTableSummaryLabels() {
        return List.of("جمع کل");
    }

    @Override
    protected List<String> getTableSummaryValues() {
        return tableSummaryValues;
    }

    @Override
    protected List<String> generateTableData(int faktorId) {
        ArrayList<SPFaktorModel> dataRow = pFaktorBLL.getSPfaktorListByMPFaktorId(faktorId);
        List<String> tableData = new ArrayList<>();
        dataRow.forEach(spFaktorModel -> tableData.add(convertSPFaktorToCsvData(spFaktorModel)));

        createReportHeaderValues(faktorId);
        createTableSummaryValues(dataRow);
        return tableData;
    }

    private void createTableSummaryValues(ArrayList<SPFaktorModel> dataRow) {
        BigDecimal totalPrice = dataRow.stream()
                .map(SPFaktorModel::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        tableSummaryValues = List.of(NumberExt.DigitSeparator(totalPrice));
    }

    private void createReportHeaderValues(int faktorId) {
        MPFaktorModel mpfaktor = pFaktorBLL.getMPfaktorById(faktorId);
        if (mpfaktor != null) {
            reportHeaderValues = List.of(String.valueOf(mpfaktor.getNum()), mpfaktor.getDate(), mpfaktor.getPersonModel().getName());
        }
    }

    private String convertSPFaktorToCsvData(SPFaktorModel spfaktor) {
        return spfaktor.getKalaModel().getName() +
                ROW_DELIMITER +
                spfaktor.getSCount() +
                ROW_DELIMITER +
                spfaktor.getPrice() +
                ROW_DELIMITER +
                spfaktor.getTotalPrice();
    }

}
