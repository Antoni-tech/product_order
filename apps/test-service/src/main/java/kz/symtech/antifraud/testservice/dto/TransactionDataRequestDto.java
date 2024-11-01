package kz.symtech.antifraud.testservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import kz.symtech.antifraud.testservice.annotations.ValidDelay;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
@ValidDelay
public class TransactionDataRequestDto {
    private Long id;

    private UUID summaryDataModelId;
    private UUID summaryDataConnectorId;
    private Long userId;
    private int numberOfRepetitions;
    private Boolean isCyclical;

    private int delay;
    @NotNull(message = "must not be null")
    private ChronoUnit chronoUnit;
    @NotNull(message = "must not be null")
    private JsonNode data;

    private int delayToReplace;
    private ChronoUnit chronoUnitToReplace;
    private JsonNode dataToReplace;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @FutureOrPresent(message = "Expiration date must be greater than current")
    private ZonedDateTime expiresAt;

}
