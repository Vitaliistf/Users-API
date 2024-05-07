package org.vitaliistf.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.vitaliistf.userapi.dto.UserDto;
import org.vitaliistf.userapi.dto.UserPatchDto;
import org.vitaliistf.userapi.dto.UserPostPutDto;
import org.vitaliistf.userapi.entity.User;
import org.vitaliistf.userapi.mapper.UserMapper;
import org.vitaliistf.userapi.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void getAllUsers() throws Exception {
        List<User> users = List.of(
                new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address 1", "+12345678901")
        );
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(new UserDto(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address 1", "+12345678901"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+12345678901"));
    }

    @Test
    void getUserById() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        UserDto userDto = new UserDto(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"))
                .andExpect(jsonPath("$.phoneNumber").value("+12345678901"));
    }

    @Test
    void createUser() throws Exception {
        UserPostPutDto userPostPutDto = new UserPostPutDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        UserDto userDto = new UserDto(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPostPutDtoToUser(any(UserPostPutDto.class))).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"))
                .andExpect(jsonPath("$.phoneNumber").value("+12345678901"));
    }

    @Test
    void updateUser() throws Exception {
        UserPostPutDto userPostPutDto = new UserPostPutDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        UserDto userDto = new UserDto(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPostPutDtoToUser(any(UserPostPutDto.class))).thenReturn(user);
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"))
                .andExpect(jsonPath("$.phoneNumber").value("+12345678901"));
    }

    @Test
    void partialUpdateUser() throws Exception {
        UserPatchDto userPatchDto = new UserPatchDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        UserDto userDto = new UserDto(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPatchDtoToUser(any(UserPatchDto.class))).thenReturn(user);
        when(userService.partialUpdateUser(eq(1L), any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"))
                .andExpect(jsonPath("$.phoneNumber").value("+12345678901"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void getUsersByBirthDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);
        List<User> users = Arrays.asList(
                new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address 1", "+12345678901"),
                new User(2L, "test2@example.com", "Jane", "Doe", LocalDate.of(1992, 3, 15), "Address 2", "+98765432109")
        );
        List<UserDto> userDtos = Arrays.asList(
                new UserDto(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address 1", "+12345678901"),
                new UserDto(2L, "test2@example.com", "Jane", "Doe", LocalDate.of(1992, 3, 15), "Address 2", "+98765432109")
        );
        when(userService.getUsersByBirthDateRange(startDate, endDate)).thenReturn(users);
        when(userMapper.userToUserDto(users.get(0))).thenReturn(userDtos.get(0));
        when(userMapper.userToUserDto(users.get(1))).thenReturn(userDtos.get(1));

        mockMvc.perform(get("/api/users/search")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test1@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+12345678901"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].birthDate").value("1992-03-15"))
                .andExpect(jsonPath("$[1].address").value("Address 2"))
                .andExpect(jsonPath("$[1].phoneNumber").value("+98765432109"));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_BadRequest() throws Exception {
        UserPostPutDto userPostPutDto = new UserPostPutDto("invalid-email", "", "", null, "", "");
        when(userMapper.userPostPutDtoToUser(any(UserPostPutDto.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_Conflict() throws Exception {
        UserPostPutDto userPostPutDto = new UserPostPutDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPostPutDtoToUser(any(UserPostPutDto.class))).thenReturn(new User());
        when(userService.createUser(any(User.class))).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUser_NotFound() throws Exception {
        UserPostPutDto userPostPutDto = new UserPostPutDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPostPutDtoToUser(any(UserPostPutDto.class))).thenReturn(new User());
        when(userService.updateUser(eq(1L), any(User.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostPutDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void partialUpdateUser_NotFound() throws Exception {
        UserPatchDto userPatchDto = new UserPatchDto("test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", "+12345678901");
        when(userMapper.userPatchDtoToUser(any(UserPatchDto.class))).thenReturn(new User());
        when(userService.partialUpdateUser(eq(1L), any(User.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatchDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByBirthDateRange_BadRequest() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("startDate", "invalid")
                        .param("endDate", "invalid"))
                .andExpect(status().isBadRequest());
    }
}