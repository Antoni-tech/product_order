package kz.symtech.antifraud.authservice.utils;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * Данный класс предоставляет функционал декодирования из BASE64
 * в массив байт
 */
@Component
public class Base64Util {
    public byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
