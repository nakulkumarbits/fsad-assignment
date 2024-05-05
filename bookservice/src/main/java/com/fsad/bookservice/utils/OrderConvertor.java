package com.fsad.bookservice.utils;

import com.fsad.bookservice.dto.OrderDTO;
import com.fsad.bookservice.entities.Order;

public class OrderConvertor {

  public static Order toEntity(OrderDTO orderDTO) {
    if (orderDTO == null) {
      return null;
    }
    return Order.builder()
        .action(orderDTO.getAction())
        .deliveryMode(orderDTO.getDeliveryMode())
        .duration(orderDTO.getDuration())
        .orderStatus(orderDTO.getOrderStatus())
        .ownerID(orderDTO.getOwnerID())
        .ownerBookID(orderDTO.getOwnerBookID())
        .recipientID(orderDTO.getRecipientID())
        .recipientBookID(orderDTO.getRecipientBookID())
        .build();
  }

  public static OrderDTO toDTO(Order order) {
    if (order == null) {
      return null;
    }

    return OrderDTO.builder()
        .action(order.getAction())
        .deliveryMode(order.getDeliveryMode())
        .duration(order.getDuration())
        .orderStatus(order.getOrderStatus())
        .ownerID(order.getOwnerID())
        .ownerBookID(order.getOwnerBookID())
        .recipientID(order.getRecipientID())
        .recipientBookID(order.getRecipientBookID())
        .build();
  }
}
