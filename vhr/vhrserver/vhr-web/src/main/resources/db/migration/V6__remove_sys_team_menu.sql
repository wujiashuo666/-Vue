-- ============================================================
-- V6 移除「系统管理 → 战队管理」菜单入口
-- 战队维护功能并入比赛中心（战队战绩页），系统管理下不再出现
-- （对应 vhr.sql 中 2026-10-30 的 menu/menu_role 插入记录）
-- ============================================================

-- 先删角色授权，再删菜单本身，避免菜单删除后找不到 mid
DELETE FROM `menu_role` WHERE `mid` IN (
    SELECT `id` FROM `menu` WHERE `path` = '/sys/team' AND `component` = 'SysTeam'
);
DELETE FROM `menu` WHERE `path` = '/sys/team' AND `component` = 'SysTeam';
