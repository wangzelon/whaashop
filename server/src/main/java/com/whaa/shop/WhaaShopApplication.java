package com.whaa.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableAsync
@MapperScan("com.whaa.shop.**.infrastructure")
@SpringBootApplication
public class WhaaShopApplication {
    private static final Logger log = LoggerFactory.getLogger(WhaaShopApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WhaaShopApplication.class, args);
        printAccessUrls(context.getEnvironment());
    }

    private static void printAccessUrls(Environment environment) {
        String protocol = environment.getProperty("server.ssl.enabled", Boolean.class, false) ? "https" : "http";
        String port = environment.getProperty("local.server.port",
                environment.getProperty("server.port", "8080"));
        String contextPath = normalizePath(environment.getProperty("server.servlet.context-path", ""));
        String apiPath = contextPath + "/api/v1";
        String swaggerPath = contextPath + environment.getProperty("springdoc.swagger-ui.path", "/swagger-ui/index.html");
        String knife4jPath = contextPath + "/doc.html";
        String openApiPath = contextPath + environment.getProperty("springdoc.api-docs.path", "/v3/api-docs");
        String localAddress = protocol + "://localhost:" + port;
        String networkAddress = protocol + "://" + localHostAddress() + ":" + port;

        log.info("\n----------------------------------------------------------\n" +
                        "  WhaaShop 后端服务启动成功\n" +
                        "  本机访问地址: {}{}\n" +
                        "  局域网访问地址: {}{}\n" +
                        "  Knife4j 接口文档: {}{}\n" +
                        "----------------------------------------------------------",
                localAddress, contextPath,
                networkAddress, contextPath,
                localAddress, knife4jPath);
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) return "";
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private static String localHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.debug("Unable to resolve local network address", e);
            return "127.0.0.1";
        }
    }
}
