/**
 * 赛事中心演示数据
 * 后端赛事接口落地前由前端提供，接口就绪后替换为 getRequest('/match/...') 即可，
 * 页面组件通过本模块导出的查询函数取数，不直接依赖具体字段结构。
 */

import { getRequest } from '@/utils/api'

// 状态用枚举，避免用中文字符串硬比较
export const MATCH_STATUS = {
  LIVE: 'live',
  UPCOMING: 'upcoming',
  FINISHED: 'finished'
}

export const STATUS_META = {
  [MATCH_STATUS.LIVE]: { label: '进行中', color: '#ef4444' },
  [MATCH_STATUS.UPCOMING]: { label: '未开始', color: '#eab308' },
  [MATCH_STATUS.FINISHED]: { label: '已结束', color: '#22c55e' }
}

// CS2 现役图池
export const CS_MAPS = [
  'Mirage', 'Nuke', 'Inferno', 'Overpass',
  'Ancient', 'Vertigo', 'Anubis', 'Train', 'Dust2'
]

export const teams = [
  { id: 1, name: 'Vitality', short: 'VIT', region: '欧洲', worldRank: 1, color: ['#f7d51d', '#1f1f1f'] },
  { id: 2, name: 'MOUZ', short: 'MOUZ', region: '欧洲', worldRank: 2, color: ['#ff5c00', '#2b2b2b'] },
  { id: 3, name: 'Team Spirit', short: 'SPR', region: '独联体', worldRank: 3, color: ['#ffd400', '#121212'] },
  { id: 4, name: 'Aurora', short: 'AUR', region: '土耳其', worldRank: 8, color: ['#00d0ff', '#0b2b3a'] },
  { id: 5, name: 'FURIA', short: 'FUR', region: '美洲', worldRank: 9, color: ['#1ed760', '#10251a'] },
  { id: 6, name: 'Falcons', short: 'FLC', region: '中东', worldRank: 4, color: ['#4ade80', '#0f2b1f'] },
  { id: 7, name: 'Astralis', short: 'AST', region: '欧洲', worldRank: 11, color: ['#e02b2b', '#1a0d0d'] },
  { id: 8, name: 'G2 Esports', short: 'G2', region: '欧洲', worldRank: 5, color: ['#ff2d55', '#1a0a12'] },
  { id: 9, name: 'The MongolZ', short: 'MGLZ', region: '亚洲', worldRank: 6, color: ['#3b82f6', '#0b1c3a'] },
  { id: 10, name: 'NAVI', short: 'NAVI', region: '独联体', worldRank: 7, color: ['#ffee00', '#0f0f00'] },
  { id: 11, name: 'Team Liquid', short: 'TL', region: '美洲', worldRank: 12, color: ['#00a8e8', '#0a1f2b'] },
  { id: 12, name: 'Fnatic', short: 'FNC', region: '欧洲', worldRank: 14, color: ['#ff6500', '#2b1600'] },
  { id: 13, name: 'Legacy', short: 'LGC', region: '美洲', worldRank: 16, color: ['#a78bfa', '#1a1233'] },
  { id: 14, name: 'TYLOO', short: 'TYLOO', region: '亚洲', worldRank: 18, color: ['#ef4444', '#2b0b0b'] },
  { id: 15, name: 'Lynn Vision', short: 'LVG', region: '亚洲', worldRank: 21, color: ['#22d3ee', '#08272e'] },
  { id: 16, name: 'FaZe Clan', short: 'FAZE', region: '欧洲', worldRank: 10, color: ['#e11d48', '#2b0713'] },
  { id: 17, name: 'paiN', short: 'PNG', region: '美洲', worldRank: 20, color: ['#f59e0b', '#2b1f05'] }
]

/**
 * maps 为逐图比分；进行中的比赛最后一张图带 live:true，表示该图尚未打完。
 * 系列赛总比分由逐图结果推导，不再单独维护，避免出现"未开始却带比分"的矛盾数据。
 */
