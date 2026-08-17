package com.whaa.shop.payment.application;import java.math.BigDecimal;import java.util.Map;public interface PaymentGateway {PaymentRequestResult create(String orderNo,BigDecimal amount,String subject);boolean verify(Map<String,String> callback);record PaymentRequestResult(String provider,String redirectUrl,String formHtml){}}

