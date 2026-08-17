package com.whaa.shop.common.config;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor; import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor; import com.baomidou.mybatisplus.annotation.DbType; import org.springframework.context.annotation.*;
@Configuration public class MybatisConfig {@Bean MybatisPlusInterceptor interceptor(){var i=new MybatisPlusInterceptor();i.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));return i;}}

