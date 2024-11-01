package kz.symtech.antifraud.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.Assert;

import java.util.Objects;

public class AssertUtils {

    private static final ObjectMapper prettyPrintObjectMapper = new ObjectMapper();

    static {
        prettyPrintObjectMapper.enable(
                SerializationFeature.INDENT_OUTPUT,
                SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS
        );
    }

    /**
     * Утвержает логическое равенство объектов. <br/>
     * В случае неравенства преобразует объекты в строковое представление,
     * удобное для визуального сравнения, и выполняет для этих представлений
     * {@link Assert#assertEquals}.
     */
    @SneakyThrows
    public static void assertEqualsWithPrettyPrint(Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            return;
        }
        Assert.assertEquals(
                prettyPrintObjectMapper.writeValueAsString(expected),
                prettyPrintObjectMapper.writeValueAsString(actual)
        );
    }

}
