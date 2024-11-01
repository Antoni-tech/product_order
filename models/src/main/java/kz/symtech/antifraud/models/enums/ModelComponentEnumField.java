package kz.symtech.antifraud.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelComponentEnumField {
    LAUNCH_SECOND_STAGE(Boolean.class),
    QUEUE_NUMBER(Integer.class),
    TO_INCIDENTS(Boolean.class),
    TEST(Boolean.class),
    CONNECTOR_OUTPUT_SDV_ID(Long.class);

    private final Class<?> type;
}
