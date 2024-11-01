package kz.symtech.antifraud.adminservice.controller;

import kz.symtech.antifraud.adminservice.dto.users.ChangeUserDTO;
import kz.symtech.antifraud.adminservice.dto.users.CreateUserRequest;
import kz.symtech.antifraud.adminservice.dto.users.CreateUserResponse;
import kz.symtech.antifraud.adminservice.service.UserService;
import kz.symtech.antifraud.models.dto.*;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest requestDTO) throws CatalogOperationException {
        CreateUserResponse result = userService.createUserAndSendMail(requestDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change")
    public ResponseEntity<?> changeUser(@RequestBody ChangeUserDTO requestDto) throws CatalogOperationException {
        userService.changeUser(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam String loginOrId) throws CatalogOperationException {
        UserResponseDTO result = userService.getUser(loginOrId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/password/check")
    public ResponseEntity<UserResponseDTO> getUserWithCheckPassword(@ModelAttribute LoginRequest request) throws CatalogOperationException {
        UserResponseDTO result = userService.getUserWithPasswordCheck(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/password/change")
    public ResponseEntity<UserResponseDTO> changePassword(@RequestBody ChangePasswordRequest request) throws CatalogOperationException {
        UserResponseDTO result = userService.changePassword(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/password/restore")
    public ResponseEntity<UserResponseDTO> restorePassword(@RequestBody RestoreRequest request) throws NoSuchAlgorithmException
    {
        UserResponseDTO result = userService.restorePassword(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<UserListDTO> getUsers(
            @RequestParam(name = "ids", required = false) List<Long> ids
    ) throws CatalogOperationException {
        UserListDTO result = userService.getUsers(ids);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search-name")
    public ResponseEntity<UserListDTO> searchUsersName(
            @RequestParam(name = "name", required = false) String name
    ) throws CatalogOperationException {
        UserListDTO result = userService.searchUsersNames(name);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter-list")
    public ResponseEntity<UserListResponseDTO> getUsers(
            @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size
    ) {
        UserListResponseDTO result = userService.getUsers(roleIds, login, name, phone, email, page, size);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
