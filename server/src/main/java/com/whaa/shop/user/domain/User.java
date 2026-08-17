package com.whaa.shop.user.domain;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDate; import java.time.LocalDateTime;

@TableName("shop_user")
public class User {
 @TableId(type=IdType.AUTO) private Long id; private String username; private String email; private String passwordHash; private String nickname; private String avatarUrl; private String bio; private String gender; private LocalDate birthday; private String role; private Boolean enabled; private LocalDateTime createdAt;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;} public String getNickname(){return nickname;} public void setNickname(String v){nickname=v;} public String getAvatarUrl(){return avatarUrl;} public void setAvatarUrl(String v){avatarUrl=v;} public String getBio(){return bio;} public void setBio(String v){bio=v;} public String getGender(){return gender;} public void setGender(String v){gender=v;} public LocalDate getBirthday(){return birthday;} public void setBirthday(LocalDate v){birthday=v;} public String getRole(){return role;} public void setRole(String v){role=v;} public Boolean getEnabled(){return enabled;} public void setEnabled(Boolean v){enabled=v;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
