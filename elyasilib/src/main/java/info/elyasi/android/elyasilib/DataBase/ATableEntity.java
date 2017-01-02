package info.elyasi.android.elyasilib.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canada on 1/18/2016.
 */
public abstract class ATableEntity {
    public abstract String getTableName();
    public abstract String[] getColumns();
    public abstract String[] getPKColumns();
    protected abstract String[] getColumnsDefinition();

    private List<String> mIndexes;

    protected List<String> getIndexes() {
        return mIndexes;
    }

    protected void addIndex(String indexName, boolean isUnique, String... columns) {
        if (mIndexes == null) {
            mIndexes = new ArrayList<>();
        }

        mIndexes.add(createIndex(getTableName(), indexName, isUnique, columns));
    }


    private static String createIndex(String tableName, String indexName, boolean isUnique, String... columns) {
        String sql = " create " + (isUnique ? "unique" : "") +
                " index " + indexName + " ON " + tableName + "(";

        for (int c = 0; c < columns.length; c++) {
            if (c > 0) sql += ",";
            sql += columns[c];
        }

        return sql + ");";
    }

    public String createTableSql() {
        StringBuilder sql = null;

        String[] cols = getColumnsDefinition();
        if (cols != null && cols.length > 0) {
            sql = new StringBuilder(cols.length * 2 + 1);
            sql.append("create table " + getTableName() + "(");
            for (int c = 0; c < cols.length; c++) {
                sql.append(cols[c]);
                if (c < cols.length - 1) {
                    sql.append(",");
                } else {
                    sql.append(");");
                }
            }
        }

        if (sql != null) {
            return sql.toString();
        }
        return null;
    }

    public String createIndexesSql() {
        if (mIndexes != null) {
            StringBuilder sql = new StringBuilder(mIndexes.size());
            for (String index : mIndexes) {
                sql.append(index);
            }
            return sql.toString();
        }
        //throw new Exception("There is not any index. don't call createIndexesSql() function");
        return null;
    }
}
