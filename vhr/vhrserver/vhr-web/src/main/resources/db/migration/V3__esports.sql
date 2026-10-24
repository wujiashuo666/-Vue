-- ============================================================
-- V3 赛事中心表：战队 / 选手 / 赛事 / 比赛 / 逐图比分
-- 由前端 mock 数据 (src/mock/esports.js) 自动生成
-- ============================================================

DROP TABLE IF EXISTS `es_match_map`;
DROP TABLE IF EXISTS `es_match`;
DROP TABLE IF EXISTS `es_tournament`;
DROP TABLE IF EXISTS `es_player`;
DROP TABLE IF EXISTS `es_team`;

CREATE TABLE `es_team` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `short_name` varchar(16) NOT NULL,
  `region` varchar(32) DEFAULT NULL,
  `world_rank` int DEFAULT NULL,
  `color1` varchar(16) DEFAULT NULL,
  `color2` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `es_player` (
  `id` int NOT NULL AUTO_INCREMENT,
  `team_id` int DEFAULT NULL,
  `nick` varchar(64) NOT NULL,
  `real_name` varchar(128) DEFAULT NULL,
  `country` varchar(8) DEFAULT NULL,
  `rating` decimal(5,2) DEFAULT NULL,
  `kd` decimal(5,2) DEFAULT NULL,
  `adr` decimal(6,2) DEFAULT NULL,
  `kast` decimal(5,2) DEFAULT NULL,
  `maps_cnt` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `es_tournament` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `es_match` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tournament_id` int DEFAULT NULL,
  `stage` varchar(32) DEFAULT NULL,
  `bo` int DEFAULT NULL,
  `team1_id` int DEFAULT NULL,
  `team2_id` int DEFAULT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'upcoming',
  `start_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tournament_id` (`tournament_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `es_match_map` (
  `id` int NOT NULL AUTO_INCREMENT,
  `match_id` int DEFAULT NULL,
  `map_order` int DEFAULT NULL,
  `map_name` varchar(32) DEFAULT NULL,
  `score1` int DEFAULT 0,
  `score2` int DEFAULT 0,
  `live` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `match_id` (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 战队
INSERT INTO `es_team` (`id`,`name`,`short_name`,`region`,`world_rank`,`color1`,`color2`) VALUES
(1, 'Vitality', 'VIT', '欧洲', 1, '#f7d51d', '#1f1f1f'),
(2, 'MOUZ', 'MOUZ', '欧洲', 2, '#ff5c00', '#2b2b2b'),
(3, 'Team Spirit', 'SPR', '独联体', 3, '#ffd400', '#121212'),
(4, 'Aurora', 'AUR', '土耳其', 8, '#00d0ff', '#0b2b3a'),
(5, 'FURIA', 'FUR', '美洲', 9, '#1ed760', '#10251a'),
(6, 'Falcons', 'FLC', '中东', 4, '#4ade80', '#0f2b1f'),
(7, 'Astralis', 'AST', '欧洲', 11, '#e02b2b', '#1a0d0d'),
(8, 'G2 Esports', 'G2', '欧洲', 5, '#ff2d55', '#1a0a12'),
(9, 'The MongolZ', 'MGLZ', '亚洲', 6, '#3b82f6', '#0b1c3a'),
(10, 'NAVI', 'NAVI', '独联体', 7, '#ffee00', '#0f0f00'),
(11, 'Team Liquid', 'TL', '美洲', 12, '#00a8e8', '#0a1f2b'),
(12, 'Fnatic', 'FNC', '欧洲', 14, '#ff6500', '#2b1600'),
(13, 'Legacy', 'LGC', '美洲', 16, '#a78bfa', '#1a1233'),
(14, 'TYLOO', 'TYLOO', '亚洲', 18, '#ef4444', '#2b0b0b'),
(15, 'Lynn Vision', 'LVG', '亚洲', 21, '#22d3ee', '#08272e'),
(16, 'FaZe Clan', 'FAZE', '欧洲', 10, '#e11d48', '#2b0713'),
(17, 'paiN', 'PNG', '美洲', 20, '#f59e0b', '#2b1f05');

-- 赛事
INSERT INTO `es_tournament` (`id`,`name`) VALUES
(1, 'ESL Pro League S24'),
(2, 'BLAST Premier'),
(3, 'IEM Katowice'),
(4, 'Perfect World 亚洲区');

-- 比赛
INSERT INTO `es_match` (`id`,`tournament_id`,`stage`,`bo`,`team1_id`,`team2_id`,`status`,`start_time`) VALUES
(1, 1, '小组赛', 3, 1, 2, 'finished', '2026-10-28 01:00:00'),
(2, 2, '小组赛', 3, 1, 3, 'finished', '2026-10-26 18:00:00'),
(3, 3, '小组赛', 3, 2, 4, 'finished', '2026-10-25 22:00:00'),
(4, 1, '小组赛', 3, 5, 6, 'finished', '2026-10-24 20:00:00'),
(5, 2, '淘汰赛', 3, 10, 8, 'finished', '2026-10-23 19:00:00'),
(6, 3, '小组赛', 3, 3, 7, 'finished', '2026-10-22 20:00:00'),
(7, 1, '小组赛', 3, 16, 11, 'finished', '2026-10-21 02:00:00'),
(8, 4, '小组赛', 3, 14, 15, 'finished', '2026-10-20 14:00:00'),
(9, 3, '小组赛', 3, 9, 17, 'finished', '2026-10-19 17:00:00'),
(10, 2, '决赛', 5, 1, 6, 'finished', '2026-10-18 21:00:00'),
(11, 1, '淘汰赛', 3, 2, 10, 'finished', '2026-10-17 18:00:00'),
(12, 4, '淘汰赛', 3, 3, 14, 'finished', '2026-10-16 15:00:00'),
(13, 1, '淘汰赛', 3, 8, 6, 'live', '2026-10-30 08:00:00'),
(14, 2, '小组赛', 3, 5, 7, 'live', '2026-10-30 07:30:00'),
(15, 2, '淘汰赛', 3, 1, 10, 'upcoming', '2026-11-02 15:00:00'),
(16, 1, '淘汰赛', 3, 2, 6, 'upcoming', '2026-11-05 20:00:00'),
(17, 3, '小组赛', 3, 16, 9, 'upcoming', '2026-11-08 19:00:00'),
(18, 3, '小组赛', 3, 3, 2, 'upcoming', '2026-11-11 16:00:00'),
(19, 4, '小组赛', 3, 14, 13, 'upcoming', '2026-11-15 14:00:00');

-- 逐图比分
INSERT INTO `es_match_map` (`match_id`,`map_order`,`map_name`,`score1`,`score2`,`live`) VALUES
(1, 1, 'Mirage', 13, 10, 0),
(1, 2, 'Nuke', 8, 13, 0),
(1, 3, 'Inferno', 13, 11, 0),
(2, 1, 'Nuke', 13, 9, 0),
(2, 2, 'Ancient', 10, 13, 0),
(2, 3, 'Train', 13, 8, 0),
(3, 1, 'Inferno', 13, 7, 0),
(3, 2, 'Overpass', 13, 9, 0),
(4, 1, 'Overpass', 9, 13, 0),
(4, 2, 'Vertigo', 11, 13, 0),
(5, 1, 'Ancient', 13, 11, 0),
(5, 2, 'Nuke', 14, 16, 0),
(5, 3, 'Dust2', 13, 10, 0),
(6, 1, 'Mirage', 13, 5, 0),
(6, 2, 'Nuke', 13, 8, 0),
(7, 1, 'Inferno', 16, 14, 0),
(7, 2, 'Ancient', 13, 7, 0),
(8, 1, 'Mirage', 13, 8, 0),
(8, 2, 'Anubis', 13, 10, 0),
(9, 1, 'Nuke', 13, 11, 0),
(9, 2, 'Vertigo', 13, 9, 0),
(10, 1, 'Mirage', 13, 7, 0),
(10, 2, 'Nuke', 11, 13, 0),
(10, 3, 'Inferno', 13, 9, 0),
(10, 4, 'Ancient', 13, 6, 0),
(11, 1, 'Ancient', 13, 10, 0),
(11, 2, 'Mirage', 10, 13, 0),
(11, 3, 'Train', 13, 11, 0),
(12, 1, 'Mirage', 13, 4, 0),
(12, 2, 'Inferno', 13, 6, 0),
(13, 1, 'Mirage', 13, 9, 0),
(13, 2, 'Nuke', 7, 5, 1),
(14, 1, 'Inferno', 6, 9, 1);

-- 选手
INSERT INTO `es_player` (`id`,`team_id`,`nick`,`real_name`,`country`,`rating`,`kd`,`adr`,`kast`,`maps_cnt`) VALUES
(1, 1, 'ZywOo', 'Mathieu Herbaut', 'FR', 1.34, 1.38, 88.40, 76.20, 42),
(2, 1, 'ropz', 'Robin Kool', 'EE', 1.18, 1.21, 79.10, 74.50, 42),
(3, 1, 'flameZ', 'Shahar Shushan', 'IL', 1.09, 1.06, 74.30, 71.80, 42),
(4, 1, 'mezii', 'William Merriman', 'GB', 1.02, 0.99, 68.20, 72.40, 42),
(5, 1, 'apEX', 'Dan Madesclaire', 'FR', 0.96, 0.92, 65.70, 69.10, 42),
(6, 2, 'torzsi', 'Ádám Torzsás', 'HU', 1.22, 1.24, 81.60, 73.90, 39),
(7, 2, 'xertioN', 'Dorian Berman', 'IL', 1.15, 1.13, 77.40, 74.10, 39),
(8, 2, 'Jimpphat', 'Jimi Salo', 'FI', 1.12, 1.11, 75.80, 72.60, 39),
(9, 2, 'Brollan', 'Ludvig Brolin', 'SE', 1.05, 1.02, 70.10, 73.20, 39),
(10, 2, 'siuhy', 'Kamil Szkaradek', 'PL', 0.98, 0.94, 63.90, 70.50, 39),
(11, 3, 'donk', 'Danil Kryshkovets', 'RU', 1.41, 1.47, 94.20, 77.80, 45),
(12, 3, 'sh1ro', 'Dmitry Sokolov', 'RU', 1.19, 1.28, 72.60, 75.30, 45),
(13, 3, 'zont1x', 'Myroslav Plakhotia', 'UA', 1.08, 1.05, 71.20, 73.40, 45),
(14, 3, 'chopper', 'Leonid Vishnyakov', 'RU', 0.99, 0.96, 64.80, 71.20, 45),
(15, 3, 'magixx', 'Boris Vorobiev', 'RU', 1.03, 1.01, 69.40, 72.90, 45),
(16, 4, 'XANTARES', 'Can Dörtkardeş', 'TR', 1.21, 1.22, 82.30, 73.10, 36),
(17, 4, 'woxic', 'Özgür Eker', 'TR', 1.11, 1.14, 70.90, 71.50, 36),
(18, 4, 'MAJ3R', 'Engin Küpeli', 'TR', 1.00, 0.97, 66.20, 70.80, 36),
(19, 4, 'jottAAA', 'Ali Haydar Yılmaz', 'TR', 1.06, 1.04, 73.50, 72.20, 36),
(20, 4, 'yiku', 'Yusuf Buğra Karadeniz', 'TR', 0.95, 0.91, 62.70, 69.60, 36),
(21, 5, 'KSCERATO', 'Kaike Cerato', 'BR', 1.20, 1.25, 80.10, 74.60, 34),
(22, 5, 'yuurih', 'Yuri Santos', 'BR', 1.14, 1.16, 76.80, 73.30, 34),
(23, 5, 'molodoy', 'Danil Golubenko', 'KZ', 1.17, 1.19, 78.40, 72.90, 34),
(24, 5, 'YEKINDAR', 'Mareks Gaļinskis', 'LV', 1.04, 1.00, 71.60, 70.40, 34),
(25, 5, 'FalleN', 'Gabriel Toledo', 'BR', 0.97, 0.95, 60.30, 71.70, 34),
(26, 6, 'NiKo', 'Nikola Kovač', 'BA', 1.27, 1.29, 86.70, 75.90, 40),
(27, 6, 'm0NESY', 'Ilya Osipov', 'RU', 1.31, 1.35, 83.20, 76.40, 40),
(28, 6, 'kyousuke', 'Maxim Lukin', 'RU', 1.13, 1.12, 75.10, 72.80, 40),
(29, 6, 'TeSeS', 'René Madsen', 'DK', 1.07, 1.04, 72.30, 73.10, 40),
(30, 6, 'Magisk', 'Emil Reif', 'DK', 1.01, 0.99, 67.80, 72.50, 40),
(31, 7, 'device', 'Nicolai Reedtz', 'DK', 1.16, 1.20, 78.90, 74.20, 31),
(32, 7, 'Staehr', 'Victor Staehr', 'DK', 1.06, 1.03, 71.40, 72.10, 31),
(33, 7, 'jabbi', 'Jakob Nygaard', 'DK', 1.02, 0.98, 69.60, 71.30, 31),
(34, 7, 'stavn', 'Martin Lund', 'DK', 1.09, 1.07, 73.80, 73.60, 31),
(35, 7, 'Hooxi', 'Rasmus Nielsen', 'DK', 0.92, 0.88, 58.40, 69.20, 31),
(36, 8, 'huNter-', 'Nemanja Kovač', 'BA', 1.18, 1.19, 79.70, 74.10, 38),
(37, 8, 'malbsMd', 'Mario Samayoa', 'GT', 1.15, 1.14, 77.20, 72.70, 38),
(38, 8, 'HeavyGod', 'Nikita Martynenko', 'RU', 1.10, 1.08, 74.50, 72.90, 38),
(39, 8, 'SunPayus', 'Alvaro Garcia', 'ES', 1.05, 1.03, 70.80, 72.40, 38),
(40, 8, 'MATYS', 'Matěj Šimko', 'SK', 1.00, 0.97, 68.10, 71.60, 38),
(41, 9, '910', 'Usukhbayar Banzragch', 'MN', 1.21, 1.23, 81.30, 73.80, 37),
(42, 9, 'bLitz', 'Garidmagnai Byambasuren', 'MN', 1.12, 1.10, 74.90, 72.50, 37),
(43, 9, 'Techno', 'Sodbayar Munkhbold', 'MN', 1.09, 1.06, 73.10, 72.20, 37),
(44, 9, 'Senzu', 'Tengis Batjargal', 'MN', 1.07, 1.05, 71.70, 71.90, 37),
(45, 9, 'mzinho', 'Ayush Batbold', 'MN', 1.03, 1.00, 69.20, 72.10, 37),
(46, 10, 'w0nderful', 'Ihor Zhdanov', 'UA', 1.19, 1.22, 76.40, 73.70, 35),
(47, 10, 'b1t', 'Valeriy Vakhovskiy', 'UA', 1.14, 1.17, 75.30, 74.20, 35),
(48, 10, 'iM', 'Mihai Ivan', 'RO', 1.08, 1.05, 72.60, 72.80, 35),
(49, 10, 'jL', 'Justinas Lekavičius', 'LT', 1.06, 1.02, 71.10, 71.50, 35),
(50, 10, 'Aleksib', 'Aleksi Virolainen', 'FI', 0.96, 0.93, 62.50, 70.90, 35),
(51, 11, 'Twistzz', 'Russel Van Dulken', 'CA', 1.17, 1.19, 78.60, 74.40, 30),
(52, 11, 'NAF', 'Keith Markovic', 'CA', 1.11, 1.13, 74.20, 73.10, 30),
(53, 11, 'ulti', 'Ultimate Yilmaz', 'TR', 1.09, 1.07, 73.40, 72.30, 30),
(54, 11, 'skullz', 'Felipe Reis', 'BR', 1.02, 0.99, 68.90, 71.40, 30),
(55, 11, 'cadiaN', 'Casper Møller', 'DK', 0.95, 0.92, 61.70, 70.60, 30),
(56, 12, 'blameF', 'Benjamin Bremer', 'DK', 1.10, 1.12, 75.70, 73.20, 28),
(57, 12, 'KRIMZ', 'Freddy Johansson', 'SE', 1.05, 1.08, 68.40, 73.60, 28),
(58, 12, 'bodyy', 'Alexandre Pianaro', 'FR', 1.00, 0.98, 66.90, 71.80, 28),
(59, 12, 'matys', 'Tomáš Šimko', 'SK', 1.03, 1.01, 70.20, 71.20, 28),
(60, 12, 'jambo', 'Danny Jørgensen', 'DK', 0.97, 0.94, 63.60, 70.10, 28),
(61, 13, 'dumau', 'Eduardo Wolkmer', 'BR', 1.12, 1.14, 76.10, 72.90, 26),
(62, 13, 'latto', 'Bruno Rebelatto', 'BR', 1.08, 1.06, 72.80, 72.10, 26),
(63, 13, 'n1ssim', 'Vinicius Pereira', 'BR', 1.05, 1.03, 71.30, 71.70, 26),
(64, 13, 'lux', 'Lucas Meneghini', 'BR', 1.01, 0.98, 68.50, 71.10, 26),
(65, 13, 'saadzin', 'Guilherme Pacheco', 'BR', 0.96, 0.93, 62.90, 69.80, 26),
(66, 14, 'JamYoung', 'Yang Yi', 'CN', 1.16, 1.18, 78.30, 73.50, 29),
(67, 14, 'Mercy', 'Zhang Mingyuan', 'CN', 1.06, 1.04, 71.90, 72.20, 29),
(68, 14, 'Moseyuh', 'Chen Qiyuan', 'CN', 1.03, 1.01, 70.40, 71.60, 29),
(69, 14, 'Attacker', 'YuanZhang Sheng', 'CN', 1.00, 0.97, 67.80, 71.30, 29),
(70, 14, 'Jee', 'Dong Jian', 'CN', 0.94, 0.91, 61.20, 69.40, 29),
(71, 15, 'z4kr', 'Zhang Kai', 'CN', 1.09, 1.11, 73.70, 72.60, 24),
(72, 15, 'Westmelon', 'Zhu Zihao', 'CN', 1.04, 1.02, 70.60, 71.90, 24),
(73, 15, 'EmiliaQAQ', 'Tang Siyu', 'CN', 1.01, 0.99, 69.10, 71.20, 24),
(74, 15, 'Starry', 'Li Xing', 'CN', 0.98, 0.95, 65.30, 70.50, 24),
(75, 15, '3glory', 'Wang Wei', 'CN', 0.93, 0.90, 60.80, 68.90, 24),
(76, 16, 'broky', 'Helvijs Saukants', 'LV', 1.15, 1.17, 77.50, 73.40, 33),
(77, 16, 'frozen', 'David Čerňanský', 'SK', 1.18, 1.20, 79.80, 74.10, 33),
(78, 16, 'rain', 'Håvard Nygaard', 'NO', 1.07, 1.05, 72.40, 72.70, 33),
(79, 16, 'EliGE', 'Jonathan Jablonowski', 'US', 1.10, 1.08, 74.60, 72.90, 33),
(80, 16, 'karrigan', 'Finn Andersen', 'DK', 0.94, 0.90, 61.40, 70.30, 33),
(81, 17, 'dgt', 'Diego Pereira', 'BR', 1.07, 1.05, 72.10, 72.40, 25),
(82, 17, 'biguzera', 'Rodrigo Bittencourt', 'BR', 1.03, 1.01, 69.70, 71.80, 25),
(83, 17, 'dav1deuS', 'David Tapia', 'BR', 1.05, 1.04, 71.20, 71.50, 25),
(84, 17, 'snow', 'João Vinicius', 'BR', 1.00, 0.97, 67.30, 70.90, 25),
(85, 17, 'kauez', 'Kauê Kaschuk', 'BR', 0.95, 0.92, 62.10, 69.70, 25);