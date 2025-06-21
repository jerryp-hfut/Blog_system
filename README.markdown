# 博客管理系统

前端基于 **Vue3 + Vite**，后端采用 **Spring Boot + MySQL**，支持局域网访问、权限分级、分类统计可视化、界面美化等多项实用功能。系统适合课程设计、个人博客、团队协作等多种场景。本项目是合肥工业大学2025年数据库课设（7940）。

---

## 项目亮点

- **注册/登录安全**：用户密码加盐哈希存储，保障账户安全。
- **权限分级**：支持管理员与普通用户分级，部分操作仅限管理员。
- **博客/分类/标签管理**：支持博客、分类、标签的增删改查。
- **分类统计可视化**：内置 ECharts 柱状图，直观展示分类下博客数量。
- **个人中心统计**：用户可查看个人博客分类分布。
- **界面美化**：输入框、下拉框、正文等统一深色字体，整体风格美观现代。
- **局域网访问**：前后端均支持 0.0.0.0 监听，便于多设备/同学访问体验。
- **必填项校验与友好提示**：前后端均有校验，错误提示清晰。

---

## 技术栈

- **前端**：Vue3, Vite, Axios, ECharts, CSS3
- **后端**：Spring Boot, Spring Data JPA, MySQL
- **其他**：Maven, npm, JWT（认证），可选 Spring Security

---

## 主要功能

- 用户注册/登录（加盐哈希加密）
- 博客发布、编辑、删除、封面上传
- 分类、标签管理及统计
- 分类下博客数量统计柱状图（ECharts）
- 个人中心博客分类统计
- 权限分级（管理员/普通用户）
- 评论、点赞（每用户每博客仅可点赞一次）
- 响应式美观界面，深色字体优化
- 局域网访问支持

---

## 系统架构

### 时序图

![时序图](teaser/uml1.png)

### 类图

![类图](teaser/uml2.png)

### 流程图

![流程图](teaser/uml3.png)

---

## 目录结构

```shell
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/example/blog/
│   │   ├── controller/         # REST 控制器
│   │   ├── entity/             # 数据库实体类
│   │   └── repository/         # JPA 数据访问层
│   ├── src/main/resources/     # 配置文件
│   ├── public/                 # 静态资源（封面、头像）
│   └── init_blog_tables.sql    # 数据库初始化/升级脚本
├── public/                     # 前端静态资源（背景图等）
├── src/                        # Vue3 前端源码
│   ├── views/                  # 页面组件（含 CategoryStats.vue）
│   ├── components/             # 公共组件
│   ├── api.js                  # Axios 封装
│   ├── router.js               # Vue 路由
│   └── style.css               # 全局样式
├── teaser/                     # UML 图存放目录
│   ├── uml1.png                # 时序图
│   ├── uml2.png                # 类图
│   └── uml3.png                # 流程图
├── vite.config.js              # Vite 配置文件
├── package.json                # 前端依赖
└── README.markdown             # 项目说明
```

---

## 快速开始

### 环境要求

- **前端**：Node.js 16+，npm 8+
- **后端**：Java 17，Maven 3.8+，MySQL 8.0+
- **其他**：Git

### 1. 克隆项目

```bash
git clone <repository-url>
cd Blog_system
```

### 2. 配置并启动后端

1. 配置 MySQL 数据库：
   - 创建数据库（如 `blog_db`）。
   - 在 `backend/src/main/resources/application.properties` 中更新数据库连接：

     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     ```

2. 初始化/升级数据库表：
   - 执行 `backend/init_blog_tables.sql` 文件（如已存在旧表，注意 user 表需有 salt 字段）。

3. 启动后端：

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   - 默认后端运行在 `http://localhost:8080`
   - **局域网访问**：确保 `application.properties` 配置 `server.address=0.0.0.0`，并开放 8080 端口。

### 3. 启动前端

1. 安装依赖：

   ```bash
   cd ../
   npm install
   ```

2. 启动开发服务器：

   ```bash
   npm run dev
   ```

   - 默认前端运行在 `http://localhost:5173`
   - **局域网访问**：如需让其他设备访问，编辑 `vite.config.js`，设置 `host: '0.0.0.0'` 并开放 5173 端口。

### 4. 体验系统

- **管理员账号**
- **普通用户**：注册新账号即为普通用户。
- **权限说明**：
  - 未登录用户可浏览博客。
  - 登录用户可发布博客、点赞、评论、管理分类/标签/博客。
  - 管理员可管理所有内容。

---

## 常见问题

- **图片不显示（封面/头像/背景）**：确保图片放在 `public/` 或 `backend/public/` 目录下，检查路径是否正确。
- **数据库升级后无法登录**：请确认 user 表已添加 salt 字段，且注册/登录接口已升级。
- **局域网无法访问**：
  - 检查前后端监听地址是否为 0.0.0.0。
  - 检查防火墙端口（5173/8080）是否已开放。
  - 局域网设备需与主机同一网段。
- **数据库外键约束错误**：删除博客前，先移除关联的评论和标签。
- **端口冲突**：
  - 前端：修改 `vite.config.js` 中的 `server.port`。
  - 后端：修改 `application.properties` 中的 `server.port`。
- **CORS 问题**：确保后端已配置允许 `http://localhost:5173` 的跨域请求。

---

## 贡献指南

欢迎为项目贡献代码！请遵循以下步骤：

1. Fork 本仓库。
2. 创建你的功能分支（`git checkout -b feature/your-feature`）。
3. 提交更改（`git commit -m 'Add your feature'`）。
4. 推送到远程（`git push origin feature/your-feature`）。
5. 创建 Pull Request。

---

## 许可证

[MIT License](LICENSE)
