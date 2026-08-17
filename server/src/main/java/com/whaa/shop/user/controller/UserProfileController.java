package com.whaa.shop.user.controller;

import com.whaa.shop.common.api.ApiResponse;import com.whaa.shop.common.security.CurrentUser;import com.whaa.shop.file.application.ObjectStorageService;import com.whaa.shop.user.application.UserProfileService;import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import java.time.LocalDate;

@RestController @RequestMapping("/api/v1/shop/profile")
public class UserProfileController {
 private final UserProfileService profiles;private final ObjectStorageService storage;
 public UserProfileController(UserProfileService profiles,ObjectStorageService storage){this.profiles=profiles;this.storage=storage;}
 @GetMapping ApiResponse<UserProfileService.ProfileView> get(){return ApiResponse.ok(profiles.get(CurrentUser.id()));}
 @PutMapping ApiResponse<UserProfileService.ProfileView> update(@Valid @RequestBody UpdateProfile r){return ApiResponse.ok(profiles.update(CurrentUser.id(),r.nickname(),r.bio(),r.gender(),r.birthday()));}
 @PostMapping("/avatar") ApiResponse<UserProfileService.ProfileView> avatar(@RequestParam MultipartFile file){var stored=storage.upload("avatars",file);if(!stored.contentType().startsWith("image/"))throw new com.whaa.shop.common.exception.BusinessException("头像必须是图片");return ApiResponse.ok(profiles.updateAvatar(CurrentUser.id(),stored.url()));}
 public record UpdateProfile(@NotBlank @Size(max=50)String nickname,@Size(max=300)String bio,@Pattern(regexp="MALE|FEMALE|UNSPECIFIED")String gender,@Past LocalDate birthday){}
}
