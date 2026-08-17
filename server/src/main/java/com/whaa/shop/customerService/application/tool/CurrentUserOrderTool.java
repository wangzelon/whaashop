package com.whaa.shop.customerService.application.tool;

import com.whaa.shop.order.application.OrderService;
import com.whaa.shop.order.domain.OrderStatus;
import com.whaa.shop.order.domain.ShopOrder;
import org.springframework.ai.tool.annotation.Tool;
import java.util.List;

/** The user id is bound by the application and can never be supplied by the model. */
public class CurrentUserOrderTool {
    private static final int MAX_ORDERS = 10;
    private final OrderService orderService;
    private final long userId;
    public CurrentUserOrderTool(OrderService orderService, long userId) { this.orderService = orderService; this.userId = userId; }

    @Tool(description = "查询当前登录用户最近的订单信息。当用户询问我的订单、订单状态、发货、物流、收货或订单号时调用。")
    public String queryCurrentUserOrders() {
        List<ShopOrder> orders = orderService.mine(userId).stream().limit(MAX_ORDERS).toList();
        if (orders.isEmpty()) return "当前登录用户暂无订单。";
        return orders.stream().map(this::describe).reduce((left, right) -> left + "\n" + right).orElseThrow();
    }
    private String describe(ShopOrder order) {
        return "订单号=%s，状态=%s，金额=%s，创建时间=%s，发货时间=%s，完成时间=%s".formatted(
                order.getOrderNo(), statusName(order.getStatus()), order.getTotalAmount(), safe(order.getCreatedAt()),
                safe(order.getShippedAt()), safe(order.getCompletedAt()));
    }
    private String statusName(OrderStatus status) {
        if (status == null) return "未知";
        return switch (status) {
            case PENDING_PAYMENT -> "待支付"; case PAID -> "已支付，待发货"; case SHIPPED -> "已发货，待收货";
            case COMPLETED -> "已完成"; case CLOSED -> "已关闭";
        };
    }
    private String safe(Object value) { return value == null ? "暂无" : value.toString(); }
}
