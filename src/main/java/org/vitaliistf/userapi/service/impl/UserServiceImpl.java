package org.vitaliistf.userapi.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vitaliistf.userapi.entity.User;
import org.vitaliistf.userapi.exception.*;
import org.vitaliistf.userapi.repository.UserRepository;
import org.vitaliistf.userapi.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Implementation of UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final int minAge;

    /**
     * Constructor for UserServiceImpl.
     *
     * @param userRepository The user repository.
     * @param minAge         Minimum age for users.
     */
    public UserServiceImpl(UserRepository userRepository, @Value("${app.min-age}") int minAge) {
        this.userRepository = userRepository;
        this.minAge = minAge;
    }

    /**
     * Retrieves all users.
     *
     * @return A list of User objects.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User object with the specified ID.
     * @throws ResourceNotFoundException if no user exists with the given ID.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    /**
     * Creates a new user.
     *
     * @param user The User object to create.
     * @return The created User object.
     * @throws EmailAlreadyExistsException    if the email already exists in the database.
     * @throws PhoneNumberAlreadyExistsException if the phone number already exists in the database.
     * @throws InvalidAgeException           if the user's age is below the minimum required age.
     */
    @Override
    public User createUser(User user) {
        validateAge(user.getBirthDate());
        validateEmailUniqueness(user.getEmail());
        validatePhoneNumberUniqueness(user.getPhoneNumber());
        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id   The ID of the user to update.
     * @param user The User object with updated information.
     * @return The updated User object.
     * @throws ResourceNotFoundException     if no user exists with the given ID.
     * @throws EmailAlreadyExistsException    if the email already exists for another user.
     * @throws PhoneNumberAlreadyExistsException if the phone number already exists for another user.
     */
    @Override
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        validateEmailUniqueness(user.getEmail(), existingUser);
        validatePhoneNumberUniqueness(user.getPhoneNumber(), existingUser);
        updateUserFields(existingUser, user);
        return userRepository.save(existingUser);
    }

    /**
     * Partially updates an existing user.
     *
     * @param id   The ID of the user to partially update.
     * @param user The User object with updated information.
     * @return The partially updated User object.
     * @throws ResourceNotFoundException     if no user exists with the given ID.
     * @throws EmailAlreadyExistsException    if the email already exists for another user.
     * @throws PhoneNumberAlreadyExistsException if the phone number already exists for another user.
     */
    @Override
    public User partialUpdateUser(Long id, User user) {
        User existingUser = getUserById(id);
        if (user.getEmail() != null) {
            validateEmailUniqueness(user.getEmail(), existingUser);
        }
        if (user.getPhoneNumber() != null) {
            validatePhoneNumberUniqueness(user.getPhoneNumber(), existingUser);
        }
        updateUserFields(existingUser, user, true);
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @throws ResourceNotFoundException if no user exists with the given ID.
     */
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Retrieves users within a specified birthdate range.
     *
     * @param startDate The start date of the birthdate range.
     * @param endDate   The end date of the birthdate range.
     * @return A list of User objects within the specified birthdate range.
     * @throws InvalidDateRangeException if the start date is after the end date.
     */
    @Override
    public List<User> getUsersByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return userRepository.findByBirthDateBetween(startDate, endDate);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email " + email + " already exists");
        }
    }

    private void validateEmailUniqueness(String email, User existingUser) {
        if (userRepository.existsByEmailAndIdNot(email, existingUser.getId())) {
            throw new EmailAlreadyExistsException("Email " + email + " already exists");
        }
    }

    private void validatePhoneNumberUniqueness(String phoneNumber) {
        if (phoneNumber != null && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException("Phone number " + phoneNumber + " already exists");
        }
    }

    private void validatePhoneNumberUniqueness(String phoneNumber, User existingUser) {
        if (phoneNumber != null && userRepository.existsByPhoneNumberAndIdNot(phoneNumber, existingUser.getId())) {
            throw new PhoneNumberAlreadyExistsException("Phone number " + phoneNumber + " already exists");
        }
    }

    private void validateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        if (age < minAge) {
            throw new InvalidAgeException("User must be at least " + minAge + " years old");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date must be before end date");
        }
    }

    private void updateUserFields(User existingUser, User user) {
        updateUserFields(existingUser, user, false);
    }

    private void updateUserFields(User existingUser, User user, boolean partialUpdate) {
        if (user.getEmail() != null || !partialUpdate) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getFirstName() != null || !partialUpdate) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null || !partialUpdate) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getBirthDate() != null || !partialUpdate) {
            validateAge(user.getBirthDate());
            existingUser.setBirthDate(user.getBirthDate());
        }
        if (user.getAddress() != null || !partialUpdate) {
            existingUser.setAddress(user.getAddress());
        }
        if (user.getPhoneNumber() != null || !partialUpdate) {
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }
    }
}
