-- ============================================================
-- V5 战队数据修正：
-- 1) short_name 此前由脚本取队名前 6 位生成（如 Vitality -> VITALI），
--    改为 HLTV 惯用缩写（VIT / SPR / FAZE ...）
-- 2) region 此前为空串，按战队所在赛区回填
-- 按队名 UPDATE，天然幂等；脚本重建过 es_team 后再次执行也无副作用
-- ============================================================

UPDATE `es_team` SET `short_name` = 'SPR',    `region` = '独联体' WHERE `name` = 'Spirit';
UPDATE `es_team` SET `short_name` = 'VIT',    `region` = '欧洲'   WHERE `name` = 'Vitality';
UPDATE `es_team` SET `short_name` = 'FUR',    `region` = '美洲'   WHERE `name` = 'FURIA';
UPDATE `es_team` SET `short_name` = 'MOUZ',   `region` = '欧洲'   WHERE `name` = 'MOUZ';
UPDATE `es_team` SET `short_name` = 'AUR',    `region` = '土耳其' WHERE `name` = 'Aurora';
UPDATE `es_team` SET `short_name` = 'FLC',    `region` = '中东'   WHERE `name` = 'Falcons';
UPDATE `es_team` SET `short_name` = 'FUT',    `region` = '土耳其' WHERE `name` = 'FUT';
UPDATE `es_team` SET `short_name` = 'LEG',    `region` = '美洲'   WHERE `name` = 'Legacy';
UPDATE `es_team` SET `short_name` = 'BB',     `region` = '独联体' WHERE `name` = 'BB';
UPDATE `es_team` SET `short_name` = 'G2',     `region` = '欧洲'   WHERE `name` = 'G2';
UPDATE `es_team` SET `short_name` = 'NAVI',   `region` = '独联体' WHERE `name` = 'NAVI';
UPDATE `es_team` SET `short_name` = 'PAR',    `region` = '独联体' WHERE `name` = 'PARIVISION';
UPDATE `es_team` SET `short_name` = 'MIBR',   `region` = '美洲'   WHERE `name` = 'MIBR';
UPDATE `es_team` SET `short_name` = '1WIN',   `region` = '独联体' WHERE `name` = '1win';
UPDATE `es_team` SET `short_name` = '9Z',     `region` = '美洲'   WHERE `name` = '9z';
UPDATE `es_team` SET `short_name` = 'MGZ',    `region` = '亚洲'   WHERE `name` = 'Mongolz';
UPDATE `es_team` SET `short_name` = 'AST',    `region` = '欧洲'   WHERE `name` = 'Astralis';
UPDATE `es_team` SET `short_name` = 'FAZE',   `region` = '欧洲'   WHERE `name` = 'FaZe';
UPDATE `es_team` SET `short_name` = 'M80',    `region` = '美洲'   WHERE `name` = 'M80';
UPDATE `es_team` SET `short_name` = 'NIP',    `region` = '欧洲'   WHERE `name` = 'NIP';
UPDATE `es_team` SET `short_name` = 'NMG',    `region` = '独联体' WHERE `name` = 'Nemiga';
UPDATE `es_team` SET `short_name` = 'B8',     `region` = '独联体' WHERE `name` = 'B8';
UPDATE `es_team` SET `short_name` = 'TL',     `region` = '美洲'   WHERE `name` = 'Liquid';
UPDATE `es_team` SET `short_name` = 'HRC',    `region` = '欧洲'   WHERE `name` = 'Heroic';
UPDATE `es_team` SET `short_name` = 'MAG',    `region` = '欧洲'   WHERE `name` = 'magic';
UPDATE `es_team` SET `short_name` = 'ENV',    `region` = '美洲'   WHERE `name` = 'Envy';
UPDATE `es_team` SET `short_name` = 'GL',     `region` = '欧洲'   WHERE `name` = 'GL';
UPDATE `es_team` SET `short_name` = 'LUM',    `region` = '美洲'   WHERE `name` = 'Luminosity';
UPDATE `es_team` SET `short_name` = 'ALL',    `region` = '欧洲'   WHERE `name` = 'Alliance';
UPDATE `es_team` SET `short_name` = 'EF',     `region` = '土耳其' WHERE `name` = 'EF';
UPDATE `es_team` SET `short_name` = 'NRG',    `region` = '美洲'   WHERE `name` = 'NRG';
UPDATE `es_team` SET `short_name` = 'TYLOO',  `region` = '亚洲'   WHERE `name` = 'TYLOO';
UPDATE `es_team` SET `short_name` = 'BIG',    `region` = '欧洲'   WHERE `name` = 'BIG';
UPDATE `es_team` SET `short_name` = 'SIN',    `region` = '欧洲'   WHERE `name` = 'SINNERS';
UPDATE `es_team` SET `short_name` = '100T',   `region` = '美洲'   WHERE `name` = '100 Thieves';
UPDATE `es_team` SET `short_name` = 'FNC',    `region` = '欧洲'   WHERE `name` = 'FNC';
UPDATE `es_team` SET `short_name` = 'JJH',    `region` = '亚洲'   WHERE `name` = 'JJH';
UPDATE `es_team` SET `short_name` = '5STAR',  `region` = '亚洲'   WHERE `name` = '5star';
UPDATE `es_team` SET `short_name` = 'IC',     `region` = '美洲'   WHERE `name` = 'Inner Circle';
UPDATE `es_team` SET `short_name` = 'PAIN',   `region` = '美洲'   WHERE `name` = 'paiN';
UPDATE `es_team` SET `short_name` = 'SNG',    `region` = '土耳其' WHERE `name` = 'Sangal';
UPDATE `es_team` SET `short_name` = 'BCG',    `region` = '独联体' WHERE `name` = 'BC.Game';
UPDATE `es_team` SET `short_name` = 'HOTU',   `region` = '独联体' WHERE `name` = 'HOTU';
UPDATE `es_team` SET `short_name` = 'K27',    `region` = '亚洲'   WHERE `name` = 'K27';
UPDATE `es_team` SET `short_name` = 'LVG',    `region` = '亚洲'   WHERE `name` = 'LVG';
