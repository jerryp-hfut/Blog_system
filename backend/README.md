# Spring Boot 博客管理系统后端

本项目为博客管理系统后端，基于 Spring Boot + Maven + MySQL。

## 主要功能
- 用户、博客、分类、标签、评论的基本 CRUD
- RESTful API 设计

## 运行前准备
1. 安装 JDK 8/11/17
2. 安装 Maven
3. 配置好 MySQL 数据库，并导入建表 SQL

## 启动方式
1. 修改 `application.properties` 数据库配置
2. 在项目根目录执行：
   ```powershell
   mvn spring-boot:run
   ```

---

如需前端对接，请使用本项目提供的 RESTful API。
