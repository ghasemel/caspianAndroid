package ir.caspiansoftware.caspianandroidapp.Enum;

import lombok.Getter;
import lombok.Setter;

public enum MaliType {
    SANDOGH("sandogh"),
    VCHECK("vcheck"),
    PAY("pay");

    @Getter
    private final String value;

    MaliType(String value) {
        this.value = value;
    }
}
