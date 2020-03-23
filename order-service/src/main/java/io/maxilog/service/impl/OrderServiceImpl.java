package io.maxilog.service.impl;

import io.maxilog.domain.Order;
import io.maxilog.repository.OrderRepository;
import io.maxilog.service.OrderService;
import io.maxilog.service.dto.OrderDTO;
import io.maxilog.domain.UserHolder;
import io.maxilog.service.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserHolder userHolder;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserHolder userHolder) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userHolder = userHolder;
    }


    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        LOG.debug("Request to save orders : {}", orderDTO);
        Order order = orderRepository.save(orderMapper.toEntity(orderDTO));
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            /*orderEmitter.send(new OrderAvro(order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItem(orderItem.getQuantity().toString(),orderItem.getProductId().toString()))
                    .collect(Collectors.toList())));*/
        }
        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all orders");
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDTO findOne(long id) {
        LOG.debug("Request to get order : {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElse(null);
    }

    @Override
    public List<OrderDTO> findMyOrders() {
        return orderMapper.toDto(orderRepository.findAllByCustomer(userHolder.getUserName()));
    }

    @Override
    public void delete(long id) {
        LOG.debug("Request to delete order : {}", id);
        orderRepository.deleteById(id);
    }
}
