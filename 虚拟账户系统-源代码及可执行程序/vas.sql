/*
Navicat MySQL Data Transfer

Source Server         : this
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : vas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-07-27 15:45:23
*/
DROP DATABASE IF EXISTS `vas`;
CREATE DATABASE `vas` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE `vas`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for agencyinformation
-- ----------------------------
DROP TABLE IF EXISTS `agencyinformation`;
CREATE TABLE `agencyinformation` (
  `agencyID` int(11) NOT NULL AUTO_INCREMENT,
  `agencyName` varchar(128) NOT NULL,
  `agencyPasswd` char(64) NOT NULL,
  `agentName` varchar(128) NOT NULL,
  `agentTel` varchar(32) NOT NULL,
  `agentEmail` varchar(128) NOT NULL,
  PRIMARY KEY (`agencyID`) USING BTREE,
  UNIQUE KEY `agencyName` (`agencyName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of agencyinformation
-- ----------------------------

-- ----------------------------
-- Table structure for platforminformation
-- ----------------------------
DROP TABLE IF EXISTS `platforminformation`;
CREATE TABLE `platforminformation` (
  `platformID` int(11) NOT NULL DEFAULT '1',
  `platformBalance` decimal(19,2) NOT NULL DEFAULT '0.00',
  `liquidationID` int(11) NOT NULL DEFAULT '2',
  `liquidationBalance` decimal(19,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`platformID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of platforminformation
-- ----------------------------
INSERT INTO `platforminformation` VALUES ('1', '0.00', '2', '0.00');

-- ----------------------------
-- Table structure for transactioninformation
-- ----------------------------
DROP TABLE IF EXISTS `transactioninformation`;
CREATE TABLE `transactioninformation` (
  `transactionID` varchar(255) NOT NULL,
  `transactionType` int(11) NOT NULL,
  `transactionDate` datetime NOT NULL,
  `payerAgencyID` int(11) DEFAULT NULL,
  `payerUserID` int(11) DEFAULT NULL,
  `receiverAgencyID` int(11) DEFAULT NULL,
  `receiverUserID` int(11) DEFAULT NULL,
  `transactionMoney` decimal(19,2) NOT NULL,
  PRIMARY KEY (`transactionID`),
  KEY `FK_PayerAgencyID` (`payerAgencyID`),
  KEY `FK_PayerUserID` (`payerUserID`),
  KEY `FK_ReceiverAgencyID` (`receiverAgencyID`),
  KEY `FK_ReceiverUserID` (`receiverUserID`),
  CONSTRAINT `FK_PayerAgencyID` FOREIGN KEY (`payerAgencyID`) REFERENCES `agencyinformation` (`agencyID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PayerUserID` FOREIGN KEY (`payerUserID`) REFERENCES `userinformation` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ReceiverAgencyID` FOREIGN KEY (`receiverAgencyID`) REFERENCES `agencyinformation` (`agencyID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ReceiverUserID` FOREIGN KEY (`receiverUserID`) REFERENCES `userinformation` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transactioninformation
-- ----------------------------

-- ----------------------------
-- Table structure for userinformation
-- ----------------------------
DROP TABLE IF EXISTS `userinformation`;
CREATE TABLE `userinformation` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(128) NOT NULL,
  `userPasswd` char(64) NOT NULL,
  `userRealName` varchar(128) NOT NULL,
  `userTel` varchar(32) NOT NULL,
  `userEmail` varchar(128) NOT NULL,
  `userIdentity` varchar(64) NOT NULL,
  `agency` int(11) NOT NULL,
  `availableBalance` decimal(19,2) NOT NULL DEFAULT '0.00',
  `frozenBalance` decimal(19,2) NOT NULL DEFAULT '0.00',
  `ifFrozen` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserID`) USING BTREE,
  UNIQUE KEY `userName` (`userName`) USING BTREE,
  UNIQUE KEY `Account` (`userIdentity`,`agency`) USING BTREE,
  KEY `agency` (`agency`) USING BTREE,
  CONSTRAINT `agency` FOREIGN KEY (`agency`) REFERENCES `agencyinformation` (`agencyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=3;

-- ----------------------------
-- Records of userinformation
-- ----------------------------
