CREATE TABLE customer_service_conversation (
    id VARCHAR(96) PRIMARY KEY,
    user_id BIGINT NULL,
    channel VARCHAR(20) NOT NULL DEFAULT 'SHOP_WEB',
    started_at DATETIME NOT NULL,
    last_active_at DATETIME NOT NULL,
    message_count INT NOT NULL DEFAULT 0,
    INDEX idx_cs_conversation_user (user_id, last_active_at),
    INDEX idx_cs_conversation_active (last_active_at)
);

CREATE TABLE customer_service_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id VARCHAR(96) NOT NULL,
    sequence_no INT NOT NULL,
    role VARCHAR(16) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(32) NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_cs_message_sequence (conversation_id, sequence_no),
    INDEX idx_cs_message_created (created_at),
    INDEX idx_cs_message_category (category, created_at),
    CONSTRAINT fk_cs_message_conversation FOREIGN KEY (conversation_id)
        REFERENCES customer_service_conversation(id) ON DELETE CASCADE
);
