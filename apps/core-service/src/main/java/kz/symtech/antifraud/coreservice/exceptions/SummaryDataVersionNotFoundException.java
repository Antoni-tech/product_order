package kz.symtech.antifraud.coreservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@Getter
@AllArgsConstructor
public class SummaryDataVersionNotFoundException extends RuntimeException{
    private Long versionId;
}
