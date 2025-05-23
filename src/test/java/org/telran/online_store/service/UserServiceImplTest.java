package org.telran.online_store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.exception.UserNotFoundException;
import org.telran.online_store.exception.UserNotUniqueException;
import org.telran.online_store.repository.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("test")
    public class UserServiceImplTest extends AbstractTests {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserJpaRepository userRepo;
//
//    @Autowired
//    private ProductJpaRepository productRepo;
//
//    @Autowired
//    private OrderJpaRepository orderRepo;
//
//    @Autowired
//    private FavoriteJpaRepository favoriteRepo;
//
//    private User testUser;
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        orderRepo.deleteAll();
//        favoriteRepo.deleteAll();
//        productRepo.deleteAll();
//        userRepo.deleteAll();
//
//        testUser = User.builder()
//                .name("Test User")
//                .email("test@example.com")
//                .phone("1234567890")
//                .password("password")
//                .role(UserRole.CLIENT)
//                .build();
//        testUser = userRepo.save(testUser);
//
//        testProduct = Product.builder()
//                .name("Test Product")
//                .description("Description")
//                .price(BigDecimal.valueOf(100))
//                .imageUrl("image.jpg")
//                .build();
//        testProduct = productRepo.save(testProduct);
//
//        UsernamePasswordAuthenticationToken auth =
//                new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword(), List.of());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder()
                .name("User One")
                .email("user1@example.com")
                .phone("123")
                .password("pass")
                .role(UserRole.CLIENT)
                .build();
        userService.create(user1);

        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .phone("456")
                .password("pass")
                .role(UserRole.CLIENT)
                .build();
        userService.create(user2);

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@example.com")));
    }

    @Test
    public void testCreateUserSuccessfully() {
        User newUser = new User();
        newUser.setName("Jane Doe");
        newUser.setEmail("jane.doe@example.com");
        newUser.setPhone("0987654321");
        newUser.setPassword("password123");

        User createdUser = userService.create(newUser);

        assertNotNull(createdUser);
        assertEquals(newUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        User duplicateUser = new User();
        duplicateUser.setName("Duplicate");
        duplicateUser.setEmail(testUser.getEmail());
        duplicateUser.setPhone("999");
        duplicateUser.setPassword("pass");

        assertThrows(UserNotUniqueException.class, () -> userService.create(duplicateUser));
    }

    @Test
    public void testGetById() {
        User foundUser = userService.getById(testUser.getId());
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
    }

    @Test
    public void testGetByEmail() {
        User u = User.builder()
                .name("User")
                .email("user@example.com")
                .phone("000")
                .password("pass")
                .role(UserRole.CLIENT)
                .build();
        userService.create(u);

        User found = userService.getByEmail("user@example.com");
        assertNotNull(found);
        assertEquals("User", found.getName());
    }

    @Test
    public void testGetByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getById(99999L));
    }

    @Test
    public void testUpdateProfile() {
        testUser.setName("Updated");
        testUser.setPhone("321");

        User updated = userService.updateProfile(testUser.getId(), testUser);

        assertEquals("Updated", updated.getName());
        assertEquals("321", updated.getPhone());
    }

    @Test
    public void testDelete() {
        userService.delete(testUser.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getById(testUser.getId()));
    }

    @Test
    public void testGetCurrentUser() {
        User currentUser = userService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(testUser.getEmail(), currentUser.getEmail());
    }
}