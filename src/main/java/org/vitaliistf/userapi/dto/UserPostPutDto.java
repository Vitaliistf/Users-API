package org.vitaliistf.userapi.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for creating a new user.
 */
public record UserPostPutDto(
    @NotBlank
    @Email(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
            message = "Email is not valid.")
    String email,

    @NotBlank(message = "First name should not be empty.")
    String firstName,

    @NotBlank(message = "Last name should not be empty.")
    String lastName,

    @Past(message = "Birth date should be in the past.")
    @NotNull(message = "Birth date should not be empty.")
    LocalDate birthDate,

    String address,

    @Pattern(regexp = "^\\+[0-9]{10,15}$", message = "Phone number is invalid")
    String phoneNumber) {

}