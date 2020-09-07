package com.gabrielspassos.poc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielspassos.poc.controller.request.UserRequest;
import com.gabrielspassos.poc.dto.UserDTO;
import com.gabrielspassos.poc.entity.UserEntity;
import com.gabrielspassos.poc.exception.EmailAlreadyUsedException;
import com.gabrielspassos.poc.exception.InvalidUserException;
import com.gabrielspassos.poc.exception.NotFoundUserException;
import com.gabrielspassos.poc.repository.UserRepository;
import com.gabrielspassos.poc.stub.UserEntityStub;
import com.gabrielspassos.poc.stub.UserRequestStub;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.gabrielspassos.poc.enumerator.UserStatusEnum.ACTIVE;
import static com.gabrielspassos.poc.enumerator.UserStatusEnum.INACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private final String validEmail = "maria.silva@gmail.com";
    private final String validLogin = "maria.silva";
    private final String validPassword = "1234";
    private final UserRequest userRequest = UserRequestStub.create(validEmail, validLogin, validPassword);
    private final UserEntity activeUser = UserEntityStub.create(1L, ACTIVE, "jose.silva", "jose.silva@gmail.com");
    private final UserEntity inactiveUser = UserEntityStub.create(2L, INACTIVE, "jose.silva", "jose.silva2@gmail.com");
    private final UserEntity newUser = UserEntityStub.create(3L, ACTIVE, validLogin, validEmail);
    private final UserEntity updatedUser = UserEntityStub.create(1L, ACTIVE, validLogin, validEmail);
    private final UserEntity deletedUser = UserEntityStub.create(3L, INACTIVE, validLogin, validEmail);
    private final List<UserEntity> users = Lists.newArrayList(activeUser, inactiveUser);

    @Test
    public void shouldReturnListOfUsers() {
        given(userRepository.findAll()).willReturn(users);

        List<UserDTO> activeUsers = userService.getActiveUsers();

        assertEquals(1, activeUsers.size());
        assertEquals(1L, activeUsers.get(0).getId());
        assertEquals("jose.silva@gmail.com", activeUsers.get(0).getEmail());
    }

    @Test
    public void shouldReturnEmptyList() {
        given(userRepository.findAll()).willReturn(Lists.emptyList());

        List<UserDTO> activeUsers = userService.getActiveUsers();

        assertEquals(0, activeUsers.size());
    }

    @Test
    public void shouldReturnUserById() {
        given(userRepository.findById(1L)).willReturn(Optional.of(activeUser));

        UserDTO user = userService.getUser(1L);

        assertEquals(1L, user.getId());
        assertEquals("jose.silva", user.getLogin());
        assertEquals("jose.silva@gmail.com", user.getEmail());
    }

    @Test
    public void shouldThrowErrorToNotFoundUserById() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundUserException.class, () -> userService.getUser(99L));

        verify(userRepository).findById(99L);
    }

    @Test
    public void shouldCreateUser() {
        given(userRepository.findByEmail(validEmail)).willReturn(null);
        given(userRepository.save(any(UserEntity.class))).willReturn(newUser);

        UserDTO user = userService.createUser(userRequest);

        assertEquals(3L, user.getId());
        assertEquals(validLogin, user.getLogin());
        assertEquals(validEmail, user.getEmail());
    }

    @Test
    public void shouldThrowErrorForEmailInUse() {
        UserRequest userWithDuplicateEmail = new ObjectMapper().convertValue(this.userRequest, UserRequest.class);
        userWithDuplicateEmail.setEmail("jose.silva@gmail.com");

        given(userRepository.findByEmail("jose.silva@gmail.com")).willReturn(activeUser);

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(userWithDuplicateEmail));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowErrorForInvalidLogin() {
        UserRequest userWithInvalidLogin = new ObjectMapper().convertValue(this.userRequest, UserRequest.class);
        userWithInvalidLogin.setLogin("  ");

        given(userRepository.findByEmail(validEmail)).willReturn(null);

        Assertions.assertThrows(InvalidUserException.class, () -> userService.createUser(userWithInvalidLogin));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowErrorForInvalidEmail() {
        UserRequest userWithInvalidEmail = new ObjectMapper().convertValue(this.userRequest, UserRequest.class);
        userWithInvalidEmail.setEmail("mariasilva");

        given(userRepository.findByEmail("mariasilva")).willReturn(null);

        Assertions.assertThrows(InvalidUserException.class, () -> userService.createUser(userWithInvalidEmail));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowErrorForInvalidPassword() {
        UserRequest userWithInvalidPassword = new ObjectMapper().convertValue(this.userRequest, UserRequest.class);
        userWithInvalidPassword.setPassword("123");

        given(userRepository.findByEmail(validEmail)).willReturn(null);

        Assertions.assertThrows(InvalidUserException.class, () -> userService.createUser(userWithInvalidPassword));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateUser() {
        given(userRepository.findById(1L)).willReturn(Optional.of(activeUser));
        given(userRepository.save(any(UserEntity.class))).willReturn(updatedUser);

        UserDTO userDTO = userService.updateUser(1L, userRequest);

        assertEquals(1L, userDTO.getId());
        assertEquals(validLogin, userDTO.getLogin());
        assertEquals(validEmail, userDTO.getEmail());
    }

    @Test
    public void shouldThrowErrorForNotFoundUserToUpdate() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundUserException.class, () -> userService.updateUser(99L, userRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteUser() {
        given(userRepository.findById(3L)).willReturn(Optional.of(newUser));
        given(userRepository.save(any())).willReturn(deletedUser);

        UserDTO userDTO = userService.deleteUser(3L);

        assertEquals(3L, userDTO.getId());
        assertEquals(validLogin, userDTO.getLogin());
        assertEquals(validEmail, userDTO.getEmail());
    }

    @Test
    public void shouldThrowErrorForNotFoundUserToDelete() {
        given(userRepository.findById(99L)).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundUserException.class, () -> userService.deleteUser(99L));

        verify(userRepository, never()).save(any());
    }

}