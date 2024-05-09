package com.fsad.bookservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStateDTO {
  @Min(value = 0, message = "Order Id cannot be negative or blank.")
  private Long orderId;
  @NotBlank(message = "from state cannot be blank.")
  private String fromState;
  @NotBlank(message = "to state cannot be blank.")
  private String toState;
}
