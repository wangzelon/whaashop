package com.whaa.shop.catalog.domain;
import com.baomidou.mybatisplus.annotation.*;
@TableName("category") public class Category {@TableId(type=IdType.AUTO)private Long id;private Long parentId;private String name;private Integer sortOrder;private Boolean enabled;
 public Long getId(){return id;}public void setId(Long v){id=v;}public Long getParentId(){return parentId;}public void setParentId(Long v){parentId=v;}public String getName(){return name;}public void setName(String v){name=v;}public Integer getSortOrder(){return sortOrder;}public void setSortOrder(Integer v){sortOrder=v;}public Boolean getEnabled(){return enabled;}public void setEnabled(Boolean v){enabled=v;}}
