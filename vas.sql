/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : vas

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 26/04/2018 14:47:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for agencyinformation
-- ----------------------------
DROP database if exists `vas`;
create database `vas`;
use `vas`;
DROP TABLE IF EXISTS `agencyinformation`;
CREATE TABLE `agencyinformation`  (
  `agencyID` int(11) NOT NULL AUTO_INCREMENT,
  `agencyName` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agencyPasswd` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agentName` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agentTel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agentEmail` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`agencyID`) USING BTREE,
  UNIQUE INDEX `agencyName`(`agencyName`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for platforminformation
-- ----------------------------
DROP TABLE IF EXISTS `platforminformation`;
CREATE TABLE `platforminformation`  (
  `platformID` int(11) NOT NULL DEFAULT 1,
  `platformBalance` decimal(19, 2) NOT NULL DEFAULT 0.00,
  `liquidationID` int(11) NOT NULL DEFAULT 2,
  `liquidationBalance` decimal(19, 2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`platformID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of platforminformation
-- ----------------------------
INSERT INTO `platforminformation` VALUES (1, 0.00, 2, 0.00);

-- ----------------------------
-- Table structure for userinformation
-- ----------------------------
DROP TABLE IF EXISTS `userinformation`;
CREATE TABLE `userinformation`  (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userPasswd` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userRealName` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userTel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userEmail` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userIdentity` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agency` int(11) NOT NULL,
  `availableBalance` decimal(19, 2) NOT NULL DEFAULT 0.00,
  `frozenBalance` decimal(19, 2) NOT NULL DEFAULT 0.00,
  `ifFrozen` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`UserID`) USING BTREE,
  UNIQUE INDEX `userName`(`userName`) USING BTREE,
  UNIQUE INDEX `Account`(`userIdentity`, `agency`) USING BTREE,
  INDEX `agency`(`agency`) USING BTREE,
  CONSTRAINT `agency` FOREIGN KEY (`agency`) REFERENCES `agencyinformation` (`agencyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
