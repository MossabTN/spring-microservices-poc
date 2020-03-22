package io.maxilog.web.rest;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import io.maxilog.service.CategoryService;
import io.maxilog.service.dto.CategoryDTO;
import io.maxilog.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController(value = "categoryRessource")
@RequestMapping("/api/")
public class CategoryResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryResource.class);

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    @Timed
    public ResponseEntity findAll(Pageable pageable) {
        LOGGER.debug("REST request to get all Categories");
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    @GetMapping("/categories/search")
    @Timed
    public List<CategoryDTO> findAllByCategoryName(@RequestParam("name") String fullName) {
        LOGGER.debug("REST request to get all Categories by fullName {}", fullName);
        return categoryService.findAllByName(fullName);
    }

    @GetMapping("/categories/{id}")
    @Timed
    public ResponseEntity findById(@PathVariable("id") long id) {
        LOGGER.debug("REST request to get Category : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(categoryService.findOne(id)));
    }

    @PostMapping("/categories")
    @Timed
    public ResponseEntity create(CategoryDTO categoryDTO) throws URISyntaxException {
        LOGGER.debug("REST request to save Category : {}", categoryDTO);
        if (categoryDTO.getId() != null) {
            throw new HystrixBadRequestException("A new category cannot already have an ID");
        }

        CategoryDTO result = categoryService.save(categoryDTO);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId())).body(result);
    }

    @PutMapping("/categories")
    @Timed
    public ResponseEntity update(CategoryDTO categoryDTO) {
        LOGGER.debug("REST request to update Category : {}", categoryDTO);
        if (categoryDTO.getId() == null) {
            throw new HystrixBadRequestException("Updated category must have an ID");
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(categoryService.save(categoryDTO)));
    }

    @DeleteMapping("/categories/{id}")
    @Timed
    public ResponseEntity delete(@PathVariable("id") long id) {
        LOGGER.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }


}