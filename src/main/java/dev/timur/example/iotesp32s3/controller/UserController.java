package dev.timur.example.iotesp32s3.controller;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.service.UserService;
import dev.timur.example.iotesp32s3.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserReadDto>> getUserById(@PathVariable Long id) {
        UserReadDto userDto = userService.getById(id);
        if (userDto != null) {
            return ResponseEntity.ok(new Response<>(userDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Response<List<UserReadDto>>> getAllUsers() {
        List<UserReadDto> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(new Response<>(users, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Response<String>> createUser(@RequestBody UserDto userDto) {
        Status status = userService.create(userDto, "defaultPassword");
        if (status == Status.IS_OK) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response<>("Пользователь создан успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка создания пользователя", status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<String>> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        Status status = userService.update(userDto, id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Пользователь обновлен успешно", status));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response<>("Ошибка обновления пользователя", status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteUser(@PathVariable Long id) {
        Status status = userService.delete(id);
        if (status == Status.IS_OK) {
            return ResponseEntity.ok(new Response<>("Пользователь удален успешно", status));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>("Пользователь не найден", status));
    }

    @GetMapping("/search/username/{username}")
    public ResponseEntity<Response<UserReadDto>> getUserByUsername(@PathVariable String username) {
        UserReadDto userDto = userService.findByUsername(username);
        if (userDto != null) {
            return ResponseEntity.ok(new Response<>(userDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<Response<UserReadDto>> getUserByEmail(@PathVariable String email) {
        UserReadDto userDto = userService.findByEmail(email);
        if (userDto != null) {
            return ResponseEntity.ok(new Response<>(userDto, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }

    @GetMapping("/active")
    public ResponseEntity<Response<List<UserReadDto>>> getActiveUsers() {
        List<UserReadDto> users = userService.findAllActiveUsers();
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(new Response<>(users, Status.IS_OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response<>(null, Status.IS_NOT_FOUND));
    }
} 