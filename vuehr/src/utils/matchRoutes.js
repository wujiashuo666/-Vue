// 用懒加载函数而非静态 import，避免与 menus.js 的 import.meta.glob 重复打包
const MatchList = () => import('../views/match/MatchList.vue')
const TeamStats = () => import('../views/match/TeamStats.vue')
const PlayerRating = () => import('../views/match/PlayerRating.vue')
const MatchManage = () => import('../views/match/MatchManage.vue')
const TournamentManage = () => import('../views/match/TournamentManage.vue')
const MatchControl = () => import('../views/match/MatchControl.vue')

/**
 * 赛事中心采用前端静态注册：菜单数据在 vhr.sql 的 menu 表中尚无对应记录，
 * 若走后端下发则用户永远看不到入口。这里在动态菜单加载完成后合并注册，
 * 后端赛事接口落地后改为读取 menu 表即可，对页面组件无影响。
 */

// 侧边栏展示用，结构与后端 menu 表下发的菜单保持一致
export const matchMenu = {
    path: '/matchcenter',
    name: '比赛中心',
    iconCls: 'fa fa-trophy',
    children: [
        { path: '/matchcenter/matches', name: '比赛列表' },
        { path: '/matchcenter/teams', name: '战队战绩' },
        { path: '/matchcenter/stats', name: '选手数据' },
        { path: '/matchcenter/tournaments', name: '赛事管理' },
        { path: '/matchcenter/manage', name: '比赛管理' },
        { path: '/matchcenter/control', name: '玩大手操纵比赛' }
    ]
}

// 路由记录，name 全局唯一；meta.menuName 供面包屑展示真实菜单名
export const matchRoutes = [
    {
        path: '/matchcenter/matches',
        name: 'match_list',
        meta: { menuName: '比赛列表' },
        component: MatchList
    },
    {
        path: '/matchcenter/teams',
        name: 'match_teams',
        meta: { menuName: '战队战绩' },
        component: TeamStats
    },
    {
        path: '/matchcenter/stats',
        name: 'match_players',
        meta: { menuName: '选手数据' },
        component: PlayerRating
    },
    {
        path: '/matchcenter/tournaments',
        name: 'match_tournaments',
        meta: { menuName: '赛事管理' },
        component: TournamentManage
    },
    {
        path: '/matchcenter/manage',
        name: 'match_manage',
        meta: { menuName: '比赛管理' },
        component: MatchManage
    },
    {
        path: '/matchcenter/control',
        name: 'match_control',
        meta: { menuName: '玩大手操纵比赛' },
        component: MatchControl
    }
]
