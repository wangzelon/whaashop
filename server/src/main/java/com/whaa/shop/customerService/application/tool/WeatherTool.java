package com.whaa.shop.customerService.application.tool;

import com.whaa.shop.customerService.infrastructure.WttrWeatherClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class WeatherTool {
    private static final Logger log = LoggerFactory.getLogger(WeatherTool.class);
    private final WttrWeatherClient client;

    public WeatherTool(WttrWeatherClient client) {
        this.client = client;
    }

    @Tool(description = "查询指定城市当前天气和今日气温。用户询问某个城市的天气、气温、湿度或是否下雨时调用。")
    public String queryCityWeather(@ToolParam(description = "城市名称，例如北京、上海、深圳或London") String city) {
        String normalized = city == null ? "" : city.trim();
        if (normalized.isEmpty() || normalized.length() > 80 || normalized.chars().anyMatch(Character::isISOControl)) {
            return "城市名称无效，请用户提供一个明确的城市名称。";
        }
        try {
            return client.currentAndToday(normalized);
        } catch (Exception e) {
            log.warn("Failed to query wttr.in weather: city={}, reason={}", normalized, e.getMessage());
            return "天气服务暂时不可用，请稍后再试。";
        }
    }
}
