package kz.symtech.antifraud.models.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SummaryFieldType {
    OBJECT,
    ARRAY_OBJECT,
    INTEGER,
    STRING,
    DOUBLE;

    public static SummaryFieldType valueOfIgnoreCase(String value) {
        Optional<SummaryFieldType> fieldType = Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(value)).findFirst();
        if (fieldType.isPresent()) {
            return fieldType.get();
        }
        throw new IllegalArgumentException("No enum constant " + SummaryFieldType.class + "." + value);
    }
}
