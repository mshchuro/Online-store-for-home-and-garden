package org.telran.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.telran.online_store.converter.UserRegistrationConverter;
import org.telran.online_store.converter.UserConverter;
import org.telran.online_store.dto.*;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;
import org.telran.online_store.security.AuthenticationService;
import org.telran.online_store.security.SignInRequest;
import org.telran.online_store.security.SignInResponse;
import org.telran.online_store.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRegistrationConverter userRegistrationConverter;

    @MockBean
    private UserConverter userConverter;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testLogin() throws Exception {
        SignInRequest signInRequest = new SignInRequest("john@mail.com", "password");
        SignInResponse signInResponse = new SignInResponse("fake-jwt-token");

        Mockito.when(authenticationService.authenticate(any(SignInRequest.class))).thenReturn(signInResponse);

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void testRegister() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest(
                "John Smith", "john@mail.com", "+1234567890", "password");

        User user = User.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .role(UserRole.CLIENT)
                .build();

        UserRegistrationResponse response = UserRegistrationResponse.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .build();

        Mockito.when(userRegistrationConverter.toEntity(any(UserRegistrationRequest.class))).thenReturn(user);
        Mockito.when(userService.create(any(User.class))).thenReturn(user);
        Mockito.when(userRegistrationConverter.toDto(user)).thenReturn(response);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@mail.com"))
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetAllUsers() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .role(UserRole.ADMINISTRATOR)
                .build();

        UserUpdateResponseDto responseDto = UserUpdateResponseDto.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .userRole(UserRole.ADMINISTRATOR)
                .build();

        List<User> users = List.of(user);

        Mockito.when(userService.getAll()).thenReturn(users);
        Mockito.when(userConverter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("john@mail.com"))
                .andExpect(jsonPath("$[0].userRole").value("ADMINISTRATOR"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetUserById() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .role(UserRole.ADMINISTRATOR)
                .build();

        UserUpdateResponseDto responseDto = UserUpdateResponseDto.builder()
                .id(1L)
                .name("John Smith")
                .email("john@mail.com")
                .phone("+1234567890")
                .userRole(UserRole.ADMINISTRATOR)
                .build();

        Mockito.when(userService.getById(anyLong())).thenReturn(user);
        Mockito.when(userConverter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@mail.com"))
                .andExpect(jsonPath("$.userRole").value("ADMINISTRATOR"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).delete(anyLong());

        mockMvc.perform(delete("/v1/users/{userId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testUpdateUserProfile() throws Exception {
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto(
                "John Updated", "+111111111", UserRole.CLIENT);

        User user = User.builder()
                .id(1L)
                .name("John Updated")
                .email("john@mail.com")
                .phone("+111111111")
                .role(UserRole.CLIENT)
                .build();

        UserUpdateResponseDto responseDto = UserUpdateResponseDto.builder()
                .id(1L)
                .name("John Updated")
                .email("john@mail.com")
                .phone("+111111111")
                .userRole(UserRole.CLIENT)
                .build();

        Mockito.when(userConverter.toEntity(requestDto)).thenReturn(user);
        Mockito.when(userService.updateProfile(anyLong(), any(User.class))).thenReturn(user);
        Mockito.when(userConverter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.phone").value("+111111111"))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));
    }
}