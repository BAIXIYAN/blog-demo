# 个人博客系统 (Boke)

基于 Spring Boot 3 开发的个人博客系统，支持文章发布审核、评论、点赞、收藏、消息通知等功能。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.5.6 | 应用框架 |
| Spring Security | 6 | 安全框架 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| MySQL | 8.0 | 数据库 |
| Thymeleaf | - | 模板引擎 |
| Lombok | - | 代码简化 |

## 功能特点

### 用户模块
- 用户注册与登录
- 角色权限控制（管理员 / 普通用户）
- 个人信息管理（头像、昵称、邮箱等）

### 文章模块
- 文章发布、编辑、删除
- 文章分类管理（技术分享、学习笔记、生活随笔、项目总结）
- 文章审核机制（待审核 → 已发布 / 已拒绝）
- 浏览量统计

### 互动功能
- 评论与回复（支持嵌套评论）
- 文章点赞
- 文章收藏

### 消息通知
- 点赞/收藏自动通知文章作者
- 已读/未读状态管理

### 后台管理
- 用户管理（查看、禁用）
- 文章审核
- 分类管理
- 评论管理
- 数据统计

## 项目结构

```
boke/
├── src/main/java/com/cg/
│   ├── config/          # 配置类
│   ├── controller/     # 控制器层
│   │   ├── admin/      # 后台管理
│   │   ├── front/      # 前台页面
│   │   └── user/       # 用户中心
│   ├── dto/            # 数据传输对象
│   ├── entity/         # 实体类
│   ├── mapper/         # 数据访问层
│   ├── security/       # 安全认证
│   ├── service/        # 业务逻辑层
│   └── util/           # 工具类
├── src/main/resources/
│   ├── templates/      # Thymeleaf 模板
│   │   ├── admin/      # 后台模板
│   │   ├── front/      # 前台模板
│   │   └── user/       # 用户模板
│   ├── application.yml # 应用配置
│   └── blog_db.sql     # 数据库脚本
└── pom.xml             # Maven 配置
```

## 数据库初始化

1. 创建数据库：

```sql
CREATE DATABASE blog_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 导入数据库脚本：

```bash
mysql -u root -p blog_db < src/main/resources/blog_db.sql
```

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 运行步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd boke
```

2. **修改数据库配置**

编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

3. **初始化数据库**
```bash
mysql -u root -p < src/main/resources/blog_db.sql
```

4. **编译项目**
```bash
mvn clean compile
```

5. **运行项目**
```bash
mvn spring-boot:run
```

6. **访问应用**
- 前台首页：http://localhost:8080/
- 用户登录：http://localhost:8080/user/login
- 管理后台：http://localhost:8080/admin/

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | 姚美妍 | （注册时设置） |

## 许可协议

MIT License