export const matches = [
  {
    id: 1, tournament: 'ESL Pro League S24', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-28T01:00:00', team1Id: 1, team2Id: 2,
    maps: [
      { name: 'Mirage', score1: 13, score2: 10 },
      { name: 'Nuke', score1: 8, score2: 13 },
      { name: 'Inferno', score1: 13, score2: 11 }
    ]
  },
  {
    id: 2, tournament: 'BLAST Premier', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-26T18:00:00', team1Id: 1, team2Id: 3,
    maps: [
      { name: 'Nuke', score1: 13, score2: 9 },
      { name: 'Ancient', score1: 10, score2: 13 },
      { name: 'Train', score1: 13, score2: 8 }
    ]
  },
  {
    id: 3, tournament: 'IEM Katowice', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-25T22:00:00', team1Id: 2, team2Id: 4,
    maps: [
      { name: 'Inferno', score1: 13, score2: 7 },
      { name: 'Overpass', score1: 13, score2: 9 }
    ]
  },
  {
    id: 4, tournament: 'ESL Pro League S24', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-24T20:00:00', team1Id: 5, team2Id: 6,
    maps: [
      { name: 'Overpass', score1: 9, score2: 13 },
      { name: 'Vertigo', score1: 11, score2: 13 }
    ]
  },
  {
    id: 5, tournament: 'BLAST Premier', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-23T19:00:00', team1Id: 10, team2Id: 8,
    maps: [
      { name: 'Ancient', score1: 13, score2: 11 },
      { name: 'Nuke', score1: 14, score2: 16 },
      { name: 'Dust2', score1: 13, score2: 10 }
    ]
  },
  {
    id: 6, tournament: 'IEM Katowice', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-22T20:00:00', team1Id: 3, team2Id: 7,
    maps: [
      { name: 'Mirage', score1: 13, score2: 5 },
      { name: 'Nuke', score1: 13, score2: 8 }
    ]
  },
  {
    id: 7, tournament: 'ESL Pro League S24', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-21T02:00:00', team1Id: 16, team2Id: 11,
    maps: [
      { name: 'Inferno', score1: 16, score2: 14 },
      { name: 'Ancient', score1: 13, score2: 7 }
    ]
  },
  {
    id: 8, tournament: 'Perfect World 亚洲区', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-20T14:00:00', team1Id: 14, team2Id: 15,
    maps: [
      { name: 'Mirage', score1: 13, score2: 8 },
      { name: 'Anubis', score1: 13, score2: 10 }
    ]
  },
  {
    id: 9, tournament: 'IEM Katowice', stage: '小组赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-19T17:00:00', team1Id: 9, team2Id: 17,
    maps: [
      { name: 'Nuke', score1: 13, score2: 11 },
      { name: 'Vertigo', score1: 13, score2: 9 }
    ]
  },
  {
    id: 10, tournament: 'BLAST Premier', stage: '决赛', bo: 5, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-18T21:00:00', team1Id: 1, team2Id: 6,
    maps: [
      { name: 'Mirage', score1: 13, score2: 7 },
      { name: 'Nuke', score1: 11, score2: 13 },
      { name: 'Inferno', score1: 13, score2: 9 },
      { name: 'Ancient', score1: 13, score2: 6 }
    ]
  },
  {
    id: 11, tournament: 'ESL Pro League S24', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-17T18:00:00', team1Id: 2, team2Id: 10,
    maps: [
      { name: 'Ancient', score1: 13, score2: 10 },
      { name: 'Mirage', score1: 10, score2: 13 },
      { name: 'Train', score1: 13, score2: 11 }
    ]
  },
  {
    id: 12, tournament: 'Perfect World 亚洲区', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.FINISHED,
    startTime: '2026-10-16T15:00:00', team1Id: 3, team2Id: 14,
    maps: [
      { name: 'Mirage', score1: 13, score2: 4 },
      { name: 'Inferno', score1: 13, score2: 6 }
    ]
  },
  {
    id: 13, tournament: 'ESL Pro League S24', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.LIVE,
    startTime: '2026-10-30T08:00:00', team1Id: 8, team2Id: 6,
    maps: [
      { name: 'Mirage', score1: 13, score2: 9 },
      { name: 'Nuke', score1: 7, score2: 5, live: true }
    ]
  },
  {
    id: 14, tournament: 'BLAST Premier', stage: '小组赛', bo: 3, status: MATCH_STATUS.LIVE,
    startTime: '2026-10-30T07:30:00', team1Id: 5, team2Id: 7,
    maps: [
      { name: 'Inferno', score1: 6, score2: 9, live: true }
    ]
  },
  {
    id: 15, tournament: 'BLAST Premier', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.UPCOMING,
    startTime: '2026-11-02T15:00:00', team1Id: 1, team2Id: 10, maps: []
  },
  {
    id: 16, tournament: 'ESL Pro League S24', stage: '淘汰赛', bo: 3, status: MATCH_STATUS.UPCOMING,
    startTime: '2026-11-05T20:00:00', team1Id: 2, team2Id: 6, maps: []
  },
  {
    id: 17, tournament: 'IEM Katowice', stage: '小组赛', bo: 3, status: MATCH_STATUS.UPCOMING,
    startTime: '2026-11-08T19:00:00', team1Id: 16, team2Id: 9, maps: []
  },
  {
    id: 18, tournament: 'IEM Katowice', stage: '小组赛', bo: 3, status: MATCH_STATUS.UPCOMING,
    startTime: '2026-11-11T16:00:00', team1Id: 3, team2Id: 2, maps: []
  },
  {
    id: 19, tournament: 'Perfect World 亚洲区', stage: '小组赛', bo: 3, status: MATCH_STATUS.UPCOMING,
    startTime: '2026-11-15T14:00:00', team1Id: 14, team2Id: 13, maps: []
  }
]

