package kz.symtech.antifraud.models.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseStatusException extends org.springframework.web.server.ResponseStatusException {

    private final Map<String, Object> attributes = new HashMap<>();
    private final String code;

    public ResponseStatusException(int rawStatusCode, String code, String reason) {
        super(rawStatusCode, reason, null);
        this.code = code;
    }

    public ResponseStatusException(HttpStatus status, String code, String reason) {
        super(status, reason);
        this.code = code;
    }

    public ResponseStatusException(HttpStatus status, String code, String reason, Throwable cause) {
        super(status, reason, cause);
        this.code = code;
    }

    public ResponseStatusException(int rawStatusCode, String code, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
        this.code = code;
    }

    public ResponseStatusException(int rawStatusCode, String code, String reason, Map<String, Object> attributes) {
        super(rawStatusCode, reason, null);
        this.code = code;
        this.attributes.putAll(attributes);
    }

    public ResponseStatusException(HttpStatus status, String code, String reason, Map<String, Object> attributes) {
        super(status, reason);
        this.code = code;
        this.attributes.putAll(attributes);
    }

    public ResponseStatusException(HttpStatus status, String code, String reason, Map<String, Object> attributes, Throwable cause) {
        super(status, reason, cause);
        this.code = code;
        this.attributes.putAll(attributes);
    }

    public ResponseStatusException(int rawStatusCode, String code, String reason, Map<String, Object> attributes, Throwable cause) {
        super(rawStatusCode, reason, cause);
        this.code = code;
        this.attributes.putAll(attributes);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "HTTP: code=" + getCode() + "; message=" + super.getReason();
    }
}
