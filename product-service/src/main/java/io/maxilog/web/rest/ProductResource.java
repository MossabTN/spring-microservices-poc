package io.maxilog.web.rest;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import io.maxilog.service.ProductService;
import io.maxilog.service.dto.ProductDTO;
import io.maxilog.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController(value = "productRessource")
@RequestMapping("/api/")
public class ProductResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @Timed
    public ResponseEntity findAll(Pageable pageable) {
        LOGGER.debug("REST request to get all Products");
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/products/search")
    @Timed
    public Page<ProductDTO> findAllByProductName(@RequestParam("name") String name, Pageable pageable) {
        LOGGER.debug("REST request to get all Products by name {}", name);
        return productService.findAllByName(name, pageable);
    }

    @GetMapping("/products/{id}")
    @Timed
    public ResponseEntity findById(@PathVariable("id") long id) {
        LOGGER.debug("REST request to get Product : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productService.findOne(id)));
    }

    @PostMapping("/products")
    @Timed
    public ResponseEntity create(ProductDTO productDTO) throws URISyntaxException {
        LOGGER.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new HystrixBadRequestException("A new product cannot already have an ID");
        }

        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId())).body(result);
    }

    @PutMapping("/products")
    @Timed
    public ResponseEntity update(ProductDTO productDTO) {
        LOGGER.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new HystrixBadRequestException("Updated product must have an ID");
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productService.save(productDTO)));
    }

    @PutMapping("/products/{id}")
    @Timed
    public ResponseEntity delete(@PathVariable("id") long id) {
        LOGGER.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}