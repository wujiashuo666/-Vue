-- ============================================================
-- V4 赛事模块完善：
-- 1) 补 es_team.logo 列（此前由 build_esports_data.py 手工添加，未进迁移）
-- 2) es_tournament 扩展区 / 起止日期字段，并回填种子赛事
-- 说明：es_team.logo 在部分环境已存在，用 information_schema 判断做幂等处理
-- ============================================================

-- ---------- 1) es_team.logo ----------
SET @ddl = (SELECT IF(COUNT(*) = 0,
    'ALTER TABLE es_team ADD COLUMN logo VARCHAR(255) NULL',
    'SELECT 1')
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'es_team' AND COLUMN_NAME = 'logo');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ---------- 2) es_tournament 扩展字段（逐列幂等） ----------
SET @ddl = (SELECT IF(COUNT(*) = 0,
    'ALTER TABLE es_tournament ADD COLUMN region VARCHAR(32) NULL',
    'SELECT 1')
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'es_tournament' AND COLUMN_NAME = 'region');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl = (SELECT IF(COUNT(*) = 0,
    'ALTER TABLE es_tournament ADD COLUMN start_date DATE NULL',
    'SELECT 1')
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'es_tournament' AND COLUMN_NAME = 'start_date');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @ddl = (SELECT IF(COUNT(*) = 0,
    'ALTER TABLE es_tournament ADD COLUMN end_date DATE NULL',
    'SELECT 1')
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'es_tournament' AND COLUMN_NAME = 'end_date');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ---------- 3) 回填种子赛事的赛区与赛程（region 为空才更新，幂等） ----------
UPDATE `es_tournament` SET `region` = '欧洲', `start_date` = '2026-09-28', `end_date` = '2026-11-15'
WHERE `id` = 1 AND (`region` IS NULL OR `region` = '');

UPDATE `es_tournament` SET `region` = '欧洲', `start_date` = '2026-10-08', `end_date` = '2026-11-20'
WHERE `id` = 2 AND (`region` IS NULL OR `region` = '');

UPDATE `es_tournament` SET `region` = '欧洲', `start_date` = '2026-10-15', `end_date` = '2026-11-30'
WHERE `id` = 3 AND (`region` IS NULL OR `region` = '');

UPDATE `es_tournament` SET `region` = '亚洲', `start_date` = '2026-10-12', `end_date` = '2026-11-25'
WHERE `id` = 4 AND (`region` IS NULL OR `region` = '');
