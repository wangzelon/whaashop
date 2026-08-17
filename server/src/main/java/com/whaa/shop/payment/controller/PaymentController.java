package com.whaa.shop.payment.controller;

import com.whaa.shop.common.api.ApiResponse;
import com.whaa.shop.common.security.CurrentUser;
import com.whaa.shop.order.application.OrderService;
import com.whaa.shop.order.domain.ShopOrder;
import com.whaa.shop.payment.application.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentGateway gateway;
    private final OrderService orders;
    private final PaymentRecordService records;

    public PaymentController(PaymentGateway g, OrderService o, PaymentRecordService r) {
        gateway = g;
        orders = o;
        records = r;
    }

    @PostMapping("/api/v1/shop/orders/{id}/payment")
    ApiResponse<PaymentGateway.PaymentRequestResult> create(@PathVariable Long id) {
        ShopOrder o = orders.owned(id, CurrentUser.id());
        records.pending(o.getId(), o.getTotalAmount());
        return ApiResponse.ok(gateway.create(o.getOrderNo(), o.getTotalAmount(), "橙选商城订单" + o.getOrderNo()));
    }

    @PostMapping("/api/v1/payment/alipay/notify")
    String notify(@RequestParam Map<String, String> p) {
        if (!gateway.verify(p) || !"TRADE_SUCCESS".equals(p.get("trade_status"))) return "failure";
        try {
            String no = p.get("out_trade_no");
            boolean paid = orders.markPaid(no, new java.math.BigDecimal(p.get("total_amount")));
            ShopOrder o = orders.findByNo(no);
            if (paid && o != null) records.paid(o.getId(), p.get("trade_no"));
            return paid ? "success" : "failure";
        } catch (Exception e) {
            log.error("Failed to process Alipay callback: outTradeNo={}, tradeNo={}", p.get("out_trade_no"), p.get("trade_no"), e);
            return "failure";
        }
    }
}
