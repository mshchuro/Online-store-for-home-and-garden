package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.UserNotFoundException;
import org.telran.online_store.exception.UserNotUniqueException;
import org.telran.online_store.repository.UserJpaRepository;
import org.telran.online_store.security.JwtService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private User user;

    @BeforeEach
    public void setUp() {
        // Создаем пользователя для тестов
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setPassword("password123");
        userRepository.save(user);

        // Генерация токена для пользователя с помощью JwtService
        String token = jwtService.generateToken(user);  // Теперь это локальная переменная, как просил компилятор

        // Подделываем аутентификацию для текущего пользователя с токеном
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), token);

        // Устанавливаем аутентификацию в контексте безопасности
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllUsers() {
        // Используем @Builder для создания пользователей
        User user1 = User.builder()
                .name("User One")
                .email("user1@example.com")
                .phone("1234567890")
                .password("password123")
                .role(UserRole.CLIENT)
                .build();
        userService.create(user1);

        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .phone("0987654321")
                .password("password123")
                .role(UserRole.CLIENT)
                .build();
        userService.create(user2);

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(6, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testCreateUserSuccessfully() {
        // Новый пользователь
        User newUser = new User();
        newUser.setName("Jane Doe");
        newUser.setEmail("jane.doe@example.com");
        newUser.setPhone("0987654321");
        newUser.setPassword("password123");

        // Создаем пользователя
        User createdUser = userService.create(newUser);

        // Проверяем, что пользователь был создан
        assertNotNull(createdUser);
        assertEquals(newUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        // Пытаемся создать пользователя с уже существующим email
        User duplicateUser = new User();
        duplicateUser.setName("John Smith");
        duplicateUser.setEmail(user.getEmail());  // Используем тот же email
        duplicateUser.setPhone("111223344");
        duplicateUser.setPassword("password123");

        // Проверка на исключение
        Exception exception = assertThrows(UserNotUniqueException.class, () -> userService.create(duplicateUser));

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    public void testGetById() {
        // Получаем пользователя по ID
        User foundUser = userService.getById(user.getId());

        // Проверяем, что пользователь найден
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void testGetByEmail() {
        // Используем @Builder для создания пользователя
        User user = User.builder()
                .name("Test User")
                .email("user@example.com")
                .phone("1234567890")
                .password("password123")
                .role(UserRole.CLIENT)
                .build();

        // Сохраняем пользователя в базе данных
        userService.create(user);

        // Получаем пользователя по email
        User foundUser = userService.getByEmail("user@example.com");

        // Проверяем, что пользователь найден
        assertNotNull(foundUser);
        assertEquals("user@example.com", foundUser.getEmail());
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    public void testGetByIdNotFound() {
        // Пытаемся получить пользователя, которого нет в базе
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getById(999L));

        assertTrue(exception.getMessage().contains("is not found"));
    }

    @Test
    public void testUpdateProfile() {
        // Обновляем профиль пользователя
        user.setName("Updated Name");
        user.setPhone("1112223333");

        // Сохраняем обновленный профиль
        User updatedUser = userService.updateProfile(user.getId(), user);

        // Проверяем обновление
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("1112223333", updatedUser.getPhone());
    }

    @Test
    public void testDelete() {
        // Удаляем пользователя
        userService.delete(user.getId());

        // Проверяем, что пользователя больше нет в базе
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.getById(user.getId()));

        assertTrue(exception.getMessage().contains("is not found"));
    }

    @Test
    public void testGetCurrentUser() {
        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Проверяем, что это тот же пользователь, который был аутентифицирован
        assertNotNull(currentUser);
        assertEquals(user.getEmail(), currentUser.getEmail());
    }
}