package io.maxilog.web.rest;


import io.maxilog.service.UserService;
import io.maxilog.service.dto.Page;
import io.maxilog.service.dto.Pageable;
import io.maxilog.service.dto.UserDTO;
import io.maxilog.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController(value = "userResource")
@RequestMapping("/api/")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    @Timed
    public Page<UserDTO> findAll(Pageable pageable) {
        LOGGER.info("REST request to get all Users");
        return userService.findPage(pageable);
    }

    @GetMapping("/users/me")
    @Timed
    public UserDTO findMyData() {
        LOGGER.info("REST request to my data");
        return userService.findMyData();
    }


    @GetMapping("/users/{id}")
    @Timed
    public ResponseEntity<?> findById(@PathParam("id") String id) {
        LOGGER.info("REST request to get User : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userService.findOne(id)));
    }

    @GetMapping("/users/username/{username}")
    @Timed
    public ResponseEntity<?> findByUsername(@PathParam("username") String username) {
        LOGGER.info("REST request to get User by username: {}", username);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userService.findUsername(username)));
    }

    @PostMapping("/users")
    @Timed
    public ResponseEntity<?> create(UserDTO userDTO) throws URISyntaxException {
        LOGGER.info("REST request to save User : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new RuntimeException("A new user cannot already have an ID");
        }

        UserDTO result = userService.save(userDTO);
        return ResponseEntity.created(new URI("/api/users/" + result.getId())).body(result);
    }

    @PutMapping("/users")
    @Timed
    public ResponseEntity<?> update(UserDTO userDTO) {
        LOGGER.info("REST request to update User : {}", userDTO);
        if (userDTO.getId() == null) {
            throw new RuntimeException("Updated user must have an ID");
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userService.update(userDTO)));
    }


    @DeleteMapping("/users/{id}")
    @Timed
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        LOGGER.info("REST request to delete User : {}", id);
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
