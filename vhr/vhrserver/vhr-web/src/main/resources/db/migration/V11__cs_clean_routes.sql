-- V11: 菜单跳转路径 CS 化——点菜单后地址栏显示简洁英文路径
-- 只改前端路由 path 列；url 列是后端接口安全映射(/employee/basic/** 等)，保持不动
UPDATE menu SET path='/player'          WHERE id=7;   -- 基本资料(选手资料)
UPDATE menu SET path='/players'        WHERE id=9;   -- 选手资料(选手管理)
UPDATE menu SET path='/rewards'        WHERE id=10;  -- 选手奖惩
UPDATE menu SET path='/training'       WHERE id=11;  -- 选手培训
UPDATE menu SET path='/salary/history' WHERE id=12;  -- 选手调薪
UPDATE menu SET path='/transfer'       WHERE id=13;  -- 选手调动
UPDATE menu SET path='/salary/plans'   WHERE id=14;  -- 薪资方案管理
UPDATE menu SET path='/salary/setup'   WHERE id=15;  -- 选手薪资设置
UPDATE menu SET path='/salary/adjust' WHERE id=18;  -- 战绩调薪
UPDATE menu SET path='/stats/all'      WHERE id=19;  -- 综合信息统计
UPDATE menu SET path='/stats/score'    WHERE id=20;  -- 选手积分统计
UPDATE menu SET path='/stats/player'   WHERE id=21;  -- 选手信息统计
UPDATE menu SET path='/stats/changelog' WHERE id=22; -- 选手变动记录统计
UPDATE menu SET path='/settings/basic'    WHERE id=23; -- 基础信息设置
UPDATE menu SET path='/settings/system'   WHERE id=24; -- 系统管理
UPDATE menu SET path='/settings/logs'     WHERE id=25; -- 操作日志管理
UPDATE menu SET path='/settings/operators' WHERE id=26; -- 操作员管理
