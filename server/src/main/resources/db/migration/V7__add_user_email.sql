ALTER TABLE shop_user ADD COLUMN email VARCHAR(120) NULL AFTER username;
CREATE UNIQUE INDEX uk_shop_user_email ON shop_user(email);
