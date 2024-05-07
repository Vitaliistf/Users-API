package org.vitaliistf.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.userapi.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing user data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves users within a specified birthdate range.
     *
     * @param startDate The start date of the birthdate range.
     * @param endDate   The end date of the birthdate range.
     * @return A list of User objects within the specified birthdate range.
     */
    List<User> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check.
     * @return true if a user with the given email exists, otherwise false.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user with the given email exists, excluding the user with the specified ID.
     *
     * @param email The email to check.
     * @param id    The ID of the user to exclude.
     * @return true if a user with the given email exists, excluding the user with the specified ID, otherwise false.
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Checks if a user with the given phone number exists.
     *
     * @param phoneNumber The phone number to check.
     * @return true if a user with the given phone number exists, otherwise false.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Checks if a user with the given phone number exists, excluding the user with the specified ID.
     *
     * @param phoneNumber The phone number to check.
     * @param id          The ID of the user to exclude.
     * @return true if a user with the given phone number exists, excluding the user with the specified ID, otherwise false.
     */
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
}