-- =============================================================
-- V10: 数据库 CS 化清理
--  1. 删除 employee 表中与 CS 战队无关的人事概念列：
--     身份证号码(idCard)、婚姻状况(wedlock)、民族(nationId)、政治面貌(politicId)
--     （nativePlace 原籍贯字段已被复用为选手真实姓名，保留）
--  2. 删除民族字典表 nation、政治面貌字典表 politicsstatus
--  3. mail_send_log 保留：添加选手时的邮件投递记录仍在使用
-- =============================================================

-- 1. 先删除引用 nation / politicsstatus 的外键约束（V1 中定义的 employee_ibfk_4/5）
ALTER TABLE employee DROP FOREIGN KEY employee_ibfk_4;
ALTER TABLE employee DROP FOREIGN KEY employee_ibfk_5;

-- 2. 删除人事列
ALTER TABLE employee
    DROP COLUMN idCard,
    DROP COLUMN wedlock,
    DROP COLUMN nationId,
    DROP COLUMN politicId;

-- 3. 删除人事字典表
DROP TABLE IF EXISTS nation;
DROP TABLE IF EXISTS politicsstatus;
