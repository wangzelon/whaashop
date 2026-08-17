package com.whaa.shop.customerService.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class WttrWeatherClient {
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();

    public WttrWeatherClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String currentAndToday(String city) throws Exception {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8).replace("+", "%20");
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://wttr.in/" + encodedCity + "?format=j1&lang=zh"))
                .timeout(Duration.ofSeconds(8)).header("Accept", "application/json").header("User-Agent", "WhaaShop/1.0").GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) throw new IllegalStateException("wttr.in returned HTTP " + response.statusCode());
        return summarize(city, objectMapper.readTree(response.body()));
    }

    private String summarize(String requestedCity, JsonNode root) {
        JsonNode current = root.path("current_condition").path(0);
        JsonNode today = root.path("weather").path(0);
        if (current.isMissingNode() || current.isEmpty()) throw new IllegalStateException("wttr.in returned no weather data");
        String resolved = root.path("nearest_area").path(0).path("areaName").path(0).path("value").asText(requestedCity);
        String description = current.path("lang_zh").path(0).path("value").asText();
        if (description.isBlank()) description = current.path("weatherDesc").path(0).path("value").asText("未知");
        return "地点=%s，当前天气=%s，气温=%s°C，体感=%s°C，湿度=%s%%，风速=%s公里/小时，今日最高=%s°C，今日最低=%s°C，降水量=%s毫米"
                .formatted(resolved, description, current.path("temp_C").asText("未知"),
                        current.path("FeelsLikeC").asText("未知"), current.path("humidity").asText("未知"),
                        current.path("windspeedKmph").asText("未知"), today.path("maxtempC").asText("未知"),
                        today.path("mintempC").asText("未知"), current.path("precipMM").asText("未知"));
    }
}
