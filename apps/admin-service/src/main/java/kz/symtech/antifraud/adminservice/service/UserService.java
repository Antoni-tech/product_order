package kz.symtech.antifraud.adminservice.service;

import kz.symtech.antifraud.adminservice.dto.users.ChangeUserDTO;
import kz.symtech.antifraud.adminservice.dto.users.CreateUserRequest;
import kz.symtech.antifraud.adminservice.dto.users.CreateUserResponse;
import kz.symtech.antifraud.adminservice.dto.users.CreateUserTempResponse;
import kz.symtech.antifraud.adminservice.entities.roles.Roles;
import kz.symtech.antifraud.adminservice.entities.roles.UserRole;
import kz.symtech.antifraud.adminservice.entities.users.UserPasswordRestore;
import kz.symtech.antifraud.adminservice.entities.users.Users;
import kz.symtech.antifraud.adminservice.repository.UserPasswordRestoreRepository;
import kz.symtech.antifraud.adminservice.repository.roles.RolesRepository;
import kz.symtech.antifraud.adminservice.repository.roles.UserRoleRepository;
import kz.symtech.antifraud.adminservice.repository.users.UserRepository;
import kz.symtech.antifraud.models.dto.*;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationException;
import kz.symtech.antifraud.models.entity.exception.CatalogOperationExceptionGenerator;
import kz.symtech.antifraud.models.utils.HashPasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Данный класс предоставляет функционал для проведения операций
 * над пользователями в системе ТФ
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPasswordRestoreRepository userPasswordRestoreRepository;
    private final RolesRepository rolesRepository;
    private final UserRoleRepository userRoleRepository;

    public CreateUserResponse createUserAndSendMail(CreateUserRequest userRequest) throws CatalogOperationException {
        CreateUserTempResponse response = createUser(userRequest);
        return CreateUserResponse.builder().id(response.getUser().getId()).build();
    }

    @Transactional
    public CreateUserTempResponse createUser(CreateUserRequest userRequest) throws CatalogOperationException {
        Optional<Users> targetUser = userRepository.findByLogin(userRequest.getLogin());
        if (targetUser.isPresent()) {
            throw CatalogOperationExceptionGenerator.generateExceptionWithContext("User with this login already exists");
        }
        String generatePassword = HashPasswordUtils.generatePassword(12);
        String salt = HashPasswordUtils.getSalt();
        String hashPass = HashPasswordUtils.hashPass(generatePassword, salt);

        Optional<Set<Roles>> targetRoles = Optional.ofNullable(rolesRepository.findByIdIn(userRequest.getRoleIds()));
        if (targetRoles.isEmpty() || !targetRoles.get().stream().map(Roles::getId).collect(Collectors.toSet())
                .containsAll(userRequest.getRoleIds())) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }

        Users newUser = Users.builder()
                .login(userRequest.getLogin())
                .name(userRequest.getName())
                .phone(userRequest.getPhone())
                .password(hashPass)
                .salt(salt)
                .createdAt(LocalDateTime.now())
                .email(userRequest.getEmail())
                .build();

        newUser.setUserRoles(targetRoles.get().stream().map(role -> UserRole.builder()
                .user(newUser)
                .role(role)
                .build()).collect(Collectors.toSet()));

        Users createdUser = userRepository.save(newUser);

        return CreateUserTempResponse.builder().user(createdUser).password(generatePassword).build();
    }

    @Transactional
    public void changeUser(ChangeUserDTO requestDTO) throws CatalogOperationException {
        Optional<Users> user = userRepository.findByLogin(requestDTO.getLogin());
        if (user.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }
        Optional<Set<Roles>> targetRoles = Optional.ofNullable(rolesRepository.findByIdIn(requestDTO.getRoleIds()));
        if (targetRoles.isEmpty() || !targetRoles.get().stream().map(Roles::getId).collect(Collectors.toSet())
                .containsAll(requestDTO.getRoleIds())) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }
        userRoleRepository.deleteAllById(user.get().getUserRoles().stream().map(UserRole::getId).toList());

        user.get().setEmail(requestDTO.getEmail());
        user.get().setName(requestDTO.getName());
        user.get().setPhone(requestDTO.getPhone());
        List<UserRole> newUserRoles = targetRoles.get().stream().map(role -> UserRole.builder()
                .user(user.get())
                .role(role)
                .build()).toList();
        Iterable<UserRole> out = userRoleRepository.saveAll(newUserRoles);
        Set<UserRole> resultSet = new HashSet<>();
        out.forEach(resultSet::add);
        user.get().setUserRoles(resultSet);
        userRepository.save(user.get());
    }

    public UserResponseDTO getUser(String loginOrId) throws CatalogOperationException {
        Optional<Users> result;
        if (StringUtils.isNumeric(loginOrId)) {
            result = userRepository.findById(Long.parseLong(loginOrId));
        } else {
            result = userRepository.findByLogin(loginOrId);
        }

        if (result.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }
        return buildUserResponse(result.get());
    }

    public UserListDTO getUsers(List<Long> ids) throws CatalogOperationException {
        List<Users> users;
        if (Objects.isNull(ids)) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findAllByIdIn(ids);
        }

        if (users.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }
        return UserListDTO.builder()
                .userResponseDTOS(users.stream().map(this::buildUserResponse).collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    public UserResponseDTO changePassword(ChangePasswordRequest request) throws CatalogOperationException {
        Users user = null;
        UserPasswordRestore userRestore = null;
        if (!request.getCode().isBlank()) {
            userRestore = userPasswordRestoreRepository.findFirstByCodeAndUsedIsFalse(request.getCode());
            user = userRestore.getUser();
        }
        if (!request.getLogin().isBlank()) {
            Optional<Users> userGet = userRepository.findByLogin(request.getLogin());
            if (userGet.isEmpty() || !userGet.get().getPassword().equals(HashPasswordUtils.hashPass(request.getOldPassword(), userGet.get().getSalt()))) {
                throw CatalogOperationExceptionGenerator.generateCheckPasswordFailException();
            }
            user = userGet.get();
        }
        if (Objects.nonNull(user)) {
            String salt = HashPasswordUtils.getSalt();
            String hashPass = HashPasswordUtils.hashPass(request.getNewPassword(), salt);
            user.setPassword(hashPass);
            user.setSalt(salt);
            userRepository.save(user);

            if (Objects.nonNull(userRestore)) {
                userRestore.setUsedAt(LocalDateTime.now());
                userRestore.setUsed(true);
                userPasswordRestoreRepository.save(userRestore);
            }
        } else {
            throw CatalogOperationExceptionGenerator.generateCheckPasswordFailException();
        }
        return buildUserResponse(user);
    }

    @Transactional
    public UserResponseDTO getUserWithPasswordCheck(LoginRequest request) throws CatalogOperationException {
        Optional<Users> user = userRepository.findByLogin(request.getLogin());
        if (user.isEmpty() || !user.get().getPassword().equals(HashPasswordUtils.hashPass(request.getPassword(), user.get().getSalt()))) {
            throw CatalogOperationExceptionGenerator.generateAuthFailException();
        }
        LocalDateTime desiredLastLogin = LocalDateTime.now();
        Date dateLastLogin = Date.from(desiredLastLogin.atZone(ZoneId.systemDefault()).toInstant());
        user.get().setLastLogin(dateLastLogin);
        userRepository.save(user.get());
        return buildUserResponse(user.get());
    }

    private UserResponseDTO buildUserResponse(Users user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .company(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .lastLogin(user.getLastLogin())
                .role(user.getUserRoles().stream().map(role -> RoleResponseDTO.RoleData.builder()
                        .id(role.getRole().getId())
                        .name(role.getRole().getName())
                        .build()).collect(Collectors.toSet()))
                .build();
    }

    public UserListResponseDTO getUsers(List<Long> roleIds, String login, String name, String phone, String email, Long page, Long size) {
        PageRequest req = Optional.ofNullable(page)
                .map(p -> PageRequest.of(p.intValue(), Objects.isNull(size) ? 10 : size.intValue(),
                        Sort.by(Sort.Direction.DESC, "createdAt")))
                .orElse(PageRequest.of(0, Integer.MAX_VALUE));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Users exampleEntity = Users.builder()
                .login(login)
                .name(name)
                .phone(phone)
                .email(email)
                .build();

        Example<Users> example = Example.of(exampleEntity, matcher);
        log.info("example:{}", example);
        Page<Users> data = userRepository.findAll(example, req);

        if (data.getContent().isEmpty()) {
            return new UserListResponseDTO();
        }

        return UserListResponseDTO.builder()
                .users(data.getContent().stream().map(this::buildUserResponse).collect(Collectors.toList()))
                .count(data.getTotalElements())
                .build();
    }

    public void deleteUser(Long id) {
        Users user = userRepository.findById(id).get();
        userRepository.delete(user);
    }

    public UserResponseDTO restorePassword(RestoreRequest request) throws NoSuchAlgorithmException {
        List<Users> users = userRepository.findByEmail(request.getEmail());

        users.forEach(v -> {
            String code = HashPasswordUtils.generateRandomKey();
            userPasswordRestoreRepository.save(
                    UserPasswordRestore.builder()
                            .code(code)
                            .used(false)
                            .createdAt(LocalDateTime.now())
                            .user(v)
                            .build());

        });
        return UserResponseDTO.builder().build();
    }

    public UserListDTO searchUsersNames(String name) throws CatalogOperationException {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Users> example = Example.of(Users.builder()
                .name(name)
                .build(), matcher);

        List<Users> users = userRepository.findAll(example);
        if (users.isEmpty()) {
            throw CatalogOperationExceptionGenerator.generateDataNotFoundException();
        }
        return UserListDTO.builder()
                .userResponseDTOS(users.stream().map(this::buildUserResponse).collect(Collectors.toSet()))
                .build();
    }

}
