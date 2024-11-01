package kz.symtech.antifraud.adminservice.service;

import kz.symtech.antifraud.adminservice.dto.roles.ChangeRoleRequestDTO;
import kz.symtech.antifraud.adminservice.dto.roles.CreateRoleRequestDTO;
import kz.symtech.antifraud.adminservice.dto.roles.CreateRoleResponseDTO;
import kz.symtech.antifraud.adminservice.entities.privileges.Privileges;
import kz.symtech.antifraud.adminservice.entities.privileges.RolePrivilege;
import kz.symtech.antifraud.adminservice.entities.roles.Roles;
import kz.symtech.antifraud.adminservice.repository.privileges.PrivilegesRepository;
import kz.symtech.antifraud.adminservice.repository.privileges.RolePrivilegeRepository;
import kz.symtech.antifraud.adminservice.repository.roles.RolesRepository;
import kz.symtech.antifraud.models.dto.PrivilegesResponseDTO;
import kz.symtech.antifraud.models.dto.RoleResponseDTO;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationExceptionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RolesRepository rolesRepository;
    private final PrivilegesRepository privilegesRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Transactional
    public CreateRoleResponseDTO createRole(CreateRoleRequestDTO requestDTO) throws CatalogOperationException {
        Roles role = rolesRepository.save(Roles.builder()
                .name(requestDTO.getName())
                .createdDate(Date.from(Instant.now()))
                .updateDate(Date.from(Instant.now()))
                .build());
        updateOrCreatePrivilegesForRole(requestDTO.getPrivileges(), role);
        return CreateRoleResponseDTO.builder()
                .id(role.getId())
                .build();
    }

    @Transactional
    public void changeRole(ChangeRoleRequestDTO requestDTO) throws CatalogOperationException {
        Optional<Roles> responseRole = rolesRepository.findById(requestDTO.getId());
        if (responseRole.isPresent()) {
            Roles role = responseRole.get();
            updateOrCreatePrivilegesForRole(requestDTO.getPrivileges(), role);
            role.setUpdateDate(Date.from(Instant.now()));
            role.setName(requestDTO.getName());
            rolesRepository.save(role);
        }
    }

    @Transactional
    public void deleteRole(Long roleId) {
        Optional<Roles> responseRole = rolesRepository.findById(roleId);
        if (responseRole.isPresent()) {
            Roles role = responseRole.get();
            List<RolePrivilege> idsToDelete = rolePrivilegeRepository.findAllByRoleId(role.getId());
            rolePrivilegeRepository.deleteAllById(idsToDelete.stream().map(BaseEntity::getId).collect(Collectors.toList()));
            rolesRepository.delete(role);
        }
    }

    private void updateOrCreatePrivilegesForRole(List<Long> privilegesId, Roles role) throws CatalogOperationException {
        if (privilegesId.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateIncorrectRequest();
        }
        List<Privileges> privileges = privilegesRepository.findByIdIn(privilegesId);
        if (privileges.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateIncorrectRequest();
        }
        if (Objects.nonNull(role)) {
            List<RolePrivilege> idsToDelete = rolePrivilegeRepository.findAllByRoleId(role.getId());
            rolePrivilegeRepository.deleteAllById(idsToDelete.stream().map(BaseEntity::getId).collect(Collectors.toList()));
        }
        List<RolePrivilege> rolePrivilege = new ArrayList<>();
        privileges.forEach(privilege -> rolePrivilege.add(
                RolePrivilege.builder()
                        .role(role)
                        .privileges(privilege)
                        .build()
        ));
        rolePrivilegeRepository.saveAll(rolePrivilege);
    }

    @Transactional
    public RoleResponseDTO getRole(List<Long> ids) throws CatalogOperationException {
        Set<Roles> rolesList = new HashSet<>();
        if (ids != null) {
            rolesList.addAll(rolesRepository.findByIdIn(ids));
        } else {
            rolesRepository.findAll().forEach(rolesList::add);
        }

        if (rolesList.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }

        return RoleResponseDTO.builder()
                .roles(rolesList.stream()
                        .map(role -> RoleResponseDTO.RoleData.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .defaultRole(role.getDefaultRole())
                                .privileges(role.getRolePrivileges()
                                        .stream()
                                        .map(rp -> PrivilegesResponseDTO.PrivilegesDataDTO.builder()
                                                .id(rp.getPrivileges().getId())
                                                .name(rp.getPrivileges().getName())
                                                .tag(rp.getPrivileges().getTag())
                                                .privilegeGroup(rp.getPrivileges().getPrivilegeGroup())
                                                .name(rp.getPrivileges().getName())
                                                .build())
                                        .collect(Collectors.toUnmodifiableSet()))
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
