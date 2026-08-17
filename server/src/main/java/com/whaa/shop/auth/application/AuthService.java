package com.whaa.shop.auth.application;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaa.shop.common.exception.BusinessException;
import com.whaa.shop.user.domain.User;
import com.whaa.shop.user.infrastructure.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;import java.util.Locale;

@Service
public class AuthService {
 private final UserMapper users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserMapper users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 @Transactional public TokenView register(String username,String email,String password,String nickname){String account=username.trim(),mail=email.trim().toLowerCase(Locale.ROOT);if(users.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername,account))>0)throw new BusinessException("用户名已存在");if(users.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail,mail))>0)throw new BusinessException("邮箱已被注册");User u=new User();u.setUsername(account);u.setEmail(mail);u.setPasswordHash(encoder.encode(password));u.setNickname(nickname.trim());u.setRole("USER");u.setEnabled(true);u.setCreatedAt(LocalDateTime.now());users.insert(u);return token(u);}
 public TokenView login(String username,String password){User u=users.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,username));if(u==null||!Boolean.TRUE.equals(u.getEnabled())||!encoder.matches(password,u.getPasswordHash()))throw new BusinessException("用户名或密码错误");return token(u);}
 private TokenView token(User u){return new TokenView(jwt.issue(u.getId(),u.getUsername(),u.getRole()),u.getId(),u.getUsername(),u.getNickname(),u.getRole());}
 public record TokenView(String token,Long userId,String username,String nickname,String role){}
}
