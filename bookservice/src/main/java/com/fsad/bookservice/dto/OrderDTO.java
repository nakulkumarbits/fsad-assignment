package com.fsad.bookservice.dto;

import com.fsad.bookservice.enums.Action;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
  private Long id;
  private Long ownerID;
  private Long ownerBookID;
  private Long recipientID;
  private Long recipientBookID;
  private Action action;
  private OrderStatus orderStatus;
  @Min(value = 1, message = "Duration cannot be less than 1.")
  private Integer duration;
  private DeliveryMode deliveryMode;

}
