package org.vitaliistf.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for updating user information.
 */
public record UserPatchDto(
        @Email(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
                message = "Email is not valid.")
        String email,

        String firstName,

        String lastName,

        @Past(message = "Birth date should be in the past.")
        LocalDate birthDate,

        String address,

        @Pattern(regexp = "^\\+[0-9]{10,15}$", message = "Phone number is invalid")
        String phoneNumber) {

}
