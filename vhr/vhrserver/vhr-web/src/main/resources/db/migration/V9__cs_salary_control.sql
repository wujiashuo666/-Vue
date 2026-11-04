-- =============================================================
-- V9: CS 化收尾
--  1. 删除"高级资料"菜单（EmpAdv 页面与 CS 无关）
--  2. 薪资管理菜单精简：删除"工资表管理/月末处理"，其余改名
--  3. 工资账套种子数据 CS 化
--  4. 新建赛事晋级/淘汰标记表（玩大手操纵比赛）
-- =============================================================

-- 1. 删除"高级资料"菜单及其角色关联
DELETE FROM menu_role WHERE mid = 8;
DELETE FROM menu WHERE id = 8;

-- 2. 薪资管理子菜单：删除与 CS 无关的"工资表管理"(16)、"月末处理"(17)
DELETE FROM menu_role WHERE mid IN (16, 17);
DELETE FROM menu WHERE id IN (16, 17);

-- 其余子菜单改名
UPDATE menu SET name = '薪资方案管理' WHERE id = 14;
UPDATE menu SET name = '选手薪资设置' WHERE id = 15;
UPDATE menu SET name = '战绩调薪' WHERE id = 18;

-- 3. 工资账套种子数据 CS 化
UPDATE salary SET name = 'S级主力合同' WHERE id = 9;
UPDATE salary SET name = '青训培养合同' WHERE id = 10;
UPDATE salary SET name = '指挥位顶薪合同' WHERE id = 13;

-- 4. 赛事晋级/淘汰标记：玩大手操纵比赛后记录每支战队的晋级结果
CREATE TABLE `es_tournament_progress` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tournament_id` int NOT NULL COMMENT '赛事 id',
  `team_id` int NOT NULL COMMENT '战队 id',
  `result` varchar(16) NOT NULL COMMENT 'advanced=晋级 / eliminated=淘汰',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tournament_team` (`tournament_id`, `team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='赛事晋级/淘汰标记';
