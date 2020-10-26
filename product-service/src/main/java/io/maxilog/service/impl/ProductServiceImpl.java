package io.maxilog.service.impl;

import io.maxilog.config.kafka.ProductMessaging;
import io.maxilog.domain.Product;
import io.maxilog.domain.UserHolder;
import io.maxilog.order.OrderAvro;
import io.maxilog.repository.ProductRepository;
import io.maxilog.service.ProductService;
import io.maxilog.service.dto.ProductDTO;
import io.maxilog.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductMessaging productMessaging;
    private final UserHolder userHolder;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper, ProductMessaging productMessaging, UserHolder userHolder) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productMessaging = productMessaging;
        this.userHolder = userHolder;
    }


    @Override
    public ProductDTO save(ProductDTO productDTO) {
        LOG.debug("Request to save Users : {}", productDTO);
        Product user = productRepository.save(productMapper.toEntity(productDTO));
        return productMapper.toDto(user);
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Users");
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> findAllByName(String name, Pageable pageable) {
        LOG.debug("Request to get all Products by name");
        return productRepository.findAllByNameContaining(name, pageable)
                .map(productMapper::toDto);
    }

    @StreamListener(Sink.INPUT)
    public void consumeKafkaOrder(Message<OrderAvro> message) {
        LOG.info("receiving order from kafka");
        LOG.info(message.getPayload().toString());

    }

    @Override
    public ProductDTO findOne(long id) {
        LOG.debug("Request to get User : {}", id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        LOG.debug("Request to delete User : {}", id);
        productRepository.deleteById(id);
    }
}
