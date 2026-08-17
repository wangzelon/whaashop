CREATE TABLE shop_user (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(30) NOT NULL UNIQUE, password_hash VARCHAR(100) NOT NULL,
 nickname VARCHAR(50) NOT NULL, role VARCHAR(16) NOT NULL, enabled BOOLEAN NOT NULL DEFAULT TRUE, created_at DATETIME NOT NULL
);
CREATE TABLE category (id BIGINT PRIMARY KEY AUTO_INCREMENT,parent_id BIGINT NULL,name VARCHAR(80) NOT NULL,sort_order INT NOT NULL DEFAULT 0,enabled BOOLEAN NOT NULL DEFAULT TRUE);
CREATE TABLE product (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,category_id BIGINT NOT NULL,name VARCHAR(150) NOT NULL,subtitle VARCHAR(255),main_image VARCHAR(500),
 detail_html LONGTEXT,min_price DECIMAL(12,2) NOT NULL DEFAULT 0,sales BIGINT NOT NULL DEFAULT 0,published BOOLEAN NOT NULL DEFAULT FALSE,created_at DATETIME NOT NULL,
 INDEX idx_product_category(category_id),INDEX idx_product_published(published)
);
CREATE TABLE product_sku (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,product_id BIGINT NOT NULL,spec_json JSON NOT NULL,price DECIMAL(12,2) NOT NULL,stock INT NOT NULL,enabled BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_sku_product FOREIGN KEY(product_id) REFERENCES product(id),INDEX idx_sku_product(product_id)
);
CREATE TABLE user_address (id BIGINT PRIMARY KEY AUTO_INCREMENT,user_id BIGINT NOT NULL,receiver_name VARCHAR(50) NOT NULL,phone VARCHAR(30) NOT NULL,province VARCHAR(50),city VARCHAR(50),district VARCHAR(50),detail VARCHAR(255) NOT NULL,is_default BOOLEAN NOT NULL DEFAULT FALSE,INDEX idx_address_user(user_id));
CREATE TABLE cart_item (id BIGINT PRIMARY KEY AUTO_INCREMENT,user_id BIGINT NOT NULL,sku_id BIGINT NOT NULL,quantity INT NOT NULL,checked BOOLEAN NOT NULL DEFAULT TRUE,UNIQUE KEY uk_cart_user_sku(user_id,sku_id));
CREATE TABLE shop_order (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,order_no VARCHAR(32) NOT NULL UNIQUE,user_id BIGINT NOT NULL,status VARCHAR(24) NOT NULL,total_amount DECIMAL(12,2) NOT NULL,
 receiver_name VARCHAR(50) NOT NULL,receiver_phone VARCHAR(30) NOT NULL,receiver_address VARCHAR(500) NOT NULL,paid_at DATETIME NULL,shipped_at DATETIME NULL,completed_at DATETIME NULL,
 created_at DATETIME NOT NULL,version INT NOT NULL DEFAULT 0,INDEX idx_order_user_created(user_id,created_at),INDEX idx_order_status(status)
);
CREATE TABLE order_item (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,order_id BIGINT NOT NULL,product_id BIGINT NOT NULL,sku_id BIGINT NOT NULL,product_name VARCHAR(150) NOT NULL,sku_spec VARCHAR(255),product_image VARCHAR(500),
 unit_price DECIMAL(12,2) NOT NULL,quantity INT NOT NULL,reviewed BOOLEAN NOT NULL DEFAULT FALSE,CONSTRAINT fk_item_order FOREIGN KEY(order_id) REFERENCES shop_order(id),INDEX idx_item_product(product_id)
);
CREATE TABLE product_review (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,order_item_id BIGINT NOT NULL,product_id BIGINT NOT NULL,user_id BIGINT NOT NULL,rating TINYINT NOT NULL,content VARCHAR(2000) NOT NULL,
 images_json JSON NOT NULL,append_count TINYINT NOT NULL DEFAULT 0,hidden BOOLEAN NOT NULL DEFAULT FALSE,created_at DATETIME NOT NULL,
 UNIQUE KEY uk_review_order_item(order_item_id),INDEX idx_review_product_created(product_id,created_at),CONSTRAINT chk_rating CHECK(rating BETWEEN 1 AND 5),CONSTRAINT chk_append_count CHECK(append_count BETWEEN 0 AND 3)
);
CREATE TABLE review_append (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,review_id BIGINT NOT NULL,sequence_no TINYINT NOT NULL,content VARCHAR(2000) NOT NULL,images_json JSON NOT NULL,created_at DATETIME NOT NULL,
 UNIQUE KEY uk_review_append_sequence(review_id,sequence_no),CONSTRAINT fk_append_review FOREIGN KEY(review_id) REFERENCES product_review(id),CONSTRAINT chk_append_sequence CHECK(sequence_no BETWEEN 1 AND 3)
);
CREATE TABLE payment_record (id BIGINT PRIMARY KEY AUTO_INCREMENT,order_id BIGINT NOT NULL,trade_no VARCHAR(64),provider VARCHAR(20) NOT NULL,status VARCHAR(20) NOT NULL,amount DECIMAL(12,2) NOT NULL,created_at DATETIME NOT NULL,updated_at DATETIME NOT NULL,UNIQUE KEY uk_payment_order_provider(order_id,provider));
CREATE TABLE knowledge_file (id BIGINT PRIMARY KEY AUTO_INCREMENT,file_name VARCHAR(255) NOT NULL,object_key VARCHAR(500) NOT NULL,mime_type VARCHAR(100),status VARCHAR(20) NOT NULL,error_message VARCHAR(1000),uploaded_by BIGINT NOT NULL,created_at DATETIME NOT NULL,updated_at DATETIME NOT NULL);
CREATE TABLE recommendation (id BIGINT PRIMARY KEY AUTO_INCREMENT,product_id BIGINT NOT NULL,position_code VARCHAR(40) NOT NULL,sort_order INT NOT NULL DEFAULT 0,enabled BOOLEAN NOT NULL DEFAULT TRUE,UNIQUE KEY uk_recommend_product_position(product_id,position_code));
CREATE TABLE flash_sale (id BIGINT PRIMARY KEY AUTO_INCREMENT,sku_id BIGINT NOT NULL,price DECIMAL(12,2) NOT NULL,stock INT NOT NULL,per_user_limit INT NOT NULL DEFAULT 1,start_at DATETIME NOT NULL,end_at DATETIME NOT NULL,enabled BOOLEAN NOT NULL DEFAULT FALSE);

