package io.maxilog.service.impl;

import io.maxilog.domain.Category;
import io.maxilog.domain.UserHolder;
import io.maxilog.repository.CategoryRepository;
import io.maxilog.service.CategoryService;
import io.maxilog.service.dto.CategoryDTO;
import io.maxilog.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserHolder userHolder;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, UserHolder userHolder) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userHolder = userHolder;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        LOG.debug("Request to save Users : {}", categoryDTO);
        Category user = categoryRepository.save(categoryMapper.toEntity(categoryDTO));
        return categoryMapper.toDto(user);
    }

    @Override
    public Page<CategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Users");
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public List<CategoryDTO> findAllByName(String name) {
        LOG.debug("Request to get all Categorys by name");
        return categoryMapper.toDto(categoryRepository.findAllByName(name));
    }

    @Override
    public CategoryDTO findOne(long id) {
        LOG.debug("Request to get User : {}", id);
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        LOG.debug("Request to delete User : {}", id);
        categoryRepository.deleteById(id);
    }
}
