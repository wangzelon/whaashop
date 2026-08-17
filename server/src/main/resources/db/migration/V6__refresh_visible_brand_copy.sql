UPDATE product SET subtitle = REPLACE(subtitle, 'WhaaShop 品质精选', '橙选品质好物') WHERE subtitle LIKE '%WhaaShop%';
UPDATE product SET detail_html = REPLACE(detail_html, 'WhaaShop 品质精选', '橙选品质好物') WHERE detail_html LIKE '%WhaaShop%';
