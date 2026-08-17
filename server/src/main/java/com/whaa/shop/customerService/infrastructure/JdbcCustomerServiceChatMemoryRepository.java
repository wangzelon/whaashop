package com.whaa.shop.customerService.infrastructure;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcCustomerServiceChatMemoryRepository implements ChatMemoryRepository {
    private final JdbcTemplate jdbc;

    public JdbcCustomerServiceChatMemoryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<String> findConversationIds() {
        return jdbc.queryForList("select id from customer_service_conversation order by last_active_at desc", String.class);
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return jdbc.query("select role, content from customer_service_message where conversation_id=? order by sequence_no",
                (rs, rowNum) -> toMessage(rs.getString("role"), rs.getString("content")), conversationId);
    }

    @Override
    @Transactional
    public void saveAll(String conversationId, List<Message> messages) {
        jdbc.update("delete from customer_service_message where conversation_id=?", conversationId);
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            String role = message.getMessageType().getValue().toUpperCase();
            String category = message.getMessageType() == MessageType.USER ? classify(message.getText()) : null;
            jdbc.update("insert into customer_service_message(conversation_id,sequence_no,role,content,category,created_at) values(?,?,?,?,?,?)",
                    conversationId, i + 1, role, message.getText(), category, Timestamp.valueOf(now));
        }
        jdbc.update("update customer_service_conversation set message_count=?,last_active_at=? where id=?",
                messages.size(), Timestamp.valueOf(now), conversationId);
    }

    @Override
    @Transactional
    public void deleteByConversationId(String conversationId) {
        jdbc.update("delete from customer_service_conversation where id=?", conversationId);
    }

    public void ensureConversation(String conversationId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        jdbc.update("insert into customer_service_conversation(id,user_id,channel,started_at,last_active_at,message_count) " +
                        "values(?,?,'SHOP_WEB',?,?,0) on duplicate key update last_active_at=values(last_active_at)",
                conversationId, userId, Timestamp.valueOf(now), Timestamp.valueOf(now));
    }

    private Message toMessage(String role, String content) {
        return switch (role) {
            case "ASSISTANT" -> new AssistantMessage(content);
            case "SYSTEM" -> new SystemMessage(content);
            default -> new UserMessage(content);
        };
    }

    private String classify(String text) {
        if (contains(text, "订单", "下单", "取消")) return "ORDER";
        if (contains(text, "物流", "快递", "发货", "收货", "到货", "签收")) return "LOGISTICS";
        if (contains(text, "退款", "退货", "换货", "售后")) return "AFTER_SALES";
        if (contains(text, "支付", "付款", "余额", "扣款")) return "PAYMENT";
        if (contains(text, "商品", "规格", "库存", "价格", "优惠")) return "PRODUCT";
        if (contains(text, "地址", "账号", "登录", "密码")) return "ACCOUNT";
        return "OTHER";
    }

    private boolean contains(String text, String... words) {
        if (text == null) return false;
        for (String word : words) if (text.contains(word)) return true;
        return false;
    }
}
