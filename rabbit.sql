/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50548
Source Host           : localhost:3306
Source Database       : rabbit

Target Server Type    : MYSQL
Target Server Version : 50548
File Encoding         : 65001

Date: 2018-02-28 19:08:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '权限名称',
  `action` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '权限行为',
  `group` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '所在分组',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父节点id',
  `resource_type` enum('menu','button') COLLATE utf8_bin NOT NULL COMMENT '资源类型',
  `sort` bigint(20) NOT NULL DEFAULT '0' COMMENT '所在排序',
  `available` bit(1) DEFAULT b'1' COMMENT '是否可用',
  `created_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `action` (`action`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '系统列表', 'sys:list', '系统管理', '0', 'menu', '0', '', '2018-02-23 22:56:11', '2018-02-26 00:46:20');
INSERT INTO `t_permission` VALUES ('2', '系统添加', 'sys:add', '系统管理', '1', 'button', '0', '', '2018-02-23 22:58:35', '2018-02-23 22:58:37');
INSERT INTO `t_permission` VALUES ('3', '系统修改', 'sys:update', '系统管理', '1', 'button', '0', '', '2018-02-23 22:59:15', '2018-02-23 22:59:18');
INSERT INTO `t_permission` VALUES ('4', '系统删除', 'sys:delete', '系统管理', '1', 'button', '0', '', '2018-02-23 22:59:50', '2018-02-26 00:35:49');
INSERT INTO `t_permission` VALUES ('5', '用户列表', 'user:list', '用户管理', '1', 'menu', '1', '', '2018-02-26 00:37:02', '2018-02-26 00:37:02');
INSERT INTO `t_permission` VALUES ('6', '用户添加', 'user:add', '用户管理', '5', 'button', '1', '', '2018-02-26 00:37:31', '2018-02-26 00:37:31');
INSERT INTO `t_permission` VALUES ('7', '用户修改', 'user:update', '用户管理', '5', 'button', '1', '', '2018-02-26 00:38:03', '2018-02-26 00:38:03');
INSERT INTO `t_permission` VALUES ('8', '用户删除', 'user:delete', '用户管理', '5', 'button', '1', '', '2018-02-26 00:38:16', '2018-02-26 00:38:16');
INSERT INTO `t_permission` VALUES ('9', '角色列表', 'role:list', '角色管理', '1', 'menu', '2', '', '2018-02-26 00:39:01', '2018-02-26 00:39:01');
INSERT INTO `t_permission` VALUES ('10', '角色添加', 'role:add', '角色管理', '9', 'button', '2', '', '2018-02-26 00:39:25', '2018-02-26 00:39:25');
INSERT INTO `t_permission` VALUES ('11', '角色修改', 'role:update', '角色管理', '9', 'button', '2', '', '2018-02-26 00:39:49', '2018-02-26 00:39:49');
INSERT INTO `t_permission` VALUES ('12', '角色删除', 'role:delete', '角色管理', '9', 'button', '2', '', '2018-02-26 00:40:09', '2018-02-26 00:40:09');
INSERT INTO `t_permission` VALUES ('13', '角色权限分配', 'role:rolepermission', '角色管理', '9', 'button', '2', '', '2018-02-26 00:42:12', '2018-02-26 00:42:12');
INSERT INTO `t_permission` VALUES ('14', '角色用户列表', 'role:roleuser', '角色管理', '9', 'button', '2', '', '2018-02-26 00:42:40', '2018-02-26 00:42:40');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT b'1' COMMENT '是否可用',
  `name` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `priority` int(11) DEFAULT '99' COMMENT '角色优先级 - 优先级数值越低,代表优先级越大,角色normal的优先级为99',
  `created_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '', 'admin', '0', '2018-01-25 23:16:22', '2018-01-25 23:16:25');
INSERT INTO `t_role` VALUES ('2', '', 'manager', '1', '2018-01-25 23:16:59', '2018-01-25 23:17:02');
INSERT INTO `t_role` VALUES ('3', '', 'normal', '99', '2018-01-25 23:17:36', '2018-02-25 23:26:41');

-- ----------------------------
-- Table structure for `t_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `FKjobmrl6dorhlfite4u34hciik` (`permission_id`),
  KEY `FK90j038mnbnthgkc17mqnoilu9` (`role_id`),
  CONSTRAINT `FK90j038mnbnthgkc17mqnoilu9` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKjobmrl6dorhlfite4u34hciik` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('1', '1', '1', '2018-02-23 23:25:46', '2018-02-23 23:25:48');
INSERT INTO `t_role_permission` VALUES ('2', '1', '2', '2018-02-23 23:25:58', '2018-02-23 23:26:00');
INSERT INTO `t_role_permission` VALUES ('3', '1', '3', '2018-02-23 23:26:07', '2018-02-23 23:26:10');
INSERT INTO `t_role_permission` VALUES ('4', '1', '4', '2018-02-23 23:26:18', '2018-02-23 23:26:21');
INSERT INTO `t_role_permission` VALUES ('5', '1', '5', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('6', '1', '6', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('7', '1', '7', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('8', '1', '8', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('9', '1', '9', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('10', '1', '10', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('11', '1', '11', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('12', '1', '12', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('13', '1', '13', '2018-02-26 00:44:59', '2018-02-26 00:44:59');
INSERT INTO `t_role_permission` VALUES ('14', '1', '14', '2018-02-26 00:44:59', '2018-02-26 00:44:59');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_date` datetime DEFAULT NULL COMMENT '创建时间',
  `email` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '电子邮箱',
  `nickname` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '昵称',
  `password` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `status` int(11) DEFAULT '1' COMMENT '用户状态 - 1:正常,0:冻结,-1:删除',
  `username` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `phone` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '电话号码',
  `photo` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '头像照片',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '2018-01-25 21:23:49', '2018-02-28 15:14:18', 'beamofsoul@sina.com', 'Justin', 'Justin1987.', '1', 'beamofsoul', '13043211234', 'beamofsoul.jpeg');
INSERT INTO `t_user` VALUES ('12', '2018-02-01 20:14:33', '2018-02-24 19:49:22', 'tutou1@gmail.com', 'tutou1', '123123', '0', 'testuser1', '13200000000', 'testuser1.jpeg');

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_date` datetime DEFAULT NULL COMMENT '创建时间',
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa9c8iiy6ut0gnx491fqx4pxam` (`role_id`),
  KEY `FKq5un6x7ecoef5w1n39cop66kl` (`user_id`),
  CONSTRAINT `FKa9c8iiy6ut0gnx491fqx4pxam` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKq5un6x7ecoef5w1n39cop66kl` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '2018-02-01 16:39:25', '2018-02-01 16:39:27', '1', '1');

-- ----------------------------
-- View structure for `v_user_role_combine_role`
-- ----------------------------
DROP VIEW IF EXISTS `v_user_role_combine_role`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_user_role_combine_role` AS select `tu`.`id` AS `id`,`tu`.`id` AS `user_id`,`tu`.`username` AS `username`,`tu`.`nickname` AS `nickname`,ifnull(group_concat(`tur`.`role_id` order by `tur`.`role_id` ASC separator ','),0) AS `role_id`,ifnull(group_concat(`tr`.`name` order by `tr`.`name` ASC separator ','),'') AS `role_name` from ((`t_user` `tu` left join `t_user_role` `tur` on((`tur`.`user_id` = `tu`.`id`))) left join `t_role` `tr` on((`tr`.`id` = `tur`.`role_id`))) group by `tu`.`id` ;
