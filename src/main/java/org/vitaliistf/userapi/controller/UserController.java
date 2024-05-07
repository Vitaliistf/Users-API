package org.vitaliistf.userapi.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.vitaliistf.userapi.dto.UserPatchDto;
import org.vitaliistf.userapi.dto.UserPostPutDto;
import org.vitaliistf.userapi.dto.UserDto;
import org.vitaliistf.userapi.entity.User;
import org.vitaliistf.userapi.mapper.UserMapper;
import org.vitaliistf.userapi.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling user-related operations.
 */
@OpenAPIDefinition(info = @Info(
        title = "Users REST API",
        description = "REST API responsible for the resource named Users"))
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity with a list of UserDto objects.
     */
    @Operation(
            summary = "Retrieves all users.",
            description = "Retrieves all users that are present in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users are retrieved.")
            })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtoList = users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtoList);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with a UserDto object.
     */
    @Operation(
            summary = "Retrieves a user by ID.",
            description = "Retrieves a user by specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A user is retrieved."),
                    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.userToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Creates a new user.
     *
     * @param userPostPutDto The DTO for creating a user.
     * @return ResponseEntity with the created UserDto object.
     */
    @Operation(
            summary = "Creates a new user.",
            description = "Creates a user that has all required data including valid age and a unique email.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User was created."),
                    @ApiResponse(responseCode = "400", description = "User is not valid.", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User email or phone number is not unique.",
                            content = @Content)
            })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserPostPutDto userPostPutDto) {
        User user = userMapper.userPostPutDtoToUser(userPostPutDto);
        User createdUser = userService.createUser(user);
        UserDto userDto = userMapper.userToUserDto(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    /**
     * Updates an existing user.
     *
     * @param id            The ID of the user to update.
     * @param userPostPutDto The DTO for updating a user.
     * @return ResponseEntity with the updated UserDto object.
     */
    @Operation(
            summary = "Updates an existing user.",
            description = "Updates a user if exists in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updates user with the provided data."),
                    @ApiResponse(responseCode = "400", description = "User is not valid.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User email or phone number is not unique.",
                            content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserPostPutDto userPostPutDto) {
        User user = userMapper.userPostPutDtoToUser(userPostPutDto);
        User updatedUser = userService.updateUser(id, user);
        UserDto userDto = userMapper.userToUserDto(updatedUser);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Partially updates an existing user.
     *
     * @param id            The ID of the user to partially update.
     * @param userPatchDto The DTO for partially updating a user.
     * @return ResponseEntity with the partially updated UserDto object.
     */
    @Operation(
            summary = "Partially updates an existing user.",
            description = "Updates only user's fields that are not null in the input object.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updates partially a user."),
                    @ApiResponse(responseCode = "400", description = "User is not valid.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User email or phone number is not unique.",
                            content = @Content)
            })
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(@PathVariable Long id,
                                                     @RequestBody @Valid UserPatchDto userPatchDto) {
        User user = userMapper.userPatchDtoToUser(userPatchDto);
        User updatedUser = userService.partialUpdateUser(id, user);
        UserDto userDto = userMapper.userToUserDto(updatedUser);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with no content.
     */
    @Operation(
            summary = "Deletes a user by ID.",
            description = "Deletes a user if present in the system.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User was deleted successfully."),
                    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves users within a specified birthdate range.
     *
     * @param startDate The start date of the birthdate range.
     * @param endDate   The end date of the birthdate range.
     * @return ResponseEntity with a list of UserDto objects.
     */
    @Operation(
            summary = "Retrieves users within a specified birthdate range.",
            description = "Searches for users using startDate and endDate request parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns all users that fit requirements."),
                    @ApiResponse(responseCode = "400", description = "Request parameters are not valid.",
                            content = @Content)
            })
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> getUsersByBirthDateRange(@RequestParam LocalDate startDate,
                                                                  @RequestParam LocalDate endDate) {
        List<User> users = userService.getUsersByBirthDateRange(startDate, endDate);
        List<UserDto> userDtoList = users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtoList);
    }
}
