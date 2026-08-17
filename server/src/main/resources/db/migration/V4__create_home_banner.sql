CREATE TABLE home_banner (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 title VARCHAR(120) NOT NULL,
 subtitle VARCHAR(300),
 image_url VARCHAR(500) NOT NULL,
 target_url VARCHAR(500),
 button_text VARCHAR(30) NOT NULL DEFAULT '立即选购',
 sort_order INT NOT NULL DEFAULT 0,
 enabled BOOLEAN NOT NULL DEFAULT TRUE,
 created_at DATETIME NOT NULL,
 updated_at DATETIME NOT NULL,
 INDEX idx_banner_enabled_sort(enabled,sort_order)
);

INSERT INTO home_banner(title,subtitle,image_url,target_url,button_text,sort_order,enabled,created_at,updated_at) VALUES
 ('智能运动手表','全天候健康监测，让运动与生活保持从容节奏','https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=1600','/products/10001','立即了解',10,TRUE,NOW(),NOW()),
 ('无线机械键盘','三模连接与舒适手感，为桌面注入效率灵感','https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=1600','/products/10002','探索新品',20,TRUE,NOW(),NOW()),
 ('每日坚果礼盒','科学搭配独立包装，把每日营养轻松带在身边','https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=1600','/products/10003','现在选购',30,TRUE,NOW(),NOW());

