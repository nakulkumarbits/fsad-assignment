package com.fsad.bookservice.enums;

public enum OrderStatus {

    AWAITING_OWNER_RESPONSE,

    INITIATE_DELIVERY,

    DELIVERY_IN_TRANSIT,

    DELIVERED,

    INITIATE_RETURN,

    RETURN_IN_TRANSIT,

    RETURNED,

    REQUEST_REJECTED
}