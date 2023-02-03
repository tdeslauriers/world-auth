package world.deslauriers.service;

import world.deslauriers.model.database.User;
import world.deslauriers.model.profile.ProfileDto;
import world.deslauriers.model.registration.RegistrationDto;

import java.util.Optional;

public interface UserService {

    Boolean userExists(String email);
    String registerUser(RegistrationDto registrationDto);

    Optional<User> lookupUserByUsername(String email);
    Optional<User> lookupUserById(Long id);

    Iterable<User> backupAll();

    Iterable<ProfileDto> getAllUsers();

    Optional<ProfileDto> getProfile(String username);
    Optional<ProfileDto> getProfileByUuid(String uuid);

    Optional<ProfileDto> updateUser(User user, ProfileDto updatedProfile);
}
