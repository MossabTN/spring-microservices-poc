package io.maxilog.service.impl;

import io.maxilog.client.keycloakClient;
import io.maxilog.domain.UserHolder;
import io.maxilog.service.UserService;
import io.maxilog.service.dto.Page;
import io.maxilog.service.dto.Pageable;
import io.maxilog.service.dto.UserDTO;
import io.maxilog.service.mapper.Impl.UserMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserHolder userHolder;
    private final keycloakClient keycloakClient;
    private final UserMapperImpl userMapper;

    public UserServiceImpl(keycloakClient keycloakClient, UserMapperImpl userMapper, UserHolder userHolder) {
        this.userHolder = userHolder;
        this.keycloakClient = keycloakClient;
        this.userMapper = userMapper;
    }


    @Override
    public UserDTO save(UserDTO userDTO) {
        LOG.info("Request to save Users : {}", userDTO);
        keycloakClient.createUser(userMapper.toEntity(userDTO));
        return userDTO;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        keycloakClient.updateUser(userDTO.getId(), userMapper.toEntity(userDTO));
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        LOG.info("Request to get all Users");
        return Objects.requireNonNull(keycloakClient.getUsersPageable(pageable.getPage(), pageable.getSize())
                .getBody())
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findPage(Pageable pageable) {
        return new Page<>(findAll(pageable), keycloakClient.countUsers().getBody());
    }

    @Override
    public UserDTO findOne(String id) {
        LOG.info("Request to get User : {}", id);
        return userMapper.toDto(keycloakClient.getUserById(id).getBody());
    }

    @Override
    public UserDTO findUsername(String username) {
        LOG.info("Request to get User by username: {}", username);
        return Objects.requireNonNull(keycloakClient.getUsersByUsername(username).getBody())
                .stream()
                .findFirst()
                .map(userMapper::toDto)
                .orElse(null);
    }

    @Override
    public UserDTO findMyData() {
        return Objects.requireNonNull(keycloakClient.getUsersByUsername(userHolder.getUserName()).getBody())
                .stream()
                .map(userMapper::toDto)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        LOG.info("Request to delete User : {}", id);
        keycloakClient.deleteUser(id);
    }
}
