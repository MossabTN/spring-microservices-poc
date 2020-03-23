package io.maxilog.service.mapper;


import io.maxilog.domain.Order;
import io.maxilog.service.dto.OrderDTO;

/**
 * Mapper for the entity Order and its DTO OrderDTO.
 */
//@Mapper(componentModel = "spring", uses = {PaymentMapper.class, AddressMapper.class, OrderItemMapper.class})
public interface OrderMapper extends EntityMapper <OrderDTO, Order> {
}
