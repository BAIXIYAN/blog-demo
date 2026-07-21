/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : blog_db

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 01/01/2026 15:33:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容',
  `summary` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0-待审核, 1-已发布, 2-已拒绝',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间（审核通过时设置）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_publish_time`(`publish_time` ASC) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `article_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 'Spring Boot入门教程', 'Spring Boot是一个快速开发框架...', 'Spring Boot基础知识介绍', NULL, 1, 1, 1, 163, '2025-12-25 18:25:30', '2025-12-25 18:25:30', NULL);
INSERT INTO `article` VALUES (2, 'MySQL数据库优化技巧', '数据库性能优化是系统开发的重要环节...', '分享MySQL数据库优化经验', NULL, 1, 1, 1, 89, '2025-12-25 18:25:30', '2025-12-25 18:25:30', NULL);
INSERT INTO `article` VALUES (3, '大学生活感悟', '大学四年转瞬即逝，珍惜每一刻...', '分享大学生活中的点滴感悟', NULL, 1, 3, 1, 45, '2025-12-25 18:25:30', '2025-12-25 18:25:30', NULL);
INSERT INTO `article` VALUES (5, 'spring boot', 'spring boot', '', '', 2, 2, 1, 5, '2025-12-25 22:42:27', '2025-12-25 23:14:53', '2025-12-25 23:14:53');
INSERT INTO `article` VALUES (13, '深入理解Spring Boot自动配置', 'Spring Boot的自动配置机制是其核心特性之一...', '解析Spring Boot自动配置原理与实现', NULL, 1, 1, 1, 121, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:00:00');
INSERT INTO `article` VALUES (14, 'MySQL索引优化实践', '索引是提高数据库查询性能的关键...', '分享MySQL索引设计与优化经验', NULL, 1, 1, 1, 95, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:05:00');
INSERT INTO `article` VALUES (15, '我的校园生活日记', '记录在重庆工程学院的学习与生活...', '校园生活中的点滴记录', NULL, 2, 3, 1, 51, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:10:00');
INSERT INTO `article` VALUES (16, 'Java多线程编程指南', '多线程编程是Java中一个重要且复杂的主题...', 'Java多线程基础与高级技巧', NULL, 1, 2, 1, 200, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:15:00');
INSERT INTO `article` VALUES (17, 'Vue.js前端框架入门', 'Vue.js是一套用于构建用户界面的渐进式框架...', 'Vue.js基础教程', NULL, 1, 1, 1, 150, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:20:00');
INSERT INTO `article` VALUES (18, '毕业设计经验分享', '完成毕业设计过程中的心得体会...', '毕业项目开发与总结', NULL, 2, 4, 1, 80, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:25:00');
INSERT INTO `article` VALUES (19, '微服务架构设计思考', '微服务已成为现代分布式系统的主流架构...', '微服务设计原则与实践', NULL, 1, 1, 1, 181, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:30:00');
INSERT INTO `article` VALUES (20, '校园风景摄影分享', '记录重庆工程学院校园的美丽瞬间...', '校园摄影作品分享', NULL, 2, 3, 1, 60, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:35:00');
INSERT INTO `article` VALUES (21, 'Docker容器化部署实践', 'Docker简化了应用程序的部署与管理...', 'Docker基础与实战', NULL, 1, 2, 1, 133, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:40:00');
INSERT INTO `article` VALUES (22, '团队协作工具使用心得', '介绍在项目开发中常用的协作工具...', '提升团队协作效率的工具推荐', NULL, 1, 4, 1, 84, '2025-12-26 10:47:37', '2025-12-26 10:47:37', '2025-12-26 11:45:00');
INSERT INTO `article` VALUES (23, '123', '231', '', '', 2, 1, 1, 7, '2025-12-29 19:52:36', '2025-12-29 19:54:58', '2025-12-29 19:54:58');

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort` int NULL DEFAULT 0 COMMENT '排序（数字越小越靠前）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_category
-- ----------------------------
INSERT INTO `article_category` VALUES (1, '技术分享', '编程技术、开发经验分享', 1, '2025-12-25 18:25:21');
INSERT INTO `article_category` VALUES (2, '学习笔记', '学习过程中的心得体会', 2, '2025-12-25 18:25:21');
INSERT INTO `article_category` VALUES (3, '生活随笔', '日常生活感悟', 3, '2025-12-25 18:25:21');
INSERT INTO `article_category` VALUES (4, '项目总结', '项目开发经验总结', 4, '2025-12-25 18:25:21');

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父评论ID（0表示顶级评论）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_comment
-- ----------------------------
INSERT INTO `article_comment` VALUES (1, '这篇文章写得太好了！', 1, 1, 0, '2025-12-25 18:25:36');
INSERT INTO `article_comment` VALUES (2, '学到了很多新知识，谢谢分享！', 1, 1, 0, '2025-12-25 18:25:36');
INSERT INTO `article_comment` VALUES (3, '期待更多技术分享！', 2, 1, 0, '2025-12-25 18:25:36');
INSERT INTO `article_comment` VALUES (10, '写得非常好！', 22, 2, 0, '2025-12-26 10:55:44');
INSERT INTO `article_comment` VALUES (15, '非常详细的教程，对我帮助很大！', 1, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (16, '请问第3章的例子可以再详细解释一下吗？', 1, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (17, '这些优化技巧很实用，已收藏！', 2, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (18, '作者能不能分享一些索引失效的场景？', 2, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (19, '同感，大学时光真的很宝贵！', 3, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (20, '想起了我的大学生活，感谢分享！', 3, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (21, '文章内容可以再丰富一些', 5, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (22, '是的，第3章的例子确实有点难理解', 1, 1, 2, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (23, '我会在下一篇文章中详细说明这个问题', 1, 2, 2, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (24, '写得太好了，收获满满！', 13, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (25, '索引优化这块讲得很透彻', 14, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (26, '照片拍得真美！', 18, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (27, 'Docker部署确实很方便', 19, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (28, '同问，期待作者解答', 2, 1, 4, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (29, '已更新文章补充相关内容', 2, 1, 4, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (30, '这篇文章涵盖了Spring Boot自动配置的核心要点，从@EnableAutoConfiguration到自动配置类的加载机制都讲得很清楚。特别是对于条件注解的解释，让我对Spring Boot的\"约定优于配置\"理念有了更深的理解。希望作者能继续分享更多Spring Boot的深度内容！', 13, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (31, '谢谢分享！', 15, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (32, '学习了！', 16, 1, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (33, '微服务架构中服务发现有什么推荐方案？', 17, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (34, '团队协作除了Git还有什么好工具推荐？', 20, 2, 0, '2025-12-26 11:53:15');
INSERT INTO `article_comment` VALUES (35, '123', 23, 1, 0, '2025-12-29 19:56:23');
INSERT INTO `article_comment` VALUES (36, '132', 23, 1, 35, '2025-12-29 19:56:26');
INSERT INTO `article_comment` VALUES (37, '1312344', 23, 1, 36, '2025-12-29 19:56:30');

-- ----------------------------
-- Table structure for article_favorite
-- ----------------------------
DROP TABLE IF EXISTS `article_favorite`;
CREATE TABLE `article_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_user`(`article_id` ASC, `user_id` ASC) USING BTREE COMMENT '同一用户对同一文章只能收藏一次',
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_favorite_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_favorite
-- ----------------------------
INSERT INTO `article_favorite` VALUES (3, 1, 2, '2025-12-26 10:41:57');
INSERT INTO `article_favorite` VALUES (5, 22, 2, '2025-12-29 22:40:56');

-- ----------------------------
-- Table structure for article_like
-- ----------------------------
DROP TABLE IF EXISTS `article_like`;
CREATE TABLE `article_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_user`(`article_id` ASC, `user_id` ASC) USING BTREE COMMENT '同一用户对同一文章只能点赞一次',
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_like_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_like
-- ----------------------------
INSERT INTO `article_like` VALUES (2, 22, 1, '2025-12-26 12:34:20');
INSERT INTO `article_like` VALUES (3, 22, 2, '2025-12-29 19:53:03');
INSERT INTO `article_like` VALUES (4, 21, 2, '2025-12-29 22:41:03');

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID（文章作者）',
  `trigger_user_id` bigint NOT NULL COMMENT '触发用户ID（点赞/收藏的用户）',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型：like-点赞, favorite-收藏',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知内容',
  `is_read` tinyint NULL DEFAULT 0 COMMENT '是否已读：0-未读, 1-已读',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `read_time` datetime NULL DEFAULT NULL COMMENT '读取时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_trigger_user_id`(`trigger_user_id` ASC) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_is_read`(`is_read` ASC) USING BTREE,
  CONSTRAINT `fk_notification_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_notification_trigger_user` FOREIGN KEY (`trigger_user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息通知表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 1, 2, 22, 'favorite', '姚美妍 收藏了您的文章《团队协作工具使用心得》', 1, '2025-12-29 22:40:56', '2025-12-29 22:41:56');
INSERT INTO `notification` VALUES (2, 1, 2, 21, 'like', '姚美妍 点赞了您的文章《Docker容器化部署实践》', 1, '2025-12-29 22:41:03', '2025-12-29 22:42:00');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（登录用）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密存储）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'user' COMMENT '角色：admin-管理员, user-普通用户',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用, 1-正常',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$rx999b48QtGUvv/3wpSRSeUdClxEnxr7OYc3j3lXose3YmI7jTR5e', 'admin@cqie.edu.cn', '系统管理员', NULL, 'admin', 1, '2025-12-25 18:24:40', '2025-12-25 23:03:24');
INSERT INTO `sys_user` VALUES (2, '姚美妍', '$2a$10$XtPYKAlb6mtGu3noB/hTAuogen51vWXVbCt25N0ITKJmgf3nlfagq', '', '姚美妍', '/uploads/avatars/2_1766734952932_6149e129bcb646538887e15baaf21e16.jpg', 'user', 1, '2025-12-25 22:31:37', '2025-12-29 19:55:45');

-- ----------------------------
-- Table structure for password_reset_token
-- ----------------------------
DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `token` varchar(100) NOT NULL COMMENT '重置令牌',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_reset_token_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='密码重置令牌表';

SET FOREIGN_KEY_CHECKS = 1;
