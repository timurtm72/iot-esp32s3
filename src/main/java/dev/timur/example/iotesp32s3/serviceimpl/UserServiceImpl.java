package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.mapper.UserMapper;
import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.repository.UserRepository;
import dev.timur.example.iotesp32s3.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с пользователями
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    /**
     * Создание нового пользователя
     * @param userDto данные пользователя с паролем
     * @return созданный пользователь (без пароля)
     */
    @Override
    @Transactional
    public UserReadDto createUser(UserDto userDto) {
        log.info("Создание нового пользователя с именем: {}", userDto.getUsername());
        
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        
        log.info("Пользователь успешно создан с ID: {}", savedUser.getId());
        return userMapper.toReadDto(savedUser);
    }
    
    /**
     * Получение пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return пользователь или пустой Optional (без пароля)
     */
    @Override
    @Cacheable(value = "users", key = "#id")
    public Optional<UserReadDto> getUserById(Long id) {
        log.debug("Поиск пользователя по ID: {}", id);
        
        return userRepository.findById(id)
                .filter(user -> user.getRemovedAt() == null)
                .map(userMapper::toReadDto);
    }
    
    /**
     * Обновление данных пользователя
     * @param id идентификатор пользователя
     * @param userDto новые данные пользователя
     * @return обновленный пользователь (без пароля)
     */
    @Override
    @Transactional
    public Optional<UserReadDto> updateUser(Long id, UserDto userDto) {
        log.info("Обновление пользователя с ID: {}", id);
        
        return userRepository.findById(id)
                .filter(user -> user.getRemovedAt() == null)
                .map(existingUser -> {
                    userMapper.updateEntity(userDto, existingUser);
                    User updatedUser = userRepository.save(existingUser);
                    
                    log.info("Пользователь с ID {} успешно обновлен", id);
                    return userMapper.toReadDto(updatedUser);
                });
    }
    
    /**
     * Мягкое удаление пользователя
     * @param id идентификатор пользователя
     * @return true если пользователь удален, false если не найден
     */
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        
        return userRepository.findById(id)
                .filter(user -> user.getRemovedAt() == null)
                .map(user -> {
                    user.setRemovedAt(LocalDateTime.now());
                    userRepository.save(user);
                    log.info("Пользователь с ID {} успешно удален", id);
                    return true;
                })
                .orElse(false);
    }
    
    /**
     * Получение всех активных пользователей
     * @return список активных пользователей (без паролей)
     */
    @Override
    @Cacheable("activeUsers")
    public List<UserReadDto> getAllActiveUsers() {
        log.debug("Получение всех активных пользователей");
        
        List<User> users = userRepository.findByRemovedAtIsNull();
        return userMapper.toReadDtoList(users);
    }
    
    /**
     * Поиск пользователя по имени пользователя
     * @param username имя пользователя
     * @return пользователь или пустой Optional (без пароля)
     */
    @Override
    public Optional<UserReadDto> getUserByUsername(String username) {
        log.debug("Поиск пользователя по имени: {}", username);
        
        return userRepository.findByUsernameAndRemovedAtIsNull(username)
                .map(userMapper::toReadDto);
    }
    
    /**
     * Поиск пользователя по электронной почте
     * @param email электронная почта
     * @return пользователь или пустой Optional (без пароля)
     */
    @Override
    public Optional<UserReadDto> getUserByEmail(String email) {
        log.debug("Поиск пользователя по email: {}", email);
        
        return userRepository.findByEmailAndRemovedAtIsNull(email)
                .map(userMapper::toReadDto);
    }
    
    /**
     * Получение пользователей по роли
     * @param role роль пользователя
     * @return список пользователей с указанной ролью (без паролей)
     */
    @Override
    public List<UserReadDto> getUsersByRole(Role role) {
        log.debug("Получение пользователей по роли: {}", role);
        
        List<User> users = userRepository.findByRoleAndRemovedAtIsNull(role);
        return userMapper.toReadDtoList(users);
    }
    
    /**
     * Получение пользователей по статусу активности
     * @param active статус активности
     * @return список пользователей с указанным статусом (без паролей)
     */
    @Override
    public List<UserReadDto> getUsersByActiveStatus(Boolean active) {
        log.debug("Получение пользователей по статусу активности: {}", active);
        
        List<User> users = userRepository.findByActiveAndRemovedAtIsNull(active);
        return userMapper.toReadDtoList(users);
    }
    
    /**
     * Получение пользователей с последним входом после указанной даты
     * @param lastLoginAfter дата последнего входа
     * @return список пользователей (без паролей)
     */
    @Override
    public List<UserReadDto> getUsersWithLastLoginAfter(LocalDateTime lastLoginAfter) {
        log.debug("Получение пользователей с последним входом после: {}", lastLoginAfter);
        
        List<User> users = userRepository.findByLastLoginAfterAndRemovedAtIsNull(lastLoginAfter);
        return userMapper.toReadDtoList(users);
    }
} 