package com.whaa.shop.order.infrastructure; import com.baomidou.mybatisplus.core.mapper.BaseMapper; import com.whaa.shop.order.domain.OrderItem; import org.apache.ibatis.annotations.*;
public interface OrderItemMapper extends BaseMapper<OrderItem>{@Update("update order_item set reviewed=1 where id=#{id} and reviewed=0")int markReviewed(Long id);}

