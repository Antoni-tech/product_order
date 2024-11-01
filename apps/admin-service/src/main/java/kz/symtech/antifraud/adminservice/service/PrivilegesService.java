package kz.symtech.antifraud.adminservice.service;

import kz.symtech.antifraud.adminservice.entities.privileges.Privileges;
import kz.symtech.antifraud.adminservice.repository.privileges.PrivilegesRepository;
import kz.symtech.antifraud.models.dto.PrivilegesResponseDTO;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationExceptionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Данный класс предоставляет фукнционал обработки
 * запросов связанных с привилегиями пользователя в
 * системе ТФ
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrivilegesService {
    private final PrivilegesRepository privilegesRepository;

    public PrivilegesResponseDTO getPrivileges(List<Long> privileges) throws CatalogOperationException {
            List<Privileges> privilegesList = new ArrayList<>();

            if (privileges != null) {
                privilegesList = privilegesRepository.findByIdIn(privileges);
            } else {
                privilegesRepository.findAll().forEach(privilegesList::add);
            }

            if (privilegesList.isEmpty()) {
                throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
            }
            return PrivilegesResponseDTO.builder()
                    .privilegesDataDTOS(privilegesList.stream()
                            .map(privilege -> PrivilegesResponseDTO.PrivilegesDataDTO.builder()
                                    .id(privilege.getId())
                                    .name(privilege.getName())
                                    .privilegeGroup(privilege.getPrivilegeGroup())
                                    .tag(privilege.getTag())
                                    .build())
                            .collect(Collectors.toSet()))
                    .build();
    }
}
