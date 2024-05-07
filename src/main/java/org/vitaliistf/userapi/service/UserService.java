package org.vitaliistf.userapi.service;

import org.springframework.stereotype.Service;
import org.vitaliistf.userapi.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
@Service
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return A list of User objects.
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User object with the specified ID.
     */
    User getUserById(Long id);

    /**
     * Creates a new user.
     *
     * @param user The User object to create.
     * @return The created User object.
     */
    User createUser(User user);

    /**
     * Updates an existing user.
     *
     * @param id   The ID of the user to update.
     * @param user The User object with updated information.
     * @return The updated User object.
     */
    User updateUser(Long id, User user);

    /**
     * Partially updates an existing user.
     *
     * @param id   The ID of the user to partially update.
     * @param user The User object with updated information.
     * @return The partially updated User object.
     */
    User partialUpdateUser(Long id, User user);

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    void deleteUser(Long id);

    /**
     * Retrieves users within a specified birthdate range.
     *
     * @param startDate The start date of the birthdate range.
     * @param endDate   The end date of the birthdate range.
     * @return A list of User objects within the specified birthdate range.
     */
    List<User> getUsersByBirthDateRange(LocalDate startDate, LocalDate endDate);

}
