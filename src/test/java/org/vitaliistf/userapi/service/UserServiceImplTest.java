package org.vitaliistf.userapi.service;

import org.vitaliistf.userapi.exception.*;
import org.vitaliistf.userapi.entity.User;
import org.vitaliistf.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User user2 = new User(2L, "test2@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15), null, null);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Exists() {
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser_ValidAge() {
        User user = new User(null, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any(String.class))).thenReturn(false);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_InvalidAge() {
        User user = new User(null, "test@example.com", "John", "Doe", LocalDate.now().plusYears(1), "123 Main St", "1234567890");

        assertThrows(InvalidAgeException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        User user = new User(null, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_PhoneNumberAlreadyExists() {
        User user = new User(null, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any(String.class))).thenReturn(true);

        assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Exists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, "updated@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);
        when(userRepository.existsByPhoneNumberAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testUpdateUser_NotFound() {
        User updatedUser = new User(1L, "updated@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, "existing@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_PhoneNumberAlreadyExists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, "updated@example.com", "Jane", "Smith", LocalDate.of(1985, 5, 15), null, "1234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);
        when(userRepository.existsByPhoneNumberAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPartialUpdateUser_Exists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, null, "Jane", null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.partialUpdateUser(1L, updatedUser);

        assertEquals("test@example.com", result.getEmail());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testPartialUpdateUser_NotFound() {
        User updatedUser = new User(1L, null, "Jane", null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.partialUpdateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPartialUpdateUser_EmailAlreadyExists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, "existing@example.com", null, null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.partialUpdateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPartialUpdateUser_PhoneNumberAlreadyExists() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, null, null, null, null, null, "1234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByPhoneNumberAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(PhoneNumberAlreadyExistsException.class, () -> userService.partialUpdateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Exists() {
        User user = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testGetUsersByBirthDateRange() {
        User user1 = new User(1L, "test1@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User user2 = new User(2L, "test2@example.com", "Jane", "Smith", LocalDate.of(1995, 5, 15), null, null);
        User user3 = new User(3L, "test3@example.com", "Bob", "Johnson", LocalDate.of(1988, 10, 20), "456 Elm St", "9876543210");
        List<User> users = Arrays.asList(user1, user2, user3);

        LocalDate startDate = LocalDate.of(1988, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);

        when(userRepository.findByBirthDateBetween(startDate, endDate)).thenReturn(users);

        List<User> result = userService.getUsersByBirthDateRange(startDate, endDate);

        assertEquals(users, result);
        verify(userRepository, times(1)).findByBirthDateBetween(startDate, endDate);
    }

    @Test
    void testGetUsersByBirthDateRange_InvalidRange() {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);

        assertThrows(InvalidDateRangeException.class, () -> userService.getUsersByBirthDateRange(startDate, endDate));
        verify(userRepository, never()).findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

//    @Test
//    void testValidateAge_Valid() {
//        LocalDate birthDate = LocalDate.of(1990, 1, 1);
//        userService.validateAge(birthDate); // No exception should be thrown
//    }
//
//    @Test
//    void testValidateAge_Invalid() {
//        LocalDate birthDate = LocalDate.now().minusYears(17);
//        assertThrows(InvalidAgeException.class, () -> userService.validateAge(birthDate));
//    }
//
//    @Test
//    void testValidateDateRange_Valid() {
//        LocalDate startDate = LocalDate.of(1990, 1, 1);
//        LocalDate endDate = LocalDate.of(1995, 12, 31);
//        userService.validateDateRange(startDate, endDate); // No exception should be thrown
//    }

//    @Test
//    void testValidateDateRange_Invalid() {
//        LocalDate startDate = LocalDate.of(2000, 1, 1);
//        LocalDate endDate = LocalDate.of(1995, 12, 31);
//        assertThrows(InvalidDateRangeException.class, () -> userService.validateDateRange(startDate, endDate));
//    }

    @Test
    void testUpdateUser_InvalidAge() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, "updated@example.com", "Jane", "Smith", LocalDate.now().plusYears(1), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);
        when(userRepository.existsByPhoneNumberAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        assertThrows(InvalidAgeException.class, () -> userService.updateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testPartialUpdateUser_InvalidAge() {
        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
        User updatedUser = new User(1L, null, null, null, LocalDate.now().plusYears(1), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(InvalidAgeException.class, () -> userService.partialUpdateUser(1L, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

//    @Test
//    void testUpdateUserFields_NullFields() {
//        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
//        User updatedUser = new User(null, null, null, null, null, null, null);
//
//        userService.updateUserFields(existingUser, updatedUser);
//
//        assertEquals("test@example.com", existingUser.getEmail());
//        assertEquals("John", existingUser.getFirstName());
//        assertEquals("Doe", existingUser.getLastName());
//        assertEquals(LocalDate.of(1990, 1, 1), existingUser.getBirthDate());
//        assertEquals("123 Main St", existingUser.getAddress());
//        assertEquals("1234567890", existingUser.getPhoneNumber());
//    }
//
//    @Test
//    void testUpdateUserFields_PartialUpdate() {
//        User existingUser = new User(1L, "test@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "1234567890");
//        User updatedUser = new User(1L, "updated@example.com", null, "Smith", null, null, null);
//
//        userService.updateUserFields(existingUser, updatedUser, true);
//
//        assertEquals("updated@example.com", existingUser.getEmail());
//        assertEquals("John", existingUser.getFirstName());
//        assertEquals("Smith", existingUser.getLastName());
//        assertEquals(LocalDate.of(1990, 1, 1), existingUser.getBirthDate());
//        assertEquals("123 Main St", existingUser.getAddress());
//        assertEquals("1234567890", existingUser.getPhoneNumber());
//    }
}