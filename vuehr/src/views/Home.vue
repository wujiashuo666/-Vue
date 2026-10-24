<template>
    <div class="home-page">
        <el-container class="outer">
            <el-header class="homeHeader">
                <div class="brand">
                    <span class="brand-mark">CS</span>
                    <span class="title">职业选手管理系统</span>
                </div>
                <div class="header-right">
                    <el-button link class="bell-btn" size="default" @click="goChat">
                        <el-icon><Bell/></el-icon>
                    </el-button>
                    <el-dropdown class="userInfo" @command="commandHandler">
                        <span class="el-dropdown-link">
                            {{user.name}}<i><img :src="user.userface" alt=""></i>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
                                <el-dropdown-item command="setting">设置</el-dropdown-item>
                                <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container class="inner">
                <el-aside width="200px">
                    <el-menu router unique-opened>
                        <!--Vue 3 中 v-if 优先级高于 v-for，需要用 template 包裹拆开-->
                        <template v-for="(item,index) in routes" :key="index">
                            <el-sub-menu v-if="!item.hidden" :index="index+''">
                                <template #title>
                                    <i style="color: #ff4655;margin-right: 5px" :class="item.iconCls"></i>
                                    <span>{{item.name}}</span>
                                </template>
                                <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj">
                                    {{child.name}}
                                </el-menu-item>
                            </el-sub-menu>
                        </template>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb v-if="$route.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{$route.meta.menuName || $route.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        computed: {
            routes() {
                return this.$store.state.routes;
            },
            user() {
                return this.$store.state.currentHr;
            }
        },
        mounted() {
            //sessionStorage 里的 user 是登录时的快照，头像等资料变更后不会更新，
            //这里进入首页时从后端拉取最新资料，同步刷新 store 与 sessionStorage
            this.getRequest("/hr/info").then(resp => {
                if (resp) {
                    window.sessionStorage.setItem("user", JSON.stringify(resp));
                    this.$store.commit('INIT_CURRENTHR', resp);
                }
            });
        },
        methods: {
            goChat() {
                this.$router.push("/chat");
            },
            commandHandler(cmd) {
                if (cmd == 'logout') {
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest("/logout");
                        window.sessionStorage.removeItem("user")
                        this.$store.commit('initRoutes', []);
                        this.$router.replace("/");
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    });
                } else if (cmd == 'userinfo') {
                    this.$router.push('/hrinfo');
                }
            }
        }
    }
</script>

<style scoped>
    .home-page {
        height: 100vh;
        overflow: hidden;
        background: #0b0e14;
    }

    /* 背景图降透明度作底纹，避免与暗色卡片抢视觉 */
    .home-page::before {
        content: '';
        position: fixed;
        inset: 0;
        background: url('/home-bg.png') no-repeat center center fixed;
        background-size: cover;
        opacity: 0.16;
        pointer-events: none;
        z-index: 0;
    }

    .outer {
        position: relative;
        z-index: 1;
        height: 100%;
    }

    .inner {
        height: calc(100% - 60px);
        min-height: 0;
    }

    .homeHeader {
        height: 60px;
        background: #0f141b;
        border-bottom: 1px solid #232b36;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 18px;
        box-sizing: border-box;
    }

    .brand {
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .brand-mark {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 34px;
        height: 34px;
        border-radius: 9px;
        background: linear-gradient(135deg, #ff4655, #ff8a00);
        color: #fff;
        font-weight: 800;
        font-size: 14px;
        letter-spacing: 0.5px;
    }

    .homeHeader .title {
        font-size: 20px;
        font-weight: 700;
        color: #e6edf3;
        letter-spacing: 0.5px;
    }

    .header-right {
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .bell-btn {
        color: #8b949e !important;
        font-size: 18px;
    }
    .bell-btn:hover {
        color: #ff4655 !important;
    }

    .el-aside {
        background: rgba(16, 21, 28, 0.85) !important;
        border-right: 1px solid #232b36;
        backdrop-filter: blur(8px);
        overflow-y: auto;
        overflow-x: hidden;
    }

    /* 内容区统一滚动，路由视图不再设 min-height，避免出现双层滚动条 */
    .el-main {
        padding: 16px 18px;
        overflow-y: auto;
        min-height: 0;
    }

    .homeRouterView {
        margin-top: 10px;
        background: rgba(15, 20, 27, 0.72);
        backdrop-filter: blur(6px);
        border: 1px solid #232b36;
        border-radius: 12px;
        padding: 20px;
    }

    .homeHeader .userInfo {
        cursor: pointer;
    }

    .el-dropdown-link {
        display: flex;
        align-items: center;
        gap: 8px;
        color: #e6edf3;
        font-size: 14px;
    }

    .el-dropdown-link img {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid #2a323d;
    }
</style>
