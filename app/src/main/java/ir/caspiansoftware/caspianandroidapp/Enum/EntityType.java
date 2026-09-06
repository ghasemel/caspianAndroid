package ir.caspiansoftware.caspianandroidapp.Enum;

import lombok.Getter;

public enum EntityType {
    PFAKTOR("pfaktor"),
    MALI("mali");

    @Getter
    private final String value;

    EntityType(String value) {
        this.value = value;
    }
}
