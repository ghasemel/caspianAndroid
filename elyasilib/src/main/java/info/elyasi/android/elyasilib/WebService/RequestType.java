package info.elyasi.android.elyasilib.WebService;

/**
 * Created by Canada on 3/2/2016.
 */
public class RequestType {

    public enum RType {
        POST,
        GET,
        DELETE,
        PUT_CONTENT,
        PUT_URL
    }

    private RType mType;

    public static RequestType create(RType rType) {
        return new RequestType(rType);
    }

    private RequestType(RType rType) {
        mType = rType;
    }

    public RType getType() {
        return mType;
    }

    @Override
    public String toString() {
        switch (mType) {
            case PUT_CONTENT:
            case PUT_URL:
                return "PUT";

            default:
                return mType.toString();
        }
    }
}
