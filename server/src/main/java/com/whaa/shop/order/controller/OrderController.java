package com.whaa.shop.order.controller; import com.whaa.shop.common.api.ApiResponse; import com.whaa.shop.common.api.PageResponse; import com.whaa.shop.common.security.CurrentUser; import com.whaa.shop.order.application.*; import com.whaa.shop.order.domain.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController public class OrderController {private final OrderService service;private final CheckoutService checkout;public OrderController(OrderService s,CheckoutService c){service=s;checkout=c;}
 @GetMapping("/api/v1/shop/orders")ApiResponse<List<ShopOrder>> mine(){return ApiResponse.ok(service.mine(CurrentUser.id()));}
 @PostMapping("/api/v1/shop/orders")ApiResponse<ShopOrder> create(@RequestBody Checkout r){return ApiResponse.ok(checkout.create(CurrentUser.id(),r.cartIds,r.receiverName,r.receiverPhone,r.receiverAddress));}
 @PostMapping("/api/v1/shop/orders/buy-now")ApiResponse<ShopOrder> buyNow(@RequestBody DirectCheckout r){return ApiResponse.ok(checkout.buyNow(CurrentUser.id(),r.skuId,r.quantity,r.receiverName,r.receiverPhone,r.receiverAddress));}
 @GetMapping("/api/v1/admin/orders")ApiResponse<PageResponse<ShopOrder>> admin(@RequestParam(required=false)OrderStatus status,@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="20")long size){return ApiResponse.ok(service.adminPage(status,page,size));}
 @GetMapping("/api/v1/shop/orders/{id}/items")ApiResponse<List<OrderItem>> items(@PathVariable Long id){return ApiResponse.ok(service.items(id,CurrentUser.id()));}
 @PostMapping("/api/v1/shop/orders/{id}/confirm")ApiResponse<Void> confirm(@PathVariable Long id){service.confirm(id,CurrentUser.id());return ApiResponse.ok();}
 @PostMapping("/api/v1/admin/orders/{id}/ship")ApiResponse<Void> ship(@PathVariable Long id){service.ship(id);return ApiResponse.ok();}
 record Checkout(List<Long> cartIds,String receiverName,String receiverPhone,String receiverAddress){}record DirectCheckout(Long skuId,int quantity,String receiverName,String receiverPhone,String receiverAddress){}
}