export const players = [
  { id: 1, teamId: 1, nick: 'ZywOo', name: 'Mathieu Herbaut', country: 'FR', rating: 1.34, kd: 1.38, adr: 88.4, kast: 76.2, maps: 42 },
  { id: 2, teamId: 1, nick: 'ropz', name: 'Robin Kool', country: 'EE', rating: 1.18, kd: 1.21, adr: 79.1, kast: 74.5, maps: 42 },
  { id: 3, teamId: 1, nick: 'flameZ', name: 'Shahar Shushan', country: 'IL', rating: 1.09, kd: 1.06, adr: 74.3, kast: 71.8, maps: 42 },
  { id: 4, teamId: 1, nick: 'mezii', name: 'William Merriman', country: 'GB', rating: 1.02, kd: 0.99, adr: 68.2, kast: 72.4, maps: 42 },
  { id: 5, teamId: 1, nick: 'apEX', name: 'Dan Madesclaire', country: 'FR', rating: 0.96, kd: 0.92, adr: 65.7, kast: 69.1, maps: 42 },

  { id: 6, teamId: 2, nick: 'torzsi', name: 'Ádám Torzsás', country: 'HU', rating: 1.22, kd: 1.24, adr: 81.6, kast: 73.9, maps: 39 },
  { id: 7, teamId: 2, nick: 'xertioN', name: 'Dorian Berman', country: 'IL', rating: 1.15, kd: 1.13, adr: 77.4, kast: 74.1, maps: 39 },
  { id: 8, teamId: 2, nick: 'Jimpphat', name: 'Jimi Salo', country: 'FI', rating: 1.12, kd: 1.11, adr: 75.8, kast: 72.6, maps: 39 },
  { id: 9, teamId: 2, nick: 'Brollan', name: 'Ludvig Brolin', country: 'SE', rating: 1.05, kd: 1.02, adr: 70.1, kast: 73.2, maps: 39 },
  { id: 10, teamId: 2, nick: 'siuhy', name: 'Kamil Szkaradek', country: 'PL', rating: 0.98, kd: 0.94, adr: 63.9, kast: 70.5, maps: 39 },

  { id: 11, teamId: 3, nick: 'donk', name: 'Danil Kryshkovets', country: 'RU', rating: 1.41, kd: 1.47, adr: 94.2, kast: 77.8, maps: 45 },
  { id: 12, teamId: 3, nick: 'sh1ro', name: 'Dmitry Sokolov', country: 'RU', rating: 1.19, kd: 1.28, adr: 72.6, kast: 75.3, maps: 45 },
  { id: 13, teamId: 3, nick: 'zont1x', name: 'Myroslav Plakhotia', country: 'UA', rating: 1.08, kd: 1.05, adr: 71.2, kast: 73.4, maps: 45 },
  { id: 14, teamId: 3, nick: 'chopper', name: 'Leonid Vishnyakov', country: 'RU', rating: 0.99, kd: 0.96, adr: 64.8, kast: 71.2, maps: 45 },
  { id: 15, teamId: 3, nick: 'magixx', name: 'Boris Vorobiev', country: 'RU', rating: 1.03, kd: 1.01, adr: 69.4, kast: 72.9, maps: 45 },

  { id: 16, teamId: 4, nick: 'XANTARES', name: 'Can Dörtkardeş', country: 'TR', rating: 1.21, kd: 1.22, adr: 82.3, kast: 73.1, maps: 36 },
  { id: 17, teamId: 4, nick: 'woxic', name: 'Özgür Eker', country: 'TR', rating: 1.11, kd: 1.14, adr: 70.9, kast: 71.5, maps: 36 },
  { id: 18, teamId: 4, nick: 'MAJ3R', name: 'Engin Küpeli', country: 'TR', rating: 1.00, kd: 0.97, adr: 66.2, kast: 70.8, maps: 36 },
  { id: 19, teamId: 4, nick: 'jottAAA', name: 'Ali Haydar Yılmaz', country: 'TR', rating: 1.06, kd: 1.04, adr: 73.5, kast: 72.2, maps: 36 },
  { id: 20, teamId: 4, nick: 'yiku', name: 'Yusuf Buğra Karadeniz', country: 'TR', rating: 0.95, kd: 0.91, adr: 62.7, kast: 69.6, maps: 36 },

  { id: 21, teamId: 5, nick: 'KSCERATO', name: 'Kaike Cerato', country: 'BR', rating: 1.20, kd: 1.25, adr: 80.1, kast: 74.6, maps: 34 },
  { id: 22, teamId: 5, nick: 'yuurih', name: 'Yuri Santos', country: 'BR', rating: 1.14, kd: 1.16, adr: 76.8, kast: 73.3, maps: 34 },
  { id: 23, teamId: 5, nick: 'molodoy', name: 'Danil Golubenko', country: 'KZ', rating: 1.17, kd: 1.19, adr: 78.4, kast: 72.9, maps: 34 },
  { id: 24, teamId: 5, nick: 'YEKINDAR', name: 'Mareks Gaļinskis', country: 'LV', rating: 1.04, kd: 1.00, adr: 71.6, kast: 70.4, maps: 34 },
  { id: 25, teamId: 5, nick: 'FalleN', name: 'Gabriel Toledo', country: 'BR', rating: 0.97, kd: 0.95, adr: 60.3, kast: 71.7, maps: 34 },

  { id: 26, teamId: 6, nick: 'NiKo', name: 'Nikola Kovač', country: 'BA', rating: 1.27, kd: 1.29, adr: 86.7, kast: 75.9, maps: 40 },
  { id: 27, teamId: 6, nick: 'm0NESY', name: 'Ilya Osipov', country: 'RU', rating: 1.31, kd: 1.35, adr: 83.2, kast: 76.4, maps: 40 },
  { id: 28, teamId: 6, nick: 'kyousuke', name: 'Maxim Lukin', country: 'RU', rating: 1.13, kd: 1.12, adr: 75.1, kast: 72.8, maps: 40 },
  { id: 29, teamId: 6, nick: 'TeSeS', name: 'René Madsen', country: 'DK', rating: 1.07, kd: 1.04, adr: 72.3, kast: 73.1, maps: 40 },
  { id: 30, teamId: 6, nick: 'Magisk', name: 'Emil Reif', country: 'DK', rating: 1.01, kd: 0.99, adr: 67.8, kast: 72.5, maps: 40 },

  { id: 31, teamId: 7, nick: 'device', name: 'Nicolai Reedtz', country: 'DK', rating: 1.16, kd: 1.20, adr: 78.9, kast: 74.2, maps: 31 },
  { id: 32, teamId: 7, nick: 'Staehr', name: 'Victor Staehr', country: 'DK', rating: 1.06, kd: 1.03, adr: 71.4, kast: 72.1, maps: 31 },
  { id: 33, teamId: 7, nick: 'jabbi', name: 'Jakob Nygaard', country: 'DK', rating: 1.02, kd: 0.98, adr: 69.6, kast: 71.3, maps: 31 },
  { id: 34, teamId: 7, nick: 'stavn', name: 'Martin Lund', country: 'DK', rating: 1.09, kd: 1.07, adr: 73.8, kast: 73.6, maps: 31 },
  { id: 35, teamId: 7, nick: 'Hooxi', name: 'Rasmus Nielsen', country: 'DK', rating: 0.92, kd: 0.88, adr: 58.4, kast: 69.2, maps: 31 },

  { id: 36, teamId: 8, nick: 'huNter-', name: 'Nemanja Kovač', country: 'BA', rating: 1.18, kd: 1.19, adr: 79.7, kast: 74.1, maps: 38 },
  { id: 37, teamId: 8, nick: 'malbsMd', name: 'Mario Samayoa', country: 'GT', rating: 1.15, kd: 1.14, adr: 77.2, kast: 72.7, maps: 38 },
  { id: 38, teamId: 8, nick: 'HeavyGod', name: 'Nikita Martynenko', country: 'RU', rating: 1.10, kd: 1.08, adr: 74.5, kast: 72.9, maps: 38 },
  { id: 39, teamId: 8, nick: 'SunPayus', name: 'Alvaro Garcia', country: 'ES', rating: 1.05, kd: 1.03, adr: 70.8, kast: 72.4, maps: 38 },
  { id: 40, teamId: 8, nick: 'MATYS', name: 'Matěj Šimko', country: 'SK', rating: 1.00, kd: 0.97, adr: 68.1, kast: 71.6, maps: 38 },

  { id: 41, teamId: 9, nick: '910', name: 'Usukhbayar Banzragch', country: 'MN', rating: 1.21, kd: 1.23, adr: 81.3, kast: 73.8, maps: 37 },
  { id: 42, teamId: 9, nick: 'bLitz', name: 'Garidmagnai Byambasuren', country: 'MN', rating: 1.12, kd: 1.10, adr: 74.9, kast: 72.5, maps: 37 },
  { id: 43, teamId: 9, nick: 'Techno', name: 'Sodbayar Munkhbold', country: 'MN', rating: 1.09, kd: 1.06, adr: 73.1, kast: 72.2, maps: 37 },
  { id: 44, teamId: 9, nick: 'Senzu', name: 'Tengis Batjargal', country: 'MN', rating: 1.07, kd: 1.05, adr: 71.7, kast: 71.9, maps: 37 },
  { id: 45, teamId: 9, nick: 'mzinho', name: 'Ayush Batbold', country: 'MN', rating: 1.03, kd: 1.00, adr: 69.2, kast: 72.1, maps: 37 },

  { id: 46, teamId: 10, nick: 'w0nderful', name: 'Ihor Zhdanov', country: 'UA', rating: 1.19, kd: 1.22, adr: 76.4, kast: 73.7, maps: 35 },
  { id: 47, teamId: 10, nick: 'b1t', name: 'Valeriy Vakhovskiy', country: 'UA', rating: 1.14, kd: 1.17, adr: 75.3, kast: 74.2, maps: 35 },
  { id: 48, teamId: 10, nick: 'iM', name: 'Mihai Ivan', country: 'RO', rating: 1.08, kd: 1.05, adr: 72.6, kast: 72.8, maps: 35 },
  { id: 49, teamId: 10, nick: 'jL', name: 'Justinas Lekavičius', country: 'LT', rating: 1.06, kd: 1.02, adr: 71.1, kast: 71.5, maps: 35 },
  { id: 50, teamId: 10, nick: 'Aleksib', name: 'Aleksi Virolainen', country: 'FI', rating: 0.96, kd: 0.93, adr: 62.5, kast: 70.9, maps: 35 },

  { id: 51, teamId: 11, nick: 'Twistzz', name: 'Russel Van Dulken', country: 'CA', rating: 1.17, kd: 1.19, adr: 78.6, kast: 74.4, maps: 30 },
  { id: 52, teamId: 11, nick: 'NAF', name: 'Keith Markovic', country: 'CA', rating: 1.11, kd: 1.13, adr: 74.2, kast: 73.1, maps: 30 },
  { id: 53, teamId: 11, nick: 'ulti', name: 'Ultimate Yilmaz', country: 'TR', rating: 1.09, kd: 1.07, adr: 73.4, kast: 72.3, maps: 30 },
  { id: 54, teamId: 11, nick: 'skullz', name: 'Felipe Reis', country: 'BR', rating: 1.02, kd: 0.99, adr: 68.9, kast: 71.4, maps: 30 },
  { id: 55, teamId: 11, nick: 'cadiaN', name: 'Casper Møller', country: 'DK', rating: 0.95, kd: 0.92, adr: 61.7, kast: 70.6, maps: 30 },

  { id: 56, teamId: 12, nick: 'blameF', name: 'Benjamin Bremer', country: 'DK', rating: 1.10, kd: 1.12, adr: 75.7, kast: 73.2, maps: 28 },
  { id: 57, teamId: 12, nick: 'KRIMZ', name: 'Freddy Johansson', country: 'SE', rating: 1.05, kd: 1.08, adr: 68.4, kast: 73.6, maps: 28 },
  { id: 58, teamId: 12, nick: 'bodyy', name: 'Alexandre Pianaro', country: 'FR', rating: 1.00, kd: 0.98, adr: 66.9, kast: 71.8, maps: 28 },
  { id: 59, teamId: 12, nick: 'matys', name: 'Tomáš Šimko', country: 'SK', rating: 1.03, kd: 1.01, adr: 70.2, kast: 71.2, maps: 28 },
  { id: 60, teamId: 12, nick: 'jambo', name: 'Danny Jørgensen', country: 'DK', rating: 0.97, kd: 0.94, adr: 63.6, kast: 70.1, maps: 28 },

  { id: 61, teamId: 13, nick: 'dumau', name: 'Eduardo Wolkmer', country: 'BR', rating: 1.12, kd: 1.14, adr: 76.1, kast: 72.9, maps: 26 },
  { id: 62, teamId: 13, nick: 'latto', name: 'Bruno Rebelatto', country: 'BR', rating: 1.08, kd: 1.06, adr: 72.8, kast: 72.1, maps: 26 },
  { id: 63, teamId: 13, nick: 'n1ssim', name: 'Vinicius Pereira', country: 'BR', rating: 1.05, kd: 1.03, adr: 71.3, kast: 71.7, maps: 26 },
  { id: 64, teamId: 13, nick: 'lux', name: 'Lucas Meneghini', country: 'BR', rating: 1.01, kd: 0.98, adr: 68.5, kast: 71.1, maps: 26 },
  { id: 65, teamId: 13, nick: 'saadzin', name: 'Guilherme Pacheco', country: 'BR', rating: 0.96, kd: 0.93, adr: 62.9, kast: 69.8, maps: 26 },

  { id: 66, teamId: 14, nick: 'JamYoung', name: 'Yang Yi', country: 'CN', rating: 1.16, kd: 1.18, adr: 78.3, kast: 73.5, maps: 29 },
  { id: 67, teamId: 14, nick: 'Mercy', name: 'Zhang Mingyuan', country: 'CN', rating: 1.06, kd: 1.04, adr: 71.9, kast: 72.2, maps: 29 },
  { id: 68, teamId: 14, nick: 'Moseyuh', name: 'Chen Qiyuan', country: 'CN', rating: 1.03, kd: 1.01, adr: 70.4, kast: 71.6, maps: 29 },
  { id: 69, teamId: 14, nick: 'Attacker', name: 'YuanZhang Sheng', country: 'CN', rating: 1.00, kd: 0.97, adr: 67.8, kast: 71.3, maps: 29 },
  { id: 70, teamId: 14, nick: 'Jee', name: 'Dong Jian', country: 'CN', rating: 0.94, kd: 0.91, adr: 61.2, kast: 69.4, maps: 29 },

  { id: 71, teamId: 15, nick: 'z4kr', name: 'Zhang Kai', country: 'CN', rating: 1.09, kd: 1.11, adr: 73.7, kast: 72.6, maps: 24 },
  { id: 72, teamId: 15, nick: 'Westmelon', name: 'Zhu Zihao', country: 'CN', rating: 1.04, kd: 1.02, adr: 70.6, kast: 71.9, maps: 24 },
  { id: 73, teamId: 15, nick: 'EmiliaQAQ', name: 'Tang Siyu', country: 'CN', rating: 1.01, kd: 0.99, adr: 69.1, kast: 71.2, maps: 24 },
  { id: 74, teamId: 15, nick: 'Starry', name: 'Li Xing', country: 'CN', rating: 0.98, kd: 0.95, adr: 65.3, kast: 70.5, maps: 24 },
  { id: 75, teamId: 15, nick: '3glory', name: 'Wang Wei', country: 'CN', rating: 0.93, kd: 0.90, adr: 60.8, kast: 68.9, maps: 24 },

  { id: 76, teamId: 16, nick: 'broky', name: 'Helvijs Saukants', country: 'LV', rating: 1.15, kd: 1.17, adr: 77.5, kast: 73.4, maps: 33 },
  { id: 77, teamId: 16, nick: 'frozen', name: 'David Čerňanský', country: 'SK', rating: 1.18, kd: 1.20, adr: 79.8, kast: 74.1, maps: 33 },
  { id: 78, teamId: 16, nick: 'rain', name: 'Håvard Nygaard', country: 'NO', rating: 1.07, kd: 1.05, adr: 72.4, kast: 72.7, maps: 33 },
  { id: 79, teamId: 16, nick: 'EliGE', name: 'Jonathan Jablonowski', country: 'US', rating: 1.10, kd: 1.08, adr: 74.6, kast: 72.9, maps: 33 },
  { id: 80, teamId: 16, nick: 'karrigan', name: 'Finn Andersen', country: 'DK', rating: 0.94, kd: 0.90, adr: 61.4, kast: 70.3, maps: 33 },

  { id: 81, teamId: 17, nick: 'dgt', name: 'Diego Pereira', country: 'BR', rating: 1.07, kd: 1.05, adr: 72.1, kast: 72.4, maps: 25 },
  { id: 82, teamId: 17, nick: 'biguzera', name: 'Rodrigo Bittencourt', country: 'BR', rating: 1.03, kd: 1.01, adr: 69.7, kast: 71.8, maps: 25 },
  { id: 83, teamId: 17, nick: 'dav1deuS', name: 'David Tapia', country: 'BR', rating: 1.05, kd: 1.04, adr: 71.2, kast: 71.5, maps: 25 },
  { id: 84, teamId: 17, nick: 'snow', name: 'João Vinicius', country: 'BR', rating: 1.00, kd: 0.97, adr: 67.3, kast: 70.9, maps: 25 },
  { id: 85, teamId: 17, nick: 'kauez', name: 'Kauê Kaschuk', country: 'BR', rating: 0.95, kd: 0.92, adr: 62.1, kast: 69.7, maps: 25 }
]

