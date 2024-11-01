package kz.symtech.antifraud.adminservice.controller;

import kz.symtech.antifraud.adminservice.service.PrivilegesService;
import kz.symtech.antifraud.models.dto.PrivilegesResponseDTO;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/privileges")
@RequiredArgsConstructor
public class PrivilegesController {
    private final PrivilegesService privilegesService;

    @GetMapping
    public ResponseEntity<PrivilegesResponseDTO> getPrivileges(
            @RequestParam(name = "ids", required = false) List<Long> ids
            ) throws CatalogOperationException {
        PrivilegesResponseDTO result = privilegesService.getPrivileges(ids);
        return ResponseEntity.ok(result);
    }
}
