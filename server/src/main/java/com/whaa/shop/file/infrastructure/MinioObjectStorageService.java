package com.whaa.shop.file.infrastructure;

import com.whaa.shop.common.exception.BusinessException;
import com.whaa.shop.file.application.ObjectStorageService;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MinioObjectStorageService implements ObjectStorageService {
    private static final Logger log = LoggerFactory.getLogger(MinioObjectStorageService.class);
    private final MinioClient client;
    private final String bucket, publicUrl;

    public MinioObjectStorageService(@Value("${whaashop.minio.endpoint}") String endpoint, @Value("${whaashop.minio.access-key}") String access, @Value("${whaashop.minio.secret-key}") String secret, @Value("${whaashop.minio.bucket}") String bucket, @Value("${whaashop.files.public-base-url}") String publicUrl) {
        this.client = MinioClient.builder().endpoint(endpoint).credentials(access, secret).build();
        this.bucket = bucket;
        this.publicUrl = publicUrl.replaceAll("/+$", "");
    }

    public StoredObject upload(String prefix, MultipartFile f) {
        if (f.isEmpty() || f.getSize() > 10 * 1024 * 1024) throw new BusinessException("文件为空或超过10MB");
        String ct = Optional.ofNullable(f.getContentType()).orElse("application/octet-stream");
        Set<String> allowed = Set.of("image/jpeg", "image/png", "image/webp", "application/pdf", "text/plain", "text/markdown", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        if (!allowed.contains(ct)) throw new BusinessException("不支持的文件类型");
        String ext = Optional.ofNullable(f.getOriginalFilename()).filter(n -> n.contains(".")).map(n -> n.substring(n.lastIndexOf('.'))).orElse("");
        String key = prefix + "/" + UUID.randomUUID() + ext;
        try {
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()))
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            client.putObject(PutObjectArgs.builder().bucket(bucket).object(key).stream(f.getInputStream(), f.getSize(), -1).contentType(ct).build());
            return new StoredObject(key, publicUrl + "/" + key, ct, f.getSize());
        } catch (Exception e) {
            log.error("MinIO upload failed: bucket={}, key={}, contentType={}, size={}", bucket, key, ct, f.getSize(), e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    public java.io.InputStream read(String key) {
        try {
            return client.getObject(GetObjectArgs.builder().bucket(bucket).object(key).build());
        } catch (Exception e) {
            log.error("MinIO read failed: bucket={}, key={}", bucket, key, e);
            throw new BusinessException("读取文件失败: " + e.getMessage());
        }
    }

    public void delete(String key) {
        try {
            client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());
        } catch (Exception e) {
            log.error("MinIO delete failed: bucket={}, key={}", bucket, key, e);
            throw new BusinessException("删除文件失败: " + e.getMessage());
        }
    }
}
