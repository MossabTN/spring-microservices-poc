package io.maxilog.web.rest;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import io.maxilog.service.OrderService;
import io.maxilog.service.dto.OrderDTO;
import io.maxilog.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController(value = "orderRessource")
@RequestMapping("/api/")
public class OrderResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/orders")
    @Timed
    public ResponseEntity findAll(Pageable pageable) {
        LOGGER.debug("REST request to get all Users");
        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    @GetMapping("/orders/me")
    @Timed
    public ResponseEntity findMyData() {
        LOGGER.debug("REST request to my data");
        return ResponseEntity.ok(orderService.findMyOrders());
    }

    @GetMapping("/orders/{id}")
    @Timed
    public ResponseEntity findById(@PathVariable("id") long id) {
        LOGGER.debug("REST request to get User : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(orderService.findOne(id)));
    }

    @PostMapping("/orders")
    @Timed
    public ResponseEntity create(OrderDTO orderDTO) throws URISyntaxException {
        LOGGER.debug("REST request to save User : {}", orderDTO);
        if (orderDTO.getId() != null) {
            throw new HystrixBadRequestException("A new user cannot already have an ID");
        }

        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId())).body(result);
    }

    @DeleteMapping("/orders/{id}")
    @Timed
    public ResponseEntity delete(@PathVariable("id") long id) {
        LOGGER.debug("REST request to delete User : {}", id);
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }


}