package com.whaa.shop.order.application;

import com.whaa.shop.common.exception.BusinessException;
import com.whaa.shop.order.domain.*;
import com.whaa.shop.order.infrastructure.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
 @Test void shippedOrderCanBeConfirmedAndIsIdempotent(){OrderMapper orders=mock(OrderMapper.class);OrderItemMapper items=mock(OrderItemMapper.class);ShopOrder shipped=order(7L,3L,OrderStatus.SHIPPED);when(orders.selectById(7L)).thenReturn(shipped);when(orders.transition(eq(7L),eq("SHIPPED"),eq("COMPLETED"),any())).thenReturn(1);new OrderService(orders,items).confirm(7L,3L);verify(orders).transition(eq(7L),eq("SHIPPED"),eq("COMPLETED"),any());shipped.setStatus(OrderStatus.COMPLETED);new OrderService(orders,items).confirm(7L,3L);}
 @Test void paidOrderCannotBeConfirmed(){OrderMapper orders=mock(OrderMapper.class);when(orders.selectById(1L)).thenReturn(order(1L,2L,OrderStatus.PAID));assertThrows(BusinessException.class,()->new OrderService(orders,mock(OrderItemMapper.class)).confirm(1L,2L));}
 @Test void anotherUserCannotConfirm(){OrderMapper orders=mock(OrderMapper.class);when(orders.selectById(1L)).thenReturn(order(1L,2L,OrderStatus.SHIPPED));assertThrows(BusinessException.class,()->new OrderService(orders,mock(OrderItemMapper.class)).confirm(1L,9L));}
 private ShopOrder order(long id,long user,OrderStatus status){ShopOrder o=new ShopOrder();o.setId(id);o.setUserId(user);o.setStatus(status);return o;}
}

