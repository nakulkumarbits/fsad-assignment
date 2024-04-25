package com.fsad.bookservice.entities;

import com.fsad.bookservice.enums.Action;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrderHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@ToString
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private long ownerBookID;

    @Column(nullable = false)
    private long recipientID;

    private long recipientBookID;

    @Column(nullable = false)
    private Action action;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private DeliveryMode deliveryMode;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedTime;

    public OrderHistory(Long ownerID, long ownerBookID, Long recipientID, long recipientBookID, Action action, int duration, DeliveryMode deliveryMode) {
        this.ownerID = ownerID;
        this.ownerBookID = ownerBookID;
        this.recipientID = recipientID;
        this.recipientBookID = recipientBookID;
        this.action = action;
        this.orderStatus = OrderStatus.AWAITING_OWNER_RESPONSE;
        this.duration = duration;
        this.deliveryMode = deliveryMode;
    }
}
