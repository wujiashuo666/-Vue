-- ============================================================
-- V7 CS 化数据完善：
-- 1) 明星选手位置（specialty）修正——按 5Eplay/HLTV 真实站位
-- 2) 职称表（joblevel）整体替换为选手等级（原为教授/工程师等 HR 内容）
-- 3) 删除菜单：备份恢复数据库、初始化数据库（纯前端演示页，无后端逻辑）
-- 4) 统计管理菜单更名：人事信息统计→选手信息统计、人事记录统计→选手变动记录统计
-- 说明：joblevel 保留原 id 仅改内容，避免 employee.jobLevelId 外键失效；
--      employee.name 为游戏 ID，全库唯一，直接按 name 定位选手
-- ============================================================

-- ---------- 1) 选手位置修正 ----------
-- Vitality：ropz 实为自由人，mezii 为步枪手（支援位）
UPDATE `employee` SET `specialty` = '自由人' WHERE `name` = 'ropz';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'mezii';
-- Spirit：donk 以激进突破著称，magixx 为步枪手
UPDATE `employee` SET `specialty` = '突破手' WHERE `name` = 'donk';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'magixx';
-- Falcons：NiKo/TeSeS 为步枪手
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'NiKo';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'TeSeS';
-- NAVI：b1t/iM 为步枪手
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'b1t';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'iM';
-- MOUZ：Jimpphat 是步枪手（狙击手是 torzsi），Spinx 为步枪手
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'Jimpphat';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'Spinx';
-- FURIA：KSCERATO 为明星步枪手
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'KSCERATO';
-- FaZe 系：rain 是著名突破手（误标为指挥），Twistzz 为步枪手
UPDATE `employee` SET `specialty` = '突破手' WHERE `name` = 'rain';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'Twistzz';
-- BC.Game：s1mple 是著名狙击手（误标为突破手），nawwk 让位改步枪手
UPDATE `employee` SET `specialty` = '狙击手' WHERE `name` = 's1mple';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'nawwk';
-- Heroic：Snappi 实为指挥（误标为突破手），Brollan 为步枪手
UPDATE `employee` SET `specialty` = '指挥' WHERE `name` = 'Snappi';
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'Brollan';
-- GL：REZ 是步枪手（误标为狙击手）
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'REZ';
-- Inner Circle：headtr1ck 是狙击手（误标为指挥）
UPDATE `employee` SET `specialty` = '狙击手' WHERE `name` = 'headtr1ck';
-- NIP：Krimbo 是步枪手（误标为狙击手）
UPDATE `employee` SET `specialty` = '步枪手' WHERE `name` = 'Krimbo';

-- ---------- 2) 职称表替换为选手等级 ----------
-- 原：教授/副教授/助教/讲师/初级工程师/中级工程师66/高级工程师/骨灰级工程师
-- titleLevel 原为 enum('正高级','副高级','中级','初级')，放不下新档位，先放宽为 varchar
ALTER TABLE `joblevel` MODIFY COLUMN `titleLevel` VARCHAR(32) NULL;
UPDATE `joblevel` SET `name` = 'S级选手', `titleLevel` = '现役顶级' WHERE `id` = 9;
UPDATE `joblevel` SET `name` = 'A级选手', `titleLevel` = '一线主力' WHERE `id` = 10;
UPDATE `joblevel` SET `name` = 'B级选手', `titleLevel` = '稳定轮换' WHERE `id` = 12;
UPDATE `joblevel` SET `name` = 'C级选手', `titleLevel` = '二线阵容' WHERE `id` = 13;
UPDATE `joblevel` SET `name` = 'D级选手', `titleLevel` = '替补席' WHERE `id` = 14;
UPDATE `joblevel` SET `name` = '青训选手', `titleLevel` = '培养期' WHERE `id` = 15;
UPDATE `joblevel` SET `name` = '自由选手', `titleLevel` = '未签约' WHERE `id` = 16;
UPDATE `joblevel` SET `name` = '传奇选手', `titleLevel` = '荣誉级' WHERE `id` = 17;

-- ---------- 3) 删除演示菜单：备份恢复数据库 / 初始化数据库 ----------
DELETE FROM `menu_role` WHERE `mid` IN (
    SELECT `id` FROM `menu` WHERE `path` IN ('/sys/data', '/sys/init')
);
DELETE FROM `menu` WHERE `path` IN ('/sys/data', '/sys/init');

-- ---------- 4) 统计管理菜单更名 ----------
UPDATE `menu` SET `name` = '选手信息统计' WHERE `path` = '/sta/pers' AND `name` = '人事信息统计';
UPDATE `menu` SET `name` = '选手变动记录统计' WHERE `path` = '/sta/record' AND `name` = '人事记录统计';
