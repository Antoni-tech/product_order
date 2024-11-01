package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import kz.symtech.antifraud.coreservice.exceptions.ApplicationException;
import kz.symtech.antifraud.coreservice.exceptions.SummaryDataVersionNotFoundException;
import kz.symtech.antifraud.models.enums.Fields;
import kz.symtech.antifraud.coreservice.entities.models.ModelComponentElements;
import kz.symtech.antifraud.coreservice.entities.models.ModelStructComponents;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static kz.symtech.antifraud.models.utils.ObjectMapperUtils.objectMapper;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectorUtilService {
    private final ConversionService conversionService;
    private final ApplicationContext applicationContext;

    private Date convertToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    public <T> Object convert(Object object, Class<T> targetType) {
        if (targetType.equals(Date.class) && object instanceof String) {
            return convertToDate((String) object);
        } else if (targetType.equals(String.class) && object instanceof List) {
            return ObjectMapperUtils.getJson(object);
        } else {
            TypeDescriptor sourceType = TypeDescriptor.forObject(object);
            TypeDescriptor target = TypeDescriptor.valueOf(targetType);

            if (conversionService.canConvert(sourceType, target)) {
                return conversionService.convert(object, sourceType, target);
            } else {
                throw new ApplicationException(String.format("Conversion not supported at %s to %s", sourceType, target), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void getDeclaredFields(Object destinationObj, Map<String, Object> targetMap) {
        Field[] fields = destinationObj.getClass().getDeclaredFields();

        for (Field field : fields) {
            Object value = targetMap.get(field.getName());
            if (value != null) {
                try {
                    field.setAccessible(true);

                    if (field.getType().isEnum() && field.getType().getSimpleName().equals("type")) {
                        field.set(destinationObj, SummaryFieldType.valueOfIgnoreCase(value.toString()));
                    } else {
                        field.set(destinationObj, convert(value, field.getType()));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    public Object findBySummaryDataVersionIdWithReflection(SummaryDataType type, Long summaryDataVersionId) {
        JpaRepository<?, Long> repository = applicationContext.getBean(type.getRepository());

        try {
            Method findBySummaryDataVersionIdMethod;

            if (type.equals(SummaryDataType.DATA_STRUCTURE)) {
                findBySummaryDataVersionIdMethod = findMethod(repository.getClass(), Fields.FIND_BY_ID_FIELD, Object.class);
            } else {
                findBySummaryDataVersionIdMethod = findMethod(repository.getClass(), Fields.FIND_BY_SUMMARY_DATA_VERSION_ID_FIELD, Long.class);
            }

            findBySummaryDataVersionIdMethod.setAccessible(true);
            Optional<?> result = (Optional<?>) findBySummaryDataVersionIdMethod.invoke(repository, summaryDataVersionId);

            if (result.isPresent()) {
                return result.get();
            }
            throw new SummaryDataVersionNotFoundException(summaryDataVersionId);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?> currentClass = clazz;
        do {
            try {
                return currentClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        } while (currentClass != null);//recursively search for findById method from superclasses

        throw new NoSuchMethodException("Method not found: " + methodName);
    }

    public SummaryDataVersion getSummaryDataVersionFromReflection(SummaryDataType type, Long summaryDataVersionId) {
        Object object = findBySummaryDataVersionIdWithReflection(type, summaryDataVersionId);
        return getSummaryDataVersionFromReflection(object);
    }

    public SummaryDataVersion getSummaryDataVersionFromReflection(Object object) {
        try {
            Method getSummaryDataVersionMethod = object.getClass().getMethod(Fields.GET_SUMMARY_DATA_VERSION_FIELD);
            return (SummaryDataVersion) getSummaryDataVersionMethod.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponentElement(ModelStructComponents modelStructComponents, ModelComponentEnumField enumField) {
        ModelComponentElements modelComponentElement = modelStructComponents.getModelComponentElements().stream()
                .filter(v -> v.getEnumField().equals(enumField))
                .findAny()
                .orElse(null);
        if (Objects.nonNull(modelComponentElement)) {
            Class<?> fieldType = modelComponentElement.getEnumField().getType();
            return objectMapper.convertValue(modelComponentElement.getValue(), (Class<T>) fieldType);
        }
        return null;
    }
}
