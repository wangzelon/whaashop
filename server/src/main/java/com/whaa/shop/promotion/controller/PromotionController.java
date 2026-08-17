package com.whaa.shop.promotion.controller;
import com.whaa.shop.common.api.*;import com.whaa.shop.promotion.application.PromotionService;import com.whaa.shop.promotion.application.PromotionService.*;import com.whaa.shop.promotion.domain.FlashSale;import com.whaa.shop.recommendation.domain.Recommendation;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController public class PromotionController {private final PromotionService service;public PromotionController(PromotionService s){service=s;}
 @GetMapping("/api/v1/admin/recommendations")ApiResponse<PageResponse<RecommendationView>> recommendations(@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="10")long size){return ApiResponse.ok(service.recommendations(page,size));}
 @PostMapping("/api/v1/admin/recommendations")ApiResponse<Recommendation> save(@RequestBody Recommendation v){return ApiResponse.ok(service.save(v));}@DeleteMapping("/api/v1/admin/recommendations/{id}")ApiResponse<Void> deleteRec(@PathVariable Long id){service.deleteRecommendation(id);return ApiResponse.ok();}
 @GetMapping("/api/v1/admin/flash-sales")ApiResponse<PageResponse<FlashSaleView>> flashes(@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="10")long size){return ApiResponse.ok(service.flashes(page,size));}
 @PostMapping("/api/v1/admin/flash-sales")ApiResponse<FlashSale> save(@RequestBody FlashSale v){return ApiResponse.ok(service.save(v));}@DeleteMapping("/api/v1/admin/flash-sales/{id}")ApiResponse<Void> deleteFlash(@PathVariable Long id){service.deleteFlash(id);return ApiResponse.ok();}
 @GetMapping("/api/v1/shop/promotions/recommendations")ApiResponse<List<RecommendationView>> shopRecommendations(@RequestParam(defaultValue="HOME_FEATURED")String position){return ApiResponse.ok(service.shopRecommendations(position));}
 @GetMapping("/api/v1/shop/promotions/flash-sales")ApiResponse<List<FlashSaleView>> shopFlashes(){return ApiResponse.ok(service.activeFlashes());}
}
