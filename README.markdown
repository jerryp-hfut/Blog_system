# 博客管理系统

欢迎体验这个全栈博客管理系统！前端基于 **Vue3** 和 **Vite**，打造响应式且美观的界面；后端采用 **Spring Boot**，搭配 **MySQL** 数据库，提供稳定高效的服务。无论是发布博客、点赞评论，还是管理分类标签，这个系统都能为你带来流畅的创作与管理体验。

---

## 项目亮点

这个博客管理系统不仅仅是一个简单的 CRUD 应用，它融合了现代 Web 开发的精华。用户可以注册登录，管理个人资料，甚至重置密码。博客支持发布、编辑、删除，还能上传封面图片，添加多标签和自定义分类。管理员拥有专属权限，轻松管理全局内容。点赞和评论功能让互动更生动，每篇博客每用户只能点赞一次，确保公平性。界面采用半透明卡片设计，背景图片可自定义，响应式布局适配各种设备。更有个性化推荐功能，为未来扩展铺平道路。

---

## 技术栈

- **前端**：Vue3, Vite, Axios, CSS3
- **后端**：Spring Boot, Spring Data JPA, MySQL
- **其他**：Maven, npm, JWT（认证）

---

## 系统架构

以下是系统的核心设计图，帮助你快速理解项目结构和工作流程：

### 时序图

![时序图](teaser/uml1.png)

### 类图

![类图](teaser/uml2.png)

### 流程图

![流程图](teaser/uml3.png)

---

## 目录结构

```
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/example/blog/
│   │   ├── controller/         # REST 控制器
│   │   ├── entity/             # 数据库实体类
│   │   └── repository/         # JPA 数据访问层
│   ├── src/main/resources/     # 配置文件
│   └── public/                 # 静态资源（封面、头像）
├── public/                     # 前端静态资源（背景图等）
├── src/                        # Vue3 前端源码
│   ├── views/                  # 页面组件
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
└── README.md                   # 项目说明
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
cd blog-system
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
2. 初始化数据库表：
   - 执行 `backend/init_blog_tables.sql` 文件。
3. 启动后端：
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   默认后端运行在 `http://localhost:8080`。

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
3. 访问 [http://localhost:5173](http://localhost:5173)。

### 4. 体验系统

- **管理员账号**：使用用户名“潘序”登录（默认管理员）。
- **普通用户**：注册新账号即为普通用户。
- **权限说明**：
  - 未登录用户可浏览博客。
  - 登录用户可发布博客、点赞、评论。
  - 管理员可管理分类、标签及所有博客。

---

## 常见问题

- **图片不显示（封面/头像/背景）**：确保图片放在 `public/` 或 `backend/public/` 目录下，检查路径是否正确。
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
