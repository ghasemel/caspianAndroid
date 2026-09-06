package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import java.util.List;

public interface TransferToServerService<T> {

    void sendToServer(List<T> dataList) throws Exception;
}
