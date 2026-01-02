package com.example.ordertracker.util;

import com.example.ordertracker.model.OrderStatus;
import java.util.List;

public final class OrderStatusGenerator {

    private OrderStatusGenerator() {
    }

    public static List<OrderStatus> progression() {
        return List.of(
                OrderStatus.CREATED,
                OrderStatus.CONFIRMED,
                OrderStatus.PACKED,
                OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED);
    }

    public static List<OrderStatus> progressionFrom(OrderStatus current) {
        List<OrderStatus> all = progression();
        if (current == null) {
            return all;
        }
        int idx = all.indexOf(current);
        if (idx < 0) {
            return all;
        }
        return all.subList(idx, all.size());
    }
}
