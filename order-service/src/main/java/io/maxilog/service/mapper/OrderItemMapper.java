package io.maxilog.service.mapper;


import io.maxilog.domain.OrderItem;
import io.maxilog.service.dto.OrderItemDTO;

/**
 * Mapper for the entity OrderItem and its DTO OrderItemDTO.
 */
//@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface OrderItemMapper extends EntityMapper <OrderItemDTO, OrderItem> {
}
