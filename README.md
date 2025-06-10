# 博客管理系统（Vue3 + Vite + Spring Boot + MySQL）

本项目是一个全栈博客管理系统，前端基于 Vue3 + Vite，后端基于 Spring Boot，数据库为 MySQL。

## 功能特性
- 用户注册、登录、个人中心、密码重置
- 博客发布、编辑、删除、封面上传
- 分类、标签管理（管理员专属）
- 博客多标签支持，支持自定义标签
- 博客点赞（每用户每博客仅可点赞一次）
- 评论功能（可禁用点赞）
- 权限区分：系统管理员/普通用户
- 支持个性化推荐（可扩展）
- 响应式美观 UI，支持背景图片与半透明内容卡片

## 目录结构
```
├── backend/         # Spring Boot 后端
│   ├── src/main/java/com/example/blog/
│   │   ├── controller/   # 控制器
│   │   ├── entity/       # 实体类
│   │   └── repository/   # JPA仓库
│   ├── resources/        # 配置文件
│   └── public/           # 静态资源（封面、头像）
├── public/          # 前端静态资源（背景图等）
├── src/             # 前端 Vue3 源码
│   ├── views/       # 页面组件
│   ├── components/  # 公共组件
│   ├── api.js       # axios 封装
│   ├── router.js    # 路由
│   └── style.css    # 全局样式
├── vite.config.js   # Vite 配置
├── package.json     # 前端依赖
└── README.md        # 项目说明
```

## 快速开始
### 1. 后端启动
- 配置 `backend/src/main/resources/application.properties` 数据库信息
- 初始化数据库（可用 `init_blog_tables.sql`）
- 进入 backend 目录，运行：
  ```powershell
  mvn spring-boot:run
  ```

### 2. 前端启动
- 安装依赖：
  ```powershell
  npm install
  ```
- 启动开发服务器：
  ```powershell
  npm run dev
  ```
- 访问 [http://localhost:5173](http://localhost:5173)

### 3. 体验
- 管理员账号：用户名“潘序”自动为管理员
- 普通用户注册即为普通用户
- 未登录仅可浏览博客，登录后可发布、点赞、评论

## 常见问题
- **封面/头像/背景图片不显示**：请确保图片放在 public 目录下，路径正确
- **数据库外键约束删除失败**：请先删除关联的评论、标签等
- **端口冲突**：可在 vite.config.js 或 application.properties 修改端口

## 许可证
MIT
