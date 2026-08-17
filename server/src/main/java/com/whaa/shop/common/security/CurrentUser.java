package com.whaa.shop.common.security;
import com.whaa.shop.common.exception.BusinessException; import org.springframework.security.core.context.SecurityContextHolder;
public final class CurrentUser {private CurrentUser(){} public static long id(){var a=SecurityContextHolder.getContext().getAuthentication();if(a==null||!a.isAuthenticated())throw new BusinessException("请先登录");try{return Long.parseLong(a.getName());}catch(Exception e){throw new BusinessException("登录状态无效");}}}

