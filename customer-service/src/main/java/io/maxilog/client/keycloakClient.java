package io.maxilog.client;

import io.maxilog.domain.RoleRepresentation;
import io.maxilog.domain.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AuthorizedFeignClient(name = "keycloak", url = "${keycloak.url}/auth/admin/realms/${keycloak.realm}")
public interface keycloakClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users", headers = "Accept=application/json")
    ResponseEntity<List<UserRepresentation>> getAllUsers();

    @RequestMapping(method = RequestMethod.GET, value = "/users", headers = "Accept=application/json")
    ResponseEntity<List<UserRepresentation>> getUsersPageable(@RequestParam(name = "first") int page, @RequestParam(name = "max") int size);

    @RequestMapping(method = RequestMethod.GET, value = "/users/count", headers = "Accept=application/json")
    ResponseEntity<Integer> countUsers();

    @RequestMapping(method = RequestMethod.GET, value = "/users", headers = "Accept=application/json")
    ResponseEntity<List<UserRepresentation>> getUsersByEmail(@RequestParam(name = "email") String email);

    @RequestMapping(method = RequestMethod.GET, value = "/users", headers = "Accept=application/json")
    ResponseEntity<List<UserRepresentation>> getUsersByUsername(@RequestParam(name = "username") String username);

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}", headers = "Accept=application/json")
    ResponseEntity<UserRepresentation> getUserById(@PathVariable(name = "id") String id);

    @RequestMapping(method = RequestMethod.POST, value = "/users", headers = "Accept=application/json")
    ResponseEntity<String> createUser(@RequestBody UserRepresentation userRepresentation);

    @RequestMapping(method = RequestMethod.PUT, value = "/users/{id}", headers = "Accept=application/json")
    ResponseEntity<String> updateUser(@PathVariable(name = "id") String id, @RequestBody UserRepresentation userRepresentation);

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{id}", headers = "Accept=application/json")
    ResponseEntity<String> deleteUser(@PathVariable(name = "id") String id);

    @RequestMapping(method = RequestMethod.POST, value = "/roles", headers = "Accept=application/json")
    ResponseEntity<String> createRole(@RequestBody RoleRepresentation roleRepresentation);

    @RequestMapping(method = RequestMethod.DELETE, value = "/roles/{role-name}", headers = "Accept=application/json")
    ResponseEntity<String> deleteRole(@PathVariable(name = "role-name") String roleName);

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}/role-mappings/realm/available", headers = "Accept=application/json")
    ResponseEntity<List<RoleRepresentation>> getRealmRolesByUser(@PathVariable(name = "id") String id);

    @RequestMapping(method = RequestMethod.POST, value = "/users/{id}/role-mappings/realm", headers = "Accept=application/json")
    ResponseEntity<String> createUserRole(@PathVariable(name = "id") String id, @RequestBody List<RoleRepresentation> roles);

}
