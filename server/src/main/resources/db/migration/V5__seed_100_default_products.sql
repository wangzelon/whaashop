-- 生成 100 个商城演示商品。固定 ID 区间使种子数据可识别，并避免影响业务自增数据。
INSERT IGNORE INTO product
    (id, category_id, name, subtitle, main_image, detail_html, min_price, sales, published, created_at)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 100
)
SELECT
    11000 + n,
    CASE MOD(n - 1, 10)
        WHEN 0 THEN 1101 WHEN 1 THEN 1101 WHEN 2 THEN 1102 WHEN 3 THEN 1102
        WHEN 4 THEN 1201 WHEN 5 THEN 1201 WHEN 6 THEN 1301 WHEN 7 THEN 1301
        WHEN 8 THEN 1401 ELSE 1401 END,
    CONCAT(
        CASE MOD(n - 1, 10)
            WHEN 0 THEN '轻智能运动手环' WHEN 1 THEN '降噪蓝牙耳机'
            WHEN 2 THEN '便携办公鼠标' WHEN 3 THEN '高清护眼台灯'
            WHEN 4 THEN '舒适纯棉短袖' WHEN 5 THEN '轻量通勤双肩包'
            WHEN 6 THEN '原味烘焙坚果' WHEN 7 THEN '低糖水果燕麦'
            WHEN 8 THEN '柔软亲肤浴巾' ELSE '简约陶瓷马克杯' END,
        ' · ', LPAD(n, 3, '0'), '款'
    ),
    CONCAT(
        CASE MOD(n - 1, 10)
            WHEN 0 THEN '全天健康记录，轻巧长续航' WHEN 1 THEN '清晰通话，沉浸式好声音'
            WHEN 2 THEN '静音按键，轻盈舒适握感' WHEN 3 THEN '多档调光，陪伴阅读办公'
            WHEN 4 THEN '透气亲肤，日常百搭版型' WHEN 5 THEN '合理分区，轻松收纳随身物品'
            WHEN 6 THEN '严选原料，每日营养补充' WHEN 7 THEN '谷物果干搭配，早餐更轻松'
            WHEN 8 THEN '蓬松吸水，细腻呵护肌肤' ELSE '温润釉面，点亮日常饮水时刻' END,
        '，WhaaShop 品质精选'
    ),
    CASE MOD(n - 1, 10)
        WHEN 0 THEN 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=800'
        WHEN 1 THEN 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800'
        WHEN 2 THEN 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800'
        WHEN 3 THEN 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800'
        WHEN 4 THEN 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800'
        WHEN 5 THEN 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800'
        WHEN 6 THEN 'https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=800'
        WHEN 7 THEN 'https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=800'
        WHEN 8 THEN 'https://images.unsplash.com/photo-1583845112203-454c2254ed1c?w=800'
        ELSE 'https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=800' END,
    CONCAT(
        '<h2>WhaaShop 品质精选</h2><p>',
        CASE MOD(n - 1, 10)
            WHEN 0 THEN '轻盈佩戴与实用功能兼备，适合通勤、运动等多种场景。'
            WHEN 1 THEN '兼顾舒适佩戴与稳定连接，为音乐和通话带来清晰体验。'
            WHEN 2 THEN '人体工学设计搭配稳定连接，是学习和办公的可靠伙伴。'
            WHEN 3 THEN '柔和光线与灵活调节设计，让阅读和工作更加舒适。'
            WHEN 4 THEN '精选舒适面料与简约剪裁，轻松融入日常穿搭。'
            WHEN 5 THEN '轻量材质搭配实用分区，满足通勤与短途出行需要。'
            WHEN 6 THEN '多种坚果科学搭配，独立包装便于随时补充能量。'
            WHEN 7 THEN '谷物、果干与坚果合理搭配，冲泡即享营养早餐。'
            WHEN 8 THEN '精选柔软纤维，吸水蓬松，适合家庭日常使用。'
            ELSE '简约器型搭配细腻釉面，适合咖啡、牛奶和日常饮水。' END,
        '</p><h3>商品特点</h3><ul><li>严选品质</li><li>实用设计</li><li>安心售后</li></ul>'
    ),
    CAST((CASE MOD(n - 1, 10)
        WHEN 0 THEN 159 WHEN 1 THEN 199 WHEN 2 THEN 69 WHEN 3 THEN 129
        WHEN 4 THEN 79 WHEN 5 THEN 169 WHEN 6 THEN 59 WHEN 7 THEN 39
        WHEN 8 THEN 49 ELSE 45 END) + MOD(n * 7, 20) AS DECIMAL(12, 2)),
    20 + MOD(n * 37, 980),
    TRUE,
    DATE_SUB(NOW(), INTERVAL n HOUR)
FROM seq;

-- 每个演示商品至少提供一个启用 SKU，确保可以正常加入购物车并下单。
INSERT IGNORE INTO product_sku
    (id, product_id, spec_json, price, stock, enabled)
SELECT
    21000 + (p.id - 11000),
    p.id,
    JSON_OBJECT(
        '款式', CASE MOD(p.id - 11001, 3) WHEN 0 THEN '经典款' WHEN 1 THEN '升级款' ELSE '轻享款' END,
        '包装', '官方标配'
    ),
    p.min_price,
    30 + MOD(p.id * 13, 270),
    TRUE
FROM product p
WHERE p.id BETWEEN 11001 AND 11100;
