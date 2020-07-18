package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.UserDTOBuilder;
import com.gabrielspassos.poc.builder.entity.UserEntityBuilder;
import com.gabrielspassos.poc.controller.request.UserRequest;
import com.gabrielspassos.poc.dto.UserDTO;
import com.gabrielspassos.poc.entity.UserEntity;
import com.gabrielspassos.poc.exception.EmailAlreadyUsedException;
import com.gabrielspassos.poc.exception.InvalidUserException;
import com.gabrielspassos.poc.exception.NotFoundUserException;
import com.gabrielspassos.poc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gabrielspassos.poc.enumerator.UserStatusEnum.ACTIVE;
import static com.gabrielspassos.poc.enumerator.UserStatusEnum.INACTIVE;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private static final String LOGIN_REGEX = "\\S.?\\w\\d?";
    private static final String PASSWORD_REGEX = "\\d{4}";
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private UserRepository userRepository;

    public List<UserDTO> getActiveUsers() {
        return userRepository.findAll().stream()
                .filter(entity -> ACTIVE.equals(entity.getStatus()))
                .map(UserDTOBuilder::build)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(UserDTOBuilder::build)
                .orElseThrow(NotFoundUserException::new);
    }

    public UserDTO createUser(UserRequest userRequest) {
        UserEntity userByEmail = userRepository.findByEmail(userRequest.getEmail());

        if (Objects.nonNull(userByEmail)) {
            throw new EmailAlreadyUsedException();
        }

        validateUser(userRequest);

        UserEntity entityToSave = UserEntityBuilder.build(userRequest, ACTIVE);
        UserEntity savedEntity = userRepository.save(entityToSave);
        return UserDTOBuilder.build(savedEntity);
    }

    public UserDTO updateUser(Long id, UserRequest userRequest) {
        validateUser(userRequest);

        return userRepository.findById(id)
                .map(entity -> UserEntityBuilder.build(entity, userRequest))
                .map(updatedUser -> userRepository.save(updatedUser))
                .map(UserDTOBuilder::build)
                .orElseThrow(NotFoundUserException::new);
    }

    public UserDTO deleteUser(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    entity.setStatus(INACTIVE);
                    return userRepository.save(entity);
                }).map(UserDTOBuilder::build)
                .orElseThrow(NotFoundUserException::new);
    }

    private void validateUser(UserRequest userRequest) {
        String login = userRequest.getLogin();
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        if (Objects.isNull(login) || isFieldValuePatternInvalid(LOGIN_REGEX, login))
            throw new InvalidUserException("Invalid login");

        if (Objects.isNull(email) || isFieldValuePatternInvalid(EMAIL_REGEX, email))
            throw new InvalidUserException("Invalid email");

        if (Objects.isNull(password) || isFieldValuePatternInvalid(PASSWORD_REGEX, password))
            throw new InvalidUserException("Invalid password");
    }

    private Boolean isFieldValuePatternInvalid(String pattern, String fieldValue) {
        return !Pattern.compile(pattern).matcher(fieldValue).find();
    }

}
