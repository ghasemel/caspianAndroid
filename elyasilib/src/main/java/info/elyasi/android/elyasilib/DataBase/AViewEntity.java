package info.elyasi.android.elyasilib.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canada on 7/29/2016.
 */
public abstract class AViewEntity {
    public abstract String getViewName();
   // public abstract String[] getTablesName();
    public abstract String[] getColumns();
    public abstract String[] getColumnsDefinition();
    public abstract String[] getJoins();

    public static final String INNER_JOIN = " inner join ";
    public static final String LEFT_JOIN = " left join ";
    public static final String GROUP_BY = " group by ";
    public static final String JOIN_ON = " on ";
    public static final String JOIN_EQUAL = " = ";
    //protected abstract String[] getColumnsDefinition();

//    CREATE VIEW mpfaktor_view AS
//    SELECT mpfaktor.*,
//    sum(spfaktor.price) AS total_price
//    FROM mpfaktor
//    INNER JOIN
//    spfaktor ON mpfaktor.id = spfaktor.mpfaktorId_FK;


    public String createViewSql() {
        StringBuilder sql = null;

        String[] cols = getColumnsDefinition();
        String[] tables = getJoins();

        if (cols != null && cols.length > 0 && tables != null && tables.length > 0) {
            sql = new StringBuilder(cols.length * 2 + tables.length + 2);

            String tmp = "create view " + getViewName() + " as SELECT ";
            sql.append(tmp);
            for (int c = 0; c < cols.length; c++) {
                sql.append(cols[c]);
                if (c < cols.length - 1) {
                    sql.append(",");
                }
            }

            sql.append(" FROM ");
            for (int t = 0; t < tables.length; t++) {
                sql.append(tables[t]);
            }
        }

        if (sql != null) {
            return sql.toString();
        }
        return null;
    }

}
