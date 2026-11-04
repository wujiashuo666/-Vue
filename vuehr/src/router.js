import {createRouter, createWebHistory} from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import HrInfo from './views/HrInfo.vue'
//比赛中心路由在 /home 下静态注册，见下方说明
import {matchRoutes} from './utils/matchRoutes'

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login,
        hidden: true
    }, {
        path: '/home',
        name: 'Home',
        component: Home,
        hidden: true,
        meta: {
            roles: ['admin', 'user']
        },
        children: [
            {
                path: '/hrinfo',
                name: '个人中心',
                component: HrInfo,
                hidden: true
            },
            //比赛中心页面与 hrinfo 一样作为 Home 的子路由静态注册。
            //此前在 menus.js 里等后端菜单返回 path='/' 的路由再合并，
            //但 /system/config/menu 实际返回的顶级路由 path 全是 /home，
            //合并条件永远不成立，导致 /match/* 路由从未注册，点菜单无反应
            ...matchRoutes
        ]
    }, {
        //Vue Router 4 之后通配符路由的写法
        path: '/:pathMatch(.*)*',
        redirect: '/home'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
