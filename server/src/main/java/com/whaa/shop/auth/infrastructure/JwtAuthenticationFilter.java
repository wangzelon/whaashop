package com.whaa.shop.auth.infrastructure;
import com.whaa.shop.auth.application.JwtService; import io.jsonwebtoken.Claims; import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import java.io.IOException; import java.util.List;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private static final Logger log=LoggerFactory.getLogger(JwtAuthenticationFilter.class);
 private final JwtService jwt; public JwtAuthenticationFilter(JwtService jwt){this.jwt=jwt;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String h=req.getHeader("Authorization");if(h!=null&&h.startsWith("Bearer "))try{Claims c=jwt.parse(h.substring(7));var a=new UsernamePasswordAuthenticationToken(c.getSubject(),null,List.of(new SimpleGrantedAuthority("ROLE_"+c.get("role",String.class))));SecurityContextHolder.getContext().setAuthentication(a);}catch(Exception e){log.debug("JWT authentication rejected: method={}, uri={}, reason={}",req.getMethod(),req.getRequestURI(),e.getMessage());}chain.doFilter(req,res);}
}
