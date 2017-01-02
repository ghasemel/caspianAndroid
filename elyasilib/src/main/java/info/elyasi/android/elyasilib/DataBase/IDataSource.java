package info.elyasi.android.elyasilib.DataBase;

import java.util.List;

/**
 * Created by Canada on 1/18/2016.
 */
public interface IDataSource<T> {

    String[] getAllColumns();
    void open();
    void close();
    int delete(T TObject);
    int insert(T TObject);
    int update(T TObject) throws Exception;

    List<T> getAll();
    T getById(int id);

}
