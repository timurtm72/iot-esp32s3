package dev.timur.example.iotesp32s3.serviceimpl;

import dev.timur.example.iotesp32s3.dto.UserDto;
import dev.timur.example.iotesp32s3.dto.UserReadDto;
import dev.timur.example.iotesp32s3.enums.Role;
import dev.timur.example.iotesp32s3.enums.Status;
import dev.timur.example.iotesp32s3.mapper.UserMapper;
import dev.timur.example.iotesp32s3.model.User;
import dev.timur.example.iotesp32s3.repository.UserRepository;
import dev.timur.example.iotesp32s3.service.UserService;
import dev.timur.example.iotesp32s3.specification.UserSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления пользователями в системе IoT.
 * Предоставляет полный набор операций CRUD с поддержкой кеширования,
 * современных методов поиска и фильтрации через Specifications,
 * а также оптимизированных запросов для повышения производительности.
 * 
 * Особенности:
 * - Использует Spring Cache для оптимизации частых запросов
 * - Применяет MapStruct для безопасного маппинга без утечки паролей
 * - Поддерживает транзакционность для операций изменения данных
 * - Интегрирован с современными Spring Data Specifications
 */
@Service
public class UserServiceImpl implements UserService {
    
    /** Репозиторий для работы с данными пользователей */
    private final UserRepository userRepository;
    
    /** Маппер для преобразования между DTO и Entity */
    private final UserMapper userMapper;

    /**
     * Конструктор сервиса с внедрением зависимостей.
     * 
     * @param userRepository репозиторий для работы с пользователями
     * @param userMapper маппер для преобразования объектов
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации повторных запросов.
     */
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserReadDto getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toReadDto).orElse(null);
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации повторных запросов.
     * Использует MapStruct для безопасного преобразования без паролей.
     */
    @Override
    @Cacheable(value = "allUsers")
    public List<UserReadDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Проверяет уникальность username и email перед созданием.
     * 
     * ВАЖНО: В production версии пароль должен хешироваться!
     */
    @Transactional
    @Override
    @CacheEvict(value = {"users", "allUsers", "activeUsers"}, allEntries = true)
    public Status create(UserDto userDto, String password) {
        if (userDto == null || password == null || password.isEmpty()) {
            return Status.IS_EMPTY;
        }
        
        // Проверка уникальности username
        if (existsByUsername(userDto.getUsername())) {
            return Status.IS_NOT_FOUND; // переиспользуем для "уже существует"
        }
        
        // Проверка уникальности email
        if (existsByEmail(userDto.getEmail())) {
            return Status.IS_NOT_FOUND; // переиспользуем для "уже существует"
        }
        
        User user = userMapper.toEntity(userDto);
        user.setPassword(password); // В реальном проекте здесь должно быть хеширование
        userRepository.save(user);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Обновляет только переданные поля (partial update).
     */
    @Transactional
    @Override
    @CacheEvict(value = {"users", "allUsers", "activeUsers"}, allEntries = true)
    public Status update(UserDto userDto, Long id) {
        if (userDto == null) {
            return Status.IS_EMPTY;
        }
        
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return Status.IS_NOT_FOUND;
        }
        
        User existingUser = existingUserOpt.get();
        
        // Обновляем только непустые поля
        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            existingUser.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getFirstName() != null) {
            existingUser.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            existingUser.setLastName(userDto.getLastName());
        }
        if (userDto.getRole() != null) {
            existingUser.setRole(userDto.getRole());
        }
        if (userDto.getActive() != null) {
            existingUser.setActive(userDto.getActive());
        }
        
        userRepository.save(existingUser);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Выполняется в транзакции и очищает связанные кеши.
     * Выполняет жесткое удаление из базы данных.
     */
    @Transactional
    @Override
    @CacheEvict(value = {"users", "allUsers", "activeUsers"}, allEntries = true)
    public Status delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return Status.IS_NULL;
        }
        userRepository.deleteById(id);
        return Status.IS_OK;
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется по username для оптимизации.
     */
    @Override
    @Cacheable(value = "userByUsername", key = "#username")
    public UserReadDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toReadDto).orElse(null);
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется по email для оптимизации.
     */
    @Override
    @Cacheable(value = "userByEmail", key = "#email")
    public UserReadDto findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toReadDto).orElse(null);
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации частых запросов активных пользователей.
     */
    @Override
    @Cacheable(value = "activeUsers")
    public List<UserReadDto> findAllActiveUsers() {
        List<User> users = userRepository.findByActiveTrue();
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный нативный SQL запрос с ранжированием результатов.
     */
    @Override
    public List<UserReadDto> searchUsers(String query) {
        List<User> users = userRepository.searchActiveUsers(query);
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный exists запрос вместо count.
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * {@inheritDoc}
     * 
     * Использует оптимизированный exists запрос вместо count.
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Новые методы с современными подходами
    
    /**
     * {@inheritDoc}
     * 
     * Использует встроенную пагинацию Spring Data для оптимальной производительности.
     */
    @Override
    public Page<UserReadDto> findAllActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findByActiveTrue(pageable);
        return users.map(userMapper::toReadDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Использует пагинированный нативный SQL запрос.
     */
    @Override
    public Page<UserReadDto> searchUsers(String query, Pageable pageable) {
        Page<User> users = userRepository.searchActiveUsers(query, pageable);
        return users.map(userMapper::toReadDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Использует Spring Data Specifications для динамических запросов.
     */
    @Override
    public List<UserReadDto> findUsersByRole(Role role) {
        List<User> users = userRepository.findAll(UserSpecification.hasRole(role));
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Композиционные Specifications для комбинирования условий.
     */
    @Override
    public List<UserReadDto> findActiveUsersByRole(Role role) {
        List<User> users = userRepository.findAll(
            UserSpecification.isActive().and(UserSpecification.hasRole(role))
        );
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует Query Method для оптимизированного поиска по дате.
     */
    @Override
    public List<UserReadDto> findUsersCreatedAfter(LocalDateTime date) {
        List<User> users = userRepository.findByActiveTrueAndCreatedAtAfter(date);
        return users.stream()
                .map(userMapper::toReadDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует композитные Specifications для гибкого комбинирования фильтров.
     * Все параметры опциональны - null значения игнорируются.
     */
    @Override
    public Page<UserReadDto> findUsersWithFilters(Boolean active, Role role, 
            String searchText, LocalDateTime createdAfter, LocalDateTime createdBefore, 
            Pageable pageable) {
        
        Page<User> users = userRepository.findAll(
            UserSpecification.buildComplexQuery(active, role, searchText, createdAfter, createdBefore),
            pageable
        );
        return users.map(userMapper::toReadDto);
    }

    /**
     * {@inheritDoc}
     * 
     * Результат кешируется для оптимизации статистических запросов.
     */
    @Override
    @Cacheable(value = "userStats", key = "'activeCount'")
    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
    }

    /**
     * {@inheritDoc}
     * 
     * Использует Specifications для подсчета активных пользователей по роли.
     * Результат кешируется с ключом, включающим название роли.
     */
    @Override
    @Cacheable(value = "userStats", key = "'roleCount_' + #role.name()")
    public long countUsersByRole(Role role) {
        return userRepository.count(
            UserSpecification.isActive().and(UserSpecification.hasRole(role))
        );
    }
} 