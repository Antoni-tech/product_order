package kz.symtech.antifraud.gatewayservice.helpers;

import kz.symtech.antifraud.gatewayservice.dto.PrivilegesResponseDTO;
import kz.symtech.antifraud.gatewayservice.dto.error.CatalogOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Данный класс представялет собой простой контроль доступа
 * к эндпоинтам системы, основываясь на наличие у пользователя необходимой привилегии
 * для доступа к определенному функционалу
 */
@Component
@Slf4j
public class SimpleACL {
    private final Map<String, Long> pathToPrivilegeMapping;

    public SimpleACL() {
        pathToPrivilegeMapping = new HashMap<>();

        // User domain mappings
        pathToPrivilegeMapping.put("admin/create_user", 1L);
        pathToPrivilegeMapping.put("admin/change_user", 1L);
        pathToPrivilegeMapping.put("admin/user", 2L);
        pathToPrivilegeMapping.put("admin/users_list", 2L);

        // Services domain mappings
        pathToPrivilegeMapping.put("catalog/create_service", 3L);
        pathToPrivilegeMapping.put("catalog/change_service", 3L);
        pathToPrivilegeMapping.put("catalog/service_status", 3L);
        pathToPrivilegeMapping.put("catalog/service", 4L);
        pathToPrivilegeMapping.put("catalog/services_list", 4L);
        pathToPrivilegeMapping.put("catalog/service_check", 4L);

        // Tariff domain mappings
        pathToPrivilegeMapping.put("catalog/create_tariff", 5L);
        pathToPrivilegeMapping.put("catalog/change_tariff", 5L);
        pathToPrivilegeMapping.put("catalog/tariff", 6L);
        pathToPrivilegeMapping.put("catalog/tariff_list", 6L);
        pathToPrivilegeMapping.put("catalog/tariff_check", 6L);

        // Client domain mappings
        pathToPrivilegeMapping.put("client/connect_tariff", 7L);
        pathToPrivilegeMapping.put("client/client_write_off", 7L);
        pathToPrivilegeMapping.put("client/write_off", 7L);
        pathToPrivilegeMapping.put("client/client_list", 8L);
        pathToPrivilegeMapping.put("client/disconnect_tariff", 9L);

        // W4 operations domain mapping
        pathToPrivilegeMapping.put("w4/product_loading", 10L);
        pathToPrivilegeMapping.put("w4/service_loading", 11L);
        pathToPrivilegeMapping.put("w4/tariff_create", 12L);
        pathToPrivilegeMapping.put("w4/update_numeric_values", 13L);
        pathToPrivilegeMapping.put("w4/tariff_status_change", 14L);
        pathToPrivilegeMapping.put("w4/tariff", 15L);
        pathToPrivilegeMapping.put("w4/tariff_list", 15L);
        pathToPrivilegeMapping.put("w4/tariff_send", 16L);
    }


    /**
     * Данный метод проверяет наличие необходимых привилегий
     * у пользователя, осуществляющего запрос
     * @param privileges
     * @param path
     * @return
     * @throws CatalogOperationException
     */
    public boolean canAccess( Set<PrivilegesResponseDTO.PrivilegesDataDTO> privileges, String path) throws CatalogOperationException {
        try{
            List<String> filteredPathMappings = pathToPrivilegeMapping.keySet().stream().filter(path::contains).toList();
            if (filteredPathMappings.isEmpty()) {
                return true;
            }
            String filteredPathMapping = filteredPathMappings.get(0);
            Set<PrivilegesResponseDTO.PrivilegesDataDTO> filteredPrivileges = privileges.stream().filter(privilegesDataDTO -> Objects.equals(privilegesDataDTO.getId(), pathToPrivilegeMapping.get(filteredPathMapping))).collect(Collectors.toSet());
            return !filteredPrivileges.isEmpty();
        }catch (RuntimeException ex) {
            log.error(ex.getMessage());
            throw CatalogOperationExceptionGenerator.generateAuthFailException();
        }
    }
}
