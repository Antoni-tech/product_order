package kz.symtech.antifraud.feignclients.clients.services;

import kz.symtech.antifraud.models.dto.UserListDTO;
import kz.symtech.antifraud.models.dto.UserResponseDTO;
import kz.symtech.antifraud.models.dto.RoleResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "admin-service")
public interface AdminServiceClient {

    @GetMapping("/admin-service/api/users/login")
    ResponseEntity<UserResponseDTO> getUser(@RequestParam(name = "loginOrId") String loginOrId);

    @GetMapping("/admin-service/api/users/list")
    ResponseEntity<UserListDTO> getUsers(@RequestParam(name = "ids") List<Long> ids);

    @GetMapping("/admin-service/api/users/search-name")
    ResponseEntity<UserListDTO> searchUsersName(@RequestParam(name = "name") String name);

    @GetMapping("/admin-service/api/users/password/check")
    ResponseEntity<UserResponseDTO> getUserWithCheckPassword(@RequestParam String login, @RequestParam String password);

    @GetMapping("/admin-service/api/roles")
    ResponseEntity<RoleResponseDTO> getRoles(@RequestParam(name = "ids", required = false) List<Long> ids);
}
