CREATE TABLE system_role (
 id BIGINT PRIMARY KEY, role_code VARCHAR(32) NOT NULL UNIQUE, role_name VARCHAR(50) NOT NULL,
 description VARCHAR(255), enabled BOOLEAN NOT NULL DEFAULT TRUE, created_at DATETIME NOT NULL
);

INSERT INTO system_role(id,role_code,role_name,description,enabled,created_at) VALUES
 (1,'ADMIN','管理员','商城运营管理角色',TRUE,NOW()),
 (2,'USER','普通用户','商城购物用户角色',TRUE,NOW());

-- 演示账号密码均为 password，非本地环境必须修改或删除。
INSERT IGNORE INTO shop_user(id,username,password_hash,nickname,avatar_url,bio,gender,birthday,role,enabled,created_at) VALUES
 (900001,'admin','$2a$10$0nYazucnGAq59EP8MuOhwe7RH4T0aWu5CNDFc6GrNrd/UGtlEKdoa','商城管理员',NULL,'负责商品与订单运营','UNSPECIFIED',NULL,'ADMIN',TRUE,NOW()),
 (900002,'demo','$2a$10$0nYazucnGAq59EP8MuOhwe7RH4T0aWu5CNDFc6GrNrd/UGtlEKdoa','橙子同学',NULL,'热爱发现生活好物','UNSPECIFIED','1998-08-08','USER',TRUE,NOW());

INSERT IGNORE INTO category(id,parent_id,name,sort_order,enabled) VALUES
 (1001,NULL,'数码家电',10,TRUE),(1002,NULL,'服饰鞋包',20,TRUE),(1003,NULL,'食品生鲜',30,TRUE),(1004,NULL,'家居生活',40,TRUE),
 (1101,1001,'智能设备',11,TRUE),(1102,1001,'电脑办公',12,TRUE),(1201,1002,'潮流服饰',21,TRUE),(1301,1003,'休闲零食',31,TRUE),(1401,1004,'居家日用',41,TRUE);

INSERT IGNORE INTO product(id,category_id,name,subtitle,main_image,detail_html,min_price,sales,published,created_at) VALUES
 (10001,1101,'智能运动手表','全天候健康监测，轻盈长续航','https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800','<h2>轻盈智能，陪伴每一天</h2><p>支持运动记录、睡眠分析与消息提醒，适合日常通勤和运动场景。</p>',399.00,128,TRUE,NOW()),
 (10002,1102,'无线机械键盘','三模连接，舒适办公与游戏体验','https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800','<h2>桌面效率新搭档</h2><p>紧凑布局、热插拔轴体，支持蓝牙、2.4G 与有线连接。</p>',269.00,86,TRUE,NOW()),
 (10003,1301,'每日坚果礼盒','科学搭配，独立小包装','https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=800','<h2>每日营养补给</h2><p>严选多种坚果与果干，独立包装便于携带。</p>',79.90,356,TRUE,NOW()),
 (10004,1401,'香氛加湿器','细腻雾化，温暖氛围灯','https://images.unsplash.com/photo-1603006905003-be475563bc59?w=800','<h2>让家更舒适</h2><p>安静运行与缺水保护，适合卧室、书房及办公桌。</p>',129.00,64,TRUE,NOW());

INSERT IGNORE INTO product_sku(id,product_id,spec_json,price,stock,enabled) VALUES
 (20001,10001,'{"color":"曜石黑"}',399.00,100,TRUE),(20002,10001,'{"color":"活力橙"}',429.00,60,TRUE),
 (20003,10002,'{"color":"云雾蓝","switch":"茶轴"}',269.00,80,TRUE),(20004,10002,'{"color":"月光白","switch":"红轴"}',289.00,70,TRUE),
 (20005,10003,'{"size":"30袋装"}',79.90,300,TRUE),(20006,10004,'{"color":"奶油白"}',129.00,120,TRUE);

INSERT IGNORE INTO recommendation(id,product_id,position_code,sort_order,enabled) VALUES
 (30001,10001,'HOME_FEATURED',10,TRUE),(30002,10003,'HOME_FEATURED',20,TRUE),(30003,10002,'HOME_NEW',10,TRUE),(30004,10004,'HOME_NEW',20,TRUE);

INSERT IGNORE INTO flash_sale(id,sku_id,price,stock,per_user_limit,start_at,end_at,enabled) VALUES
 (40001,20005,59.90,50,1,DATE_SUB(NOW(),INTERVAL 1 DAY),DATE_ADD(NOW(),INTERVAL 30 DAY),TRUE);
