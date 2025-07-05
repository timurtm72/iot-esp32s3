package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public ResponseEntity<UserReadDto> create(@RequestBody UserDto dto) {
        UserReadDto created = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserReadDto> update(@PathVariable Long id, @RequestBody UserDto dto) {
        return userService.updateUser(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Получить всех активных пользователей")
    @GetMapping
    public List<UserReadDto> findAll() {
        return userService.getAllActiveUsers();
    }

    @Operation(summary = "Получить пользователей по роли")
    @GetMapping("/role/{role}")
    public List<UserReadDto> byRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }

    @Operation(summary = "Пользователи, входившие после даты")
    @GetMapping("/last-login-after/{isoDateTime}")
    public List<UserReadDto> lastLoginAfter(@PathVariable String isoDateTime) {
        return userService.getUsersWithLastLoginAfter(LocalDateTime.parse(isoDateTime));
    }
} 