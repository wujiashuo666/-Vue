# 职业选手管理系统 (CS:GO Esports Management System)

一个基于 **Spring Boot + Vue 3** 前后端分离的 CS:GO 电子竞技职业选手与赛事管理系统。系统在通用人事管理平台的基础上，扩展了战队、选手、赛事、比赛等电竞核心业务模块，支持赛事全流程的数字化管理。

## ✨ 核心功能

### 电竞业务模块
- **战队管理 (Teams)**：战队信息的增删改查与数据维护
- **选手管理 (Players)**：职业选手档案管理，支持批量导入导出
- **赛事管理 (Tournaments)**：赛事创建、编辑与赛程编排
- **比赛管理 (Matches)**：比赛记录管理，支持多地图（Map）明细
- **赛事进度控制 (Tournament Progress)**：赛事进行状态与进度的实时控制
- **数据统计 (Team Stats)**：战队战绩等数据的统计分析

### 基础人事模块
- 员工信息管理、部门管理、职位与职称管理
- 薪资管理、调薪记录、绩效奖惩（EC）
- 员工培训、员工调动、离职管理
- 权限管理（用户 / 角色 / 菜单三级 RBAC）
- 系统操作日志、统计看板

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.1.2 (Java 25) |
| 持久层 | MyBatis + MyBatis-Plus |
| 数据库 | MySQL 8.0（Flyway 数据库迁移） |
| 安全认证 | Spring Security（JWT） |
| 缓存 / 消息 | Redis / RabbitMQ |
| 文件存储 | FastDFS |
| 前端框架 | Vue 3.5 + Vue Router + Vuex |
| UI 组件库 | Element Plus |
| 构建工具 | Vite 8 |

## 📁 项目结构

```
vhr
├── vhrserver              # 后端服务（多模块 Maven 工程）
│   ├── vhr-web            # Web 层：Controller、配置、静态资源
│   ├── vhr-service        # 业务逻辑层
│   ├── vhr-mapper         # 数据访问层（MyBatis Mapper）
│   └── vhr-model          # 实体模型层
├── mailserver             # 邮件服务模块（RabbitMQ 消费）
├── vuehr                  # 前端项目（Vue 3 + Vite）
├── vhr.sql                # 数据库初始化脚本
└── 启动网站.bat            # Windows 一键启动脚本
```

## 🚀 快速开始

### 环境要求

- JDK 25
- Maven 3.10+
- Node.js 18+（推荐 20+）
- MySQL 8.0
- Redis 6+
- RabbitMQ 3.x

### 1. 初始化数据库

```bash
# 创建数据库并导入表结构与数据
mysql -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS vhr DEFAULT CHARACTER SET utf8mb4;"
mysql -uroot -p123456 vhr < vhr.sql
```

> 数据库默认配置见 `vhr/vhrserver/vhr-web/src/main/resources/application.yml`，可根据本机环境修改账号、密码及连接地址。

### 2. 启动后端

```bash
cd vhr
mvn clean package -DskipTests
java -jar vhrserver/vhr-web/target/vhr-web-*.jar --server.port=8081
```

> 后端默认端口 `8081`，可通过 `--server.port` 覆盖。

### 3. 启动前端

```bash
cd vuehr
npm install
npm run dev
```

前端开发服务器启动后，默认代理到后端接口，访问 http://localhost:8081。

### 4. 一键启动（Windows）

双击项目根目录的 `启动网站.bat`，脚本会自动完成后端启动与内网穿透。

### 5. 登录系统

默认账号：`admin`，密码：`123`（可在数据库中修改）。

## 📦 生产部署

前端打包后，将静态资源放入后端，由后端统一对外提供服务：

```bash
cd vuehr
npm run build
# 将 dist 目录内容拷贝至 vhr-web/src/main/resources/static 后重新打包后端
```

后端 `8081` 端口同时提供 API 与静态页面服务，并已配置 SPA History 路由回退，刷新页面不会 404。

## 📄 数据库表说明

- `es_team` / `es_player` / `es_tournament` / `es_match` / `es_match_map`：电竞核心业务表
- `es_tournament_progress`：赛事进度控制表
- `department` / `employee` / `salary` 等：通用人事模块表

## 🤝 贡献

欢迎提交 Issue 与 Pull Request。

## 📝 许可证

本项目仅用于学习与交流。
