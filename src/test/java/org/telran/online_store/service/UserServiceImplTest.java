package org.telran.online_store.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.telran.online_store.entity.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(value = "/userDataInit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void testGetAll() {
        assertEquals(2, userService.getAll().size());
    }

    @Test
    void testGetById() {
        Long id = userService.getByEmail("john").getId();
        assertEquals("john", userService.getById(id).getName());
    }

    @Test
    void testDeleteById() {
        List<User> all = userService.getAll();
        userService.delete(all.get(0).getId());

        assertEquals(1, userService.getAll().size());
    }

    @Test
    void testCreateUser() {
        String userName = "Max";
        String email = "123qwe@gmail.com";
        String phone = "555666777";

        User user = new User();
        user.setName(userName);
        user.setEmail(email);
        user.setPhone(phone);

        userService.create(user);

        User fromDb = userService.getByEmail(userName);

        assertNotNull(fromDb);
        assertEquals(userName, fromDb.getName());
        assertEquals(email, fromDb.getEmail());
        assertEquals(phone, fromDb.getPhone());
    }

    @Test
    void testUpdateUser() {
//        User userBeforeUpdate = userService.getByEmail("alex");
//        Long id = userBeforeUpdate.getId();
//        assertEquals("alex", userBeforeUpdate.getName());
//        assertEquals("12345678", userBeforeUpdate.getPhone());
//
//        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
//        updateRequest.setName("Alexander");
//        updateRequest.setPhone("98989898");
//
//        userService.updateProfile(id, updateRequest);
//
//        User updatedUser = userService.getByEmail("Alexander");
//        assertEquals("Alexander", updatedUser.getName());
//        assertEquals("98989898", updatedUser.getPhone());
    }
}