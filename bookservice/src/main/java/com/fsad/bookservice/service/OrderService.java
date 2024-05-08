package com.fsad.bookservice.service;

import com.fsad.bookservice.dto.OrderDTO;
import com.fsad.bookservice.dto.OrderResponseDTO;
import com.fsad.bookservice.entities.Order;
import com.fsad.bookservice.repository.OrderRepository;
import com.fsad.bookservice.utils.OrderConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  public OrderResponseDTO getOrderSummary(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Order> orderPage = orderRepository.findByRecipientID(userId, pageable);
    return getOrderResponseDTO(orderPage);
  }

  public OrderResponseDTO getRequests(Long userId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Order> requestPage = orderRepository.findByOwnerID(userId, pageable);
    return getOrderResponseDTO(requestPage);
  }

  private static OrderResponseDTO getOrderResponseDTO(Page<Order> page) {
    OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
        .page(page.getNumber())
        .size(page.getSize())
        .totalPages(page.getTotalPages())
        .totalElements(page.getTotalElements())
        .build();
    if (page.getTotalElements() > 0) {
      List<OrderDTO> orderDTOS = new ArrayList<>();
      page.getContent().forEach(order -> orderDTOS.add(OrderConvertor.toDTO(order)));
      orderResponseDTO.setOrderDTOS(orderDTOS);
    } else {
      orderResponseDTO.setOrderDTOS(Collections.emptyList());
    }
    return orderResponseDTO;
  }
}
