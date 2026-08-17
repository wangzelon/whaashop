package com.whaa.shop.product.domain;
import com.baomidou.mybatisplus.annotation.*; import java.math.BigDecimal;
@TableName("product_sku") public class Sku { @TableId(type=IdType.AUTO) private Long id; private Long productId; private String specJson; private BigDecimal price; private Integer stock; private Boolean enabled;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;} public String getSpecJson(){return specJson;} public void setSpecJson(String v){specJson=v;} public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;} public Integer getStock(){return stock;} public void setStock(Integer v){stock=v;} public Boolean getEnabled(){return enabled;} public void setEnabled(Boolean v){enabled=v;}}

