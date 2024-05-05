package com.fsad.bookservice.entities;

import com.fsad.bookservice.enums.Action;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderDetail")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Data
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long ownerID;

  @Column(nullable = false)
  private Long ownerBookID;

  @Column(nullable = false)
  private Long recipientID;

  private Long recipientBookID;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Action action;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OrderStatus orderStatus;

  private Integer duration;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private DeliveryMode deliveryMode;

  @Column(nullable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(nullable = false)
  @LastModifiedDate
  private LocalDateTime modifiedDate;

  @Version
  private Long version;
}
