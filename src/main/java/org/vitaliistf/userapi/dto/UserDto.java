package org.vitaliistf.userapi.dto;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for representing user information.
 */
public record UserDto (
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber) {
}
