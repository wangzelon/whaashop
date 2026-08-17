package com.whaa.shop.product.controller; import com.whaa.shop.common.api.*; import com.whaa.shop.product.application.ProductService; import com.whaa.shop.product.domain.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController public class ProductController {private final ProductService service;public ProductController(ProductService s){service=s;}
 @GetMapping("/api/v1/shop/products") ApiResponse<PageResponse<Product>> page(@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="20")long size,@RequestParam(required=false)String keyword,@RequestParam(required=false)Long categoryId){return ApiResponse.ok(service.page(page,size,keyword,categoryId));}
 @GetMapping("/api/v1/shop/products/weekly-picks") ApiResponse<List<Product>> weeklyPicks(@RequestParam(required=false)Long categoryId){return ApiResponse.ok(service.weeklyPicks(categoryId));}
 @GetMapping("/api/v1/shop/products/hot-picks") ApiResponse<List<Product>> hotPicks(){return ApiResponse.ok(service.hotPicks());}
 @GetMapping("/api/v1/shop/products/new-arrivals") ApiResponse<List<Product>> newArrivals(){return ApiResponse.ok(service.newArrivals());}
 @GetMapping("/api/v1/shop/products/{id}") ApiResponse<ProductView> detail(@PathVariable Long id){return ApiResponse.ok(new ProductView(service.detail(id),service.skus(id)));}
 @GetMapping("/api/v1/admin/products") ApiResponse<PageResponse<Product>> adminPage(@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="20")long size,@RequestParam(required=false)String keyword){return ApiResponse.ok(service.adminPage(page,size,keyword));}
 @PostMapping("/api/v1/admin/products") ApiResponse<Product> save(@RequestBody Product p){return ApiResponse.ok(service.save(p));}
 @PutMapping("/api/v1/admin/products/{id}") ApiResponse<Product> update(@PathVariable Long id,@RequestBody Product p){p.setId(id);return ApiResponse.ok(service.save(p));}
 @PostMapping("/api/v1/admin/skus") ApiResponse<Sku> saveSku(@RequestBody Sku sku){return ApiResponse.ok(service.saveSku(sku));}
 record ProductView(Product product,List<Sku> skus){}
}
