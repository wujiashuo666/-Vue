import {getRequest} from "./api";
import {matchMenu, matchRoutes} from "./matchRoutes";

//Vite 用 import.meta.glob 实现按需加载
const modules = import.meta.glob('../views/**/*.vue')

const resolveView = (component) => {
    if (component.startsWith("Home")) {
        return `../views/${component}.vue`;
    } else if (component.startsWith("Emp")) {
        return `../views/emp/${component}.vue`;
    } else if (component.startsWith("Per")) {
        return `../views/per/${component}.vue`;
    } else if (component.startsWith("Sal")) {
        return `../views/sal/${component}.vue`;
    } else if (component.startsWith("Sta")) {
        return `../views/sta/${component}.vue`;
    } else if (component.startsWith("Sys")) {
        return `../views/sys/${component}.vue`;
    } else if (component.startsWith("Match")) {
        //赛事模块页面在 views/match 子目录下，缺这个分支会解析到 views/MatchList.vue 导致白屏
        return `../views/match/${component}.vue`;
    }
    return `../views/${component}.vue`;
}

export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return Promise.resolve();
    }
    //返回 Promise，供导航守卫等待动态路由注册完成后再放行
    return getRequest("/system/config/menu").then(data => {
        if (data) {
            let fmtRoutes = formatRoutes(data);
            //路由注册说明：/emp/** 等后端菜单路由由下面的 addRoute 动态注册；
            ///match/** 比赛中心路由与 hrinfo 一起在 router.js 中静态注册
            //（作为 Home 的子路由，页面带侧边栏/顶栏框架）。
            //此前曾在这里等 path='/' 的路由出现后把 matchRoutes 合并为其 children，
            //但 /system/config/menu 返回的顶级路由 path 全部是 /home，条件永远不成立，
            //导致 /match/* 路由从未注册、点击比赛中心菜单没有反应，故改为静态注册
            fmtRoutes.forEach(route => router.addRoute(route));
            //侧边栏展示用原始菜单数据（路由表中的 name 因唯一性要求可能被重命名）
            store.commit('initRoutes', [...data, matchMenu]);
        }
    })
}

export const formatRoutes = (routes) => {
    let fmRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            children
        } = router;
        if (children && children instanceof Array) {
            children = formatRoutes(children);
        }
        //Vue Router 5 要求路由 name 全局唯一，而菜单数据中存在同名菜单（如“员工资料”既是
        //一级菜单又是人事管理的子菜单），同名会让 addRoute 覆盖删除已有路由。
        //路由 name 改用 path+菜单名 拼接保证唯一，原菜单名存入 meta.menuName 供面包屑展示
        let fmRouter = {
            path: path,
            name: path + '_' + name,
            iconCls: iconCls,
            meta: Object.assign({}, meta, {menuName: name}),
            children: children,
            component: modules[resolveView(component)]
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
