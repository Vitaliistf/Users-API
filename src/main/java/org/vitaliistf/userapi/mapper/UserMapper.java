package org.vitaliistf.userapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitaliistf.userapi.dto.UserPatchDto;
import org.vitaliistf.userapi.dto.UserPostPutDto;
import org.vitaliistf.userapi.dto.UserDto;
import org.vitaliistf.userapi.entity.User;

/**
 * Mapper interface for mapping between User and its DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a User entity to a UserDto.
     *
     * @param user The User entity to map.
     * @return The mapped UserDto.
     */
    UserDto userToUserDto(User user);

    /**
     * Maps a UserPostPutDto to a User entity.
     *
     * @param userPostPutDTO The UserCreateDto to map.
     * @return The mapped User entity.
     */
    @Mapping(target = "id", ignore = true)
    User userPostPutDtoToUser(UserPostPutDto userPostPutDTO);

    /**
     * Maps a UserPatchDto to a User entity.
     *
     * @param userPatchDTO The UserUpdateDto to map.
     * @return The mapped User entity.
     */
    @Mapping(target = "id", ignore = true)
    User userPatchDtoToUser(UserPatchDto userPatchDTO);
}
