package com.fsad.bookservice.controllers;

import com.fsad.bookservice.dto.OrderDTO;
import com.fsad.bookservice.dto.OrderResponseDTO;
import com.fsad.bookservice.dto.OrderStateDTO;
import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.entities.Order;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.enums.OrderStatus;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.repository.OrderRepository;
import com.fsad.bookservice.service.OrderService;
import com.fsad.bookservice.utils.BookUtil;
import com.fsad.bookservice.utils.OrderConvertor;
import com.fsad.bookservice.utils.Patcher;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fsad.bookservice.enums.Action.EXCHANGE;
import static com.fsad.bookservice.enums.Action.LEND;
import static com.fsad.bookservice.enums.OrderStatus.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Validated
@RestController
public class OrderController {

  @Autowired
  private BookUtil bookUtil;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private Patcher patcher;

  @Autowired
  private OrderService orderService;

  private final List<OrderStatus> ALLOWED_STATUS_LIST = List.of(AWAITING_OWNER_RESPONSE, INITIATE_DELIVERY, DELIVERY_IN_TRANSIT);

  @Operation(summary = "Fetch all orders for the logged in user.")
  @GetMapping("/orders")
  public ResponseEntity<OrderResponseDTO> getOrderSummary(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size,
                                                          @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        OrderResponseDTO orderSummary = orderService.getOrderSummary(response.getBody(), page, size);
        return new ResponseEntity<>(orderSummary, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error("[getOrderSummary] Failed to get orders", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Update order state using orderId.")
  @PatchMapping("/orders")
  public ResponseEntity<Void> updateOrderState(@RequestBody @Valid OrderStateDTO orderStateDTO, @RequestHeader("Authorization") String token) {
    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      orderService.updateOrderState(orderStateDTO, response.getBody());
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Fetch order requests for logged in user.")
  @GetMapping("/requests")
  public ResponseEntity<OrderResponseDTO> getRequests(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                                      @RequestHeader("Authorization") String token) {
    try {
      ResponseEntity<Long> response = bookUtil.validateToken(token);
      if (response.getStatusCode() == HttpStatus.OK) {
        OrderResponseDTO serviceRequests = orderService.getRequests(response.getBody(), page, size);
        return new ResponseEntity<>(serviceRequests, HttpStatus.OK);
      }
    } catch (Exception e) {
      log.error("[getRequests] Failed to get orders", e);
      if (e instanceof HttpClientErrorException) {
        return new ResponseEntity<>(((HttpClientErrorException) e).getStatusCode());
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @Operation(summary = "Book exchange request.")
  @PostMapping("/exchange")
  public ResponseEntity<Void> exchangeBook(@RequestBody OrderDTO orderDTO,
                                           @RequestHeader("Authorization") String token) {
    log.info("exchange : {}", orderDTO);

    if (orderDTO.getDeliveryMode() == null)
      orderDTO.setDeliveryMode(DeliveryMode.STANDARD);

    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {

      Optional<Book> recipientBook = bookRepository.findById(orderDTO.getRecipientBookID());
      if (recipientBook.isPresent()) {
        Book recipientBookObj = recipientBook.get();
        orderDTO.setRecipientID(recipientBookObj.getUserID());
        if (Objects.equals(recipientBookObj.getUserID(), response.getBody())) {

          Optional<Order> existingOrderOpt = orderRepository.findByRecipientIDAndOwnerBookIDAndOrderStatusIn(orderDTO.getRecipientID(), orderDTO.getOwnerBookID(), ALLOWED_STATUS_LIST);
          if (existingOrderOpt.isPresent()) {
            log.info("Request already exist for same book.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
          }

          Optional<Book> ownerBook = bookRepository.findById(orderDTO.getOwnerBookID());
          if (ownerBook.isPresent()) {
            Book ownerBookObj = ownerBook.get();
            orderDTO.setOwnerID(ownerBookObj.getUserID());
            orderDTO.setOrderStatus(AWAITING_OWNER_RESPONSE);
            orderDTO.setAction(EXCHANGE);

            Order order = OrderConvertor.toEntity(orderDTO);

            orderRepository.save(order);
            return new ResponseEntity<>(HttpStatus.OK);
          } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }
        } else {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      }
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @Operation(summary = "Book lend request.")
  @PostMapping("/lend")
  public ResponseEntity<Void> lendBook(@RequestBody @Valid OrderDTO orderDTO,
                                       @RequestHeader("Authorization") String token) {

    log.info("orderDTO : {}", orderDTO);

    if (orderDTO.getDeliveryMode() == null)
      orderDTO.setDeliveryMode(DeliveryMode.STANDARD);

    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {

      Long recipientID = response.getBody();

      Optional<Order> existingOrderOpt = orderRepository.findByRecipientIDAndOwnerBookIDAndOrderStatusIn(recipientID, orderDTO.getOwnerBookID(), ALLOWED_STATUS_LIST);
      if (existingOrderOpt.isPresent()) {
        log.info("Request already exist for same book.");
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }

      Optional<Book> optionalBook = bookRepository.findById(orderDTO.getOwnerBookID());
      if (optionalBook.isPresent()) {
        Book ownerBook = optionalBook.get();
        orderDTO.setOwnerID(ownerBook.getUserID());
        orderDTO.setOrderStatus(AWAITING_OWNER_RESPONSE);
        orderDTO.setAction(LEND);

        orderDTO.setRecipientID(recipientID);

        Order order = OrderConvertor.toEntity(orderDTO);

        orderRepository.save(order);
        return new ResponseEntity<>(HttpStatus.OK);
      }
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @Operation(summary = "Accept or reject book exchange request.")
  @PatchMapping("/exchange/{orderID}/{action}")
  public ResponseEntity<Void> exchangeResponse(@PathVariable long orderID,
                                               @PathVariable String action,
                                               @RequestHeader("Authorization") String token) {
    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      Optional<Order> order = orderRepository.findById(orderID);
      if (order.isPresent()) {
        Order orderObj = order.get();
        Order originalOrderObj = order.get();
        if (orderObj.getOwnerID() == response.getBody()) {
          if (!orderObj.getAction().equals(EXCHANGE) || !orderObj.getOrderStatus().equals(AWAITING_OWNER_RESPONSE)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }
          if (action.equalsIgnoreCase("accept")) {
            long prevOwnerID = orderObj.getOwnerID();
            long prevRecipientID = orderObj.getRecipientID();

            Optional<Book> ownerBook = bookRepository.findById(orderObj.getOwnerBookID());
            Optional<Book> recipientBook = bookRepository.findById(orderObj.getRecipientBookID());
            if (ownerBook.isPresent() && recipientBook.isPresent()) {
              Book ownerBookObj = ownerBook.get();
              Book recipientBookObj = recipientBook.get();

              ownerBookObj.setUserID(prevRecipientID);
              recipientBookObj.setUserID(prevOwnerID);

              bookRepository.saveAll(List.of(ownerBookObj, recipientBookObj));
            } else {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            orderObj.setOrderStatus(INITIATE_DELIVERY);
          } else if (action.equalsIgnoreCase("reject")) {
            orderObj.setOrderStatus(REQUEST_REJECTED);
          }
          orderRepository.save(originalOrderObj);
          return new ResponseEntity<>(HttpStatus.OK);
        }
      }
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @Operation(summary = "Accept or reject book lend request.")
  @PatchMapping("/lend/{orderID}/{action}")
  public ResponseEntity<Void> lendResponse(@PathVariable long orderID,
                                           @PathVariable String action,
                                           @RequestHeader("Authorization") String token) {
    ResponseEntity<Long> response = bookUtil.validateToken(token);
    if (response.getStatusCode() == HttpStatus.OK) {
      Optional<Order> order = orderRepository.findById(orderID);
      if (order.isPresent()) {
        Order orderObj = order.get();
        Order originalOrderObj = order.get();
        if (orderObj.getOwnerID() == response.getBody()) {
          if (!orderObj.getAction().equals(LEND) || !orderObj.getOrderStatus().equals(AWAITING_OWNER_RESPONSE)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }
          if (action.equalsIgnoreCase("accept")) {
            long prevRecipientID = orderObj.getRecipientID();

            Optional<Book> ownerBook = bookRepository.findById(orderObj.getOwnerBookID());
            if (ownerBook.isPresent()) {
              Book ownerBookObj = ownerBook.get();
              ownerBookObj.setUserID(prevRecipientID);

              bookRepository.save(ownerBookObj);
            } else {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            orderObj.setOrderStatus(INITIATE_DELIVERY);
          } else if (action.equalsIgnoreCase("reject")) {
            orderObj.setOrderStatus(REQUEST_REJECTED);
          }
          orderRepository.save(originalOrderObj);
          return new ResponseEntity<>(HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
      }
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}