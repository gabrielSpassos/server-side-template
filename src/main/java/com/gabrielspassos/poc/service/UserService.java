package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.UserDTOBuilder;
import com.gabrielspassos.poc.builder.entity.UserEntityBuilder;
import com.gabrielspassos.poc.controller.request.UserRequest;
import com.gabrielspassos.poc.dto.UserDTO;
import com.gabrielspassos.poc.entity.UserEntity;
import com.gabrielspassos.poc.exception.EmailAlreadyUsedException;
import com.gabrielspassos.poc.exception.NotFoundUserException;
import com.gabrielspassos.poc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gabrielspassos.poc.enumerator.UserStatusEnum.ACTIVE;
import static com.gabrielspassos.poc.enumerator.UserStatusEnum.INACTIVE;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
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

        UserEntity entityToSave = UserEntityBuilder.build(userRequest, ACTIVE);
        UserEntity savedEntity = userRepository.save(entityToSave);
        return UserDTOBuilder.build(savedEntity);
    }

    public UserDTO updateUser(Long id, UserRequest userRequest) {
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

}
