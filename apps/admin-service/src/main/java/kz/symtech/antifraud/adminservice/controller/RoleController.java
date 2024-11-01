package kz.symtech.antifraud.adminservice.controller;

import kz.symtech.antifraud.adminservice.dto.roles.ChangeRoleRequestDTO;
import kz.symtech.antifraud.adminservice.dto.roles.CreateRoleRequestDTO;
import kz.symtech.antifraud.adminservice.dto.roles.CreateRoleResponseDTO;
import kz.symtech.antifraud.adminservice.service.RoleService;
import kz.symtech.antifraud.models.dto.RoleResponseDTO;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<CreateRoleResponseDTO> createRole(@RequestBody CreateRoleRequestDTO requestDTO) throws CatalogOperationException {
        CreateRoleResponseDTO result = roleService.createRole(requestDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change")
    public ResponseEntity<?> changeRole(@RequestBody ChangeRoleRequestDTO requestDTO) throws CatalogOperationException {
        roleService.changeRole(requestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RoleResponseDTO> getRole(
            @RequestParam(name = "ids", required = false) List<Long> ids
    ) throws CatalogOperationException {
        RoleResponseDTO result = roleService.getRole(ids);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}