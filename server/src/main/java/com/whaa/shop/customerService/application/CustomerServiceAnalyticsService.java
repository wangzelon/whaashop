package com.whaa.shop.customerService.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceAnalyticsService {
    private final JdbcTemplate jdbc;

    public CustomerServiceAnalyticsService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Dashboard dashboard(int requestedDays) {
        int days = Math.max(1, Math.min(requestedDays, 90));
        String interval = days + " day";
        Long conversations = value("select count(*) from customer_service_conversation where started_at>=date_sub(now(),interval " + interval + ")");
        Long questions = value("select count(*) from customer_service_message where role='USER' and created_at>=date_sub(now(),interval " + interval + ")");
        Long users = value("select count(distinct user_id) from customer_service_conversation where user_id is not null and started_at>=date_sub(now(),interval " + interval + ")");
        Double averageTurns = jdbc.queryForObject("select coalesce(avg(message_count/2.0),0) from customer_service_conversation where started_at>=date_sub(now(),interval " + interval + ")", Double.class);
        List<Map<String, Object>> categories = jdbc.queryForList("select category,count(*) value from customer_service_message where role='USER' and created_at>=date_sub(now(),interval " + interval + ") group by category order by value desc");
        List<Map<String, Object>> trend = jdbc.queryForList("select date(created_at) day,count(*) questions from customer_service_message where role='USER' and created_at>=date_sub(current_date,interval " + (days - 1) + " day) group by date(created_at) order by day");
        List<Map<String, Object>> hotQuestions = jdbc.queryForList("select content question,count(*) frequency,max(created_at) lastAskedAt from customer_service_message where role='USER' and created_at>=date_sub(now(),interval " + interval + ") group by content order by frequency desc,lastAskedAt desc limit 10");
        return new Dashboard(conversations, questions, users, Math.round(averageTurns * 10) / 10.0, categories, trend, hotQuestions);
    }

    private long value(String sql) {
        Long result = jdbc.queryForObject(sql, Long.class);
        return result == null ? 0 : result;
    }

    public record Dashboard(long conversations, long questions, long users, double averageTurns,
                            List<Map<String, Object>> categories, List<Map<String, Object>> trend,
                            List<Map<String, Object>> hotQuestions) {}
}
