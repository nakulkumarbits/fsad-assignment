package com.fsad.bookservice.enums;

public enum OrderStatus {

    AWAITING_OWNER_RESPONSE,

    AWAITING_RECIPIENT_RESPONSE,

    INITIATE_DELIVERY,

    DELIVERY_IN_TRANSIT,

    DELIVERED,

    INITIATE_RETURN,

    RETURN_IN_TRANSIT,

    RETURNED
}
