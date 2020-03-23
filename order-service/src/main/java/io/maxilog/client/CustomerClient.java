package io.maxilog.client;

import io.maxilog.service.dto.CustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@AuthorizedFeignClient(name = "customer")
public interface CustomerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/username/{username}", headers = "Accept=application/json")
    ResponseEntity<CustomerDTO> getUserByUsername(@PathVariable("username") String username);
}