/* ---------- 查询工具 ---------- */

export const getTeam = (id) => teams.find(t => t.id === id) || { id, name: '未知战队', short: '?', color: ['#555', '#222'] }

/** 由逐图结果推导系列赛比分，未开始的比赛返回 0:0 */
export const seriesScore = (match) => {
  let s1 = 0
  let s2 = 0
  match.maps.forEach(m => {
    if (m.live) return
    if (m.score1 > m.score2) s1++
    else if (m.score2 > m.score1) s2++
  })
  return { team1Score: s1, team2Score: s2 }
}

/** 进行中比赛当前打的图与其比分 */
export const currentMap = (match) => match.maps.find(m => m.live) || null

export const tournaments = [...new Set(matches.map(m => m.tournament))]

/** 比赛用到的全部地图，用于筛选下拉 */
export const usedMaps = [...new Set(matches.flatMap(m => m.maps.map(x => x.name)))].sort()

/**
 * 战队战绩：从比赛数据实时推导，避免维护两份互相矛盾的数据
 */
export const teamStats = () => {
  const stat = teams.map(t => ({
    ...t,
    wins: 0, losses: 0, mapsWon: 0, mapsLost: 0, recent: []
  }))
  const mapById = new Map(stat.map(t => [t.id, t]))

  matches
    .filter(m => m.status === MATCH_STATUS.FINISHED)
    .sort((a, b) => new Date(a.startTime) - new Date(b.startTime))
    .forEach(m => {
      const t1 = mapById.get(m.team1Id)
      const t2 = mapById.get(m.team2Id)
      if (!t1 || !t2) return
      const { team1Score: s1, team2Score: s2 } = seriesScore(m)
      const win1 = s1 > s2
      if (win1) { t1.wins++; t2.losses++ } else { t2.wins++; t1.losses++ }
      m.maps.forEach(mp => {
        t1.mapsWon += mp.score1
        t1.mapsLost += mp.score2
        t2.mapsWon += mp.score2
        t2.mapsLost += mp.score1
      })
      t1.recent.push({ opponent: t2.name, result: win1 ? 'W' : 'L', score: `${s1}-${s2}` })
      t2.recent.push({ opponent: t1.name, result: win1 ? 'L' : 'W', score: `${s2}-${s1}` })
    })

  stat.forEach(t => {
    const total = t.wins + t.losses
    t.total = total
    t.winRate = total ? Math.round((t.wins / total) * 100) : 0
    t.mapDiff = t.mapsWon - t.mapsLost
    // 只保留最近 5 场
    t.recent = t.recent.slice(-5)
  })
  return stat.sort((a, b) => b.winRate - a.winRate || b.mapDiff - a.mapDiff)
}

/* ---------- 接入真实后端 ---------- */

/**
 * 从后端 /esports/* 拉取真实数据；后端不可用时回退到本文件静态演示数据。
 * 返回结构：{ teams, players, matches, tournaments, usedMaps, teamStats, fromApi }
 */
export async function fetchEsports() {
  try {
    const [teams, players, matches, tournaments, teamStats] = await Promise.all([
      getRequest('/esports/teams'),
      getRequest('/esports/players'),
      getRequest('/esports/matches'),
      getRequest('/esports/tournaments'),
      getRequest('/esports/team-stats')
    ])
    const usedMaps = [...new Set(matches.flatMap(m => (m.maps || []).map(x => x.name)))].sort()
    return { teams, players, matches, tournaments, usedMaps, teamStats, fromApi: true }
  } catch (e) {
    console.warn('[esports] 后端接口不可用，使用本地演示数据', e)
    const usedMaps = [...new Set(matches.flatMap(m => m.maps.map(x => x.name)))].sort()
    return { teams, players, matches, tournaments, usedMaps, teamStats: teamStats(), fromApi: false }
  }
}
