package com.fsad.bookservice.repository;

import com.fsad.bookservice.entities.Order;
import com.fsad.bookservice.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByRecipientIDAndOwnerBookIDAndOrderStatusIn(Long recipientID, Long ownerBookID, List<OrderStatus> ALLOWED_STATUS_LIST);

  Page<Order> findByRecipientID(Long recipientID, Pageable pageable);

  Page<Order> findByOwnerID(Long userID, Pageable pageable);
}
