package io.maxilog.client;

import io.maxilog.service.dto.CustomerDTO;
import io.maxilog.service.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@AuthorizedFeignClient(name = "product")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}", headers = "Accept=application/json")
    ResponseEntity<ProductDTO> getProductById(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.GET, value = "/products/by-ids", headers = "Accept=application/json")
    ResponseEntity<List<ProductDTO>> getProductByIds(@RequestBody List<Long> ids);

}
