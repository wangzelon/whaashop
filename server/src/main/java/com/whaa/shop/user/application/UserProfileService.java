package com.whaa.shop.user.application;

import com.whaa.shop.common.exception.BusinessException;
import com.whaa.shop.user.domain.User;
import com.whaa.shop.user.infrastructure.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class UserProfileService {
 private final UserMapper users;
 private final String publicFileBaseUrl;
 public UserProfileService(UserMapper users,@Value("${whaashop.files.public-base-url}")String publicFileBaseUrl){this.users=users;this.publicFileBaseUrl=publicFileBaseUrl.replaceAll("/+$","");}
 public ProfileView get(long userId){return view(require(userId));}
 @Transactional public ProfileView update(long userId,String nickname,String bio,String gender,LocalDate birthday){User u=require(userId);u.setNickname(nickname.trim());u.setBio(bio==null?null:bio.trim());u.setGender(normalizeGender(gender));u.setBirthday(birthday);users.updateById(u);return view(u);}
 @Transactional public ProfileView updateAvatar(long userId,String avatarUrl){if(avatarUrl==null||avatarUrl.isBlank()||avatarUrl.length()>500)throw new BusinessException("头像地址无效");User u=require(userId);u.setAvatarUrl(avatarUrl);users.updateById(u);return view(u);}
 private User require(long id){User u=users.selectById(id);if(u==null||!Boolean.TRUE.equals(u.getEnabled()))throw new BusinessException("用户不存在或已停用");return u;}
 private String normalizeGender(String value){String v=value==null?"UNSPECIFIED":value.toUpperCase();if(!v.equals("MALE")&&!v.equals("FEMALE")&&!v.equals("UNSPECIFIED"))throw new BusinessException("性别字段无效");return v;}
 private ProfileView view(User u){return new ProfileView(u.getId(),u.getUsername(),u.getNickname(),publicAvatarUrl(u.getAvatarUrl()),u.getBio(),u.getGender(),u.getBirthday(),u.getRole(),u.getCreatedAt());}
 private String publicAvatarUrl(String value){if(value==null||value.isBlank())return value;int start=value.indexOf("/avatars/");return start<0?value:publicFileBaseUrl+value.substring(start);}
 public record ProfileView(Long id,String username,String nickname,String avatarUrl,String bio,String gender,LocalDate birthday,String role,java.time.LocalDateTime createdAt){}
}
