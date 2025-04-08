package org.telran.online_store.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
    void testDeleteById() {
        userService.delete(2L);
        assertEquals(1, userService.getAll().size());
    }

    @Test
    void testGetById() {
        assertEquals("alex", userService.getById(1L).getName());
    }

}